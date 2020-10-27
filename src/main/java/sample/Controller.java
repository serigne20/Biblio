package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Au début de la classe, appelle les differents attributs à récupérer pour
 * les méthodes qui vont suivre. C'est le fx:id qui permet de les identifier dans
 * le fichier fxml
 */
public class Controller implements Initializable {
    @FXML private TextField titreInput;
    @FXML private TextField auteurInput;
    @FXML private TextField parutionInput;
    @FXML private TextField colonneInput;
    @FXML private TextField rangeeInput;
    @FXML private TextArea resumeInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    /**
     * A quoi sert la méthode ? la méthode est l'action de cliquer sur le bouton about us.
     * Elle permet d'afficher une autre vue s'appellant Trombinoscope. Elle affiche la photo des développeurs
     * @param Event à quoi sert le paramètre de la méthode ? C'est l'évenement de cliquer sur le bouton About us
     */
    @FXML
    private void Aboutus (ActionEvent Event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/trombi.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Trombinoscope");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){System.out.println("raté");}
    }

    /**
     * Cette méthode permet de valider le formulaire, elle respecte le constructeur livre
     * et si des informations manquent, elle remplit automatiquement les champs.
     * A terme cette méthode permettra d'envoyer les nouvelles données dans le tableau
     * @param Event C'est l'évenement de cliquer sur le bouton
     */
    @FXML
    private void validerLivre (ActionEvent Event){
        String prenom, nom= "";
        String titre = titreInput.getText();
        String paru=parutionInput.getText();
        String res=resumeInput.getText();
        String aut=auteurInput.getText();
        try{int c =Integer.parseInt(colonneInput.getText());
            int r =Integer.parseInt(rangeeInput.getText());
            if (c<=5 && c>=1 && r<=7 && r>=1){
                if (titreInput.getText().isEmpty()){
                    titre= "Titre incconu";
                }
                if (auteurInput.getText().isEmpty()){
                    aut="auteur inconnu";
                }
                if (parutionInput.getText().isEmpty()){
                    paru="parution inconnu";
                }
                if (resumeInput.getText().isEmpty()){
                    res="résumé vide";
                }
                String[] auteur= aut.split(" ");
                prenom=auteur[1];
                nom=auteur[0];
                livre l1 = new livre (titre,nom,prenom,res,c,r,paru);
                System.out.println(l1.toString());
            }
            else{
                System.out.println("zebi");
            }
        }
        catch(NumberFormatException e){
            System.out.println("zebi");
        }


    }
}