package com.azijja.expense.tracker;

import java.io.File;
import java.util.ArrayList;

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
            default:
                System.out.println("\n❌ Error: Command handler not implemented for '" + cmdData.command().name().toLowerCase() + "'\n");
        }
    }

    private static void handleAdd(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
        int latestId = FileManager.readJsonFile("out/data" + File.separator + "expenses.json");
        String description = valueArgs.get(commandArgs.indexOf("--description"));
        double amount = Double.parseDouble(valueArgs.get(commandArgs.indexOf("--amount")));

        String expenseData = (latestId + 1) + "," + formattedDate + "," + description + "," + amount;
        FileManager.appendExpenseToCsv("out/data" + File.separator + "expenses.csv", expenseData);
        FileManager.updateLatestTransactionId(latestId + 1);

        System.out.println("Date: " + formattedDate + ", Description: " + description + ", Amount: " + amount + ", ID: " + (latestId + 1));
    }

    private static void handleList(ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDate) {
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
        System.out.println("Deleting an expense...");
        JSONArray currentExpenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
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
}
