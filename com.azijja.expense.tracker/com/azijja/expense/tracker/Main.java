package com.azijja.expense.tracker;

public class Main {
    public static void main(String[] args) {
        // Auto-initialize data folder and files if not present
        if (!SetupManager.isSetupComplete()) {
            SetupManager.initialize();
        }
        
        if (args.length == 0) {
            Tools.printWelcome();
            return;
        } else {
            ArgsProcessor.procesArgs(args);
        }
    }
}
