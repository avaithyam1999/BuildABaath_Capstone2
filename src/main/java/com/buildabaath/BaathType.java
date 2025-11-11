package com.buildabaath;

public class BaathType {
    private String name;
    private String description;
    private double basePriceSmall;
    private double basePriceMedium;
    private double basePriceLarge;

    public BaathType(String name, String description, double basePriceSmall, double basePriceMedium, double basePriceLarge) {
        this.name = name;
        this.description = description;
        this.basePriceSmall = basePriceSmall;
        this.basePriceMedium = basePriceMedium;
        this.basePriceLarge = basePriceLarge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePriceSmall() {
        return basePriceSmall;
    }

    public void setBasePriceSmall(double basePriceSmall) {
        this.basePriceSmall = basePriceSmall;
    }

    public double getBasePriceMedium() {
        return basePriceMedium;
    }

    public void setBasePriceMedium(double basePriceMedium) {
        this.basePriceMedium = basePriceMedium;
    }

    public double getBasePriceLarge() {
        return basePriceLarge;
    }

    public void setBasePriceLarge(double basePriceLarge) {
        this.basePriceLarge = basePriceLarge;
    }

    public double getPrice(String size) {
        switch (size.trim().toLowerCase()) {
            case "small" -> {
                return basePriceSmall;
            }
            case "medium" -> {
                return basePriceMedium;
            }
            case "large" -> {
                return basePriceLarge;
            }
        }
    }
}
