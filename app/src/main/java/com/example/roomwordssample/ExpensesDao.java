package com.example.roomwordssample;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ExpensesDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Expenses expense);

    @Query("DELETE FROM expenses_table")
    void deleteAll();

    @Query("SELECT * FROM expenses_table ORDER BY expense ASC")
    LiveData<List<Expenses>> getAllExpenses();

    @Query("SELECT SUM(expense) as total FROM expenses_table where category LIKE :category")
    LiveData<Float> getCategoryTotal(String category);

}
