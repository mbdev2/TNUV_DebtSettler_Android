package com.example.debtsettler_v3.model;

import android.os.Parcelable;

import java.io.Serializable;

public class Members implements Serializable {

    Integer id;
    String _id;
    Integer imageUrl;
    String name;
    Double money;
    String barvaUp;



    public Members(Integer id, String _id, Integer imageUrl, String name, Double money, String barva) {
        this.id = id;
        this._id = _id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.money = money;
        this.barvaUp=barva;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String get_Id() {

        return _id;
    }

    public void set_Id(String _id) {

        this._id = _id;
    }

    public Integer getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {

        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getBarvaUp() { return barvaUp; }

    public void setBarvaUp(String barvaUp) { this.barvaUp=barvaUp; }

    @Override
    public String toString() {
        return name;
    }
}
