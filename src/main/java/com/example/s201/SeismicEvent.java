package com.example.s201;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SeismicEvent {
    private String identifiant;
    private Date date;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            this.date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
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

    public String getChoc() {
        return choc;
    }

    public void setChoc(String choc) {
        this.choc = choc;
    }

    public double getXRgf93L93() {
        return xRgf93L93;
    }

    public void setXRgf93L93(double xRgf93L93) {
        this.xRgf93L93 = xRgf93L93;
    }

    public double getYRgf93L93() {
        return yRgf93L93;
    }

    public void setYRgf93L93(double yRgf93L93) {
        this.yRgf93L93 = yRgf93L93;
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

    public int getIntensiteEpicentrale() {
        return intensiteEpicentrale;
    }

    public void setIntensiteEpicentrale(int intensiteEpicentrale) {
        this.intensiteEpicentrale = intensiteEpicentrale;
    }

    public String getQualiteIntensiteEpicentrale() {
        return qualiteIntensiteEpicentrale;
    }

    public void setQualiteIntensiteEpicentrale(String qualiteIntensiteEpicentrale) {
        this.qualiteIntensiteEpicentrale = qualiteIntensiteEpicentrale;
    }
}
