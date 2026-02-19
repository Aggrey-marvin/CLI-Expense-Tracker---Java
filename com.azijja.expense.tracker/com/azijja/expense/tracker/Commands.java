package com.azijja.expense.tracker;

public enum Commands {
    ADD ("add"),
    LIST ("list"),
    SUMMARY ("summary"),
    DELETE ("delete"),
    CREATE_CATEGORY ("create_category"),
    LIST_CATEGORIES ("list_categories"),
    DELETE_CATEGORY ("delete_category");

    private final String name;

    private Commands(String command) {
        name = command;
    }  
    
    public String toString() {
        return name;
    }

    public String [] getParameters() {
        return CommandDetails.parameters.get(this);
    }
}
