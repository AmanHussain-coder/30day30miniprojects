import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Expense {
    String date;
    String category;
    String description;
    double amount;

    public Expense(String date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public String toString() {
        return String.format("Date: %s | Category: %s | Description: %s | Amount: %.2f",
                             date, category, description, amount);
    }

    public String toFileString() {
        return date + "," + category + "," + description + "," + amount;
    }

    public static Expense fromFileString(String line) {
        String[] parts = line.split(",", 4);
        return new Expense(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
    }
}

public class ExpenseTracker {
    static ArrayList<Expense> expenses = new ArrayList<>();
    static final String FILE_NAME = "expenses.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadExpensesFromFile();

        boolean running = true;
        while (running) {
            System.out.println("\n====== Expense Tracker ======");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Expenses");
            System.out.println("4. Filter by Category");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewAllExpenses();
                case 3 -> viewTotalExpenses();
                case 4 -> filterByCategory();
                case 5 -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void loadExpensesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(Expense.fromFileString(line));
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, no problem
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void saveExpenseToFile(Expense e) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(e.toFileString());
            writer.newLine();
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }
    }

    public static void addExpense() {
        System.out.print("Enter date (e.g. 2025-05-28): ");
        String date = scanner.nextLine();

        System.out.print("Enter category (e.g. Food, Travel): ");
        String category = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        Expense e = new Expense(date, category, description, amount);
        expenses.add(e);
        saveExpenseToFile(e);
        System.out.println("Expense added and saved!");
    }

    public static void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("\n--- All Expenses ---");
        for (Expense e : expenses) {
            System.out.println(e);
        }
    }

    public static void viewTotalExpenses() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.printf("Total Expenses: %.2f\n", total);
    }

    public static void filterByCategory() {
        System.out.print("Enter category to filter by: ");
        String category = scanner.nextLine();
        boolean found = false;

        System.out.println("\n--- Expenses in Category: " + category + " ---");
        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase(category)) {
                System.out.println(e);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }
}