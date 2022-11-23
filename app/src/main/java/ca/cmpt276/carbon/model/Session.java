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
    private String gameDifficulty;               // Easy, Normal, Hard
    private List<Integer> playerScoreList;

    // Constructor
    public Session() {
        this.timePlayed = formatTime();
        this.players = -1;
        this.totalScore = -1;
        this.playerScoreList = new ArrayList<>();
        this.gameDifficulty = "Normal";
    }

    // Getter/Setter methods
    public String getGameDifficulty() {
        return gameDifficulty;
    }
    public void setGameDifficulty(String gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
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

    public Achievements getAchievementLevel() {
        return achievementLevel;
    }
    public void setAchievementLevel(Achievements achievementLevel) {
        this.achievementLevel = achievementLevel;
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
