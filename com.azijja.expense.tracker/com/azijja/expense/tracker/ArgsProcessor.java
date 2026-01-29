package com.azijja.expense.tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
        } else if (args.length - 1 > expectedParams.length * 2) {
            System.out.println("\n❌ Error: Too many parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> ");
            }
            System.out.println("\n");
            return;
        }

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

        switch (command) {
            case ADD:
                System.out.println("Adding a new expense...");
                int latestId = FileReader.readJsonFile("data" + File.separator + "expenses.json");
                System.out.println("Latest ID: " + latestId);
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
