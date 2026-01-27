package com.azijja.expense.tracker;

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

        String [] expectedParams = command.getParameters();
        if (args.length - 1 < expectedParams.length) {
            System.out.println("\n❌ Error: Missing parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> ");
            }
            System.out.println("\n");
            return;
        }

        switch (command) {
            case ADD:
                System.out.println("Adding a new expense...");
                // Further implementation here
                break;
            case LIST:
                System.out.println("Listing all expenses...");
                // Further implementation here
                break;
            case SUMMARY:
                System.out.println("Showing expense summary...");
                // Further implementation here
                break;
            case DELETE:
                System.out.println("Deleting an expense...");
                // Further implementation here
                break;
            default:
                System.out.println("❌ Error: Command not implemented yet.");
        }
    }
}
