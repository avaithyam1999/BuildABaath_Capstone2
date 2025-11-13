package com.buildabaath.models.toppings;

import com.buildabaath.models.abstracts.Topping;

public class Sauce extends Topping {
    public Sauce(String name) {
        super(name);
    }

    @Override
    public double getPrice(String size) {
        return 0.0;
    }

}
