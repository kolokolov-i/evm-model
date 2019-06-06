package superbro.evm.gui.inspector;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import superbro.evm.MachineManager;
import superbro.evm.core.CPU;
import superbro.evm.core.Machine;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private ComboBox<Integer> pageListCode;
    @FXML
    private ComboBox<Integer> pageListData;
    @FXML
    private ComboBox<Integer> pageListStack;

    private Machine machine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i<256; i++){
            list.add(i);
        }
        pageListCode.getItems().addAll(FXCollections.observableList(list));
        pageListData.getItems().addAll(FXCollections.observableList(list));
        pageListStack.getItems().addAll(FXCollections.observableList(list));
        pageListCode.setValue(0);
        pageListData.setValue(0);
        pageListStack.setValue(0);
    }

    public void setMachine(MachineManager.MachineItem machine) {
        this.machine = machine.instance;
        updateView();
    }

    public void updateView() {
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
        updateHexCode();
        updateHexData();
        updateHexStack();
    }

    @FXML
    public void updateHexStack() {
        textStack.setText(buildHexDump8(
                machine.memoryStack.data, pageListStack.getValue()));
    }

    @FXML
    public void updateHexCode(){
        textCode.setText(buildHexDump16(
                machine.memoryCode.data, pageListCode.getValue()));
    }

    @FXML
    public void updateHexData(){
        textData.setText(buildHexDump8(
                machine.memoryData.data, pageListData.getValue()));
    }

    private String buildHexDump16(short[] data, int pn) {
        String s;
        int i, j, k;
        k = pn * 256;
        s = "     |  00   01   02   03   04   05   06   07   08   09   0A   0B   0C   0D   0E   0F\n";
        for (i = 0; i < 16; i++) {
            s += String.format("%04X | ", k);
            for (j = 0; j < 16; j++) {
                s += String.format("%04X ", data[k++]);
            }
            s += "\n";
        }
        return s;
    }

    private String buildHexDump8(byte[] data, int pn) {
        String s;
        int i, j, k;
        k = pn * 256;
        s = "     | 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F\n";
        for (i = 0; i < 16; i++) {
            s += String.format("%04X | ", k);
            for (j = 0; j < 16; j++) {
                s += String.format("%02X ", data[k++]);
            }
            s += "\n";
        }
        return s;
    }
}
