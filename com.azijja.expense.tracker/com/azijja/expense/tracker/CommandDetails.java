package com.azijja.expense.tracker;

import java.util.EnumMap;

public class CommandDetails {
    public static final EnumMap<Commands, String> descriptions = new EnumMap<Commands, String>(Commands.class);
    public static final EnumMap<Commands, String[]> parameters = new EnumMap<Commands, String[]>(Commands.class);
    
    static {
        descriptions.put(Commands.ADD, "Add a new expense");
        descriptions.put(Commands.LIST, "View all expenses");
        descriptions.put(Commands.SUMMARY, "View expense summary");
        descriptions.put(Commands.DELETE, "Remove an expense");
        descriptions.put(Commands.CREATE_CATEGORY, "Create a new expense category");
        descriptions.put(Commands.LIST_CATEGORIES, "List all expense categories");
        descriptions.put(Commands.DELETE_CATEGORY, "Delete an expense category");
        
        parameters.put(Commands.ADD, new String[]{"--description", "--amount"});
        parameters.put(Commands.LIST, new String[]{});
        parameters.put(Commands.SUMMARY, new String[]{});
        parameters.put(Commands.DELETE, new String[]{"--id"});
        parameters.put(Commands.CREATE_CATEGORY, new String[]{"--name"});
        parameters.put(Commands.LIST_CATEGORIES, new String[]{});
        parameters.put(Commands.DELETE_CATEGORY, new String[]{"--id"});
    }
}
