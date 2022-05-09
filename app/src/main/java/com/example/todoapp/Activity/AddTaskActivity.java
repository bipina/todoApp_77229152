package com.example.todoapp.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.Model.Task;
import com.example.todoapp.Model.TaskViewModel;
import com.example.todoapp.R;
import com.example.todoapp.Data.Repository;
import com.example.todoapp.Utils.DateUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    TextInputLayout tlTitle, tlDescription, tlDate;
    EditText etTitle, etDescription, etDate;
    RadioButton rbHigh, rbMedium, rbLow;
    Button btnSubmit;
    Toolbar toolbar;
    private Repository repository;
    Bundle bb;
    boolean isEdit = false;
    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_task);

        //variable and class declaration
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                AddTaskActivity.this.getApplication()).create(TaskViewModel.class);

        bb = getIntent().getExtras();
        repository = Repository.getInstance(AddTaskActivity.this);
        tlTitle = findViewById(R.id.tl_title);
        tlDescription = findViewById(R.id.tl_description);
        tlDate = findViewById(R.id.tl_date);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        rbHigh = findViewById(R.id.rd_high);
        rbMedium = findViewById(R.id.rd_medium);
        rbLow = findViewById(R.id.rd_low);
        btnSubmit = findViewById(R.id.btn_add);
        toolbar = findViewById(R.id.toolbar);

        //default radio button selection
        rbHigh.setChecked(true);

        //toolbar setup
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        String month = "";
                        String day = "";
                        if (selectedmonth < 10) {
                            month = "0" + String.valueOf(selectedmonth + 1);
                        } else {
                            month = String.valueOf(selectedmonth + 1);
                        }
                        if (selectedday < 10) {
                            day = "0" + String.valueOf(selectedday);
                        } else {
                            day = String.valueOf(selectedday);
                        }
                        String date = String.valueOf(selectedyear + "/" + month + "/" + day);
                        etDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });

        //for edit option
        if (bb != null) {
            isEdit = true;
            taskViewModel.get(bb.getInt("task_id")).observe(this, tasks -> {
                etTitle.setText(tasks.getTitle());
                etDescription.setText(tasks.getDescription());
                etDate.setText(DateUtils.convertToString(tasks.getDueDate(), "YYYY/mm/dd"));
                if (tasks.getPriority() == 1) {
                    rbHigh.setChecked(true);
                } else if (tasks.getPriority() == 2) {
                    rbMedium.setChecked(true);
                } else {
                    rbLow.setChecked(true);
                }
            });
            btnSubmit.setText("Update");
            toolbar.setTitle("Update Todo");
        }

        //onclick submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTitle.getText().toString().isEmpty()) {
                    tlTitle.setError("Required.");
                    etTitle.requestFocus();
                } else if (etDescription.getText().toString().isEmpty()) {
                    tlDescription.setError("Required.");
                    etDescription.requestFocus();
                } else if (etDate.getText().toString().isEmpty()) {
                    tlDate.setError("Required.");
                    etDate.requestFocus();
                } else {
                    int priority = 1;
                    if (rbHigh.isChecked()) {
                        priority = 1;
                    } else if (rbMedium.isChecked()) {
                        priority = 2;
                    } else {
                        priority = 3;
                    }
                    Task task = new Task(etTitle.getText().toString(), etDescription.getText().toString(), new Date(), new Date(), DateUtils.convertToDate(etDate.getText().toString()), priority, 0);

                    if (!isEdit) {
                        TaskViewModel.insert(task);
                    } else {
                        task.setId(bb.getInt("task_id"));
                        TaskViewModel.update(task);
                    }
                }
                finish();
            }
        });

       
    }

}
