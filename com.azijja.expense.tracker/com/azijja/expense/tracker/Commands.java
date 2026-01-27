package com.azijja.expense.tracker;

public enum Commands {
    ADD ("add"),
    LIST ("list"),
    SUMMARY ("summary"),
    DELETE ("delete");

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
