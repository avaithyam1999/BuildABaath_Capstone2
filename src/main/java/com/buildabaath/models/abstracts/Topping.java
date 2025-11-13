package com.buildabaath.models.abstracts;


public abstract class Topping {
    private String name;
    private boolean extra;

    public Topping(String name) {
        this.name = name;
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }


    public abstract double getPrice(String size);
}
