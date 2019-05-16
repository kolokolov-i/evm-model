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

    static void scanMachines() {

    }

    public static void createMachine(String name) {
        Optional<MachineItem> item = machines.stream().filter(m -> m.name.equals(name.toUpperCase())).findAny();
        if(item.isPresent()){
            JOptionPane.showMessageDialog(null, "Machine is already exist", "Can't create machine", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        MachineItem machine = new MachineItem(name);
        try {
            Path path = Config.machinesDirectory.resolve(machine.name);
            if (Files.exists(path)) {
                JOptionPane.showMessageDialog(null, "Machine is already exist", "Machine added", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                Files.createDirectory(path);
            }
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
        public String title;
        public Machine instance;

        public MachineItem(String title) {
            this.title = title;
            this.name = title.toUpperCase();
        }
    }
}
