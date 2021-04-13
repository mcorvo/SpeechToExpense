package com.example.roomwordssample;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

class ExpensesRepository {

    private ExpensesDao mExpensesDao;
    private LiveData<List<Expenses>> mAllExpenses;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ExpensesRepository(Application application) {
        ExpensesRoomDatabase db = ExpensesRoomDatabase.getDatabase(application);
        mExpensesDao = db.expensesDao();
        mAllExpenses = mExpensesDao.getAllExpenses();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Expenses>> getAllExpenses() {
        return mAllExpenses;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Expenses expense) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mExpensesDao.insert(expense);
        });
    }

    void deleteAll() {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mExpensesDao.deleteAll());
    }
}