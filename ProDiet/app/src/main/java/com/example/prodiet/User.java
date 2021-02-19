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

    public static void clear() {
        User.username = null;
        User.password = null;
        User.gender = null;
        User.height = null;
        User.weight = null;
        User.birthyear = null;
        User.vegan = null;
    }

    public static boolean isEmpty() {
        return User.username == null && User.password == null;
    }

    public static String profileString() {
        return "username: " + User.username
                + "\ngender: " + User.gender
                + "\nheight: " + User.height
                + "\nweight: " + User.weight
                + "\nbirthyear: " + User.birthyear
                + "\nvegan: " + User.vegan;
    }

    public static String assessmentString() {
        return "gender: " + User.gender
                + "\nheight: " + User.height
                + "\nweight: " + User.weight
                + "\nbirthyear: " + User.birthyear;
    }

}
