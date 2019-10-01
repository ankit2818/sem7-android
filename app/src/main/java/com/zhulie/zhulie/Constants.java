package com.zhulie.zhulie;

public class Constants {

  private static String IP = "192.168.0.101:3000";
  static String BASEURL = "http://"+IP;

  static String REGISTERURL = BASEURL+"/user/register"; // http://IP:3000/user/register
  static String LOGINURL = BASEURL+"/user/login"; // http://IP:3000/user/login
  public static String CREATEPROJECT = BASEURL+"/project/create"; // http://IP:3000/project/create

}
