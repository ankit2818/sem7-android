package com.zhulie.zhulie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

  private View view;
  private RecyclerView recyclerView;
  FloatingActionButton addNewProject;

  ArrayList<Team> teamArrayList;
  String[] urls = {"https://loremflickr.com/200/200", "https://loremflickr.com/300/300", "https://loremflickr.com/400/400", "https://loremflickr.com/500/500"};
  String[] names = {"User1", "User2", "User3", "User4"};

  String[] projectIds = {"546534","5645342","54351","534687643541", "354654", "3545654315631", "56465465", "5867845","86798645","87645"};

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.home_fragment, container, false);

    teamArrayList = new ArrayList<>();
    for (int k = 0; k < 10 ; k++) {
      teamArrayList.add(new Team(projectIds[k],urls, names, "Project Title "+k, "This is description for project "+k+" hgk as hkj shdvkjkg hjjgv bjnbsd jvm bbv j dvjbjvh", "12/02/2018"));
    }

    recyclerView = view.findViewById(R.id.recyclerView);
    addNewProject = view.findViewById(R.id.addNewProject);

    TeamAdapter teamAdapter = new TeamAdapter(getContext(), teamArrayList);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(teamAdapter);

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0)
          addNewProject.hide();
        else if (dy < 0)
          addNewProject.show();
      }
    });

    addNewProject.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.addNewProject) {
      openDialog();
    }
  }

  private void openDialog() {
    AddProject addProject = new AddProject();
    assert getFragmentManager() != null;
    addProject.show(getFragmentManager(), "Add New Project Dialog");
  }
}
