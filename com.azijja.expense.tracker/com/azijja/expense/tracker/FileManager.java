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
}
