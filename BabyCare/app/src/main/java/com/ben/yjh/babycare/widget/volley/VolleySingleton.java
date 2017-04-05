package com.ben.yjh.babycare.widget.volley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ben.yjh.babycare.application.MyApplication;

public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        CustomHurlStack customHurlStack = new CustomHurlStack();
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext(), customHurlStack);
    }

    public static VolleySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }
}