package com.buildabaath.ui;

import com.buildabaath.ConsoleFormatter;
import com.buildabaath.models.ReceiptWriter;
import com.buildabaath.models.abstracts.Item;
import com.buildabaath.models.products.*;
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

    public void startBuildingThatBaath() {
        showWelcomeScreen();

        boolean mainProgramRunning = true;
        mainLoop:
        while (mainProgramRunning) {
            ConsoleFormatter.clearScreen();
            ConsoleFormatter.printHeader("BUILD A BAATH - MAIN MENU");
            System.out.println();

            ConsoleFormatter.printMenuItem(1, "🆕 New Order");
            ConsoleFormatter.printMenuItem(2, "👋 Never mind, I like bland food");
            ConsoleFormatter.printDivider();

            System.out.print("\nYour choice: ");
            int mainUserChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainUserChoice) {
                case 1 -> {
                    Order currentOrder = new Order();
                    boolean orderRunning = true;
                    orderLoop: while (orderRunning) {
                        ConsoleFormatter.clearScreen();
                        ConsoleFormatter.printHeader("ORDER SCREEN");
                        System.out.println();

                        System.out.println(ConsoleFormatter.THANOS + "📋 Order Status:" + ConsoleFormatter.RESET);
                        System.out.printf("   Items in cart: %s%d%s\n",
                                ConsoleFormatter.BOLD, currentOrder.getItems().size(), ConsoleFormatter.RESET);
                        System.out.print("   Current total: ");
                        ConsoleFormatter.printPrice(currentOrder.getTotalPrice());
                        System.out.println("\n");

                        ConsoleFormatter.printDivider();
                        ConsoleFormatter.printMenuItem(1, "🍚 Add Main Item");
                        ConsoleFormatter.printMenuItem(2, "⭐ Add Specialty Item");
                        ConsoleFormatter.printMenuItem(3, "🥤 Add Drink");
                        ConsoleFormatter.printMenuItem(4, "🥘 Add Side Item");
                        ConsoleFormatter.printMenuItem(5, "🍰 Add Dessert");
                        ConsoleFormatter.printMenuItem(6, "💳 Check Out");
                        ConsoleFormatter.printMenuItem(7, "❌ Cancel Order");
                        ConsoleFormatter.printDivider();

                        System.out.print("\nYour choice: ");
                        int userOrderMenuChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (userOrderMenuChoice) {
                            case 1 -> {
                                ConsoleFormatter.clearScreen();
                                ConsoleFormatter.printHeader("SELECT YOUR MAIN ITEM");
                                System.out.println();

                                ArrayList<MainItemType> types = loadMainItemTypes();
                                int mainItemCounter = 1;
                                for (MainItemType itemType : types) {
                                    System.out.printf("%s%d)%s %s\n",
                                            ConsoleFormatter.BOLD, mainItemCounter++,
                                            ConsoleFormatter.RESET, itemType.getDisplayName());
                                    System.out.print("   Small: ");
                                    ConsoleFormatter.printPrice(itemType.getPrice("small"));
                                    System.out.print(" | Medium: ");
                                    ConsoleFormatter.printPrice(itemType.getPrice("medium"));
                                    System.out.print(" | Large: ");
                                    ConsoleFormatter.printPrice(itemType.getPrice("large"));
                                    System.out.println();
                                }
                                ConsoleFormatter.printDivider();

                                System.out.print("\nSelect item: ");
                                int itemTypeChoice = scanner.nextInt();
                                scanner.nextLine();

                                if (itemTypeChoice < 1 || itemTypeChoice > types.size()) {
                                    ConsoleFormatter.printError("That is an invalid entry.\n");
                                    promptUserForEnter();
                                    return;
                                }

                                MainItemType selectedType = types.get(itemTypeChoice - 1);
                                ConsoleFormatter.clearScreen();
                                ConsoleFormatter.printHeader("SELECT SIZE");
                                System.out.println();

                                System.out.printf("%sSelected: %s%s\n\n",
                                        ConsoleFormatter.THANOS, selectedType.getDisplayName(),
                                        ConsoleFormatter.RESET);

                                ConsoleFormatter.printMenuItem(1, "Small", selectedType.getPrice("small"));
                                ConsoleFormatter.printMenuItem(2, "Medium", selectedType.getPrice("medium"));
                                ConsoleFormatter.printMenuItem(3, "Large", selectedType.getPrice("large"));
                                ConsoleFormatter.printDivider();

                                System.out.print("\nYour choice: ");

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
                                        ConsoleFormatter.printError("Invalid size choice. Try again");
                                        promptUserForEnter();
                                    }
                                };
                                MainItem item = new MainItem(selectedType, mainSize);

                                addProteinToItem(item);
                                addPremiumToppingsToItem(item);
                                addRegularToppingsToItem(item);
                                addSauceToItem(item);
                                ConsoleFormatter.printBox("""
                                =====Special Order??=====
                                Would you like your order to be baked and served in a tandoori clay pot(+$2.00)?
                                1. Yes
                                2. No
                                """);
                                int specializedChoice = scanner.nextInt();
                                scanner.nextLine();

                                currentOrder.addItem(item);
                                currentOrder.updateTotalPrice();
                                ConsoleFormatter.printSuccess(String.format("%s %s added to order!", mainSize, selectedType.getDisplayName()));
                                promptUserForEnter();
                            }
                            case 2 -> {

                            }
                            case 3 -> {
                                addDrinkToOrder(currentOrder);
                            }
                            case 4 -> {
                                addSideToOrder(currentOrder);
                            }
                            case 5 -> {
                                addDessertToOrder(currentOrder);
                            }
                            case 6 -> {
                                if (currentOrder.getItems().isEmpty()) {
                                    ConsoleFormatter.printWarning("Your order is empty. Add an item and then checkout");
                                    promptUserForEnter();
                                } else {
                                    checkOutOrder(currentOrder);

                                    System.out.printf("\n%sConfirm your order?%s\n", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
                                    ConsoleFormatter.printBox(String.format("Order Total: $%.2f", currentOrder.getFinalTotal()));
                                    ConsoleFormatter.printMenuItem(1, "✓ Confirm Order");
                                    ConsoleFormatter.printMenuItem(2, "✗ Go Back");
                                    ConsoleFormatter.printDivider();

                                    System.out.print("\nYour choice: ");
                                    int checkOutChoice = scanner.nextInt();
                                    scanner.nextLine();

                                    if (checkOutChoice == 1) {
                                        ReceiptWriter receiptWriter = new ReceiptWriter(currentOrder);
                                        receiptWriter.saveReceiptToFile();
                                        ConsoleFormatter.printBox("Thank you for your order! You can find your receipt in the folder\nPlease come again! 🙏");
                                        break mainLoop;
                                    }
                                }
                            }
                            case 7 -> {
                                ConsoleFormatter.printBox("Order Cancelled. Lame.");
                                break orderLoop;
                            }
                            default -> {
                                ConsoleFormatter.printBox("Invalid Option.\n Try again");
                                promptUserForEnter();
                            }
                        }
                    }
                }
                case 2 -> {
                    ConsoleFormatter.clearScreen();
                    ConsoleFormatter.printBox("Thanks for visiting Build a Baath and have a tasty day");
                    break mainLoop;
                }
                default -> {
                    ConsoleFormatter.printError(String.format("Unexpected Value: %d", mainUserChoice));
                }
            }
        }
    }

    private void showWelcomeScreen() {
        ConsoleFormatter.clearScreen();
        System.out.println("""
                ▗▄▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖   ▗▄▄▄      ▗▄▖     ▗▄▄▖  ▗▄▖  ▗▄▖▗▄▄▄▖▗▖ ▗▖
                ▐▌ ▐▌▐▌ ▐▌  █  ▐▌   ▐▌  █    ▐▌ ▐▌    ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █  ▐▌ ▐▌
                ▐▛▀▚▖▐▌ ▐▌  █  ▐▌   ▐▌  █    ▐▛▀▜▌    ▐▛▀▚▖▐▛▀▜▌▐▛▀▜▌ █  ▐▛▀▜▌
                ▐▙▄▞▘▝▚▄▞▘▗▄█▄▖▐▙▄▄▖▐▙▄▄▀    ▐▌ ▐▌    ▐▙▄▞▘▐▌ ▐▌▐▌ ▐▌ █  ▐▌ ▐▌
                """);
        ConsoleFormatter.printBox("""
                🍛 Welcome to Build a Baath! 🍛
                Your favorite Build-Your-Own Indian Concept!
                """);

        promptUserForEnter();
    }

    private void promptUserForEnter() {
        System.out.printf("\n%s Press Enter to Continue.....%s\n", ConsoleFormatter.THANOS, ConsoleFormatter.RESET);
        scanner.nextLine();
    }


    private void checkOutOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        viewOrderSummary(currentOrder);

        System.out.print("\nSubtotal: ");
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice());
        System.out.println();
        ConsoleFormatter.printDivider();

        System.out.printf("\n%s💰 Please select a Tip Amount%s\n", ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.printf("%sWe are severely underpaid. Please.%s\n", ConsoleFormatter.CYAN, ConsoleFormatter.RESET);
        System.out.println();

        System.out.printf("%s1)%s 15%% - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice() * 0.15);
        System.out.println();

        System.out.printf("%s2)%s 20%% - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice() * 0.20);
        System.out.println();

        System.out.printf("%s3)%s 25%% - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice() * 0.25);
        System.out.println();

        System.out.printf("%s4)%s Custom Percentage \n", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        System.out.printf("%s5)%s No Tip >:O \n", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int tipAmountChoice = scanner.nextInt();
        scanner.nextLine();

        switch (tipAmountChoice) {
            case 1 -> {
                currentOrder.setTipAmount(15.00);
            }
            case 2 -> {
                currentOrder.setTipAmount(20.00);
            }
            case 3 -> {
                currentOrder.setTipAmount(25.00);
            }
            case 4 -> {
                System.out.println("Enter Tip Percentage: \n");
                double customTipAmount = scanner.nextDouble();
                scanner.nextLine();
                currentOrder.setTipAmount(customTipAmount);
            }
            case 5 -> {
                currentOrder.setTipAmount(0);
            }
        }
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("FINAL ORDER SUMMARY");
        System.out.println();
        viewOrderSummary(currentOrder);

        System.out.print("\nSubtotal: ");
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice());
        System.out.printf("\nTip (%.0f%%): ", currentOrder.getTipPercentage());
        ConsoleFormatter.printPrice(currentOrder.getTipAmount());
        System.out.println();
        ConsoleFormatter.printDivider();
        System.out.printf("%sTOTAL: %s", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(currentOrder.getFinalTotal());
        System.out.println("\n");
    }

    private void viewOrderSummary(Order currentOrder) {
        int itemNumber = 1;
        for (Item item : currentOrder.getItems()) {
            System.out.printf("%sItem #%d%s\n", ConsoleFormatter.BOLD, itemNumber++, ConsoleFormatter.RESET);
            System.out.println(item.getDescription());
            System.out.print("Price: ");
            ConsoleFormatter.printPrice(item.calculatePrice());
            System.out.println();
        }
    }

    private void addDessertToOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A DESSERT");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Ras Malai");
        ConsoleFormatter.printMenuItem(2, "Gulab Jamun");
        ConsoleFormatter.printMenuItem(3, "Jalebi");
        ConsoleFormatter.printMenuItem(4, "Kaju Katli");
        ConsoleFormatter.printDivider();

        System.out.println("\nYour choice: ");
        int dessertChoice = scanner.nextInt();
        scanner.nextLine();

        String dessertName = "";
        switch (dessertChoice) {
            case 1 -> {
                dessertName = "Ras Malai";
            }
            case 2 -> {
                dessertName = "Gulab Jamun";
            }
            case 3 -> {
                dessertName = "Jalebi";
            }
            case 4 -> {
                dessertName = "Kaju Katli";
            }
            default -> {
                ConsoleFormatter.printError("You've entered the wrong dessert");
            }
        }

        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();
        System.out.printf("%sSelected: %s%s\n\n", ConsoleFormatter.CYAN, dessertName, ConsoleFormatter.RESET);

        ConsoleFormatter.printMenuItem(1, "Small", 2.50);
        ConsoleFormatter.printMenuItem(2, "Medium", 4.00);
        ConsoleFormatter.printMenuItem(3, "Large", 5.50);
        ConsoleFormatter.printDivider();

        System.out.println("\nYour choice: ");
        int sizeChoice = scanner.nextInt();
        scanner.nextLine();

        String dessertSize = "";
        switch (sizeChoice) {
            case 1 -> {
                dessertSize = "small";
            }
            case 2 -> {
                dessertSize = "medium";
            }
            case 3 -> {
                dessertSize = "large";
            }
            default -> {
                ConsoleFormatter.printError("You've entered an invalid option");
            }
        }
        Dessert dessert = new Dessert(dessertName, dessertSize);
        currentOrder.addItem(dessert);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order!", dessertSize, dessertName));
        promptUserForEnter();
    }

    private void addSideToOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A SIDE");

        ArrayList<Side> sides = loadSides();

        int sideCounter = 1;
        for (Side side : sides) {
            System.out.printf("%s%d)%s %-30s \n", ConsoleFormatter.BOLD, sideCounter++, ConsoleFormatter.RESET, side.getName());
            ConsoleFormatter.printPrice(1.50);
            System.out.println();
        }
        ConsoleFormatter.printDivider();

        System.out.println("\nYour Choice: ");
        int sideChoice = scanner.nextInt();
        scanner.nextLine();

        if (sideChoice > 0 && sideChoice <= sides.size()) {
            Side side = new Side(sides.get(sideChoice - 1).getName());
            currentOrder.addItem(side);
            currentOrder.updateTotalPrice();
            ConsoleFormatter.printSuccess(String.format("%s added to order!", side.getName()));
        } else {
            ConsoleFormatter.printError("You entered an invalid side choice");
        }
    }

    private void addDrinkToOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A DRINK");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "☕️ Filter Coffee");
        ConsoleFormatter.printMenuItem(2, "🫖 Masala Tea");
        ConsoleFormatter.printMenuItem(3, "🥛 Badam Milk");
        ConsoleFormatter.printMenuItem(4, "🥤 Thums Up");
        ConsoleFormatter.printMenuItem(5, "🥭 Maaza");
        ConsoleFormatter.printDivider();

        System.out.println("\nYour choice: ");
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
                ConsoleFormatter.printError("You entered an invalid drink choice");
            }
        }

        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();
        System.out.printf("%sSelected: %s%s\n\n", ConsoleFormatter.CYAN, drinkName, ConsoleFormatter.RESET);

        System.out.printf("%s1)%s Small - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(3.00);
        System.out.println();

        System.out.printf("%s2)%s Medium - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(3.75);
        System.out.println();

        System.out.printf("%s3)%s Large - ", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printPrice(4.20);
        System.out.println();

        ConsoleFormatter.printDivider();

        System.out.println("\nYour choice: ");
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
                ConsoleFormatter.printError("invalid choice");
            }
        }
        Drink drink = new Drink(drinkName, drinkSize);
        currentOrder.addItem(drink);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order", drinkSize, drinkName));
    }

    private void addSauceToItem(MainItem item) {
        boolean addingSauces = true;

        sauceyLoop:while (addingSauces) {
            ConsoleFormatter.clearScreen();
            ConsoleFormatter.printHeader("ADD SAUCE (ALSO FREE!?!?)");
            System.out.println();

            ArrayList<Sauce> sauces = loadSauces();
            int sauceCounter = 1;
            for (Sauce sauce : sauces) {
                ConsoleFormatter.printMenuItem(sauceCounter++, sauce.getName());
            }
            ConsoleFormatter.printMenuItem(0, "✓ Done adding sauces");
            ConsoleFormatter.printDivider();

            System.out.println("\nYour choice: ");
            int sauceChoice = scanner.nextInt();
            scanner.nextLine();

            if (sauceChoice == 0) {
                addingSauces = false;
            } else if (sauceChoice > 0 && sauceChoice <= sauces.size()) {
                Sauce selectedSauce = new Sauce(sauces.get(sauceChoice - 1).getName());
                item.getSauces().add(selectedSauce);
                ConsoleFormatter.printSuccess(String.format("%s added!", selectedSauce.getName()));
                promptUserForEnter();
            } else {
                ConsoleFormatter.printError("Invalid Sauce, boss");
                promptUserForEnter();
            }
        }
    }

    private void addRegularToppingsToItem(MainItem item) {
        boolean addingRegularToppings = true;
        regularToppingLoop: while (addingRegularToppings) {
            ConsoleFormatter.clearScreen();
            ConsoleFormatter.printHeader("ADD REGULAR TOPPINGS (FREE!)");
            System.out.println();

            ArrayList<RegularTopping> regularToppings = loadRegularToppings();

            int counter = 1;
            for (RegularTopping regularTopping : regularToppings) {
                ConsoleFormatter.printMenuItem(counter++, regularTopping.getName());
            }
            ConsoleFormatter.printMenuItem(0, "✓ Done adding regular toppings");
            ConsoleFormatter.printDivider();

            int regularToppingChoice = scanner.nextInt();
            scanner.nextLine();

            if (regularToppingChoice == 0) {
                addingRegularToppings = false;
            } else if (regularToppingChoice > 0 && regularToppingChoice <= regularToppings.size()) {
                RegularTopping selectedRegularTopping = new RegularTopping(regularToppings.get(regularToppingChoice - 1).getName());
                item.getRegularToppings().add(selectedRegularTopping);
                ConsoleFormatter.printSuccess("Added!");
                promptUserForEnter();
            } else {
                ConsoleFormatter.printError("Invalid choice");
                promptUserForEnter();
            }
        }
    }

    private void addPremiumToppingsToItem(MainItem item) {
        boolean addingToppings = true;
        toppingLoop: while (addingToppings) {
            ConsoleFormatter.clearScreen();
            ConsoleFormatter.printHeader("ADD PREMIUM TOPPINGS");
            System.out.println();

            System.out.printf("%sPremium Topping Pricing:%s\n", ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
            System.out.println("  Small: $0.75 ($0.30 - Extra)");
            System.out.println("  Medium: $1.50 ($0.60 - Extra)");
            System.out.println("  Large: $2.25 ($0.90 - Extra)");
            System.out.println();

            ArrayList<PremiumTopping> premiumToppings = loadPremiumToppings();

            int premiumToppingCounter = 1;
            for (PremiumTopping premiumTopping : premiumToppings) {
                ConsoleFormatter.printMenuItem(premiumToppingCounter++, premiumTopping.getName());
            }
            ConsoleFormatter.printMenuItem(9, "✓ Done adding Premium Toppings");
            ConsoleFormatter.printMenuItem(0, "⏭ Skip Premium Toppings");
            ConsoleFormatter.printDivider();

            System.out.println("\nYour choice: ");
            int premiumToppingChoice = scanner.nextInt();
            scanner.nextLine();

            if (premiumToppingChoice == 9 || premiumToppingChoice == 0) {
                break toppingLoop;
            } else if (premiumToppingChoice > 0 && premiumToppingChoice <= premiumToppings.size()) {
                PremiumTopping selectedPremiumTopping = new PremiumTopping(premiumToppings.get(premiumToppingChoice - 1).getName());

                ConsoleFormatter.clearScreen();
                System.out.printf("%sWould you like extra %s?%s\n\n",
                        ConsoleFormatter.BOLD, selectedPremiumTopping.getName(), ConsoleFormatter.RESET);
                System.out.printf("%sExtra Premium Topping Pricing:%s\n", ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
                System.out.println("  Small: $0.30");
                System.out.println("  Medium: $0.60");
                System.out.println("  Large: $0.90");
                System.out.println();

                ConsoleFormatter.printMenuItem(1, "Yes");
                ConsoleFormatter.printMenuItem(2, "No");
                ConsoleFormatter.printDivider();

                System.out.print("\nYour choice: ");
                int extraPremiumSelection = scanner.nextInt();
                selectedPremiumTopping.setExtra(extraPremiumSelection == 1);
                item.getPremiumToppings().add(selectedPremiumTopping);

                if (selectedPremiumTopping.isExtra()) {
                    ConsoleFormatter.printSuccess(String.format("Extra %s Added!", selectedPremiumTopping.getName()));
                } else {
                    ConsoleFormatter.printSuccess(String.format("%s Added!", selectedPremiumTopping.getName()));
                }
                promptUserForEnter();
            } else {
                ConsoleFormatter.printError("Invalid choice");
                promptUserForEnter();
            }
        }
    }

    private void addProteinToItem(MainItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT YOUR PROTEIN");
        System.out.println();

        System.out.printf("%sProtein Pricing:%s\n", ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println("  Small: $1.00 ($0.50 - Extra)");
        System.out.println("  Medium: $2.00 ($1.00 - Extra)");
        System.out.println("  Large: $3.00 ($1.50 - Extra)");
        System.out.println();

        ArrayList<Protein> proteins = loadProteins();

        int proteinCounter = 1;
        for (Protein protein : proteins) {
            ConsoleFormatter.printMenuItem(proteinCounter++, protein.getName());
        }
        ConsoleFormatter.printMenuItem(0, "No protein");
        ConsoleFormatter.printDivider();

        System.out.println("\nYour choice");
        int proteinChoice = scanner.nextInt();
        scanner.nextLine();

        if (proteinChoice > 0 && proteinChoice <= proteins.size()) {
            Protein selectedProtein = new Protein(proteins.get(proteinChoice - 1).getName());

            ConsoleFormatter.clearScreen();
            System.out.printf("%sWould you like extra Protein?%s\n\n", ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
            System.out.printf("%sExtra Protein Pricing:%s\n", ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
            System.out.println("  Small: $0.50");
            System.out.println("  Medium: $1.00");
            System.out.println("  Large: $1.50");
            System.out.println();

            ConsoleFormatter.printMenuItem(1, "Yes");
            ConsoleFormatter.printMenuItem(2, "No");
            ConsoleFormatter.printDivider();

            System.out.println("\nYour choice: ");
            int extraProteinSelected = scanner.nextInt();
            selectedProtein.setExtra(extraProteinSelected == 1);
            scanner.nextLine();

            item.setProtein(selectedProtein);

            if (selectedProtein.isExtra()) {
                ConsoleFormatter.printSuccess(String.format("Extra %s added!", selectedProtein.getName()));
            } else {
                ConsoleFormatter.printSuccess(String.format("%s added!", selectedProtein.getName()));
            }
            promptUserForEnter();
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
}