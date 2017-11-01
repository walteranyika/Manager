package io.walter.manager.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 7/7/17.
 */

public class Service extends RealmObject{
    @PrimaryKey
    private int code;
    private String title;
    private double price;
    private String description;
    private int color;

    public Service() {
    }

    public Service(int code, String title, double price, String description,int color) {
        this.code = code;
        this.title = title;
        this.price = price;
        this.description = description;
        this.color = color;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
