package com.zhulie.zhulie;

public class Task {

  private String name, desc, fromdate, todate, prority, image;

  public Task(String name, String desc, String fromdate, String todate, String prority, String image) {
    this.name = name;
    this.desc = desc;
    this.fromdate = fromdate;
    this.todate = todate;
    this.prority = prority;
    this.image = image;
  }

  public Task(String name, String desc, String fromdate, String todate, String prority) {
    this.name = name;
    this.desc = desc;
    this.fromdate = fromdate;
    this.todate = todate;
    this.prority = prority;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getFromdate() {
    return fromdate;
  }

  public void setFromdate(String fromdate) {
    this.fromdate = fromdate;
  }

  public String getTodate() {
    return todate;
  }

  public void setTodate(String todate) {
    this.todate = todate;
  }

  public String getPrority() {
    return prority;
  }

  public void setPrority(String prority) {
    this.prority = prority;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
