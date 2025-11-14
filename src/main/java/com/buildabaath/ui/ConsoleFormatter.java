package com.buildabaath.ui;

public class ConsoleFormatter {
    // ANSI color codes
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String THANOS = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    // Box drawing characters
    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String HORIZONTAL = "═";
    private static final String VERTICAL = "║";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printHeader(String text) {
        int width = 50;
        int padding = (width - text.length() - 2) / 2;

        System.out.printf("%s%s%s%s%s\n", THANOS, TOP_LEFT, HORIZONTAL.repeat(width), TOP_RIGHT, RESET);
        System.out.printf("%s%s%s%s%s%s%s%s%s\n",
                THANOS, VERTICAL, RESET,
                " ".repeat(padding),
                BOLD, YELLOW, text, RESET,
                " ".repeat(width - padding - text.length()));
        System.out.printf("%s%s%s\n", THANOS, VERTICAL, RESET);
        System.out.printf("%s%s%s%s%s\n", THANOS, BOTTOM_LEFT, HORIZONTAL.repeat(width), BOTTOM_RIGHT, RESET);
    }

    public static void printDivider() {
        System.out.printf("%s%s%s\n", BLUE, "─".repeat(52), RESET);
    }

    public static void printSuccess(String message) {
        System.out.printf("%s✓ %s%s\n", GREEN, message, RESET);
    }

    public static void printError(String message) {
        System.out.printf("%s✗ %s%s\n", RED, message, RESET);
    }

    public static void printWarning(String message) {
        System.out.printf("%s⚠ %s%s\n", YELLOW, message, RESET);
    }

    public static void printPrice(double price) {
        System.out.printf("%s$%.2f%s", GREEN, price, RESET);
    }

    // Menu item with price
    public static void printMenuItem(int number, String name, double price) {
        System.out.printf("%s%2d)%s %-30s ", BOLD, number, RESET, name);
        printPrice(price);
        System.out.println();
    }

    // Menu item without price (overloaded method)
    public static void printMenuItem(int number, String name) {
        System.out.printf("%s%2d)%s %s\n", BOLD, number, RESET, name);
    }

    public static void printBox(String content) {
        String[] lines = content.split("\n");
        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) maxLength = line.length();
        }

        System.out.printf("%s┌%s┐%s\n", THANOS, "─".repeat(maxLength + 2), RESET);
        for (String line : lines) {
            System.out.printf("%s│%s %s%s %s│%s\n",
                    THANOS, RESET, line,
                    " ".repeat(maxLength - line.length()),
                    THANOS, RESET);
        }
        System.out.printf("%s└%s┘%s\n", THANOS, "─".repeat(maxLength + 2), RESET);
    }

    public static void printProgressBar(int current, int total) {
        int barLength = 30;
        int filled = (int) ((current / (double) total) * barLength);

        System.out.print("\r[");
        System.out.printf("%s%s%s", GREEN, "█".repeat(filled), RESET);
        System.out.print(" ".repeat(barLength - filled));
        System.out.printf("] %d/%d items", current, total);
    }
}


