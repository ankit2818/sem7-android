package com.zhulie.zhulie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.zhulie.zhulie.Constants.BASEURL;

public class AddProject extends AppCompatDialogFragment {

  private EditText projectName, description, projectMembers;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View view = inflater.inflate(R.layout.add_project_form, null);

    projectName = view.findViewById(R.id.projectName);
    description = view.findViewById(R.id.description);
    projectMembers = view.findViewById(R.id.projectMembers);

    builder
            .setView(view)
            .setTitle("Add New Project")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                String projectname = projectName.getText().toString();
                String desc = description.getText().toString();
                String[] members = projectMembers.getText().toString().trim().split(",");

                /** Add New Project */
                if(AddNewProject(projectname, desc, members)) {
                  Toast.makeText(getContext(), "Project Added Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                  Toast.makeText(getContext(), "Failed to add Project", Toast.LENGTH_SHORT).show();
                }
              }
            });

    return builder.create();
  }

  private boolean AddNewProject(String projectname, String desc, String[] members) {
    final boolean[] created = {false};

    Map<String, String> params = new HashMap<>();
    params.put("projectName", projectname);
    params.put("description", desc);
    JSONArray jsonArray = new JSONArray(Arrays.asList(members));
    params.put("members", String.valueOf(jsonArray));

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            BASEURL,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                created[0] = true;
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                created[0] = false;
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
    return created[0];
  }
}
