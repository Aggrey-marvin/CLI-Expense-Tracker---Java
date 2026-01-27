package com.azijja.expense.tracker;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Tools.printWelcome();
            return;
        } else {
            ArgsProcessor.procesArgs(args);
        }
    }
}
