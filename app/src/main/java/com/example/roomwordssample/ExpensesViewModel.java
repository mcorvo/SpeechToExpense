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

    LiveData<List<Expenses>> getAllExpenses() { return mAllExpenses; }

    public void insert(Expenses expense) { mRepository.insert(expense); }

    public void deleteAll() { mRepository.deleteAll(); }
}