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

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
  private Context context;
  private ArrayList<Team> teamList;

  public TeamAdapter(Context context, ArrayList<Team> teamList) {
    this.context = context;
    this.teamList = teamList;
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
    CircleImageView[] userImages = {teamViewHolder.userImage1, teamViewHolder.userImage2, teamViewHolder.userImage3, teamViewHolder.userImage4};
    TextView[] teamMembers = {teamViewHolder.userName1, teamViewHolder.userName2, teamViewHolder.userName3, teamViewHolder.userName4};

    final TextView projectName = teamViewHolder.projectName;
    TextView dateCreated = teamViewHolder.dateCreated;
    TextView description = teamViewHolder.description;

    final Button viewButton = teamViewHolder.viewButton;
    final String projectId = teamList.get(i).getProjectId();

    projectName.setText(teamList.get(i).getProjectName());
    dateCreated.setText(teamList.get(i).getDateCreated());
    description.setText(teamList.get(i).getDescription());
    viewButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(context, projectName.getText(), Toast.LENGTH_SHORT).show();
        Intent projectIntent = new Intent(viewButton.getContext(), ProjectActivity.class);
        projectIntent.putExtra("projectId", projectId);
        viewButton.getContext().startActivity(projectIntent);
      }
    });
    for (int j = 0; j < userImages.length; j++) {
      teamMembers[j].setText(teamList.get(i).getTeamMembers()[j]);
      Glide.with(context).load(Uri.parse(teamList.get(i).getImageURL()[j])).centerCrop().placeholder(R.drawable.user).error(R.drawable.error).into(userImages[j]);
    }


  }

  @Override
  public int getItemCount() {
    return teamList.size();
  }

  public class TeamViewHolder extends RecyclerView.ViewHolder {

    CircleImageView userImage1, userImage2, userImage3, userImage4;
    TextView projectName, dateCreated, description, userName1, userName2, userName3, userName4;
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
      description = itemView.findViewById(R.id.description);

      viewButton = itemView.findViewById(R.id.viewButton);
    }
  }
}
