package com.example.s201;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public static MapView map = new MapView();

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

        System.out.println("test");
    }

}