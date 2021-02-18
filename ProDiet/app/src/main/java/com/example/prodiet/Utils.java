package com.example.prodiet;

import java.util.Calendar;

public class Utils {

    public static double calculateBMI(double height, double weight) {
        return (weight / (height * height)) * 10000;
    }

    public static String weightStatus(double height, double weight) {
        double bmi = calculateBMI(height, weight);
        if (bmi < 18.5){
            return "underweight";
        }
        else if (bmi >= 18.5 && bmi <= 24.9) {
            return "normal";
        }
        else if (bmi >= 25 && bmi <= 29.9) {
            return "overweight";
        }
        else if (bmi >= 30) {
            return "obese";
        } else {
            return "weight status error";
        }
    }

    public static double idealWeight(String gender, double height) {
        // Calculated using Devine Equation
        if (gender.equals("female")) {
            return 50 + 0.9 * (height - 152);
        }
        else if (gender.equals("male")) {
            return 45.5 + 0.9 * (height - 152);
        }
        else {
            return 0;
        }
    }


    public static double calculateBMR(String gender, double height, double weight, int age) {
        // Calculated using Harris-Benedict 1990 Equation
        // Basal Metabolic Rate -> used to calculate calorie per day
        if (gender.equals("female")) {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
        else if (gender.equals("male")){
            return 10 * weight + 6.25 * height - 5 * age + 5;
        }
        else {
            return 0;
        }
    }


    public static double normalCalorie(double bmr) {
        // Calories needed to maintain same weight
        String activity = "lightly active";
        double activityFactor = 0;
        if (activity.equals("sedentary")) {
            activityFactor = 1.2;
        }
        else if (activity.equals("lightly active")) {
            activityFactor = 1.375;
        }
        else if (activity.equals("moderately active")) {
            activityFactor = 1.55;
        }
        else if (activity.equals("very active")) {
            activityFactor = 1.725;
        }
        else if (activity.equals("extra active")) {
            activityFactor = 1.9;
        }
        return bmr * activityFactor;
    }


    public static double actualCalorie(String gender, double height, double weight, int age) {
        // Recommended calories per day to reach ideal weight using recommended weight loss rate of one lb per week or 3500 calories per week
        // 3500 calories per lb but 7700 calories per kg
        int weightLossRate = 3500;
        /*
        If you want to determine how long a diet at such rate will take:
        int totalCalorieDeficit = (weight - idealWeight(gender, height)) * 7700;
        dietLength = totalCalorieDeficit/weightLossRate;
        */
        return normalCalorie(calculateBMR(gender, height, weight, age)) - (weightLossRate / 7);
    }


    public static double caloriePerMeal(String gender, double height, double weight, int birthyear) {
        Calendar now = Calendar.getInstance();

        int currYear = now.get(Calendar.YEAR);
        int age = currYear - birthyear;
        double mealFactor = 0;
        int currHour = now.get(Calendar.HOUR_OF_DAY);

        if (currHour >= 16 && currHour <= 24){
            mealFactor = 0.3;
        }
        else if (currHour >= 12 && currHour < 16){
            mealFactor = 0.3;
        }
        else if (currHour >= 0 && currHour < 12){
            mealFactor = 0.4;
        }
        else {
            mealFactor = 0;
        }
        return mealFactor * actualCalorie(gender, height, weight, age);
    }

    public static void pseudoMain() {
        double weight = 72;
        double height = 178;
        String gender = "male";
        int birthyear = 2003;

        System.out.println(weightStatus(height, weight));
        System.out.println(caloriePerMeal(gender, height, weight, birthyear));

        /*
        Notes:
        1) Age will be needed.
        2) If you do not plan to take in desired weight, I will use ideal weight as calculated by the Devine formula.
        3) Activity level is assumed to be lightly active, but I can change this, if we want the user to input activity level.
        4) Weight loss rate is assumed to 3500 calories per week.
        */
    }
}
