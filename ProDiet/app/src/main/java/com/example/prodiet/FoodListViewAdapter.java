package com.example.prodiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FoodListViewAdapter extends ArrayAdapter<Food> {
    private ArrayList<Food> foods;

    public FoodListViewAdapter(ArrayList<Food> foods, Context context) {
        super(context, R.layout.food_row, foods);
        this.foods = foods;
    }

    /**
     * Return food item view by list view index
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.food_row, parent, false);

        Food food_item = foods.get(position);

        TextView food_row_foodname = view.findViewById(R.id.food_row_foodname);
        TextView food_row_calories = view.findViewById(R.id.food_row_calories);

        food_row_foodname.setText(food_item.getFoodName());
        food_row_calories.setText(food_item.itemString());

        return view;
    }
}