package com.example.admin.godzillaandroid.comm;

public class Constants {

    //public static String ip ="127.0.0.1";
    public static String ip ="www.minidou.top";
    public static String port ="8080";
    public static String ArticleTitleList ="http://"+ip+":"+port+"/article/getArticleTitleList";
    public static String ArticleById ="http://"+ip+":"+port+"/article/getArticle/";
    //根据用户id获取文章列表
    public static String findAllByuserId ="http://"+ip+":"+port+"/article/findAllByuserId/";

    //删除文章
    public static String deleteUserArticle= "http://"+ip+":"+port+"/article/deleteUserArticle/";
    //设置已读
    public static String modifyRead= "http://"+ip+":"+port+"/article/modifyRead/";
    //登录校验
    public static String UserByCountAndPassword ="http://"+ip+":"+port+"/users/getUserByCountAndPassword";


    //根据用户id获取notes列表
    public static String findNotesByUserId ="http://"+ip+":"+port+"/notes/findNotesByUserId/";

    //新增notes列表
    public static String addNotes ="http://"+ip+":"+port+"/notes/addNotes";

    //modifynotes列表
    public static String updateNote ="http://"+ip+":"+port+"/notes/updateNote";

    //modifynotes列表
    public static String deleteNote ="http://"+ip+":"+port+"/notes/deleteNotes/";

    //页面
    public static String BROWERURL = "http://www.minidou.top:8080/#/taskmain";

    public static String MUSICRURL = "http://www.minidou.top:8080/#/music";





}
