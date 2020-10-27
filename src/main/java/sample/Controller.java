package sample;

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
import javafx.stage.Stage;
import javafx.stage.Popup;

import javax.annotation.PostConstruct;
import javax.swing.*;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML private javafx.scene.control.MenuItem CloseAppButton;
    @FXML private TableView<Livre> tableBook;
    @FXML private TableColumn<Livre, String> TitleColumn;
    @FXML private TableColumn<Livre, String> AuthorColumn;
    @FXML private TableColumn<Livre, String> ResumeColumn;
    @FXML private TableColumn<Livre, Integer> ColumnColumn;
    @FXML private TableColumn<Livre, Integer> RangeColumn;
    @FXML private TableColumn<Livre, String> ReleaseColumn;
    @FXML private TextField TitleInput;
    @FXML private TextField AuthorInput;
    @FXML private TextField ReleaseInput;
    @FXML private TextField ColumnInput;
    @FXML private TextField LineInput;
    @FXML private TextArea ResumeInput;

    /**
     * Cette méthode permet d'initialiser l'interface ainsi que notre tableau et notre event sur celui-ci.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mise en place des colonnes du tableau
        TitleColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Titre"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Auteur"));
        ResumeColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Resume"));
        ColumnColumn.setCellValueFactory(new PropertyValueFactory<Livre, Integer>("Colonne"));
        RangeColumn.setCellValueFactory(new PropertyValueFactory<Livre, Integer>("Rangee"));
        ReleaseColumn.setCellValueFactory(new PropertyValueFactory<Livre, String>("Parution"));
        //Ajout des livres dans le tableau
        tableBook.setItems(getLivre());
        //Mise en place d'un OnMouseClickedEvent afin d'avoir les données du tableau
        tableBook.setRowFactory(tv -> {
            TableRow<Livre> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(!row.isEmpty()){
                    Livre rowData = row.getItem();
                    TitleInput.setText(rowData.getTitre());
                    AuthorInput.setText(rowData.getAuteur());
                    ReleaseInput.setText(String.valueOf(rowData.getParution()));
                    ColumnInput.setText(String.valueOf(rowData.getColonne()));
                    LineInput.setText(String.valueOf(rowData.getRangee()));
                    ResumeInput.setText(rowData.getResume());
                }
            });
            return row;
        });
    }

    /**
     * getLivres() permet l'ajout dans une liste des livres.
     * return ObservableList<Livre> permet de retourner une liste de livre qui sera ajouter ensuite dans notre TableView.
     */
    public ObservableList<Livre> getLivre(){
        ObservableList<Livre> livres = FXCollections.observableArrayList();
        livres.add(new Livre("Moi, Boy","Roald","Dahl",1984,1,3,"Autobiographie"));
        livres.add(new Livre("Fables de la Fontaine","Jean","de la Fontaine",1668,2,2,"Fables"));
        return livres;
    }

    /**
     * Cette méthode permet d'afficher le menu de confirmation de l'arrêt de l'application.
     * @param event sert à exectuer la méthode se trouvant dans le onAction de notre fichier sample.fxml.
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
}
