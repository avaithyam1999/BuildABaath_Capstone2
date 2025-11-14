package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Dessert extends Item {
    private String size;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;

    public Dessert(String name, String size) {
        super(name, 0.0);
        this.size = size;
        this.smallPrice = 2.50;
        this.mediumPrice = 4.00;
        this.largePrice = 5.50;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getSmallPrice() {
        return smallPrice;
    }

    public void setSmallPrice(double smallPrice) {
        this.smallPrice = smallPrice;
    }

    public double getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(double mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

    public double getLargePrice() {
        return largePrice;
    }

    public void setLargePrice(double largePrice) {
        this.largePrice = largePrice;
    }

    @Override
    public double calculatePrice() {
        return switch (size.trim().toLowerCase()) {
            case "medium" -> mediumPrice;
            case "large" -> largePrice;
            default -> smallPrice;
        };
    }

    @Override
    public String getDescription() {
        return size + " " + super.getName() + " (Dessert)";
    }
}
