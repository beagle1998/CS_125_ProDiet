package com.example.prodiet;

import java.util.Calendar;

public class MatchingUtils {

    /**
     * Return BMI (Body Mass Index) based on height and weight
     */
    public static double calculateBMI(double height, double weight) {
        return (weight / (height * height)) * 10000;
    }

    /**
     * Return weight status based on height and weight
     */
    public static String weightStatus(double height, double weight) {
        double bmi = calculateBMI(height, weight);
        if (bmi < 18.5){
            return "underweight";
        }
        else if (bmi >= 18.5 && bmi <= 24.9) {
            return "normal";
        }
        else if (bmi > 24.9 && bmi <= 29.9) {
            return "overweight";
        }
        else if (bmi >29) {
            return "obese";
        } else {
            return "weight status error";
        }
    }

    /**
     * Return idea weight based on gender and height
     */
    public static double idealWeight(String gender, double height) {
        // Calculated using Devine Equation
        if (gender.equals("male")) {
            return 50 + 0.9 * (height - 152);
        }
        else if (gender.equals("female")) {
            return 45.5 + 0.9 * (height - 152);
        }
        else {
            return 0;
        }
        //ALTERNATIVE IDEAL WEIGHT FORMULA(LORENTZ)
        /**
         *         if (gender.equals("male")) {
         *             return (height - 100) - ((height - 150)/4);
         *         }
         *         else if (gender.equals("female")) {
         *             return (height - 100) - ((height - 150)/2);
         *         }
         *         else {
         *             return 0;
         *         }**/
    }

    /**
     * Return BMR (Basal Metabolic Rate) based on gender, height, weight, and age
     */
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

    /**
     * Return activity level based on walking steps
     */
    static String activity(int steps){
        if (steps <= 5000){
            return "sedentary";
        }
        else if (steps > 5000 && steps <= 7500){
            return "lightly active";
        }
        else if (steps > 7500 && steps <= 10000){
            return "moderately active";
        }
        else if (steps > 10000 && steps <= 12500){
            return "very active";
        }
        else if (steps > 12500){
            return "extra active";
        }
        return "Error in Activity";
    }

    /**
     * Return calories needed to maintain the same weight based on BMR (Basal Metabolic Rate) and activity level
     */
    public static double normalCalorie(double bmr, String activity) {
        // Calories needed to maintain same weight
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

    /**
     * Return recommended calories per day to reach ideal weight using recommended weight loss rate of one lb per week,
     * based on gender, height, weight, age, and walking steps
     */
    public static double actualCalorie(String gender, double height, double weight, int age, Integer steps) {
        // Recommended calories per day to reach ideal weight using recommended weight loss rate of one lb per week or 3500 calories per week
        // 3500 calories per lb but 7700 calories per kg
        int weightLossRate = 3500;
        /*
        If you want to determine how long a diet at such rate will take:
        int totalCalorieDeficit = (weight - idealWeight(gender, height)) * 7700;
        dietLength = totalCalorieDeficit/weightLossRate;
        */
        int weightChangeRate = 0;
        double weightDiff = weight - idealWeight(gender, height);
        if (weightDiff > 0){
            // overweight
            weightChangeRate = -3500;
        } else if (weightDiff == 0) {
            // ideal weight
            weightChangeRate = 0;
        } else {
            // underweight
            weightChangeRate = 3500;
        }
        if (steps == null){
            return normalCalorie(calculateBMR(gender, height, weight, age), "lightly active") + (weightChangeRate/7);
        } else{
            return normalCalorie(calculateBMR(gender, height, weight, age), activity(steps)) + (weightChangeRate/7);
        }
    }

    /**
     * Return recommended calories based on user's personal information, activity level (steps), and current time
     */
    public static double caloriePerMeal(String gender, double height, double weight, int birthyear, Integer steps) {
        Calendar now = Calendar.getInstance();

        int currYear = now.get(Calendar.YEAR);
        int age = currYear - birthyear;
        double mealFactor = 0;
        int currHour = now.get(Calendar.HOUR_OF_DAY);
        System.out.println("Time: " + currHour);
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
        return mealFactor * actualCalorie(gender, height, weight, age, steps);
    }

    /**
     * Return meal type based on current time
     */
    public static String mealType() {
        int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (currHour >= 0 && currHour < 12){
            return "Breakfast";
        } else if (currHour >= 12 && currHour < 16){
            return "Lunch";
        } else {
            return "Dinner";
        }
    }

    /**
     * Return food category path
     */
    public static String categoryPath(boolean vegan) {
        String vegan_string = vegan ? "Vegan" : "Nonvegan";
        return "Recipes/" + vegan_string + "/" + mealType();
    }

    /**
     * Pseudo main function for testing
     */
    public static void pseudoMain() {
        double weight = 72;
        double height = 178;
        String gender = "male";
        int birthyear = 2003;
        Integer steps = 5000;

        System.out.println("Weight Status: " + weightStatus(height, weight));
        System.out.println("Calories next meal: " + caloriePerMeal(gender, height, weight, birthyear, steps));

        /*
        Notes:
        1) Age will be needed.
        2) If you do not plan to take in desired weight, I will use ideal weight as calculated by the Devine formula.
        3) Activity level is assumed to be lightly active, but I can change this, if we want the user to input activity level.
        4) Weight loss rate is assumed to 3500 calories per week.
        */
    }
}
