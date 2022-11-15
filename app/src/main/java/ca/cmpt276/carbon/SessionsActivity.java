package ca.cmpt276.carbon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

/**
 *This activity ask the user to enter number of players, total Score and displays
 * achievement level
 */
public class SessionsActivity extends AppCompatActivity {
    // Variables
    private int sessionIndex;           // For add/edit sessions
    private int configIndex;            // Game index of gameConfig
    private EditText totalPlayers;      // Total players of a single session
    private EditText totalScore;        // Total score of all players in a session
    private TextView achievement;       // Display achievement of player

    private int lowScore;               // Low score of game
    private int highScore;              // High score of game
    private int intPlayers;             // Integer of total players
    private int intScore;               // Integer of total score

    private String stringPlayers;       // String of total players
    private String stringScore;         // String of total score

    // Objects
    private Session session;            // Session for add session
    private Achievements level;         // Achievement for display

    // Singleton
    private GameConfig gameConfiguration;

    // TODO : NEW STUFF HERE ------------

    private int p1Score, p2Score, p3Score, p4Score;
    private int combinedScore;

    private EditText etP1Score, etP2Score, etP3Score, etP4Score;
    private List<EditText> etPlayerScoreList;

    private List<Integer> playerScoreList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Toolbar toolbar = findViewById(R.id.sessionsToolbar);
        setSupportActionBar(toolbar);

        // Enable toolbar
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialization/intents
        initializeSession();

        // Make an array list of playerScores
        playerScoreList = new ArrayList<>();

        // If index is -1, go to add new session screen
        if (sessionIndex == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Watch for change to display correct achievement level
            //totalScore.addTextChangedListener(playerNumTextWatcher);
            totalPlayers.addTextChangedListener(playerNumTextWatcher);

            etP1Score.addTextChangedListener(scoreTextWatcher);
            etP2Score.addTextChangedListener(scoreTextWatcher);
            etP3Score.addTextChangedListener(scoreTextWatcher);
            etP4Score.addTextChangedListener(scoreTextWatcher);

        }
        else {
            // Set title to View Session
            getSupportActionBar().setTitle("View Session");

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();
            playerScoreList = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayerScoreList();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());

            int playerScoresToShow = playerScoreList.size();

            switch (playerScoresToShow) {
                case 1:
                    p1Score = playerScoreList.get(0);

                    etP1Score.setText("" + p1Score);
                    break;
                case 2:
                    p1Score = playerScoreList.get(0);
                    p2Score = playerScoreList.get(1);

                    etP1Score.setText("" + p1Score);
                    etP2Score.setText("" + p2Score);
                    break;
                case 3:
                    p1Score = playerScoreList.get(0);
                    p2Score = playerScoreList.get(1);
                    p3Score = playerScoreList.get(2);

                    etP1Score.setText("" + p1Score);
                    etP2Score.setText("" + p2Score);
                    etP3Score.setText("" + p3Score);
                    break;
                case 4:
                    p1Score = playerScoreList.get(0);
                    p2Score = playerScoreList.get(1);
                    p3Score = playerScoreList.get(2);
                    p4Score = playerScoreList.get(3);

                    etP1Score.setText("" + p1Score);
                    etP2Score.setText("" + p2Score);
                    etP3Score.setText("" + p3Score);
                    etP4Score.setText("" + p4Score);
                    break;
                default:
                    break;
            }

            // Disable editing
            disableTextFields(totalPlayers);
            disableTextFields(totalScore);

