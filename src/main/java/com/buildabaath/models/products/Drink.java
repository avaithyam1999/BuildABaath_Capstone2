package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Drink extends Item {
    private String size;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;

    public Drink(String name) {
        super(name, 0.0);
        this.size = size;
        this.smallPrice = 3.00;
        this.mediumPrice = 3.75;
        this.largePrice = 4.20;
    }

    @Override
    public double calculatePrice() {
        switch (size.trim().toLowerCase()) {
            case "small" -> {
                return smallPrice;
            }
            case "medium" -> {
                return mediumPrice;
            }
            case "large" -> {
                return largePrice;
            }
        }
        return smallPrice;
    }

    @Override
    public String getDescription() {
        return size + " " + super.getName() + "(Drink)";
    }
}
