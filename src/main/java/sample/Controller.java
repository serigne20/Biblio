package sample;

import JaxbTests.Bibliotheque;
import JaxbTests.Bibliotheque.Livre;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Au début de la classe, appelle les differents attributs à récupérer pour
 * les méthodes qui vont suivre. C'est le fx:id qui permet de les identifier dans
 * le fichier fxml
 */
public class Controller implements Initializable {
    @FXML private TextField TitreInput;
    @FXML private TextField AuteurInput;
    @FXML private TextField ParutionInput;
    @FXML private TextField ColonneInput;
    @FXML private TextField RangeeInput;
    @FXML private TextArea ResumeInput;
    @FXML private javafx.scene.control.MenuItem CloseAppButton;
    @FXML private TableView<Livre> tableBook;
    @FXML private TableColumn<Livre, String> TitreColumn;
    @FXML private TableColumn<Livre, String> AuteurColumn;
    @FXML private TableColumn<Livre, String> ResumeColumn;
    @FXML private TableColumn<Livre, Integer> ColonneColumn;
    @FXML private TableColumn<Livre, Integer> RangeeColumn;
    @FXML private TableColumn<Livre, String> ParutionColumn;
    private ObservableList<Livre> livres = FXCollections.observableArrayList();
    private int Livreindex;


    /**
     * cette méthode permet d'intialisez l'interface, le tableau et les évenenements liés à celui-ci
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        // Mise en place des colonnes du tableau
        TitreColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Titre"));
        AuteurColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Auteur"));
        ResumeColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Presentation"));
        ColonneColumn.setCellValueFactory(new PropertyValueFactory<Livre, Integer>("Colonne"));
        RangeeColumn.setCellValueFactory(new PropertyValueFactory<Livre, Integer>("Rangee"));
        ParutionColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Parution"));
        //Ajout des Livres dans le tableau
        //Mise en place d'un OnMouseClickedEvent afin d'avoir les données du tableau
        tableBook.setRowFactory(tv -> {
            TableRow<Livre> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(!row.isEmpty()){
                    Livre rowData = row.getItem();
                    TitreInput.setText(rowData.getTitre());
                    AuteurInput.setText(rowData.getAuteur().getPrenom()+" "+rowData.getAuteur().getNom());
                    ParutionInput.setText(String.valueOf(rowData.getParution()));
                    ColonneInput.setText(String.valueOf(rowData.getColonne()));
                    RangeeInput.setText(String.valueOf(rowData.getRangee()));
                    ResumeInput.setText(rowData.getPresentation());
                }
            });
            return row;
        });
    }

    public ObservableList<Livre> getLivre(Livre l) {
        livres.add(l);
        return livres;
    }

    /**
     * Lorsqu'on clique sur le bouton "Quit" dans l'onglet "File une vue s'affiche, cette vue nous demande
     * de quitter ou non
     * @param event
     */
    public void ShowCloseAppMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/CloseApp.fxml"));
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
     * a méthode est l'action de cliquer sur le bouton about us.
     * Elle permet d'afficher une autre vue s'appellant Trombinoscope. Elle affiche la photo des développeurs
     * @param Event C'est l'évenement de cliquer sur le bouton About us
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
     * Cette méthode permet de valider le formulaire, elle respecte le constructeur Livre
     * et si des informations manquent, elle remplit automatiquement les champs.
     * elle met à jour les données dans le tableau.
     * @param Event C'est l'évenement de cliquer sur le bouton "Valider"
     */
    @FXML
    private void validerLivre (ActionEvent Event){
        String prenom, nom= "";
        String titre = TitreInput.getText();
        String res=ResumeInput.getText();
        String aut=AuteurInput.getText();
        try{int c =Integer.parseInt(ColonneInput.getText());
            int r =Integer.parseInt(RangeeInput.getText());
            int paru=Integer.parseInt(ParutionInput.getText());
            if (c<=5 && c>=1 && r<=7 && r>=1){
                if (TitreInput.getText().isEmpty()){
                    titre= "Titre inconnu";
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
                Livre.Auteur auteur1 = new Livre.Auteur();
                auteur1.setNom(nom);
                auteur1.setPrenom(prenom);
                Bibliotheque.Livre l1 = new Livre ();
                l1.setTitre(titre);
                l1.setAuteur(auteur1);
                l1.setColonne((short)c);
                l1.setParution(paru);
                l1.setPresentation(res);
                l1.setRangee((short)r);
                tableBook.setItems(getLivre(l1));
                System.out.println(l1.toString());
            }
            else{
                this.erreur();
            }
        }
        catch(NumberFormatException e) {
            this.erreur();
        }
    }

    /**
     * méthode permet d'afficher une vue de message d'erreur, vue = Erreur.fxml
     */
    @FXML
    public void erreur() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/Erreur.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Erreur");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("raté");
        }

    }


    /**
     * Ouvre un explorateur de fichier et ne sélectionne que les fichiers XML lorsqu'on clique sur
     * l'onglet Edit puis OpenFile
     * @param actionEvent Le fait de cliquer sur le bouton Open File
     */
    @FXML
    public void Open(javafx.event.ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(null);
        this.loadXMLFile(selectedFile);

    }

    public void loadXMLFile(File file) {
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance("JaxbTests");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Bibliotheque bibliotheque = (Bibliotheque) unmarshaller.unmarshal(file);
            List<Livre> list = bibliotheque.getLivre();
            for (int i = 0; i < list.size(); i++) {
                Bibliotheque.Livre livre = list.get(i);
                livres.add(livre);
                tableBook.setItems(livres);
            }
        } catch (Exception e) {
            System.out.println("raté");
        }
    }

    public void disableInput() {
        this.TitreInput.setDisable(true);
        this.AuteurInput.setDisable(true);
        this.ResumeInput.setDisable(true);
        this.ColonneInput.setDisable(true);
        this.RangeeInput.setDisable(true);
        this.ParutionInput.setDisable(true);
    }

    public void enableInput() {
        this.TitreInput.setDisable(false);
        this.AuteurInput.setDisable(false);
        this.ResumeInput.setDisable(false);
        this.ColonneInput.setDisable(false);
        this.RangeeInput.setDisable(false);
        this.ParutionInput.setDisable(false);
        this.resetInput();
    }

    public void resetInput() {
        this.TitreInput.setText("");
        this.AuteurInput.setText("");
        this.ParutionInput.setText("");
        this.ColonneInput.setText("");
        this.RangeeInput.setText("");
        this.ResumeInput.setText("");
    }

    public void suppLivre() {
        String titre = this.TitreInput.getText();
        String paru = this.ParutionInput.getText();
        String res = this.ResumeInput.getText();
        String aut = this.AuteurInput.getText();
        int c = Integer.parseInt(this.ColonneInput.getText());
        int r = Integer.parseInt(this.RangeeInput.getText());
        new Livre();
        this.livres.remove(this.Livreindex);
        this.resetInput();
    }
}