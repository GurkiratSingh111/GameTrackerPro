package ca.cmpt276.carbon.model;


/**
 *Achievement class consists of private attributes AchievementLevel
 * which defines each Achievement Level. It also has other attributes lowScore,
 * highScore, deltaScore,intializeAchievements which intializes all the
 * AchievementLevels and getAchievement function which returns the AchievementLevel

 */
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

    // Constructor
    public Achievements(int low, int high, double factor) {
        this.lowScore = low;
        this.highScore = high;
        deltaScore = ((double)high - low) / (NUM_OF_LVLS - 1);
        initializeAchievements(factor);
    }
    //factor is according to the difficulty level - "1" for normal, "0.75" for easy and "1.25" for hard
    void initializeAchievements(double factor) {
        LVL_MAX = new AchievementLevel(factor*(highScore), INF, "Master Macadamia");
        LVL_8 = new AchievementLevel(factor*(highScore - deltaScore),factor* highScore, "Amazing Almond");
        LVL_7 = new AchievementLevel(factor*(highScore - 2 * deltaScore),factor* (highScore - deltaScore), "Pretty Pecan");
        LVL_6 = new AchievementLevel(factor*(highScore - 3 * deltaScore),factor*(highScore - 2 * deltaScore), "Crazy CornNut");
        LVL_5 = new AchievementLevel(factor*(highScore - 4 * deltaScore),factor*(highScore - 3 * deltaScore), "Wacky Walnut");
        LVL_4 = new AchievementLevel(factor*(highScore - 5 * deltaScore),factor*(highScore - 4 * deltaScore), "Savvy SoyNut");
        LVL_3 = new AchievementLevel(factor*(highScore - 6 * deltaScore),factor*(highScore - 5 * deltaScore), "Crafty Cashew");
        LVL_2 = new AchievementLevel(factor*(highScore - 7 * deltaScore), factor*(highScore - 6 * deltaScore), "Happy Hazelnut");
        LVL_1 = new AchievementLevel(factor*(lowScore), factor*(highScore - 7 * deltaScore), "Playful Pistachio");
        LVL_MIN = new AchievementLevel(NEG_INF,factor*lowScore, "Pleasant Peanut");
    }

    // Returns AchievementLevel object from score and number of players
    // PRE: players must be greater than 0
    public AchievementLevel getAchievement(double score, int numOfPlayers) {
        if (numOfPlayers <= 0) {
            throw new RuntimeException("Players must be a positive integer");
        }
        double scorePerPlayer = (double)score / numOfPlayers;
        if (scorePerPlayer > LVL_8.getMax()) {
            return LVL_MAX;
        }
        else if (scorePerPlayer >=  LVL_8.getMin() && scorePerPlayer <=  LVL_8.getMax()) {
            return LVL_8;
        }
        else if (scorePerPlayer >= LVL_7.getMin() && scorePerPlayer <  LVL_8.getMin()) {
            return LVL_7;
        }
        else if (scorePerPlayer >=  LVL_6.getMin() && scorePerPlayer <  LVL_7.getMin()) {
            return LVL_6;
        }
        else if (scorePerPlayer >=  LVL_5.getMin() && scorePerPlayer < LVL_6.getMin()) {
            return LVL_5;
        }
        else if (scorePerPlayer >=  LVL_4.getMin() && scorePerPlayer <  LVL_5.getMin()) {
            return LVL_4;
        }
        else if (scorePerPlayer >=  LVL_3.getMin() && scorePerPlayer <  LVL_4.getMin()) {
            return LVL_3;
        }
        else if (scorePerPlayer >=  LVL_2.getMin() && scorePerPlayer <  LVL_3.getMin()) {
            return LVL_2;
        }
        else if (scorePerPlayer >=  LVL_1.getMin() && scorePerPlayer < LVL_2.getMin()) {
            return LVL_1;
        }
        else if (scorePerPlayer < LVL_1.getMin()) {
            return LVL_MIN;
        }
        else {
            throw new RuntimeException("Invalid level");
        }
    }

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

    public int getHighScore() {
        return highScore;
    }

    public int getLowScore() {
        return lowScore;
    }

    public double getDeltaScore() {
        return deltaScore;
    }

    public double returnLowScore(double factor) {

        return factor*lowScore;
    }

    public void setLowScore(int lowScore) {
        this.lowScore = lowScore;
    }

    public double returnHighScore(double factor) {
        return factor*highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
