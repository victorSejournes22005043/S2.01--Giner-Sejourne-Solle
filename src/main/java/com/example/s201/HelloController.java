package com.example.s201;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    public static MapView map = new MapView();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMap();
    }

    public void initMap(){
        map.addEventFilter(MouseEvent.ANY, event -> event.consume());
        map.addEventFilter(ScrollEvent.ANY, event -> event.consume());
        MapPoint mapPoint = new MapPoint(46.727638, 2.213749);
        map.setZoom(5.1);
        map.flyTo(0, mapPoint, 0.1);
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

            // dans le cas où toutes les valeurs sont non null

        }
        else {

            // dans le cas où une valeur est null

            erreur.setText("Message d'erreur :");
            messageErreur.setText("Information vide");
        }
    }



}