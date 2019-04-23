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

    public static void showSplash(){
        splashFrame = new JFrame();
        splashFrame.setUndecorated(true);
        splashFrame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        splashFrame.setBounds(width/2-150, height/2-75, 300, 150);
        splashFrame.setLayout(new BorderLayout());
        JProgressBar progress = new JProgressBar();
        progress.setValue(50);
        splashFrame.add(progress, BorderLayout.SOUTH);
        splashFrame.setVisible(true);
    }

    public static void create(Stage primaryStage) throws Exception {
        loadManagerStage(primaryStage);
        loadIDEStage();
    }

    private static void loadIDEStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/ide/ide.fxml"));
        Parent root = fxmlLoader.load();
        ideController = fxmlLoader.getController();
        ideStage = new Stage();
        ideStage.setTitle("IDE");
        ideStage.setScene(new Scene(root, 640, 480));
    }

    private static void loadManagerStage(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/manager/manager.fxml"));
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

    public static void hideSplash() {
        splashFrame.dispose();
    }
}
