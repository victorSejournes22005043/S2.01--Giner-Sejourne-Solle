package com.example.s201;
import com.example.s201.CSVManager;
import com.example.s201.SeismicEvent;

import com.gluonhq.maps.MapLayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import javax.swing.*;
import java.io.File;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    public MapView map = new MapView();
    @FXML
    public TableView tab = new TableView();

    @FXML
    public BorderPane borderPane = new BorderPane();

    @FXML
    public TextField rayon = new TextField();
    @FXML
    public TextField coordX = new TextField();
    @FXML
    public TextField coordY = new TextField();

    @FXML
    public Label erreur = new Label();
    @FXML
    public Label messageErreur = new Label();

    @FXML
    public DatePicker debut = new DatePicker();
    @FXML
    public DatePicker fin = new DatePicker();

    public FileChooser fileChooser = new FileChooser();

    public static String path;

    private void updateTable() {
        // Supprime toutes les données précédentes
        tab.getItems().clear();

        // Ajoute les nouvelles données
        for (SeismicEvent event : CSVManager.eventArr) {
            tab.getItems().add(event);
        }
    }
    public static TableColumn<SeismicEvent, String> column1 = new TableColumn<SeismicEvent, String>("identifiant");
    public static TableColumn<SeismicEvent, String> column2 = new TableColumn<SeismicEvent, String>("date");
    public static TableColumn<SeismicEvent, String> column3 = new TableColumn<SeismicEvent, String>("nom");
    public static TableColumn<SeismicEvent, String> column4 = new TableColumn<SeismicEvent, String>("region");
    public static TableColumn<SeismicEvent, String> column5 = new TableColumn<SeismicEvent, String>("latitude");
    public static TableColumn<SeismicEvent, String> column6 = new TableColumn<SeismicEvent, String>("longitude");
    public static TableColumn<SeismicEvent, String> column7 = new TableColumn<SeismicEvent, String>("intensite");
    public static TableColumn<SeismicEvent, String> column8 = new TableColumn<SeismicEvent, String>("qualiteIntensiteEpicentrale");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMap();
        initTab();
    }

    public void initMap(){
        map.addEventFilter(MouseEvent.ANY, event -> event.consume());
        map.addEventFilter(ScrollEvent.ANY, event -> event.consume());
        MapPoint mapPoint = new MapPoint(46.727638, 2.213749);
        map.setZoom(5.1);
        map.flyTo(0, mapPoint, 0.1);
        map.setOnMouseClicked(event -> {
            MapPoint pos = map.getMapPosition(event.getX(),event.getY());
            setPoint(pos);
            coordX.setText(String.valueOf(pos.getLatitude()));
            coordY.setText(String.valueOf(pos.getLongitude()));
                }
        );
    }

    public void setPoint(MapPoint point){
        MapLayer layer = new CustomCircleMarkerLayer(point);
        map.addLayer(layer);
    }

    public void initTab() {
        TableColumn<SeismicEvent, String> column1 = new TableColumn<>("identifiant");
        TableColumn<SeismicEvent, String> column2 = new TableColumn<>("date");
        TableColumn<SeismicEvent, String> column3 = new TableColumn<>("nom");
        TableColumn<SeismicEvent, String> column4 = new TableColumn<>("regionEpicentrale");
        TableColumn<SeismicEvent, String> column5 = new TableColumn<>("latitudeWGS84");
        TableColumn<SeismicEvent, String> column6 = new TableColumn<>("longitudeWGS84");
        TableColumn<SeismicEvent, String> column7 = new TableColumn<>("intensiteEpicentrale");
        TableColumn<SeismicEvent, String> column8 = new TableColumn<>("qualiteIntensiteEpicentrale");
        column1.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        column2.setCellValueFactory(new PropertyValueFactory<>("date"));
        column3.setCellValueFactory(new PropertyValueFactory<>("nom"));
        column4.setCellValueFactory(new PropertyValueFactory<>("regionEpicentrale"));
        column5.setCellValueFactory(new PropertyValueFactory<>("latitudeWGS84"));
        column6.setCellValueFactory(new PropertyValueFactory<>("longitudeWGS84"));
        column7.setCellValueFactory(new PropertyValueFactory<>("intensiteEpicentrale"));
        column8.setCellValueFactory(new PropertyValueFactory<>("qualiteIntensiteEpicentrale"));
        tab.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8);
    }

    @FXML
    public void carte(){
        borderPane.setCenter(map);
    }
    @FXML
    public void tableau(){
        borderPane.setCenter(tab);
    }
    @FXML
    public void graphique(){
        borderPane.setCenter(null);
    }

    @FXML
    public void vider(){
        rayon.clear();
        coordX.clear();
        coordY.clear();
        erreur.setText("");
        messageErreur.setText("");
        debut.setValue(null);
        fin.setValue(null);
    }

    @FXML
    public void rechercher(){
        if (!coordX.getText().isEmpty()
        && !coordY.getText().isEmpty()
        && !rayon.getText().isEmpty()
        && !(debut.getValue() == null)
        && !(fin.getValue() == null)){

        // Le reste de votre logique de recherche...

        List<SeismicEvent> filteredEvents = new ArrayList<>(CSVManager.eventArr);

        // Recherche par coordonnées et rayon si renseignés
        if (!coordX.getText().isEmpty() && !coordY.getText().isEmpty() && !rayon.getText().isEmpty()) {
            double lat = Double.parseDouble(coordX.getText());
            double lon = Double.parseDouble(coordY.getText());
            double radius = Double.parseDouble(rayon.getText());
            filteredEvents = filterEventsByCoordinatesAndRadius(filteredEvents, lat, lon, radius);
        }
        else {

            // dans le cas où une valeur est null

            erreur.setText("Message d'erreur :");
            messageErreur.setText("Information vide");
        }
    }

    @FXML
    private void openCSV() throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(new JFrame());

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        System.out.println(path);
        CSVManager.readCSV();

        CSVManager.originalEvents = new ArrayList<>(CSVManager.eventArr);  // Faites une copie des données initiales

        // Ajoutez ce code pour mettre à jour le graphique :
        if (borderPane.getCenter() instanceof BarChart) {
            updateChart();
        }

    }


    private Map<String, Integer> countEventsPerYear(List<SeismicEvent> events) {
        Map<String, Integer> counts = new TreeMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        for (SeismicEvent event : events) {
            String year = formatter.format(event.getDate());
            counts.put(year, counts.getOrDefault(year, 0) + 1);
        }

        return counts;
    }

}