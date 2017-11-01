package io.walter.manager.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class Order extends RealmObject {
    @PrimaryKey
    private int code;
    private String type;
    private double total;
    private String date;
    private String client;
    private int clientCode;

    private RealmList<OrderItem> orderItems;

    public Order() {
    }

    public Order(int code, String type, double total, String date, String client, int clientCode) {
        this.code = code;
        this.type = type;
        this.total = total;
        this.date = date;
        this.client = client;
        this.clientCode = clientCode;
    }

    public RealmList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(RealmList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getClientCode() {
        return clientCode;
    }

    public void setClientCode(int clientCode) {
        this.clientCode = clientCode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
