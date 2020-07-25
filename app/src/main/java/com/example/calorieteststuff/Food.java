package com.example.calorieteststuff;

public class Food {

    public String name;
    public double calories;
    private final int imageResource;

    public Food(String name, double calories, int imageResource) {
        this.name = name;
        this.calories = calories;
        this.imageResource = imageResource;
    }

    String getName() {
        return name;
    }

    /**
     * Gets the info about the Food.
     *
     * @return The info about the Food.
     */
    public double  getCalories() {
        return calories;
    }

    public int getImageResource() {
        return imageResource;
    }

}
