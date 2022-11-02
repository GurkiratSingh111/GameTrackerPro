package ca.cmpt276.carbon.model;

public class AchievementLevel {
    private double min;
    private double max;
    private String name;

    public AchievementLevel(double min, double max, String name) {
        this.min = min;
        this.max = max;
        this.name = name;
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
