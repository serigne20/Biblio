package sample.ViewControllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FileMenuController extends Controller implements Initializable{
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private javafx.scene.control.Button closeButton;

    /**
     * Quitte l'application
     * @param event évènement de clique sur le bouton
     */
    public void Quit(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
    /**
     * Quitte la fenêtre
     * @param event évènement de clique sur le bouton
     */
    public void NoQuit(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CloseApp.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
