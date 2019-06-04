package superbro.evm.gui.manager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import superbro.evm.MachineManager;
import superbro.evm.gui.GUI;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MachineManager.MachineItem selectedMachine;

    @FXML
    private Button btnNew;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnPoweroff;
    @FXML
    private Button btnIDE;
    @FXML
    private Button btnInspector;
    @FXML
    private ListView<MachineManager.MachineItem> mList;
    @FXML
    private Rectangle rectGreen;

    @FXML
    private Label mName;

    @FXML
    private VBox dev0Holder;
    @FXML
    private VBox dev1Holder;
    @FXML
    private VBox dev2Holder;
    @FXML
    private VBox dev3Holder;
    @FXML
    private VBox dev4Holder;
    @FXML
    private VBox dev5Holder;
    @FXML
    private VBox dev6Holder;
    @FXML
    private VBox dev7Holder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("controller init");
        updateMachinesList();
        mList.setCellFactory(machineListView -> new MachineListViewCell());
        mList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMachine = newValue;
            updateViewForMachine();
        });
    }

    @FXML
    public void btnNewAction(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Machine creating");
        dialog.setHeaderText("Enter machine name:");
        dialog.setContentText("Machine:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(MachineManager::createMachine);
        updateMachinesList();
    }

    @FXML
    public void btnDeleteAction(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete machine");
        alert.setHeaderText("Are you sure want to delete machine from list?");
        alert.setContentText(selectedMachine.title);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MachineManager.deleteMachine(selectedMachine);
                updateMachinesList();
            }
        });
    }

    @FXML
    public void btnStartAction(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        selectedMachine.start();
        updateMachinesList();
    }

    @FXML
    public void btnPauseAction(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        selectedMachine.pause();
        updateMachinesList();
        if(selectedMachine.inspectorController!=null) {
            selectedMachine.inspectorController.updateView();
        }
    }

    @FXML
    public void btnResetAction(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        selectedMachine.reset();
        updateMachinesList();
    }

    @FXML
    public void btnPoweroffAction(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        selectedMachine.poweroff();
        updateMachinesList();
    }

    @FXML
    public void btnIDE(ActionEvent e) {
        GUI.showIDE();
    }

    @FXML
    public void btnInspector(ActionEvent e) {
        if (selectedMachine == null) {
            return;
        }
        try {
            GUI.showInspector(selectedMachine);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateMachinesList() {
        mList.setItems(FXCollections.observableArrayList(MachineManager.getMachines()));
        mList.refresh();
    }

    private void updateViewForMachine() {
        if (selectedMachine == null) {
            mName.setText("");
            return;
        }
        mName.setText(selectedMachine.title);
    }

    @FXML
    public void greenRectClick(MouseEvent e) {

    }

    class MachineListViewCell extends ListCell<MachineManager.MachineItem> {

        @Override
        protected void updateItem(MachineManager.MachineItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText("");
                return;
            }
            if (item == null) {
                setText("");
                return;
            }
            Color c;
            String state;
            switch (item.status) {
                case RUN:
                    c = Color.rgb(0, 140, 70);
                    state = " [ RUN ]";
                    break;
                case PAUSE:
                    c = Color.rgb(0, 80, 160);
                    state = " [ PAUSE ]";
                    break;
                case ERROR:
                    c = Color.rgb(190, 20, 0);
                    state = " [ ERROR ]";
                    break;
                case IDLE:
                default:
                    c = Color.BLACK;
                    state = "";
            }
            setText(item.title + state);
            setTextFill(c);
        }
    }
}
