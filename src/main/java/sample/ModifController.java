package sample;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class ModifController {
    private ObservableList<Bibliotheque.Livre> livresData;
    private int index;
    private boolean isConnected;
    private Connection sqlCo = null;
    private PreparedStatement pst = null;
    private UtilsFunction utils = new UtilsFunction();
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
    @FXML private ImageView bookURL;
    @FXML private javafx.scene.control.Button btvalider;
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void getData(ObservableList<Bibliotheque.Livre> livres, int LivreIndex, Connection sql, boolean connected){
        livresData = livres;
        index = LivreIndex;
        TitreInput.setText(livres.get(LivreIndex).getTitre());
        AuteurInput.setText(livres.get(LivreIndex).getAuteur().getPrenom()+" "+livres.get(LivreIndex).getAuteur().getNom());
        ParutionInput.setText(String.valueOf(livres.get(LivreIndex).getParution()));
        ColonneInput.setText(String.valueOf(livres.get(LivreIndex).getColonne()));
        RangeeInput.setText(String.valueOf(livres.get(LivreIndex).getRangee()));
        EditeurInput.setText(String.valueOf(livres.get(LivreIndex).getEditeur()));
        FormatInput.setText(String.valueOf(livres.get(LivreIndex).getFormat()));
        ResumeInput.setText(livres.get(LivreIndex).getPresentation());
        if(livres.get(LivreIndex).getEtat() == "En Prêt"){
            pret.setSelected(true);
        }
        else{
            available.setSelected(true);
        }
        URLInput.setText(livres.get(LivreIndex).getURL());
        if(URLInput.getText().isEmpty()){
            URLInput.setText("@Photos/livreinconnu.jpg");
            showBookImage(URLInput.getText());
        }
        else{
            showBookImage(livres.get(LivreIndex).getURL());
        }
    }
    public void erreur() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Erreur.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Erreur");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){System.out.println("raté");}
    }
    public void modifLivre(){
        Bibliotheque.Livre l = new Bibliotheque.Livre();
        String prenom, nom= "";
        String titre = TitreInput.getText();
        String res=ResumeInput.getText();
        String aut=AuteurInput.getText();
        String url=URLInput.getText();
        String form=FormatInput.getText();
        String edit=EditeurInput.getText();
        try{
            int c =Integer.parseInt(ColonneInput.getText());
            int paru=Integer.parseInt(ParutionInput.getText());
            int r =Integer.parseInt(RangeeInput.getText());
            if (c<=5 && c>=1 && r<=7 && r>=1){
                if (TitreInput.getText().isEmpty()){
                    titre= "Titre inconnu";
                }
                if (AuteurInput.getText().indexOf(" ")==-1){
                    if (AuteurInput.getText().isEmpty()){
                        aut="Auteur inconnu";
                    }
                    else{
                        aut=" "+AuteurInput.getText();
                    }
                }
                if (ResumeInput.getText().isEmpty()){
                    res="Résumé vide";
                }
                if (EditeurInput.getText().isEmpty()){
                    edit="Editeur Inconnu";
                }
                if (FormatInput.getText().isEmpty()){
                    form="Format inconnu";
                }
                String[] auteur= aut.split(" ");
                prenom=auteur[0];
                nom=auteur[1];
                Bibliotheque.Livre.Auteur auteur1 = new Bibliotheque.Livre.Auteur();
                auteur1.setNom(nom);
                auteur1.setPrenom(prenom);
                l.setAuteur(auteur1);
                l.setTitre(titre);
                l.setColonne((short)c);
                l.setParution(paru);
                l.setPresentation(res);
                l.setRangee((short) r);
                l.setFormat(form);
                l.setEditeur(edit);
                if(pret.isSelected()){
                    l.setEtat("En Prêt");
                }
                else if(available.isSelected()){
                    l.setEtat("Disponible");
                }
                l.setURL(url);
                livresData.set(index,l);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/livre.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) btvalider.getScene().getWindow();
                stage.close();
                //tableBook.setItems(getLivre2(l));
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
    public void showBookImage(String url) {
        Image image = new Image(url);
        if (image.isError()) {
            image = new Image("/Photos/livreinconnu.jpg");
        }
        bookURL.setImage(image);
    }
}
