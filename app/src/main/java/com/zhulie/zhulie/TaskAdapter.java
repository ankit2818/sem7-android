package com.zhulie.zhulie;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

  private Context context;
  private ArrayList<Task> taskArrayList;

  public TaskAdapter(Context context, ArrayList<Task> taskArrayList) {
    this.context = context;
    this.taskArrayList = taskArrayList;
  }

  @NonNull
  @Override
  public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.single_task_layout, viewGroup, false);
    TaskAdapter.TaskViewHolder taskViewHolder = new TaskAdapter.TaskViewHolder(view);
    return taskViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
    CircleImageView assignedUserImage = taskViewHolder.assignedUserImage;
    TextView taskName = taskViewHolder.taskName;
    TextView taskDescription = taskViewHolder.taskDescription;
    TextView fromDate = taskViewHolder.fromDate;
    TextView toDate = taskViewHolder.toDate;
    TextView priority = taskViewHolder.priority;

    taskName.setText(taskArrayList.get(i).getName());
    taskDescription.setText(taskArrayList.get(i).getDesc());
    fromDate.setText(taskArrayList.get(i).getFromdate());
    toDate.setText(taskArrayList.get(i).getTodate());
    priority.setText(taskArrayList.get(i).getPrority());

    Glide.with(context).load(Uri.parse(taskArrayList.get(i).getImage())).placeholder(R.drawable.user).error(R.drawable.error).into(assignedUserImage);
  }

  @Override
  public int getItemCount() {
    return taskArrayList.size();
  }


  public class TaskViewHolder extends RecyclerView.ViewHolder {

    TextView taskName, taskDescription, priority, fromDate, toDate;
    CircleImageView assignedUserImage;

    public TaskViewHolder(@NonNull View itemView) {
      super(itemView);

      taskName = itemView.findViewById(R.id.taskName);
      taskDescription = itemView.findViewById(R.id.taskDescription);
      priority = itemView.findViewById(R.id.priority);
      fromDate = itemView.findViewById(R.id.fromDate);
      toDate = itemView.findViewById(R.id.toDate);
      assignedUserImage = itemView.findViewById(R.id.assignedUserImage);
    }
  }
}
