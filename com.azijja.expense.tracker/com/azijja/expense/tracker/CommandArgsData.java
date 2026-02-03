package com.azijja.expense.tracker;

import java.util.ArrayList;

public record CommandArgsData(Commands command, ArrayList<String> commandArgs, ArrayList<String> valueArgs, String formattedDateString) {
    
}
