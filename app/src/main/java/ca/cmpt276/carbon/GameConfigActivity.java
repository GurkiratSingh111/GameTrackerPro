package ca.cmpt276.carbon;

import static ca.cmpt276.carbon.MainActivity.NAME;
import static ca.cmpt276.carbon.MainActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.carbon.model.Game;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

/**
 * This activity allows the user to add game configurations, edit and delete game configurations too.
 */
public class GameConfigActivity extends AppCompatActivity {

    // Intent extra Constant
    private static final String EXTRA_GAME_INDEX = "gameIndex: ";
    private static final int PERMISSION_CODE = 100;                // Camera permission code
    private static final int CAMERA_REQUEST_CODE = 101;            // Camera request code

    // Variables
    private List<String> gameSessions;                             // List of Sessions played for display
    private ListView sessionList;                                  // For printing sessions played

    private int index;                                             // Intent for editing gameConfig
    private Boolean isEditGameConfig = false;                      // Boolean that keeps track of if you are viewing/editing a gameConfig

    private EditText gameName, lowScore, highScore;                // EditText for fields of game name and low/high score
    private int numLowScore, numHighScore;                         // Int of low/high score

    private String indexedGameName;                                // Index of a gameConfig's name
    private int indexedLowScore, indexedHighScore;                 // Indexes of a gameConfig's low/high score
    private String oldGameName;                                    // Old value of gameConfig name for editing
    private int oldLowScore, oldHighScore;                         // Old value of gameConfig low/high score for editing
    private String newGameName;                                    // New value of storing name of gameConfig
    private int newLowScore, newHighScore;                         // New value of storing low/high score of gameConfig
    private Button btnStatistics;                                  // Button to view Sessions Statistics
    private Button viewAchievements;                               // Button to view Achievements
    private Button takePhotoBtn;                                   // Button for taking photos
    private FloatingActionButton btnAddSession;                    // Floating Action Button for creating new Session

    private ImageView gamePhoto;                                   // Current image for the Game

    private TextView welcomeScreenMsg;                             // Empty state message for no sessions in List
    private ImageView welcomeImage;                                // Empty state image for no sessions in List
    private ImageView welcomePointer;                              // Empty state pointer for no sessions in List

    private Uri game_image_uri;                                    // Storage for photos taken by camera
    private Uri current_image_uri;                                 // Current photo taken
    private boolean isPhotoTaken;                                  // Check if a photo was taken

    // Objects
    private GameConfig gameConfiguration;                          // Stores a List of Games
    private Game game = new Game();                                // New Game object

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
        gamePhoto = findViewById(R.id.imageViewSelectedImage);
        btnStatistics =findViewById(R.id.btnStats);
        // View achievements
        viewAchievements = findViewById(R.id.btnViewAchievements);
        viewAchievements.setVisibility(View.GONE);

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

