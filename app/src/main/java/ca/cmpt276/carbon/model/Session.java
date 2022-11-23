package ca.cmpt276.carbon.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *This class stores the timePlayed, players and totalScore, achievementLevel
 */
public class Session {

    // Variables
    private final String timePlayed;
    private int players;
    private int totalScore;
    private Achievements achievementLevel;
    private String sessionDifficulty;           // Easy, Normal, Hard
    private String sessionTheme;                // None, Nut, Emoji, Middle Earth
    private List<Integer> playerScoreList;

    // Constructor
    public Session() {
        this.timePlayed = formatTime();
        this.players = -1;
        this.totalScore = -1;
        this.playerScoreList = new ArrayList<>();
        this.sessionDifficulty = "Normal";
        this.sessionTheme = "None";
    }

    // Getter/Setter methods
    public int getPlayers() {
        return players;
    }
    public void setPlayers(int players) {
        this.players = players;
    }

    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Achievements getAchievementLevel() {
        return achievementLevel;
    }
    public void setAchievementLevel(Achievements achievementLevel) {
        this.achievementLevel = achievementLevel;
    }

    public String getSessionDifficulty() {
        return sessionDifficulty;
    }
    public void setSessionDifficulty(String sessionDifficulty) {
        this.sessionDifficulty = sessionDifficulty;
    }

    public String getSessionTheme() {
        return sessionTheme;
    }
    public void setSessionTheme(String sessionTheme) {
        this.sessionTheme = sessionTheme;
    }

    public String getTimePlayed() {
        return timePlayed;
    }

    public List<Integer> getPlayerScoreList() {
        return this.playerScoreList;
    }
    public void setPlayerScoreList(List<Integer> playerScoreList) {
        this.playerScoreList = playerScoreList;
    }

    // Time format
    public String formatTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ hh:mm a");
        return currentTime.format(format);
    }


}
