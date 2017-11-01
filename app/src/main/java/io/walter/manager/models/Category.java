package io.walter.manager.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 7/22/17.
 */

public class Category extends RealmObject{
    @PrimaryKey
    private int id;
    private String title;
    private int count;
    private int color;
    private RealmList<Product> products;

    public Category() {
    }

    public Category(int id, String title, int count, int color) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public RealmList<Product> getProducts() {
        return products;
    }

    public void setProducts(RealmList<Product> products) {
        this.products = products;
    }
}
