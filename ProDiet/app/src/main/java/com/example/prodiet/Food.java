package com.example.prodiet;

public class Food {
    private String foodName;
    private Double calories;
    private Double carbohydrates;
    private Double fat;
    private Double protein;
    private Double servingSize;

    public Food() { }

    public Food(String foodName, Double calories, Double carbohydrates,
                Double fat, Double protein, Double servingSize) {
        this.foodName = foodName;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.servingSize = servingSize;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getServingSize() {
        return servingSize;
    }

    public void setServingSize(Double servingSize) {
        this.servingSize = servingSize;
    }

    public String itemString() {
        return String.format("Calories:%.1f Protein:%.1f Carbohydrates:%.1f Fat:%.1f",
                this.calories, this.protein, this.carbohydrates, this.fat);
    }
}
