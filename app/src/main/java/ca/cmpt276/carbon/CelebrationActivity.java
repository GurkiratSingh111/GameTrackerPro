package ca.cmpt276.carbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ca.cmpt276.carbon.model.AchievementLevel;
import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

public class CelebrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView congratsImg;
    private MediaPlayer congratsSound;
    private Achievements achievement;
    private AchievementLevel intentLevel;
    private TextView tvCurAch;
    private TextView tvNextAch;
    private int levelID;
    private int score;
    private int numOfPlayers;
    GameConfig gameConfig;
    private int gameIndex;
    private int sessionIndex;
    private Session curSession;
    Spinner spThemes;
    private ImageView imgCurAch;
    private ImageView imgNextAch;
    private Button btnAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration);

        gameConfig = GameConfig.getInstance();
        curSession = gameConfig.getGame(gameIndex).getSessionAtIndex(sessionIndex);

        // Receive intent
        Intent intent = getIntent();
        achievement = curSession.getAchievementLevel();
        intentLevel = (AchievementLevel) intent.getExtras().getSerializable("LEVEL");
        score = (int) intent.getIntExtra("SCORE", 0);
        numOfPlayers = (int) intent.getIntExtra("PLAYERS", 0);
        levelID = intentLevel.getId();

        // Enable toolbar
        Toolbar toolbar = findViewById(R.id.tbCelebration);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Let's Celebrate!");

        // Dropdown
        initializeDropdown();

        // Initialize Congratulations pop-up
        congratsImg = findViewById(R.id.imgCongrats);

        // Initialize Textviews
        tvCurAch = findViewById(R.id.tvCurAch);
        tvNextAch = findViewById(R.id.tvNextAch);

        // Initialize ImageViews
        imgCurAch = findViewById(R.id.imgCurAch);
        imgNextAch = findViewById(R.id.imgNextAch);

        congratsAnimation(congratsImg);
        setInfo();

        // Initialize button
        findViewById(R.id.btnAnimation).setOnClickListener(v -> {
            congratsAnimation(congratsImg);
        });
    }

    private void initializeDropdown() {
        spThemes = findViewById(R.id.spThemeCeleb);
        ArrayAdapter<CharSequence> adapterThemes = ArrayAdapter.createFromResource(this,R.array.themes,
                android.R.layout.simple_spinner_item);
        adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThemes.setAdapter(adapterThemes);
        if (achievement.getTheme().equals(Achievements.NONE)) {
            spThemes.setSelection(0);
        }
        else if (achievement.getTheme().equals(Achievements.NUT)) {
            spThemes.setSelection(1);
        }
        else if (achievement.getTheme().equals(Achievements.EMOJI)) {
            spThemes.setSelection(2);
        }
        else if (achievement.getTheme().equals(Achievements.MIDDLE_EARTH)){
            spThemes.setSelection(3);
        }
        spThemes.setOnItemSelectedListener(this);
    }

    private void changeSessionTheme(String theme) {
        curSession.setSessionTheme(theme);
        curSession.getAchievementLevel().setTheme(theme);
    }

    @Override
    // Toolbar widgets for save and delete session
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setText() {
        tvCurAch.setText("You've achieved " + achievement.getLevelByID(levelID).getName() +
                " with a score of " + score);

        if (levelID >= 9) {
            tvNextAch.setText("Maximum level achieved! Great job!");
        }
        else {
            tvNextAch.setText("You need " + calculatePointsNeeded() + " more point(s) for next " +
                    "achievement " + achievement.getLevelByID(levelID + 1).getName());
        }
    }

    private void setImg() {
        imgCurAch.setImageResource(achievement.getLevelByID(levelID).getImage());
        if (levelID < 9) {
            imgNextAch.setImageResource(achievement.getLevelByID(levelID + 1).getImage());
        }
        else {
            imgNextAch.setImageResource(R.drawable.empty);
        }
    }

    private void setInfo() {
        setImg();
        setText();
    }

    private double calculatePointsNeeded() {
        double nextScore = achievement.getLevelByID(levelID + 1).getMin() * numOfPlayers;
        return nextScore - score;
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

    private void congratsAnimation(ImageView img) {
        fadeAnimation(img);
        bounceAnimation(img);
        playSound();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        // Theme
        if (text.equals("Nut")) {
            achievement.setTheme(Achievements.NUT);
            curSession.setSessionTheme((Achievements.NUT));

        }
        else if (text.equals("Emoji")) {
            achievement.setTheme(Achievements.EMOJI);
            curSession.setSessionTheme((Achievements.EMOJI));
        }
        else if (text.equals("Middle Earth")) {
            achievement.setTheme(Achievements.MIDDLE_EARTH);
            curSession.setSessionTheme((Achievements.MIDDLE_EARTH));
        }
        else if (text.equals("None")) {
            achievement.setTheme(Achievements.NONE);
            curSession.setSessionTheme((Achievements.NONE));
        }
        setInfo();
        changeSessionTheme(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Unused
    }
}