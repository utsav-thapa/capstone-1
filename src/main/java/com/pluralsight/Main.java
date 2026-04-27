package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";
    static Scanner scanner = new Scanner(System.in);

    static ArrayList <Transactions> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);


    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you for using Bank of Thapa.");
        System.out.println("Have a good day! :)");
    }

    private static void mainMenu() {
        String homeScreen = """
                
                        Welcome to Bank of Thapa!
                ---------------------------------------------
                
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

    private static ArrayList<Transactions> loadTransactions(String transactionsFileName) {

        ArrayList <Transactions> transactions = new ArrayList <Transactions>();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader(transactionsFileName);
            bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            //to skip the first line of the file which is the header
            line = bufferedReader.readLine();

            while(line != null) {
                Transactions transaction = parseTransaction(line);
                transactions.add(transaction);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();


        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("The system can't find the file named " + transactionsFileName);;
        } catch (IOException ioException) {
            System.err.println("The system wasn't able to read the file named " + transactionsFileName);;
        }
        return transactions;
    }

    private static Transactions parseTransaction(String line) {
        String[] parts =line.split("\\|");
        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transactions(date,time,description,vendor,amount);
    };




    private static void ledger() {
    }

    private static void makePayment() {

    }

    private static void addDeposit() {
    }
}
