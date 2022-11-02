package ca.cmpt276.carbon.model;

public class AchievementLevel {
    private int min;
    private int max;
    private String name;

    public AchievementLevel(int min, int max, String name) {
        this.min = min;
        this.max = max;
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getName() {
        return name;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setName(String name) {
        this.name = name;
    }
}
