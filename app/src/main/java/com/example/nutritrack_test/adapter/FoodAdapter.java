package com.example.nutritrack_test.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nutritrack_test.FoodDetailActivity;
import com.example.nutritrack_test.R;
import com.example.nutritrack_test.models.FoodItem;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodsList;
    private OnFoodItemClickListener listener;
    private Context context;

    public interface OnFoodItemClickListener {
        void onFoodItemClick(FoodItem foodItem, boolean isChecked);
    }

    public FoodAdapter(List<FoodItem> foodsList, OnFoodItemClickListener listener) {
        this.foodsList = foodsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodsList.get(position);
        holder.foodName.setText(food.getName());
        holder.foodRegion.setText(food.getRegion());
        holder.foodDescription.setText(food.getDescription());
        holder.dietTypeChip.setText(food.getDietType());
        
        // Set nutrition information in the individual text views
        holder.caloriesText.setText(String.format(Locale.getDefault(), "%.0f cal", food.getCalories()));
        holder.proteinText.setText(String.format(Locale.getDefault(), "%.1fg", food.getProtein()));
        holder.carbsText.setText(String.format(Locale.getDefault(), "%.1fg", food.getCarbs()));
        holder.fatText.setText(String.format(Locale.getDefault(), "%.1fg", food.getFat()));

        // Set checkbox state
        holder.foodCheckbox.setChecked(food.isCompleted());
        holder.foodCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            food.setCompleted(isChecked);
            if (listener != null) {
                listener.onFoodItemClick(food, isChecked);
            }
        });
        
        // Set click listener to open food details
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra(FoodDetailActivity.EXTRA_FOOD_ITEM, new Gson().toJson(food));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    public void updateFoods(List<FoodItem> newFoods) {
        this.foodsList = newFoods;
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodRegion;
        TextView foodDescription;
        TextView caloriesText;
        TextView proteinText;
        TextView carbsText;
        TextView fatText;
        Chip dietTypeChip;
        CheckBox foodCheckbox;
        CardView cardView;

        FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodRegion = itemView.findViewById(R.id.foodRegion);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            caloriesText = itemView.findViewById(R.id.caloriesText);
            proteinText = itemView.findViewById(R.id.proteinText);
            carbsText = itemView.findViewById(R.id.carbsText);
            fatText = itemView.findViewById(R.id.fatText);
            dietTypeChip = itemView.findViewById(R.id.dietTypeChip);
            foodCheckbox = itemView.findViewById(R.id.foodCheckbox);
            cardView = (CardView) itemView;
        }
    }
} 