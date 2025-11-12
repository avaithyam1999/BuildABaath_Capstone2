package com.buildabaath.ui;

import com.buildabaath.Order;

import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public void startProgram() {
        System.out.println("Welcome to Build a Baath! - Your favorite Build your Own Concept!");

        boolean mainProgramRunning = true;
        mainLoop:
        while (mainProgramRunning) {
            System.out.println("""
                    ==========Build A Baath==========
                    Please Select an Option:
                    1. New Order
                    2. Never mind, I like bland food
                    """);
            int mainUserChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainUserChoice) {
                case 1 -> {
                    Order currentOrder = new Order();
                    boolean orderRunning = true;
                    orderLoop:
                    while (orderRunning) {

                    }
                }
                case 2 -> {
                    System.out.println("Thanks for visiting Build a Baath and have a tasty day");
                    break mainLoop;
                }
            }
        }
    }
}
