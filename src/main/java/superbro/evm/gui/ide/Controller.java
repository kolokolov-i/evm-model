package superbro.evm.gui.ide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import superbro.evm.translator.MessageRecord;
import superbro.evm.translator.Translator;
import superbro.evm.translator.asm.AsmTranslator;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        if(!messageList.getItems().isEmpty()){
            messageAccordion.setExpandedPane(messagePane);
        }
    }
}