package com.buildabaath.ui;

import com.buildabaath.models.products.Drink;
import com.buildabaath.models.products.MainItem;
import com.buildabaath.models.products.Order;
import com.buildabaath.models.products.Side;
import com.buildabaath.models.properties.MainItemType;
import com.buildabaath.models.toppings.PremiumTopping;
import com.buildabaath.models.toppings.Protein;
import com.buildabaath.models.toppings.RegularTopping;
import com.buildabaath.models.toppings.Sauce;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
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
                    orderLoop: while (orderRunning) {
                        System.out.printf("""
                                ==========Order Screen==========
                                Items already in Order: %d
                                Current Order Total: %.2f
                                1. Add Main Item
                                2. Add a Specialty Item
                                3. Add Drink
                                4. Add a Side Item
                                5. Add Dessert
                                6. View Order Details
                                7. Cancel Order
                                """, currentOrder.getItems().size(), currentOrder.getTotalPrice());
                        int userOrderMenuChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (userOrderMenuChoice) {
                            case 1 -> {
                                System.out.println("=====Select your Main Item=====");
                                ArrayList<MainItemType> types = loadMainItemTypes();
                                int mainItemCounter = 1;
                                for (MainItemType itemType : types) {
                                    System.out.printf("%d) %s (Small: $%.2f, Medium: $%.2f, Large: $%.2f)\n",mainItemCounter++, itemType.getDisplayName(), itemType.getPrice("small"), itemType.getPrice("medium"), itemType.getPrice("large"));
                                }

                                int itemTypeChoice = scanner.nextInt();
                                scanner.nextLine();

                                if (itemTypeChoice < 1 || itemTypeChoice > types.size()) {
                                    System.out.println("That is an invalid entry.\n");
                                    return;
                                }

                                MainItemType selectedType = types.get(itemTypeChoice - 1);
                                System.out.println(itemTypeChoice + "selected.\n Which size would you like?\n1. Small\n2. Medium\n3. Large\n");
                                int sizeChoice = scanner.nextInt();
                                scanner.nextLine();
                                String mainSize = "";
                                switch (sizeChoice) {
                                    case 1 -> {
                                        mainSize = "small";
                                    }
                                    case 2 -> {
                                        mainSize = "medium";
                                    }
                                    case 3 -> {
                                        mainSize = "large";
                                    }
                                    default -> {
                                        System.out.println("Invalid size choice");
                                    }
                                };
                                MainItem item = new MainItem(selectedType, mainSize);

                                addProteinToItem(item);
                                addPremiumToppingsToItem(item);
                                addRegularToppingsToItem(item);
                                addSauceToItem(item);
                            }
                            case 3 -> {
                                addDrinkToOrder(currentOrder);
                            }
                            case 4 -> {
                                addSideToOrder(currentOrder);
                            }
                            case 5 -> {
//                                viewOrder();
                            }
                            case 6 -> {
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
                default -> throw new IllegalStateException("Unexpected value: " + mainUserChoice);
            }
        }
    }

    private void addSideToOrder(Order currentOrder) {
        System.out.println("====Select a Side====");
        ArrayList<Side> sides = loadSides();

        int sideCounter = 1;
        for (Side side : sides) {
            System.out.printf("%d) %s - $1.50\n", sideCounter++, side.getName());
        }

        int sideChoice = scanner.nextInt();
        scanner.nextLine();

        if (sideChoice > 0 && sideChoice <= sides.size()) {
            Side side = new Side(sides.get(sideChoice - 1).getName());
            currentOrder.addItem(side);
            System.out.println(sideChoice + "added to order!");
        } else {
            System.out.println("Invalid Side Option.\n");
        }
    }

    private void addDrinkToOrder(Order currentOrder) {
        System.out.println("""
                ====Select a Drink====
                1. Filter Coffee
                2. Masala Tea
                3. Badam Milk
                4. Thums Up
                5. Maaza
                """);
        int drinkChoice = scanner.nextInt();
        scanner.nextLine();

        String drinkName = "";
        switch (drinkChoice) {
            case 1 -> {
                drinkName = "Filter Coffee";
            }
            case 2 -> {
                drinkName = "Masala Tea";
            }
            case 3 -> {
                drinkName = "Badam Milk";
            }
            case 4 -> {
                drinkName = "Thums Up";
            }
            case 5 -> {
                drinkName = "Maaza";
            }
            default -> {
                System.out.println("Invalid drink choice");
            }
        }

        System.out.println("""
                What size?
                1. Small - $2.00
                2. Medium - $2.50
                3. Large - $3.00
                """);
        int sizeChoice = scanner.nextInt();
        scanner.nextLine();

        String drinkSize = "";
        switch (sizeChoice) {
            case 1 -> {
                drinkSize = "small";
            }
            case 2 -> {
                drinkSize = "medium";
            }
            case 3 -> {
                drinkSize = "large";
            }
            default -> {
                System.out.println("invalid choice");
            }
        }
        ;

        Drink drink = new Drink(drinkName, drinkSize);
        currentOrder.addItem(drink);
        System.out.printf("\n%s %s added to order!", drinkSize, drinkName);
    }

    private void addSauceToItem(MainItem item) {
        boolean addingSauces = true;

        sauceyLoop:while (addingSauces) {
            System.out.println("=====Add Sauce(ALSO FREE?!?!");
            ArrayList<Sauce> sauces = loadSauces();
             int sauceCounter = 1;
            for (Sauce sauce : sauces) {
                System.out.printf("%d) %s\n", sauceCounter++, sauce.getName());
            }
            System.out.println("0) Done adding sauces");

            int sauceChoice = scanner.nextInt();
            scanner.nextLine();

            if (sauceChoice == 0) {
                addingSauces = false;
            } else if (sauceChoice > 0 && sauceChoice <= sauces.size()) {
                Sauce selectedSauce = new Sauce(sauces.get(sauceChoice - 1).getName());
                item.getSauces().add(selectedSauce);
                System.out.println(selectedSauce + "added!");
            }
        }
    }

    private void addRegularToppingsToItem(MainItem item) {
        boolean addingRegularToppings = true;
        regularToppingLoop: while (addingRegularToppings) {
            System.out.println("=======Add Regular Toppings(FREE!!)");
            ArrayList<RegularTopping> regularToppings = loadRegularToppings();

            int counter = 1;
            for (RegularTopping regularTopping : regularToppings) {
                System.out.printf("%d) %s\n", counter++, regularTopping.getName());
            }
            System.out.println("0) Done adding regular toppings");

            int regularToppingChoice = scanner.nextInt();
            scanner.nextLine();

            if (regularToppingChoice == 0) {
                addingRegularToppings = false;
            } else if (regularToppingChoice > 0 && regularToppingChoice <= regularToppings.size()) {
                RegularTopping selectedRegularTopping = new RegularTopping(regularToppings.get(regularToppingChoice - 1).getName());
                item.getRegularToppings().add(selectedRegularTopping);
                System.out.println("Added!");
            }
        }
    }

    private void addPremiumToppingsToItem(MainItem item) {
        boolean addingToppings = true;
        toppingLoop: while (addingToppings) {
            System.out.println("""
        ======Add Premium Toppings=====
        Premium Topping Pricing:
        Small: $.75 ($.30 - Extra)
        Medium: $1.50 ($.60 - Extra)
        Large: $2.25 ($.90 - Extra)
        """);
            ArrayList<PremiumTopping> premiumToppings = loadPremiumToppings();

            int premiumToppingCounter = 1;
            for (PremiumTopping premiumTopping : premiumToppings) {
                System.out.printf(("%d) %s\n"), premiumToppingCounter++, premiumTopping.getName());
            }
            System.out.println("9) Done adding Premium Toppings\n0)Skip Premium Toppings");

            int premiumToppingChoice = scanner.nextInt();
            scanner.nextLine();

            if (premiumToppingChoice == 9 || premiumToppingChoice == 0) {
                break toppingLoop;
            } else if (premiumToppingChoice > 0 && premiumToppingChoice <= premiumToppings.size()) {
                PremiumTopping selectedPremiumTopping = new PremiumTopping(premiumToppings.get(premiumToppingChoice - 1).getName());

                System.out.printf("""
                        Would you like extra %s?
                        Extra Premium Topping Pricing:
                        Small: $.30
                        Medium: $.60
                        Large: $.90
                        1. Yes
                        2. No
                        """, selectedPremiumTopping);
                int extraPremiumSelection = scanner.nextInt();
                selectedPremiumTopping.setExtra(extraPremiumSelection == 1);

                item.getPremiumToppings().add(selectedPremiumTopping);
                if (!selectedPremiumTopping.isExtra()) {
                    System.out.printf("Extra %s Added!", selectedPremiumTopping);
                } else {
                    System.out.printf("%s Added!", selectedPremiumTopping);
                }
            }
        }
    }

    private void addProteinToItem(MainItem item) {
        System.out.println("=====Select your Protein=====");
        ArrayList<Protein> proteins = loadProteins();

        int proteinCounter = 1;
        for (Protein protein : proteins) {
            System.out.printf("%d) %s\n", proteinCounter++, protein.getName());
        }
        System.out.println("0) No protein");

        int proteinChoice = scanner.nextInt();
        scanner.nextLine();

        if (proteinChoice > 0 && proteinChoice <= proteins.size()) {
            Protein selectedProtein = new Protein(proteins.get(proteinChoice - 1).getName());

            System.out.println("Would you like extra Protein?(Small: $.50, Medium: $1.00, Large: $1.50\n1. Yes\n2. No\n");
            int extraProteinSelected = scanner.nextInt();
            selectedProtein.setExtra(extraProteinSelected == 1);
            scanner.nextLine();

            item.setProtein(selectedProtein);
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
        proteins.add(new Protein("Nandu(Crab) Curry"));
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
        sauces.add(new Sauce("Rasam"));
        sauces.add(new Sauce("Coconut Chutney"));
        sauces.add(new Sauce("Mint Chutney"));
        return sauces;
    }

    private ArrayList<Side> loadSides() {
        ArrayList<Side> sides = new ArrayList<>();
        sides.add(new Side("Chicken Tikka Samosa"));
        sides.add(new Side("Paneer Tikka Samosa"));
        return sides;
    }

    private ArrayList<Drink> loadDrinks() {
        ArrayList<Drink> drinks = new ArrayList<>();
        drinks.add(new Drink("Filter Coffee", "small"));
        drinks.add(new Drink("Filter Coffee", "medium"));
        drinks.add(new Drink("Filter Coffee", "large"));
        drinks.add(new Drink("Masala Tea", "small"));
        drinks.add(new Drink("Masala Tea", "medium"));
        drinks.add(new Drink("Masala Tea", "large"));
        drinks.add(new Drink("Badam Milk", "small"));
        drinks.add(new Drink("Badam Milk", "medium"));
        drinks.add(new Drink("Badam Milk", "large"));
        drinks.add(new Drink("Thums Up", "small"));
        drinks.add(new Drink("Thums Up", "medium"));
        drinks.add(new Drink("Thums Up", "large"));
        drinks.add(new Drink("Maaza", "small"));
        drinks.add(new Drink("Maaza", "medium"));
        drinks.add(new Drink("Maaza", "large"));
        return drinks;
    }

}
