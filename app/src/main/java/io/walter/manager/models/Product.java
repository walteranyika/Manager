package io.walter.manager.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 7/7/17.
 */

public class Product extends RealmObject{
    @PrimaryKey
    private int code;
    private String title;
    private double price;
    private int quantity;
    private String description;
    private String category;
    private int color;
    private  boolean isTaxable;
    private double reOrderLevel;

    public Product() {
    }

    public Product(int code, String title, double price, int quantity,double reOrderLevel ,String description, String category, int color, boolean isTaxable) {
        this.code = code;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.reOrderLevel=reOrderLevel;
        this.description = description;
        this.category = category;
        this.color = color;
        this.isTaxable=isTaxable;
    }

    public double getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(double reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
