package ca.cmpt276.carbon.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Session {
    // Variables
    private String timePlayed;
    private int players;
    private int totalScore;


    // Default Constructor
    public Session() {
        this.timePlayed = null;
        this.players = -1;
        this.totalScore = -1;
    }

    // Constructor
    public Session(int players, int totalScore) {
        this.timePlayed = formatTime();
        this.players = players;
        this.totalScore = totalScore;
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

    // Time format
    public String formatTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ hh:mm a");
        return currentTime.format(format);
    }
}
