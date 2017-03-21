package com.ben.yjh.babycare.http;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class CustomRequest extends JsonObjectRequest {

    private Map<String, String> mMap;

    public CustomRequest(int method, String url, JSONObject jsonRequest,
                         Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener, Map<String, String> mMap) {
        super(method, url, jsonRequest, listener, errorListener);
        this.mMap = mMap;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }



    //    private final Class<T> clazz;

//    public CustomRequest(int method, Class<T> clazz, String url, Map<String,
//            String> map, Response.ErrorListener listener) {
//        super(method, url, listener);
//        this.clazz = clazz;
//        this.mMap = map;
//    }
//
////    public CustomRequest(int method, String url, JSONObject jsonRequest,
////                         Map<String, String> listener,
////                         Response.Listener<JSONObject> errorListener, Response.ErrorListener map) {
////        super(method, url, jsonRequest, listener, errorListener);
////        this.mMap = map;
////    }
//
//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        Log.d("", "");
//        return mMap;
//    }
//
//    @Override
//    protected Response<T> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String json = new String(response.data,
//                    HttpHeaderParser.parseCharset(response.headers));
//            return Response.success(new Gson().fromJson(json, clazz),
//                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
////    @Override
////    protected void deliverResponse(JsonObject response) {
////        Log.d("deliverResponse: ", response.getAsString());
////    }
//
//    @Override
//    protected void deliverResponse(T response) {
//        Log.d("deliverResponse: ", response.toString());
//    }
}