            etP1Score.setEnabled(false);
            etP2Score.setEnabled(false);
            etP3Score.setEnabled(false);
            etP4Score.setEnabled(false);
            /*disableTextFields(etP1Score);
            disableTextFields(etP2Score);
            disableTextFields(etP3Score);
            disableTextFields(etP4Score);*/

        }
    }

    private void initializeSession() {

        // Get the game session
        gameConfiguration = GameConfig.getInstance();

        totalPlayers = findViewById(R.id.totalPlayers);

        // Get the player score fields
        etP1Score = findViewById(R.id.etPlayer1Score);
        etP2Score = findViewById(R.id.etPlayer2Score);
        etP3Score = findViewById(R.id.etPlayer3Score);
        etP4Score = findViewById(R.id.etPlayer4Score);

        etP1Score.setEnabled(false);
        etP2Score.setEnabled(false);
        etP3Score.setEnabled(false);
        etP4Score.setEnabled(false);

        // add them to an array (brute force) for now
        /*playerScoreList.add(etP1Score);
        playerScoreList.add(etP2Score);
        playerScoreList.add(etP3Score);
        playerScoreList.add(etP4Score);
        for (int i = 0; i < 4; i++) {
            playerScoreList.get(i).setEnabled(false);
        }*/

        // Disable total score now that player score is being used
        totalScore = findViewById(R.id.totalScore);
        totalScore.setEnabled(false);

        achievement = findViewById(R.id.achievementTextView);

        // Get Intent
        Intent i = getIntent();
        sessionIndex = i.getIntExtra("SESSION_INDEX", -1);
        configIndex = i.getIntExtra("GAME_INDEX", -1);
        lowScore = i.getIntExtra("LOW_SCORE", -1);
        highScore = i.getIntExtra("HIGH_SCORE", -1);

        // Initialize achievement levels
        level = new Achievements(lowScore, highScore);
    }

    @Override
    // Add/View session menus
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sessionIndex == -1) {
            getMenuInflater().inflate(R.menu.menu_add_session, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_view_session, menu);
        }
        return true;
    }

    @Override
    // Toolbar widgets for save, edit, and delete session
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_session:
                saveSession();
                return true;
            case R.id.menu_edit_session:
                editSession();
                return true;
            case R.id.menu_delete_session:
                deleteSession();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Toolbar widget helper methods
    private void saveSession() {
        try {
            if (!totalPlayers.getText().toString().equals("")) {
                stringPlayers = totalPlayers.getText().toString();

                intPlayers = Integer.parseInt(stringPlayers);

                playerScoreList.clear();

                // populate the player score list
                switch (intPlayers) {
                    case 1:
                        if(etP1Score.getText().toString().isEmpty()) {
                            Toast.makeText(SessionsActivity.this, "Scores cannot be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        playerScoreList.add(p1Score);
                        break;
                    case 2:
                        if( etP1Score.getText().toString().isEmpty() ||
                            etP2Score.getText().toString().isEmpty()) {
                            Toast.makeText(SessionsActivity.this, "Scores cannot be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        playerScoreList.add(p1Score);
                        playerScoreList.add(p2Score);
                        break;
                    case 3:
                        if( etP1Score.getText().toString().isEmpty() ||
                            etP2Score.getText().toString().isEmpty() ||
                            etP3Score.getText().toString().isEmpty()) {
                            Toast.makeText(SessionsActivity.this, "Scores cannot be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        playerScoreList.add(p1Score);
                        playerScoreList.add(p2Score);
                        playerScoreList.add(p3Score);
                        break;
                    case 4:
                        if( etP1Score.getText().toString().isEmpty()||
                            etP2Score.getText().toString().isEmpty() ||
                            etP3Score.getText().toString().isEmpty() ||
                            etP4Score.getText().toString().isEmpty()) {
                            Toast.makeText(SessionsActivity.this, "Scores cannot be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        playerScoreList.add(p1Score);
                        playerScoreList.add(p2Score);
                        playerScoreList.add(p3Score);
                        playerScoreList.add(p4Score);
                        break;
                    default:
                        break;
                }

                intScore = combinedScore;

                // Check if players are 0
                if (intPlayers < 1 || intPlayers > 4) {
                    throw new IllegalArgumentException();
                }

                String achievedLevel = level.getAchievement(intScore, intPlayers).getName();

                Log.i("Session Index", Integer.toString(sessionIndex));

                // Creating new session
                if (sessionIndex == -1) {
                    // Create new session and add to List
                    session = new Session(intPlayers, intScore, playerScoreList);

                    session.setAchievementLevel(level.getAchievement(intScore, intPlayers).getName());

                    gameConfiguration.getGame(configIndex).addSession(session);
                }
                // Editing existing session
                else {
                    // Replace values in the session
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayers(intPlayers);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setTotalScore(intScore);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayerScoreList(playerScoreList);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setAchievementLevel(achievedLevel);
                }

                Toast.makeText(SessionsActivity.this, "" + combinedScore, Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(SessionsActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(SessionsActivity.this, "Players cannot be zero.", Toast.LENGTH_SHORT).show();
        }
        // TODO REMOVE OLD METHOD AFTER FINAL EDITS
        /*try {
            if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                stringPlayers = totalPlayers.getText().toString();
                stringScore = totalScore.getText().toString();

                intPlayers = Integer.parseInt(stringPlayers);
                intScore = Integer.parseInt(stringScore);

                // Check if players are 0
                if (intPlayers == 0) {
                    throw new IllegalArgumentException();
                }

                String achievedLevel = level.getAchievement(intScore, intPlayers).getName();
                Log.i("Session Index", Integer.toString(sessionIndex));
                // Creating new session
                if (sessionIndex == -1) {
                    // Create new session and add to List
                    session = new Session(intPlayers, intScore);
                    session.setAchievementLevel(level.getAchievement(intScore, intPlayers).getName());
                    gameConfiguration.getGame(configIndex).addSession(session);
                }
                // Editing existing session
                else {
                    // Replace values in the session
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayers(intPlayers);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setTotalScore(intScore);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setAchievementLevel(achievedLevel);
                }

                Toast.makeText(SessionsActivity.this, "" + combinedScore, Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(SessionsActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(SessionsActivity.this, "Players cannot be zero.", Toast.LENGTH_SHORT).show();
        }*/
    }
    private void editSession() {
        enableTextFields(totalPlayers);
        enablePlayerScoreTextFieldsBasedOnNumPlayers();

    }
    private void deleteSession() {
        new AlertDialog.Builder(SessionsActivity.this).setTitle("Delete Current Session?")
                .setPositiveButton("Yes", (dialog, option) -> {

                    // Find the game at index passed in and delete
                    gameConfiguration.getGame(configIndex).deleteSession(sessionIndex);

                    // Display message to show that game was deleted
                    Toast.makeText(SessionsActivity.this, "Game Session Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", (dialog, option) -> {
                    // Do nothing and stay on current screen
                }).show();
    }

    // Enable/disable text fields for edit session
    private void enableTextFields(EditText editText) {
        editText.setEnabled(true);
        editText.setBackgroundColor(Color.LTGRAY);
        editText.setTextColor(Color.BLUE);

        // Watch for change to display correct achievement level
        totalScore.addTextChangedListener(playerNumTextWatcher);
        totalPlayers.addTextChangedListener(playerNumTextWatcher);

        etP1Score.addTextChangedListener(scoreTextWatcher);
        etP2Score.addTextChangedListener(scoreTextWatcher);
        etP3Score.addTextChangedListener(scoreTextWatcher);
        etP4Score.addTextChangedListener(scoreTextWatcher);
    }
    private void disableTextFields(EditText editText) {
        editText.setEnabled(false);
        editText.setBackgroundColor(Color.BLACK);
        editText.setTextColor(Color.YELLOW);
    }

    // TextWatcher for data fields
    private final TextWatcher playerNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                if (!totalPlayers.getText().toString().equals("")) {
                    // Get data fields
                    stringPlayers = totalPlayers.getText().toString();

                    intPlayers = Integer.parseInt(stringPlayers);

                    // Check if players is 0
                    if (intPlayers > 0 && intPlayers < 5) {
                        enablePlayerScoreTextFieldsBasedOnNumPlayers();

                        // TODO WHY DOES THIS CRASH?? Need to add editTexts to an array
                        /*playerScoreList.add(etP1Score);
                        playerScoreList.add(etP2Score);
                        playerScoreList.add(etP3Score);
                        playerScoreList.add(etP4Score);*/

                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
                else {

                    achievement.setText("ACHIEVEMENT: ");
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(SessionsActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
            }
            catch (IllegalArgumentException e) {
                Toast.makeText(SessionsActivity.this, "Must have 1 to 4 Players.", Toast.LENGTH_SHORT).show();
            }

            // TODO : OLD method - update and delete after final edits
            /*try {
                if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                    // Get data fields
                    stringPlayers = totalPlayers.getText().toString();
                    stringScore = totalScore.getText().toString();

                    intPlayers = Integer.parseInt(stringPlayers);
                    intScore = Integer.parseInt(stringScore);

                    // Check if players is 0
                    if (intPlayers != 0) {

                        achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
                else {

                    achievement.setText("ACHIEVEMENT: ");
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(SessionsActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
            }
            catch (IllegalArgumentException e) {
                Toast.makeText(SessionsActivity.this, "Players cannot be zero.", Toast.LENGTH_SHORT).show();
            }*/
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Not needed
        }
    };

    private void enablePlayerScoreTextFieldsBasedOnNumPlayers() {

            switch (intPlayers) {
                case 1:
                    etP1Score.setEnabled(true);
                    etP2Score.setEnabled(false);
                    etP3Score.setEnabled(false);
                    etP4Score.setEnabled(false);
                    break;
                case 2:
                    etP1Score.setEnabled(true);
                    etP2Score.setEnabled(true);
                    etP3Score.setEnabled(false);
                    etP4Score.setEnabled(false);
                    break;
                case 3:
                    etP1Score.setEnabled(true);
                    etP2Score.setEnabled(true);
                    etP3Score.setEnabled(true);
                    etP4Score.setEnabled(false);
                    break;
                case 4:
                    etP1Score.setEnabled(true);
                    etP2Score.setEnabled(true);
                    etP3Score.setEnabled(true);
                    etP4Score.setEnabled(true);
                    break;
                default:
                    etP1Score.setEnabled(false);
                    etP2Score.setEnabled(false);
                    etP3Score.setEnabled(false);
                    etP4Score.setEnabled(false);
            }

    }

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // DO not implement
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (!etP1Score.getText().toString().isEmpty()) {
                        p1Score = Integer.parseInt(etP1Score.getText().toString().trim());
                    }
                    if (!etP2Score.getText().toString().isEmpty()) {
                        p2Score = Integer.parseInt(etP2Score.getText().toString().trim());
                    }
                    if (!etP3Score.getText().toString().isEmpty()) {
                        p3Score = Integer.parseInt(etP3Score.getText().toString().trim());
                    }
                    if (!etP4Score.getText().toString().isEmpty()) {
                        p4Score = Integer.parseInt(etP4Score.getText().toString().trim());
                    }
                }
                catch (NumberFormatException e) {
                    // Do nothing
                }

                combinedScore = p1Score + p2Score + p3Score + p4Score;

                if (combinedScore > -1) {
                    totalScore.setText("" + combinedScore);
                    achievement.setText("ACHIEVEMENT: " + level.getAchievement(combinedScore, intPlayers).getName());
                }
                else {
                    //totalScore.setText("--");
                    achievement.setText("ACHIEVEMENT: ");
                }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // DO not implement
        }
    };
}
