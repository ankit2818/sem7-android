package com.zhulie.zhulie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.zhulie.zhulie.Constants.REGISTERURL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

  private EditText registerName, registerEmail, registerPassword, registerConfirmPassword;
  private Button registerButton, loginButton;
  private TextView nameError, emailError, passwordError, confirmPasswordError;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    /** findViewById() */
    registerName = findViewById(R.id.registerName);
    registerEmail = findViewById(R.id.registerEmail);
    registerPassword = findViewById(R.id.registerPassword);
    registerConfirmPassword = findViewById(R.id.registerConfirmPassword);

    nameError = findViewById(R.id.nameError);
    emailError = findViewById(R.id.emailError);
    passwordError = findViewById(R.id.passwordError);
    confirmPasswordError = findViewById(R.id.confirmPasswordError);

    registerButton = findViewById(R.id.registerButton);
    loginButton = findViewById(R.id.loginButton);

    registerEmail.addTextChangedListener(this);
    registerName.addTextChangedListener(this);
    registerPassword.addTextChangedListener(this);
    registerConfirmPassword.addTextChangedListener(this);

    /** Set Listener */
    registerButton.setOnClickListener(this);
    loginButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.registerButton:
        registerUser(registerName.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString(), registerConfirmPassword.getText().toString());
        break;
      case R.id.loginButton:
        goToLoginPage();
        break;
    }
  }

  private void goToLoginPage() {
    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    finish();
  }

  private void registerUser(final String registerName, final String registerEmail, final String registerPassword, String registerConfirmPassword) {

    Map<String, String> params = new HashMap<>();
    params.put("name", registerName);
    params.put("email", registerEmail);
    params.put("password", registerPassword);
    params.put("confirmPassword", registerConfirmPassword);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            REGISTERURL,
            new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  if (response.getBoolean("status")) {
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    if (nameError.getVisibility() == View.VISIBLE || emailError.getVisibility() == View.VISIBLE || passwordError.getVisibility() == View.VISIBLE || confirmPasswordError.getVisibility() == View.VISIBLE) {
                      nameError.setVisibility(View.GONE);
                      emailError.setVisibility(View.GONE);
                      passwordError.setVisibility(View.GONE);
                      confirmPasswordError.setVisibility(View.GONE);
                    }
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    loginIntent.putExtra("email", response.getString("email"));
                    startActivity(loginIntent);
                  } else {
                    Log.d("res", String.valueOf(response));
                    if (!response.getString("name").equals("")) {
                      nameError.setText(response.getString("name"));
                      nameError.setVisibility(View.VISIBLE);
                    }
                    if (!response.getString("email").equals("")) {
                      emailError.setText(response.getString("email"));
                      emailError.setVisibility(View.VISIBLE);
                    }
                    if (!response.getString("password").equals("")) {
                      passwordError.setText(response.getString("password"));
                      passwordError.setVisibility(View.VISIBLE);
                    }
                    if (!response.getString("confirmPassword").equals("")) {
                      confirmPasswordError.setText(response.getString("confirmPassword"));
                      confirmPasswordError.setVisibility(View.VISIBLE);
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
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
              }
            }
    ) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
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
    nameError.setText("");
    nameError.setVisibility(View.GONE);
    emailError.setText("");
    emailError.setVisibility(View.GONE);
    passwordError.setText("");
    passwordError.setVisibility(View.GONE);
    confirmPasswordError.setText("");
    confirmPasswordError.setVisibility(View.GONE);
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }
}
