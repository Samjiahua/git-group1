package com.lenovo.smarttraffic.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lenovo.smarttraffic.InitApp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static void sp_put(String k, Object v) {
        SharedPreferences sp = InitApp.getContext().getSharedPreferences("NEWKJ", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (v instanceof String) {
            edit.putString(k, (String) v);
        } else if (v instanceof Boolean) {
            edit.putBoolean(k, (Boolean) v);
        } else if (v instanceof Integer) {
            edit.putInt(k, (Integer) v);
        } else if (v instanceof Float) {
            edit.putFloat(k, (Float) v);
        } else if (v instanceof Long) {
            edit.putLong(k, (Long) v);
        } else {
            return;
        }
        edit.commit();
    }

    public static Object sp_get(String k, Object v) {
        SharedPreferences sp = InitApp.getContext().getSharedPreferences("NEWKJ", Context.MODE_PRIVATE);
        if (v instanceof String) {
            return sp.getString(k, (String) v);
        } else if (v instanceof Boolean) {
            return sp.getBoolean(k, (Boolean) v);
        } else if (v instanceof Integer) {
            return sp.getInt(k, (Integer) v);
        } else if (v instanceof Float) {
            return sp.getFloat(k, (Float) v);
        } else if (v instanceof Long) {
            return sp.getLong(k, (Long) v);
        } else {
            return null;
        }
    }

    /*

    Map map = new HashMap();
        map.put("UserName","user1");
        VollryUtils.post("", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("jso = ",jsonObject.toString());
                try {
                    if ("F".equals(jsonObject.getString("RESULT"))){
                        Toast.makeText(InitApp.getContext(), "网络信息！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(InitApp.getContext(), "网络不通！", Toast.LENGTH_SHORT).show();
                return;
            }
        });

     */

    public static String getWeekTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String format = simpleDateFormat.format(date);


        return format+"\n" +"星期四";
    }

    public static String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh.mm.ss");
        String format = simpleDateFormat.format(date);

        return format;
    }
}
