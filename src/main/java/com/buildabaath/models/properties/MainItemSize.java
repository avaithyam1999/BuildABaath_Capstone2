package com.buildabaath.models.properties;

public class MainItemSize {
    private String sizeName;
    private double multiplier;

    public MainItemSize(String sizeName, double multiplier) {
        this.sizeName = sizeName;
        this.multiplier = multiplier;
    }

    public String getSizeName() {
        return sizeName;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
