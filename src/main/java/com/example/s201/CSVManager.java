package com.example.s201;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.IOException;


public class CSVManager {
    public static List<List<String>> records = new ArrayList<List<String>>();
    public static ArrayList<SeismicEvent> eventArr = new ArrayList<SeismicEvent>();
    public static List<SeismicEvent> originalEvents = new ArrayList<>();
    public static void setEvents(List<SeismicEvent> events) {
        eventArr.clear();
        eventArr.addAll(events);
    }
    public static void readCSV(){
        try (CSVReader csvReader = new CSVReader(new FileReader(SisutController.path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i = 1; i< records.size(); ++i){
            eventArr.add(new SeismicEvent());
            eventArr.get(i-1).setIdentifiant(records.get(i).get(0));
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
        }

        System.out.println(eventArr);

    }
}