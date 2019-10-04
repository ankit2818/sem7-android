package com.zhulie.zhulie;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zhulie.zhulie.Constants.CREATEPROJECT;

public class HomeFragment extends Fragment implements View.OnClickListener {

  private View view;
  private RecyclerView recyclerView;
  FloatingActionButton addNewProject;
  private String projectname;
  private String desc;
  private String[] projectmembers;
  private EditText projectName, description, projectMembers;
  boolean created = false;

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
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
    dialogBuilder.setTitle("Add Project");
    dialogBuilder.setIcon(R.drawable.error);
    dialogBuilder.setCancelable(false);
    LayoutInflater layoutInflater = this.getLayoutInflater();
    final View dialogView = layoutInflater.inflate(R.layout.add_project_form, null);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        if(AddProject(dialogView)) {
          Toast.makeText(getContext(), "Project Added Successfully", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "Failed to add Project", Toast.LENGTH_SHORT).show();
        }
      }
    });
    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
      }
    });
    AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();
  }

  public boolean AddProject(View dialogView) {
    projectName = dialogView.findViewById(R.id.alertProjectName);
    description = dialogView.findViewById(R.id.alertDescription);
    projectMembers = dialogView.findViewById(R.id.alertProjectMembers);
    projectname = projectName.getText().toString();
    desc = description.getText().toString();
    projectmembers = projectMembers.getText().toString().trim().replaceAll(" ", "").split(",");

    /** Add New Project */
    Map<String, String> params = new HashMap<>();
    params.put("projectName", projectname);
    params.put("description", desc);
    JSONArray jsonArray = new JSONArray(Arrays.asList(projectmembers));
    params.put("members", String.valueOf(jsonArray));

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            CREATEPROJECT,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  created = response.getBoolean("success");
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                created = false;
              }
            }
    ) {
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
      }
    };
    /** Add to queue */
    MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    return created;
  }
}
