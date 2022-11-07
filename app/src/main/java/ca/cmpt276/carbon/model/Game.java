package ca.cmpt276.carbon.model;

import java.util.ArrayList;

import ca.cmpt276.carbon.R;

public class Game {

    // TODO CHECKLIST: Possibly pass some of these responsibilities to Session class
    // 1) Save game session creation date: use LocalDateTime
    // 2) Return string from session to display: sessionToString
    // 3) Be able to edit session
    // 4) ...

    ArrayList<Session> sessionsList;

    private String gameName;
    private int highScore;
    private int lowScore;
    private int imageID;

    public int getImageID() {
        return imageID;
    }
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    // default constructor
    public Game() {
        this.sessionsList = new ArrayList<>();
        highScore = 0;
        lowScore = 0;
        gameName = "";
        imageID= R.drawable.p1;
    }

    // custom constructor with parameters
    public Game(String g,int ls,int hs) {
        gameName = g;
        this.lowScore = ls;
        this.highScore = hs;
        this.sessionsList = new ArrayList<>();
    }

    public int getHighScore() {
        return highScore;
    }
    public int getLowScore() {
        return lowScore;
    }
    public String getGameName() {
        return gameName;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    public void setLowScore(int lowScore) {
        this.lowScore = lowScore;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // Add, replace, delete new session
    public void addSession(Session s) {
        sessionsList.add(s);
    }
    public void deleteSession(int index) {
        sessionsList.remove(index);
    }

    // Helper methods
    public Session getSessionAtIndex(int index) {
        return sessionsList.get(index);
    }

    // Size of sessionsList
    public int getSize() {
        return sessionsList.size();
    }

}
