package com.buildabaath.ui;

import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public void displayMainMenu() {
        System.out.println("Welcome to Build a Baath! - Your favorite Build your Own Concept!");

        boolean mainProgramRunning = true;

        mainLoop:
        while (mainProgramRunning) {
            System.out.println("""
                    =========Build-A-Baath=========
                    Select an option:
                    1. View Menu
                    2. Order
                    3. I'd like to leave now
                    """);
            int mainMenuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainMenuChoice) {
                case 1 -> {
                    System.out.println("""
                            =========Menu=========
                            
                            Main Item Type:
                                - Baath Bowl
                                - Kati Roll
                                - 
                            """);
                }
                case 2 -> {
                    System.out.println("""
                            =========Order Menu=========
                            Select an Option:
                            1. Order a Combo
                            2. Order A la Carte
                            3. Edit Cart
                            4. Ready to Checkout
                            """);
                    int orderMenuChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (orderMenuChoice) {
                        case 1 -> {

                        }
                    }
                }
                case 3 -> {
                    System.out.println("Are you sure you want to leave?");
                    String userQuitChoice1 = scanner.nextLine();

                    switch (userQuitChoice1) {
                        case "yes" -> {
                            System.out.println("Are you sure");
                        }
                    }
                }
            }
        }
    }
}
