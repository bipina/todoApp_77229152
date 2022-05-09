package com.example.todoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todoapp.Adapter.TodoAdapter;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.TaskViewModel;
import com.example.todoapp.R;
import com.example.todoapp.Data.Repository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    Toolbar toolbar;
    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication()).create(TaskViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            populateData(tasks);
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.menu_new:
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
                break;
            case R.id.menu_delete:
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Are you sure want to delete all task ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                TaskViewModel.deleteAll();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.menu_about:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;
            case R.id.menu_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void populateData(List<Task> taskList) {
        if (taskList.size() > 0) {
            adapter = new TodoAdapter(taskList, MainActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no_data).setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
        }
    }
}