package superbro.evm.gui.codingdemo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.BitSet;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextField tfAHInput;
    @FXML
    TextField tfALInput;
    @FXML
    TextField tfAH;
    @FXML
    TextField tfAL;
    @FXML
    TextField tfAS;
    @FXML
    TextField tfBHOutput;
    @FXML
    TextField tfBLOutput;
    @FXML
    TextField tfBH;
    @FXML
    TextField tfBL;
    @FXML
    TextField tfBS;
    @FXML
    TextField tfT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfAHInput.setText("01000100");
        tfALInput.setText("00111101");
        changeInput();
    }

    @FXML
    public void changeInput(){
        tfAH.setText(tfAHInput.getText());
        tfAL.setText(tfALInput.getText());
        //BitSet
        //tfAS.setText();
        changeTransport();
    }
    @FXML
    public void changeTransport(){

    }
}
