package com.example.todoapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Activity.AddTaskActivity;
import com.example.todoapp.Activity.MainActivity;
import com.example.todoapp.Model.TaskViewModel;
import com.example.todoapp.R;
import com.example.todoapp.Model.Task;
import com.example.todoapp.Utils.DateUtils;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {


    private List<Task> data;

    public TodoAdapter(List<Task> data, Context context) {
        this.data = data;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.duedate.setText("Due Date : " + DateUtils.convertToString(item.getDueDate(),"dd MMM,YYYY"));

        String priority = "";
        if (item.getPriority() == 1) {
            priority = "High";
        } else if (item.getPriority() == 2) {
            priority = "Medium";
        } else {
            priority = "Low";
        }
        holder.priority.setText(priority);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AddTaskActivity.class);
                intent.putExtra("task_id",item.getId());
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure want to remove task ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                TaskViewModel.delete(item);
                                data.remove(item);
                                notifyDataSetChanged();
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
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, description, duedate, priority;
        private TextView edit, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_descr);
            duedate = itemView.findViewById(R.id.tv_due);
            priority = itemView.findViewById(R.id.tv_priority);
            edit = itemView.findViewById(R.id.tv_edit);
            remove = itemView.findViewById(R.id.tv_remove);
        }
    }
}
