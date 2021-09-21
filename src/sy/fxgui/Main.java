package sy.fxgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new GameManager(), 1300, 750); //Everything is contained in GameManager
        primaryStage.setResizable(false); //Assets are made to fit only one size
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
