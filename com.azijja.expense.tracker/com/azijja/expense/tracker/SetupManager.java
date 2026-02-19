package com.azijja.expense.tracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SetupManager {
    private static final String DATA_DIR = "out/data";
    private static final String EXPENSES_JSON = DATA_DIR + "/app.json";
    private static final String EXPENSES_CSV = DATA_DIR + "/expenses.csv";
    private static final String CATEGORIES_CSV = DATA_DIR + "/categories.csv";
    
    public static void initialize() {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("✓ Created data directory");
            }
            
            // Create app.json if it doesn't exist
            File jsonFile = new File(EXPENSES_JSON);
            if (!jsonFile.exists()) {
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write("[{\"latestExpenseId\":0, \"latestCategoryId\":0}]");
                }
                System.out.println("✓ Created app.json");
            }
            
            // Create expenses.csv if it doesn't exist
            File csvFile = new File(EXPENSES_CSV);
            if (!csvFile.exists()) {
                try (FileWriter writer = new FileWriter(csvFile)) {
                    writer.write("id,description,amount,date\n");
                }
                System.out.println("✓ Created expenses.csv with headers");
            }
            
            // Create categories.csv if it doesn't exist
            File categoriesFile = new File(CATEGORIES_CSV);
            if (!categoriesFile.exists()) {
                try (FileWriter writer = new FileWriter(categoriesFile)) {
                    writer.write("id,name\n");
                }
                System.out.println("✓ Created categories.csv with headers");
            }
            
            System.out.println("\n✅ Setup completed successfully!\n");
            
        } catch (IOException e) {
            System.err.println("❌ Error during setup: " + e.getMessage());
        }
    }
    
    public static boolean isSetupComplete() {
        return Files.exists(Paths.get(EXPENSES_JSON)) && 
               Files.exists(Paths.get(EXPENSES_CSV));
    }
    
    // Main method to run setup independently
    public static void main(String[] args) {
        System.out.println("\n🔧 Running Expense Tracker Setup...\n");
        initialize();
    }
}
