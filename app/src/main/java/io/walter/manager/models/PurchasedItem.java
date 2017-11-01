package io.walter.manager.models;

import io.realm.RealmObject;

/**
 * Created by walter on 10/30/17.
 */

public class PurchasedItem extends RealmObject {
    private int code;
    private String product;
    private double price;
    private int quantity;
    private int purchase_date;
    private String purchase_month;
    private String raw_date;

    public PurchasedItem() {
    }

    public PurchasedItem(int code, String product, double price, int quantity, int purchase_date, String purchase_month, String raw_date) {
        this.code = code;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.purchase_date = purchase_date;
        this.purchase_month = purchase_month;
        this.raw_date = raw_date;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public int getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(int purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getPurchase_month() {
        return purchase_month;
    }

    public void setPurchase_month(String purchase_month) {
        this.purchase_month = purchase_month;
    }

    public String getRaw_date() {
        return raw_date;
    }

    public void setRaw_date(String raw_date) {
        this.raw_date = raw_date;
    }
}
