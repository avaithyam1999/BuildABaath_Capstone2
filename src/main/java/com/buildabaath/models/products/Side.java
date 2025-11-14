package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Side extends Item {

    public Side(String name) {
        super(name, 1.50);
    }

    @Override
    public double calculatePrice() {
        return getBasePrice();
    }

    @Override
    public String getDescription() {
        return super.getName() + " (Side)";
    }
}
