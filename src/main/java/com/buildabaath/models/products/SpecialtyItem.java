package com.buildabaath.models.products;

import com.buildabaath.models.properties.MainItemType;
import com.buildabaath.models.toppings.PremiumTopping;
import com.buildabaath.models.toppings.Protein;
import com.buildabaath.models.toppings.RegularTopping;
import com.buildabaath.models.toppings.Sauce;

public class SpecialtyItem extends MainItem {
    private String specialtyItemName;

    public SpecialtyItem(MainItemType type, String size, String specialtyItemName) {
        super(type, size);
        this.specialtyItemName = specialtyItemName;
        this.setSpecialtyItem(true);
    }

    public String getSpecialtyItemName() {
        return specialtyItemName;
    }

    public void setSpecialtyItemName(String specialtyItemName) {
        this.specialtyItemName = specialtyItemName;
    }

    @Override
    public String getDescription() {
        String description = "*** " + specialtyItemName + " *** - " + getSize() + " " + getName();

        if (getProtein() != null) {
            description += "\n - " + getProtein().getName();
            if (getProtein().isExtra()) {
                description += " (Extra Protein)";
            }
        }

        for (PremiumTopping premiumTopping : getPremiumToppings()) {
            description += "\n - " + premiumTopping.getName();
            if (premiumTopping.isExtra()) {
                description += " (Extra Premium Topping)";
            }
        }

        for (RegularTopping regularTopping : getRegularToppings()) {
            description += "\n - " + regularTopping.getName();
        }

        for (Sauce sauce : getSauces()) {
            description += "\n - " + sauce.getName();
        }

        return description;
    }

    public static SpecialtyItem createTandooriDeluxeBowl(String size) {
        MainItemType riceType = new MainItemType("Rice", "Basmati Baath Bowl", 5.50, 8.00, 10.50);
        SpecialtyItem item = new SpecialtyItem(riceType, "Tandoori Deluxe Bowl", size);

        Protein protein = new Protein("Tandoori Chicken");
        item.setProtein(protein);

        item.getPremiumToppings().add(new PremiumTopping("Paneer Bhurji"));
        item.getPremiumToppings().add(new PremiumTopping("Soft Boiled Egg"));

        item.getRegularToppings().add(new RegularTopping("Cucumbers"));
        item.getRegularToppings().add(new RegularTopping("Bell Peppers"));
        item.getRegularToppings().add(new RegularTopping("Raw Onions"));
        item.getRegularToppings().add(new RegularTopping("Cilantro"));

        item.getSauces().add(new Sauce("Butter Masala Gravy"));
        item.getSauces().add(new Sauce("Mint Chutney"));

        return item;
    }
    public static SpecialtyItem createSpicyBiryaniSupreme(String size) {
        MainItemType biryaniType = new MainItemType("Biryani", "Dum Biryani", 5.50, 8.00, 10.50);
        SpecialtyItem item = new SpecialtyItem(biryaniType,"Spicy Biryani Supreme", size);

        Protein protein = new Protein("Mutton Keema");
        item.setProtein(protein);

        item.getPremiumToppings().add(new PremiumTopping("Chana Dal(Chick-peas)"));
        item.getPremiumToppings().add(new PremiumTopping("Saffron Cauliflower Poriyal"));

        item.getRegularToppings().add(new RegularTopping("Jalapeños"));
        item.getRegularToppings().add(new RegularTopping("Raw Onions"));
        item.getRegularToppings().add(new RegularTopping("Tomato"));
        item.getRegularToppings().add(new RegularTopping("Cilantro"));

        item.getSauces().add(new Sauce("Kadhai Gravy"));
        item.getSauces().add(new Sauce("Rasam"));

        return item;
    }
}
