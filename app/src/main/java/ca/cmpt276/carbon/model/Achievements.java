package ca.cmpt276.carbon.model;



import java.io.Serializable;
import ca.cmpt276.carbon.R;


/**
 *Achievement class consists of private attributes AchievementLevel
 * which defines each Achievement Level. It also has other attributes lowScore,
 * highScore, deltaScore, intializeAchievements which intializes all the
 * AchievementLevels and getAchievement function which returns the AchievementLevel
 */

public class Achievements implements Serializable {
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
    public static final String NUT = "NUT";
    public static final String EMOJI = "EMOJI";
    public static final String NONE = "NONE";
    public static final String MIDDLE_EARTH = "MIDDLE_EARTH";
    private String theme = NONE;
    double factor;

    // Constructor
    public Achievements(int low, int high, double factor) {
        this.lowScore = low;
        this.highScore = high;
        this.factor = factor;
        deltaScore = ((double)high - low) / (NUM_OF_LVLS - 1);
        setTheme(theme);
    }

    // factor is according to the difficulty level - "1" for normal, "0.75" for easy and "1.25" for hard
    public void setFactor(double factor) {
        this.factor = factor;
        LVL_MAX.setMin(factor*highScore);
        LVL_8.setMin(factor*(highScore - deltaScore));
        LVL_7.setMin(factor*(highScore - 2 * deltaScore));
        LVL_6.setMin(factor*(highScore - 3 * deltaScore));
        LVL_5.setMin(factor*(highScore - 4 * deltaScore));
        LVL_4.setMin(factor*(highScore - 5 * deltaScore));
        LVL_3.setMin(factor*(highScore - 6 * deltaScore));
        LVL_2.setMin(factor*(highScore - 7 * deltaScore));
        LVL_1.setMin(factor*lowScore);
        LVL_8.setMax(factor*highScore);
        LVL_7.setMax(factor*(highScore - deltaScore));
        LVL_6.setMax(factor*(highScore - 2 * deltaScore));
        LVL_5.setMax(factor*(highScore - 3 * deltaScore));
        LVL_4.setMax(factor*(highScore - 4 * deltaScore));
        LVL_3.setMax(factor*(highScore - 5 * deltaScore));
        LVL_2.setMax(factor*(highScore - 6 * deltaScore));
        LVL_1.setMax(factor*(highScore - 7 * deltaScore));
        LVL_MIN.setMax(factor*lowScore);
    }

    // No theme
    public void initializeNoneAchievements(double factor) {
        LVL_MAX = new AchievementLevel(factor*(highScore), INF, "Level 10", R.drawable.empty, 9);
        LVL_8 = new AchievementLevel(factor*(highScore - deltaScore),factor*highScore, "Level 9", R.drawable.empty, 8);
        LVL_7 = new AchievementLevel(factor*(highScore - 2 * deltaScore),factor*(highScore - deltaScore), "Level 8", R.drawable.empty, 7);
        LVL_6 = new AchievementLevel(factor*(highScore - 3 * deltaScore),factor*(highScore - 2 * deltaScore), "Level 7", R.drawable.empty, 6);
        LVL_5 = new AchievementLevel(factor*(highScore - 4 * deltaScore),factor*(highScore - 3 * deltaScore), "Level 6", R.drawable.empty, 5);
        LVL_4 = new AchievementLevel(factor*(highScore - 5 * deltaScore),factor*(highScore - 4 * deltaScore), "Level 5", R.drawable.empty, 4);
        LVL_3 = new AchievementLevel(factor*(highScore - 6 * deltaScore),factor*(highScore - 5 * deltaScore), "Level 4", R.drawable.empty, 3);
        LVL_2 = new AchievementLevel(factor*(highScore - 7 * deltaScore), factor*(highScore - 6 * deltaScore), "Level 3", R.drawable.empty, 2);
        LVL_1 = new AchievementLevel(factor*(lowScore), factor*(highScore - 7 * deltaScore), "Level 2", R.drawable.empty, 1);
        LVL_MIN = new AchievementLevel(NEG_INF,factor*lowScore, "Level 1", R.drawable.empty, 0);
    }

