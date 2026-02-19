package com.azijja.expense.tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ArgsProcessor {
    public static CommandArgsData processArgs(String[] args) {
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
            return null;
        }

        // Confirm passed args number
        String [] expectedParams = command.getParameters();
        if (args.length - 1 < expectedParams.length * 2) {
            System.out.println("\n❌ Error: Missing parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> <value>");
            }
            System.out.println("\n");
            return null;
        } else if (args.length - 1 > expectedParams.length * 2) {
            System.out.println("\n❌ Error: Too many parameters for command '" + command.name().toLowerCase() + "'");
            System.out.print("Expected parameters: ");
            for (String param : expectedParams) {
                System.out.print("<" + param + "> ");
            }
            System.out.println("\n");
            return null;
        }

        // Check for any missing args
        String [] allArgs = new String[args.length - 1];
        System.arraycopy(args, 1, allArgs, 0, args.length - 1);

        for (String arg : expectedParams) {
            if (!Arrays.asList(allArgs).contains(arg)) {
                System.out.println("\n❌ Error: Missing parameter '<" + arg + ">' for command '" + command.name().toLowerCase() + "'\n");
                return null;
            }
        }

        ArrayList <String> commandArgs = new ArrayList<>();
        ArrayList <String> commandValues = new ArrayList<>();

        for (String aarg : allArgs) {
            if (Arrays.asList(expectedParams).contains(aarg)) {
                commandArgs.add(aarg);
            } else {
                commandValues.add(aarg);
            }
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(now);

        CommandArgsData cmdData = new CommandArgsData(command, commandArgs, commandValues, formattedDate);
        return cmdData;

    }
}
