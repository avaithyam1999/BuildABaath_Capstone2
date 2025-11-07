package com.buildabaath.models.abstracts;

import com.buildabaath.interfaces.Priceable;

public abstract class Topping implements Priceable {
    private String name;
    private double priceForSmall;
    private double priceForMedium;
    private double priceForLarge;
    private boolean extra;

    public Topping(String name, double priceForSmall, double priceForMedium, double priceForLarge, boolean extra) {
        this.name = name;
        this.priceForSmall = priceForSmall;
        this.priceForMedium = priceForMedium;
        this.priceForLarge = priceForLarge;
        this.extra = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceForSmall() {
        return priceForSmall;
    }

    public void setPriceForSmall(double priceForSmall) {
        this.priceForSmall = priceForSmall;
    }

    public double getPriceForMedium() {
        return priceForMedium;
    }

    public void setPriceForMedium(double priceForMedium) {
        this.priceForMedium = priceForMedium;
    }

    public double getPriceForLarge() {
        return priceForLarge;
    }

    public void setPriceForLarge(double priceForLarge) {
        this.priceForLarge = priceForLarge;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    @Override
    public abstract double calculatePrice();
}
