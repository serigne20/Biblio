package sample;

import JaxbTests.Bibliotheque;
import JaxbTests.ObjectFactory;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
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
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private ObservableList<Bibliotheque.Livre> livres = FXCollections.observableArrayList();
    private int Livreindex;
    private File selectedFile = null;
    private Connection sql = null;
    private PreparedStatement pst = null;
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
    @FXML private Button ModifButton;
    @FXML private Label CoLabel;
    @FXML private Button DBButton;

    /**
     * Cette méthode permet d'initialiser l'interface ainsi que notre tableau et notre event sur celui-ci.
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
                    }
                    else{
                        available.setSelected(true);
                    }
                    showBookImage(rowData.getURL());
                }
            });
            return row;
        });
    }

    /**
     * getLivres(Livre) permet l'ajout d'un livre dans une liste.
     * return ObservableList<Livre> permet de retourner une liste de livre qui sera ajouter ensuite dans notre TableView.
     */
    public Bibliotheque.Livre getLivreFromIndex(int index){
        return livres.get(index);
    }
    public void showBookImage(String url){
        Image image = new Image(url);
        if(image.isError()){
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
    public void showAjoutLivre(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ajout.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Ajout d'un Livre");
            AjoutController ajoutController = fxmlLoader.getController();
            ajoutController.getData(livres);
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        }
    }
    public void showModifLivre(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/livre.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Modification d'un Livre");
            ModifController modifController = fxmlLoader.getController();
            modifController.getData(livres,Livreindex);
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        }
    }
    /**
     * A quoi sert la méthode ? la méthode est l'action de cliquer sur le bouton about us.
     * Elle permet d'afficher une autre vue s'appellant Trombinoscope. Elle affiche la photo des développeurs
     * @param Event à quoi sert le paramètre de la méthode ? C'est l'évenement qui permet de cliquer sur le bouton About us
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
     * Permet l'ouverture de l'explorateur de fichier afin de choisir un fichier XML
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
     * Affiche un message d'erreur lorsque les valeurs limites de colonne ou rangée
     */

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
        pret.setDisable(true);
        available.setDisable(true);
        EditeurInput.setDisable(true);
        FormatInput.setDisable(true);
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
        pret.setDisable(false);
        available.setDisable(false);
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
        EditeurInput.setText("");
        FormatInput.setText("");
        ResumeInput.setText("");
        pret.setSelected(false);
        available.setSelected(false);
    }
    public void suppLivre(){
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
            tableBook.getItems().clear();
            for (int i = 0; i < list.size(); i++) {
                Bibliotheque.Livre livre = list.get(i);
                livres.add(livre);
                tableBook.setItems(livres);
            }
            CoLabel.setText("Deconnecté");
            CoLabel.setTextFill(Color.web("#FC0000"));
            DBButton.setDisable(false);
        } catch (Exception e) {
            System.out.println("raté2");
        }
    }

    /**
     *
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
     *
     */
    public void Save(){
        saveXMLFile(selectedFile);
    }

    /**
     *
     */
    public void SaveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.XML"));
        selectedFile = fileChooser.showSaveDialog(null);
        saveXMLFile(selectedFile);
    }
    public void saveWord(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("docx files", "*.DOCX"));
        File wordLivre = fileChooser.showSaveDialog(null);
        word(wordLivre);
    }
    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr((CTPPrGeneral) ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    public void word(File wordLivre) {
        try{
            XWPFDocument doc = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(wordLivre);
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun ru = paragraph.createRun();
            ru.setItalic(true);
            ru.setBold(true);
            ru.setFontSize(50);
            ru.setTextPosition(100);
            ru.setText("Gestionnaire d'une Bibliothèque");
            ru.addBreak(BreakType.PAGE);

            // the body content
            doc.createTOC();
            addCustomHeadingStyle(doc, "heading 1", 1);
            addCustomHeadingStyle(doc, "heading 2", 2);
            addCustomHeadingStyle(doc, "heading 3", 3);
            XWPFParagraph sommaire = doc.createParagraph();
            sommaire.setStyle("heading 1");
            CTP ctP = sommaire.getCTP();
            CTSimpleField toc = ((CTP)ctP).addNewFldSimple();
            toc.setInstr("TOC \\h");
            toc.setDirty(STOnOff.EQUAL);
            XWPFRun srun = sommaire.createRun();
            for(int i=0;i<livres.size();i++) {
                srun.setText(livres.get(i).getTitre());
                srun.addBreak();
            }
            srun.addBreak(BreakType.PAGE);
            for(int i=0;i<livres.size();i++){
                XWPFParagraph title = doc.createParagraph();
                XWPFParagraph book = doc.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                book.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                XWPFRun bookRun = book.createRun();
                titleRun.setFontSize(20);
                titleRun.setBold(true);
                titleRun.setUnderline(UnderlinePatterns.SINGLE);
                titleRun.setText("Livre "+ (i+1) + " :");
                titleRun.addBreak();
                InputStream is;
                is = new URL(livres.get(i).getURL()).openStream();
                bookRun.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, livres.get(i).getURL(), Units.toEMU(150), Units.toEMU(150)); // 150x150 pixels
                bookRun.addBreak();
                bookRun.setFontSize(14);
                bookRun.setTextPosition(20);
                bookRun.setText("Titre : "+ livres.get(i).getTitre());
                bookRun.addBreak();
                bookRun.setText("Parution : "+ livres.get(i).getParution());
                bookRun.addBreak();
                bookRun.setText("Résumé : "+ livres.get(i).getPresentation());
                bookRun.addBreak();
                bookRun.setText("Colonne : "+ livres.get(i).getColonne());
                bookRun.addBreak();
                bookRun.setText("Rangée : "+ livres.get(i).getRangee());
                bookRun.addBreak();
                bookRun.setText("Etat : "+ livres.get(i).getEtat());
                bookRun.addBreak();
                bookRun.setText("Image URL : "+ livres.get(i).getURL());
                if(i!=livres.size()-1) bookRun.addBreak(BreakType.PAGE);

            }
            doc.write(out);
            out.close();

        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("ok");
    }
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
    public void handleDBConnection(){
        sql = DBConnection.SQLConnection();
        if(sql!=null) {
            CoLabel.setText("Connecté");
            CoLabel.setTextFill(Color.web("#00FF00"));
            DBButton.setDisable(true);
        }
        /*String query = "SELECT * FROM livre";
        try {
            pst = sql.prepareStatement(query);
            ResultSet resp = pst.executeQuery();
            while(resp.next()) {
                Bibliotheque.Livre respLivre = new Bibliotheque.Livre();
                Bibliotheque.Livre.Auteur respAut= new Bibliotheque.Livre.Auteur();
                respLivre.setTitre(resp.getString("titre"));
                respAut.setNom(resp.getString("nomaut"));
                respAut.setPrenom(resp.getString("prenomaut"));
                respLivre.setAuteur(respAut);
                respLivre.setParution((short)resp.getInt("parution"));
                respLivre.setColonne((short)resp.getInt("colonne"));
                respLivre.setRangee((short)resp.getInt("rangee"));
                respLivre.setPresentation(resp.getString("res"));
                respLivre.setEtat(resp.getString("dispo"));
                respLivre.setURL(resp.getString("url"));
                respLivre.setEditeur(resp.getString("edition"));
                respLivre.setFormat(resp.getString("format"));
                livres.add(respLivre);
                System.out.println(resp.getString("titre"));
            }
            tableBook.setItems(livres);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("query did not work");
        }*/
    }
}
