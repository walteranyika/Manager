package io.walter.manager.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contact extends RealmObject {
    @PrimaryKey
    private int code;
    private String names;
    private  String telephone;
    private  String email;
    private  String type;
    private RealmList<Order> orders;

    public Contact() {
    }

    public Contact(int code, String names, String telephone, String email, String type) {
        this.code = code;
        this.names = names;
        this.telephone = telephone;
        this.email = email;
        this.type = type;
    }

    public RealmList<Order> getOrders() {
        return orders;
    }

    public void setOrders(RealmList<Order> orders) {
        this.orders = orders;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
