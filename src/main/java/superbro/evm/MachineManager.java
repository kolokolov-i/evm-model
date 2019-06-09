package superbro.evm;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import superbro.evm.core.Machine;
import superbro.evm.gui.inspector.Controller;

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
                    m.instance.holderItem = m;
                    m.statusProp = new SimpleObjectProperty<>(m.status);
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
                machine.instance = Machine.getStandardInstance();
                machine.instance.holderItem = machine;
                Machine.saveTo(machine.instance, path);
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
        transient public ObjectProperty<Status> statusProp;
        transient public String description;
        transient public Controller inspectorController;

        public MachineItem(String title) {
            this.title = title;
            this.name = title.toUpperCase();
            this.status = Status.IDLE;
            statusProp = new SimpleObjectProperty<>(status);
        }

        public void start() {
            if (instance == null) {
                return;
            }
            status = Status.RUN;
            instance.start();
        }

        public void pause() {
            if (instance == null) {
                return;
            }
            status = Status.PAUSE;
            instance.pause();
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
            save();
        }

        public void save(){
            try {
                Machine.saveTo(instance, Config.machinesDirectory.resolve(name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Status getStatusProp() {
            return statusProp.get();
        }

        public ObjectProperty<Status> statusPropProperty() {
            return statusProp;
        }

        public void setStatusProp(Status statusProp) {
            this.statusProp.set(statusProp);
        }
    }

    public enum Status {
        IDLE, RUN, PAUSE, ERROR
    }
}
