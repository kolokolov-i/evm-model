package superbro.evm.gui.ide;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import superbro.evm.translator.Translator;
import superbro.evm.translator.asm.AsmTranslator;

import java.net.URL;
import java.util.ResourceBundle;

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
        messageList.getItems().addAll(trans.getMessages());
        binCode.setText(trans.getListingCode());
        binData.setText(trans.getListingData());
        if(!success){

        }
    }
}
