package com.lenovo.smarttraffic.util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lenovo.smarttraffic.InitApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class VollryUtils {
    public static final String ObjUrl = "http://219.243.137.117:8888/api/v2/";

    public static void post(String url, Map<String, Object> map, Response.Listener<JSONObject> ok, Response.ErrorListener er) {
        try {
            JSONObject object = new JSONObject();
            if (map != null) {
                for (String key :
                        map.keySet()) {
                    object.put(key, map.get(key));
                }
            }
            if (ok == null) {
                ok = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                };
            }
            if (er == null) {
                er = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                };
            }
            InitApp.getRequestQueue().add(new JsonObjectRequest(Request.Method.POST, ObjUrl + url, object, ok, er));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
