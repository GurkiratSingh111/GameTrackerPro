package ca.cmpt276.carbon.model;

import java.util.ArrayList;

public class Game {
    ArrayList<Session> sessionsList;
    String gameName;
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public Game() {
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
