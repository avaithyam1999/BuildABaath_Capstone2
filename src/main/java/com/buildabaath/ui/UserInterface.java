package com.buildabaath.ui;

import com.buildabaath.models.products.Order;
import com.buildabaath.models.properties.MainItemType;
import com.buildabaath.models.toppings.PremiumTopping;
import com.buildabaath.models.toppings.Protein;
import com.buildabaath.models.toppings.RegularTopping;
import com.buildabaath.models.toppings.Sauce;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    private Order order;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.order = null;
    }

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
                        System.out.printf("""
                                ==========Order Screen==========
                                Items already in Order: %d
                                1. Add Main Item
                                2. Add Drink
                                3. Add a Side Item
                                4. Add Dessert
                                5. View Order Details
                                6. Cancel Order
                                """, currentOrder.getItems().size());
                        int userOrderMenuChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (userOrderMenuChoice) {
                            case 1 -> {
                                try {
                                    System.out.println("=====Select your Main=====");
                                    ArrayList<MainItemType> types = get();
                                }
                            }
                            case 2 -> {
//                                addDrink();
                            }
                            case 3 -> {
//                                addSideItem();
                            }
                            case 4 -> {
//                                viewOrder();
                            }
                            case 5 -> {
//                                checkout()
//                                ordering = false;
                            }
                            default -> {
                                System.out.println("Invalid Option.\n Try again");
                            }
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Thanks for visiting Build a Baath and have a tasty day");
                    break mainLoop;
                }
            }
        }
    }

    private ArrayList<MainItemType> loadMainItemTypes() {
        ArrayList<MainItemType> mainItemTypes = new ArrayList<>();
        mainItemTypes.add(new MainItemType("Rice", "Basmati Baath Bowl", 3.50, 6.00, 8.50));
        mainItemTypes.add(new MainItemType("Naan", "Baked Naan Flatbread", 3.50, 6.00, 8.50));
        mainItemTypes.add(new MainItemType("Chaat", "Street Samosa Chaat", 4.00, 6.50, 9.00));
        mainItemTypes.add(new MainItemType("Biryani", "Dum Biryani", 4.00, 6.50, 9.00));
        return mainItemTypes;
    }

    private ArrayList<Protein> loadProteins() {
        ArrayList<Protein> proteins = new ArrayList<>();
        proteins.add(new Protein("Chicken Tikka"));
        proteins.add(new Protein("Tandoori Chicken"));
        proteins.add(new Protein("Paneer Tikka"));
        proteins.add(new Protein("Malai Paneer"));
        proteins.add(new Protein("Mutton Keema"));
        proteins.add(new Protein("King Fish Curry"));
        return proteins;
    }

    private ArrayList<PremiumTopping> loadPremiumToppings() {
        ArrayList<PremiumTopping> premiumToppings = new ArrayList<>();
        premiumToppings.add(new PremiumTopping("Chana Dal(Chick-peas)"));
        premiumToppings.add(new PremiumTopping("Saffron Cauliflower Poriyal"));
        premiumToppings.add(new PremiumTopping("Paneer Bhurji"));
        premiumToppings.add(new PremiumTopping("Soft Boiled Egg"));
        return premiumToppings;
    }

    private ArrayList<RegularTopping> loadRegularToppings() {
        ArrayList<RegularTopping> regularToppings = new ArrayList<>();
        regularToppings.add(new RegularTopping("Cucumbers"));
        regularToppings.add(new RegularTopping("Beet Root"));
        regularToppings.add(new RegularTopping("Bell Peppers"));
        regularToppings.add(new RegularTopping("Jalapeños"));
        regularToppings.add(new RegularTopping("Raw Onions"));
        regularToppings.add(new RegularTopping("Carrots"));
        regularToppings.add(new RegularTopping("Cilantro"));
        regularToppings.add(new RegularTopping("Tomato"));
        regularToppings.add(new RegularTopping("Lettuce"));
        return regularToppings;
    }

    private ArrayList<Sauce> loadSauces() {
        ArrayList<Sauce> sauces = new ArrayList<>();
        sauces.add(new Sauce("Butter Masala Gravy"));
        sauces.add(new Sauce("Kadhai Gravy"));
        sauces.add(new Sauce("Dahi"));
        sauces.add(new Sauce(""));
        return sauces;
    }
}
