package com.zhulie.zhulie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.zhulie.zhulie.Constants.GETPROJECT;
import static com.zhulie.zhulie.Constants.GETTASK;

public class ProjectActivity extends AppCompatActivity {

  private CircleImageView user1, user2, user3, user4;
  private TextView projectName, descriptionProject;
  private RecyclerView tasksList;
  private FloatingActionButton addNewTask;
  private ArrayList<Task> taskArrayList;
  private SharedPreferences sharedPreferences;
  private ProgressBar progressBar;
  private Button retryTaskButton;
  private LinearLayout taskFailed;
  private TaskAdapter taskAdapter;
  private String projectId;

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

    projectName.setText(name);
    descriptionProject.setText(desc);
    Glide.with(getApplicationContext()).load(userImage1).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user1);
    Glide.with(getApplicationContext()).load(userImage2).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user2);
    Glide.with(getApplicationContext()).load(userImage3).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user3);
    Glide.with(getApplicationContext()).load(userImage4).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user4);

    sharedPreferences = getApplicationContext().getSharedPreferences("zhulie", 0);

    /** Add data to TaskList */
    taskArrayList = new ArrayList<>();
//    for (int k = 0; k < 50 ; k++) {
//      taskArrayList.add(new Task("Task "+k, "b scahsc nm asjc h uiukvju kg oeighjjgr  jemfn mcnxv jn nfv", "10-20-5201", "12-54-5214", "High", "https://loremflickr.com/200/200"));
//    }

    taskAdapter = new TaskAdapter(getApplicationContext(), taskArrayList);
    tasksList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    tasksList.setAdapter(taskAdapter);
    addTasks(getApplicationContext());

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
  }

  private void addTasks(final Context applicationContext) {
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
                Log.d("Response", String.valueOf(response));
                /** Process Response */
                try {
                  progressBar.setVisibility(View.GONE);
                  if(response.getBoolean("success")) {
                    JSONArray taskArray = response.getJSONArray("task");
                    for(int k = 0; k < taskArray.length(); k++) {
                      JSONObject task = (JSONObject) taskArray.get(k);
                      taskArrayList.add(new Task(task.getString("title"), task.getString("description"), task.getString("from").split("T")[0], task.getString("to").split("T")[0], task.getString("priority"), task.getString("assigned")));
                      taskAdapter.notifyDataSetChanged();
                    }
                  } else {
                    taskFailed.setVisibility(View.VISIBLE);
                    retryTaskButton.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        addTasks(applicationContext);
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
                    addTasks(applicationContext);
                  }
                });
                /** Process Error */
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                  Toast.makeText(applicationContext, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                  Toast.makeText(applicationContext, "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                  Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                  Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                  Toast.makeText(applicationContext, "Failed to process data", Toast.LENGTH_SHORT).show();
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
    MySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest);
  }

}
