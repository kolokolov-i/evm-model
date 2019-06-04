package superbro.evm.gui.inspector;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import superbro.evm.MachineManager;
import superbro.evm.core.CPU;
import superbro.evm.core.Machine;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Stage stage;

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

    private Machine machine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("inspector controller init");
    }

    public void setMachine(MachineManager.MachineItem machine) {
        this.machine = machine.instance;
        System.out.println("inspector set machine");
        updateView();
    }

    public void updateView() {
        System.out.println("inspector update");
        CPU cpu = machine.cpu;
        tfReg0.setText(String.format("%02X", cpu.regs8[0].value));
        tfReg1.setText(String.format("%02X", cpu.regs8[1].value));
        tfReg2.setText(String.format("%02X", cpu.regs8[2].value));
        tfReg3.setText(String.format("%02X", cpu.regs8[3].value));
        tfReg4.setText(String.format("%02X", cpu.regs8[4].value));
        tfReg5.setText(String.format("%02X", cpu.regs8[5].value));
        tfReg6.setText(String.format("%02X", cpu.regs8[6].value));
        tfReg7.setText(String.format("%02X", cpu.regs8[7].value));
        tfRegPC.setText(String.format("%04X", cpu.PC.value));
        tfRegIR.setText(String.format("%04X", cpu.IR.value));
        tfRegSP.setText(String.format("%04X", cpu.SP.value));
        tfRegBP.setText(String.format("%04X", cpu.BP.value));
        byte f = cpu.RF.value;
        String s = " 0 0 ";
        s += (f & 0x20) != 0 ? "1 " : "0 ";
        s += (f & 0x10) != 0 ? "1 " : "0 ";
        s += (f & 0x08) != 0 ? "1 " : "0 ";
        s += (f & 0x04) != 0 ? "1 " : "0 ";
        s += (f & 0x02) != 0 ? "1 " : "0 ";
        s += (f & 0x01) != 0 ? "1 " : "0 ";
        tfRegF.setText(s);
        f = cpu.RI.value;
        s = " ";
        s += (f & 0x80) != 0 ? "1 " : "0 ";
        s += (f & 0x40) != 0 ? "1 " : "0 ";
        s += (f & 0x20) != 0 ? "1 " : "0 ";
        s += (f & 0x10) != 0 ? "1 " : "0 ";
        s += (f & 0x08) != 0 ? "1 " : "0 ";
        s += (f & 0x04) != 0 ? "1 " : "0 ";
        s += (f & 0x02) != 0 ? "1 " : "0 ";
        s += (f & 0x01) != 0 ? "1 " : "0 ";
        tfRegI.setText(s);
        s = "";
        int i, j, k = 0;
        short[] data = machine.memoryCode.data;
        for (int iPage = 0; iPage < 256; iPage++) {
            s += "\tPage #" + iPage + "\n";
            for (i = 0; i < 16; i++) {
                for (j = 0; j < 16; j++) {
                    s += String.format("%04X ", data[k++]);
                }
                s += "\n";
            }
        }
        textCode.setText(s);
    }
}
