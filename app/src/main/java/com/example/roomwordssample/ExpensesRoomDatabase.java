package com.example.roomwordssample;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Expenses.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class ExpensesRoomDatabase extends RoomDatabase {
    public abstract ExpensesDao expensesDao();

    private static volatile ExpensesRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ExpensesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExpensesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpensesRoomDatabase.class, "expenses_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ExpensesDao dao = INSTANCE.expensesDao();
                dao.deleteAll();

                Date date = new Date();
                Expenses exp = new Expenses((float)0.0, "grocery", date);
                dao.insert(exp);
            });
        }
    };

}
