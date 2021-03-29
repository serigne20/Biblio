package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ErreurController extends Controller implements Initializable{

    @FXML
    private javafx.scene.control.Button CloseButton;
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void NoQuit(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Erreur.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) CloseButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
