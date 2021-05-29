package com.example.debtsettler_v3.model;

public class Nakupi {
    Integer idNakupa;
    String imeTrgovine;
    String opisNakupa;
    String datumNakupa;
    String cenaNakupa;
    String avtor;
    String barvaUp;

    public Nakupi(Integer idNakupa, String imeTrgovine, String opisNakupa, String datumNakupa, String cenaNakupa, String avtor, String barvaUp){
        this.idNakupa=idNakupa;
        this.imeTrgovine=imeTrgovine;
        this.opisNakupa=opisNakupa;
        this.datumNakupa=datumNakupa;
        this.cenaNakupa=cenaNakupa;
        this.avtor=avtor;
        this.barvaUp=barvaUp;
    }

    public Integer getIdNakupa() { return idNakupa; }

    public void setIdNakupa(Integer id) { this.idNakupa = id; }

    public String getImeTrgovine() { return imeTrgovine; }

    public void setImeTrgovine(String imeTrgovine) { this.imeTrgovine=imeTrgovine; }

    public String getOpisNakupa() { return opisNakupa; }

    public void setOpisNakupa(String opisNakupa) { this.opisNakupa=opisNakupa; }

    public String getDatumNakupa() { return datumNakupa; }

    public void setDatumNakupa(String datumNakupa) { this.datumNakupa=datumNakupa; }

    public String getCenaNakupa() { return cenaNakupa; }

    public void setCenaNakupa(String cenaNakupa) { this.cenaNakupa=cenaNakupa; }

    public String getAvtor() { return avtor; }

    public void setAvtor(String avtor) { this.avtor=avtor; }

    public String getBarvaUp() { return barvaUp; }

    public void setBarvaUp(String barvaUp) { this.barvaUp=barvaUp; }
}

