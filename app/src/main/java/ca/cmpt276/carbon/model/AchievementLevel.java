package ca.cmpt276.carbon.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * AchievementLevel class consists of min (Minimum Score), max (Maximum Score)
 * and name.It encapsulates getters and setters for all the three member
 * variables and a constructor
 */

public class AchievementLevel implements Serializable {
    private double min;
    private double max;
    private String name;
    private int id;
    @DrawableRes
    private int image;

    public AchievementLevel(double min, double max, String name, @DrawableRes int image, int id) {
        this.min = min;
        this.max = max;
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DrawableRes
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String getName() {
        return name;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setName(String name) {
        this.name = name;
    }
}
