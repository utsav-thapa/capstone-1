package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";
    static Scanner scanner = new Scanner(System.in);
    static String BOLD = "\u001B[1m";
    static String BLUE = "\u001B[94m";
    static String PURPLE = "\u001B[95m";
    static String RESET = "\u001B[0m";

    static ArrayList <Transactions> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);


    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you for using Bank of Thapa.");
        System.out.println("Have a good day! :)");
    }

    private static void mainMenu() {

        String mainHeader = """
                ================================================
                        Welcome to Bank of Thapa!
                ================================================
                """;
        String mainMenu ="""
                
                    What would you like to do today?
                
                ------------------------------------------------
                *) Add Deposit          (Press D)
                *) Make Payment (Debit) (Press P)
                *) Ledger               (Press L)
                *) Exit                 (Press X to Exit)
                ------------------------------------------------
                Enter:   """;
        boolean running = true;

        do {
            System.out.print(BOLD + BLUE + mainHeader + RESET + PURPLE + mainMenu + RESET);
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
                    System.err.println("Are you trying to hack the bank?");
                    break;
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

    private static void addDeposit() {
        System.out.print(PURPLE+ "Enter the deposit amount: $" + RESET);
        double depositAmount = Double.parseDouble(scanner.nextLine());

        System.out.print(PURPLE + "Enter the vendor name: " + RESET);
        String vendor = scanner.nextLine();

        System.out.print(PURPLE + "Enter the description: " + RESET);
        String description = scanner.nextLine();

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME);
            bufferedWriter = new BufferedWriter(fileWriter);

            String line = "%t|%t|%s|%s|%.2f";
            String finalLine = String.format(line,LocalDate.now(),LocalTime.now(),description,vendor,depositAmount);
            bufferedWriter.write(finalLine);
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ledger() {
        String ledgerMenu = """
                
                """;
    }

    private static void makePayment() {

    }
}
