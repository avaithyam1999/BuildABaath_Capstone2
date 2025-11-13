package com.buildabaath.models.products;

import com.buildabaath.interfaces.Customizable;
import com.buildabaath.models.abstracts.Item;
import com.buildabaath.models.abstracts.Topping;
import com.buildabaath.models.properties.MainItemType;
import com.buildabaath.models.toppings.PremiumTopping;
import com.buildabaath.models.toppings.Protein;
import com.buildabaath.models.toppings.RegularTopping;
import com.buildabaath.models.toppings.Sauce;

import java.util.ArrayList;
import java.util.List;

public class MainItem extends Item implements Customizable {
    private MainItemType type;
    private String size;
    private Protein protein;
    private ArrayList<PremiumTopping> premiumToppings;
    private ArrayList<RegularTopping> regularToppings;
    private ArrayList<Sauce> sauces;
    private boolean specialtyItem;

    public MainItem(MainItemType type, String size) {
        super(type.getDisplayName(), type.getPrice(size));
        this.type = type;
        this.size = size;
        this.protein = null;
        this.premiumToppings = new ArrayList<>();
        this.regularToppings = new ArrayList<>();
        this.sauces = new ArrayList<>();
        this.specialtyItem = false;
    }

    public MainItemType getType() {
        return type;
    }

    public void setType(MainItemType type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public ArrayList<PremiumTopping> getPremiumToppings() {
        return premiumToppings;
    }

    public void setPremiumToppings(ArrayList<PremiumTopping> premiumToppings) {
        this.premiumToppings = premiumToppings;
    }

    public ArrayList<RegularTopping> getRegularToppings() {
        return regularToppings;
    }

    public void setRegularToppings(ArrayList<RegularTopping> regularToppings) {
        this.regularToppings = regularToppings;
    }

    public ArrayList<Sauce> getSauces() {
        return sauces;
    }

    public void setSauces(ArrayList<Sauce> sauces) {
        this.sauces = sauces;
    }

    public boolean isSpecialtyItem() {
        return specialtyItem;
    }

    public void setSpecialtyItem(boolean specialtyItem) {
        this.specialtyItem = specialtyItem;
    }

    @Override
    public double calculatePrice() {
        double total = getBasePrice();
        if (protein != null) {
            total += protein.getPrice(size);
        }
        for (PremiumTopping premiumTopping : premiumToppings) {
            total += premiumTopping.getPrice(size);
        }
        return total;
    }

    @Override
    public String getDescription() {
        String description = size + " " + getName();

        if (protein != null) {
            description += "\n - " + protein.getName();
            if (protein.isExtra()) {
                description += " (Extra Protein)";
            }
        }

        for (PremiumTopping premiumTopping : premiumToppings) {
            description += "\n - " + premiumTopping.getName();
            if (premiumTopping.isExtra()) {
                description += " (Extra Premium Topping)";
            }
        }

        for (RegularTopping regularTopping : regularToppings) {
            description += "\n - " + regularTopping.getName();
        }

        for (Sauce sauce : sauces) {
            description += "\n - " + sauce.getName();
        }

        if (specialtyItem) {
            description += "\n - ***SPECIALTY ITEM***";
        }

        return description;
    }

    @Override
    public void customize() {
    }
}

