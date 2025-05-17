package com.example.nutritrack_test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nutritrack_test.R;
import com.example.nutritrack_test.models.MealCategory;
import com.example.nutritrack_test.models.FoodItem;
import com.google.android.material.chip.Chip;
import java.util.List;
import java.util.Locale;

public class MealCategoryAdapter extends RecyclerView.Adapter<MealCategoryAdapter.MealCategoryViewHolder> {
    private List<MealCategory> mealCategories;

    public MealCategoryAdapter(List<MealCategory> mealCategories) {
        this.mealCategories = mealCategories;
    }

    @NonNull
    @Override
    public MealCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_category, parent, false);
        return new MealCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealCategoryViewHolder holder, int position) {
        MealCategory category = mealCategories.get(position);
        holder.categoryTitle.setText(category.getCategoryName());
        
        // Set category icon based on category name
        switch (category.getCategoryName().toLowerCase()) {
            case "breakfast":
                holder.categoryIcon.setImageResource(R.drawable.ic_breakfast);
                break;
            case "lunch":
                holder.categoryIcon.setImageResource(R.drawable.ic_lunch);
                break;
            case "evening snacks":
                holder.categoryIcon.setImageResource(R.drawable.ic_snack);
                break;
            case "dinner":
                holder.categoryIcon.setImageResource(R.drawable.ic_dinner);
                break;
            default:
                holder.categoryIcon.setImageResource(R.drawable.ic_meal_plan);
                break;
        }
        
        // Clear existing food items
        holder.foodItemsContainer.removeAllViews();
        
        // Add food items
        for (FoodItem item : category.getFoodItems()) {
            View foodItemView = LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_food, holder.foodItemsContainer, false);
            
            TextView foodName = foodItemView.findViewById(R.id.foodName);
            TextView foodRegion = foodItemView.findViewById(R.id.foodRegion);
            TextView foodDescription = foodItemView.findViewById(R.id.foodDescription);
            TextView caloriesText = foodItemView.findViewById(R.id.caloriesText);
            TextView proteinText = foodItemView.findViewById(R.id.proteinText);
            TextView carbsText = foodItemView.findViewById(R.id.carbsText);
            TextView fatText = foodItemView.findViewById(R.id.fatText);
            CheckBox checkBox = foodItemView.findViewById(R.id.foodCheckbox);
            Chip dietTypeChip = foodItemView.findViewById(R.id.dietTypeChip);
            View colorIndicator = foodItemView.findViewById(R.id.foodItemColorIndicator);
            
            // Set color indicator based on diet type
            if (item.getDietType().equalsIgnoreCase("Vegetarian")) {
                colorIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorVegetarian));
            } else if (item.getDietType().equalsIgnoreCase("Vegan")) {
                colorIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorVegan));
            } else {
                colorIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
            }
            
            // Set food item data
            foodName.setText(item.getName());
            foodRegion.setText(item.getRegion());
            foodDescription.setText(item.getDescription());
            caloriesText.setText(String.format(Locale.getDefault(), "%.0f cal", item.getCalories()));
            proteinText.setText(String.format(Locale.getDefault(), "%.1fg", item.getProtein()));
            carbsText.setText(String.format(Locale.getDefault(), "%.1fg", item.getCarbs()));
            fatText.setText(String.format(Locale.getDefault(), "%.1fg", item.getFat()));
            
            // Set diet type chip
            dietTypeChip.setText(item.getDietType());
            
            checkBox.setChecked(item.isCompleted());
            
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setCompleted(isChecked);
            });
            
            holder.foodItemsContainer.addView(foodItemView);
        }

        // Toggle expand/collapse
        holder.expandIcon.setRotation(category.isExpanded() ? 180 : 0);
        holder.foodItemsContainer.setVisibility(category.isExpanded() ? View.VISIBLE : View.GONE);
        
        holder.headerLayout.setOnClickListener(v -> {
            category.setExpanded(!category.isExpanded());
            holder.expandIcon.setRotation(category.isExpanded() ? 180 : 0);
            holder.foodItemsContainer.setVisibility(category.isExpanded() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return mealCategories.size();
    }

    public void updateData(List<MealCategory> newCategories) {
        this.mealCategories = newCategories;
        notifyDataSetChanged();
    }

    static class MealCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        ImageView expandIcon;
        ImageView categoryIcon;
        LinearLayout foodItemsContainer;
        View headerLayout;

        MealCategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            expandIcon = itemView.findViewById(R.id.expandIcon);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            foodItemsContainer = itemView.findViewById(R.id.foodItemsContainer);
            headerLayout = itemView.findViewById(R.id.categoryHeader);
        }
    }
} 