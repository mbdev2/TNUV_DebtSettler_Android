package com.example.debtsettler_v3.model;

public class Items {

    String id;
    String name;
    String opis;
    boolean isSelected;


    public Items(String id, String name, String opis, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.opis = opis;
        this.isSelected = isSelected;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean equalsId(String id) {
        return id == this.id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
