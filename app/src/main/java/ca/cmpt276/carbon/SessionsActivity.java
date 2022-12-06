package ca.cmpt276.carbon;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    // Constants
    private static final int PERMISSION_CODE = 200;     // Camera permission code
    private static final int CAMERA_REQUEST_CODE = 201; // Camera request code

    // Variables
    private int sessionIndex;                           // For add/edit sessions
    private int configIndex;                            // Game index of gameConfig
    private EditText totalPlayers;                      // Total players of a single session
    private EditText totalScore;                        // Total score of all players in a session
    private TextView achievement;                       // Display achievement of player
    private Button takePhotoBtn;                        // Button for taking photos
    private Uri sessions_image_uri;                     // Storage for photo taken
    private Uri current_image_uri;                      // Current photo taken
    private boolean isPhotoTaken;                       // Check if a photo was taken
    private ImageView sessionPhoto;                     // Current image for the session

    private int lowScore;                               // Low score of game
    private int highScore;                              // High score of game
    private int intPlayers;                             // Integer of total players
    private int intScore;                               // Integer of total score
    private double factor;                              // Double of factor for difficulty
    private String stringPlayers;                       // String of total players
    private String stringScore;                         // String of total score

    // Dropdown variables
    private String achievementTheme;                    // Theme for Achievement in Session
    private String theme;                               // Theme for Session
    private String difficulty;                          // Difficulty for Session

    // Objects
    private Session session;                            // Session for add session
    private Achievements currentAchievement;            // Current Achievement of session

    // Singleton
    private GameConfig gameConfiguration;

    private int combinedScore;                          // Combined score of all players
    private int prevNumPlayers;

    private List<Integer> scoreList;                    // List of score of players
    private List<Integer> tempScoreList;                // Temporary list of scores for num player changes
    private List<Integer> currScoreList;                // List of current score of players
    private List<Integer> oldScoreList;                 // List of old score of players
    private ListView listView;                          // ListView of score of players
    private ListviewAdapter adapter;                    // Adapter for listView


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

        oldScoreList = new ArrayList<>();
        tempScoreList = new ArrayList<>();

        // If index is -1, go to add new session screen
        if (sessionIndex == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Watch for change to display correct achievement level
            totalScore.addTextChangedListener(playerNumTextWatcher);
            totalPlayers.addTextChangedListener(playerNumTextWatcher);
            scoreList = new ArrayList<>();

            // set default players the first time you create the session
            intPlayers = 1;
            intScore = 0;
            totalPlayers.setText("" + 2);

            // initialize achievement
            currentAchievement = new Achievements(lowScore, highScore, factor);
            currentAchievement.setTheme(achievementTheme);

            // display default player count, scores and achievement
            initializePlayerScores();
            updateAchievementAndScore();
        }
        else {
            // Set title to Edit Session
            getSupportActionBar().setTitle("Edit Session");
            session = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex);

            // Populate fields
            intPlayers = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers();
            intScore = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore();
            currScoreList = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayerScoreList();
            scoreList = gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayerScoreList();
            oldScoreList.addAll(currScoreList);
            tempScoreList.addAll(currScoreList);

            currentAchievement = session.getAchievementLevel();

            totalPlayers.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getPlayers()));
            totalScore.setText(Integer.toString(gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).getTotalScore()));
            achievement.setText("ACHIEVEMENT: " + currentAchievement.getAchievement(intScore, intPlayers).getName());

            totalPlayers.addTextChangedListener(playerNumTextWatcher);

            initializePlayerScoresOld();

            // Check if a photo was taken
            if (session.isPhotoTaken()) {
                sessions_image_uri = session.getPhoto();
                sessionPhoto.setImageURI(sessions_image_uri);
                isPhotoTaken = true;
            }

            // Populate dropdown fields
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

    private void initializePlayerScoresOld() {

        listView = findViewById(R.id.lvPlayerScores);
        listView.setItemsCanFocus(true);

        adapter = new ListviewAdapter(SessionsActivity.this, currScoreList, tempScoreList, totalScore, achievement, combinedScore, session.getAchievementLevel(), prevNumPlayers, totalPlayers);
        listView.setAdapter(adapter);

        int oldScore = 0;

        for (int i = 0; i < currScoreList.size(); i++) {
            oldScore += currScoreList.get(i);
        }

        totalScore.setText("" + oldScore);
        achievement.setText("ACHIEVEMENT is: " + currentAchievement.getAchievement(oldScore, intPlayers).getName());
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
        sessionPhoto = findViewById(R.id.imageViewSessionsImage);

        // Get Intent
        Intent i = getIntent();
        sessionIndex = i.getIntExtra("SESSION_INDEX", -1);
        configIndex = i.getIntExtra("GAME_INDEX", -1);
        lowScore = i.getIntExtra("LOW_SCORE", -1);
        highScore = i.getIntExtra("HIGH_SCORE", -1);

        achievementTheme = Achievements.NONE;
        factor = 1.0;

        // Initialize achievement levels
        session.setAchievementLevel(new Achievements(lowScore, highScore, factor));

        // Initialize photo button
        takePhotoBtn = findViewById(R.id.sessionsTakePhotoBtn);
        isPhotoTaken = false;
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraPermissions();
            }
        });
    }
    private void initializePlayerScores() {

        if (totalPlayers.getText().toString().isEmpty()) {
            Toast.makeText(SessionsActivity.this, "Number of players can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (intPlayers < 1 || intPlayers > 25) {
                Toast.makeText(SessionsActivity.this, "Number of players must be between 1 and 25", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        listView = findViewById(R.id.lvPlayerScores);
        listView.setItemsCanFocus(true);

        if (tempScoreList.size() > scoreList.size()) {
            scoreList.clear();
            scoreList.addAll(tempScoreList);
        }

        while(tempScoreList.size() < intPlayers) {
            tempScoreList.add(0);
        }

        for (int i = 0; i < tempScoreList.size(); i++) {
            System.out.println("" + tempScoreList.get(i));
        }

        while (scoreList.size() < intPlayers) {
            scoreList.add(0);
        }
        while (scoreList.size() > intPlayers) {
            scoreList.remove(scoreList.size()-1);
        }

        adapter = new ListviewAdapter(SessionsActivity.this, scoreList, tempScoreList, totalScore, achievement, combinedScore, session.getAchievementLevel(), prevNumPlayers, totalPlayers);
        listView.setAdapter(adapter);
        totalScore.setText("" + adapter.getUpdatedCombinedScore());
        achievement.setText("ACHIEVEMENT is: " + currentAchievement.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
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
        achievement.setText("ACHIEVEMENT is: " + currentAchievement.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
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
                revertUnsavedChanged();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void revertUnsavedChanged() {

        if (oldScoreList.size() != 0) {
            gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPlayerScoreList(oldScoreList);
        }
    }

    // Toolbar widget helper methods
    private void saveSession() {
        try {
            if (!totalPlayers.getText().toString().isEmpty()) {

                combinedScore = 0;
                for (int i = 0; i < scoreList.size(); i++) {
                    if (scoreList.get(i) == null) {
                        throw new IllegalArgumentException();
                    }
                }

                stringPlayers = totalPlayers.getText().toString();

                intPlayers = Integer.parseInt(stringPlayers);

                if (intPlayers > 25) {
                    throw new IllegalArgumentException("Can't have more than 25 players");
                }
                if (intPlayers < 1) {
                    throw new IllegalArgumentException("Can't have less than 1 player");
                }
                int scoreListSize = scoreList.size();
                if (intPlayers != scoreListSize) {
                    throw new IllegalArgumentException("The player size was not set");
                }

                for (int i = 0; i < intPlayers; i++) {
                    combinedScore += scoreList.get(i);
                }

                // clear the old list after using it for creation
                tempScoreList.clear();

                intScore = combinedScore;

                // Creating new session
                if (sessionIndex == -1) {
                    // Create new session and add to List
                    session.setPlayers(intPlayers);
                    session.setTotalScore(intScore);
                    session.setPlayerScoreList(scoreList);
                    session.setSessionTheme(theme);
                    session.setSessionDifficulty(difficulty);
                    session.setAchievementLevel(currentAchievement);

                    // Check if image was taken
                    if (isPhotoTaken) {
                        session.setPhoto(sessions_image_uri);
                        session.setPhotoTaken(true);
                    }

                    gameConfiguration.getGame(configIndex).addSession(session);

                    // Play congratulations message
                    openCelebrationActivity();
                    finish();
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

                    // Check if a new photo was taken
                    if (isPhotoTaken) {
                        gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPhoto(sessions_image_uri);
                        gameConfiguration.getGame(configIndex).getSessionAtIndex(sessionIndex).setPhotoTaken(true);
                    }
                    finish();
                }

            } else {
                Toast.makeText(SessionsActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(SessionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        currentAchievement = new Achievements(lowScore, highScore, factor);
        currentAchievement.setTheme(achievementTheme);

        if (adapter != null) {
            achievement.setText("ACHIEVEMENT is: " + currentAchievement.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers).getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing here
    }

    private void openCelebrationActivity() {
        Intent intent = new Intent(SessionsActivity.this, CelebrationActivity.class);

        // Send current achievement object to celebration class and start the activity
        intent.putExtra("ACHIEVEMENT", currentAchievement);
        intent.putExtra("LEVEL", currentAchievement.getAchievement(adapter.getUpdatedCombinedScore(), intPlayers));
        intent.putExtra("SCORE", combinedScore);
        intent.putExtra("PLAYERS", intPlayers);
        intent.putExtra("GAME_INDEX", configIndex);
        intent.putExtra("SESSION_INDEX", sessionIndex);
        startActivity(intent);
    }

    // Get camera permissions
    private void getCameraPermissions() {
        // Check for if camera permission is granted
        // Else, open camera
        if (ContextCompat.checkSelfPermission(SessionsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SessionsActivity.this, new String[]{
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
            if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                sessions_image_uri = current_image_uri;
                sessionPhoto.setImageURI(sessions_image_uri);
                isPhotoTaken = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
