package com.zhulie.zhulie;

public class Team {
  private String projectId;
  private String[] imageURL;
  private String[] teamMembers;
  private String projectName, description, dateCreated;

  public Team(String projectId, String[] imageURL, String[] teamMembers, String projectName, String description, String dateCreated) {
    this.projectId = projectId;
    this.imageURL = imageURL;
    this.teamMembers = teamMembers;
    this.projectName = projectName;
    this.description = description;
    this.dateCreated = dateCreated;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public void setImageURL(String[] imageURL) {
    this.imageURL = imageURL;
  }

  public void setTeamMembers(String[] teamMembers) {
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

  public String getProjectId() {
    return projectId;
  }

  public String[] getImageURL() {
    return imageURL;
  }

  public String[] getTeamMembers() {
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
