package com.zhulie.zhulie;

import java.util.ArrayList;

public class Project {
  private String projectId;
  private ArrayList<String> imageURL;
  private ArrayList<String> teamMembers;
  private String projectName;
  private String description;
  private String dateCreated;

  private String dateTill;

  public Project(String projectId, ArrayList<String> imageURL, ArrayList<String> teamMembers, String projectName, String description, String dateCreated, String dateTill) {
    this.projectId = projectId;
    this.imageURL = imageURL;
    this.teamMembers = teamMembers;
    this.projectName = projectName;
    this.description = description;
    this.dateCreated = dateCreated;
    this.dateTill = dateTill;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public void setImageURL(ArrayList<String> imageURL) {
    this.imageURL = imageURL;
  }

  public void setTeamMembers(ArrayList<String> teamMembers) {
    this.teamMembers = teamMembers;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  public void setDateTill(String dateTill) {
    this.dateTill = dateTill;
  }


  public String getDateTill() {
    return dateTill;
  }

  public String getProjectId() {
    return projectId;
  }

  public ArrayList<String> getImageURL() {
    return imageURL;
  }

  public ArrayList<String> getTeamMembers() {
    return teamMembers;
  }

  public String getProjectName() {
    return projectName;
  }

  public String getDescription() {
    return description;
  }

  public String getDateCreated() {
    return dateCreated;
  }

}
