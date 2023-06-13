package com.example.s201;

import java.io.*;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CSVManager {
    public static List<List<String>> records = new ArrayList<List<String>>();
    public static ArrayList<SeismicEvent> eventArr = new ArrayList<SeismicEvent>();
    public static ArrayList<SeismicEvent> eventArr = new ArrayList<SeismicEvent>();
    public static List<SeismicEvent> originalEvents = new ArrayList<>();
    public static void setEvents(List<SeismicEvent> events) {
        eventArr.clear();
        eventArr.addAll(events);
    }
    public static void readCSV(){
        try (CSVReader csvReader = new CSVReader(new FileReader(HelloController.path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<SeismicEvent> eventArr = new ArrayList<SeismicEvent>();
        for(int i = 1; i< records.size(); ++i){
            eventArr.add(new SeismicEvent());
            eventArr.get(i-1).setDate(records.get(i).get(1));
            eventArr.get(i-1).setNom(records.get(i).get(3));
            eventArr.get(i-1).setRegionEpicentrale(records.get(i).get(4));
            if(records.get(i).get(8) != ""){
                eventArr.get(i-1).setLatitudeWGS84(Double.parseDouble(records.get(i).get(8)));
            }
            if(records.get(i).get(9) != ""){
                eventArr.get(i-1).setLongitudeWGS84(Double.parseDouble(records.get(i).get(9)));
            }
            if(records.get(i).get(10) != ""){
                eventArr.get(i-1).setIntensiteEpicentrale(Double.parseDouble(records.get(i).get(10)));
            }
            eventArr.get(i-1).setQualiteIntensiteEpicentrale(records.get(i).get(11));
            //seismicEvents.add(event);

        }

        System.out.println(eventArr);

    }

    public static double getDistance(double x1,double y1,double x2, double y2){
        double somme = (x1-x2)*(x1-x2) - (y1-y2)*(y1-y2);
        return Math.sqrt(somme);
    }

    public static void updateCSV(Double x, Double y, Double rayon) {
        eventArr.clear();
        for(int i = 1; i< records.size(); ++i) {
            try {
                if (getDistance(x, y, Double.parseDouble(records.get(i).get(8)), Double.parseDouble(records.get(i).get(9))) <= rayon) {
                    eventArr.add(new SeismicEvent());
                    System.out.println(eventArr.size());
                    /*eventArr.get(i - 1).setIdentifiant(records.get(i).get(0));
                    eventArr.get(i - 1).setDate(records.get(i).get(1));
                    eventArr.get(i - 1).setNom(records.get(i).get(3));
                    eventArr.get(i - 1).setRegionEpicentrale(records.get(i).get(4));
                    if (records.get(i).get(8) != "") {
                        eventArr.get(i - 1).setLatitudeWGS84(Double.parseDouble(records.get(i).get(8)));
                    }
                    if (records.get(i).get(9) != "") {
                        eventArr.get(i - 1).setLongitudeWGS84(Double.parseDouble(records.get(i).get(9)));
                    }
                    if (records.get(i).get(10) != "") {
                        eventArr.get(i - 1).setIntensiteEpicentrale(Double.parseDouble(records.get(i).get(10)));
                    }
                    eventArr.get(i - 1).setQualiteIntensiteEpicentrale(records.get(i).get(11));
                    //seismicEvents.add(event);*/
                }
            }catch (NumberFormatException e) {
                eventArr.add(new SeismicEvent());
                System.out.println("La chaîne de caractères n'est pas un nombre valide.");
            }

        }

    }

}
