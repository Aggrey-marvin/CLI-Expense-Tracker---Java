package com.azijja.expense.tracker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileManager {
    private static final String DATA_DIR = "out/data";
    private static final String APP_JSON = DATA_DIR + "/app.json";
    private static final String EXPENSES_CSV = DATA_DIR + "/expenses.csv";
    private static final String CATEGORIES_CSV = DATA_DIR + "/categories.csv";

    public static int getLatestId(String entity) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(APP_JSON)));
            JSONArray appData = new JSONArray(content);
            
            if (entity.equals("expense")) {
                return appData.getJSONObject(0).getInt("latestExpenseId");
            } else if (entity.equals("category")) {
                return appData.getJSONObject(0).getInt("latestCategoryId");
            } else {
                throw new IllegalArgumentException("Invalid entity type: " + entity);
            }
            
        } catch (IOException e) {
            // File doesn't exist yet
            return 0;
        } catch (Exception e) {
            // Empty file or invalid JSON
            return 0;
        }
    }

    public static void appendExpenseToCsv(String filePath, String expenseData) {
        try {
            Files.write(Paths.get(filePath), (expenseData + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public static void updateLatestRecordId(int latestId, String entity) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(APP_JSON)));
            JSONArray contentJson = new JSONArray(content);
            JSONObject appData = contentJson.getJSONObject(0);

            if (entity.equals("expense")) {
                appData.put("latestExpenseId", latestId);
            } else if (entity.equals("category")) {
                appData.put("latestCategoryId", latestId);
            }

            Files.write(Paths.get(APP_JSON), contentJson.toString(4).getBytes());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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

    public static void writeExpenseToCsv (String filePath, String expenseData) {

        try {
                java.nio.file.Files.write(java.nio.file.Paths.get(filePath), expenseData.getBytes());
            } catch (java.io.IOException e) {
                System.out.println("Error updating CSV file: " + e.getMessage());
            }
    }

    public static void appendCategoryToCsv(String categoryData) {
        try {
            Files.write(Paths.get(CATEGORIES_CSV), (categoryData + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}

