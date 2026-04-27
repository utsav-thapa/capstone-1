package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";
    static Scanner scanner = new Scanner(System.in);

    static ArrayList <String> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thanks for using Bank of Thapa.");


    }

    private static void mainMenu() {
        String homeScreen = """
                
                Welcome to Bank of Thapa!
                ---------------------------
                
                What would you like to do today?
                
                *) Add Deposit          (Press D)
                *) Make Payment (Debit) (Press P)
                *) Ledger               (Press L)
                *) Exit                 (Press X to Exit)
                """;
        boolean running = true;

        do {
            System.out.println(homeScreen);
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "D", "d":
                    addDeposit();
                    break;
                case "p", "P":
                    makePayment();
                    break;
                case "l", "L":
                    ledger();
                    break;
                case "X", "x":
                    running = false;
                    break;
                default:
                    System.out.println("Are you trying to hack the bank?");
            }
        } while (running);
    }




    private static void ledger() {
    }

    private static void makePayment() {

    }

    private static void addDeposit() {
    }
}
