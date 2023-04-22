package org.example.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.Egzemplarze;
import org.example.Katalog;
import org.example.Main;
import org.example.verify.logincontroller;

import java.io.IOException;

import static org.example.Main.dbload;

public class katalog_itemcontroller extends home{

    @FXML
    public void font() {
        Font ssp_sb_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/SourceSerifPro-SemiBold.ttf"),25);
        Font pop_r_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"),18);
        Font pop_r_h2 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"),14);
        Font pop_b_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-SemiBold.ttf"),20);
        Font pop_b_h2 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-SemiBold.ttf"),14);
        nametag.setFont(ssp_sb_h1);
        labelbiblioteka.setFont(pop_b_h2);
        labelglowna.setFont(pop_b_h2);
        labelkatalog.setFont(pop_b_h2);
        labelkontakt.setFont(pop_b_h2);
        labelkategorie.setFont(pop_b_h2);
        labelnowosci.setFont(pop_b_h2);
        labelrezerwacje.setFont(pop_b_h2);
        labelwypozyczenia.setFont(pop_b_h2);
        searchbar.setFont(pop_r_h1);
        title_book.setFont(ssp_sb_h1);
        author_book.setFont(pop_r_h1);
        isbn_book.setFont(pop_r_h1);
        publisher_book.setFont(pop_r_h1);
        year_book.setFont(pop_r_h1);
    }

    @FXML
    private AnchorPane anchortable;

    @FXML
    private Label author_book;

    @FXML
    private ImageView avatar;
    @FXML
    private Label background_tytul;
    @FXML
    private ImageView cover_book;

    @FXML
    private Label isbn_book;

    @FXML
    private Label labelbiblioteka;

    @FXML
    private Label labelglowna;

    @FXML
    private Label labelkatalog;

    @FXML
    private Label labelkategorie;

    @FXML
    private Label labelkontakt;

    @FXML
    private Label labelnowosci;

    @FXML
    private Label labelrezerwacje;

    @FXML
    private Label labelwypozyczenia;

    @FXML
    private ImageView logout;

    @FXML
    private VBox menubox;

    @FXML
    private Label nametag;

    @FXML
    private Label publisher_book;

    @FXML
    private TextField searchbar;

    @FXML
    private AnchorPane table;

    @FXML
    private Label title_book;

    @FXML
    private Label year_book;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Egzemplarze> lista = new TableView<Egzemplarze>();

    void load(int id){ //ladujemy po lp. wiersza z tabeli
        String[] tab = dbload.array.get(id);
        if(tab[0]!=null){
            author_book.setText("Autor: "+tab[2]);
            title_book.setText("\""+tab[1]+"\"");
            year_book.setText("Rok wydania: "+tab[3]);
            publisher_book.setText("Wydawnictwo: "+tab[8]);
            isbn_book.setText("ISBN: "+tab[5]);
            String text = tab[1].substring(0,8);
            background_tytul.setText(text);
            egzemplarz_lista(id+1);
        }
    }

    @FXML
    void searchbar_exited(MouseEvent event) {

    }

    public void init(String imie, String nazwisko, MouseEvent event, Image image) {
        nametag.setText(imie + " " + nazwisko);
        searchbar_exited(event);
        avatar.setImage(Main.user.getImage());
        cover_book.setImage(Main.kat.getOkladka());
        avatar_view();
        lista.setPlaceholder(new Label("Jesteśmy zaskoczeni, że niczego nie znaleźliśmy! Czyżbyśmy mieli dzień wolny?"));
        cover_view();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0); //TODO tabela bez zmian
    }

    void avatar_view() {
        int radius = 28;
        double centerX = avatar.getBoundsInLocal().getWidth() / 2.0;
        double centerY = avatar.getBoundsInLocal().getHeight() / 2.0;
        Circle clipCircle = new Circle(centerX, centerY, radius);
        avatar.setClip(clipCircle);
    }

    public void egzemplarz_lista(int id) {
        dbload.print_copies(id);
        ObservableList<Egzemplarze> items = FXCollections.observableArrayList();

        TableColumn nazwaCol = new TableColumn("Nazwa");
        nazwaCol.setMinWidth(anchortable.getPrefWidth()*0.25);
        nazwaCol.setCellValueFactory(
                new PropertyValueFactory<>("nazwa"));
        //idCol.setVisible(false);

        TableColumn nrCol = new TableColumn("Numer egzemplarza");
        nrCol.setMinWidth(anchortable.getPrefWidth()*0.15);
        nrCol.setCellValueFactory(
                new PropertyValueFactory<>("id_egzemplarze"));

        TableColumn lokalizacjaCol = new TableColumn("Lokalizacja");
        lokalizacjaCol.setMinWidth(anchortable.getPrefWidth()*0.25);
        lokalizacjaCol.setCellValueFactory(
                new PropertyValueFactory<>("lokalizacja"));

        TableColumn dostepneCol = new TableColumn("Czy dostępne");
        dostepneCol.setMinWidth(anchortable.getPrefWidth()*0.1);
        dostepneCol.setCellValueFactory(
                new PropertyValueFactory<>("czy_dostepne"));

        TableColumn zwrotCol = new TableColumn("Data zwrotu");
        zwrotCol.setMinWidth(anchortable.getPrefWidth()*0.25);
        zwrotCol.setCellValueFactory(
                new PropertyValueFactory<>("data_zwrotu"));

        /*TableColumn isbnCol = new TableColumn("ISBN");//todo guzik wypozyczenia ksiazki; aktywny jak czydost = T
        isbnCol.setMinWidth(anchortable.getPrefWidth()*0.2);
        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("isbn"));*/

        for (String[] tab : dbload.copy) {
            String nazwa = tab[0];
            Integer id_egzemplarze = Integer.valueOf(tab[1]);
            String lokalizacja = tab[2];
            String czy_dostepne = tab[3];
            String data_zwrotu = tab[4];
            items.add(new Egzemplarze(nazwa,id_egzemplarze,lokalizacja,czy_dostepne,data_zwrotu));
        }
        //Dodaj wartości do kolumn
        if(items!=null) {
            lista.setItems(items);
        }
        //Dodaj kolumny do tabeli
        lista.getColumns().addAll(nazwaCol,nrCol,lokalizacjaCol,dostepneCol,zwrotCol);
        // Ustaw preferowaną wielkość TableView na zgodną z AnchorPane
        lista.setPrefWidth(anchortable.getPrefWidth());
        lista.setPrefHeight(anchortable.getPrefHeight());
        // Powiąż preferowane wielkości TableView i AnchorPane
        lista.prefWidthProperty().bind(anchortable.widthProperty());
        lista.prefHeightProperty().bind(anchortable.heightProperty());
        // Dodaj TableView do AnchorPane
        anchortable.getChildren().addAll(lista);
        // Ustaw parametry kotwiczenia TableView na wartość 0
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchortable, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchortable, 0.0);
        //dodaj css
        lista.getStylesheets().add("/fxml.home/home.css");


        /*lista.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                TablePosition tablePosition = lista.getSelectionModel().getSelectedCells().get(0);
                int row = tablePosition.getRow();
                Integer data = (Integer) idCol.getCellObservableValue(row).getValue();
                System.out.println(data);
                katalog_item(event,data,row);//get data
            }
        });*/
    }

    void cover_view() {
        double centerX = cover_book.getBoundsInLocal().getWidth();
        double centerY = cover_book.getBoundsInLocal().getHeight();
        Rectangle rectangle = new Rectangle(centerX, centerY);
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        cover_book.setClip(rectangle);
    }
}
