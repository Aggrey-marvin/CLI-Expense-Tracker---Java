package com.azijja.expense.tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArgsProcessor {
    public static void procesArgs(String[] args) {
        String firstArg = args[0].toLowerCase();
        Commands command = null;
        for (Commands cmd : Commands.values()) {
            if (cmd.name().toLowerCase().equals(firstArg)) {
                command = cmd;
                break;
            }
        }   

        if (command == null) {
            System.out.println("\n❌ Error: Unknown command '" + firstArg + "'");
            System.out.println("\n📋 Available commands: add, list, summary, delete");
            System.out.println("💡 Tip: Run without arguments to see the welcome screen\n");
            return;
        }

        // Confirm passed args number
        String [] expectedParams = command.getParameters();
        if (args.length - 1 < expectedParams.length) {
            System.out.println("\n❌ Error: Missing parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> ");
            }
            System.out.println("\n");
            return;
        } else if (args.length - 1 > expectedParams.length * 2) {
            System.out.println("\n❌ Error: Too many parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> ");
            }
            System.out.println("\n");
            return;
        }

        // Check for any missing args
        String [] allArgs = new String[args.length - 1];
        System.arraycopy(args, 1, allArgs, 0, args.length - 1);

        for (String arg : expectedParams) {
            if (!Arrays.asList(allArgs).contains(arg)) {
                System.out.println("\n❌ Error: Missing parameter '<" + arg + ">' for command '" + command.name().toLowerCase() + "'\n");
            }
        }

        ArrayList <String> commandArgs = new ArrayList<>();
        ArrayList <String> commandValues = new ArrayList<>();

        for (String arg : allArgs) {
            if (Arrays.asList(expectedParams).contains(arg)) {
                commandArgs.add(arg);
            } else {
                commandValues.add(arg);
            }
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(now);

        switch (command) {
            case ADD:
                System.out.println("Adding a new expense...");
                int latestId = FileManager.readJsonFile("out/data" + File.separator + "expenses.json");
                String description = commandValues.get(commandArgs.indexOf("--description"));
                double amount = Double.parseDouble(commandValues.get(commandArgs.indexOf("--amount")));

                String expenseData = (latestId + 1) + "," + formattedDate + "," + description + "," + amount;
                FileManager.writeExpenseToCsv("out/data" + File.separator + "expenses.csv", expenseData);
                FileManager.updateLatestTransactionId(latestId + 1);

                System.out.println("Date: " + formattedDate + ", Description: " + description + ", Amount: " + amount + ", ID: " + (latestId + 1));
                break;
            case LIST:
                System.out.println("Listing all expenses...");
                JSONArray expenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
                System.out.println("# ID  Date        Description      Amount");
                for (int i = 0; i < expenses.length(); i++) {
                    JSONObject expense = expenses.getJSONObject(i);
                    System.out.printf("# %-3d %-10s  %-15s  %.2f%n", expense.getInt("id"), expense.getString("date"), expense.getString("description"), expense.getDouble("amount"));
                }
                break;
            case SUMMARY:
                System.out.println("Showing expense summary...");
                JSONArray allExpenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
                double total = 0.0;
                for (int i = 0; i < allExpenses.length(); i++) {
                    JSONObject expense = allExpenses.getJSONObject(i);
                    total += expense.getDouble("amount");
                }
                System.out.printf("Total Expenses: %.2f%n", total);
                break;
            case DELETE:
                System.out.println("Deleting an expense...");
                JSONArray currentExpenses = FileManager.readExpensesJson("out/data" + File.separator + "expenses.csv");
                int idToDelete = Integer.parseInt(commandValues.get(commandArgs.indexOf("--id")));
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
                    try {
                        java.nio.file.Files.write(java.nio.file.Paths.get("out/data" + File.separator + "expenses.csv"), csvContent.toString().getBytes());
                        System.out.println("Expense with ID " + idToDelete + " deleted successfully.");
                    } catch (java.io.IOException e) {
                        System.out.println("Error updating CSV file: " + e.getMessage());
                    }
                }    
                break;
            default:
                System.out.println("❌ Error: Command not implemented yet.");
        }
    }
}
