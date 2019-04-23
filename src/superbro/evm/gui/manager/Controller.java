package superbro.evm.gui.manager;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import superbro.evm.MachineManager;
import superbro.evm.gui.GUI;
import superbro.evm.gui.ide.IDE;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
    private ListView mList;
    @FXML
    private Rectangle rectGreen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void btnNewAction(ActionEvent e) {
        DialogCreateMachine.run();
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

    public void updateMachinesList() {
        ObservableList items = mList.getItems();
        items.clear();
        List<MachineManager.MachineItem> machines = MachineManager.getMachines();
        for (MachineManager.MachineItem m : machines) {
            items.add(m.name);
        }
    }

    @FXML
    public void greenRectClick(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "Hi", "EVM", JOptionPane.INFORMATION_MESSAGE);
    }

}
