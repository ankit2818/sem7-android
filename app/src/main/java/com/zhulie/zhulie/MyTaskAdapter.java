package com.zhulie.zhulie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.MyTaskViewHolder> {

  private Context context;
  private ArrayList<Task> myTaskArrayList;

  public MyTaskAdapter(Context context, ArrayList<Task> taskArrayList) {
    this.context = context;
    this.myTaskArrayList = taskArrayList;
  }

  @NonNull
  @Override
  public MyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.single_task_layout, viewGroup, false);
    MyTaskAdapter.MyTaskViewHolder myTaskViewHolder = new MyTaskAdapter.MyTaskViewHolder(view);
    return myTaskViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull MyTaskViewHolder myTaskViewHolder, int i) {
    TextView taskName = myTaskViewHolder.taskName;
    TextView taskDescription = myTaskViewHolder.taskDescription;
    TextView fromDate = myTaskViewHolder.fromDate;
    TextView toDate = myTaskViewHolder.toDate;
    TextView priority = myTaskViewHolder.priority;

    taskName.setText(myTaskArrayList.get(i).getName());
    taskDescription.setText(myTaskArrayList.get(i).getDesc());
    fromDate.setText(myTaskArrayList.get(i).getFromdate());
    toDate.setText(myTaskArrayList.get(i).getTodate());
    priority.setText(myTaskArrayList.get(i).getPrority());
  }

  @Override
  public int getItemCount() {
    return myTaskArrayList.size();
  }


  public class MyTaskViewHolder extends RecyclerView.ViewHolder {

    TextView taskName, taskDescription, priority, fromDate, toDate;

    public MyTaskViewHolder(@NonNull View itemView) {
      super(itemView);

      taskName = itemView.findViewById(R.id.taskName);
      taskDescription = itemView.findViewById(R.id.taskDescription);
      priority = itemView.findViewById(R.id.priority);
      fromDate = itemView.findViewById(R.id.fromDate);
      toDate = itemView.findViewById(R.id.toDate);
    }
  }
}
