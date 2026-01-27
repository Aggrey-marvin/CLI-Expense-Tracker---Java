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
        
        parameters.put(Commands.ADD, new String[]{"description", "amount"});
        parameters.put(Commands.LIST, new String[]{});
        parameters.put(Commands.SUMMARY, new String[]{});
        parameters.put(Commands.DELETE, new String[]{"id"});
    }
}
