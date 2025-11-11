package com.buildabaath.models.abstracts;

import com.buildabaath.interfaces.Priceable;

public abstract class Topping implements Priceable {
    private String name;
    private double basePrice;
    private boolean extra;

    public Topping(String name, double basePrice, boolean extra) {
        this.name = name;
        this.basePrice = basePrice;
        this.extra = extra;
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

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public double getToppingSizeMultiplier(String size) {
        switch (size.trim().toLowerCase()) {
            case "small" -> {
                return 1.0;
            }
            case "medium" -> {
                return 2.0;
            }
            case "large" -> {
                return 3.0;
            }
        }
    }

    public abstract double calculatePrice(String size);
}
