package ca.cmpt276.carbon.model;

import java.util.ArrayList;

public class GameConfig {
    public ArrayList<Game> getGamesList() {
        return gamesList;
    }
    public void insertGame(Game game)
    {
        gamesList.add(game);
    }

    private ArrayList<Game> gamesList;
    private static GameConfig instance;

    // Constructor
    public GameConfig() {
        this.gamesList = new ArrayList<>();
    }
    public static GameConfig getInstance(){
        if(instance== null)
        {
            instance= new GameConfig();
        }
        return instance;
    }
    public ArrayList<String> gameStr() {
        ArrayList<String> gameNames= new ArrayList<>();
        for (int i = 0; i < gamesList.size(); i++)
        {
            String name= (gamesList.get(i).gameName);
            //String name= "Chess";
            gameNames.add(name);
        }
        return gameNames;
    }
}
