package com.example.todoapp.Database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoapp.Utils.Converter;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Data.TaskDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class},  version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class DatabaseHelper extends RoomDatabase {

    public static final String DATABASE_NAME = "TASKDB2";

    private static DatabaseHelper sInstance;
    private static final Object LOCK = new Object();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static final RoomDatabase.Callback databaseCallBack =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(() -> {
                        //Invoke DAO and Write
                        TaskDao todoDao = sInstance.todoDao();
                        todoDao.deleteAll();
                    });
                }
            };

    public static DatabaseHelper getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }

        return sInstance;
    }

    public abstract TaskDao todoDao();

}
