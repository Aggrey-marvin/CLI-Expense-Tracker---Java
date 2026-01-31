package com.azijja.expense.tracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileManager {
    public static int readJsonFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray expenses = new JSONArray(content);
            
            int maxId = 0;
            for (int i = 0; i < expenses.length(); i++) {
                JSONObject expense = expenses.getJSONObject(i);
                int id = expense.getInt("id");
                if (id > maxId) {
                    maxId = id;
                }
            }
            return maxId;
            
        } catch (IOException e) {
            // File doesn't exist yet
            return 0;
        } catch (Exception e) {
            // Empty file or invalid JSON
            return 0;
        }
    }

    public static void writeExpenseToCsv(String filePath, String expenseData) {
        try {
            Files.write(Paths.get(filePath), (expenseData + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static void updateLatestTransactionId(int latestTransactionId) {
        try {
            String filePath = "out/data/expenses.json";
            JSONArray expenses;
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                expenses = new JSONArray(content);
            } catch (IOException e) {
                expenses = new JSONArray();
            }

            if (expenses.length() > 0) {
                JSONObject latestEntry = expenses.getJSONObject(expenses.length() - 1);
                latestEntry.put("id", latestTransactionId);
            }

            Files.write(Paths.get(filePath), expenses.toString(4).getBytes());
        } catch (IOException e) {
            System.out.println("Error updating JSON file: " + e.getMessage());
        }
    }

    public static JSONArray readExpensesJson(String filePath) {
        try {
            JSONArray expenses = new JSONArray();
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] lines = content.split(System.lineSeparator());
            
            boolean isFirstLine = true;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    JSONObject expense = new JSONObject();
                    expense.put("id", Integer.parseInt(fields[0].trim()));
                    expense.put("date", fields[1].trim());
                    expense.put("description", fields[2].trim());
                    expense.put("amount", Double.parseDouble(fields[3].trim()));
                    expenses.put(expense);
                }
            }
            return expenses;
        } catch (IOException e) {
            return new JSONArray();
        }
        }
    }

