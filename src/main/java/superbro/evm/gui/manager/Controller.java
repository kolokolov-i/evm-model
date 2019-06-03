package superbro.evm.gui.manager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import javax.swing.*;
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
        System.out.println("controller init");
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

    }

    @FXML
    public void btnStartAction(ActionEvent e) {

    }

    @FXML
    public void btnResetAction(ActionEvent e) {

    }

    @FXML
    public void btnPoweroffAction(ActionEvent e) {

    }

    @FXML
    public void btnIDE(ActionEvent e) {
        GUI.showIDE();
    }

    @FXML
    public void btnInspector(ActionEvent e) {
        if(selectedMachine == null){
            return;
        }
        superbro.evm.gui.inspector.Controller inspector;
        try {
            inspector = GUI.showInspector(selectedMachine);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateMachinesList() {
        mList.setItems(FXCollections.observableArrayList(MachineManager.getMachines()));
    }

    private void updateViewForMachine(){
        if(selectedMachine==null){
            mName.setText("");
            return;
        }
        mName.setText(selectedMachine.title);
    }

    @FXML
    public void greenRectClick(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "Hi", "EVM", JOptionPane.INFORMATION_MESSAGE);
    }

    class MachineListViewCell extends ListCell<MachineManager.MachineItem> {

        @Override
        protected void updateItem(MachineManager.MachineItem item, boolean empty) {
            super.updateItem(item, empty);
            if(empty){
                return;
            }
            if(item==null){
                return;
            }
            Color c;
            setText(item.title);
            switch (item.status){
                case RUN:
                    c = Color.rgb(0, 140, 70);
                    break;
                case PAUSE:
                    c = Color.rgb(0, 80, 160);
                    break;
                case ERROR:
                    c = Color.rgb(190, 20, 0);
                    break;
                case IDLE:
                default:
                    c = Color.BLACK;
            }
            setTextFill(c);
        }
    }
}
