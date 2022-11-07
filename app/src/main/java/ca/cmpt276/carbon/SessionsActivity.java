package ca.cmpt276.carbon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

public class SessionsActivity extends AppCompatActivity {

    // Variables
    private int sessionIndex;                  // For add/edit sessions
    private int configIndex;            // Game index of gameConfig
    private EditText totalPlayers;      // Total players of a single session
    private EditText totalScore;        // Total score of all players in a session
    private TextView achievement;       // Display achievement of player
    private Button saveBtn;             // Save button for saving data to List
    private int intPlayers;             // Integer of total players
    private int intScore;               // Integer of total score

    private int lowScore;               // Low score of game
    private int highScore;              // High score of game

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
        saveBtn = findViewById(R.id.saveSessionBtn);

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

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stringPlayers = totalPlayers.getText().toString();
                    stringScore = totalScore.getText().toString();

                    if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                        intPlayers = Integer.parseInt(stringPlayers);
                        intScore = Integer.parseInt(stringScore);
                    }

                    // Create new session and add to List
                    session = new Session(intPlayers, intScore);
                    session.setAchievementLevel(level.getAchievement(intScore, intPlayers).getName());
                    gameConfiguration.getGame(configIndex).addSession(session);

                    finish();
                }
            });
        }
        else {
            // Set title to Edit Session
            getSupportActionBar().setTitle("Edit Session");

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());

            // Watch for change to display correct achievement level
            totalScore.addTextChangedListener(inputTextWatcher);
            totalPlayers.addTextChangedListener(inputTextWatcher);

            // Save new input
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stringPlayers = totalPlayers.getText().toString();
                    stringScore = totalScore.getText().toString();

                    if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                        intPlayers = Integer.parseInt(stringPlayers);
                        intScore = Integer.parseInt(stringScore);
                    }

                    String achievedLevel = level.getAchievement(intScore, intPlayers).getName();

                    // Replace values in the session
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayers(intPlayers);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setTotalScore(intScore);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setAchievementLevel(achievedLevel);

                    finish();
                }
            });
        }
    }

    private TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                    stringPlayers = totalPlayers.getText().toString();
                    stringScore = totalScore.getText().toString();
                    intPlayers = Integer.parseInt(stringPlayers);
                    intScore = Integer.parseInt(stringScore);
                    achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());
                }
                else {
                    achievement.setText("ACHIEVEMENT: ");
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(SessionsActivity.this, "Fields must not be empty.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed
        }
    };

}
