package superbro.evm;

import com.google.gson.annotations.SerializedName;
import superbro.evm.core.Machine;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MachineManager {

    static List<MachineItem> machines = new ArrayList<>();

    static List<MachineItem> getMachineList() {
        return machines;
    }

    private static void scanMachines() {

    }

    public static void createMachine(String name) {
        Optional<MachineItem> item = machines.stream().filter(m -> m.name == name).findAny();
        if(item.isPresent()){
            JOptionPane.showMessageDialog(null, "Machine is already exist", "Can't create machine", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String folder = name.toLowerCase();
        MachineItem machine = new MachineItem(name, folder);
        try {
            Path path = Config.machinesDirectory.resolve(folder);
            if (Files.exists(path)) {
                machines.add(machine);
                Config.saveMachinesConfig();
                JOptionPane.showMessageDialog(null, "Machine is already exist", "Machine added", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Files.createDirectory(path);
            machines.add(machine);
            Config.saveMachinesConfig();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    public static List<MachineItem> getMachines() {
        return machines;
    }

    public static class MachineItem {
        public String name;
        private String path;

        public MachineItem(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }
}
