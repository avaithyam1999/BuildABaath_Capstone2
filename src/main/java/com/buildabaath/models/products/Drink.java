package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Drink extends Item {
    private String size;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;

    public Drink(String name, String size) {
        super(name, 0.0);
        this.size = size;
        this.smallPrice = 3.00;
        this.mediumPrice = 3.75;
        this.largePrice = 4.20;
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
        return size + " " + super.getName() + "(Drink)";
    }
}
