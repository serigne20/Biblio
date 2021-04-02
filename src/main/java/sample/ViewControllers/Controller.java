package sample.ViewControllers;

import JaxbTests.Bibliotheque;
import JaxbTests.ObjectFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import sample.Others.DBConnection;
import sample.Others.UtilsFunction;
import sample.Others.Word;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<Bibliotheque.Livre> livres = FXCollections.observableArrayList();
    private int Livreindex;
    private File selectedFile = null;
    private Connection sql = null;
    private PreparedStatement pst = null;
    private  boolean connected = false;
    private UtilsFunction utils = new UtilsFunction();
    private Word word = new Word();
    private DBConnection dbConnection = new DBConnection();
    @FXML private javafx.scene.control.MenuItem CloseAppButton;
    @FXML private TableView<Bibliotheque.Livre> tableBook;
    @FXML private TableColumn<Bibliotheque.Livre, String> TitreColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> AuteurColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> ResumeColumn;
    @FXML private TableColumn<Bibliotheque.Livre, Integer> ColonneColumn;
    @FXML private TableColumn<Bibliotheque.Livre, Integer> RangeeColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> ParutionColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> EtatColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> EditeurColumn;
    @FXML private TableColumn<Bibliotheque.Livre, String> FormatColumn;
    @FXML private TextField TitreInput;
    @FXML private TextField AuteurInput;
    @FXML private TextField ParutionInput;
    @FXML private TextField ColonneInput;
    @FXML private TextField RangeeInput;
    @FXML private TextField EditeurInput;
    @FXML private TextField FormatInput;
    @FXML private TextArea ResumeInput;
    @FXML private RadioButton pret;
    @FXML private RadioButton available;
    @FXML private ImageView bookURL;
    @FXML private Button AjoutButton;
    @FXML private Button SuppButton;
    @FXML private Button ModifButton;
    @FXML private Label CoLabel;
    @FXML private Button DBButton;

    /**
     * Cette méthode permet d'initialiser l'interface ainsi que notre tableau et notre event sur celui-ci.
     * Desactivation des boutons afin d'obliger l'utilisateur a choisir un fichier XML pour lancer le processus
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mise en place des colonnes du tableau
        TitreColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Titre"));
        AuteurColumn.setCellValueFactory(livre ->{
            SimpleObjectProperty property = new SimpleObjectProperty();
            property.setValue(livre.getValue().getAuteur().getPrenom() + " " + livre.getValue().getAuteur().getNom());
            return property;
        });
        ResumeColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Presentation"));
        ColonneColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, Integer>("Colonne"));
        RangeeColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, Integer>("Rangee"));
        ParutionColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Parution"));
        EditeurColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Editeur"));
        FormatColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Format"));
        EtatColumn.setCellValueFactory(new PropertyValueFactory<Bibliotheque.Livre, String>("Etat"));
        disableInput();
        ModifButton.setDisable(true);
        AjoutButton.setDisable(true);
        SuppButton.setDisable(true);
        DBButton.setDisable(true);
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
                    EditeurInput.setText(String.valueOf(rowData.getEditeur()));
                    FormatInput.setText(String.valueOf(rowData.getFormat()));
                    ResumeInput.setText(rowData.getPresentation());
                    Livreindex = row.getIndex();
                    ModifButton.setDisable(false);
                    if(rowData.getEtat() == "En Prêt"){
                        pret.setSelected(true);
                        available.setSelected(false);
                    }
                    else{
                        pret.setSelected(false);
                        available.setSelected(true);
                    }
                    if (rowData.getURL().contains("http")) {
                        showBookImage(rowData.getURL());
                    }
                    else{
                        try {
                            rowData.setURL(getClass().getResource("/fxml/Photos/livreinconnu.jpg").toURI().toString());
                            showBookImage(rowData.getURL());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return row;
        });
    }

    /**
     * getLivreFromIndex(int index) permet de retourner un Livre en de son index donné en paramètre
     * @param index index du livre
     * @return un livre en fonction de son index
     */
    public Bibliotheque.Livre getLivreFromIndex(int index){
        return livres.get(index);
    }

    /**
     * Fonction permettant d'afficher une image en fonction de l'url passer en paramètre
     * Si l'image reçoit un url non valide, nous affichons une erreur
     * Dans l'autre cas nous affichons cette image dans l'ImageView de notre interface JavaFX
     * @param url Paramètre permettant de choisir une image via un url sur internet
     */
    public void showBookImage(String url){
            Image image = new Image(url);
            if (image.isError()) {
                System.out.println("erreur");
            }
            bookURL.setImage(image);
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
     * Cette fonction permet l'affichage de l'interface d'ajout d'un livre.
     * Elle permet aussi de passer au Controller d'Ajout la connexion à la base de données et les livres de notre tableau.
     * @param event sert à executer la méthode se trouvant dans le onAction de notre fichier sample.fxml.
     */
    public void showAjoutLivre(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ajout.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Ajout d'un Livre");
            AjoutController ajoutController = fxmlLoader.getController();
            ajoutController.getData(livres, connected,sql);
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        }
    }

    /**
     * Cette fonction permet l'affichage de l'interface de modification d'un livre.
     * Elle permet aussi de passer au Controller de Modification
     * la connexion à la base de données, les livres de notre tableau ainsi que l'index du livre à modifier.
     * @param event sert à executer la méthode se trouvant dans le onAction de notre fichier sample.fxml.
     */
    public void showModifLivre(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/livre.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Modification d'un Livre");
            ModifController modifController = fxmlLoader.getController();
            modifController.getData(livres,Livreindex,sql,connected);
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        }
    }
    /**
     * Cette fonction permet d'afficher une autre vue s'appellant Trombinoscope.
     * Elle affiche la photo des développeurs
     * @param Event évenement qui permet de cliquer sur le bouton About us
     */
    public void AboutUs (ActionEvent Event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/trombi.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Trombinoscope");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.println("raté");
        }
    }

    /**
     * Permet l'ouverture de l'explorateur de fichier afin de choisir un fichier XML.
     * Il sera ensuite charger par la fonction loadXMLFile()
     * {@link #loadXMLFile(File)}
     * @param actionEvent ce paramètre permet l'action de cliquer sur le bouton et d'excuter la méthode.
     */
    @FXML
    public void Open(javafx.event.ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.XML"));
        selectedFile = fileChooser.showOpenDialog(null);
        loadXMLFile(selectedFile);
    }

    /**
     * Permet d'empêcher l'écriture dans les inputs
     */
    public void disableInput(){
        TitreInput.setDisable(true);
        AuteurInput.setDisable(true);
        ResumeInput.setDisable(true);
        ColonneInput.setDisable(true);
        RangeeInput.setDisable(true);
        ParutionInput.setDisable(true);
        pret.setDisable(true);
        available.setDisable(true);
        EditeurInput.setDisable(true);
        FormatInput.setDisable(true);
    }
    /**
     * Permet de vider les inputs pour l'affichage des livres
     */
    public void resetInput(){
        TitreInput.setText("");
        AuteurInput.setText("");
        ParutionInput.setText("");
        ColonneInput.setText("");
        RangeeInput.setText("");
        EditeurInput.setText("");
        FormatInput.setText("");
        ResumeInput.setText("");
        pret.setSelected(false);
        available.setSelected(false);
    }

    /**
     * Cette méthode permet de supprimer un livre dans la base de données si nous sommes connectés via une requête SQL DELETE.
     * Dans le cas contraire elle pourra aussi supprimer un livre en le supprimant de notre tableau.
     */
    public void suppLivre(){
        if(connected){
                dbConnection.deleteQuery(sql,livres,Livreindex);
                dbConnection.selectQuery(sql,livres);
        }
        else{
            livres.remove(Livreindex);
        }
        resetInput();
    }

    /**
     * Permet d'ouvrir un fichier XML et de compléter le tableau avec les données de celui-ci.
     * Utilisation de Jaxb afin de remplir nos objets Bibliotheque et Livre via des fichiers XML.
     * Passage en mode non connecté lors du chargement du fichier XML.
     * @param selectedFile permet de récupérer le nom du fichier choisi par l'utilisateur grâce à la méthode Open()
     */
    public void loadXMLFile(File selectedFile) {
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance("JaxbTests");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Bibliotheque bibliotheque = (Bibliotheque) unmarshaller.unmarshal(selectedFile);
            List<Bibliotheque.Livre> list = bibliotheque.getLivre();
            tableBook.getItems().clear();
            for (int i = 0; i < list.size(); i++) {
                Bibliotheque.Livre livre = list.get(i);
                livres.add(livre);
                tableBook.setItems(livres);
            }
            AjoutButton.setDisable(false);
            SuppButton.setDisable(false);
            CoLabel.setText("Deconnecté");
            CoLabel.setTextFill(Color.web("#FC0000"));
            DBButton.setDisable(false);
            connected = false;
        } catch (Exception e) {
            System.out.println("raté2");
        }
    }

    /**
     * Sauvegarde du fichier XML précedemment ouvert dans l'application
     * Utilisation de Jaxb afin de transformer nos objets Bibliotheque et Livre sous la forme de balise XML.
     * @param selectedFile
     */
    public void saveXMLFile(File selectedFile) {
        JAXBContext jc = null;
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            Bibliotheque bibliotheque = (Bibliotheque) objectFactory.createBibliotheque();
            List listlivres = bibliotheque.getLivre();
            jc = JAXBContext.newInstance("JaxbTests");
            for (int i = 0; i < livres.size(); i++) {
                Bibliotheque.Livre l1 = getLivreFromIndex(i);
                listlivres.add(l1);
            }
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(bibliotheque, selectedFile);
        } catch (Exception e) {
            System.out.println("raté save");
        }
    }

    /**
     * Cette methode se lance lors de l'appui du bouton Save dans le Menu de notre Applcation.
     * {@link #saveXMLFile(File)}
     */
    public void Save(){
        saveXMLFile(selectedFile);
    }

    /**
     * Ouvre l'explorateur de fichier afin de sauvegarder-sous le fichier XML voulu.
     *{@link #saveXMLFile(File)}
     */
    public void SaveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.XML"));
        selectedFile = fileChooser.showSaveDialog(null);
        saveXMLFile(selectedFile);
    }

    /**
     * Ouvre l'explorateur de fichier afin de sauvegarder le fichier Word lors d'un export.
     * @param event
     */
    public void saveWord(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("docx files", "*.DOCX"));
        File wordLivre = fileChooser.showSaveDialog(null);
        word.exportWord(wordLivre,livres);
    }



    /**
     * Permet la connection à la base de données SQL Server et l'affichage du mode connecté.
     */
    public void handleDBConnection(){
        sql = DBConnection.SQLConnection();
        if(sql!=null) {
            CoLabel.setText("Connecté");
            CoLabel.setTextFill(Color.web("#00FF00"));
            DBButton.setDisable(true);
            connected = true;
        }
    }

    /**
     * Affichage d'une fenêtre après la connection à la base de données
     * L'utilisateur peux choisir entre "Ecraser" les données du tableau actuel par la base de données
     * et "Sychronisation" pour ajouter les livres d'un fichier XML dans la base de données
     * @param event
     */
    public void checkDBSync(ActionEvent event){
        try {
            handleDBConnection();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Synchronisation Base de données");
            stage.setScene(new Scene(root1));
            stage.show();
            SyncController sync = fxmlLoader.getController();
            sync.getData(sql,livres);
        }
        catch (Exception e){
            System.out.println("raté");
        }
    }
}
