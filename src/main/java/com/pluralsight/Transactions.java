package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transactions {
    private LocalDate date;
    private LocalDateTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transactions(LocalDate date, LocalDateTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void allTranscations(){
        System.out.printf("%-12t %-10t %-30s %-15s %-8f",date,time,description,vendor,amount);
    }
}
