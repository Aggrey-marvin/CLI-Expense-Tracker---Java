package com.azijja.expense.tracker;

import java.util.Locale.Category;

public class Tools {
    public static void printWelcome() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           💰 Expense Tracker CLI 💰                        ║");
        System.out.println("║     Your Personal Finance Management Companion             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Track your expenses effortlessly from the command line!\n");
        
        System.out.println("📋 Available Commands:");
        for (Commands command : Commands.values()) {
            String description = CommandDetails.descriptions.get(command);
            System.out.printf("*  %-9s - %s%n", command.name().toLowerCase(), description);
        }
        
        System.out.println("\n💡 Tip: Type a command to get started!\n");
    }

    
 }   
