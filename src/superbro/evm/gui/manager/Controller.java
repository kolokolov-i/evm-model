package superbro.evm.gui.manager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import superbro.evm.MachineManager;
import superbro.evm.gui.GUI;

import javax.swing.*;
import java.net.URL;
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
    private ListView<MachineManager.MachineItem> mList;
    @FXML
    private Rectangle rectGreen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateMachinesList();
        mList.setCellFactory(machineListView -> new MachineListViewCell());
    }

    @FXML
    public void btnNewAction(ActionEvent e) {
        DialogCreateMachine.run();
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

    private void updateMachinesList() {
        mList.setItems(FXCollections.observableArrayList(MachineManager.getMachines()));

//        ObservableList items = mList.getItems();
//        items.clear();
//        List<MachineManager.MachineItem> machines = MachineManager.getMachines();
//        for (MachineManager.MachineItem m : machines) {
//            items.add(m.title);
//        }
    }

    @FXML
    public void greenRectClick(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "Hi", "EVM", JOptionPane.INFORMATION_MESSAGE);
    }

    class MachineListViewCell extends ListCell<MachineManager.MachineItem> {

        @Override
        protected void updateItem(MachineManager.MachineItem item, boolean empty) {
            super.updateItem(item, empty);
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
