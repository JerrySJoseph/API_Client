package com.jstechnologies.androidpostman.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppRequestQueue {
    private static AppRequestQueue mInstance;
    private RequestQueue requestQueue;
    private static Context mcontext;

    AppRequestQueue(Context context)
    {
        mcontext=context;
        this.requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(mcontext.getApplicationContext());
        return requestQueue;
    }
    public static synchronized AppRequestQueue getInstance(Context context)
    {
        if(mInstance==null)
            mInstance=new AppRequestQueue(context);
        return mInstance;
    }
    public<T> void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }

}
