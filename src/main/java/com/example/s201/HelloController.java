package com.example.s201;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.List;


import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

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
    }

    public void initTab() {
        TableView<SeismicEvent> table = new TableView<SeismicEvent>();

        TableColumn<SeismicEvent, String> column1 = new TableColumn<SeismicEvent, String>("identifiant");
        TableColumn<SeismicEvent, String> column2 = new TableColumn<SeismicEvent, String>("date");
        TableColumn<SeismicEvent, String> column3 = new TableColumn<SeismicEvent, String>("nom");
        TableColumn<SeismicEvent, String> column4 = new TableColumn<SeismicEvent, String>("regionEpicentrale");
        TableColumn<SeismicEvent, String> column5 = new TableColumn<SeismicEvent, String>("latitudeWGS84");
        TableColumn<SeismicEvent, String> column6 = new TableColumn<SeismicEvent, String>("longitudeWGS84");
        TableColumn<SeismicEvent, String> column7 = new TableColumn<SeismicEvent, String>("intensiteEpicentrale");
        TableColumn<SeismicEvent, String> column8 = new TableColumn<SeismicEvent, String>("qualiteIntensiteEpicentrale");
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
        updateChart();
    }

    private void updateChart() {
        List<SeismicEvent> events = CSVManager.eventArr;
        Map<String, Integer> counts = countEventsPerYear(events);
        BarChart<String, Number> chart = createChart(counts);
        borderPane.setCenter(chart);
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
    private List<SeismicEvent> filterEventsByCoordinatesAndRadius(List<SeismicEvent> events, double lat, double lon, double radius) {
        List<SeismicEvent> filteredEvents = new ArrayList<>();

        for (SeismicEvent event : events) {
            double distance = calculateDistance(lat, lon, event.getLatitudeWGS84(), event.getLongitudeWGS84());

            if (distance <= radius) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }
    private List<SeismicEvent> filterEventsByDate(List<SeismicEvent> events, Date startDate, Date endDate) {
        List<SeismicEvent> filteredEvents = new ArrayList<>();

        for (SeismicEvent event : events) {
            if (event.getDate().after(startDate) && event.getDate().before(endDate)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }
    //...
    @FXML
    public void rechercher(){
        // Reset l'erreur avant une nouvelle recherche
        erreur.setText("");
        messageErreur.setText("");

        // Restaure les données à leur état original
        CSVManager.eventArr = new ArrayList<>(CSVManager.originalEvents);

        // Le reste de votre logique de recherche...

        List<SeismicEvent> filteredEvents = new ArrayList<>(CSVManager.eventArr);

        // Recherche par coordonnées et rayon si renseignés
        if (!coordX.getText().isEmpty() && !coordY.getText().isEmpty() && !rayon.getText().isEmpty()) {
            double lat = Double.parseDouble(coordX.getText());
            double lon = Double.parseDouble(coordY.getText());
            double radius = Double.parseDouble(rayon.getText());
            filteredEvents = filterEventsByCoordinatesAndRadius(filteredEvents, lat, lon, radius);
        }

        // Recherche par date si renseignées
        // Recherche par date si renseignées
        if (debut.getValue() != null && fin.getValue() != null) {
            // Conversion des valeurs de DatePicker en java.util.Date
            Date startDate = java.util.Date.from(debut.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = java.util.Date.from(fin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Vérifier si la date de début est avant la date de fin
            if (startDate.after(endDate)) {
                erreur.setText("Message d'erreur :");
                messageErreur.setText("La date de début doit être avant la date de fin");
                return;  // Si la date de début est après la date de fin, on quitte la méthode
            } else {
                filteredEvents = filterEventsByDate(filteredEvents, startDate, endDate);
            }
        }
        // Si aucune recherche n'a été effectuée
        if (coordX.getText().isEmpty() && coordY.getText().isEmpty() && rayon.getText().isEmpty() && debut.getValue() == null && fin.getValue() == null) {
            erreur.setText("Message d'erreur :");
            messageErreur.setText("Aucun critère de recherche renseigné");
        } else {
            CSVManager.setEvents(filteredEvents); // utilisez la méthode définie précédemment

            if (borderPane.getCenter() instanceof BarChart) {
                updateChart();
            } else if (borderPane.getCenter() instanceof TableView) {
                updateTable();
            }

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
    public BarChart<String, Number> createChart(Map<String, Integer> counts) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Année");
        yAxis.setLabel("Nombre de séismes");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        return barChart;
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers

        return distance;
    }
    private List<SeismicEvent> filterEvents(List<SeismicEvent> events, double lat, double lon, double radius, Date startDate, Date endDate) {
        List<SeismicEvent> filteredEvents = new ArrayList<>();

        for (SeismicEvent event : events) {
            double distance = calculateDistance(lat, lon, event.getLatitudeWGS84(), event.getLongitudeWGS84());

            if (distance <= radius && event.getDate().after(startDate) && event.getDate().before(endDate)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }
}