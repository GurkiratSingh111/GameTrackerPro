package ca.cmpt276.carbon.model;

import java.util.ArrayList;

public class Session {
    private int players;
    private int totalScore;

    // Constructor
    public Session(int players, int totalScore) {
        this.players = players;
        this.totalScore = totalScore;
    }

    // Get average score
    public int getAverageScore() {
        return (totalScore / players);
    }
}
