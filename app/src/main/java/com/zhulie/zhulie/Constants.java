package com.zhulie.zhulie;

class Constants {

  private static String IP = "192.168.43.2:2000";
  static String BASEURL = "http://" + IP;

  static String REGISTERURL = BASEURL + "/api/user/register"; // http://IP:3000/user/register
  static String LOGINURL = BASEURL + "/api/user/login"; // http://IP:3000/user/login
  static String GETPROJECT = BASEURL + "/api/project/myprojects"; // http://IP:3000/project/myprojects
  static String CREATEPROJECT = BASEURL + "/api/project/create"; // http://IP:3000/project/create
  static String GETTASK = BASEURL + "/api/project/getTask"; // http://IP:3000/project/getTask
  static String ADDTASK = BASEURL + "/api/project/addTask"; // http://IP:3000/project/addTask

}
