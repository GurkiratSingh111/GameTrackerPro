package ca.cmpt276.carbon.model;

public class Achievements {
//    public static final String LVL_MAX = "Master Macadamia";
//    public static final String LVL_8 = "Amazing Almond";
//    public static final String LVL_7 = "Pretty Pecan";
//    public static final String LVL_6 = "Crazy CornNut";
//    public static final String LVL_5 = "Wacky Walnut";
//    public static final String LVL_4 = "Savvy Soynut";
//    public static final String LVL_3 = "Crafty Cashew";
//    public static final String LVL_2 = "Happy Hazelnut";
//    public static final String LVL_1 = "Playful Pistachio";
//    public static final String LVL_MIN = "Pleasant Peanut";

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
    private int numOfPlayers;
    private double deltaScore;
    public static final int NUM_OF_LVLS = 10;

    public Achievements(int low, int high) {
        this.lowScore = low;
        this.highScore = high;
        deltaScore = (high - low) / NUM_OF_LVLS - 1;
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

//    String getAchievementLvl(int score, int numOfPlayers) {
//        if (numOfPlayers <= 0) {
//            throw new RuntimeException("players must be a positive integers");
//        }
//        int scorePerPerson = score / numOfPlayers;
//
//        if (scorePerPerson > highScore) {
//            return LVL_MAX;
//        }
//        else if (scorePerPerson <= highScore && scorePerPerson > (highScore - deltaScore)) {
//            return LVL_8;
//        }
//        else if (scorePerPerson <= (highScore - deltaScore) && scorePerPerson > (highScore -
//                (2 * deltaScore))) {
//            return LVL_7;
//        }
//        else if (scorePerPerson <= (highScore - 2 * deltaScore) && scorePerPerson > (highScore -
//                (3 * deltaScore))){
//            return LVL_6;
//        }
//        else if (scorePerPerson <= (highScore - 3 * deltaScore) && scorePerPerson > (highScore -
//                (4 * deltaScore))) {
//            return LVL_5;
//        }
//        else if (scorePerPerson <= (highScore - 4 * deltaScore) && scorePerPerson > (highScore -
//                (5 * deltaScore))) {
//            return LVL_4;
//        }
//        else if (scorePerPerson <= (highScore - 5 * deltaScore) && scorePerPerson > (highScore -
//                (6 * deltaScore))) {
//            return LVL_3;
//        }
//        else if (scorePerPerson <= (highScore - 6 * deltaScore) && scorePerPerson > (highScore -
//                (7 * deltaScore))) {
//            return LVL_2;
//        }
//        else {
//            return LVL_MIN;
//        }
//    }

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

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
