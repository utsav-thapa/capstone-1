package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    static ArrayList <Transaction> transactions = loadTransactions(TRANSACTIONS_FILE_NAME);

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
                Enter:""";
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
    private static Transaction parseTransaction(String line) {
        String[] parts =line.split("\\|");
        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transaction(date,time,description,vendor,amount);
    }
    private static ArrayList<Transaction> loadTransactions(String transactionsFileName) {

        ArrayList <Transaction> transactions = new ArrayList <Transaction>();
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader(transactionsFileName);
            bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();

            //to skip the first line of the file which is the header
            line = bufferedReader.readLine();

            while(line != null) {
                Transaction transaction = parseTransaction(line);
                transactions.add(transaction);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            //sorting it by recent transactions first by time
            justSortIt(transactions);

        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("The system can't find the file named " + transactionsFileName);
        } catch (IOException ioException) {
            System.err.println("The system wasn't able to read the file named " + transactionsFileName);
        }
        return transactions;
    }

    private static void addDeposit() {
        System.out.println(GREEN);
        addEntry();
        System.out.println(RESET);
    }

    private static void makePayment() {
        System.out.print(RED);
        addEntry();
        System.out.println(RESET);
    }
    private static void addEntry() {
        System.out.print("Enter the amount: $");
        double depositAmount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter the description: ");
        String description = scanner.nextLine();

        System.out.print("Enter the date of the transaction (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the time of the transaction (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());

        System.out.println(RESET);

        FileWriter fileWriter;
        BufferedWriter bufferedWriter;

        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE_NAME, true); //true appends the file instead of replacing it
            bufferedWriter = new BufferedWriter(fileWriter);

//            //had to format it because the output of the just localtime gave out the seconds in a float
//            DateTimeFormatter est = DateTimeFormatter.ofPattern("HH:mm:ss");
//            String timeNow = est.format(LocalTime.now());

            String finalLine = String.format("%s|%s|%s|%s|%.2f\n",
                    date,time,description,vendor,depositAmount);
            bufferedWriter.write(finalLine);
            bufferedWriter.close();
            transactions.add(new Transaction(date,time,description,vendor,depositAmount));

        } catch (IOException e) {
            System.err.println("Something went wrong.");
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
                    Enter:""";
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
        justSortIt(transactions);

        System.out.printf(PURPLE+"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount"+RESET);
        for (Transaction i: transactions) {
            i.displayTransactions();
        }
        
    }

    private static void justSortIt(ArrayList<Transaction> transactions) {
        //sorting it by recent transactions first by time
        transactions.sort(Comparator.comparing(Transaction::getTime).reversed());

        //sorting it by date
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
    }

    private static void deposits() {
        justSortIt(transactions);

        System.out.printf(GREEN +"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
        for (Transaction i: transactions) {
            if (i.getAmount()>0) {
                i.displayTransactions();
            }
        }
        System.out.println(RESET);
    }

    private static void payments() {
        justSortIt(transactions);

        System.out.printf(RED +"%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
        for (Transaction i: transactions) {
            if (i.getAmount() < 0) {
                i.displayTransactions();
            }
        }
        System.out.println(RESET);
    }

    private static void reports() {
        justSortIt(transactions);

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
                Enter:""";

        do {
            System.out.print(BLUE + reportsMenu);
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");

                    for (Transaction t : transactions) {
                        if ((t.getDate().getYear() == LocalDate.now().getYear()) &&
                        (t.getDate().getMonth() == LocalDate.now().getMonth())) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "2":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    if (LocalDate.now().getMonthValue() == 1){
                        for (Transaction t : transactions) {
                            if (((t.getDate().getYear() == LocalDate.now().getYear()-1)) &&
                            ((t.getDate().getMonthValue() == 12))) {
                                t.displayTransactions();
                            }
                        }

                    } else {
                        for (Transaction t : transactions) {
                            if ((t.getDate().getYear() == LocalDate.now().getYear()) &&
                                    (t.getDate().getMonthValue() == (LocalDate.now().getMonthValue() - 1))) {
                                t.displayTransactions();
                            }
                        }
                    }
                    break;
                case "3":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    for (Transaction t : transactions) {
                        if (t.getDate().getYear() == LocalDate.now().getYear()) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "4":
                    System.out.printf("%-12s %-10s %-30s %-15s %s\n","Date","Time","Transaction","Vendor","Amount");
                    for (Transaction t: transactions) {
                        if (t.getDate().getYear() == (LocalDate.now().getYear()) - 1) {
                            t.displayTransactions();
                        }
                    }
                    break;
                case "5":
                    System.out.println("What is the name of the vendor?");
                    String inputVendor = scanner.nextLine();
                    for (Transaction t: transactions) {
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


        //
//        ArrayList<Transaction> results = filterByDate(transactions);
//        results = filterByDescription(results);
//
//        results = filterByAmount(results);
//
//
    }
}