    public void initializeNutAchievements(double factor) {
        LVL_MAX = new AchievementLevel(factor*(highScore), INF, "Master Macadamia", R.drawable.macadamia, 9);
        LVL_8 = new AchievementLevel(factor*(highScore - deltaScore),factor*highScore, "Amazing Almond", R.drawable.almond, 8);
        LVL_7 = new AchievementLevel(factor*(highScore - 2 * deltaScore),factor*(highScore - deltaScore), "Pretty Pecan", R.drawable.pecan, 7);
        LVL_6 = new AchievementLevel(factor*(highScore - 3 * deltaScore),factor*(highScore - 2 * deltaScore), "Crazy CornNut", R.drawable.cornnut, 6);
        LVL_5 = new AchievementLevel(factor*(highScore - 4 * deltaScore),factor*(highScore - 3 * deltaScore), "Wacky Walnut", R.drawable.walnut, 5);
        LVL_4 = new AchievementLevel(factor*(highScore - 5 * deltaScore),factor*(highScore - 4 * deltaScore), "Savvy SoyNut", R.drawable.soynut, 4);
        LVL_3 = new AchievementLevel(factor*(highScore - 6 * deltaScore),factor*(highScore - 5 * deltaScore), "Crafty Cashew", R.drawable.cashew, 3);
        LVL_2 = new AchievementLevel(factor*(highScore - 7 * deltaScore), factor*(highScore - 6 * deltaScore), "Happy Hazelnut", R.drawable.hazelnut, 2);
        LVL_1 = new AchievementLevel(factor*(lowScore), factor*(highScore - 7 * deltaScore), "Playful Pistachio", R.drawable.pistachio, 1);
        LVL_MIN = new AchievementLevel(NEG_INF,factor*lowScore, "Pleasant Peanut", R.drawable.peanut, 0);
    }

    public void initializeEmojiAchievements(double factor) {
        LVL_MAX = new AchievementLevel(factor*(highScore), INF, "Star Struck Stella", R.drawable.starstruck, 9);
        LVL_8 = new AchievementLevel(factor*(highScore - deltaScore),factor*highScore, "Cool Catherine", R.drawable.cool, 8);
        LVL_7 = new AchievementLevel(factor*(highScore - 2 * deltaScore),factor*(highScore - deltaScore), "Happy Hank", R.drawable.happy, 7);
        LVL_6 = new AchievementLevel(factor*(highScore - 3 * deltaScore),factor*(highScore - 2 * deltaScore), "Nerdy Ned", R.drawable.nerd, 6);
        LVL_5 = new AchievementLevel(factor*(highScore - 4 * deltaScore),factor*(highScore - 3 * deltaScore), "Sassy Sarah", R.drawable.sassy, 5);
        LVL_4 = new AchievementLevel(factor*(highScore - 5 * deltaScore),factor*(highScore - 4 * deltaScore), "Smiley Sally", R.drawable.slight_smile, 4);
        LVL_3 = new AchievementLevel(factor*(highScore - 6 * deltaScore),factor*(highScore - 5 * deltaScore), "Bored Bobby", R.drawable.bored, 3);
        LVL_2 = new AchievementLevel(factor*(highScore - 7 * deltaScore), factor*(highScore - 6 * deltaScore), "Worried Wart", R.drawable.worried, 2);
        LVL_1 = new AchievementLevel(factor*(lowScore), factor*(highScore - 7 * deltaScore), "Crying Crabby", R.drawable.crying, 1);
        LVL_MIN = new AchievementLevel(NEG_INF,factor*lowScore, "Swearing Sam", R.drawable.swear, 0);
    }

