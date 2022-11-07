package ca.cmpt276.carbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.Game;
import ca.cmpt276.carbon.model.GameConfig;

public class GameConfigActivity extends AppCompatActivity {

    // Intent extra Constant
    private static final String EXTRA_GAME_INDEX = "gameIndex: ";

    private GameConfig gameConfiguration;
    private Game game= new Game();
    private List<String> gameSessions;

    // index passed in by the editing game intent call
    private int index;

    // Edit text fields for the three fields on screen
    private EditText gameName, lowScore, highScore;
    private int numLowScore, numHighScore;

    // Variables for indexed game retrieval
    private String indexedGameName;
    private int indexedLowScore, indexedHighScore;

    // Variables for storing old game information for editing config
    private String oldGameName;
    private int oldLowScore, oldHighScore;

    // Variables for storing new game information for editing config
    private String newGameName;
    private int newLowScore, newHighScore;

    // bool to keep track if you're in editing mode or viewing mode
    // this will also be used later to update the game session scores
    private Boolean isEditGameConfig = false;

    // view achievement level button
    private Button viewAchievements;
    TextView selectIcon;
    // Add new session button
    FloatingActionButton btnAddSession;

    // For printing sessions played
    private ListView sessionList;

    GridLayout gridImageLayout;

    // empty state images and text
    TextView welcomeScreenMsg;
    ImageView welcomeImage;
    ImageView welcomePointer;

    // Build an intent int input is the index of the game clicked
    public static Intent makeLaunchIntent(Context c, int input) {
        Intent intent = new Intent(c, GameConfigActivity.class);
        intent.putExtra(EXTRA_GAME_INDEX, input);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.gameConfigToolbar);
        setSupportActionBar(toolbar);

        // setup grid and images
        gridImageLayout = findViewById(R.id.gridLayout);
        selectIcon = findViewById(R.id.selectGameIcon);

        // View achievements
        viewAchievements = findViewById(R.id.btnViewAchievements);
        viewAchievements.setVisibility(View.INVISIBLE);

        viewAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = AchievementsActivity.makeLaunchIntent(GameConfigActivity.this,
                        game.getLowScore(), game.getHighScore());
                startActivity(i);
            }
        });

        // Enable up on toolbar
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // handle the extra
        Intent i = getIntent();
        index = i.getIntExtra(EXTRA_GAME_INDEX, 0);

        // get the instance of the singleton
        gameConfiguration = GameConfig.getInstance();

        // Initialize add session button
        btnAddSession = findViewById(R.id.btnAddNewSession);

        // if index is -1, you're on add game screen
        if(index == -1) {

            // hide the session empty state images
            hideMascotOnAddConfig();

            // set the title of the activity
            getSupportActionBar().setTitle("Add Configuration");

            // make a new game since we're adding
            game = new Game();

            // Make button invisible
            btnAddSession.setVisibility(View.INVISIBLE);
            gridImageLayout.setVisibility(View.VISIBLE);

            // get user inputs
            setupGameConfigDataFields();
        }
        // else you're in viewing game mode -
        // in this mode, you can edit the HS, LS, add a session, remove session etc.
        else if (index >= 0) {
            // Populate game sessions
            populateGameSessions(index);

            // ListView for Sessions
            registerClickCallback();

            viewAchievements.setVisibility(View.VISIBLE);
            btnAddSession.setVisibility(View.VISIBLE);
            gridImageLayout.setVisibility(View.GONE);
            selectIcon.setVisibility(View.GONE);

            // Change title to show editing game instead
            getSupportActionBar().setTitle("Game Sessions");

            // Get the game at index
            game = gameConfiguration.getGame(index);

            // are you editing?
            isEditGameConfig = false;

            // Display the game and it's sessions (if any)
            displayGame();

            // Button for add session
            btnAddSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(GameConfigActivity.this, SessionsActivity.class);
                    i.putExtra("SESSION_INDEX", -1);
                    i.putExtra("GAME_INDEX", index);
                    i.putExtra("LOW_SCORE", game.getLowScore());
                    i.putExtra("HIGH_SCORE", game.getHighScore());
                    startActivity(i);
                }
            });
        }
    }

    // method to hide the peanut and text on game config creation
    private void hideMascotOnAddConfig() {

        welcomeScreenMsg = findViewById(R.id.tvSessionsEmptyState);
        welcomeImage = findViewById(R.id.imageViewSessionsPeanut);
        welcomePointer = findViewById(R.id.imageViewSessionsArrow);

        welcomeScreenMsg.setVisibility(View.GONE);
        welcomeImage.setVisibility(View.GONE);
        welcomePointer.setVisibility(View.GONE);

    }

    // method to show empty state message when no sessions populated
    private void showEmptyState() {

        // Assets for welcome image screen
        welcomeScreenMsg = findViewById(R.id.tvSessionsEmptyState);
        welcomeImage = findViewById(R.id.imageViewSessionsPeanut);
        welcomePointer = findViewById(R.id.imageViewSessionsArrow);

        // If listview is empty, show welcome image
        if (gameSessions.isEmpty()) {
            sessionList.setVisibility(View.GONE);
            welcomeScreenMsg.setVisibility(View.VISIBLE);
            welcomeImage.setVisibility(View.VISIBLE);
            welcomePointer.setVisibility(View.VISIBLE);
        }
        // otherwise, show list
        else {
            sessionList.setVisibility(View.VISIBLE);
            welcomeScreenMsg.setVisibility(View.GONE);
            welcomeImage.setVisibility(View.GONE);
            welcomePointer.setVisibility(View.GONE);
        }
    }

    // For adding a session inside game config
    private void registerClickCallback() {

        sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(GameConfigActivity.this, SessionsActivity.class);
                i.putExtra("SESSION_INDEX", position);
                i.putExtra("GAME_INDEX", index);
                i.putExtra("LOW_SCORE", game.getLowScore());
                i.putExtra("HIGH_SCORE", game.getHighScore());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Re-Populate ListView of Sessions
        if (index != -1) {
            populateGameSessions(index);
        }
    }

    // disables the text fields because you're in viewing mode
    private void disableEditText(EditText editText) {

        editText.setEnabled(false);
        editText.setBackgroundColor(Color.BLACK);
        editText.setTextColor(Color.YELLOW);

        viewAchievements.setVisibility(View.VISIBLE);
        btnAddSession.setVisibility(View.VISIBLE);
        sessionList.setVisibility(View.VISIBLE);

    }

    // enables text fields because you're editing game config
    private void enableEditText(EditText editText) {

        editText.setEnabled(true);
        editText.setBackgroundColor(Color.LTGRAY);
        editText.setTextColor(Color.BLUE);

        viewAchievements.setVisibility(View.INVISIBLE);
        btnAddSession.setVisibility(View.INVISIBLE);
        sessionList.setVisibility(View.INVISIBLE);

    }

    // displays the game information when clicked in list view
    private void displayGame() {

        // Set all the information for the game
        // set name
        gameName = findViewById(R.id.etGameName);
        gameName.setText(game.getGameName());

        // set low score
        lowScore = findViewById(R.id.etLowScore);
        lowScore.setText(Integer.toString(game.getLowScore()));

        // set high score
        highScore = findViewById(R.id.etHighScore);
        highScore.setText(Integer.toString(game.getHighScore()));

        // check if you're in viewing or editing mode
        if (!isEditGameConfig) {
            // disable text fields because user shouldn't be able to edit
            disableEditText(gameName);
            disableEditText(lowScore);
            disableEditText(highScore);
        }
        else {
            // enable editing once the edit button in toolbar pressed
            enableEditText(gameName);
            enableEditText(lowScore);
            enableEditText(highScore);

            // watch the text fields for updates
            gameName.addTextChangedListener(inputTextWatcher);
            lowScore.addTextChangedListener(inputTextWatcher);
            highScore.addTextChangedListener(inputTextWatcher);
        }

    }

    // On the first play through, add the fields and respond to save button
    private void setupGameConfigDataFields() {

        gameName = findViewById(R.id.etGameName);
        lowScore = findViewById(R.id.etLowScore);
        highScore = findViewById(R.id.etHighScore);
    }


    // adds the game configuration to the list of games
    private void addGameConfig() {

        String name = gameName.getText().toString();
        String score1 = lowScore.getText().toString();
        String score2 = highScore.getText().toString();

        if(name.isEmpty() || score1.isEmpty() || score2.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!highScore.getText().toString().equals("") && !lowScore.getText().toString().equals(""))
        {
            numLowScore = Integer.parseInt(score1);
            numHighScore = Integer.parseInt(score2);
        }

        if (numHighScore <= numLowScore ) {
            Toast.makeText(this, "High score must be greater than Low score", Toast.LENGTH_SHORT).show();
            return;
        }

        // make a new game with given parameters
        //game = new Game(name, numLowScore, numHighScore);
        game.setGameName(name);
        game.setHighScore(numHighScore);
        game.setLowScore(numLowScore);


        // add game to configs
        gameConfiguration.addGame(game);

        // Finish the activity after adding and return to main menu
        Toast.makeText(GameConfigActivity.this, "Added", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Text watcher that keeps track of fields when user is editing game config
    private TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed, possibly add implementation to have the fields highlight red
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // TODO later - switch case for inputs to decrease repetitive code
            try {
                if (!gameName.getText().toString().isEmpty()) {
                    indexedGameName = gameName.getText().toString();
                }
                if (!lowScore.getText().toString().isEmpty()) {
                    indexedLowScore = Integer.parseInt(lowScore.getText().toString().trim());
                }
                if (!highScore.getText().toString().isEmpty()) {
                    indexedHighScore = Integer.parseInt(highScore.getText().toString().trim());
                }

            } catch (NumberFormatException ex) {
                //Toast.makeText(AddGameActivity.this, "must not be empty", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            // not needed
        }
    };


    // TODO : ALL TOOLBAR ACTIONS GO HERE ------------------------------------
    // Menu and action bar things
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu based on index passed in
        if (index == -1) {
            getMenuInflater().inflate(R.menu.menu_add_game_config, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_view_game_config, menu);
        }
        return true;
    }

    // correspond to app bar menu choices
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_game:
                if (index == -1) {

                    // normal configuration of adding game
                    setupGameConfigDataFields();
                    addGameConfig();

                } else {
                    // save the newly entered values
                    saveEditedValuesForGameConfig();
                }
                return true;

            case R.id.action_delete_game:
                deleteGameConfig();
                return true;

            case R.id.action_edit_game:

                // set editing games to true
                isEditGameConfig = true;

                // Save values in case the user changes mind
                storeOldValues();

                // this is in order to accommodate the updating of scores
                // through all the list of play sessions
                editGameConfiguration();

                return true;

            case android.R.id.home:
                if (index == -1) {
                    finish();
                } else {
                    displayCancelMessage();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // store old values in case user changes their mind
    // about editing the game config and presses up
    private void storeOldValues() {
        oldGameName = game.getGameName();
        oldLowScore = game.getLowScore();
        oldHighScore = game.getHighScore();

    }

    // Set previous values if cancelled mid way
    private void cancelValueInputs() {
        game.setGameName(oldGameName);
        game.setLowScore(oldLowScore);
        game.setHighScore(oldHighScore);
    }

    // Save the edited values when user presses save after changing fields
    private void saveEditedValuesForGameConfig() {

        try {
            newGameName = gameName.getText().toString();
            newLowScore = Integer.parseInt(lowScore.getText().toString().trim());
            newHighScore = Integer.parseInt(highScore.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(GameConfigActivity.this, "Score must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newGameName.isEmpty()) {
            Toast.makeText(GameConfigActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newHighScore <= newLowScore ) {
            Toast.makeText(this, "High score must be greater than Low score", Toast.LENGTH_SHORT).show();
            return;
        }

        game.setGameName(newGameName);
        game.setHighScore(newHighScore);
        game.setLowScore(newLowScore);

        finish();

    }

    // Enable editing of Game Config when edit button on toolbar pressed
    private void editGameConfiguration() {

        // extract it into variables
        indexedGameName = game.getGameName();
        indexedLowScore = game.getLowScore();
        indexedHighScore = game.getHighScore();

        // set the text fields
        EditText gameName = findViewById(R.id.etGameName);
        EditText lowScore = findViewById(R.id.etLowScore);
        EditText highScore = findViewById(R.id.etHighScore);

        // display the text fields
        gameName.setText(indexedGameName);
        lowScore.setText(" " + indexedLowScore);
        highScore.setText(" " + indexedHighScore);

        enableEditText(gameName);
        enableEditText(lowScore);
        enableEditText(highScore);

    }

    // method to delete the game configuration
    private void deleteGameConfig() {
        // Get confirmation to delete
        new AlertDialog.Builder(GameConfigActivity.this).setTitle("Delete Game Configuration?")
                .setPositiveButton("Yes", (dialog, option) -> {

                    // Find the game at index passed in and delete
                    gameConfiguration.deleteGame(index);

                    // Display message to show that game was deleted
                    Toast.makeText(GameConfigActivity.this, "Game Configuration Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", (dialog, option) -> {
                    // Do nothing and stay on current screen
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        displayCancelMessage();
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Confirm if user wants to cancel
    private void displayCancelMessage() {

        new AlertDialog.Builder(this).setTitle("Cancel Operation?")
                .setPositiveButton("Yes", (dialog, option) -> {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    if (isEditGameConfig) {
                        cancelValueInputs();
                    }
                    finish();
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int option) {
                        // Do nothing and stay on current screen
                    }
                }).show();
    }

    private void populateGameSessions(int gameIndex) {
        // List of Sessions
        gameSessions = new ArrayList<>();

        // Populate ListView
        for (int i = 0; i < gameConfiguration.getGame(gameIndex).getSize(); i++) {

            // Time played, total players, combined score, achievement earned
            String time = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getTimePlayed();
            int players = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getPlayers();
            int score = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getTotalScore();
            String level = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getAchievementLevel();

            gameSessions.add("Time played: " + time + ", Total Players: " + players +
                    ", Score: " + score + ", Level: " + level);
        }

        // Array adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                gameSessions
        );

        sessionList = findViewById(R.id.sessionsListView);
        sessionList.setAdapter(adapter);

        // method to display empty state if no sessions
        showEmptyState();

        adapter.notifyDataSetChanged();
    }

    public void imageView1Clicked(View view) {
        //ImageView img = (ImageView) view;
        int tappedImage = R.drawable.p1;
        game.setImageID(tappedImage);
    }
    public void imageView2Clicked(View view) {
        int tappedImage = R.drawable.p2;
        game.setImageID(tappedImage);
    }
    public void imageView3Clicked(View view) {
        int tappedImage = R.drawable.p3;
        game.setImageID(tappedImage);
    }
    public void imageView4Clicked(View view) {
        int tappedImage = R.drawable.p4;
        game.setImageID(tappedImage);
    }
}
