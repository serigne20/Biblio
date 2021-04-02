package sample.ViewControllers;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Others.UtilsFunction;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjoutController{
    private ObservableList<Bibliotheque.Livre> livresData;
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
    @FXML private javafx.scene.control.Button btvalider;
    public void initialize(URL location, ResourceBundle resources) {
        }

    /**
     * Récupération des données envoyées par le Controller principal
     * @param livres liste des livres du tableau
     * @param connected valeur de connection
     * @param sql connection à la base de données SQL Server
     */
        public void getData(ObservableList<Bibliotheque.Livre> livres, boolean connected, Connection sql){
            livresData = livres;
            isConnected = connected;
            sqlCo = sql;
        }
    /**
     * Validation du formulaire d'ajout et affichage dans le tableau.
     * {'UtilsFuntcion': {@link UtilsFunction#verifyUnicity(ObservableList, Bibliotheque.Livre)}} Vérification de l'unicité
     * @param Event évenement de cliquer sur le bouton
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
            if (c<=5 && c>=1 && r<=7 && r>=1 && pret.isSelected() || available.isSelected()){
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
                if (EditeurInput.getText().isEmpty()){
                    edit="Editeur Inconnu";
                }
                if (FormatInput.getText().isEmpty()){
                    form="Format inconnu";
                }
                if (ResumeInput.getText().isEmpty()){
                    res="Résumé vide";
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
                l1.setColonne((short) c);
                l1.setParution(paru);
                l1.setPresentation(res);
                l1.setRangee((short) r);
                l1.setEditeur(edit);
                l1.setFormat(form);
                if (pret.isSelected()) {
                    l1.setEtat("En Prêt");
                } else if (available.isSelected()) {
                    l1.setEtat("Disponible");
                } else {
                    utils.erreur();
                }
                if (url.contains("http")) {
                    l1.setURL(url);
                }
                else{
                    try {
                        l1.setURL(getClass().getResource("/fxml/Photos/livreinconnu.jpg").toURI().toString());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                if(utils.verifyUnicity(livresData,l1)) {
                    if (isConnected) {
                        String etat = "";
                        if (pret.isSelected()) {
                            etat = "En Prêt";
                        } else if (available.isSelected()) {
                            etat = "Disponible";
                        } else {
                            utils.erreur();
                        }
                        String query = "INSERT INTO livre (titre, nomaut, prenomaut, parution, colonne, rangee, res," +
                                "dispo, edition, format, url) VALUES('" +
                                titre + "', '" +
                                nom + "', '" +
                                prenom + "', " +
                                paru + ", " +
                                c + ", " +
                                r + ", '" +
                                res + "', '" +
                                etat + "', '" +
                                edit + "', '" +
                                form + "', '" +
                                url + "')";

                        pst = sqlCo.prepareStatement(query);
                        int resp = pst.executeUpdate();
                        if (resp == 1) {
                            System.out.println("query worked");
                        } else {
                            System.out.println("query did not work");
                        }
                        utils.selectQuery(sqlCo, livresData);
                    }
                    else{
                        livresData.add(l1);
                    }
                }
                else{
                    System.out.println("Problème d'unicité du Livre");
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ajout.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = (Stage) btvalider.getScene().getWindow();
                stage.close();
            }
            else{
                utils.erreur();
            }
        }
        catch(NumberFormatException | IOException | SQLException e){
            utils.erreur();
        }

    }

    /**
     * Enleve la selection de "En Prêt"
     */
    public void unselectPret(){
        pret.setSelected(false);
    }
    /**
     * Enleve la selection de "Disponible"
     */
    public void unselectDispo(){
        available.setSelected(false);
    }
}
