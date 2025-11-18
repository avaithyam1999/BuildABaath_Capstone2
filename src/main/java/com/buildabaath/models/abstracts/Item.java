package com.buildabaath.models.abstracts;

import com.buildabaath.models.interfaces.Priceable;

public abstract class Item implements Priceable {
    private String name;
    private double basePrice;

    public Item(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public abstract double calculatePrice();
    public abstract String getDescription();
}
