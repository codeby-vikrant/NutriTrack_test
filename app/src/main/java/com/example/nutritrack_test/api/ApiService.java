package com.example.nutritrack_test.api;

import com.example.nutritrack_test.models.Food;
import com.example.nutritrack_test.models.MealPlan;
import com.example.nutritrack_test.models.User;
import com.example.nutritrack_test.models.MealPlanRequest;
import com.example.nutritrack_test.models.MealPlanResponse;
import com.example.nutritrack_test.models.FoodItem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/health")
    Call<Map<String, String>> checkHealth();
    
    @POST("/login")
    Call<Map<String, String>> login(@Body Map<String, String> credentials);
    
    @GET("/api/users")
    Call<List<User>> getUsers();
    
    @POST("/api/users")
    Call<User> createUser(@Body User user);
    
    @GET("/api/foods")
    Call<List<Food>> getFoods();
    
    @POST("/api/meal-plans")
    Call<MealPlan> createMealPlan(@Body Map<String, Object> request);

    @POST("/api/meal-plans")
    Call<MealPlan> generateMealPlan(@Body User user);

    @POST("/api/meal-plans")
    Call<MealPlanResponse> generateMealPlan(@Body MealPlanRequest request);

    @GET("api/foods")
    Call<List<FoodItem>> getAllFoods();
    
    @GET("api/foods/filter")
    Call<List<FoodItem>> getFilteredFoods(
        @Query("region") String region,
        @Query("diet") String dietaryPreference,
        @Query("mealType") String mealType
    );
    
    @GET("api/foods/search")
    Call<List<FoodItem>> searchFoods(@Query("query") String query);
}
