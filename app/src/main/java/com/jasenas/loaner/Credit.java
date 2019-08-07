package com.jasenas.loaner;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Credit {
    int id;
    String name;
    Calendar date;
    int amount;
    boolean isMonthly;
    List<Payment> payments;

    public Credit(int id) {
        this.id = id;
        this.name = "";
        this.date = Calendar.getInstance();
        this.amount = 0;
        this.isMonthly = false;
        this.payments = new ArrayList<>();
    }

    public Credit(int id, String name, Calendar date, int amount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.isMonthly = false;
        this.payments = new ArrayList<>();
    }

    public Credit(int id, String name, Calendar date, int amount, boolean isMonthly) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.isMonthly = isMonthly;
        this.payments = new ArrayList<>();
    }

    public Credit(int id, String name, Calendar date, int amount, List<Payment> payments) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.isMonthly = false;
        this.payments = payments;
    }

    public Credit(int id, String name, Calendar date, int amount, List<Payment> payments, boolean isMonthly) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.isMonthly = isMonthly;
        this.payments = payments;
    }

    public Credit(CreditDB creditDB) throws ParseException, JSONException {
        this.id = creditDB.id;
        this.name = creditDB.name;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.date  = Calendar.getInstance();
        this.date.setTime(df.parse(creditDB.date));

        this.amount = creditDB.amount;
        this.isMonthly = creditDB.isMonthly;

        this.payments = CreditManager.parsePayments(creditDB.payments);

    }

    public int countPaid() {
        int paidCounter = 0;
        for(int x = 0; x < this.payments.size(); x++) {
            if (this.payments.get(x).isPaid) paidCounter++;
        }

        return paidCounter;
    }

    public int countUnpaid() {
        return this.payments.size() - this.countPaid();
    }

    public boolean isPaid() {
        return this.countPaid() == this.payments.size();
    }

    public void auto() {
        int cut = (int) Math.ceil((double) this.amount / this.payments.size());

        for (int x = 0; x < this.payments.size(); x++) {
            this.payments.get(x).amount = cut;
        }
    }

}
