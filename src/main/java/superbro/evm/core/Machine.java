package superbro.evm.core;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class Machine {

    private Device[] devices;
    private Memory
            memoryBIOS, memoryCode, memoryData, memoryStack;

    public static Machine loadFrom(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path.resolve("machine.json"));
        Machine machine = gson.fromJson(reader, Machine.class);
        Path fmCode = path.resolve("code.memory");
        if(Files.exists(fmCode)){
            machine.memoryCode = new Memory.Word(Files.newInputStream(fmCode));
        }
        else{
            machine.memoryCode = new Memory.Word();
        }
        Path fmData = path.resolve("data.memory");
        if(Files.exists(fmCode)){
            machine.memoryData = new Memory.Byte(Files.newInputStream(fmData));
        }
        else{
            machine.memoryData = new Memory.Byte();
        }
        Path fmStack = path.resolve("stack.memory");
        if(Files.exists(fmCode)){
            machine.memoryStack = new Memory.Byte(Files.newInputStream(fmStack));
        }
        else{
            machine.memoryStack = new Memory.Byte();
        }
        return machine;
    }

    public Machine() {
        devices = new Device[8];
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
            //.registerTypeAdapter(Machine.class, new MachineSerializer())
            .create();

    private static class MachineDeserializer implements JsonDeserializer<Machine> {

        @Override
        public Machine deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Machine result = new Machine();
            JsonObject json = jsonElement.getAsJsonObject();
            String bios = json.get("bios").getAsString();
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
            return null;
        }
    }
}
