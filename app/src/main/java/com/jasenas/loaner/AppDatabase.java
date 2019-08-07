package com.jasenas.loaner;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CreditDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CreditDAO getCreditDAO();
}
