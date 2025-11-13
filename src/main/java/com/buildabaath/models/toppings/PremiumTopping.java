package com.buildabaath.models.toppings;

import com.buildabaath.models.abstracts.Topping;

public class PremiumTopping extends Topping {
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;
    private double extraPremiumToppingSmall;
    private double extraPremiumToppingMedium;
    private double extraPremiumToppingLarge;

    public PremiumTopping(String name) {
        super(name);
        this.smallPrice = .75;
        this.mediumPrice = 1.50;
        this.largePrice = 2.25;
        this.extraPremiumToppingSmall = .30;
        this.extraPremiumToppingMedium = .60;
        this.extraPremiumToppingLarge = .90;
    }

    @Override
    public double getPrice(String size) {
        double price = 0.0;

        switch (size.trim().toLowerCase()) {
            case "small" -> {
                price = smallPrice;
                if (isExtra()) {
                    price += extraPremiumToppingSmall;
                }
            }
            case "medium" -> {
                price = mediumPrice;
                if (isExtra()) {
                    price += extraPremiumToppingMedium;
                }
            }
            case "large" -> {
                price = largePrice;
                if (isExtra()) {
                    price += extraPremiumToppingLarge;
                }
            }
        }
        return price;
    }


    @Override
    public double calculatePrice() {
        return 0;
    }
}
