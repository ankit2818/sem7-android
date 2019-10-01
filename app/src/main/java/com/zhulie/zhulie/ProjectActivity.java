package com.zhulie.zhulie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectActivity extends AppCompatActivity {

  private TextView projectId1, projectId2, projectId3, projectId4, projectId5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project);

    Intent intent = getIntent();

    projectId1 = findViewById(R.id.projectId1);
    projectId2 = findViewById(R.id.projectId2);
    projectId3 = findViewById(R.id.projectId3);
    projectId4 = findViewById(R.id.projectId4);
    projectId5 = findViewById(R.id.projectId5);

    projectId1.setText(intent.getStringExtra("projectId"));
    projectId2.setText(intent.getStringExtra("projectId"));
    projectId3.setText(intent.getStringExtra("projectId"));
    projectId4.setText(intent.getStringExtra("projectId"));
    projectId5.setText(intent.getStringExtra("projectId"));
  }
}
