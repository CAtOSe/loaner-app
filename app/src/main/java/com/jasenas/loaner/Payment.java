package com.jasenas.loaner;

public class Payment {
    String name;
    int amount;
    boolean isPaid;

    public Payment() {
        this.name = "";
        this.amount = 0;
        this.isPaid = false;
    }

    public Payment(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.isPaid = false;
    }

    public Payment(String name, int amount, boolean isPaid) {
        this.name = name;
        this.amount = amount;
        this.isPaid = isPaid;
    }

    public boolean toggleStatus() {
        this.isPaid = !isPaid;
        return isPaid;
    }
}
