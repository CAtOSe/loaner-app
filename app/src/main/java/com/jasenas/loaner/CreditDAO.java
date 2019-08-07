package com.jasenas.loaner;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CreditDAO {
    @Insert
    void insert(CreditDB... credits);

    @Update
    void update(CreditDB... credits);

    @Delete
    void delete(CreditDB credit);

    @Query("SELECT * FROM credits")
    List<CreditDB> getItems();

    @Query("SELECT * FROM credits WHERE id = :id")
    CreditDB getItemByID(int id);

    @Query("SELECT MAX(id) FROM credits")
    int getMaxId();
}
