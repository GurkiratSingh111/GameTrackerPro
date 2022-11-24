package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import ca.cmpt276.carbon.model.Achievements;

/**
 *This activity stores the list of achievements
 */
public class AchievementsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText etNumPlayers;
    private ListView list;
    private int num;
    public static final String EXTRA_LOW_SCORE = "low score";
    public static final String EXTRA_HIGH_SCORE = "high score";
    private int lowScore;
    private int highScore;
    Achievements achievementLvls;
    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("#.##");
    private double factor = 1;
    TextView[] pointsArray;
    TextView[] titleArray;
    ImageView[] imageArray;
    Spinner spDiff;
    Spinner spThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        // Dropdown menus
        spDiff = findViewById(R.id.spDiff);
        ArrayAdapter<CharSequence> adapterDiff = ArrayAdapter.createFromResource(this,R.array.levels,
                android.R.layout.simple_spinner_item);
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDiff.setAdapter(adapterDiff);
        spDiff.setOnItemSelectedListener(this);

        spThemes = findViewById(R.id.spTheme);
        ArrayAdapter<CharSequence> adapterThemes = ArrayAdapter.createFromResource(this,R.array.themes,
                android.R.layout.simple_spinner_item);
        adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThemes.setAdapter(adapterThemes);
        spThemes.setOnItemSelectedListener(this);

        // Intent
        Intent i = getIntent();
        lowScore = i.getIntExtra(EXTRA_LOW_SCORE, 0);
        highScore = i.getIntExtra(EXTRA_HIGH_SCORE, 0);

        // Create achievement object
        etNumPlayers = findViewById(R.id.etTempNumPlayers);
        etNumPlayers.addTextChangedListener(playerNumWatcher);
        achievementLvls = new Achievements(lowScore, highScore, factor);

        // make an array of text views for the levels
        makePointsArray();
        makeTitleArray();
        makeImageArray();
        setTitleArray();
        setImageArray();
    }

    public static Intent makeLaunchIntent(Context c, int lowScore, int highScore) {
        Intent intent = new Intent(c, AchievementsActivity.class);
        intent.putExtra(EXTRA_LOW_SCORE, lowScore);
        intent.putExtra(EXTRA_HIGH_SCORE, highScore);
        return intent;
    }

    TextWatcher playerNumWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed. Do not implement.
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                if (etNumPlayers.getText().toString().equals("")) {
                    num = 0;
                    updatePoints();
                }
                num = Integer.parseInt(etNumPlayers.getText().toString());

                if (num <= 0) {
                    Toast.makeText(AchievementsActivity.this, "Number of players must" +
                                    " be greater than zero",
                            Toast.LENGTH_SHORT).show();
                    throw new IllegalArgumentException();
                }
                else {
                    achievementLvls.setFactor(factor);
                    updatePoints();
                }
            }
            catch (Exception ex) {
                // Watch text, do nothing.
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed. Do not implement.
        }
    };

    private void updatePoints() {
        // Set the lowest level as low score
        pointsArray[0].setText(REAL_FORMATTER.format(num * achievementLvls.returnLowScore(factor)));

        // set the middle 8 levels as required
        for (int i = 1; i < 9; i++) {
            String index = Integer.toString(i);
            pointsArray[i].setText(REAL_FORMATTER.format(num * achievementLvls.getLevel(index).getMin()));
        }

        // set the highest level as high score
        pointsArray[9].setText(REAL_FORMATTER.format(num * achievementLvls.returnHighScore(factor)));
    }

    // Sets the titles
    private void setTitleArray() {
        titleArray[9].setText(achievementLvls.getLevel("MAX").getName() + ": > ");
        titleArray[0].setText(achievementLvls.getLevel("MIN").getName() + ": < ");
        for (int i = 1; i <= 8; i++) {
            titleArray[i].setText(achievementLvls.getLevel("" + i).getName() + ": >= ");
        }
    }

    // Initializes array with ids
    private void makeImageArray() {
        imageArray = new ImageView[] {
            (ImageView) findViewById(R.id.imgLvl1),
            (ImageView) findViewById(R.id.imgLvl2),
            (ImageView) findViewById(R.id.imgLvl3),
            (ImageView) findViewById(R.id.imgLvl4),
            (ImageView) findViewById(R.id.imgLvl5),
            (ImageView) findViewById(R.id.imgLvl6),
            (ImageView) findViewById(R.id.imgLvl7),
            (ImageView) findViewById(R.id.imgLvl8),
            (ImageView) findViewById(R.id.imgLvl9),
            (ImageView) findViewById(R.id.imgLvl10)
        };
    }

    // Sets the images
    private void setImageArray() {
        imageArray[0].setImageResource(achievementLvls.getLevel("MIN").getImage());
        imageArray[9].setImageResource(achievementLvls.getLevel("MAX").getImage());
        for (int i = 1; i <= 8; i++) {
            imageArray[i].setImageResource(achievementLvls.getLevel("" + i).getImage());
        }
    }

    // Initializes array with ids
    private void makePointsArray() {
        pointsArray = new TextView[] {
                (TextView) findViewById(R.id.tvDisplayScoreLvl1),
                (TextView) findViewById(R.id.tvDisplayScoreLvl2),
                (TextView) findViewById(R.id.tvDisplayScoreLvl3),
                (TextView) findViewById(R.id.tvDisplayScoreLvl4),
                (TextView) findViewById(R.id.tvDisplayScoreLvl5),
                (TextView) findViewById(R.id.tvDisplayScoreLvl6),
                (TextView) findViewById(R.id.tvDisplayScoreLvl7),
                (TextView) findViewById(R.id.tvDisplayScoreLvl8),
                (TextView) findViewById(R.id.tvDisplayScoreLvl9),
                (TextView) findViewById(R.id.tvDisplayScoreLvl10) };
    }

    // Initializes array with ids
    private void makeTitleArray() {
        titleArray = new TextView[] {
                (TextView) findViewById(R.id.tvLvl1),
                (TextView) findViewById(R.id.tvLvl2),
                (TextView) findViewById(R.id.tvLvl3),
                (TextView) findViewById(R.id.tvLvl4),
                (TextView) findViewById(R.id.tvLvl5),
                (TextView) findViewById(R.id.tvLvl6),
                (TextView) findViewById(R.id.tvLvl7),
                (TextView) findViewById(R.id.tvLvl8),
                (TextView) findViewById(R.id.tvLvl9),
                (TextView) findViewById(R.id.tvLvl10) };
    }

    // DROPDOWN MENU
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        // Difficulty
        if (text.equals("Normal")) {
            factor = 1;
        }
        else if (text.equals("Easy")) {
            factor= 0.75;
        }
        else if (text.equals("Hard")) {
            factor= 1.25;
        }
        achievementLvls.setFactor(factor);
        updatePoints();

        // Theme
        if (text.equals("Nut")) {
            achievementLvls.setTheme(Achievements.NUT);
        }
        else if (text.equals("Emoji")) {
            achievementLvls.setTheme(Achievements.EMOJI);
        }
        else if (text.equals("Middle Earth")) {
            achievementLvls.setTheme(Achievements.MIDDLE_EARTH);
        }
        else if (text.equals("None")) {
            achievementLvls.setTheme(Achievements.NONE);
        }
        setImageArray();
        setTitleArray();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing here
    }
}