package ca.cmpt276.carbon.model;

import java.util.ArrayList;

public class Game {
    ArrayList<Session> sessionsList;
    String gameName;
    int high_score;
    int low_score;
    public int getHigh_score() {
        return high_score;
    }

    public void setHigh_score(int high_score) {
        this.high_score = high_score;
    }

    public int getLow_score() {
        return low_score;
    }

    public void setLow_score(int low_score) {
        this.low_score = low_score;
    }


    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public Game() {
        this.sessionsList = new ArrayList<>();
        high_score=0;
        low_score=0;
        gameName="";
    }
    public Game(String g,int lowScore,int highScore)
    {
        gameName=g;
        low_score=lowScore;
        high_score=highScore;
        this.sessionsList = new ArrayList<>();
    }

    // Add new Game
    public void addNewGame(Session session) {
        sessionsList.add(session);
    }

    // Helper methods
    public Session getGameAtIndex(int index) {
        return sessionsList.get(index);
    }


}
