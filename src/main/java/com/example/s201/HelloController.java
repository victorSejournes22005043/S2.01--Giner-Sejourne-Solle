package com.example.s201;
import com.example.s201.CSVManager;
import com.example.s201.SeismicEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

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

    public static TableColumn<SeismicEvent, String> column1 = new TableColumn<SeismicEvent, String>("identifiant");
    public static TableColumn<SeismicEvent, String> column2 = new TableColumn<SeismicEvent, String>("date");
    public static TableColumn<SeismicEvent, String> column3 = new TableColumn<SeismicEvent, String>("nom");
    public static TableColumn<SeismicEvent, String> column4 = new TableColumn<SeismicEvent, String>("region");
    public static TableColumn<SeismicEvent, String> column5 = new TableColumn<SeismicEvent, String>("latitude");
    public static TableColumn<SeismicEvent, String> column6 = new TableColumn<SeismicEvent, String>("longitude");
    public static TableColumn<SeismicEvent, String> column7 = new TableColumn<SeismicEvent, String>("intensite");
    public static TableColumn<SeismicEvent, String> column8 = new TableColumn<SeismicEvent, String>("qualiteIntensiteEpicentrale");


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

    public void initMap() {
        map.addEventFilter(MouseEvent.ANY, event -> event.consume());
        map.addEventFilter(ScrollEvent.ANY, event -> event.consume());
        MapPoint mapPoint = new MapPoint(46.727638, 2.213749);
        map.setZoom(5.1);
        map.flyTo(0, mapPoint, 0.1);
    }

    public void initTab() {
        TableView<SeismicEvent> table = new TableView<SeismicEvent>();

        column1.setMinWidth(111);
        column2.setMinWidth(111);
        column3.setMinWidth(111);
        column4.setMinWidth(111);
        column5.setMinWidth(111);
        column6.setMinWidth(111);
        column7.setMinWidth(111);
        column8.setMinWidth(111);

        tab.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8);
    }

    public void editTab(){
        column1.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        column2.setCellValueFactory(new PropertyValueFactory<>("date"));
        column3.setCellValueFactory(new PropertyValueFactory<>("nom"));
        column4.setCellValueFactory(new PropertyValueFactory<>("regionEpicentrale"));
        column5.setCellValueFactory(new PropertyValueFactory<>("latitudeWGS84"));
        column6.setCellValueFactory(new PropertyValueFactory<>("longitudeWGS84"));
        column7.setCellValueFactory(new PropertyValueFactory<>("intensiteEpicentrale"));
        column8.setCellValueFactory(new PropertyValueFactory<>("qualiteIntensiteEpicentrale"));
        for(int i = 0; i<CSVManager.eventArr.size(); ++i){
            tab.getItems().add(CSVManager.eventArr.get(i));
        }
    }

    @FXML
    public void carte() {
        borderPane.setCenter(map);
    }

    @FXML
    public void tableau() {
        borderPane.setCenter(tab);
    }

    @FXML
    public void graphique() {
        updateChart();
    }

    private Map<String, Integer> countEventsPerDate(List<SeismicEvent> events) {
        Map<String, Integer> counts = new TreeMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        for (SeismicEvent event : events) {
            String year = formatter.format(event.getDate());

            // Récupérer l'année de début de la tranche de 10 ans
            int startYear = Integer.parseInt(year.substring(0, 3) + "0");

            // Récupérer l'année de fin de la tranche de 10 ans
            int endYear = startYear + 9;

            // Construire la clé pour la tranche de 10 ans
            String range = startYear + "-" + endYear;

            // Ajouter l'événement à la tranche correspondante
            counts.put(range, counts.getOrDefault(range, 0) + 1);
        }

        return counts;
    }

    private PieChart createIntensityPieChart(List<SeismicEvent> events) {
        PieChart pieChart = new PieChart();
        int[] rangeCounts = new int[4];  // Compteurs pour chaque tranche d'intensité

        for (SeismicEvent event : events) {
            double intensity = event.getIntensiteEpicentrale();
            if (intensity < 2.5) {
                rangeCounts[0]++;
            } else if (intensity < 5) {
                rangeCounts[1]++;
            } else if (intensity < 7.5) {
                rangeCounts[2]++;
            } else {
                rangeCounts[3]++;
            }
        }

        PieChart.Data slice1 = new PieChart.Data("0-2.5", rangeCounts[0]);
        PieChart.Data slice2 = new PieChart.Data("2.5-5", rangeCounts[1]);
        PieChart.Data slice3 = new PieChart.Data("5-7.5", rangeCounts[2]);
        PieChart.Data slice4 = new PieChart.Data("7.5-10", rangeCounts[3]);

        pieChart.getData().addAll(slice1, slice2, slice3, slice4);

        pieChart.setTitle("Répartition des intensités");

        // Afficher le nom du secteur à l'intérieur du camembert
        for (PieChart.Data data : pieChart.getData()) {
            Tooltip.install(data.getNode(), new Tooltip(data.getName()));
        }

        return pieChart;
    }

    @FXML
    public void vider() {
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
    public void rechercher() {
        // Reset l'erreur avant une nouvelle recherche
        erreur.setText("");
        messageErreur.setText("");

        // Restaure les données à leur état original
        //CSVManager.eventArr = new ArrayList<>(CSVManager.originalEvents);

        // Le reste de votre logique de recherche...

        List<SeismicEvent> filteredEvents = new ArrayList<>(CSVManager.eventArr);

        // Recherche par coordonnées et rayon si renseignés
        /*if (!coordX.getText().isEmpty() && !coordY.getText().isEmpty() && !rayon.getText().isEmpty()) {
            double lat = Double.parseDouble(coordX.getText());
            double lon = Double.parseDouble(coordY.getText());
            double radius = Double.parseDouble(rayon.getText());
            filteredEvents = filterEventsByCoordinatesAndRadius(filteredEvents, lat, lon, radius);
        }*/

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

        // Actualiser les données dans CSVManager avec les événements filtrés
        CSVManager.setEvents(filteredEvents);

        // Actualiser l'affichage en fonction du composant actuellement affiché dans le borderPane
        if (borderPane.getCenter() instanceof TableView) {
            updateTable();
        } else if (borderPane.getCenter() instanceof BarChart) {
            updateChart();
        }
        //CSVManager.updateCSV(Double.parseDouble(coordX.getText()), Double.parseDouble(coordY.getText()), Double.parseDouble(rayon.getText()));
    }
    private void updateChart() {
        List<SeismicEvent> events = CSVManager.eventArr;
        Map<String, Integer> countsPerDate = countEventsPerYear(events);

        // Convertir la Map de Date à String
        Map<String, Integer> countsPerDateString = new TreeMap<>();
        for (Map.Entry<String, Integer> entry : countsPerDate.entrySet()) {
            countsPerDateString.put(entry.getKey(), entry.getValue());
        }

        BarChart<String, Number> chart = createChart(countsPerDateString);
        PieChart pieChart = createIntensityPieChart(events);

        // Créez un GridPane pour afficher les deux graphiques côte à côte
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);  // Espacement horizontal entre les colonnes

        // Ajoutez le camembert et son tableau des valeurs
        gridPane.add(pieChart, 0, 0);  // Camembert dans la première colonne, première ligne

        // Créez le tableau des valeurs du camembert
        VBox valuesTable = createValuesTable(pieChart);
        gridPane.add(valuesTable, 0, 1);  // Tableau des valeurs dans la première colonne, deuxième ligne

        // Ajoutez le graphique bâton
        gridPane.add(chart, 1, 0, 1, 2);  // Graphique bâton dans la deuxième colonne, occupe deux lignes

        borderPane.setCenter(gridPane);
    }


    private VBox createValuesTable(PieChart pieChart) {
        VBox vbox = new VBox();
        vbox.setSpacing(5);  // Espacement vertical entre les éléments

        // Parcourir les données du camembert et créer une étiquette pour chaque secteur
        for (PieChart.Data data : pieChart.getData()) {
            String label = data.getName() + " (" + data.getPieValue() + ")";
            Label valueLabel = new Label(label);
            valueLabel.setTextFill(Color.BLACK);
            vbox.getChildren().add(valueLabel);
        }

        return vbox;
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
        editTab();

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

        // Limiter à maximum 10 bâtons si le nombre de données est supérieur à 10
        int maxBars = 10;
        if (counts.size() <= maxBars) {
            yAxis.setTickUnit(1);
        } else {
            yAxis.setTickUnit(Math.ceil((double) counts.size() / maxBars));
        }

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