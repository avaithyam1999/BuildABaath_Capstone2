package com.buildabaath.models.toppings;

import com.buildabaath.models.abstracts.Topping;

public class Protein extends Topping {
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;
    private double extraToppingForSmall;
    private double extraToppingForMedium;
    private double extraToppingForLarge;

    public Protein(String name) {
        super(name);
        this.smallPrice = 1.00;
        this.mediumPrice = 2.00;
        this.largePrice = 3.00;
        this.extraToppingForSmall = .50;
        this.extraToppingForMedium = 1.00;
        this.extraToppingForLarge = 1.50;
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

    public double getExtraToppingForSmall() {
        return extraToppingForSmall;
    }

    public void setExtraToppingForSmall(double extraToppingForSmall) {
        this.extraToppingForSmall = extraToppingForSmall;
    }

    public double getExtraToppingForMedium() {
        return extraToppingForMedium;
    }

    public void setExtraToppingForMedium(double extraToppingForMedium) {
        this.extraToppingForMedium = extraToppingForMedium;
    }

    public double getExtraToppingForLarge() {
        return extraToppingForLarge;
    }

    public void setExtraToppingForLarge(double extraToppingForLarge) {
        this.extraToppingForLarge = extraToppingForLarge;
    }

    @Override
    public double getPrice(String size) {
        double price = 0.0;

        switch (size.trim().toLowerCase()) {
            case "small" -> {
                price = smallPrice;
                if (isExtra()) {
                    price += extraToppingForSmall;
                }
            }
            case "medium" -> {
                price = mediumPrice;
                if (isExtra()) {
                    price += extraToppingForMedium;
                }
            }
            case "large" -> {
                price = largePrice;
                if (isExtra()) {
                    price += extraToppingForLarge;
                }
            }
        }
        return price;
    }
}
