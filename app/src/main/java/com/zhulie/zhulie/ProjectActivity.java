package com.zhulie.zhulie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectActivity extends AppCompatActivity {

  private CircleImageView user1, user2, user3, user4;
  private TextView projectName, descriptionProject;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project);

    Intent intent = getIntent();
    String id = intent.getStringExtra("projectId");
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

    projectName.setText(name);
    descriptionProject.setText(desc);
    Glide.with(getApplicationContext()).load(userImage1).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user1);
    Glide.with(getApplicationContext()).load(userImage2).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user2);
    Glide.with(getApplicationContext()).load(userImage3).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user3);
    Glide.with(getApplicationContext()).load(userImage4).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(user4);
  }
}
