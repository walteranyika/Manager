package io.walter.manager.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 10/30/17.
 */

public class PurchaseSummary extends RealmObject {
    @PrimaryKey
    private int code;
    private double total_price;
    private Date purchase_date;
    private String purchase_month;
    private String raw_date;
    private int customer_id;
    private RealmList<PurchasedItem> purchasedItems;

    public PurchaseSummary() {
    }

    public PurchaseSummary(int code, double total_price, Date purchase_date, String purchase_month, String raw_date, int customer_id) {
        this.code = code;
        this.total_price = total_price;
        this.purchase_date = purchase_date;
        this.purchase_month = purchase_month;
        this.raw_date = raw_date;
        this.customer_id = customer_id;
    }

    public RealmList<PurchasedItem> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(RealmList<PurchasedItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
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

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
}
