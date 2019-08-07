package com.jasenas.loaner;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;

import java.util.Calendar;


@Entity(tableName = "credits")
public class CreditDB {
    @PrimaryKey
    @NonNull
    int id;
    String name;
    String date;
    int amount;
    boolean isMonthly;
    String payments;

    public CreditDB(int id, String name, String date, int amount, boolean isMonthly, String payments) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.isMonthly = isMonthly;
        this.payments = payments;
    }

    public CreditDB(Credit credit) throws JSONException {
        this.id = credit.id;
        this.name = credit.name;
        this.amount = credit.amount;
        this.isMonthly = credit.isMonthly;

        this.payments = CreditManager.convertPayments(credit.payments);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.date = df.format(credit.date.getTime());
        System.out.println(this.date);
    }
}
