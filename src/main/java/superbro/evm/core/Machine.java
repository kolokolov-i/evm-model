package superbro.evm.core;

import com.google.gson.*;
import superbro.evm.core.cpu.MachineThread;
import superbro.evm.core.device.Empty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Machine {

    public Device[] devices;
    private boolean[] devAutostart;
    public Memory.Byte
            memoryData, memoryStack;
    public Memory.Word
            memoryBIOS, memoryCode;
    private String modelBIOS;
    public CPU cpu;
    private MachineThread machineThread;

    private static Memory.Word stdBIOS;

    public static void setStdBIOS(Memory.Word mem) {
        stdBIOS = mem;
    }

    public static Machine loadFrom(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path.resolve("machine.json"));
        Machine machine = gson.fromJson(reader, Machine.class);
        if (machine.modelBIOS.equals("std")) {
            machine.memoryBIOS = stdBIOS.clone();
        } else {
            Path fmBios = path.resolve("bios.mem");
            machine.memoryBIOS = new Memory.Word(Files.newInputStream(fmBios));
        }
        Path fmCode = path.resolve("code.mem");
        if (Files.exists(fmCode)) {
            machine.memoryCode = new Memory.Word(Files.newInputStream(fmCode));
        } else {
            machine.memoryCode = new Memory.Word();
        }
        Path fmData = path.resolve("data.mem");
        if (Files.exists(fmCode)) {
            machine.memoryData = new Memory.Byte(Files.newInputStream(fmData));
        } else {
            machine.memoryData = new Memory.Byte();
        }
        Path fmStack = path.resolve("stack.mem");
        if (Files.exists(fmCode)) {
            machine.memoryStack = new Memory.Byte(Files.newInputStream(fmStack));
        } else {
            machine.memoryStack = new Memory.Byte();
        }
        return machine;
    }

    public static void saveTo(Machine machine, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path.resolve("machine.json"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        gson.toJson(machine, writer);
        writer.close();
        OutputStream out;
        out = Files.newOutputStream(path.resolve("code.mem"), StandardOpenOption.CREATE);
        machine.memoryCode.write(out);
        out.close();
        out = Files.newOutputStream(path.resolve("data.mem"), StandardOpenOption.CREATE);
        machine.memoryData.write(out);
        out.close();
        out = Files.newOutputStream(path.resolve("stack.mem"), StandardOpenOption.CREATE);
        machine.memoryStack.write(out);
        out.close();
    }

    public static Machine getStandardInstance() {
        Machine result = new Machine();
        result.modelBIOS = "std";
        result.memoryBIOS = stdBIOS;
        result.memoryCode = new Memory.Word();
        result.memoryData = new Memory.Byte();
        result.memoryStack = new Memory.Byte();
        return result;
    }

    public Machine() {
        devices = new Device[8];
        devAutostart = new boolean[8];
        for (int i = 0; i < 8; i++) {
            devices[i] = new Empty();
            devAutostart[i] = false;
        }
        this.cpu = new CPU(this);
    }

    public void step() {
        short command = memoryCode.data[cpu.PC.value];
        cpu.execute(command);
    }

    public void start() {
        machineThread = new MachineThread(this);
        machineThread.start();
    }

    public void pause() {
        machineThread.interrupt();
    }

    public void poweroff() {
        cpu.reset();
        machineThread.interrupt();
    }

    public void reset() {
        cpu.reset();
        machineThread = new MachineThread(this);
        machineThread.start();
    }

    public void uploadCode(int page, short[] src){
        int i = page*256;
        if(i>65536){
            return;
        }
        for(int j =0; j<src.length; i++, j++){
            memoryCode.data[i] = src[j];
        }
    }

    public void uploadData(int page, byte[] src){
        int i = page*256;
        if(i>65536){
            return;
        }
        for(int j =0; j<src.length; i++, j++){
            memoryData.data[i] = src[j];
        }
    }

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Machine.class, new MachineDeserializer())
            .registerTypeAdapter(Machine.class, new MachineSerializer())
            .create();

    private static class MachineDeserializer implements JsonDeserializer<Machine> {
        @Override
        public Machine deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Machine result = new Machine();
            JsonObject json = jsonElement.getAsJsonObject();
            result.modelBIOS = json.get("bios").getAsString();
            JsonObject devices = json.get("devices").getAsJsonObject();
            for (int i = 0; i < 8; i++) {
                if (devices.has("dev" + i)) {
                    JsonObject devObj = devices.get("dev" + i).getAsJsonObject();
                    Device dev = DeviceManager.get(devObj.get("name").getAsString());
                    result.devices[i] = dev;
                    result.devAutostart[i] = devObj.get("autostart").getAsBoolean();
                }
            }
            return result;
        }
    }

    private static class MachineSerializer implements JsonSerializer<Machine> {
        @Override
        public JsonElement serialize(Machine machine, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();
            result.addProperty("bios", "std");
            JsonObject devices = new JsonObject();
            for (int i = 0; i < 8; i++) {
                JsonObject devObj = new JsonObject();
                devObj.addProperty("name", machine.devices[i].getName());
                devObj.addProperty("autostart", machine.devAutostart[i]);
                devices.add("dev" + i, devObj);
            }
            result.add("devices", devices);
            return result;
        }
    }
}
