package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

public class Side extends Item {
    private String size;
    private String flavor;

    public Side(String name, double price) {
        super(name, price);
    }


    @Override
    public double calculatePrice() {
        return 0;
    }
}
