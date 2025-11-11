package com.buildabaath.models.products;

import com.buildabaath.BaathType;
import com.buildabaath.models.abstracts.Item;
import com.buildabaath.models.abstracts.Topping;
import java.util.List;

public class Bowl extends Item {
    private String size;
    private BaathType baathType;
    private List<Topping> toppings;
    private boolean specialItem;

    public Bowl(String name, double price, String size, BaathType baathType, List<Topping> toppings, boolean specialItem) {
        super("Build a Baath Bowl");
        this.size = size;
        this.baathType = baathType;
        this.toppings = toppings;
        this.specialItem = specialItem;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BaathType getBaathType() {
        return baathType;
    }

    public void setBaathType(BaathType baathType) {
        this.baathType = baathType;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public boolean isSpecialItem() {
        return specialItem;
    }

    public void setSpecialItem(boolean specialItem) {
        this.specialItem = specialItem;
    }
}
