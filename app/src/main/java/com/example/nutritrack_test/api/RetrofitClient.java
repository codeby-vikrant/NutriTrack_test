package com.example.nutritrack_test.api;

import android.os.Build;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    // Use 10.0.2.2 for emulator, actual IP for real device
    private static final String EMULATOR_BASE_URL = "http://10.0.2.2:8000/";
    private static final String DEVICE_BASE_URL = "http://172.27.81.202:8000/"; // Updated IP address
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor for debugging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttpClient with logging and timeouts
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true) // Enable retries
                    .build();

            // Choose base URL based on whether running on emulator or real device
            String baseUrl = Build.FINGERPRINT.contains("generic") ? EMULATOR_BASE_URL : DEVICE_BASE_URL;

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
