package superbro.evm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Config {

    public static Path
            directory = Paths.get(System.getProperty("user.dir")),
            mainConfig = directory.resolve("evm.conf.json"),
            machinesConfig = directory.resolve("machines.json"),
            machinesDirectory = directory.resolve("machines");
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        try{
            if (Files.exists(mainConfig)) {
                loadMainConfig();
            } else {
                createMainConfig();
            }
            if(Files.exists(machinesConfig)){
                loadMachinesConfig();
            }
            else{
                createMachinesConfig();
            }
        }
        catch(IOException e){
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    private static void loadMachinesConfig() throws IOException {
        BufferedReader reader = Files.newBufferedReader(machinesConfig);
        Type type = new TypeToken<List<MachineManager.MachineItem>>(){}.getType();
        MachineManager.machines = gson.fromJson(reader, type);
        reader.close();
    }

    private static void createMachinesConfig() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(machinesConfig, StandardOpenOption.CREATE);
        gson.toJson(MachineManager.machines, writer);
        Files.createDirectory(machinesDirectory);
        writer.close();
    }

    private static void loadMainConfig() throws IOException {
        BufferedReader reader = Files.newBufferedReader(mainConfig);
        String sign = gson.fromJson(reader, String.class);
        reader.close();
    }

    public static void saveMachinesConfig() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(machinesConfig, StandardOpenOption.CREATE);
        gson.toJson(MachineManager.machines, writer);
        writer.close();
    }

    private static void createMainConfig() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(mainConfig, StandardOpenOption.CREATE);
        gson.toJson("EVM1", writer);
        writer.close();
    }
}
