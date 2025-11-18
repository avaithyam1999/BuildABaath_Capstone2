package com.buildabaath.data;

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

    private String generateReceiptFile() {
        ArrayList<Item> items = order.getItems();
        DateTimeFormatter receiptFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm a");
        String itemList = "";

        for (Item item : items) {
            itemList += item.getDescription();
            itemList += String.format("\nItem Price: $%.2f\n\n", item.calculatePrice());
        }

        String receipt = String.format("""
                ==Build A Baath Receipt==
                =========================
                %s
                Order Time: %s
                
                %s
                
                Total: $%.2f
                Tip Amount: %.2f
                
                Grand Total: $%.2f
                
                SIGN HERE:
                
                __________________________
                
                Thank you for your business
                Please come again!
                """, order.getOrderID(), order.getOrderTime().format(receiptFormatter), itemList, order.getTotalPrice(), order.getTipAmount(), order.getFinalTotal());
        return receipt;
    }

    public void saveReceiptToFile() {
        try {
            BufferedWriter buffWriter = new BufferedWriter(new FileWriter("receipts/" + fileName));
            buffWriter.write(generateReceiptFile());
            buffWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
