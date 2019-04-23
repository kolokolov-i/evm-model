package superbro.evm;

import javafx.application.Application;
import javafx.stage.Stage;
import superbro.evm.gui.GUI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            GUI.create(primaryStage);
        }
        catch (Exception ex){
            ex.printStackTrace(System.err);
            throw ex;
        }
        primaryStage.show();
        GUI.hideSplash();
    }

    public static void main(String[] args) {
        GUI.showSplash();
        Config.load();
        launch(args);
    }
}
