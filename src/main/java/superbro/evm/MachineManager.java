package superbro.evm;

import javafx.scene.control.Alert;
import superbro.evm.core.Machine;

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
                    m.description = "Ready";
                } catch (Exception e) {
                    e.printStackTrace();
                    m.instance = null;
                    m.status = Status.ERROR;
                    m.description = "Loading error";
                }
            }
        }
    }

    public static void createMachine(String name) {
        Optional<MachineItem> item = machines.stream().filter(m -> m.name.equals(name.toUpperCase())).findAny();
        if (item.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Create machine");
            alert.setHeaderText("Machine is already exist");
            alert.setContentText(name);
            alert.showAndWait();
            return;
        }
        MachineItem machine = new MachineItem(name);
        try {
            Path path = Config.machinesDirectory.resolve(machine.name);
            if (Files.exists(path)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Create machine");
                alert.setHeaderText("Machine is already exist");
                alert.setContentText("Machine \"" + name + "\" added to list");
                alert.showAndWait();
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

    public static void deleteMachine(MachineItem machine) {
        machines.remove(machine);
        try {
            Config.saveMachinesConfig();
        } catch (IOException e) {
            e.printStackTrace();
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
        transient public String description;

        public MachineItem(String title) {
            this.title = title;
            this.name = title.toUpperCase();
            this.status = Status.IDLE;
        }

        public void start() {
            if (instance == null) {
                return;
            }
            status = Status.RUN;
            instance.start();
        }

        public void reset() {
            if (instance == null) {
                return;
            }
            status = Status.RUN;
            instance.reset();
        }

        public void poweroff() {
            if (instance == null) {
                return;
            }
            status = Status.IDLE;
            instance.poweroff();
        }
    }

    public enum Status {
        IDLE, RUN, PAUSE, ERROR
    }
}
