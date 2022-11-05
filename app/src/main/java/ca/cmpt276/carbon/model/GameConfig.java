package ca.cmpt276.carbon.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameConfig implements Iterable<Game> {

    private List<Game> gamesList;
    private static GameConfig instance;

    // Constructor
    public GameConfig() {
        this.gamesList = new ArrayList<>();
    }

    // for singleton model
    public static GameConfig getInstance(){
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public static void setInstance(GameConfig config) {
        instance = config;
    }

    // returns the array list of games
    public List<Game> getGamesList() {
        return gamesList;
    }

    // adds a game into the list
    public void addGame(Game game) {
        gamesList.add(game);
    }

    // delete a game from list
    public void deleteGame(int index) {
        gamesList.remove(index);
    }

    // returns the game at index
    public Game getGame(int index) {
        return gamesList.get(index);
    }

    // Returns the size of the list
    public int size() {
        return gamesList.size();
    }

    // TODO Not needed yet. Possibly remove in future if never used.
    // returns a string of games
    public List<String> gameStr() {
        List<String> gameNames= new ArrayList<>();
        for (int i = 0; i < gamesList.size(); i++)
        {
            // changed "game" to "getGameName()" for encapsulation
            String name= (gamesList.get(i).getGameName());
            //String name= "Chess";
            gameNames.add(name);
        }
        return gameNames;
    }

    // Overriding method to create an iterator of games
    @Override
    public Iterator<Game> iterator() {
        return gamesList.iterator();
    }


}
