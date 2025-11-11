package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Drink extends Item {
    private String size;
    private String flavor;

    public Drink(String name, double price, String size, String flavor) {
        super(name, price);
        this.size = size;
        this.flavor = flavor;
    }

    @Override
    public double calculatePrice() {
        return 0;
    }
}
