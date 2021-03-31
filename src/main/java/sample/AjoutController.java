package sample;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjoutController{
    private ObservableList<Bibliotheque.Livre> livresData;
    @FXML private TextField TitreInput;
    @FXML private TextField AuteurInput;
    @FXML private TextField ParutionInput;
    @FXML private TextField ColonneInput;
    @FXML private TextField RangeeInput;
    @FXML private TextField EditeurInput;
    @FXML private TextField FormatInput;
    @FXML private TextArea ResumeInput;
    @FXML private TextField URLInput;
    @FXML private RadioButton pret;
    @FXML private RadioButton available;
    @FXML private javafx.scene.control.Button btvalider;
    public void initialize(URL location, ResourceBundle resources) {
        }
        public void getData(ObservableList<Bibliotheque.Livre> livres){
            livresData = livres;
        }
    @FXML
    public void erreur() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Erreur.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Erreur");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){System.out.println("raté6");}
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
        String titre = TitreInput.getText();
        String res=ResumeInput.getText();
        String aut=AuteurInput.getText();
        String form=FormatInput.getText();
        String edit=EditeurInput.getText();
        String url=URLInput.getText();
        try{
            int c =Integer.parseInt(ColonneInput.getText());
            int paru=Integer.parseInt(ParutionInput.getText());
            int r =Integer.parseInt(RangeeInput.getText());
            if (c<=5 && c>=1 && r<=7 && r>=1){
                if (TitreInput.getText().isEmpty()){
                    titre= "Titre incconu";
                }
                if (AuteurInput.getText().indexOf(" ")==-1){
                    if (AuteurInput.getText().isEmpty()){
                        aut="auteur inconnu";
                    }
                    else{
                        aut=" "+AuteurInput.getText();
                    }
                }
                if (EditeurInput.getText().isEmpty()){
                    edit="Editeur Inconnu";
                }
                if (FormatInput.getText().isEmpty()){
                    form="Format inconnu";
                }
                if (ResumeInput.getText().isEmpty()){
                    res="résumé vide";
                }
                String[] auteur= aut.split(" ");
                prenom=auteur[0];
                nom=auteur[1];
                Bibliotheque.Livre l1 = new Bibliotheque.Livre();
                Bibliotheque.Livre.Auteur auteur1 = new Bibliotheque.Livre.Auteur();
                auteur1.setNom(nom);
                auteur1.setPrenom(prenom);
                l1.setAuteur(auteur1);
                l1.setTitre(titre);
                l1.setColonne((short)c);
                l1.setParution(paru);
                l1.setPresentation(res);
                l1.setRangee((short) r);
                l1.setEditeur(edit);
                l1.setFormat((form));
                if(pret.isSelected()){
                    l1.setEtat("En Prêt");
                }
                else if(available.isSelected()){
                    l1.setEtat("Disponible");
                }
                else {
                    erreur();
                }
                l1.setURL(url);
                livresData.add(l1);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ajout.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) btvalider.getScene().getWindow();
                stage.close();
            }
            else{
                erreur();
            }
        }
        catch(NumberFormatException | IOException e){
            erreur();
        }

    }
    public void unselectPret(){
        pret.setSelected(false);
    }
    public void unselectDispo(){
        available.setSelected(false);
    }
}
