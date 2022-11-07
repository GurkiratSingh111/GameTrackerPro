package ca.cmpt276.carbon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

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

        // Initialization
        gameConfiguration = GameConfig.getInstance();
        totalPlayers = findViewById(R.id.totalPlayers);
        totalScore = findViewById(R.id.totalScore);
        achievement = findViewById(R.id.achievementTextView);

        // Get Intent
        Intent i = getIntent();
        sessionIndex = i.getIntExtra("SESSION_INDEX", -1);
        configIndex = i.getIntExtra("GAME_INDEX", -1);
        lowScore = i.getIntExtra("LOW_SCORE", -1);
        highScore = i.getIntExtra("HIGH_SCORE", -1);

        // Initialize achievement levels
        level = new Achievements(lowScore, highScore);

        // If index is -1, go to add new session screen
        if (sessionIndex == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Create new Session
            session = new Session();

            // Watch for change to display correct achievement level
            totalScore.addTextChangedListener(inputTextWatcher);
            totalPlayers.addTextChangedListener(inputTextWatcher);
        }
        else {
            // Set title to View Session
            getSupportActionBar().setTitle("View Session");

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());

            // Disable editing
            disableTextFields(totalPlayers);
            disableTextFields(totalScore);
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Toolbar widget helper methods
    private void saveSession() {
        try {
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

                finish();
            } else {
                Toast.makeText(SessionsActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(SessionsActivity.this, "Players cannot be zero.", Toast.LENGTH_SHORT).show();
        }
    }
    private void editSession() {
        enableTextFields(totalPlayers);
        enableTextFields(totalScore);
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
        totalScore.addTextChangedListener(inputTextWatcher);
        totalPlayers.addTextChangedListener(inputTextWatcher);
    }
    private void disableTextFields(EditText editText) {
        editText.setEnabled(false);
        editText.setBackgroundColor(Color.BLACK);
        editText.setTextColor(Color.YELLOW);
    }

    // TextWatcher for data fields
    private final TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
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
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Not needed
        }
    };
}
