package ca.cmpt276.carbon.model;

public class Achievements {
    public static final String LVL_MAX = "Master Macadamia";
    public static final String LVL_8 = "Amazing Almond";
    public static final String LVL_7 = "Pretty Pecan";
    public static final String LVL_6 = "Crazy CornNut";
    public static final String LVL_5 = "Wacky Walnut";
    public static final String LVL_4 = "Savvy Soynut";
    public static final String LVL_3 = "Crafty Cashew";
    public static final String LVL_2 = "Happy Hazelnut";
    public static final String LVL_1 = "Playful Pistachio";
    public static final String LVL_MIN = "Pleasant Peanut";
    private int lowScore;
    private int highScore;
    private int numOfPlayers;
    private double deltaScore;
    public static final int NUM_OF_LVLS = 10;

    Achievements(int low, int high, int players) {
        if (players <= 0) {
            throw new RuntimeException("players must be a positive integers");
        }
        this.lowScore = low;
        this.highScore = high;
        this.numOfPlayers = players;
        deltaScore = (high - low) / NUM_OF_LVLS - 1;
    }

    String getAchievementLvl(int score) {
        int scorePerPerson = score / numOfPlayers;

        if (scorePerPerson > highScore) {
            return LVL_MAX;
        }
        else if (scorePerPerson <= highScore && scorePerPerson > (highScore - deltaScore)) {
            return LVL_8;
        }
        else if (scorePerPerson <= (highScore - deltaScore) && scorePerPerson > (highScore -
                (2 * deltaScore))) {
            return LVL_7;
        }
        else if (scorePerPerson <= (highScore - 2 * deltaScore) && scorePerPerson > (highScore -
                (3 * deltaScore))){
            return LVL_6;
        }
        else if (scorePerPerson <= (highScore - 3 * deltaScore) && scorePerPerson > (highScore -
                (4 * deltaScore))) {
            return LVL_5;
        }
        else if (scorePerPerson <= (highScore - 4 * deltaScore) && scorePerPerson > (highScore -
                (5 * deltaScore))) {
            return LVL_4;
        }
        else if (scorePerPerson <= (highScore - 5 * deltaScore) && scorePerPerson > (highScore -
                (6 * deltaScore))) {
            return LVL_3;
        }
        else if (scorePerPerson <= (highScore - 6 * deltaScore) && scorePerPerson > (highScore -
                (7 * deltaScore))) {
            return LVL_2;
        }
        else {
            return LVL_MIN;
        }
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

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
