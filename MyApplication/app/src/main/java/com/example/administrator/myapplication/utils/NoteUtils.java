package com.example.administrator.myapplication.utils;

import com.example.administrator.myapplication.ServiceUtil;
import com.example.administrator.myapplication.commom.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2018/7/10.
 */
public class NoteUtils {

    public static String addNote(String content, long userId) {
        String result = "";
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("note", content);
            jsonObj.put("content", content);
            jsonObj.put("userId", userId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String timestamp = formatter.format(curDate);
            jsonObj.put("date", timestamp);
            ServiceUtil serviceUtil = new ServiceUtil();
            result = serviceUtil.getServiceInfoPost(Constants.addNotes, jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            result = "failed";
        }
        return result;

    }


    public static String modifyNote(String content, int id) {
        String result = "";
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("note", content);
            jsonObj.put("content", content);
            jsonObj.put("id", id);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String timestamp = formatter.format(curDate);
            jsonObj.put("date", timestamp);
            ServiceUtil serviceUtil = new ServiceUtil();
            result = serviceUtil.getServiceInfoPost(Constants.updateNote, jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            result = "failed";
        }
        return result;

    }


    public static String deleteNote(String content, Long noteId) {
        String result = "";
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", noteId);
            ServiceUtil serviceUtil = new ServiceUtil();
            result = serviceUtil.getServiceInfoPost(Constants.deleteNote + noteId, jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            result = "failed";
        }
        return result;

    }
}
