package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.carbon.model.AchievementLevel;
import ca.cmpt276.carbon.model.Achievements;
import ca.cmpt276.carbon.model.GameConfig;

public class AchievementsActivity extends AppCompatActivity {
    private EditText etNumPlayers;
    private ListView list;
    private int num;
    public static final String EXTRA_LOW_SCORE = "low score";
    public static final String EXTRA_HIGH_SCORE = "high score";
    private int lowScore;
    private int highScore;
    Achievements achievementLvls;

    TextView[] textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        etNumPlayers = findViewById(R.id.etTempNumPlayers);
        etNumPlayers.addTextChangedListener(playerNumWatcher);

        // make an array of text views for the levels
        makeTextViewArray();

        Intent i = getIntent();
        lowScore = i.getIntExtra(EXTRA_LOW_SCORE, 0);
        highScore = i.getIntExtra(EXTRA_HIGH_SCORE, 0);

        achievementLvls = new Achievements(lowScore, highScore);
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
                num = Integer.parseInt(etNumPlayers.getText().toString());

                if (num <= 0) {
                    Toast.makeText(AchievementsActivity.this, "Number of players must" +
                                    " be greater than zero",
                            Toast.LENGTH_SHORT).show();
                    throw new IllegalArgumentException();
                }
                else {
                    populateTextView();
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

    private void populateTextView() {

        // Set the lowest level as low score
        textViewArray[0].setText("" + num * achievementLvls.getLowScore() );

        // set the middle 8 levels as required
        for (int i = 1; i < 9; i++) {
            String index = Integer.toString(i);
            textViewArray[i].setText("" + num * achievementLvls.getLevel(index).getMin());
        }

        // set the highest level as high score
        textViewArray[9].setText("" + num * achievementLvls.getHighScore() );
    }

    private void makeTextViewArray() {
        textViewArray = new TextView[] {
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

}