package com.buildabaath.models.products;

import com.buildabaath.models.abstracts.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Order {
    private String orderNumber;
    private ArrayList<Item> items;
    private double totalPrice;
    private LocalDateTime orderTime;

    public Order() {
        this.orderNumber = generateOrderNumber();
        this.items = new ArrayList<Item>();
        this.totalPrice = 0.0;
        this.orderTime = LocalDateTime.now();
    }

    public String getOrderID() {
        return orderNumber;
    }

    public void setOrderID(String orderID) {
        this.orderNumber = orderID;
    }

    public ArrayList getItems() {
        return items;
    }

    public void setItems(ArrayList items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String generateOrderNumber() {
        Random random = new Random();
        int randomIDNum = random.nextInt(999) + 100;
        return String.format("ORDER NUMBER: %d", randomIDNum);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void updateTotalPrice() {
        totalPrice = 0.0;
        for (Item item : items) {
            totalPrice += item.calculatePrice();
        }
    }

    public void emptyOrder() {
        items.clear();
        totalPrice = 0.0;
    }
}
