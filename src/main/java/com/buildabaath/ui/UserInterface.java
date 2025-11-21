package com.buildabaath.ui;

import com.buildabaath.data.ReceiptWriter;
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
        displayMainMenu();
    }

    private void displayMainMenu() {
        boolean running = true;

        while (running) {
            ConsoleFormatter.clearScreen();
            ConsoleFormatter.printHeader("BUILD A BAATH - MAIN MENU");
            System.out.println();

            ConsoleFormatter.printMenuItem(1, "🆕 New Order");
            ConsoleFormatter.printMenuItem(2, "👋 Never mind, I like bland food");
            ConsoleFormatter.printDivider();

            System.out.print("\nYour choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    if (processNewOrder()) {
                        running = false; // Exit program after successful order
                    }
                }
                case 2 -> {
                    displayExitMessage();
                    running = false;
                }
                default -> ConsoleFormatter.printError(String.format("Unexpected Value: %d", choice));
            }
        }
    }

    private boolean processNewOrder() {
        Order currentOrder = new Order();
        return displayOrderMenu(currentOrder);
    }

    private boolean displayOrderMenu(Order currentOrder) {
        while (true) {
            displayOrderStatus(currentOrder);

            int choice = getUserOrderMenuChoice();

            switch (choice) {
                case 1 -> handleAddMainItem(currentOrder);
                case 2 -> handleAddSpecialtyItem(currentOrder);
                case 3 -> addDrinkToOrder(currentOrder);
                case 4 -> addSideToOrder(currentOrder);
                case 5 -> addDessertToOrder(currentOrder);
                case 6 -> {
                    if (handleCheckout(currentOrder)) {
                        return true; // Order completed - exit program
                    }
                }
                case 7 -> {
                    ConsoleFormatter.printBox("Order Cancelled. Lame.");
                    return false; // Order cancelled - return to main menu
                }
                default -> {
                    ConsoleFormatter.printBox("Invalid Option.\n Try again");
                    promptUserForEnter();
                }
            }
        }
    }

    private void displayOrderStatus(Order currentOrder) {
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
    }

    private int getUserOrderMenuChoice() {
        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void handleAddMainItem(Order currentOrder) {
        MainItemType selectedType = selectMainItemType();
        if (selectedType == null) return;

        String size = selectSize(selectedType.getDisplayName(), selectedType);
        if (size.isEmpty()) return;

        MainItem item = new MainItem(selectedType, size);

        addProteinToItem(item);
        addPremiumToppingsToItem(item);
        addRegularToppingsToItem(item);
        addSauceToItem(item);
        handleSpecialOrder(item);

        currentOrder.addItem(item);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order!", size, selectedType.getDisplayName()));
        promptUserForEnter();
    }

    private MainItemType selectMainItemType() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT YOUR MAIN ITEM");
        System.out.println();

        ArrayList<MainItemType> types = loadMainItemTypes();
        displayMainItemTypes(types);

        System.out.print("\nSelect item: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > types.size()) {
            ConsoleFormatter.printError("That is an invalid entry.\n");
            promptUserForEnter();
            return null;
        }

        return types.get(choice - 1);
    }

    private void displayMainItemTypes(ArrayList<MainItemType> types) {
        int counter = 1;
        for (MainItemType itemType : types) {
            System.out.printf("%s%d)%s %s\n",
                    ConsoleFormatter.BOLD, counter++,
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
    }

    private String selectSize(String itemName, MainItemType type) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();

        System.out.printf("%sSelected: %s%s\n\n",
                ConsoleFormatter.THANOS, itemName, ConsoleFormatter.RESET);

        ConsoleFormatter.printMenuItem(1, "Small", type.getPrice("small"));
        ConsoleFormatter.printMenuItem(2, "Medium", type.getPrice("medium"));
        ConsoleFormatter.printMenuItem(3, "Large", type.getPrice("large"));
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return switch (choice) {
            case 1 -> "small";
            case 2 -> "medium";
            case 3 -> "large";
            default -> {
                ConsoleFormatter.printError("Invalid size choice. Try again");
                promptUserForEnter();
                yield "";
            }
        };
    }

    private void handleSpecialOrder(MainItem item) {
        ConsoleFormatter.printBox("""
                =====Special Order??=====
                Would you like your order to be baked and served in a tandoori clay pot(+$2.00)?
                1. Yes
                2. No
                """);
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            item.setSpecialtyItem(true);
            item.setBasePrice(item.getBasePrice() + 2.00);
            ConsoleFormatter.printSuccess("Your dish will be served in a Tandoori Clay Pot!!!");
            promptUserForEnter();
        }
    }

    private void handleAddSpecialtyItem(Order currentOrder) {
        int specialtyChoice = displaySpecialtyItemMenu();
        if (specialtyChoice < 1 || specialtyChoice > 2) {
            ConsoleFormatter.printError("Invalid choice!");
            promptUserForEnter();
            return;
        }

        String size = selectSpecialtySize(specialtyChoice);
        if (size.isEmpty()) return;

        SpecialtyItem specialtyItem = createSpecialtyItem(specialtyChoice, size);

        if (promptCustomizeSpecialtyItem()) {
            customizeSpecialtyItem(specialtyItem);
        }

        currentOrder.addItem(specialtyItem);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order!", size, specialtyItem.getSpecialtyItemName()));
        promptUserForEnter();
    }

    private int displaySpecialtyItemMenu() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SPECIALTY ITEM");
        System.out.println();

        System.out.printf("%s ⭐️ CHEF SELECTED SPECIALTY BOWLS ⭐️%s\n\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);

        System.out.printf("""
                %s1) Tandoori Deluxe Bowl%s
                Base: Basmati Baath Bowl
                Protein: Tandoori Chicken
                Premium: Paneer Bhurji, Soft Boiled Egg
                Veggies: Cucumbers, Bell Peppers, Raw Onions, Cilantro
                Sauces: Butter Masala Gravy, Mint Chutney
                
                """, ConsoleFormatter.BOLD, ConsoleFormatter.RESET);

        System.out.printf("""
                %s2) Spicy Biryani Supreme%s
                Base: Dum Biryani
                Protein: Mutton Keema
                Premium: Chana Dal, Saffron Cauliflower Poriyal
                Veggies: Jalapeños, Raw Onions, Tomato, Cilantro
                Sauces: Kadhai Gravy, Rasam
                
                """, ConsoleFormatter.BOLD, ConsoleFormatter.RESET);

        ConsoleFormatter.printDivider();
        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private String selectSpecialtySize(int specialtyChoice) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();

        String specialtyName = (specialtyChoice == 1) ? "Tandoori Deluxe Bowl" : "Spicy Biryani Supreme";
        System.out.printf("%sSelected: %s%s\n\n",
                ConsoleFormatter.THANOS, specialtyName, ConsoleFormatter.RESET);

        displaySpecialtyPricing(specialtyChoice);

        System.out.print("\nYour choice: ");
        int sizeChoice = scanner.nextInt();
        scanner.nextLine();

        return switch (sizeChoice) {
            case 1 -> "small";
            case 2 -> "medium";
            case 3 -> "large";
            default -> {
                ConsoleFormatter.printError("Invalid size choice");
                promptUserForEnter();
                yield "";
            }
        };
    }

    private void displaySpecialtyPricing(int specialtyChoice) {
        if (specialtyChoice == 1) {
            SpecialtyItem small = SpecialtyItem.createTandooriDeluxeBowl("small");
            SpecialtyItem medium = SpecialtyItem.createTandooriDeluxeBowl("medium");
            SpecialtyItem large = SpecialtyItem.createTandooriDeluxeBowl("large");

            ConsoleFormatter.printMenuItem(1, "Small", small.calculatePrice());
            ConsoleFormatter.printMenuItem(2, "Medium", medium.calculatePrice());
            ConsoleFormatter.printMenuItem(3, "Large", large.calculatePrice());
        } else {
            SpecialtyItem small = SpecialtyItem.createSpicyBiryaniSupreme("small");
            SpecialtyItem medium = SpecialtyItem.createSpicyBiryaniSupreme("medium");
            SpecialtyItem large = SpecialtyItem.createSpicyBiryaniSupreme("large");

            ConsoleFormatter.printMenuItem(1, "Small", small.calculatePrice());
            ConsoleFormatter.printMenuItem(2, "Medium", medium.calculatePrice());
            ConsoleFormatter.printMenuItem(3, "Large", large.calculatePrice());
        }
        ConsoleFormatter.printDivider();
    }

    private SpecialtyItem createSpecialtyItem(int choice, String size) {
        return (choice == 1) ?
                SpecialtyItem.createTandooriDeluxeBowl(size) :
                SpecialtyItem.createSpicyBiryaniSupreme(size);
    }

    private boolean promptCustomizeSpecialtyItem() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("CUSTOMIZE SPECIALTY ITEM?");
        System.out.println();

        System.out.printf("%sThis item comes pre-configured with all toppings.%s\n",
                ConsoleFormatter.CYAN, ConsoleFormatter.RESET);
        System.out.println("Would you like to modify it?\n");

        ConsoleFormatter.printMenuItem(1, "Yes, let me customize it");
        ConsoleFormatter.printMenuItem(2, "No, keep it as is");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice == 1;
    }

    private void customizeSpecialtyItem(SpecialtyItem item) {
        boolean customizing = true;

        while (customizing) {
            displayCustomizationMenu(item);

            System.out.print("\nYour choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            customizing = handleCustomizationChoice(item, choice);
        }
    }

    private void displayCustomizationMenu(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("CUSTOMIZE SPECIALTY ITEM");
        System.out.println();

        System.out.printf("%sCurrent Configuration:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println(item.getDescription());
        System.out.println();

        ConsoleFormatter.printDivider();
        ConsoleFormatter.printMenuItem(1, "🍗 Modify Protein");
        ConsoleFormatter.printMenuItem(2, "⭐ Add/Remove Premium Toppings");
        ConsoleFormatter.printMenuItem(3, "🥗 Add/Remove Regular Toppings");
        ConsoleFormatter.printMenuItem(4, "🍛 Add/Remove Sauces");
        ConsoleFormatter.printMenuItem(0, "✓ Done Customizing");
        ConsoleFormatter.printDivider();
    }

    private boolean handleCustomizationChoice(SpecialtyItem item, int choice) {
        switch (choice) {
            case 1 -> modifySpecialtyProtein(item);
            case 2 -> modifyPremiumTopping(item);
            case 3 -> modifyRegularToppings(item);
            case 4 -> modifySauces(item);
            case 0 -> {
                return false;
            }
        }
        return true;
    }

    private void modifySpecialtyProtein(SpecialtyItem item) {
        displayProteinModificationMenu(item);

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        handleProteinModification(item, choice);
    }

    private void displayProteinModificationMenu(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("MODIFY PROTEIN");
        System.out.println();

        String currentProtein = (item.getProtein() != null) ?
                item.getProtein().getName() : "None";

        System.out.printf("%sCurrent Protein:%s %s\n\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET, currentProtein);

        ConsoleFormatter.printMenuItem(1, "Change Protein");
        ConsoleFormatter.printMenuItem(2, "Remove Protein");
        ConsoleFormatter.printMenuItem(3, "Make it Extra");
        ConsoleFormatter.printMenuItem(0, "Go Back");
        ConsoleFormatter.printDivider();
    }

    private void handleProteinModification(SpecialtyItem item, int choice) {
        switch (choice) {
            case 1 -> {
                item.setProtein(null);
                addProteinToItem(item);
            }
            case 2 -> {
                item.setProtein(null);
                ConsoleFormatter.printSuccess("Protein removed!");
                promptUserForEnter();
            }
            case 3 -> {
                if (item.getProtein() != null) {
                    item.getProtein().setExtra(true);
                    ConsoleFormatter.printSuccess("Protein is now extra!");
                } else {
                    ConsoleFormatter.printError("No protein to make extra!");
                }
                promptUserForEnter();
            }
        }
    }

    private void modifyPremiumTopping(SpecialtyItem item) {
        displayPremiumToppingMenu(item);

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        handlePremiumToppingModification(item, choice);
    }

    private void displayPremiumToppingMenu(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("MODIFY PREMIUM TOPPINGS");
        System.out.println();

        System.out.printf("%sCurrent Premium Toppings:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);

        if (item.getPremiumToppings().isEmpty()) {
            System.out.println("None");
        } else {
            displayPremiumToppingList(item);
        }
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Add Premium Topping");
        ConsoleFormatter.printMenuItem(2, "Remove Premium Topping");
        ConsoleFormatter.printMenuItem(3, "Make Topping Extra");
        ConsoleFormatter.printMenuItem(0, "Go Back");
        ConsoleFormatter.printDivider();
    }

    private void displayPremiumToppingList(SpecialtyItem item) {
        int counter = 1;
        for (PremiumTopping topping : item.getPremiumToppings()) {
            String extra = topping.isExtra() ? " (Extra)" : "";
            System.out.printf("  %d. %s%s\n", counter++, topping.getName(), extra);
        }
    }

    private void handlePremiumToppingModification(SpecialtyItem item, int choice) {
        switch (choice) {
            case 1 -> addPremiumToppingsToItem(item);
            case 2 -> removePremiumToppingsFromItem(item);
            case 3 -> makeSpecialtyPremiumToppingExtra(item);
        }
    }

    private void removePremiumToppingsFromItem(SpecialtyItem item) {
        if (item.getPremiumToppings().isEmpty()) {
            ConsoleFormatter.printError("You don't have any premium toppings on your item");
            promptUserForEnter();
            return;
        }

        int choice = selectPremiumToppingToRemove(item);

        if (choice > 0 && choice <= item.getPremiumToppings().size()) {
            String removed = item.getPremiumToppings().get(choice - 1).getName();
            item.getPremiumToppings().remove(choice - 1);
            ConsoleFormatter.printSuccess(removed + " removed from item");
        }
        promptUserForEnter();
    }

    private int selectPremiumToppingToRemove(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        System.out.println("Select topping to remove:\n");

        int counter = 1;
        for (PremiumTopping topping : item.getPremiumToppings()) {
            ConsoleFormatter.printMenuItem(counter++, topping.getName());
        }
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void makeSpecialtyPremiumToppingExtra(SpecialtyItem item) {
        if (item.getPremiumToppings().isEmpty()) {
            ConsoleFormatter.printError("No premium toppings available to add extra");
            promptUserForEnter();
            return;
        }

        int choice = selectPremiumToppingForExtra(item);

        if (choice > 0 && choice <= item.getPremiumToppings().size()) {
            item.getPremiumToppings().get(choice - 1).setExtra(true);
            ConsoleFormatter.printSuccess("Made extra!");
            promptUserForEnter();
        }
    }

    private int selectPremiumToppingForExtra(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        System.out.println("Select topping to make extra:\n");

        int counter = 1;
        for (PremiumTopping topping : item.getPremiumToppings()) {
            ConsoleFormatter.printMenuItem(counter++, topping.getName());
        }
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void modifyRegularToppings(SpecialtyItem item) {
        displayRegularToppingMenu(item);

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        handleRegularToppingModification(item, choice);
    }

    private void displayRegularToppingMenu(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("MODIFY REGULAR TOPPINGS");
        System.out.println();

        System.out.printf("%sCurrent Regular Toppings:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);

        if (item.getRegularToppings().isEmpty()) {
            System.out.println("None");
        } else {
            displayRegularToppingList(item);
        }
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Add Regular Topping");
        ConsoleFormatter.printMenuItem(2, "Remove Regular Topping");
        ConsoleFormatter.printMenuItem(0, "Go Back");
        ConsoleFormatter.printDivider();
    }

    private void displayRegularToppingList(SpecialtyItem item) {
        int counter = 1;
        for (RegularTopping topping : item.getRegularToppings()) {
            System.out.printf("  %d. %s\n", counter++, topping.getName());
        }
    }

    private void handleRegularToppingModification(SpecialtyItem item, int choice) {
        switch (choice) {
            case 1 -> addRegularToppingsToItem(item);
            case 2 -> removeRegularTopping(item);
        }
    }

    private void removeRegularTopping(SpecialtyItem item) {
        if (item.getRegularToppings().isEmpty()) {
            ConsoleFormatter.printError("No regular toppings to remove!");
            promptUserForEnter();
            return;
        }

        int choice = selectRegularToppingToRemove(item);

        if (choice > 0 && choice <= item.getRegularToppings().size()) {
            String removed = item.getRegularToppings().get(choice - 1).getName();
            item.getRegularToppings().remove(choice - 1);
            ConsoleFormatter.printSuccess(removed + " removed!");
        }
        promptUserForEnter();
    }

    private int selectRegularToppingToRemove(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        System.out.println("Select topping to remove:\n");

        int counter = 1;
        for (RegularTopping topping : item.getRegularToppings()) {
            ConsoleFormatter.printMenuItem(counter++, topping.getName());
        }
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void modifySauces(SpecialtyItem item) {
        displaySauceMenu(item);

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        handleSauceModification(item, choice);
    }

    private void displaySauceMenu(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("MODIFY SAUCES");
        System.out.println();

        System.out.printf("%sCurrent Sauces:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);

        if (item.getSauces().isEmpty()) {
            System.out.println("None");
        } else {
            displaySauceList(item);
        }
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Add Sauce");
        ConsoleFormatter.printMenuItem(2, "Remove Sauce");
        ConsoleFormatter.printMenuItem(0, "Go Back");
        ConsoleFormatter.printDivider();
    }

    private void displaySauceList(SpecialtyItem item) {
        int counter = 1;
        for (Sauce sauce : item.getSauces()) {
            System.out.printf("  %d. %s\n", counter++, sauce.getName());
        }
    }

    private void handleSauceModification(SpecialtyItem item, int choice) {
        switch (choice) {
            case 1 -> addSauceToItem(item);
            case 2 -> removeSauce(item);
        }
    }

    private void removeSauce(SpecialtyItem item) {
        if (item.getSauces().isEmpty()) {
            ConsoleFormatter.printError("No sauces to remove!");
            promptUserForEnter();
            return;
        }

        int choice = selectSauceToRemove(item);

        if (choice > 0 && choice <= item.getSauces().size()) {
            String removed = item.getSauces().get(choice - 1).getName();
            item.getSauces().remove(choice - 1);
            ConsoleFormatter.printSuccess(removed + " removed!");
        }
        promptUserForEnter();
    }

    private int selectSauceToRemove(SpecialtyItem item) {
        ConsoleFormatter.clearScreen();
        System.out.println("Select sauce to remove:\n");

        int counter = 1;
        for (Sauce sauce : item.getSauces()) {
            ConsoleFormatter.printMenuItem(counter++, sauce.getName());
        }
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private boolean handleCheckout(Order currentOrder) {
        if (currentOrder.getItems().isEmpty()) {
            ConsoleFormatter.printWarning("Your order is empty. You can't checkout if there's nothing to checkout bro...");
            promptUserForEnter();
            return false;
        }

        checkOutOrder(currentOrder);

        if (confirmOrder(currentOrder)) {
            saveReceipt(currentOrder);
            return true; // Exit program
        }
        return false; // Continue ordering
    }

    private boolean confirmOrder(Order currentOrder) {
        System.out.printf("\n%sConfirm your order?%s\n",
                ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        ConsoleFormatter.printBox(String.format("Order Total: $%.2f", currentOrder.getFinalTotal()));
        ConsoleFormatter.printMenuItem(1, "✓ Confirm Order");
        ConsoleFormatter.printMenuItem(2, "✗ Go Back");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice == 1;
    }

    private void saveReceipt(Order currentOrder) {
        ReceiptWriter receiptWriter = new ReceiptWriter(currentOrder);
        receiptWriter.saveReceiptToFile();
        ConsoleFormatter.printBox("Thank you for your order! You can find your receipt in the folder\nPlease come again! 🙏");
        promptUserForEnter();
    }

    private void checkOutOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        viewOrderSummary(currentOrder);

        System.out.print("\nSubtotal: ");
        ConsoleFormatter.printPrice(currentOrder.getTotalPrice());
        System.out.println();
        ConsoleFormatter.printDivider();

        promptForTip(currentOrder);

        displayFinalOrderSummary(currentOrder);
    }

    private void promptForTip(Order currentOrder) {
        System.out.printf("\n%s💰 Please select a Tip Amount%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.printf("%sWe are severely underpaid. Please.%s\n",
                ConsoleFormatter.CYAN, ConsoleFormatter.RESET);
        System.out.println();

        displayTipOptions(currentOrder);

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        processTipChoice(currentOrder, choice);
    }

    private void displayTipOptions(Order currentOrder) {
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
    }

    private void processTipChoice(Order currentOrder, int choice) {
        switch (choice) {
            case 1 -> currentOrder.setTipAmount(15.00);
            case 2 -> currentOrder.setTipAmount(20.00);
            case 3 -> currentOrder.setTipAmount(25.00);
            case 4 -> {
                System.out.print("Enter Tip Percentage: ");
                double customTip = scanner.nextDouble();
                scanner.nextLine();
                currentOrder.setTipAmount(customTip);
            }
            case 5 -> currentOrder.setTipAmount(0);
        }
    }

    private void displayFinalOrderSummary(Order currentOrder) {
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
            System.out.printf("%sItem #%d%s\n",
                    ConsoleFormatter.BOLD, itemNumber++, ConsoleFormatter.RESET);
            System.out.println(item.getDescription());
            System.out.print("Price: ");
            ConsoleFormatter.printPrice(item.calculatePrice());
            System.out.println();
        }
    }

    private void addDessertToOrder(Order currentOrder) {
        String dessertName = selectDessert();
        if (dessertName.isEmpty()) return;

        String size = selectDessertSize(dessertName);
        if (size.isEmpty()) return;

        Dessert dessert = new Dessert(dessertName, size);
        currentOrder.addItem(dessert);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order!", size, dessertName));
        promptUserForEnter();
    }

    private String selectDessert() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A DESSERT");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Ras Malai");
        ConsoleFormatter.printMenuItem(2, "Gulab Jamun");
        ConsoleFormatter.printMenuItem(3, "Jalebi");
        ConsoleFormatter.printMenuItem(4, "Kaju Katli");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return switch (choice) {
            case 1 -> "Ras Malai";
            case 2 -> "Gulab Jamun";
            case 3 -> "Jalebi";
            case 4 -> "Kaju Katli";
            default -> {
                ConsoleFormatter.printError("You've entered the wrong dessert");
                yield "";
            }
        };
    }

    private String selectDessertSize(String dessertName) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();
        System.out.printf("%sSelected: %s%s\n\n",
                ConsoleFormatter.CYAN, dessertName, ConsoleFormatter.RESET);

        ConsoleFormatter.printMenuItem(1, "Small", 2.50);
        ConsoleFormatter.printMenuItem(2, "Medium", 4.00);
        ConsoleFormatter.printMenuItem(3, "Large", 5.50);
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return switch (choice) {
            case 1 -> "small";
            case 2 -> "medium";
            case 3 -> "large";
            default -> {
                ConsoleFormatter.printError("You've entered an invalid option");
                yield "";
            }
        };
    }

    private void addSideToOrder(Order currentOrder) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A SIDE");

        ArrayList<Side> sides = loadSides();
        displaySides(sides);

        System.out.print("\nYour Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= sides.size()) {
            Side side = new Side(sides.get(choice - 1).getName());
            currentOrder.addItem(side);
            currentOrder.updateTotalPrice();
            ConsoleFormatter.printSuccess(String.format("%s added to order!", side.getName()));
        } else {
            ConsoleFormatter.printError("You entered an invalid side choice");
        }
        promptUserForEnter();
    }

    private void displaySides(ArrayList<Side> sides) {
        int counter = 1;
        for (Side side : sides) {
            System.out.printf("%s%d)%s %-30s ",
                    ConsoleFormatter.BOLD, counter++, ConsoleFormatter.RESET, side.getName());
            ConsoleFormatter.printPrice(1.50);
            System.out.println();
        }
        ConsoleFormatter.printDivider();
    }

    private void addDrinkToOrder(Order currentOrder) {
        String drinkName = selectDrink();
        if (drinkName.isEmpty()) return;

        String size = selectDrinkSize(drinkName);
        if (size.isEmpty()) return;

        Drink drink = new Drink(drinkName, size);
        currentOrder.addItem(drink);
        currentOrder.updateTotalPrice();
        ConsoleFormatter.printSuccess(String.format("%s %s added to order", size, drinkName));
        promptUserForEnter();
    }

    private String selectDrink() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT A DRINK");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "☕️ Filter Coffee");
        ConsoleFormatter.printMenuItem(2, "🫖 Masala Tea");
        ConsoleFormatter.printMenuItem(3, "🥛 Badam Milk");
        ConsoleFormatter.printMenuItem(4, "🥤 Thums Up");
        ConsoleFormatter.printMenuItem(5, "🥭 Maaza");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return switch (choice) {
            case 1 -> "Filter Coffee";
            case 2 -> "Masala Tea";
            case 3 -> "Badam Milk";
            case 4 -> "Thums Up";
            case 5 -> "Maaza";
            default -> {
                ConsoleFormatter.printError("You entered an invalid drink choice");
                yield "";
            }
        };
    }

    private String selectDrinkSize(String drinkName) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT SIZE");
        System.out.println();
        System.out.printf("%sSelected: %s%s\n\n",
                ConsoleFormatter.CYAN, drinkName, ConsoleFormatter.RESET);

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

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return switch (choice) {
            case 1 -> "small";
            case 2 -> "medium";
            case 3 -> "large";
            default -> {
                ConsoleFormatter.printError("invalid choice");
                yield "";
            }
        };
    }

    private void addSauceToItem(MainItem item) {
        boolean addingSauces = true;

        while (addingSauces) {
            addingSauces = displaySauceSelectionMenu(item);
        }
    }

    private boolean displaySauceSelectionMenu(MainItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("ADD SAUCE (ALSO FREE!?!?)");
        System.out.println();

        ArrayList<Sauce> sauces = loadSauces();
        int counter = 1;
        for (Sauce sauce : sauces) {
            ConsoleFormatter.printMenuItem(counter++, sauce.getName());
        }
        ConsoleFormatter.printMenuItem(0, "✓ Done adding sauces");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) {
            return false;
        } else if (choice > 0 && choice <= sauces.size()) {
            Sauce selectedSauce = new Sauce(sauces.get(choice - 1).getName());
            item.getSauces().add(selectedSauce);
            ConsoleFormatter.printSuccess(String.format("%s added!", selectedSauce.getName()));
            promptUserForEnter();
        } else {
            ConsoleFormatter.printError("Invalid Sauce, boss");
            promptUserForEnter();
        }
        return true;
    }

    private void addRegularToppingsToItem(MainItem item) {
        boolean addingToppings = true;

        while (addingToppings) {
            addingToppings = displayRegularToppingSelectionMenu(item);
        }
    }

    private boolean displayRegularToppingSelectionMenu(MainItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("ADD REGULAR TOPPINGS (FREE!)");
        System.out.println();

        ArrayList<RegularTopping> regularToppings = loadRegularToppings();

        int counter = 1;
        for (RegularTopping topping : regularToppings) {
            ConsoleFormatter.printMenuItem(counter++, topping.getName());
        }
        ConsoleFormatter.printMenuItem(0, "✓ Done adding regular toppings");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) {
            return false;
        } else if (choice > 0 && choice <= regularToppings.size()) {
            RegularTopping selected = new RegularTopping(regularToppings.get(choice - 1).getName());
            item.getRegularToppings().add(selected);
            ConsoleFormatter.printSuccess("Added!");
            promptUserForEnter();
        } else {
            ConsoleFormatter.printError("Invalid choice");
            promptUserForEnter();
        }
        return true;
    }

    private void addPremiumToppingsToItem(MainItem item) {
        boolean addingToppings = true;

        while (addingToppings) {
            addingToppings = displayPremiumToppingSelectionMenu(item);
        }
    }

    private boolean displayPremiumToppingSelectionMenu(MainItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("ADD PREMIUM TOPPINGS");
        System.out.println();

        System.out.printf("%sPremium Topping Pricing:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println("  Small: $0.75 ($0.30 - Extra)");
        System.out.println("  Medium: $1.50 ($0.60 - Extra)");
        System.out.println("  Large: $2.25 ($0.90 - Extra)");
        System.out.println();

        ArrayList<PremiumTopping> premiumToppings = loadPremiumToppings();

        int counter = 1;
        for (PremiumTopping topping : premiumToppings) {
            ConsoleFormatter.printMenuItem(counter++, topping.getName());
        }
        ConsoleFormatter.printMenuItem(9, "✓ Done adding Premium Toppings");
        ConsoleFormatter.printMenuItem(0, "⏭ Skip Premium Toppings");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 9 || choice == 0) {
            return false;
        } else if (choice > 0 && choice <= premiumToppings.size()) {
            PremiumTopping selected = new PremiumTopping(premiumToppings.get(choice - 1).getName());

            if (promptForExtraPremiumTopping(selected.getName())) {
                selected.setExtra(true);
            }

            item.getPremiumToppings().add(selected);

            String message = selected.isExtra() ?
                    String.format("Extra %s Added!", selected.getName()) :
                    String.format("%s Added!", selected.getName());
            ConsoleFormatter.printSuccess(message);
            promptUserForEnter();
        } else {
            ConsoleFormatter.printError("Invalid choice");
            promptUserForEnter();
        }
        return true;
    }

    private boolean promptForExtraPremiumTopping(String toppingName) {
        ConsoleFormatter.clearScreen();
        System.out.printf("%sWould you like extra %s?%s\n\n",
                ConsoleFormatter.BOLD, toppingName, ConsoleFormatter.RESET);
        System.out.printf("%sExtra Premium Topping Pricing:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println("  Small: $0.30");
        System.out.println("  Medium: $0.60");
        System.out.println("  Large: $0.90");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Yes");
        ConsoleFormatter.printMenuItem(2, "No");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice == 1;
    }

    private void addProteinToItem(MainItem item) {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printHeader("SELECT YOUR PROTEIN");
        System.out.println();

        System.out.printf("%sProtein Pricing:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println("  Small: $1.00 ($0.50 - Extra)");
        System.out.println("  Medium: $2.00 ($1.00 - Extra)");
        System.out.println("  Large: $3.00 ($1.50 - Extra)");
        System.out.println();

        ArrayList<Protein> proteins = loadProteins();

        int counter = 1;
        for (Protein protein : proteins) {
            ConsoleFormatter.printMenuItem(counter++, protein.getName());
        }
        ConsoleFormatter.printMenuItem(0, "No protein");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= proteins.size()) {
            Protein selected = new Protein(proteins.get(choice - 1).getName());

            if (promptForExtraProtein()) {
                selected.setExtra(true);
            }

            item.setProtein(selected);

            String message = selected.isExtra() ?
                    String.format("Extra %s added!", selected.getName()) :
                    String.format("%s added!", selected.getName());
            ConsoleFormatter.printSuccess(message);
            promptUserForEnter();
        }
    }

    private boolean promptForExtraProtein() {
        ConsoleFormatter.clearScreen();
        System.out.printf("%sWould you like extra Protein?%s\n\n",
                ConsoleFormatter.BOLD, ConsoleFormatter.RESET);
        System.out.printf("%sExtra Protein Pricing:%s\n",
                ConsoleFormatter.YELLOW, ConsoleFormatter.RESET);
        System.out.println("  Small: $0.50");
        System.out.println("  Medium: $1.00");
        System.out.println("  Large: $1.50");
        System.out.println();

        ConsoleFormatter.printMenuItem(1, "Yes");
        ConsoleFormatter.printMenuItem(2, "No");
        ConsoleFormatter.printDivider();

        System.out.print("\nYour choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice == 1;
    }

    private void showWelcomeScreen() {
        ConsoleFormatter.clearScreen();
        System.out.println("""
                ▗▄▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖   ▗▄▄▄      ▗▄▖     ▗▄▄▖  ▗▄▖  ▗▄▖▗▄▄▄▖▗▖ ▗▖
                ▐▌ ▐▌▐▌ ▐▌  █  ▐▌   ▐▌  █    ▐▌ ▐▌    ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌ █  ▐▌ ▐▌
                ▐▛▀▚▖▐▌ ▐▌  █  ▐▌   ▐▌  █    ▐▛▀▜▌    ▐▛▀▚▖▐▛▀▜▌▐▛▀▜▌ █  ▐▛▀▜▌
                ▐▙▄▞▘▝▚▄▞▘▗▄█▄▖▐▙▄▄▖▐▙▄▄▀    ▐▌ ▐▌    ▐▙▄▞▘▐▌ ▐▌▐▌ ▐▌ █  ▐▌ ▐▌
                """);
        System.out.println("""
                ⠀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠉⠛⠛⠓⠶⠶⠦⠤⢤⣄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⣤⣤⣤⣤⣄⣀⣀⣀⣀⡀⠀⠈⠉⠉⠙⠛⠛⠶⠶⠶⣤⡄⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠙⠛⠛⠛⠛⠳⠶⠶⠶⠶⣤⣤⡄⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣐⣶⣿⣦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⢠⣰⣤⣶⣿⣛⢻⣿⣿⣿⣿⣿⣦⣼⣤⣆⣀⠀⡀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⣨⣿⣿⢿⣿⣿⣿⣿⣿⣥⣼⣿⢿⣿⣿⣟⠻⣿⣯⣤⡄⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠛⠛⠒⠒⠛⠛⠛⠛⠛⠛⠛⠛⠒⠚⠛⠛⠛⠛⠛⠛⠃⠀⠀⠀⠀
                ⠀⠀⢰⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⡆⠀⠀
                ⠀⠀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀
                ⠀⠀⠀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠀⠀⠀
                ⠀⠀⠀⠀⠀⠙⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢉⣉⣉⣉⣉⣉⣉⡉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                """);
        ConsoleFormatter.printBox("""
                🍛 Welcome to Build a Baath! 🍛
                Your favorite Build-Your-Own Indian Concept!
                """);

        promptUserForEnter();
    }

    private void displayExitMessage() {
        ConsoleFormatter.clearScreen();
        ConsoleFormatter.printBox("Thanks for visiting Build a Baath and have a tasty day");
    }

    private void promptUserForEnter() {
        System.out.printf("\n%s Press Enter to Continue.....%s\n",
                ConsoleFormatter.THANOS, ConsoleFormatter.RESET);
        scanner.nextLine();
    }

    // Data loading methods
    private ArrayList<MainItemType> loadMainItemTypes() {
        ArrayList<MainItemType> types = new ArrayList<>();
        types.add(new MainItemType("Rice", "Basmati Baath Bowl", 3.50, 6.00, 8.50));
        types.add(new MainItemType("Naan", "Baked Naan Flatbread", 3.50, 6.00, 8.50));
        types.add(new MainItemType("Chaat", "Street Samosa Chaat", 4.00, 6.50, 9.00));
        types.add(new MainItemType("Biryani", "Dum Biryani", 4.00, 6.50, 9.00));
        return types;
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
        ArrayList<PremiumTopping> toppings = new ArrayList<>();
        toppings.add(new PremiumTopping("Chana Dal(Chick-peas)"));
        toppings.add(new PremiumTopping("Saffron Cauliflower Poriyal"));
        toppings.add(new PremiumTopping("Paneer Bhurji"));
        toppings.add(new PremiumTopping("Soft Boiled Egg"));
        return toppings;
    }

    private ArrayList<RegularTopping> loadRegularToppings() {
        ArrayList<RegularTopping> toppings = new ArrayList<>();
        toppings.add(new RegularTopping("Cucumbers"));
        toppings.add(new RegularTopping("Beet Root"));
        toppings.add(new RegularTopping("Bell Peppers"));
        toppings.add(new RegularTopping("Jalapeños"));
        toppings.add(new RegularTopping("Raw Onions"));
        toppings.add(new RegularTopping("Carrots"));
        toppings.add(new RegularTopping("Cilantro"));
        toppings.add(new RegularTopping("Tomato"));
        toppings.add(new RegularTopping("Lettuce"));
        return toppings;
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