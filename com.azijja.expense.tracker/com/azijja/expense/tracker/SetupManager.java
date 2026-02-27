package com.azijja.expense.tracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SetupManager {
    private static final String DATA_DIR = "out/data";
    private static final String APP_JSON = DATA_DIR + "/app.json";
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
            File jsonFile = new File(APP_JSON);
            if (!jsonFile.exists()) {
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write("[{\"latestExpenseId\":0, \"latestCategoryId\":1}]");
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
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(now);

                try (FileWriter writer = new FileWriter(categoriesFile)) {
                    writer.write("id,date,name\n1," + formattedDate + ",Uncategorized\n");
                }
                System.out.println("✓ Created categories.csv with headers");
            }
            
            System.out.println("\n✅ Setup completed successfully!\n");
            
        } catch (IOException e) {
            System.err.println("❌ Error during setup: " + e.getMessage());
        }
    }
    
    public static boolean isSetupComplete() {
        return Files.exists(Paths.get(APP_JSON)) && 
               Files.exists(Paths.get(EXPENSES_CSV)) && Files.exists(Paths.get(CATEGORIES_CSV));
    }
    
    // Main method to run setup independently
    public static void main(String[] args) {
        System.out.println("\n🔧 Running Expense Tracker Setup...\n");
        initialize();
    }

    public static void createCategoryFile(int id) {
        File categoriesFile = new File(DATA_DIR + "/expense_category_" + id + ".csv");
        try (FileWriter writer = new FileWriter(categoriesFile)) {
            writer.write("id,description,amount,date\n");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
