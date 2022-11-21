package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.cmpt276.carbon.model.Game;
import ca.cmpt276.carbon.model.GameConfig;
import android.content.SharedPreferences;
import com.google.gson.Gson;

/**
 * Main activity that initializes a collection of game configurations
 * and lists them on screen. If no list is created, shows a nice welcome
 * message to prompt user to add games. Allows the user to add a new
 * game config or edit the game config by clicking on it once its created
 */
public class MainActivity extends AppCompatActivity {
    // Variables
    private ListView list;              // ListView for Game
    private List<String> gameList;      // List of Game in strings for ListView

    // Shared preferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "Game Config";

    // Singleton
    private GameConfig gameConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.gameConfigToolbar);
        setSupportActionBar(toolbar);

        // Set the title of the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("COOP Game!");

        // Load the game config
        loadData();

        // fill in the items in list view
        populateListView();

        // click function to display info about the item clicked
        registerClickCallback();

        // When pressed, launches add game activity (passes in index of games)
        findViewById(R.id.btnAddGame).setOnClickListener( v -> {
            Intent i = GameConfigActivity.makeLaunchIntent(MainActivity.this, -1);
            startActivity(i);
        });
    }

    // Saves data for next launch
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameConfiguration);
        editor.putString(NAME, json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NAME, "");

        if (!json.isEmpty()) {
            GameConfig newConfig = gson.fromJson(json, GameConfig.class);
            GameConfig.setInstance(newConfig);
        }
        gameConfiguration = GameConfig.getInstance();
    }

    // Override the start method and refresh the list displayed
    @Override
    protected void onStart() {
        super.onStart();

        // Populate list view on startup, if any games
        populateListView();

        // Print welcome message if no games found
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        // Assets for welcome image screen
        TextView welcomeScreenMsg = findViewById(R.id.tvPistolPete);
        ImageView welcomeImage = findViewById(R.id.imageViewPeanut);
        ImageView welcomePointer = findViewById(R.id.imgArrow);

        // If listview is empty, show welcome image
        if (gameConfiguration.size() == 0) {
            list.setVisibility(View.GONE);
            welcomeScreenMsg.setVisibility(View.VISIBLE);
            welcomeImage.setVisibility(View.VISIBLE);
            welcomePointer.setVisibility(View.VISIBLE);
        }
        // Otherwise, show list
        else {
            list.setVisibility(View.VISIBLE);
            welcomeScreenMsg.setVisibility(View.GONE);
            welcomeImage.setVisibility(View.GONE);
            welcomePointer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Populate list view on startup, if any games
        populateListView();
    }

    // Method that registers which item in the list is clicked
    private void registerClickCallback() {
        // Get the list
        list = findViewById(R.id.listViewGameList);

        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent i = GameConfigActivity.makeLaunchIntent(MainActivity.this, position);
            startActivity(i);
        });
    }

    // List of Games
    private void populateListView() {
        // Make a list of Games
        gameList = new ArrayList<>();

        // Populate List of Games for ListView
        for (int i = 0; i < gameConfiguration.size(); i++) {

            String name = gameConfiguration.getGame(i).getGameName();
            int ls =  gameConfiguration.getGame(i).getLowScore();
            int hs = gameConfiguration.getGame(i).getHighScore();

            gameList.add("" + name + "\nLow Score: " + ls + "\nHigh Score: " + hs);
        }

        // Adapter to connect between listview and items
        ArrayAdapter<String> adapter = new MyListAdapter();

        // Configure the list view
        list = findViewById(R.id.listViewGameList);
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        saveData();
    }

    // Nice UI design of ListView
    private class MyListAdapter extends ArrayAdapter<String> {

        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_design, gameList);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_design, parent, false);
            }
            String str = gameList.get(position);
            Game g = gameConfiguration.getGame(position);
            ImageView imageView = itemView.findViewById(R.id.item_icon);
            // imageView.setImageResource(g.getImageID());

            TextView makeText = itemView.findViewById(R.id.textView);
            makeText.setText(str);
            return itemView;
        }
    }
}