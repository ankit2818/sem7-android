package com.zhulie.zhulie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zhulie.zhulie.Constants.LOGINURL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

  private EditText loginEmail, loginPassword;
  private Button loginButton, registerButton;
  private TextView emailError, passwordError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    /** find element */
    loginEmail = findViewById(R.id.loginEmail);
    loginPassword = findViewById(R.id.loginPassword);
    loginButton = findViewById(R.id.loginButton);
    registerButton = findViewById(R.id.registerButton);

    emailError = findViewById(R.id.emailError);
    passwordError = findViewById(R.id.passwordError);

    loginEmail.addTextChangedListener(this);
    loginPassword.addTextChangedListener(this);

    loginButton.setOnClickListener(this);
    registerButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.registerButton:
        goToRegisterPage();
        break;
      case R.id.loginButton:
        loginWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString());
        break;
    }
  }

  private void goToRegisterPage() {
    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    finish();
  }

  private void loginWithEmailAndPassword(String loginEmail, String loginPassword) {

    Map<String, String> params = new HashMap<>();
    params.put("email", loginEmail);
    params.put("password", loginPassword);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            LOGINURL,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  if (response.getBoolean("loggedIn")) {
                    if(emailError.getVisibility() == View.VISIBLE || passwordError.getVisibility() == View.VISIBLE) {
                      emailError.setVisibility(View.GONE);
                      passwordError.setVisibility(View.GONE);
                    }
                    String token = response.getString("token");
                    String email = response.getString("email");
                    String name = response.getString("name");
                    String imageUri = response.getString("imageUri");

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    /** Save to SharedPreference */
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("zhulie", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedIn", response.getBoolean("loggedIn"));
                    editor.putString("email", email);
                    editor.putString("name", name);
                    editor.putString("imageUri", imageUri);
                    editor.putString("token", token);
                    editor.apply();
                    startActivity(mainIntent);
                    finish();
                  } else {
                    if(!response.getString("email").equals("")) {
                      emailError.setText(response.getString("email"));
                      emailError.setVisibility(View.VISIBLE);
                    }
                    if(!response.getString("password").equals("")) {
                      passwordError.setText(response.getString("password"));
                      passwordError.setVisibility(View.VISIBLE);
                    }
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
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
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    emailError.setText("");
    emailError.setVisibility(View.GONE);
    passwordError.setText("");
    passwordError.setVisibility(View.GONE);
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }
}
