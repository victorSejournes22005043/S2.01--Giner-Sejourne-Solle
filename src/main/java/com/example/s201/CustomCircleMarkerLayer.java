package com.example.s201;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/** Affiche un point rouge sur la carte */
public class CustomCircleMarkerLayer extends MapLayer {

    private ArrayList<MapPoint> LmapPoint = new ArrayList<MapPoint>();
    private ArrayList<Circle> Lcircle = new ArrayList<Circle>();

    /* La fonction est appelée à chaque rafraichissement de la carte */
    @Override
    protected void layoutLayer() {
        //triTaille();
        System.out.println(LmapPoint.size());
        for (int i = 0; i<LmapPoint.size();++i){
            /* Conversion du MapPoint vers Point2D */
            Point2D point2d = this.getMapPoint(LmapPoint.get(i).getLatitude(), LmapPoint.get(i).getLongitude());

            /* Déplace le cercle selon les coordonnées du point */
            if (!(point2d == null)) {
                Lcircle.get(i).setTranslateX(point2d.getX());
                Lcircle.get(i).setTranslateY(point2d.getY());
            }
        }
    }
    public void triTaille(){

        ArrayList<MapPoint> newLMapPoints = new ArrayList<MapPoint>();
        ArrayList<Circle> newLCircle = new ArrayList<Circle>();

        for (int i = 6; i>1; --i){

            for (int j = 0; j<Lcircle.size(); ++j){
                if (Lcircle.get(j).getRadius() == i){
                    newLCircle.add(Lcircle.get(j));
                    newLMapPoints.add(LmapPoint.get(j));

                }
            }
        }
        Lcircle = newLCircle;
        LmapPoint = newLMapPoints;

    }
    public void addPoint(MapPoint mapPoint,Color color,double taille){
        LmapPoint.add(mapPoint);
        /* Cercle rouge de taille 5 */
        Circle circle = new Circle(taille, color);
        circle.setOpacity(0.5);
        this.Lcircle.add(circle);

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }
    public void reset(){
        LmapPoint.clear();
        Lcircle.clear();
    }
}