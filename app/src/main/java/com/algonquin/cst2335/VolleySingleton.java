package com.algonquin.cst2335;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 * Singleton class for managing a single instance of Volley RequestQueue.
 */
public class VolleySingleton {
    /** The single instance of VolleySingleton. */
    @SuppressLint("StaticFieldLeak")
    private static VolleySingleton instance;
    /** The RequestQueue instance for making network requests. */
    private RequestQueue requestQueue;
    /** The application context. */
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;
    /**
     * Private constructor to prevent instantiation from outside.
     *
     * @param context The application context.
     */
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }
    /**
     * Returns the singleton instance of VolleySingleton.
     *
     * @param context The application context.
     * @return The singleton instance.
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }
    /**
     * Retrieves the RequestQueue instance.
     *
     * @return The RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }
    /**
     * Adds a request to the RequestQueue.
     *
     * @param req The request to add.
     * @param <T> The type of the request response.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
