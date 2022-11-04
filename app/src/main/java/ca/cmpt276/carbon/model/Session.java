package ca.cmpt276.carbon.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Session {
    // Variables
    private LocalDateTime timePlayed;
    private int players;
    private int totalScore;
    private String achievement;

    // TODO - add achievement to this

    // Constructor
    public Session(LocalDateTime timePlayed, int players, int totalScore) {
        this.timePlayed = timePlayed;
        this.players = players;
        this.totalScore = totalScore;
    }

    // Getter/Setter methods
    public LocalDateTime getTimePlayed() {
        return timePlayed;
    }
    public void setTimePlayed(LocalDateTime timePlayed) {
        this.timePlayed = timePlayed;
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

    // Time format
    public String formatTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d @ hh:mm a");
        return timePlayed.format(format);
    }
}
