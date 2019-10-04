package com.zhulie.zhulie;

public class Member {

  private String imageUri;
  private String name;

  public Member(String imageUri, String name) {
    this.imageUri = imageUri;
    this.name = name;
  }

  public void setImageUri(String imageUri) {
    this.imageUri = imageUri;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageUri() {
    return imageUri;
  }

  public String getName() {
    return name;
  }
}
