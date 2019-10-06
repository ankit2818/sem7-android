package com.zhulie.zhulie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyTasksFragment extends Fragment {

  private View view;
  private RecyclerView myTaskRecyclerView;

  ArrayList<Task> myTasksList;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.my_tasks_fragment, container, false);

    myTasksList = new ArrayList<>();
    for (int k = 0; k < 5 ; k++) {
      myTasksList.add(new Task("Task "+k, "b scahsc nm asjc h uiukvju kg oeighjjgr  jemfn mcnxv jn nfv", "10-20-5201", "12-54-5214", "High"));
    }

    myTaskRecyclerView = view.findViewById(R.id.myTaskRecyclerView);
    MyTaskAdapter myTaskAdapter = new MyTaskAdapter(getContext(), myTasksList);
    myTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    myTaskRecyclerView.setAdapter(myTaskAdapter);

    return view;
  }
}
