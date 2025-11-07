package com.buildabaath;

public abstract class Topping {
    private String name;
    private String size;
    private boolean extra;

    public Topping(String name, String size, boolean extra) {
        this.name = name;
        this.size = size;
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public abstract double getPrice();
}
