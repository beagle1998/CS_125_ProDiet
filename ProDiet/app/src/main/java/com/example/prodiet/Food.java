package com.example.prodiet;

public class Food {
    private String foodname;
    private String foodID;
    private Double calories;
    private Double carbohydrates;
    private Double fat;
    private Double protein;
    private Double servingSize;
//    private Integer servingSize;

    public Food() { }

    public Food(String foodname, String foodID, Double calories, Double carbohydrates,
                Double fat, Double protein, Double servingSize) {
        this.foodname = foodname;
        this.foodID = foodID;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.servingSize = servingSize;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
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
}
