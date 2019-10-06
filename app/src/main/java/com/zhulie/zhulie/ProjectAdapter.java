package com.zhulie.zhulie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.TeamViewHolder> {
  private Context context;
  private ArrayList<Project> projectList;

  public ProjectAdapter(Context context, ArrayList<Project> projectList) {
    this.context = context;
    this.projectList = projectList;
  }

  @NonNull
  @Override
  public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.project_item, viewGroup, false);
    TeamViewHolder teamViewHolder = new TeamViewHolder(view);
    return teamViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull TeamViewHolder teamViewHolder, int i) {
    final int count = i;
    CircleImageView[] userImages = {teamViewHolder.userImage1, teamViewHolder.userImage2, teamViewHolder.userImage3, teamViewHolder.userImage4};
    TextView[] teamMembers = {teamViewHolder.userName1, teamViewHolder.userName2, teamViewHolder.userName3, teamViewHolder.userName4};

    final TextView projectName = teamViewHolder.projectName;
    TextView dateCreated = teamViewHolder.dateCreated;
    TextView dateTill = teamViewHolder.dateTill;
    final TextView description = teamViewHolder.description;

    final Button viewButton = teamViewHolder.viewButton;
    final String projectId = projectList.get(i).getProjectId();

    projectName.setText(projectList.get(i).getProjectName());
    dateCreated.setText(projectList.get(i).getDateCreated());
    dateTill.setText(projectList.get(i).getDateTill());
    description.setText(projectList.get(i).getDescription());
    viewButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(context, projectName.getText(), Toast.LENGTH_SHORT).show();
        /** Start Project Intent and pass the data */
        Intent projectIntent = new Intent(viewButton.getContext(), ProjectActivity.class);
        projectIntent.putExtra("projectId", projectId);
        projectIntent.putExtra("projectName", projectName.getText());
        projectIntent.putExtra("projectDescription", description.getText());
        projectIntent.putExtra("user1ImageUri", projectList.get(count).getImageURL().get(0));
        projectIntent.putExtra("user2ImageUri", projectList.get(count).getImageURL().get(1));
        projectIntent.putExtra("user3ImageUri", projectList.get(count).getImageURL().get(2));
        projectIntent.putExtra("user4ImageUri", projectList.get(count).getImageURL().get(3));
        viewButton.getContext().startActivity(projectIntent);
      }
    });
    for (int j = 0; j < userImages.length; j++) {
      teamMembers[j].setText(projectList.get(i).getTeamMembers().get(j));
      Glide.with(context).load(Uri.parse(projectList.get(i).getImageURL().get(j))).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(userImages[j]);
    }


  }

  @Override
  public int getItemCount() {
    return projectList.size();
  }

  public class TeamViewHolder extends RecyclerView.ViewHolder {

    CircleImageView userImage1, userImage2, userImage3, userImage4;
    TextView projectName, dateCreated, dateTill, description, userName1, userName2, userName3, userName4;
    Button viewButton;

    public TeamViewHolder(@NonNull View itemView) {
      super(itemView);

      userImage1 = itemView.findViewById(R.id.userImage1);
      userImage2 = itemView.findViewById(R.id.userImage2);
      userImage3 = itemView.findViewById(R.id.userImage3);
      userImage4 = itemView.findViewById(R.id.userImage4);

      userName1 = itemView.findViewById(R.id.userName1);
      userName2 = itemView.findViewById(R.id.userName2);
      userName3 = itemView.findViewById(R.id.userName3);
      userName4 = itemView.findViewById(R.id.userName4);

      projectName = itemView.findViewById(R.id.projectName);
      dateCreated = itemView.findViewById(R.id.dateCreated);
      dateTill = itemView.findViewById(R.id.dateTill);
      description = itemView.findViewById(R.id.description);

      viewButton = itemView.findViewById(R.id.viewButton);
    }
  }
}
