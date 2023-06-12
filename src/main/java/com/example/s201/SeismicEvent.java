package com.example.s201;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeismicEvent {
    private String identifiant;
    private Date date;
    private String nom;
    private String regionEpicentrale;
    private double latitudeWGS84;
    private double longitudeWGS84;
    private Double intensiteEpicentrale;
    private String qualiteIntensiteEpicentrale;

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String dateStr) {
        List<SimpleDateFormat> formatters = new ArrayList<>();
        formatters.add(new SimpleDateFormat("yyyy/MM/dd"));
        formatters.add(new SimpleDateFormat("yyyy/MM"));
        formatters.add(new SimpleDateFormat("yyyy"));

        ParseException lastException = null;
        for (SimpleDateFormat formatter : formatters) {
            try {
                this.date = formatter.parse(dateStr);
                return; // Si l'analyse réussit, sortir de la fonction
            } catch (ParseException e) {
                lastException = e; // Sauvegarder l'exception pour pouvoir la lancer si aucune des dates ne fonctionne
            }
        }
        // Si aucun des formats de date ne fonctionne, lancer une exception
        throw new IllegalArgumentException("Couldn't parse date: " + dateStr, lastException);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRegionEpicentrale() {
        return regionEpicentrale;
    }

    public void setRegionEpicentrale(String regionEpicentrale) {
        this.regionEpicentrale = regionEpicentrale;
    }

    public double getLatitudeWGS84() {
        return latitudeWGS84;
    }

    public void setLatitudeWGS84(double latitudeWGS84) {
        this.latitudeWGS84 = latitudeWGS84;
    }

    public double getLongitudeWGS84() {
        return longitudeWGS84;
    }

    public void setLongitudeWGS84(double longitudeWGS84) {
        this.longitudeWGS84 = longitudeWGS84;
    }

    public Double getIntensiteEpicentrale() {
        return intensiteEpicentrale;
    }

    public void setIntensiteEpicentrale(Double intensiteEpicentrale) {
        this.intensiteEpicentrale = intensiteEpicentrale;
    }

    public String getQualiteIntensiteEpicentrale() {
        return qualiteIntensiteEpicentrale;
    }

    public void setQualiteIntensiteEpicentrale(String qualiteIntensiteEpicentrale) {
        this.qualiteIntensiteEpicentrale = qualiteIntensiteEpicentrale;
    }
}
