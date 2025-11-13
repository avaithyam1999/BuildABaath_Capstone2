package com.buildabaath.models;

import com.buildabaath.models.abstracts.Item;
import com.buildabaath.models.products.Order;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReceiptWriter {
    private Order order;
    private String fileName;

    public ReceiptWriter(Order order) {
        this.order = order;
        this.fileName = formatDateTime() + ".txt";
    }

    private String formatDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return order.getOrderTime().format(formatter);
    }

    private String generateFileName() {
        ArrayList<Item> items = order.getItems();
        String itemList = "";

        for (Item item : items) {
            itemList += item.getDescription();
            itemList += String.format("Price: $%.2f\n\n", item.calculatePrice());
        }

        String receipt = String.format("""
                ==Build A Baath Receipt==
                =========================
                Order ID: %s
                Order Time: %s
                
                %s
                Total: $%.2f
                
                
                Thank you for your business
                Please come again!
                """, order.getOrderID(), order.getOrderTime(), itemList, order.getTotalPrice());
        return receipt;
    }

    public void saveReceiptToFile() {
        try {
            BufferedWriter buffWriter = new BufferedWriter(new FileWriter("receipts/" + fileName));
            buffWriter.write(generateFileName());
            buffWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
