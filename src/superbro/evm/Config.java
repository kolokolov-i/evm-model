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

    static Main mainConfig;

    static Path
            directory = Paths.get(System.getProperty("user.dir")),
            mainConfigPath = directory.resolve("evm.conf.json"),
            machinesConfig = directory.resolve("machines.json"),
            machinesDirectory = directory.resolve("machines");
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        try {
            if (Files.exists(mainConfigPath)) {
                loadMainConfig();
            } else {
                createMainConfig();
            }
            if(Files.exists(machinesConfig)){
                loadMachinesConfig();
            }
            else{
                saveMachinesConfig();
            }
            MachineManager.scanMachines();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    static void saveMachinesConfig() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(machinesConfig, StandardOpenOption.CREATE);
        gson.toJson(MachineManager.machines, writer);
        writer.close();
        /*for(MachineManager.MachineItem m : MachineManager.machines){
            Files.createDirectory(machinesDirectory.resolve(m.name));
        }*/
    }

    private static void loadMachinesConfig() throws IOException {
        BufferedReader reader = Files.newBufferedReader(machinesConfig);
        Type type = new TypeToken<List<MachineManager.MachineItem>>() {
        }.getType();
        MachineManager.machines = gson.fromJson(reader, type);
        reader.close();
    }

    private static void createMainConfig() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(mainConfigPath, StandardOpenOption.CREATE);
        gson.toJson(new Main(), writer);
        writer.close();
    }

    private static void loadMainConfig() throws IOException {
        BufferedReader reader = Files.newBufferedReader(mainConfigPath);
        mainConfig = gson.fromJson(reader, Main.class);
        reader.close();
        if (mainConfig == null) {
            createMainConfig();
        }
    }

    static class Main {

        String version;
        String language;

        public Main() {
            version = "1.0";
            language = "eng";
        }
    }
}
