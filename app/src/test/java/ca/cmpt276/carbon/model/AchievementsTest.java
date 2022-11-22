package ca.cmpt276.carbon.model;

/**
 * JUnit Testing for Achievements Class
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AchievementsTest {

    @Test
    void checkInvalidNumberOfPlayers()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        Assertions.assertThrows(IllegalArgumentException.class,()->achievements.getAchievement(120,-1));
    }

    @Test
    void checkGetAchievementLv2()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(120,4);
        Assertions.assertEquals(lv,achievements.getLVL_2());
    }

    @Test
    void checkGetAchievementLv1()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(100,4);
        Assertions.assertEquals(lv,achievements.getLVL_1());
    }

    @Test
    void checkGetAchievementLv3()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(160,4);
        Assertions.assertEquals(lv,achievements.getLVL_3());
    }

    @Test
    void checkGetAchievementLv4()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(200,4);
        Assertions.assertEquals(lv,achievements.getLVL_4());
    }

    @Test
    void checkGetAchievementLv5()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(240,4);
        Assertions.assertEquals(lv,achievements.getLVL_5());
    }

    @Test
    void checkGetAchievementLv6()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(280,4);
        Assertions.assertEquals(lv,achievements.getLVL_6());
    }

    @Test
    void checkGetAchievementLv7()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(320,4);
        Assertions.assertEquals(lv,achievements.getLVL_7());
    }

    @Test
    void checkGetAchievementLv8()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(360,4);
        Assertions.assertEquals(lv,achievements.getLVL_8());
    }

    @Test
    void checkGetAchievementLvMax()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(420,4);
        Assertions.assertEquals(lv,achievements.getLVL_MAX());
    }

    @Test
    void checkGetAchievementLvMin()
    {
        Achievements achievements= new Achievements(10,100,1.0);
        achievements.setFactor(1.0);
        AchievementLevel lv= achievements.getAchievement(10,4);
        Assertions.assertEquals(lv,achievements.getLVL_MIN());
    }

    @Test
    void checkSetFactor()
    {
        Achievements achievements= new Achievements(10,20,1.25);
        achievements.setFactor(1.0);
        Assertions.assertEquals(1.0,achievements.getFactor());
    }

    @Test
    void checkReturnHighScore()
    {
        Achievements achievements= new Achievements(10,20,1.25);
        Assertions.assertEquals(25.0,achievements.returnHighScore(1.25));
    }

    @Test
    void checkReturnLowScore()
    {
        Achievements achievements= new Achievements(10,20,1.25);
        Assertions.assertEquals(12.5,achievements.returnLowScore(1.25));
    }

    @Test
    void checkSetLowScore()
    {
        Achievements achievements= new Achievements(10,20,1.25);
        achievements.setLowScore(15);
        Assertions.assertEquals(15,achievements.getLowScore());
    }

    @Test
    void checkSetHighScore()
    {
        Achievements achievements= new Achievements(10,20,1.25);
        achievements.setHighScore(35);
        Assertions.assertEquals(35,achievements.getHighScore());
    }

    @Test
    void checkGetFactor()
    {
        Achievements achievements= new Achievements(10,20,1);
        Assertions.assertEquals(1.0,achievements.getFactor());
    }

    @Test
    void checkGetLowScore() {
        Achievements achievements= new Achievements(10,20,1);
        Assertions.assertEquals(10,achievements.getLowScore());
    }
    @Test
    void checkGetHighScore()
    {
        Achievements achievements= new Achievements(10,20,1);
        Assertions.assertEquals(20,achievements.getHighScore());
    }

    @Test
    void checkDeltaScore()
    {
        Achievements achievements= new Achievements(0,18,1);
        Assertions.assertEquals(2.0,achievements.getDeltaScore());
    }

    @Test
    void checkGetLevelsException()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> achievements.getLevel("20"));
    }

    @Test
    void checkGetLv1()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_1(),achievements.getLevel("1"));
    }

    @Test
    void checkGetLv2()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_2(),achievements.getLevel("2"));
    }

    @Test
    void checkGetLv3()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_3(),achievements.getLevel("3"));
    }

    @Test
    void checkGetLv4()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_4(),achievements.getLevel("4"));
    }

    @Test
    void checkGetLv5()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_5(),achievements.getLevel("5"));

    }

    @Test
    void checkGetLv6()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_6(),achievements.getLevel("6"));
    }

    @Test
    void checkGetLv7()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_7(),achievements.getLevel("7"));
    }

    @Test
    void checkGetLv8()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_8(),achievements.getLevel("8"));
    }

    @Test
    void checkGetLvMax()
    {
        Achievements achievements= new Achievements(18,0,1);
        Assertions.assertEquals(achievements.getLVL_MAX(),achievements.getLevel("MAX"));
    }

    @Test
    void checkGetLvMin() {
        Achievements achievements = new Achievements(18, 0, 1);
        Assertions.assertEquals(achievements.getLVL_MIN(), achievements.getLevel("MIN"));
    }

    @Test
    void checkSetThemeNUT()
    {
        Achievements achievements = new Achievements(10, 20, 1);
        achievements.setTheme("NUT");
        Assertions.assertEquals(achievements.getTheme(), "NUT");
    }

    @Test
    void checkSetThemeEmoji()
    {
        Achievements achievements = new Achievements(10, 20, 1);
        achievements.setTheme("EMOJI");
        Assertions.assertEquals(achievements.getTheme(),"EMOJI");
    }

    @Test
    void checkSetThemeMiddleEarth()
    {
        Achievements achievements = new Achievements(10, 20, 1);
        achievements.setTheme("MIDDLE_EARTH");
        Assertions.assertEquals(achievements.getTheme(),"MIDDLE_EARTH");
    }

    @Test
    void checkSetThemeRandom()
    {
        Achievements achievements = new Achievements(10, 20, 1);
        achievements.setTheme("Random");
        Assertions.assertEquals(achievements.getTheme(),"Random");
    }
}