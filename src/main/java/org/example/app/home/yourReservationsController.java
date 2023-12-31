package org.example.app.home;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.example.Rezerwacje;
import org.example.User;
import org.example.app.appParent;

import static org.example.Main.*;

/**
 * Klasa {@code yourReservationsController} jest kontrolerem widoku informacji o rezerwacjach aktualnie zalogowanego użytkownika.
 * Odpowiada za obsługę interakcji użytkownika, wyświetlanie informacji, udostępnia funkcje przedłużania i anulowania rezerwacji oraz inicjalizację widoku.
 * Dziedziczy po klasie {@link appParent}, aby działać w kontekście głównego okna aplikacji.
 */
public class yourReservationsController extends appParent {

    @FXML
    private Label Name;

    @FXML
    private Label Wyp;

    @FXML
    private AnchorPane anchor_hire;

    @FXML
    private ImageView avatar;

    @FXML
    private Label labelrezerwacje;

    @FXML
    private Label nametag;

    @FXML
    private AnchorPane anchor;
    @FXML
    private final TableView<Rezerwacje> lista = new TableView<>();
    @FXML
    private final AnchorPane anchortable = new AnchorPane();

    /**
     * Metoda {@code init} inicjalizuje widok ekranu domowego.
     * Ustawia tekst w polu nametag, wczytuje obrazek awatara użytkownika oraz wywołuje metody odpowiedzialne za wyświetlanie obrazków i ustawienie stylu labelglowna.
     *
     * @param imie     imię użytkownika
     * @param nazwisko nazwisko użytkownika
     */
    public void init(String imie, String nazwisko) {
        nametag.setText(imie + " " + nazwisko);
        avatar.setImage(User.getInstance().getImage());
        avatar_view();
        Lista_Hire(User.getInstance().getId());
        Name.setText(imie + " " + nazwisko);
        labelrezerwacje.setStyle("-fx-text-fill:#808080");

    }

