package com.example.roomwordssample;

import androidx.annotation.NonNull;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "expenses_table")
public class Expenses {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "expense_id")
    private int mExpense_id;

    @ColumnInfo(name = "expense")
    private float mExpense;

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverters.class)
    private Date mDate;

    public Expenses(@NonNull float mExpense, @NonNull String mCategory, @NonNull Date mDate) {
        this.mExpense = mExpense;
        this.mCategory = mCategory;
        this.mDate = mDate;
    }

    @NonNull
    public final String getRecord() {
        String expense = new String("");
        expense = Float.toString(mExpense) + " " + mCategory +
                mDate.toString();
        return expense;
    }

    public final int getExpense_id() { return this.mExpense_id; }
    public final float getExpense() { return this.mExpense; }
    public final String getCategory() { return this.mCategory; }
    public final Date getDate() { return this.mDate; }

    public void setExpense_id(int mExpense_id) { this.mExpense_id = mExpense_id; }
}
