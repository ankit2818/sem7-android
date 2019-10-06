package com.zhulie.zhulie;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zhulie.zhulie.Constants.CREATEPROJECT;
import static com.zhulie.zhulie.Constants.GETPROJECT;

public class ProjectFragment extends Fragment implements View.OnClickListener {

  private View view;
  private RecyclerView recyclerView;
  FloatingActionButton addNewProject;
  private String projectname, token;
  private String desc;
  private String projectmembers;
  private String projectguide, fromdate, todate;
  private EditText projectName, description, projectMembers, projectGuide, projectFromDate, projectToDate;
  private ProjectAdapter projectAdapter;
  private ProgressBar progressBar;
  private LinearLayout failedProjectView;
  private Button retryButton;
  private SharedPreferences sharedPreferences;

  ArrayList<Project> projectArrayList;
  String[] urls = {"https://loremflickr.com/200/200", "https://loremflickr.com/300/300", "https://loremflickr.com/400/400", "https://loremflickr.com/500/500"};
  String[] names = {"User1", "User2", "User3", "User4"};

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.project_fragment, container, false);

    /** Get Token */
    sharedPreferences = getActivity().getSharedPreferences("zhulie", 0);
    token = sharedPreferences.getString("token", "");

    projectArrayList = new ArrayList<>();

    recyclerView = view.findViewById(R.id.recyclerView);
    addNewProject = view.findViewById(R.id.addNewProject);
    progressBar = view.findViewById(R.id.projectProgressbar);
    failedProjectView = view.findViewById(R.id.projectFailed);
    retryButton = view.findViewById(R.id.retryButton);

    projectAdapter = new ProjectAdapter(getContext(), projectArrayList);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(projectAdapter);
    addData(getActivity());

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

  private void addData(final FragmentActivity activity) {
    if(progressBar.getVisibility() == View.GONE) {
      failedProjectView.setVisibility(View.GONE);
      progressBar.setVisibility(View.VISIBLE);
    }
    /** Add New Project */
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            GETPROJECT,
            null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                /** Process Response */
                try {
                  progressBar.setVisibility(View.GONE);
                  if(response.getBoolean("success")) {
                    JSONArray projectArray = response.getJSONArray("projects");
                    for(int k = 0; k < projectArray.length(); k++) {
                      JSONObject projectObject = (JSONObject) projectArray.get(k);
                      JSONArray projectMembers = projectObject.getJSONArray("members");
                      ArrayList<String> members = new ArrayList<>();
                      ArrayList<String> names = new ArrayList<>();
                      for(int i = 0 ; i < projectMembers.length(); i++) {
                        String email = projectMembers.getString(i);
                        members.add(email);
                        names.add(email.split("@")[0]);
                      }
                      members.add(sharedPreferences.getString("email", ""));
                      names.add(sharedPreferences.getString("name", ""));
                      projectArrayList.add(new Project(projectObject.getString("_id"), members, names, projectObject.getString("projectName"), projectObject.getString("description"), projectObject.getString("from").split("T")[0], projectObject.getString("to").split("T")[0]));
                      projectAdapter.notifyDataSetChanged();
                    }
                  } else {
                    failedProjectView.setVisibility(View.VISIBLE);
                    retryButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        addData(activity);
                      }
                    });
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                failedProjectView.setVisibility(View.VISIBLE);
                retryButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    addData(activity);
                  }
                });
                /** Process Error */
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                  Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(getContext(), "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                  Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                  Toast.makeText(getContext(), "Failed to process data", Toast.LENGTH_SHORT).show();
                }
              }
            }
    ) {
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", token);
        return headers;
      }
    };
    /** Add to queue */
    MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.addNewProject) {
      openDialog();
    }
  }

  private void openDialog() {
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
    dialogBuilder.setTitle("Add Project");
    LayoutInflater layoutInflater = this.getLayoutInflater();
    final View dialogView = layoutInflater.inflate(R.layout.add_project_form, null);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        /** Request For Data */
        projectName = dialogView.findViewById(R.id.alertProjectName);
        description = dialogView.findViewById(R.id.alertDescription);
        projectMembers = dialogView.findViewById(R.id.alertProjectMembers);
        projectGuide = dialogView.findViewById(R.id.alertProjectGuide);
        projectFromDate = dialogView.findViewById(R.id.alertFromDate);
        projectToDate = dialogView.findViewById(R.id.alertToDate);

        projectname = projectName.getText().toString();
        desc = description.getText().toString();
        projectmembers = projectMembers.getText().toString().trim().replaceAll(" ", "");
        projectguide = projectGuide.getText().toString().trim();
        fromdate = projectFromDate.getText().toString().trim();
        todate = projectToDate.getText().toString().trim();


        /** Add New Project */
        Map<String, String> params = new HashMap<>();
        params.put("projectName", projectname);
        params.put("description", desc);
        params.put("members", projectmembers);
        params.put("guide", projectguide);
        params.put("from", fromdate);
        params.put("to", todate);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                CREATEPROJECT,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                    /** Process Response */
                    try {
                      if (response.getBoolean("success")) {
                        Toast.makeText(getContext(), "Successfully added project", Toast.LENGTH_SHORT).show();
                        JSONObject project = response.getJSONObject("Project");
                        JSONArray projectMembers = project.getJSONArray("members");
                        ArrayList<String> members = new ArrayList<>();
                        ArrayList<String> names = new ArrayList<>();
                        for(int i = 0 ; i < projectMembers.length(); i++) {
                          String email = projectMembers.getString(i);
                          members.add(email);
                          names.add(email.split("@")[0]);
                        }
                        members.add(sharedPreferences.getString("email", ""));
                        names.add(sharedPreferences.getString("name", ""));
                        projectArrayList.add(new Project(project.getString("_id"), members, names, project.getString("projectName"), project.getString("description"), project.getString("from").split("T")[0], project.getString("to").split("T")[0]));
                        projectAdapter.notifyDataSetChanged();
                      } else {
                        Toast.makeText(getContext(), "Failed to add project", Toast.LENGTH_SHORT).show();
                      }
                    } catch (JSONException e) {
                      e.printStackTrace();
                    }
                  }
                },
                new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                    /** Process Error */
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                      Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                      Toast.makeText(getContext(), "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                      Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                      Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                      Toast.makeText(getContext(), "Failed to process data", Toast.LENGTH_SHORT).show();
                    }
                  }
                }
        ) {
          @Override
          public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", token);
            return headers;
          }
        };
        /** Add to queue */
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
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
}