    /**
     * Metoda wyświetlająca listę wypożyczeń dla danego identyfikatora.
     *
     * @param id Identyfikator wypożyczenia
     */
    public void Lista_Hire(int id) {
        // Pobranie informacji o rezerwacji z bazy danych
        db_getData.getReservationInformation(id);
        // Tworzenie listy obserwowalnej dla elementów reprezentujących wypożyczenia
        ObservableList<Rezerwacje> items = FXCollections.observableArrayList();

// Tworzenie kolumn dla tabeli
        TableColumn<Rezerwacje, ?> autorCol = new TableColumn<>("Autor");
        autorCol.setMinWidth(anchor_hire.getPrefWidth() * 0.10);
        autorCol.setCellValueFactory(
                new PropertyValueFactory<>("nazwa_autora"));

        TableColumn<Rezerwacje, ?> nazwaCol = new TableColumn<>("Nazwa");
        nazwaCol.setMinWidth(anchor_hire.getPrefWidth() * 0.15);
        nazwaCol.setCellValueFactory(
                new PropertyValueFactory<>("nazwa"));


        TableColumn<Rezerwacje, ?> egzemplarzeCol = new TableColumn<>("Numer egzemplarze");
        egzemplarzeCol.setMinWidth(anchor_hire.getPrefWidth() * 0.05);
        egzemplarzeCol.setCellValueFactory(
                new PropertyValueFactory<>("id_egzemplarze"));

        TableColumn<Rezerwacje, ?> data_koncaCol = new TableColumn<>("Data konca");
        data_koncaCol.setMinWidth(anchor_hire.getPrefWidth() * 0.10);
        data_koncaCol.setCellValueFactory(
                new PropertyValueFactory<>("data_konca"));

        TableColumn<Rezerwacje, ?> data_rezerwacjiCol = new TableColumn<>("Data rezerwacji");
        data_rezerwacjiCol.setMinWidth(anchor_hire.getPrefWidth() * 0.10);
        data_rezerwacjiCol.setCellValueFactory(
                new PropertyValueFactory<>("data_rezerwacji"));

        TableColumn<Rezerwacje, String> przedluz_rezerwacjeCol = new TableColumn<>("Przedluż rezerwacje");
        przedluz_rezerwacjeCol.setMinWidth(anchor_hire.getPrefWidth() * 0.2);
        przedluz_rezerwacjeCol.setCellValueFactory(
                new PropertyValueFactory<>("przedluz_rezerwacje"));

        TableColumn<Rezerwacje, String> anuluj_rezerwacjeCol = new TableColumn<>("Anuluj rezerwacje");
        anuluj_rezerwacjeCol.setMinWidth(anchor_hire.getPrefWidth() * 0.15);
        anuluj_rezerwacjeCol.setCellValueFactory(
                new PropertyValueFactory<>("anuluj_rezerwacje"));

        przedluz_rezerwacjeCol.setCellFactory(col -> {
            TableCell<Rezerwacje, String> cell = new TableCell<>();
            cell.itemProperty().addListener((obs, old, newVal) -> {
                int rowIndex = cell.getIndex();
                if (przedluz_rezerwacjeCol.getCellObservableValue(rowIndex) != null) {
                    Node centreBox = createPriorityGraphic();
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(centreBox));
                    // Obsługa podwójnego kliknięcia w komórkę
                    cell.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                            //Jezeli przekroczono limit przedłużeń
                            if (Integer.parseInt(przedluz_rezerwacjeCol.getCellObservableValue(rowIndex).getValue()) > 2) {
                                Label notificationLabel = new Label("Przekroczono limit przedluzen rezerwacji.");
                                Font pop_r_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"), 18);
                                notificationLabel.setFont(pop_r_h1);
                                notificationLabel.setAlignment(Pos.CENTER);
                                notificationLabel.setPrefSize(300, 50);
                                notificationLabel.setLayoutX(700);
                                notificationLabel.setLayoutY(320);
                                notificationLabel.setStyle("""
                                        -fx-border-radius: 10;
                                            -fx-border-color: #004aad;
                                            -fx-background-radius: 10;
                                            -fx-background-color: NULL;
                                            -fx-border-width: 1;
                                            -fx-text-fill: #004aad;""");
                                Timeline timeline = new Timeline(new KeyFrame(
                                        Duration.seconds(3),
                                        event2 -> notificationLabel.setVisible(false)
                                ));
                                timeline.play();
                                anchor.getChildren().add(notificationLabel);
                            }
                            //Jezeli nie przekroczono limitu przedłużeń
                            else {
                                TablePosition<Rezerwacje, ?> tablePosition = lista.getSelectionModel().getSelectedCells().get(0);
                                int row = tablePosition.getRow();
                                int data = Integer.parseInt((String) egzemplarzeCol.getCellObservableValue(row).getValue());
                                int result = db_updateData.updateReservation(data);
                                Label notificationLabel = new Label();
                                if (result > 0) {
                                    notificationLabel.setText("Przedłużono pomyślnie.");
                                } else {
                                    notificationLabel.setText("Wystąpił błąd. Spróbuj ponownie.");
                                }
                                Font pop_r_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"), 18);
                                notificationLabel.setFont(pop_r_h1);
                                notificationLabel.setAlignment(Pos.CENTER);
                                notificationLabel.setPrefSize(300, 50);
                                notificationLabel.setLayoutX(700);
                                notificationLabel.setLayoutY(320);
                                notificationLabel.setStyle("""
                                        -fx-border-radius: 10;
                                            -fx-border-color: #004aad;
                                            -fx-background-radius: 10;
                                            -fx-background-color: NULL;
                                            -fx-border-width: 1;
                                            -fx-text-fill: #004aad;""");
                                Timeline timeline = new Timeline(new KeyFrame(
                                        Duration.seconds(3),
                                        event2 -> {
                                            notificationLabel.setVisible(false);
                                            yourReservation_clicked(event);
                                        }
                                ));
                                timeline.play();
                                anchor.getChildren().add(notificationLabel);
                            }
                        }
                    });
                }
            });
            return cell;

        });

        // Ustawienie tworzenia komórek dla kolumny "Anuluj rezerwację"
        anuluj_rezerwacjeCol.setCellFactory(col -> {
            TableCell<Rezerwacje, String> cell = new TableCell<>();
            cell.itemProperty().addListener((obs, old, newVal) -> {
                int rowIndex = cell.getIndex();
                if (anuluj_rezerwacjeCol.getCellObservableValue(rowIndex) != null) {
                    Node centreBox = createDeleteGraphic();
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(centreBox));

                    cell.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                            if (anuluj_rezerwacjeCol.getCellData(rowIndex).contentEquals("1")) {
                                TablePosition<Rezerwacje, ?> tablePosition = lista.getSelectionModel().getSelectedCells().get(0);
                                int row = tablePosition.getRow();
                                int data = Integer.parseInt((String) egzemplarzeCol.getCellObservableValue(row).getValue());
                                if (db_deleteData.deleteReservation(data, User.getInstance().getId()) > 0) {
                                    Label notificationLabel = new Label("Anulowano rezerwacje.");
                                    Font pop_r_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"), 18);
                                    notificationLabel.setFont(pop_r_h1);
                                    notificationLabel.setAlignment(Pos.CENTER);
                                    notificationLabel.setPrefSize(300, 50);
                                    notificationLabel.setLayoutX(150);
                                    notificationLabel.setLayoutY(70);
                                    notificationLabel.setStyle("""
                                            -fx-border-radius: 10;
                                                -fx-border-color: #004aad;
                                                -fx-background-radius: 10;
                                                -fx-background-color: NULL;
                                                -fx-border-width: 1;
                                                -fx-text-fill: #004aad;""");
                                    Timeline timeline = new Timeline(new KeyFrame(
                                            Duration.seconds(3),
                                            event2 -> {
                                                notificationLabel.setVisible(false);
                                                labelrezerwacje.fireEvent(event);
                                            }
                                    ));
                                    timeline.play();
                                    anchor.getChildren().add(notificationLabel);
                                } else {
                                    Label notificationLabel = new Label("Nie udalo sie anulowac rezerwacji.");
                                    Font pop_r_h1 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-Regular.ttf"), 18);
                                    notificationLabel.setFont(pop_r_h1);
                                    notificationLabel.setAlignment(Pos.CENTER);
                                    notificationLabel.setPrefSize(300, 50);
                                    notificationLabel.setLayoutX(150);
                                    notificationLabel.setLayoutY(70);
                                    notificationLabel.setStyle("""
                                            -fx-border-radius: 10;
                                                -fx-border-color: #004aad;
                                                -fx-background-radius: 10;
                                                -fx-background-color: NULL;
                                                -fx-border-width: 1;
                                                -fx-text-fill: #004aad;""");
                                    Timeline timeline = new Timeline(new KeyFrame(
                                            Duration.seconds(3),
                                            event2 -> {
                                                notificationLabel.setVisible(false);
                                                labelrezerwacje.fireEvent(event);
                                            }
                                    ));
                                    timeline.play();
                                    anchor.getChildren().add(notificationLabel);
                                }
                            }
                        }
                    });
                }
            });
            return cell;

        });


