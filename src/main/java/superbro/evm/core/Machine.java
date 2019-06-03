package superbro.evm.core;

import com.google.gson.*;
import superbro.evm.core.device.Empty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Machine {

    private Device[] devices;
    private Memory.Byte
            memoryData, memoryStack;
    private Memory.Word
            memoryBIOS, memoryCode;
    private String modelBIOS;
    private CPU cpu;

    private static Memory.Word stdBIOS;

    public static void setStdBIOS(Memory.Word mem){
        stdBIOS = mem;
    }

    public static Machine loadFrom(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path.resolve("machine.json"));
        Machine machine = gson.fromJson(reader, Machine.class);
        if(machine.modelBIOS.equals("std")){
            machine.memoryBIOS = stdBIOS.clone();
        }
        else{
            Path fmBios = path.resolve("bios.mem");
            machine.memoryBIOS = new Memory.Word(Files.newInputStream(fmBios));
        }
        Path fmCode = path.resolve("code.mem");
        if(Files.exists(fmCode)){
            machine.memoryCode = new Memory.Word(Files.newInputStream(fmCode));
        }
        else{
            machine.memoryCode = new Memory.Word();
        }
        Path fmData = path.resolve("data.mem");
        if(Files.exists(fmCode)){
            machine.memoryData = new Memory.Byte(Files.newInputStream(fmData));
        }
        else{
            machine.memoryData = new Memory.Byte();
        }
        Path fmStack = path.resolve("stack.mem");
        if(Files.exists(fmCode)){
            machine.memoryStack = new Memory.Byte(Files.newInputStream(fmStack));
        }
        else{
            machine.memoryStack = new Memory.Byte();
        }
        return machine;
    }

    public static void saveTo(Machine machine, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
        gson.toJson(machine, writer);
        writer.close();
    }

    public static Machine getStandardInstance(){
        Machine result = new Machine();
        result.modelBIOS = "std";
        result.memoryBIOS = stdBIOS;
        result.memoryCode = new Memory.Word();
        result.memoryData = new Memory.Byte();
        result.memoryStack = new Memory.Byte();
        result.cpu = new CPU(result);
        return result;
    }

    public Machine() {
        devices = new Device[8];
        for(int i = 0; i<8; i++){
            devices[i] = new Empty();
        }
    }

    public void start() {

    }

    public void pause() {

    }

    public void poweroff() {

    }

    public void reset() {

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
                if(devices.has("dev" + i)){
                    String devName = devices.get("dev" + i).getAsString();
                    result.devices[i] = DeviceManager.get(devName);
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
                devices.addProperty("dev" + i, machine.devices[i].getName());
            }
            result.add("devices", devices);
            return result;
        }
    }
}
