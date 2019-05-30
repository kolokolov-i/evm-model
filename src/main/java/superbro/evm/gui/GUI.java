package superbro.evm.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import superbro.evm.Main;
import superbro.evm.gui.manager.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {

    private static superbro.evm.gui.manager.Controller managerController;
    private static superbro.evm.gui.ide.Controller ideController;
    private static Stage ideStage;

    private static JFrame splashFrame;
    private static JProgressBar splashProgress;
    private static JLabel splashMessage;

    public static void splashShow(){
        splashFrame = new JFrame();
        splashFrame.setUndecorated(true);
        splashFrame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        int splashW = 300; int splashH = 150;
        splashFrame.setBounds(width/2-splashW/2, height/2-splashH/2, splashW, splashH);
        splashFrame.setLayout(new BorderLayout());
        splashProgress = new JProgressBar();
        splashProgress.setValue(0);
        // 300 135
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(80, 150, 120));
        splashMessage = new JLabel("Loading ...");
        splashMessage.setForeground(Color.WHITE);
        splashMessage.setBounds(15, 120, 200, 15);
        panel.add(splashMessage);
        splashFrame.add(panel, BorderLayout.CENTER);
        splashFrame.add(splashProgress, BorderLayout.SOUTH);
        splashFrame.setVisible(true);
    }

    public static void create(Stage primaryStage) throws Exception {
        GUI.splahSet("Loading Manager", 50);
        loadManagerStage(primaryStage);
        GUI.splahSet("Loading IDE", 70);
        loadIDEStage();
    }

    private static void loadIDEStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/superbro/evm/gui/ide/ide.fxml"));
        Parent root = fxmlLoader.load();
        ideController = fxmlLoader.getController();
        ideStage = new Stage();
        ideStage.setTitle("IDE");
        ideStage.setScene(new Scene(root, 640, 480));
    }

    private static void loadManagerStage(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("/superbro/evm/gui/manager/manager.fxml"));
        Parent root = fxmlLoader.load();
        managerController = fxmlLoader.getController();
        primaryStage.setTitle("EVM Manager");
        primaryStage.setScene(new Scene(root, 640, 480));
    }

    public static Controller getManagerController() {
        return managerController;
    }

    public static superbro.evm.gui.ide.Controller getIdeController() {
        return ideController;
    }

    public static void showIDE() {
        ideStage.show();
    }

    public static void splahSet(String message, int progress){
        splashMessage.setText(message);
        splashProgress.setValue(progress);
    }

    public static void splashHide() {
        splashFrame.dispose();
    }
}
