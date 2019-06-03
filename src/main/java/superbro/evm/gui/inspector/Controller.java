package superbro.evm.gui.inspector;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import superbro.evm.MachineManager;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField tfReg0;
    @FXML
    private TextField tfReg1;
    @FXML
    private TextField tfReg2;
    @FXML
    private TextField tfReg3;
    @FXML
    private TextField tfReg4;
    @FXML
    private TextField tfReg5;
    @FXML
    private TextField tfReg6;
    @FXML
    private TextField tfReg7;
    @FXML
    private TextField tfRegPC;
    @FXML
    private TextField tfRegIR;
    @FXML
    private TextField tfRegSP;
    @FXML
    private TextField tfRegBP;
    @FXML
    private TextField tfRegF;
    @FXML
    private TextField tfRegI;

    @FXML
    private TextArea textCode;
    @FXML
    private TextArea textData;
    @FXML
    private TextArea textStack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("inspector controller init");
    }

    public void setMachine(MachineManager.MachineItem machine){

        System.out.println("inspector set machine");
    }
}
