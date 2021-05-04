package com.example.roomwordssample;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ExpensesViewModel extends AndroidViewModel {

    private ExpensesRepository mRepository;

    private final LiveData<List<Expenses>> mAllExpenses;

    public ExpensesViewModel(Application application) {
        super(application);
        mRepository = new ExpensesRepository(application);
        mAllExpenses = mRepository.getAllExpenses();
    }

    // Get all expenses registered in Room DB
    LiveData<List<Expenses>> getAllExpenses() { return mAllExpenses; }

    // Get expenses registered for a given category
    LiveData<Float> getCategoryTotal(String category) {
        return mRepository.getCategoryTotal(category);
    }

    public void insert(Expenses expense) { mRepository.insert(expense); }

    public void deleteAll() { mRepository.deleteAll(); }
}