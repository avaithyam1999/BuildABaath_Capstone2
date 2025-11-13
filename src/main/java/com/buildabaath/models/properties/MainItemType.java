package com.buildabaath.models.properties;

public class MainItemType {
    private String typeName;
    private String displayName;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;

    public MainItemType(String typeName, String displayName, double smallPrice, double mediumPrice, double largePrice) {
        this.typeName = typeName;
        this.displayName = displayName;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getPrice(String size) {
        switch (size.trim().toLowerCase()) {
            case "small" -> {
                return smallPrice;
            }
            case "medium" -> {
                return mediumPrice;
            }
            case "large" -> {
                return largePrice;
            }
        }
        return smallPrice;
    }
}
