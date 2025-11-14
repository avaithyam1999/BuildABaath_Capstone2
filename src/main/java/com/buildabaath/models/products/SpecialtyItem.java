package com.buildabaath.models.products;

import com.buildabaath.models.properties.MainItemType;

public class SpecialtyItem extends MainItem {
    private String specialtyItemName;

    public SpecialtyItem(MainItemType type, String size, String specialtyItemName) {
        super(type, size);
        this.specialtyItemName = specialtyItemName;
    }
}
