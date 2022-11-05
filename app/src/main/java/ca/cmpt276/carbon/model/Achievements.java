package ca.cmpt276.carbon.model;

public class Achievements {
    private AchievementLevel LVL_MAX;
    private AchievementLevel LVL_8;
    private AchievementLevel LVL_7;
    private AchievementLevel LVL_6;
    private AchievementLevel LVL_5;
    private AchievementLevel LVL_4;
    private AchievementLevel LVL_3;
    private AchievementLevel LVL_2;
    private AchievementLevel LVL_1;
    private AchievementLevel LVL_MIN;

    public static final double INF = -1;
    public static final double NEG_INF = -2;
    private int lowScore;
    private int highScore;
    private double deltaScore;
    public static final int NUM_OF_LVLS = 10;

    public Achievements(int low, int high) {
        this.lowScore = low;
        this.highScore = high;
        deltaScore = (high - low) / (NUM_OF_LVLS - 1);
        initializeAchievements();
    }

    void initializeAchievements() {
        LVL_MAX = new AchievementLevel(highScore, INF, "Master Macadamia");
        LVL_8 = new AchievementLevel(highScore - deltaScore, highScore, "Amazing Almond");
        LVL_7 = new AchievementLevel(highScore - 2 * deltaScore, highScore - deltaScore, "Pretty Pecan");
        LVL_6 = new AchievementLevel(highScore - 3 * deltaScore, highScore - 2 * deltaScore, "Crazy CornNut");
        LVL_5 = new AchievementLevel(highScore - 4 * deltaScore, highScore - 3 * deltaScore, "Wacky Walnut");
        LVL_4 = new AchievementLevel(highScore - 5 * deltaScore, highScore - 4 * deltaScore, "Savvy SoyNut");
        LVL_3 = new AchievementLevel(highScore - 6 * deltaScore, highScore - 5 * deltaScore, "Crafty Cashew");
        LVL_2 = new AchievementLevel(highScore - 7 * deltaScore, highScore - 6 * deltaScore, "Happy Hazelnut");
        LVL_1 = new AchievementLevel(lowScore, highScore - 7 * deltaScore, "Playful Pistachio");
        LVL_MIN = new AchievementLevel(NEG_INF, lowScore, "Pleasant Peanut");
    }

    // Returns AchievementLevel object from score and number of players
    // PRE: players must be greater than 0


    public AchievementLevel getLevel(String level) {
        if (level.equals("MAX")) {
            return LVL_MAX;
        }
        else if (level.equals("1")) {
            return LVL_1;
        }
        else if (level.equals("2")) {
            return LVL_2;
        }
        else if (level.equals("3")) {
            return LVL_3;
        }
        else if (level.equals("4")) {
            return LVL_4;
        }
        else if (level.equals("5")) {
            return LVL_5;
        }
        else if (level.equals("6")) {
            return LVL_6;
        }
        else if (level.equals("7")) {
            return LVL_7;
        }
        else if (level.equals("8")) {
            return LVL_8;
        }
        else if (level.equals("MIN")) {
            return LVL_MIN;
        }
        else {
            throw new RuntimeException("not valid level");
        }
    }

    public double getDeltaScore() {
        return deltaScore;
    }

    public int getLowScore() {
        return lowScore;
    }

    public void setLowScore(int lowScore) {
        this.lowScore = lowScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }


}
