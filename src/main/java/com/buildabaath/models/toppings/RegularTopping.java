package com.buildabaath.models.toppings;

import com.buildabaath.models.abstracts.Topping;

public class RegularTopping extends Topping {
    public RegularTopping(String name) {
        super(name);
    }

    @Override
    public double getPrice(String size) {
        return 0;
    }

    @Override
    public double calculatePrice() {
        return 0;
    }


}
