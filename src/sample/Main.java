package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Bibliothéque");
        primaryStage.setScene(new Scene(root, 650, 475));
        primaryStage.show();
    }

    @FXML
    public void CloseApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }

}
