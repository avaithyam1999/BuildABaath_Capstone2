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
                                1. Add Main Item
                                2. Add a Specialty Item
                                3. Add Drink
                                4. Add a Side Item
                                5. Add Dessert
                                6. View Order Details
                                7. Cancel Order
                                """, currentOrder.getItems().size());
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
                                String size = switch (sizeChoice) {
                                    case 2 -> "medium";
                                    case 3 -> "large";
                                    default -> "small";
                                };
                                MainItem item = new MainItem(selectedType, size);

                                addProteinToItem(item);

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
                                        if (selectedPremiumTopping.isExtra()) {
                                            System.out.printf("Extra %s Added!", selectedPremiumTopping);
                                        } else if (!selectedPremiumTopping.isExtra()) {
                                            System.out.printf("%s Added!", selectedPremiumTopping);
                                        }
                                    }
                                }
                            }
                            case 3 -> {
//                                addDrink();
                            }
                            case 4 -> {
//                                addSideItem();
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
