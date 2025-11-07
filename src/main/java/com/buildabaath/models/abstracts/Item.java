package com.buildabaath.models.abstracts;

import com.buildabaath.interfaces.Priceable;

public abstract class Item implements Priceable {
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public abstract double calculatePrice();
}
