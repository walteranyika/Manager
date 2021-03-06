package io.walter.manager.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 10/30/17.
 */

public class TemporaryItem extends RealmObject {
    @PrimaryKey
    private int code;
    private String title;
    private double price;
    private int quantity;
    private String description;
    private String category;
    private int color;
    private double total;
    private  boolean isTaxable;

    public TemporaryItem() {
    }

    public TemporaryItem(int code, String title, double price, int quantity, String description, String category, int color, double total, boolean isTaxable) {
        this.code = code;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.color = color;
        this.total = total;
        this.isTaxable = isTaxable;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }
}
