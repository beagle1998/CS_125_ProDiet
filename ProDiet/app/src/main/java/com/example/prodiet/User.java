package com.example.prodiet;

public class User {

    private static String username;
    private static String password;
    private static String gender;
    private static Double height;
    private static Double weight;
    private static Integer birthyear;
    private static Boolean vegan;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        User.gender = gender;
    }

    public static Double getHeight() {
        return height;
    }

    public static void setHeight(Double height) {
        User.height = height;
    }

    public static Double getWeight() {
        return weight;
    }

    public static void setWeight(Double weight) {
        User.weight = weight;
    }

    public static Integer getBirthyear() {
        return birthyear;
    }

    public static void setBirthyear(Integer birthyear) {
        User.birthyear = birthyear;
    }

    public static Boolean getVegan() {
        return vegan;
    }

    public static void setVegan(Boolean vegan) {
        User.vegan = vegan;
    }

    /**
     * Clear all user information
     */
    public static void clear() {
        User.username = null;
        User.password = null;
        User.gender = null;
        User.height = null;
        User.weight = null;
        User.birthyear = null;
        User.vegan = null;
    }

    /**
     * Return true if there is no user information available, otherwise return false
     */
    public static boolean isEmpty() {
        return User.username == null && User.password == null;
    }

    /**
     * Return partial user information as string (without password)
     */
    public static String profileString() {
        return String.format("Username: %s\nGender: %s\nHeight: %.1fcm & Weight: %.1fkg\nBirthyear: %d\nVegan: %b",
                User.username, User.gender, User.height, User.weight, User.birthyear, User.vegan);
    }

    /**
     * Return partial user information as string (without username, password, vegan)
     */
    public static String assessmentString() {
        return String.format("Gender: %s\nHeight: %.1fcm\nWeight: %.1fkg\nBirthyear: %d",
                User.gender, User.height, User.weight, User.birthyear);
    }

}
