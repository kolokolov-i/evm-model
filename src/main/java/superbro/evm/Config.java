package superbro.evm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import superbro.evm.core.Machine;
import superbro.evm.core.Memory;
import superbro.evm.gui.GUI;

import java.io.*;
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
            machinesDirectory = directory.resolve("machines"),
            systemDirectory = directory.resolve("system");
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        try {
            if (Files.exists(mainConfigPath)) {
                loadMainConfig();
            } else {
                createMainConfig();
            }
            GUI.splahSet("Loading machines", 20);
            if(Files.exists(systemDirectory)){
                loadSystemConfig();
            }
            else{
                createSystemConfig();
            }
            if (!Files.exists(machinesDirectory)) {
                Files.createDirectory(machinesDirectory);
            }
            if (Files.exists(machinesConfig)) {
                loadMachinesConfig();
            } else {
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

    private static void createSystemConfig() throws IOException {
        Files.createDirectory(systemDirectory);
        InputStream input = Config.class.getResourceAsStream("/superbro/evm/system/std.bios.mem");
        OutputStream output = Files.newOutputStream(systemDirectory.resolve("std.bios.mem"));
        Memory.Word bios = new Memory.Word(input);
        bios.write(output);
    }

    private static void loadSystemConfig() throws IOException {
        InputStream in = Files.newInputStream(systemDirectory.resolve("std.bios.mem"));
        Memory.Word bios = new Memory.Word(in);
        Machine.setStdBIOS(bios);
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
