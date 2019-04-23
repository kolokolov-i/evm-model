package superbro.evm.gui.manager;

import superbro.evm.MachineManager;
import superbro.evm.gui.GUI;

import javax.swing.*;
import java.awt.event.*;

public class DialogCreateMachine extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lbl1;
    private JTextField textFieldName;

    public DialogCreateMachine() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        MachineManager.createMachine(textFieldName.getText());
        Controller controller = GUI.getManagerController();
        controller.updateMachinesList();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void run() {
        DialogCreateMachine dialog = new DialogCreateMachine();
        dialog.pack();
        dialog.setVisible(true);
    }
}
