package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;

public class UnicitySyncController {
    @FXML private Button CloseButton;
    @FXML
    public void closeWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/unicitySync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) CloseButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
