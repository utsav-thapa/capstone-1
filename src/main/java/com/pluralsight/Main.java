package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static final String TRANSACTIONS_FILE_NAME = "src/main/resources/transactions.csv";
    static Scanner scanner = new Scanner(System.in);
    static String BOLD = "\u001B[1m";
    static String RED = "\u001B[91m";
    static String BLUE = "\u001B[94m";
    static String GREEN = "\u001B[92m";
    static String PURPLE = "\u001B[95m";
    static String RESET = "\u001B[0m";
    static FileWriter fileWriter = null;
    static BufferedWriter bufferedWriter = null;

    static ArrayList <Transactions> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);

    public static void main(String[] args) {
        mainMenu();
        System.out.println(RED + BOLD + "Thank you for using Bank of Thapa."+ RESET);
        System.out.println(BLUE +"Have a good day! :)" + RESET);
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
            System.out.print(BOLD + RED + mainHeader + RESET + BLUE + mainMenu + RESET);
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


    private static Transactions parseTransaction(String line) {
        String[] parts =line.split("\\|");
        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transactions(date,time,description,vendor,amount);
    };
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

            //sorting it by recent transactions first by time
            transactions.sort(Comparator.comparing(Transactions::getTime).reversed());

            //sorting it by date
            transactions.sort(Comparator.comparing(Transactions::getDate).reversed());




        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("The system can't find the file named " + transactionsFileName);;
        } catch (IOException ioException) {
            System.err.println("The system wasn't able to read the file named " + transactionsFileName);;
        }
        return transactions;
    }

    private static void addDeposit() {
        System.out.print(GREEN+ "Enter the deposit amount: $" + RESET);
        double depositAmount = Double.parseDouble(scanner.nextLine());

        System.out.print(GREEN + "Enter the vendor name: " + RESET);
        String vendor = scanner.nextLine();

        System.out.print(GREEN + "Enter the description: " + RESET);
        String description = scanner.nextLine();

        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true); //true appends the file instead of replacing it
            bufferedWriter = new BufferedWriter(fileWriter);

            //had to format it because the output of the just localtime gave out the seconds in a float
            DateTimeFormatter est = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timeNow = est.format(LocalTime.now());

            String finalLine = String.format("%s|%s|%s|%s|%.2f\n",
                    LocalDate.now(),timeNow,description,vendor,depositAmount);
            bufferedWriter.write(finalLine);
//            bufferedWriter.flush();
            bufferedWriter.close();
            transactions.add(new Transactions(LocalDate.now(),LocalTime.now(),description,vendor,depositAmount));

        } catch (IOException e) {
            System.err.println("Something went wrong.");;
        }
    }

    private static void makePayment() {
        System.out.print(RED + "Enter the payment amount: $" + RESET);
        double paymentAmount = Double.parseDouble(scanner.nextLine());

        System.out.print(RED + "Enter the vendor name: " + RESET);
        String vendor = scanner.nextLine();

        System.out.print(RED + "Enter the description: " + RESET);
        String description = scanner.nextLine();

        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true); //true appends the file instead of replacing it
            bufferedWriter = new BufferedWriter(fileWriter);

            DateTimeFormatter est = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timeNow = est.format(LocalTime.now());

            String finalLine = String.format("%s|%s|%s|%s|-%.2f\n",
                    LocalDate.now(),timeNow,description,vendor,paymentAmount);
            bufferedWriter.write(finalLine);
//            bufferedWriter.flush();
            bufferedWriter.close();
            transactions.add(new Transactions(LocalDate.now(),LocalTime.now(),description,vendor,paymentAmount));


        } catch (IOException e) {
            System.err.println("Something went wrong.");;
        }
    }
    private static void ledger() {
        boolean running = true;
        String ledgerHead = """
                    ================================================================
                    --------X--------X-------Ledger--------X--------X--------X-----
                    ================================================================
                    
                    *) All (Display all entries)                  (Enter 'A')
                    *) Deposits (Deposits made into the account.) (Enter 'D')
                    *) Payments (Payments made from the account.) (Enter 'P')
                    *) Reports (Run a custom search.)             (Enter 'R')
                    *) Home (Go back to the home page.)           (Enter 'H')
                    ----------------------------------------------------------------
                    Enter: """;
        do {

            System.out.print(BLUE + ledgerHead + RESET);
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "A", "a":
                    displayAllEntries();
                    break;
                case "d", "D":
                    deposits();
                    break;
                case "p", "P":
                    payments();
                    break;
                case "R", "r":
                    reports();
                    break;
                case "H", "h":
                    running = false;
                    break;
                default:
                    System.err.println("Are you trying to break the system?");
                    break;
            }
        } while (running);
    }

    private static void displayAllEntries() {
        //sorting it by recent transactions first by time
        transactions.sort(Comparator.comparing(Transactions::getTime).reversed());

        //sorting it by date
        transactions.sort(Comparator.comparing(Transactions::getDate).reversed());

        System.out.printf(PURPLE+"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount"+RESET);
        for (Transactions i: transactions) {
            i.displayTransactions();
        }
        
    }

    private static void deposits() {
        //sorting it by recent transactions first by time
        transactions.sort(Comparator.comparing(Transactions::getTime).reversed());

        //sorting it by date
        transactions.sort(Comparator.comparing(Transactions::getDate).reversed());

        System.out.printf(GREEN +"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
        for (Transactions i: transactions) {
            if (i.getAmount()>0) {
                i.displayTransactions();
            }
        }
        System.out.println(RESET);

        
    }

    private static void payments() {
        //sorting it by recent transactions first by time
        transactions.sort(Comparator.comparing(Transactions::getTime).reversed());

        //sorting it by date
        transactions.sort(Comparator.comparing(Transactions::getDate).reversed());

        System.out.printf(RED +"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
        for (Transactions i: transactions) {
            if (i.getAmount() < 0) {
                i.displayTransactions();
            }
        }
        System.out.println(RESET);
        
    }

    private static void reports() {
        //sorting it by recent transactions first by time
        transactions.sort(Comparator.comparing(Transactions::getTime).reversed());

        //sorting it by date
        transactions.sort(Comparator.comparing(Transactions::getDate).reversed());

        boolean running = true;
        String reportsMenu = """
                =======================
                     Reports
                =======================
                1. Month to Date
                2. Previous Month
                3. Year to Date
                4. Previous Year
                5. Search by Vendor
                6. Custom Search
                0. Back to the Ledger page
                
                =======================
                Enter: """;

        do {
            System.out.println(BLUE + reportsMenu);
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");

                    for (Transactions t : transactions) {
                        if ((t.getDate().getYear() == LocalDate.now().getYear()) &&
                        (t.getDate().getMonth() == LocalDate.now().getMonth())) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "2":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    for (Transactions t: transactions) {
                        if ((t.getDate().getYear() == 2026) &&
                                (t.getDate().getMonthValue() == (LocalDate.now().getMonthValue() - 1))) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "3":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    for (Transactions t : transactions) {
                        if (t.getDate().getYear() == LocalDate.now().getYear()) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "4":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    for (Transactions t: transactions) {
                        if (t.getDate().getYear() == (LocalDate.now().getYear()) - 1) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "5":
                    System.out.println("What is the name of the vendor?");
                    String inputVendor = scanner.nextLine();
                    for (Transactions t: transactions) {
                        if (t.getVendor().equalsIgnoreCase(inputVendor)) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.err.println("Are you trying to break the app?");
                    break;
            }
        } while (running);

        
    }

    private static void customSearch() {
    }
}
