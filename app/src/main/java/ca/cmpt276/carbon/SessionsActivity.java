package ca.cmpt276.carbon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.media.MediaPlayer;

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

    // Objects
    private Session session;            // Session for add session
    private Achievements level;         // Achievement for display

    // Singleton
    private GameConfig gameConfiguration;

    // TODO : NEW STUFF HERE ------------

    private int combinedScore;
    private int prevNumPlayers;
    private boolean isAdapterOn;

    private List<Integer> scoreList;
    private ListView listView;
    private ListviewAdapter adapter;

    private AlertDialog.Builder congratsMsg;
    private ImageView congratsImg;
    private MediaPlayer congratsSound;

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

        //Spinner
        Spinner spinner = findViewById(R.id.gameLevels);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Initialization/intents
        initializeSession();

        // Initialize Congratulations pop-up
        congratsMsg = new AlertDialog.Builder(this);
        congratsImg = findViewById(R.id.imgCongrats);
        congratsImg.setVisibility(View.GONE);

        // If user clicks out of alert, finish activity
        congratsMsg.setOnDismissListener(dialog -> finish());


        // If index is -1, go to add new session screen
        if (sessionIndex == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Watch for change to display correct achievement level
            totalScore.addTextChangedListener(playerNumTextWatcher);
            totalPlayers.addTextChangedListener(playerNumTextWatcher);
            scoreList = new ArrayList<>();

            isAdapterOn = false;

        }
        else {
            // Set title to View Session
            getSupportActionBar().setTitle("View Session");

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();
            scoreList = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayerScoreList();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + level.getAchievement(intScore, intPlayers).getName());

            totalPlayers.addTextChangedListener(playerNumTextWatcher);
            isAdapterOn = true;

            initializePlayerScores();

        }

        findViewById(R.id.btnSetNumPlayers).setOnClickListener( v -> {
            initializePlayerScores();
        });

        findViewById(R.id.btnUpdateScore).setOnClickListener( v -> {
            updateAchievementAndScore();
        });

    }

    private void updateAchievementAndScore() {

        totalScore.setText("" + adapter.getUpdatedCombinedScore());
        achievement.setText("ACHIEVEMENT is: " + level.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
    }

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

        // Initialize achievement levels
        level = new Achievements(lowScore, highScore, factor);
    }

    private void initializePlayerScores() {

        listView = findViewById(R.id.lvPlayerScores);
        listView.setItemsCanFocus(true);

        while (scoreList.size() < intPlayers) {
            scoreList.add(0);
        }
        while (scoreList.size() > intPlayers) {
            scoreList.remove(scoreList.size()-1);
        }

        adapter = new ListviewAdapter( SessionsActivity.this, scoreList, totalScore, achievement, combinedScore, level, prevNumPlayers, totalPlayers);
        listView.setAdapter(adapter);

        totalScore.setText("" + adapter.getUpdatedCombinedScore());
        achievement.setText("ACHIEVEMENT is: " + level.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());

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
    // Toolbar widgets for save, edit, and delete session
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

                String achievedLevel = level.getAchievement(intScore, intPlayers).getName();

                Log.i("Session Index", Integer.toString(sessionIndex));

                // Creating new session
                if (sessionIndex == -1) {
                    // Create new session and add to List
                    //session = new Session(intPlayers, intScore, playerScoreList);
                    session.setPlayers(intPlayers);
                    session.setTotalScore(intScore);
                    session.setPlayerScoreList(scoreList);


                    session.setAchievementLevel(level.getAchievement(intScore, intPlayers).getName());

                    gameConfiguration.getGame(configIndex).addSession(session);

                    // TODO ADD ACHIEVEMENT IMGS AND LEVEL TO PLACEHOLDER CONGRATS MSG
                    congratsAnimation(congratsImg);

                }
                // Editing existing session
                else {
                    // Replace values in the session
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayers(intPlayers);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setTotalScore(intScore);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayerScoreList(scoreList);
                    gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setAchievementLevel(achievedLevel);
                    finish();
                }

                Toast.makeText(SessionsActivity.this, "" + combinedScore, Toast.LENGTH_SHORT).show();

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

    // Enable/disable text fields for edit session
    private void enableTextFields(EditText editText) {
        editText.setEnabled(true);
        editText.setBackgroundColor(Color.LTGRAY);
        editText.setTextColor(Color.BLUE);

        // Watch for change to display correct achievement level
        totalPlayers.addTextChangedListener(playerNumTextWatcher);

    }

    private void disableTextFields(EditText editText) {
        editText.setEnabled(false);
        editText.setBackgroundColor(Color.BLACK);
        editText.setTextColor(Color.YELLOW);
    }

    // TextWatcher for data fields
    private final TextWatcher playerNumTextWatcher = new TextWatcher() {

        boolean changed = false;

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
        if (text.equals("Easy")) {
            factor = 0.75;
            session.setGameLevel("Easy");
            level = new Achievements(lowScore, highScore, factor);
        } else if (text.equals("Normal")) {
            factor = 1.0;
            session.setGameLevel("Normal");
            level = new Achievements(lowScore, highScore, factor);
        } else if (text.equals("Hard")) {
            factor = 1.25;
            session.setGameLevel("Hard");
            level = new Achievements(lowScore, highScore, factor);
        }
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        if (adapter != null) {
            achievement.setText("ACHIEVEMENT is: " + level.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
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
                .setIcon(R.drawable.empty)
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