        // Initialize photo button
        takePhotoBtn = findViewById(R.id.gameTakePhotoBtn);
        isPhotoTaken = false;
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraPermissions();
            }
        });
        // if index is -1, you're on add game screen
        if (index == -1) {

            btnStatistics.setVisibility(View.GONE);
            // hide the session empty state images
            hideMascotOnAddConfig();

            // set the title of the activity
            getSupportActionBar().setTitle("Add Configuration");

            // make a new game since we're adding
            game = new Game();

            // Make button invisible
            btnAddSession.setVisibility(View.GONE);
            //btnStatistics.setVisibility(View.GONE);
            // get user inputs
            setupGameConfigDataFields();
        }
        //when you are in viewing game mode
        //There are no sessions in that game.
        else if(index >=0 && gameConfiguration.getGamesList().get(index).getSize()==0)
        {
            btnStatistics.setVisibility(View.GONE);
            populateGameSessions(index);

            // ListView for Sessions
            registerClickCallback();

            viewAchievements.setVisibility(View.VISIBLE);
            btnAddSession.setVisibility(View.VISIBLE);

            // Change title to show editing game instead
            getSupportActionBar().setTitle("Game Sessions");

            // Get the game at index
            game = gameConfiguration.getGame(index);

            // are you editing?
            isEditGameConfig = false;

            // Check if a photo was taken
            if (game.isPhotoTaken()) {
                game_image_uri = game.getPhoto();
                gamePhoto.setImageURI(game_image_uri);
                isPhotoTaken = true;
            }

            // Display the game and its sessions (if any)
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

        // else you're in viewing game mode -
        // in this mode, you can edit the HS, LS, add a session, remove session etc.
        else if (index >= 0) {

            // Populate game sessions
            populateGameSessions(index);

            // ListView for Sessions
            registerClickCallback();

            viewAchievements.setVisibility(View.VISIBLE);
            btnAddSession.setVisibility(View.VISIBLE);

            // Change title to show editing game instead
            getSupportActionBar().setTitle("Game Sessions");

            // Get the game at index
            game = gameConfiguration.getGame(index);

            // are you editing?
            isEditGameConfig = false;

            // Check if a photo was taken
            if (game.isPhotoTaken()) {
                game_image_uri = game.getPhoto();
                gamePhoto.setImageURI(game_image_uri);
                isPhotoTaken = true;
            }

            // Display the game and its sessions (if any)
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

    @Override
    protected void onResume() {
        super.onResume();
        saveData();

        // Re-Populate ListView of Sessions
        if (index != -1) {
            populateGameSessions(index);
        }
        //When there are no sessions or if we are creating a new game
        if((index >=0 && gameConfiguration.getGamesList().get(index).getSize()==0) || (index==-1))
        {
            btnStatistics.setVisibility(View.GONE);
        }
        else
        {
            btnStatistics.setVisibility(View.VISIBLE);
        }

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

    // On the first play through, add the fields and respond to save button
    private void setupGameConfigDataFields() {
        gameName = findViewById(R.id.etGameName);
        lowScore = findViewById(R.id.etLowScore);
        highScore = findViewById(R.id.etHighScore);
    }

    private void populateGameSessions(int gameIndex) {
        // List of Sessions
        gameSessions = new ArrayList<>();
        saveData();

        // Populate ListView
        if (gameConfiguration != null) {

            for (int i = 0; i < gameConfiguration.getGame(gameIndex).getSize(); i++) {

                // Time played, total players, combined score, achievement earned
                String time = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getTimePlayed();
                int players = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getPlayers();
                int score = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getTotalScore();
                String level = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getAchievementLevel().getAchievement(score, players).getName();
                String difficultyLevel = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getSessionDifficulty();

                gameSessions.add("Time played: " + time + "\nTotal Players: " + players +
                        "\nScore: " + score + "\nLevel: " + level + "\nDifficulty Level: " + difficultyLevel);
            }
        }
        // Array adapter for ListView
        ArrayAdapter<String> adapter = new SessionsListAdapter();

        sessionList = findViewById(R.id.sessionsListView);
        sessionList.setAdapter(adapter);

        // method to display empty state if no sessions
        showEmptyState();

        adapter.notifyDataSetChanged();
    }

    // UI Design of Sessions ListView
    private class SessionsListAdapter extends ArrayAdapter<String> {
        // Constructor
        public SessionsListAdapter() {
            super(GameConfigActivity.this, R.layout.session_design, gameSessions);
        }

        // Get data for display
        public View getView(int position, View view, ViewGroup parent) {
            View sessionsView = view;
            if (sessionsView == null) {
                sessionsView = getLayoutInflater().inflate(R.layout.session_design, parent, false);
            }

            String str = gameSessions.get(position);
            Session session = game.getSessionAtIndex(position);
            ImageView imageView = sessionsView.findViewById(R.id.sessions_icon);
            ImageView photoView = sessionsView.findViewById(R.id.sessions_photo_icon);

            // Check if session has a theme and use image if true
            // Else use a default image
            if (!session.getSessionTheme().equals("None")) {
                imageView.setImageResource(session.getAchievementLevel().getAchievement(session.getTotalScore(), session.getPlayers()).getImage());
            }
            else {
                imageView.setImageResource(R.drawable.p3);
            }

            // If photo was not taken, use default image
            // Else, use photo taken
            if (!session.isPhotoTaken()) {
                photoView.setImageResource(R.drawable.p1);
            }
            else {
                photoView.setImageURI(session.getPhoto());
            }


            TextView displayText = sessionsView.findViewById(R.id.sessionsText);
            displayText.setText(str);

            return sessionsView;
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

    // disables the text fields because you're in viewing mode
    private void disableEditText(EditText editText) {

        editText.setEnabled(false);
        editText.setBackgroundColor(Color.BLACK);
        editText.setTextColor(Color.YELLOW);

        viewAchievements.setVisibility(View.VISIBLE);
        btnAddSession.setVisibility(View.VISIBLE);
        sessionList.setVisibility(View.VISIBLE);
        takePhotoBtn.setVisibility(View.GONE);
    }

    // enables text fields because you're editing game config
    private void enableEditText(EditText editText) {

        editText.setEnabled(true);
        editText.setBackgroundColor(Color.LTGRAY);
        editText.setTextColor(Color.BLUE);

        viewAchievements.setVisibility(View.GONE);
        btnAddSession.setVisibility(View.GONE);
        sessionList.setVisibility(View.GONE);
        takePhotoBtn.setVisibility(View.VISIBLE);
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
        } else {
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

    // adds the game configuration to the list of games
    private void addGameConfig() {

        String name = gameName.getText().toString();
        String score1 = lowScore.getText().toString();
        String score2 = highScore.getText().toString();

        if ((score1.length() > 10) || (score2.length() > 10)) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || score1.isEmpty() || score2.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!highScore.getText().toString().equals("") && !lowScore.getText().toString().equals("")) {
            numLowScore = Integer.parseInt(score1);
            numHighScore = Integer.parseInt(score2);
        }

        if (numHighScore <= numLowScore) {
            Toast.makeText(this, "High score must be greater than Low score", Toast.LENGTH_SHORT).show();
            return;
        }

        // make a new game with given parameters
        //game = new Game(name, numLowScore, numHighScore);
        game.setGameName(name);
        game.setHighScore(numHighScore);
        game.setLowScore(numLowScore);

        // Add image to Game if a photo was taken
        if (isPhotoTaken) {
            game.setPhoto(game_image_uri);
            game.setPhotoTaken(true);
        }

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

                // hide the session empty state images
                hideMascotOnAddConfig();

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

        // Store previously taken photo (if any)
        if (isPhotoTaken) {
            game_image_uri = game.getPhoto();
        }
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
            Toast.makeText(GameConfigActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newGameName.isEmpty()) {
            Toast.makeText(GameConfigActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newHighScore <= newLowScore) {
            Toast.makeText(this, "High score must be greater than Low score", Toast.LENGTH_SHORT).show();
            return;
        }

        game.setGameName(newGameName);
        game.setHighScore(newHighScore);
        game.setLowScore(newLowScore);

        // Check if a photo was taken
        if (isPhotoTaken) {
            game.setPhotoTaken(true);
            game.setPhoto(game_image_uri);
        }

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
        lowScore.setText(indexedLowScore + "");
        highScore.setText(indexedHighScore + "");

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
        saveData();
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

    // Enable camera permissions or open camera if already enabled
    private void getCameraPermissions() {
        // Check for if camera permission is granted
        // Else, open camera
        if (ContextCompat.checkSelfPermission(GameConfigActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GameConfigActivity.this, new String[]{
                    Manifest.permission.CAMERA}, PERMISSION_CODE);
        }
        else {
            openCamera();
        }
    }

    // Override for camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
            else {
                Toast.makeText(this, "Camera permissions are not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Open the camera
    private void openCamera() {
        ContentValues value = new ContentValues();
        value.put(MediaStore.Images.Media.TITLE, "Photo");
        value.put(MediaStore.Images.Media.DESCRIPTION, "Camera");
        current_image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);

        // Open camera
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT, current_image_uri);
        startActivityIfNeeded(openCamera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Get image if accepted
        if (resultCode == -1) {     // -1 = photo taken, 0 = no photo taken
            Log.i("Photo code", Integer.toString(resultCode));
            if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                game_image_uri = current_image_uri;
                gamePhoto.setImageURI(game_image_uri);
                isPhotoTaken = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickStatistics(View view)
    {
        Intent intent = AchievementStatistics.makeIntent(GameConfigActivity.this,index);
        //intent.putExtra(AchievementStatistics.SESSION_INDEX,index);
        startActivity(intent);
    }
}

