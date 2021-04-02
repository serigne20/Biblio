package sample.ViewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UnicityController {
    @FXML
    private Button CloseButton;

    /**
     * Ferme la fenêtre
     * @param event
     */
    @FXML
    public void closeWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/unicity.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) CloseButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