// Dodanie elementów reprezentujących wypożyczenia do listy
        for (String[] tab : db_getData.rental) {
            String id_egzemplarze = tab[0];
            String nazwa = tab[1];
            String nazwa_autora = tab[2];
            String data_konca = tab[3];
            String data_rezerwacji = tab[4];
            String przedluz_rezerwacje = tab[5];
            String anuluj_rezerwacje = tab[6];

            items.add(new Rezerwacje(id_egzemplarze, nazwa, nazwa_autora, data_konca, data_rezerwacji, przedluz_rezerwacje, anuluj_rezerwacje));
        }

        // Ustawienie danych w liście
        lista.setItems(items);
// Dodanie kolumn do tabeli
        lista.getColumns().addAll(egzemplarzeCol, nazwaCol, autorCol, data_koncaCol, data_rezerwacjiCol, przedluz_rezerwacjeCol, anuluj_rezerwacjeCol);
// Ustawienie komunikatu informującego o braku wyników w liście
        lista.setPlaceholder(new Label("Jesteśmy zaskoczeni, że niczego nie znaleźliśmy! Czyżbyśmy mieli dzień wolny?"));
// Ustawienie preferowanej szerokości i wysokości tabeli
        lista.setPrefWidth(anchor_hire.getPrefWidth());
        lista.setPrefHeight(anchor_hire.getPrefHeight());

        lista.prefWidthProperty().bind(anchor_hire.widthProperty());
        lista.prefHeightProperty().bind(anchor_hire.heightProperty());
        // Dodanie tabeli do kontenera
        anchor_hire.getChildren().addAll(lista);
// Ustawienie kotwic dla tabeli
        AnchorPane.setTopAnchor(lista, 0.0);
        AnchorPane.setLeftAnchor(lista, 0.0);
        AnchorPane.setBottomAnchor(lista, 0.0);
        AnchorPane.setRightAnchor(lista, 0.0);
// Dodanie arkusza stylów do tabeli
        lista.getStylesheets().add("/fxml.home/home.css");

    }

    /**
     * Metoda {@code avatar_view} ustawia clipping dla obrazka avatara, aby uzyskać efekt zaokrąglonych rogów.
     */
    void avatar_view() {
        int radius = 28;
        double centerX = avatar.getBoundsInLocal().getWidth() / 2.0;
        double centerY = avatar.getBoundsInLocal().getHeight() / 2.0;
        Circle clipCircle = new Circle(centerX, centerY, radius);
        avatar.setClip(clipCircle);
    }

    /**
     * Metoda inicjalizująca styl czcionki dla elementów w scenie.
     * Wywołuje również metodę inicjalizującą styl czcionki dla klasy nadrzędnej.
     *
     * @param scene obiekt {@link Scene} reprezentujący scenę JavaFX
     * @see appParent#font(Scene)
     */
    @FXML
    public void font(Scene scene) {
        super.font(scene);
        Font pop_b_h2 = Font.loadFont(getClass().getResourceAsStream("/res/font/Poppins-SemiBold.ttf"), 21);
        Wyp.setFont(pop_b_h2);
        Name.setFont(pop_b_h2);
    }

}