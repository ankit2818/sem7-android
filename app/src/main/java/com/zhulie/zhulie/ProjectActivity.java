package com.zhulie.zhulie;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhulie.zhulie.Constants.ADDTASK;
import static com.zhulie.zhulie.Constants.GETTASK;

public class ProjectActivity extends AppCompatActivity implements View.OnClickListener {

  private CircleImageView user1, user2, user3, user4;
  private TextView projectName, descriptionProject;
  private RecyclerView tasksList;
  private FloatingActionButton addNewTask;
  private ArrayList<Task> taskArrayList;
  private SharedPreferences sharedPreferences;
  private ProgressBar progressBar;
  private Button retryTaskButton;
  private LinearLayout taskFailed, noProjectTask;
  private TaskAdapter taskAdapter;
  private String projectId;
  private EditText alertTaskName, alertTaskDescription, alertTaskAssign, alertTaskFromDate, alertTaskToDate, alertTaskPriority;
  private String alerttaskname, alerttaskdescription, alerttaskassign, alerttaskfromdate, alerttasktodate, alerttaskpriority;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project);

    Intent intent = getIntent();
    projectId = intent.getStringExtra("projectId");
    String name = intent.getStringExtra("projectName");
    String desc = intent.getStringExtra("projectDescription");
    Uri userImage1 = Uri.parse(intent.getStringExtra("user1ImageUri"));
    Uri userImage2 = Uri.parse(intent.getStringExtra("user2ImageUri"));
    Uri userImage3 = Uri.parse(intent.getStringExtra("user3ImageUri"));
    Uri userImage4 = Uri.parse(intent.getStringExtra("user4ImageUri"));

    projectName = findViewById(R.id.titleProjectName);
    descriptionProject = findViewById(R.id.descriptionProject);
    user1 = findViewById(R.id.user1);
    user2 = findViewById(R.id.user2);
    user3 = findViewById(R.id.user3);
    user4 = findViewById(R.id.user4);
    tasksList = findViewById(R.id.tasksList);
    addNewTask = findViewById(R.id.addNewTask);
    retryTaskButton = findViewById(R.id.retryTaskButton);
    progressBar = findViewById(R.id.taskProgressbar);
    taskFailed = findViewById(R.id.taskFailed);
    noProjectTask = findViewById(R.id.noProjectTask);

    projectName.setText(name);
    descriptionProject.setText(desc);
    Glide.with(getApplicationContext()).load(userImage1).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user1);
    Glide.with(getApplicationContext()).load(userImage2).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user2);
    Glide.with(getApplicationContext()).load(userImage3).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user3);
    Glide.with(getApplicationContext()).load(userImage4).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user4);

    sharedPreferences = getApplicationContext().getSharedPreferences("zhulie", 0);

    /** Add data to TaskList */
    taskArrayList = new ArrayList<>();

    taskAdapter = new TaskAdapter(this, taskArrayList);
    tasksList.setLayoutManager(new LinearLayoutManager(this));
    tasksList.setAdapter(taskAdapter);
    addTasks(this);

    tasksList.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
          addNewTask.hide();
        }
        else if (dy < 0) {
          addNewTask.show();
        }
      }
    });

    addNewTask.setOnClickListener(this);
  }

  private void addTasks(final ProjectActivity projectActivity) {
    if(progressBar.getVisibility() == View.GONE) {
      taskFailed.setVisibility(View.GONE);
      progressBar.setVisibility(View.VISIBLE);
    }

    Map<String, String> params = new HashMap<>();
    params.put("projectId", projectId);
    /** Add New Project */
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            GETTASK,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                /** Process Response */
                try {
                  progressBar.setVisibility(View.GONE);
                  if(response.getBoolean("success")) {
                    JSONArray taskArray = response.getJSONArray("task");
                    if(taskArray.length() == 0) {
                      noProjectTask.setVisibility(View.VISIBLE);
                    } else {
                      for(int k = 0; k < taskArray.length(); k++) {
                        JSONObject task = (JSONObject) taskArray.get(k);
                        taskArrayList.add(new Task(task.getString("title"), task.getString("description"), task.getString("from").split("T")[0], task.getString("to").split("T")[0], task.getString("priority"), task.getString("assigned")));
                        taskAdapter.notifyDataSetChanged();
                      }
                    }
                  } else {
                    taskFailed.setVisibility(View.VISIBLE);
                    retryTaskButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        addTasks(projectActivity);
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
                taskFailed.setVisibility(View.VISIBLE);
                retryTaskButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    addTasks(projectActivity);
                  }
                });
                /** Process Error */
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                  Toast.makeText(projectActivity, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(projectActivity, "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                  Toast.makeText(projectActivity, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(projectActivity, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                  Toast.makeText(projectActivity, "Failed to process data", Toast.LENGTH_SHORT).show();
                }
              }
            }
    ) {
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", sharedPreferences.getString("token", ""));
        return headers;
      }
    };
    /** Add to queue */
    MySingleton.getInstance(projectActivity).addToRequestQueue(jsonObjectRequest);
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.addNewTask) {
      openDialog();
    }
  }

  private void openDialog() {
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    dialogBuilder.setTitle("Add Task");
    LayoutInflater layoutInflater = this.getLayoutInflater();
    final View dialogView = layoutInflater.inflate(R.layout.add_task_form, null);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        /** Request For Data */
        /** Find View*/
        alertTaskName = dialogView.findViewById(R.id.alertTaskName);
        alertTaskDescription = dialogView.findViewById(R.id.alertTaskDescription);
        alertTaskAssign = dialogView.findViewById(R.id.alertTaskAssign);
        alertTaskFromDate = dialogView.findViewById(R.id.alertTaskFromDate);
        alertTaskToDate = dialogView.findViewById(R.id.alertTaskToDate);
        alertTaskPriority = dialogView.findViewById(R.id.alertTaskPriority);
        /** Set data */
        alerttaskname = alertTaskName.getText().toString();
        alerttaskdescription = alertTaskDescription.getText().toString();
        alerttaskassign = alertTaskAssign.getText().toString();
        alerttaskfromdate = alertTaskFromDate.getText().toString();
        alerttasktodate = alertTaskToDate.getText().toString();
        alerttaskpriority = alertTaskPriority.getText().toString();

        /** Add New Project */
        Map<String, String> params = new HashMap<>();
        /** params.put() */
        params.put("projectId", projectId);
        params.put("title", alerttaskname);
        params.put("description", alerttaskdescription);
        params.put("from", alerttaskfromdate);
        params.put("to", alerttasktodate);
        params.put("assigned", alerttaskassign);
        params.put("priority", alerttaskpriority);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ADDTASK,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                    /** Process Response */
                    try {
                      if (response.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "Successfully added task", Toast.LENGTH_SHORT).show();
                        JSONObject task = response.getJSONObject("task");
                        taskArrayList.add(new Task(task.getString("title"), task.getString("description"), task.getString("from").split("T")[0], task.getString("to").split("T")[0], task.getString("priority"), task.getString("assigned")));
                        taskAdapter.notifyDataSetChanged();
                      } else {
                        Toast.makeText(getApplicationContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
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
                      Toast.makeText(getApplicationContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                      Toast.makeText(getApplicationContext(), "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                      Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                      Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                      Toast.makeText(getApplicationContext(), "Failed to process data", Toast.LENGTH_SHORT).show();
                    }
                  }
                }
        ) {
          @Override
          public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", sharedPreferences.getString("token", ""));
            return headers;
          }
        };
        /** Add to queue */
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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
