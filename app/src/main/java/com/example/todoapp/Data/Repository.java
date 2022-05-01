package com.example.todoapp.Data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.todoapp.Database.DatabaseHelper;
import com.example.todoapp.Model.Task;

import java.util.List;

public class Repository {

    private LiveData<List<Task>> taskList;
    private static Repository sINSTANCE;
    private TaskDao taskDao;

    public Repository(Context context) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        taskDao=database.todoDao();
        taskList = taskDao.getAllTasks();
    }

    public static Repository getInstance(Context context){
        if(sINSTANCE == null){
            sINSTANCE = new Repository(context);
       }
        return sINSTANCE;
    }


    public LiveData<Task> getTaskById(int id){
        return taskDao.getTaskById(id);
    }

    public LiveData<List<Task>> getAllTask(){
        return taskList;
    }

    public void insert(Task task){
        DatabaseHelper.databaseWriteExecutor.execute(() -> taskDao.insert(task));
    }

    public void update(Task task){
        DatabaseHelper.databaseWriteExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task){
        DatabaseHelper.databaseWriteExecutor.execute(() -> taskDao.delete(task));
   }

    public void deleteAll(){
        DatabaseHelper.databaseWriteExecutor.execute(() -> taskDao.deleteAll());
    }
}
