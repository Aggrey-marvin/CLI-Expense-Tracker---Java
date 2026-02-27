package com.azijja.expense.tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Executor {
    public static void executeCommand(CommandArgsData cmdData) {
        switch (cmdData.command()) {
            case ADD:
                handleAdd(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case LIST:
                handleList(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case SUMMARY:
                handleSummary(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case DELETE:
                handleDelete(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case CREATE_CATEGORY:
                handleCreateCategory(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case LIST_CATEGORIES:
                handlelistCategories(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            case DELETE_CATEGORY:
                handleDeleteCategory(cmdData.commandArgs(), cmdData.valueArgs(), cmdData.formattedDateString());
                break;
            default:
                System.out.println("\n❌ Error: Command handler not implemented for '" + cmdData.command().name().toLowerCase() + "'\n");
        }
    }

    private static void handleAdd(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        int latestId = FileManager.getLatestId("expense");
        String description = valueArgs.get(commandArgs.indexOf("--description"));
        try {
            Double.parseDouble(valueArgs.get(commandArgs.indexOf("--amount")));
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: Invalid amount value. Please enter a valid number for '--amount'\n");
            return;
        }
        double amount = Double.parseDouble(valueArgs.get(commandArgs.indexOf("--amount")));

        Scanner scanner = new Scanner(System.in);
        printCategories();
        System.out.print("Enter category ID for this expense (default is 1 for 'Uncategorized'): ");
        String categoryInput = scanner.nextLine().trim();
        int categoryId = 1; // Default to 'Uncategorized'
        if (!categoryInput.isEmpty()) {
            try {
                Integer.parseInt(categoryInput);
            } catch (NumberFormatException e) {
                System.out.println("\n❌ Error: Invalid category ID. Please enter a valid number for category ID\n");
                scanner.close();
                return;
            }
            categoryId = Integer.parseInt(categoryInput);
        }
        scanner.close();

        String expenseFileName = FileManager.getExpenseFileName(categoryId);

        File categoryFile = new File(expenseFileName);
        if (!categoryFile.exists()) {
            System.out.println("\n❌ Error: Category with ID " + categoryId + " does not exist. Please create the category first before adding expenses to it.\n");
            return;
        }

        String expenseData = (latestId + 1) + "," + formattedDate + "," + description + "," + amount;
        FileManager.appendExpenseToCsv(expenseFileName, expenseData);
        FileManager.updateLatestRecordId(latestId + 1, "expense");

        System.out.println("Date: " + formattedDate + ", Description: " + description + ", Amount: " + amount + ", ID: " + (latestId + 1));
    }

    private static void handleList(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        ExpenseFileData expenseFilesData = FileManager.getExpenseFiles();

        // To continue from there later....

        JSONArray expenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
        System.out.println("# ID  Date        Description      Amount");
        for (int i = 0; i < expenses.length(); i++) {
            JSONObject expense = expenses.getJSONObject(i);
            System.out.printf("# %-3d %-10s  %-15s  %.2f%n", expense.getInt("id"), expense.getString("date"), expense.getString("description"), expense.getDouble("amount"));
        }
    }

    private static void handleSummary(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        JSONArray allExpenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
        double total = 0.0;
        for (int i = 0; i < allExpenses.length(); i++) {
            JSONObject expense = allExpenses.getJSONObject(i);
            total += expense.getDouble("amount");
        }
        System.out.printf("Total Expenses: %.2f%n", total);
    }

    private static void handleDelete(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        JSONArray currentExpenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
        try {
            Integer.parseInt(valueArgs.get(commandArgs.indexOf("--id")));
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: Invalid ID value. Please enter a valid number for '--id'\n");
            return;
        }
        int idToDelete = Integer.parseInt(valueArgs.get(commandArgs.indexOf("--id")));
        JSONArray updatedExpenses = new JSONArray();
        boolean found = false;
        for (int i = 0; i < currentExpenses.length(); i++) {
            JSONObject expense = currentExpenses.getJSONObject(i);
            if (expense.getInt("id") != idToDelete) {
                updatedExpenses.put(expense);
            } else {
                found = true;
            }
        }
        if (found) {
            // Rewrite CSV file
            StringBuilder csvContent = new StringBuilder("id,description,amount,date\n");
            for (int i = 0; i < updatedExpenses.length(); i++) {
                JSONObject expense = updatedExpenses.getJSONObject(i);
                csvContent.append(expense.getInt("id")).append(",")
                            .append(expense.getString("date")).append(",")
                            .append(expense.getString("description")).append(",")
                            .append(expense.getDouble("amount")).append("\n");
            }

            String filePath = "out/data" + File.separator + "expenses.csv";
            FileManager.writeExpenseToCsv(filePath, csvContent.toString());
            System.out.println("Expense with ID " + idToDelete + " deleted successfully.");
        }  
    }

    private static void handleCreateCategory(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        int latestId = FileManager.getLatestId("category");
        String name = valueArgs.get(commandArgs.indexOf("--name"));

        // Make sure category name is unique
        JSONArray existingCategories = FileManager.getCategories();
        for (int i = 0; i < existingCategories.length(); i++) {
            JSONObject category = existingCategories.getJSONObject(i);
            if (category.getString("name").equalsIgnoreCase(name)) {
                System.out.println("\n❌ Error: Category name '" + name + "' already exists. Please choose a different name.\n");
                return;
            }
        }

        String categoryData = (latestId + 1) + "," + formattedDate + "," + name;
        FileManager.updateCategoryToCsv(categoryData, true);
        FileManager.updateLatestRecordId(latestId + 1, "category");

        SetupManager.createCategoryFile(latestId + 1);

        System.out.println("Date: " + formattedDate + ", Description: " + name + ", ID: " + (latestId + 1));
    }

    private static void handlelistCategories(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        JSONArray categories = FileManager.getCategories();

        if (categories.length() < 1) {
            System.out.println("There are no categories");
            return;
        }

        System.out.println("# ID  Date          Name");
        for (int i = 0; i < categories.length(); i++) {
            JSONObject category = categories.getJSONObject(i);
            System.out.printf("# %-3d %-10s    %-15s%n", category.getInt("id"), category.getString("date"), category.getString("name"));
        }
    }

    private static void handleDeleteCategory(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        JSONArray currentCategories = FileManager.getCategories();
        int idToDelete;
        try {
            idToDelete = Integer.parseInt(valueArgs.get(commandArgs.indexOf("--id")));
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: Invalid ID value. Please enter a valid number for '--id'\n");
            return;
        }

        if (idToDelete == 1) {
            System.out.println("\n❌ Error: Cannot delete the default 'Uncategorized' category.\n");
            return;
        }

        JSONArray updatedCategories = new JSONArray();
        boolean found = false;
        for (int i = 0; i < currentCategories.length(); i++) {
            JSONObject category = currentCategories.getJSONObject(i);
            if (category.getInt("id") != idToDelete) {
                updatedCategories.put(category);
            } else {
                found = true;
            }
        }
        if (found) {
            // Rewrite CSV file
            StringBuilder csvContent = new StringBuilder("id,date,name\n");
            for (int i = 0; i < updatedCategories.length(); i++) {
                JSONObject category = updatedCategories.getJSONObject(i);
                csvContent.append(category.getInt("id")).append(",")
                            .append(category.getString("date")).append(",")
                            .append(category.getString("name")).append("\n");
            }

            FileManager.updateCategoryToCsv(csvContent.toString(), false);
            System.out.println("Category with ID " + idToDelete + " deleted successfully.");
        } else {
            System.out.println("\n❌ Error: Category with ID " + idToDelete + " not found.\n");
        }
    }

    public static void printCategories() {
        System.out.println("\n📂 Expense Categories:");
        Executor.handlelistCategories(null, null, null);
    }
}
