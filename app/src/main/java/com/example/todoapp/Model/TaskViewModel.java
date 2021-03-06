package com.example.todoapp.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoapp.Data.Repository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static Repository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allTasks = repository.getAllTask();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public static void insert(Task task) {
        repository.insert(task);
    }

    public LiveData<Task> get(int id) {
        return repository.getTaskById(id);
    }

    public static void update(Task task) {
        repository.update(task);
    }

    public static void delete(Task task) {
        repository.delete(task);
    }

    public static void deleteAll() {
        repository.deleteAll();
    }
}


