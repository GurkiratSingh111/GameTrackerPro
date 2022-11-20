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
    private String timePlayed;
    private int players;
    private int totalScore;
    private String achievementLevel;
    private String gameLevel; //Easy,Normal or Hard

    private List<Integer> playerScoreList;

    // Default Constructor
    public Session() {
        this.timePlayed = "";
        this.players = -1;
        this.totalScore = -1;
        this.achievementLevel = "";
        this.playerScoreList = new ArrayList<>();
        this.gameLevel= "Normal";
    }

    // Constructor
    public Session(int players, int totalScore, List<Integer> pList) {
        this.timePlayed = formatTime();
        this.players = players;
        this.totalScore = totalScore;
        this.playerScoreList = pList;
    }

    // Getter/Setter methods

    public String getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(String gameLevel) {
        this.gameLevel = gameLevel;
    }

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

    public String getAchievementLevel() {
        return achievementLevel;
    }
    public void setAchievementLevel(String achievementLevel) {
        this.achievementLevel = achievementLevel;
    }

    public String getTimePlayed() {
        return timePlayed;
    }

    // Time format
    public String formatTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ hh:mm a");
        return currentTime.format(format);
    }

    public List<Integer> getPlayerScoreList() {
        return this.playerScoreList;
    }

    public void setPlayerScoreList(List<Integer> playerScoreList) {
        this.playerScoreList = playerScoreList;
    }
}
