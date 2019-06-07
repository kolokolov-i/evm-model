package superbro.evm.gui.ide;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import superbro.evm.MachineManager;
import superbro.evm.core.Machine;
import superbro.evm.gui.GUI;
import superbro.evm.translator.Translator;
import superbro.evm.translator.asm.AsmTranslator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    TextArea textCode;
    @FXML
    TextArea textData;
    @FXML
    TextArea binCode;
    @FXML
    TextArea binData;
    @FXML
    ListView<String> messageList;
    @FXML
    Accordion messageAccordion;
    @FXML
    TitledPane messagePane;
    @FXML
    ComboBox<Integer> pageList;
    @FXML
    Button btnUpload;

    short[] code;
    byte[] data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i<256; i++){
            list.add(i);
        }
        pageList.getItems().addAll(FXCollections.observableList(list));
        pageList.setValue(0);
        code = null;
        data = null;
    }

    @FXML
    public void uploadProgramm(){
        MachineManager.MachineItem selectedMachine = GUI.getManagerController().selectedMachine;
        if(selectedMachine==null){
            return;
        }
        Machine machine = selectedMachine.instance;
        if(machine==null){
            return;
        }
        if(code == null){
            return;
        }
        if(selectedMachine.status == MachineManager.Status.IDLE){
            machine.uploadCode(pageList.getValue(), code);
            selectedMachine.save();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Upload program");
            alert.setHeaderText("Machine must be loaded and stopped");
            alert.setContentText(selectedMachine.title);
            alert.showAndWait();
        }
    }

    @FXML
    public void btnActionNew(ActionEvent e){
        System.out.println("new");
    }
    @FXML
    public void btnActionOpen(ActionEvent e){
        System.out.println("open");
    }
    @FXML
    public void btnActionSave(ActionEvent e){
        System.out.println("save");
    }
    @FXML
    public void btnActionSaveAs(ActionEvent e){
        System.out.println("saveAs");
    }
    @FXML
    public void btnActionTranslate(ActionEvent e){
        String srcCode = textCode.getText();
        String srcData = textData.getText();
        code = null;
        data = null;
        Translator trans = new AsmTranslator(srcCode, srcData);
        boolean success = trans.translate();
        messageList.getItems().clear();
        messageList.getItems().addAll(
                trans.getMessager().getErrors().stream().map(t->t.toString("Error"))
                        .collect(Collectors.toList()));
        messageList.getItems().addAll(
                trans.getMessager().getWarnings().stream().map(t->t.toString("Warning"))
                        .collect(Collectors.toList()));
        binCode.setText(trans.getListingCode());
        binData.setText(trans.getListingData());
        code = trans.getRawCode();
        data = null;
        if(!messageList.getItems().isEmpty()){
            messageAccordion.setExpandedPane(messagePane);
        }
    }
}
