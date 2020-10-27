package sample;

import JaxbTests.Bibliotheque;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Popup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;


public class Controller implements Initializable {
    private ObservableList<Bibliotheque.Livre> livres = FXCollections.observableArrayList();
    private int Livreindex;
    @FXML private javafx.scene.control.MenuItem CloseAppButton;
    @FXML private TableView<Bibliotheque.Livre> tableBook;
    @FXML private TableColumn<Bibliotheque.Livre, String> TitreColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> AuteurColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> ResumeColumn;
    @FXML private TableColumn<Bibliotheque.Livre, Integer> ColonneColumn;
    @FXML private TableColumn<Bibliotheque.Livre, Integer> RangeeColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> ParutionColumn;
    @FXML private TextField TitreInput;
    @FXML private TextField AuteurInput;
    @FXML private TextField ParutionInput;
    @FXML private TextField ColonneInput;
    @FXML private TextField RangeeInput;
    @FXML private TextArea ResumeInput;

    /**
     * Cette méthode permet d'initialiser l'interface ainsi que notre tableau et notre event sur celui-ci.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mise en place des colonnes du tableau
        TitreColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Titre"));
        AuteurColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Auteur"));
        ResumeColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Presentation"));
        ColonneColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, Integer>("Colonne"));
        RangeeColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, Integer>("Rangee"));
        ParutionColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Parution"));
        disableInput();
        //Mise en place d'un OnMouseClickedEvent afin d'avoir les données du tableau
        tableBook.setRowFactory(tv -> {
            TableRow<Bibliotheque.Livre> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(!row.isEmpty()){
                    Bibliotheque.Livre rowData = row.getItem();
                    TitreInput.setText(rowData.getTitre());
                    AuteurInput.setText(rowData.getAuteur().getPrenom()+" "+rowData.getAuteur().getNom());
                    ParutionInput.setText(String.valueOf(rowData.getParution()));
                    ColonneInput.setText(String.valueOf(rowData.getColonne()));
                    RangeeInput.setText(String.valueOf(rowData.getRangee()));
                    ResumeInput.setText(rowData.getPresentation());
                    Livreindex = row.getIndex();
                }
            });
            return row;
        });
    }

    /**
     * getLivres(Livre) permet l'ajout d'un livre dans une liste.
     * return ObservableList<Livre> permet de retourner une liste de livre qui sera ajouter ensuite dans notre TableView.
     */
    public ObservableList<Bibliotheque.Livre> getLivre(Bibliotheque.Livre l){
        livres.add(l);
        return livres;
    }

    /**
     * Cette méthode permet d'afficher le menu de confirmation de l'arrêt de l'application.
     * @param event sert à executer la méthode se trouvant dans le onAction de notre fichier sample.fxml.
     * Ce qui permet d'activer la méthode lors d'un clique.
     */
    public void ShowCloseAppMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CloseApp.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Quitter");
            stage.show();

        } catch (Exception e) {
            System.out.println("raté");
        }
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
        String titre = TitreInput.getText();
        String res=ResumeInput.getText();
        String aut=AuteurInput.getText();
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
                if (ResumeInput.getText().isEmpty()){
                    res="résumé vide";
                }
                String[] auteur= aut.split(" ");
                prenom=auteur[1];
                nom=auteur[0];
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

                tableBook.setItems(getLivre(l1));
                disableInput();
                resetInput();
            }
            else{
                erreur();
            }
        }
        catch(NumberFormatException e){
            System.out.println("zebi");
        }

    }

    /**
     * Permet l'ouverture de l'explorateur de fichier afin de choisir un fichier XML
     * @param actionEvent ce paramètre permet l'action de cliquer sur le bouton et d'excuter la méthode.
     */
    @FXML
    public void Open(javafx.event.ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(null);
        loadXMLFile(selectedFile);

    }

    /**
     * Affiche un message d'erreur lorsque les valeurs limites de colonne ou rangée
     */
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
        catch (Exception e){System.out.println("raté");}
    }

    /**
     * Permet d'empêcher l'écriture dans les inputs tant que l'utilisateur n'a pas appuyer sur le bouton Ajouter
     */
    public void disableInput(){
        TitreInput.setDisable(true);
        AuteurInput.setDisable(true);
        ResumeInput.setDisable(true);
        ColonneInput.setDisable(true);
        RangeeInput.setDisable(true);
        ParutionInput.setDisable(true);
    }

    /**
     * Permet l'écriture dans les inputs afin d'ajouter ou modifier un livre
     */
    public void enableInput(){
        TitreInput.setDisable(false);
        AuteurInput.setDisable(false);
        ResumeInput.setDisable(false);
        ColonneInput.setDisable(false);
        RangeeInput.setDisable(false);
        ParutionInput.setDisable(false);
        resetInput();
    }

    /**
     * Permet le reste des inputs afin de ne pas ajouter 2 livres similaires
     */
    public void resetInput(){
        TitreInput.setText("");
        AuteurInput.setText("");
        ParutionInput.setText("");
        ColonneInput.setText("");
        RangeeInput.setText("");
        ResumeInput.setText("");
    }
    public void suppLivre(){
        String titre = TitreInput.getText();
        String paru=ParutionInput.getText();
        String res=ResumeInput.getText();
        String aut=AuteurInput.getText();
        int c =Integer.parseInt(ColonneInput.getText());
        int r =Integer.parseInt(RangeeInput.getText());
        livres.remove(Livreindex);
        resetInput();
    }

    /**
     * Permet d'ouvrir un fichier XML et de compléter le tableau avec les données de celui-ci
     * @param selectedFile permet de récupérer le nom du fichier choisi par l'utilisateur grâce à la méthode Open()
     */
    public void loadXMLFile(File selectedFile) {
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance("JaxbTests");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Bibliotheque bibliotheque = (Bibliotheque) unmarshaller.unmarshal(selectedFile);
            List<Bibliotheque.Livre> list = bibliotheque.getLivre();
            for (int i = 0; i < list.size(); i++) {
                Bibliotheque.Livre livre = list.get(i);
                livres.add(livre);
                tableBook.setItems(livres);
            }
        } catch (Exception e) {
            System.out.println("raté");
        }
    }
}
