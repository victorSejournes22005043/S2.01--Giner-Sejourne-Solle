package com.example.s201;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvDataReader {
    public List<SeismicEvent> loadData(String csvFilePath) throws IOException {
        List<SeismicEvent> seismicEvents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");  // Les valeurs sont séparées par des virgules dans le dossier csv
                SeismicEvent event = new SeismicEvent();
                event.setIdentifiant(values[0]);
                event.setDate(values[1]);
                event.setHeure(values[2]);
                event.setNom(values[3]);
                event.setRegionEpicentrale(values[4]);
                event.setChoc(values[5]);
                event.setXRgf93L93(Double.parseDouble(values[6]));
                event.setYRgf93L93(Double.parseDouble(values[7]));
                event.setLatitudeWGS84(Double.parseDouble(values[8]));
                event.setLongitudeWGS84(Double.parseDouble(values[9]));
                event.setIntensiteEpicentrale(Integer.parseInt(values[10]));
                event.setQualiteIntensiteEpicentrale(values[11]);
                seismicEvents.add(event);
            }
        }
        return seismicEvents;
    }
    public class SeismicEvent {
        private String identifiant;

        private String date;  // change type from Date to String

        private String heure;
        private String nom;
        private String regionEpicentrale;
        private String choc;
        private double xRgf93L93;
        private double yRgf93L93;
        private double latitudeWGS84;
        private double longitudeWGS84;
        private int intensiteEpicentrale;
        private String qualiteIntensiteEpicentrale;


        // Add getters and setters here
        public void setIdentifiant(String identifiant) {
            this.identifiant = identifiant;
        }

        public void setDate(String date) {
            this.date = date;  // no conversion needed

        }

        public void setHeure(String heure) {
            this.heure = heure;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setRegionEpicentrale(String regionEpicentrale) {
            this.regionEpicentrale = regionEpicentrale;
        }

        public void setChoc(String choc) {
            this.choc = choc;
        }

        public void setXRgf93L93(double xRgf93L93) {
            this.xRgf93L93 = xRgf93L93;
        }

        public void setYRgf93L93(double yRgf93L93) {
            this.yRgf93L93 = yRgf93L93;
        }

        public void setLatitudeWGS84(double latitudeWGS84) {
            this.latitudeWGS84 = latitudeWGS84;
        }

        public void setLongitudeWGS84(double longitudeWGS84) {
            this.longitudeWGS84 = longitudeWGS84;
        }

        public void setIntensiteEpicentrale(int intensiteEpicentrale) {
            this.intensiteEpicentrale = intensiteEpicentrale;
        }

        public void setQualiteIntensiteEpicentrale(String qualiteIntensiteEpicentrale) {
            this.qualiteIntensiteEpicentrale = qualiteIntensiteEpicentrale;
        }
    }

}