    public void initializeMiddleEarthAchievements(double factor) {
        LVL_MAX = new AchievementLevel(factor*(highScore), INF, "Wonderful Wizard", R.drawable.wizard, 9);
        LVL_8 = new AchievementLevel(factor*(highScore - deltaScore),factor*highScore, "Festive Fairy", R.drawable.fairy, 8);
        LVL_7 = new AchievementLevel(factor*(highScore - 2 * deltaScore),factor*(highScore - deltaScore), "Shy Sidekick", R.drawable.sidekick, 7);
        LVL_6 = new AchievementLevel(factor*(highScore - 3 * deltaScore),factor*(highScore - 2 * deltaScore), "Brave Bird", R.drawable.siren, 6);
        LVL_5 = new AchievementLevel(factor*(highScore - 4 * deltaScore),factor*(highScore - 3 * deltaScore), "Humble Human", R.drawable.human, 5);
        LVL_4 = new AchievementLevel(factor*(highScore - 5 * deltaScore),factor*(highScore - 4 * deltaScore), "Soulful Spirit", R.drawable.spirit, 4);
        LVL_3 = new AchievementLevel(factor*(highScore - 6 * deltaScore),factor*(highScore - 5 * deltaScore), "Viscous Viking", R.drawable.viking, 3);
        LVL_2 = new AchievementLevel(factor*(highScore - 7 * deltaScore), factor*(highScore - 6 * deltaScore), "Easy Elf", R.drawable.elf, 2);
        LVL_1 = new AchievementLevel(factor*(lowScore), factor*(highScore - 7 * deltaScore), "Greasy Gargoyle", R.drawable.gargoyle, 1);
        LVL_MIN = new AchievementLevel(NEG_INF,factor*lowScore, "Odourish Orc", R.drawable.orc, 0);
    }

    // Returns AchievementLevel object from score and number of players
    // PRE: players must be greater than 0
    public AchievementLevel getAchievement(double score, int numOfPlayers) {
        if (numOfPlayers <= 0) {
            throw new IllegalArgumentException("Players must be a positive integer");
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
        else  {
            return LVL_MIN;
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
            throw new IllegalArgumentException("not valid level");
        }
    }

    public AchievementLevel getLevelByID(int id) {
        if (id == 9) {
            return LVL_MAX;
        }
        else if (id == 1) {
            return LVL_1;
        }
        else if (id == 2) {
            return LVL_2;
        }
        else if (id == 3) {
            return LVL_3;
        }
        else if (id == 4) {
            return LVL_4;
        }
        else if (id == 5) {
            return LVL_5;
        }
        else if (id == 6) {
            return LVL_6;
        }
        else if (id == 7) {
            return LVL_7;
        }
        else if (id == 8) {
            return LVL_8;
        }
        else if (id == 0) {
            return LVL_MIN;
        }
        else {
            throw new IllegalArgumentException("not valid level");
        }
    }

    public AchievementLevel getLVL_MAX() {
        return LVL_MAX;
    }

    public AchievementLevel getLVL_8() {
        return LVL_8;
    }

    public AchievementLevel getLVL_7() {
        return LVL_7;
    }

    public AchievementLevel getLVL_6() {
        return LVL_6;
    }

    public AchievementLevel getLVL_5() {
        return LVL_5;
    }

    public AchievementLevel getLVL_4() {
        return LVL_4;
    }

    public AchievementLevel getLVL_3() {
        return LVL_3;
    }

    public AchievementLevel getLVL_2() {
        return LVL_2;
    }

    public AchievementLevel getLVL_1() {
        return LVL_1;
    }

    public AchievementLevel getLVL_MIN() {
        return LVL_MIN;
    }

    public double getFactor() {
        return factor;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
        if (theme.equals(NUT)) {
            initializeNutAchievements(factor);
        }
        else if (theme.equals(EMOJI)) {
            initializeEmojiAchievements(factor);
        }
        else if (theme.equals(MIDDLE_EARTH)) {
            initializeMiddleEarthAchievements(factor);
        }
        else {
            initializeNoneAchievements(factor);
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
