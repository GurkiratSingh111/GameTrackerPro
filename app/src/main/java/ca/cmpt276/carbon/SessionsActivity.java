package ca.cmpt276.carbon;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

/**
 * This activity ask the user to enter number of players, total Score and displays
 * achievement level
 */
public class SessionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    private double factor;
    private String stringPlayers;       // String of total players
    private String stringScore;         // String of total score

    // Dropdown variables
    private String achievementTheme;    // Theme for Achievement in Session
    private String theme;               // Theme for Session
    private String difficulty;          // Difficulty for Session

    // Objects
    private Session session;            // Session for add session

    // Singleton
    private GameConfig gameConfiguration;

    private int combinedScore;          // Combined score of all playerse
    private int prevNumPlayers;

    private List<Integer> scoreList;    // List of score of players
    private ListView listView;          // ListView of score of players
    private ListviewAdapter adapter;    // Adapter for listView

    // Congratulations message
    private AlertDialog.Builder congratsMsg;
    private ImageView congratsImg;
    private MediaPlayer congratsSound;

    // Spinners
    private Spinner difficultySpinner;  // Difficulty dropdown
    private Spinner themeSpinner;       // Theme dropdown

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        session = new Session();

        Toolbar toolbar = findViewById(R.id.sessionsToolbar);
        setSupportActionBar(toolbar);

        // Enable toolbar
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialization/intents
        initializeSession();
        initializeSpinner();
        initializeCongratsMessage();

        // If index is -1, go to add new session screen
        if (sessionIndex == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Watch for change to display correct achievement level
            totalScore.addTextChangedListener(playerNumTextWatcher);
            totalPlayers.addTextChangedListener(playerNumTextWatcher);
            scoreList = new ArrayList<>();
        }
        else {
            // Set title to Edit Session
            getSupportActionBar().setTitle("Edit Session");
            session = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex);

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();
            scoreList = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayerScoreList();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + session.getAchievementLevel().getAchievement(intScore, intPlayers).getName());

            totalPlayers.addTextChangedListener(playerNumTextWatcher);

            initializePlayerScores();

            // Set dropdown fields
            populateDropdownDifficulty(difficultySpinner);
            populateDropdownTheme(themeSpinner);

            difficulty = session.getSessionDifficulty();
            theme = session.getSessionTheme();
            achievementTheme = session.getAchievementLevel().getTheme();
        }

        findViewById(R.id.btnSetNumPlayers).setOnClickListener( v -> {
            initializePlayerScores();
        });

        findViewById(R.id.btnUpdateScore).setOnClickListener( v -> {
            updateAchievementAndScore();
        });

    }
    // Initialization methods
    private void initializeSession() {

        // Get the game session
        gameConfiguration = GameConfig.getInstance();

        totalPlayers = findViewById(R.id.totalPlayers);

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

        achievementTheme =Achievements.NONE;
        theme = "None";
        difficulty = "Normal";
        factor = 1.0;

        // Initialize achievement levels
        session.setAchievementLevel(new Achievements(lowScore, highScore, factor));
    }
    private void initializePlayerScores() {

        if (totalPlayers.getText().toString().isEmpty()) {
            Toast.makeText(SessionsActivity.this, "Number of players can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        listView = findViewById(R.id.lvPlayerScores);
        listView.setItemsCanFocus(true);

        while (scoreList.size() < intPlayers) {
            scoreList.add(0);
        }
        while (scoreList.size() > intPlayers) {
            scoreList.remove(scoreList.size()-1);
        }

        adapter = new ListviewAdapter( SessionsActivity.this, scoreList, totalScore, achievement, combinedScore, session.getAchievementLevel(), prevNumPlayers, totalPlayers);
        listView.setAdapter(adapter);

        totalScore.setText("" + adapter.getUpdatedCombinedScore());
        achievement.setText("ACHIEVEMENT is: " + session.getAchievementLevel().getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());

    }
    private void initializeSpinner() {
        // Difficulty spinner
        difficultySpinner = findViewById(R.id.gameLevels);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setOnItemSelectedListener(this);

        // Theme spinner
        themeSpinner = findViewById(R.id.themeVariants);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);
    }
    private void initializeCongratsMessage() {
        // Initialize Congratulations pop-up
        congratsMsg = new AlertDialog.Builder(this);
        congratsImg = findViewById(R.id.imgCongrats);
        congratsImg.setVisibility(View.GONE);

        // If user clicks out of alert, finish activity
        congratsMsg.setOnDismissListener(dialog -> finish());
    }

    // Spinner initialization methods for edit session
    private void populateDropdownDifficulty(Spinner difficultySpinner) {
        String difficulty = session.getSessionDifficulty();
        difficultySpinner.setSelection(getIndexOfSpinner(difficultySpinner, difficulty));

    }
    private void populateDropdownTheme(Spinner themeSpinner) {
        String theme = session.getSessionTheme();

        themeSpinner.setSelection(getIndexOfSpinner(themeSpinner, theme));
    }

    // Get position of a specified value in Spinner
    private int getIndexOfSpinner(Spinner spinner, String name) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void updateAchievementAndScore() {
        totalScore.setText("" + adapter.getUpdatedCombinedScore());
        achievement.setText("ACHIEVEMENT is: " + session.getAchievementLevel().getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
    }

    @Override
    // Add/View session menus
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sessionIndex == -1) {
            getMenuInflater().inflate(R.menu.menu_add_session, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_view_session, menu);
        }
        return true;
    }

    @Override
    // Toolbar widgets for save and delete session
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_session:
                saveSession();
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
            if (!totalPlayers.getText().toString().isEmpty()) {

                for (int i = 0; i < scoreList.size(); i++) {
                    if (scoreList.get(i) == null) {
                        throw new IllegalArgumentException();
                    }
                }

                stringPlayers = totalPlayers.getText().toString();

                intPlayers = Integer.parseInt(stringPlayers);

                for (int i = 0; i < intPlayers; i++) {
                    combinedScore += scoreList.get(i);
                }

                intScore = combinedScore;

                // Creating new session
                if (sessionIndex == -1) {
                    // Create new session and add to List
                    session.setPlayers(intPlayers);
                    session.setTotalScore(intScore);
                    session.setPlayerScoreList(scoreList);

                    gameConfiguration.getGame(configIndex).addSession(session);

                    // Play congratulations message
                    congratsAnimation(congratsImg);

                }
                // Editing existing session
                else {
                    // Replace values in the session
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayers(intPlayers);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setTotalScore(intScore);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayerScoreList(scoreList);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setAchievementLevel(new Achievements(lowScore, highScore, factor));
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setSessionTheme(theme);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setSessionDifficulty(difficulty);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getAchievementLevel().setTheme(achievementTheme);
                    finish();
                }

            } else {
                Toast.makeText(SessionsActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(SessionsActivity.this, "Players cannot be zero.", Toast.LENGTH_SHORT).show();
        }
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

                    if (intPlayers < 1 || intPlayers > 25) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(SessionsActivity.this, "Invalid input.", Toast.LENGTH_SHORT).show();
            }
            catch (IllegalArgumentException e) {
                Toast.makeText(SessionsActivity.this, "Must have 1 to 25 Players.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();


        // Difficulty
        if (text.equals("Easy")) {
            factor = 0.75;
            difficulty = "Easy";
            theme = achievementTheme;
        }
        else if (text.equals("Normal")) {
            factor = 1.0;
            difficulty = "Normal";
            theme = achievementTheme;
        }
        else if (text.equals("Hard")) {
            factor = 1.25;
            difficulty = "Hard";
            theme = achievementTheme;
        }

        // Theme
        if (text.equals("Nut")) {
            theme = "Nut";
            achievementTheme = Achievements.NUT;
        }
        else if (text.equals("Emoji")) {
            theme = "Emoji";
            achievementTheme = Achievements.EMOJI;
        }
        else if (text.equals("Middle Earth")) {
            theme = "Middle Earth";
            achievementTheme = Achievements.MIDDLE_EARTH;
        }
        else if (text.equals("None")) {
            theme = "None";
            achievementTheme = Achievements.NONE;
        }

        // Current achievement
        Achievements currentAchievement = new Achievements(lowScore, highScore, session.getAchievementLevel().getFactor());
        currentAchievement.setTheme(achievementTheme);

        if (adapter != null) {
            achievement.setText("ACHIEVEMENT is: " + currentAchievement.getAchievement(intScore, intPlayers).getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing here
    }

    private void fadeAnimation(ImageView img) {
        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .playOn(img);
    }

    private void bounceAnimation(ImageView img) {
        YoYo.with(Techniques.Bounce)
                .duration(700)
                .repeat(3)
                .playOn(img);
    }

    private void congratsAnimation(ImageView img) {
        img.setVisibility(View.VISIBLE);
        fadeAnimation(img);
        bounceAnimation(img);
        playSound();

        // Delay pop up for animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                congratsMsg();
            }
        }, 2700);
    }

    private void congratsMsg() {
        // Allow user to exit by clicking outside of alert box
        congratsMsg.setCancelable(true);

        // Alert display
        congratsMsg.setTitle("Congratulations")
                .setMessage("You've added a new session!")
                .setIcon(session.getAchievementLevel().getAchievement(intScore, intPlayers).getImage())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    // Play sound method
    private void playSound() {
        congratsSound = MediaPlayer.create(this, R.raw.win);

        congratsSound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                congratsSound.start();
            }
        });
        congratsSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                congratsSound.release();
            }
        });
    }
}
