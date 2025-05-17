package com.example.nutritrack_test.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONArray;

public class NetworkManager {
    private static NetworkManager instance;
    private RequestQueue requestQueue;
    private static Context context;
    private static final String TAG = "NetworkManager";

    // Choose the appropriate URL based on your environment:

    // For physical device on same network as your computer:
    private static final String BASE_URL = "http://192.168.0.103:8090/api"; // Updated IP address

    // For emulator:
    // private static final String BASE_URL = "http://10.0.2.2:8090/api"; // Use
    // this for emulator

    // Other options (commented out):
    // private static final String BASE_URL = "http://127.0.0.1:8090/api"; // For
    // local testing on same device
    // private static final String BASE_URL = "http://192.168.157.185:8090/api"; //
    // Original IP address

    private NetworkManager(Context context) {
        NetworkManager.context = context;
        requestQueue = getRequestQueue();
        Log.d(TAG, "NetworkManager initialized with BASE_URL: " + BASE_URL);
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // Create a custom HttpStack for Android 13+ compatibility
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

            // Set default retry policy for all requests
            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                    Log.d(TAG, "Request finished: " + request.getUrl());
                }
            });
        }
        return requestQueue;
    }

    /**
     * Checks if the network is available
     * 
     * @return true if network is available, false otherwise
     */
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.net.Network network = connectivityManager.getActiveNetwork();
                if (network == null)
                    return false;

                android.net.NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null
                        && (capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                // For older devices
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking network availability", e);
            return false;
        }
    }

    /**
     * Checks if the server is reachable
     * 
     * @param listener Callback for server status
     */
    public void checkServerConnection(final ServerConnectionListener listener) {
        if (!isNetworkAvailable()) {
            if (listener != null) {
                listener.onConnectionResult(false, "No network connection");
            }
            return;
        }

        String url = BASE_URL + "/health";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Server connection successful");
                    if (listener != null) {
                        listener.onConnectionResult(true, "Server connection successful");
                    }
                },
                error -> {
                    Log.e(TAG, "Server connection failed: " + error.toString());
                    if (listener != null) {
                        String errorMessage = "Server connection failed";
                        if (error.networkResponse != null) {
                            errorMessage += " - Status Code: " + error.networkResponse.statusCode;
                        }
                        listener.onConnectionResult(false, errorMessage);
                    }
                });

        // Set shorter timeout for health check
        request.setShouldRetryServerErrors(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                5000, // 5 seconds timeout
                0, // no retries
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(request);
    }

    /**
     * Interface for server connection status callback
     */
    public interface ServerConnectionListener {
        void onConnectionResult(boolean isConnected, String message);
    }

    public void getAllFoods(Response.Listener<JSONArray> successListener,
            Response.ErrorListener errorListener) {
        // Check network before making request
        if (!isNetworkAvailable()) {
            errorListener
                    .onErrorResponse(new com.android.volley.NoConnectionError(new Exception("No network connection")));
            return;
        }

        String url = BASE_URL + "/foods";
        Log.d(TAG, "Making request to: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "getAllFoods success, received " + response.length() + " items");
                    successListener.onResponse(response);
                },
                error -> {
                    Log.e(TAG, "getAllFoods error: " + error.toString(), error);
                    errorListener.onErrorResponse(error);
                });

        // Add timeout settings - 10 seconds
        request.setShouldRetryServerErrors(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, // 10 seconds timeout
                1, // 1 retry
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(request);
    }

    public void searchFoods(String query, Response.Listener<JSONArray> successListener,
            Response.ErrorListener errorListener) {
        String url = BASE_URL + "/foods/search?query=" + query;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                successListener, errorListener);
        getRequestQueue().add(request);
    }

    public void getFilteredFoods(String region, String diet, String mealType,
            Response.Listener<JSONArray> successListener,
            Response.ErrorListener errorListener) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/foods/filter?");
        if (region != null)
            urlBuilder.append("region=").append(region).append("&");
        if (diet != null)
            urlBuilder.append("diet=").append(diet).append("&");
        if (mealType != null)
            urlBuilder.append("mealType=").append(mealType);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBuilder.toString(), null,
                successListener, errorListener);
        getRequestQueue().add(request);
    }

    public void generateMealPlan(JSONObject requestData, Response.Listener<JSONObject> successListener,
            Response.ErrorListener errorListener) {
        String url = BASE_URL + "/generate_meal_plan";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                successListener, errorListener);
        getRequestQueue().add(request);
    }

    public void getUserProfile(String userId, Response.Listener<JSONObject> successListener,
            Response.ErrorListener errorListener) {
        String url = BASE_URL + "/user/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                successListener, errorListener);
        getRequestQueue().add(request);
    }

    public void updateUserProfile(String userId, JSONObject userData, Response.Listener<JSONObject> successListener,
            Response.ErrorListener errorListener) {
        String url = BASE_URL + "/user/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, userData,
                successListener, errorListener);
        getRequestQueue().add(request);
    }

    public void generateDietPlan(String dietType, int targetCalories,
            Response.Listener<JSONArray> successListener,
            Response.ErrorListener errorListener) {
        String url = BASE_URL + "/generate-diet-plan" +
                "?diet_type=" + dietType +
                "&target_calories=" + targetCalories;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                successListener,
                errorListener);
        getRequestQueue().add(request);
    }
}