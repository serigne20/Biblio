package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javax.swing.*;
import java.net.URL;

public class Controller {

    @FXML
    public void CloseApp(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CloseApp.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Popup popup = new Popup();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1,400,200));
            stage.setTitle("Quitter");
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
