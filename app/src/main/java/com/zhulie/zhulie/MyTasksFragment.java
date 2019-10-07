package com.zhulie.zhulie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static com.zhulie.zhulie.Constants.GETTASK;

public class MyTasksFragment extends Fragment {

  private View view;
  private RecyclerView myTaskRecyclerView;
  private LinearLayout myTaskFailed, noTask;
  private Button retryMyTaskButton;
  private ProgressBar myTaskProgressbar;
  private SharedPreferences sharedPreferences;

  ArrayList<Task> myTasksList;
  private String token;
  private MyTaskAdapter myTaskAdapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.my_tasks_fragment, container, false);

    /** Get Token */
    sharedPreferences = getActivity().getSharedPreferences("zhulie", 0);
    token = sharedPreferences.getString("token", "");

    myTasksList = new ArrayList<>();
//    for (int k = 0; k < 5 ; k++) {
//      myTasksList.add(new Task("Task "+k, "b scahsc nm asjc h uiukvju kg oeighjjgr  jemfn mcnxv jn nfv", "10-20-5201", "12-54-5214", "High"));
//    }

    myTaskRecyclerView = view.findViewById(R.id.myTaskRecyclerView);
    myTaskProgressbar = view.findViewById(R.id.myTaskProgressbar);
    myTaskFailed = view.findViewById(R.id.myTaskFailed);
    noTask = view.findViewById(R.id.noTask);
    retryMyTaskButton = view.findViewById(R.id.retryMyTaskButton);

    myTaskAdapter = new MyTaskAdapter(getContext(), myTasksList);
    myTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    myTaskRecyclerView.setAdapter(myTaskAdapter);

    addTasks(getContext());

    return view;
  }

  private void addTasks(final Context applicationContext) {
    if(myTaskProgressbar.getVisibility() == View.GONE) {
      myTaskFailed.setVisibility(View.GONE);
      myTaskProgressbar.setVisibility(View.VISIBLE);
    }

    /** Add New Project */
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            GETTASK,
            null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                /** Process Response */
                try {
                  myTaskProgressbar.setVisibility(View.GONE);
                  if(response.getBoolean("success")) {
                    JSONArray taskArray = response.getJSONArray("task");
                    if(taskArray.length() == 0) {
                      noTask.setVisibility(View.VISIBLE);
                    } else {
                      for(int k = 0; k < taskArray.length(); k++) {
                        JSONObject task = (JSONObject) taskArray.get(k);
                        myTasksList.add(new Task(task.getString("title"), task.getString("description"), task.getString("from").split("T")[0], task.getString("to").split("T")[0], task.getString("priority"), task.getString("assigned")));
                        myTaskAdapter.notifyDataSetChanged();
                      }
                    }
                  } else {
                    myTaskFailed.setVisibility(View.VISIBLE);
                    retryMyTaskButton.setOnClickListener(new View.OnClickListener() {
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
                myTaskProgressbar.setVisibility(View.GONE);
                myTaskFailed.setVisibility(View.VISIBLE);
                retryMyTaskButton.setOnClickListener(new View.OnClickListener() {
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
