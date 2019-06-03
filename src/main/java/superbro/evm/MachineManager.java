package superbro.evm;

import superbro.evm.core.Machine;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MachineManager {

    static List<MachineItem> machines = new ArrayList<>();

    static void scanMachines() {
        for (MachineItem m : machines) {
            Path path = Config.machinesDirectory.resolve(m.name);
            if (Files.exists(path)) {
                try {
                    m.instance = Machine.loadFrom(path);
                    m.status = Status.IDLE;
                    m.desription = "Ready";
                } catch (Exception e) {
                    m.instance = null;
                    m.status = Status.ERROR;
                    m.desription = "Loading error";
                }
            }
        }
    }

    public static void createMachine(String name) {
        Optional<MachineItem> item = machines.stream().filter(m -> m.name.equals(name.toUpperCase())).findAny();
        if (item.isPresent()) {
            JOptionPane.showMessageDialog(null, "Machine is already exist", "Can't create machine", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        MachineItem machine = new MachineItem(name);
        try {
            Path path = Config.machinesDirectory.resolve(machine.name);
            if (Files.exists(path)) {
                JOptionPane.showMessageDialog(null, "Machine is already exist", "Machine added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Files.createDirectory(path);
                Machine instance = Machine.getStandardInstance();
                Machine.saveTo(instance, path.resolve("machine.json"));
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
        transient public Machine instance;
        transient public Status status;
        transient public String desription;

        public MachineItem(String title) {
            this.title = title;
            this.name = title.toUpperCase();
            this.status = Status.IDLE;
        }
    }

    public enum Status {
        IDLE, RUN, PAUSE, ERROR
    }
}
