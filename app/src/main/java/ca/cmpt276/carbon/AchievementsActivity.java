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
import android.widget.Toast;

import ca.cmpt276.carbon.model.AchievementLevel;
import ca.cmpt276.carbon.model.Achievements;

public class AchievementsActivity extends AppCompatActivity {
    private EditText etNumPlayers;
    private ListView list;
    private int num;
    public static final String EXTRA_LOW_SCORE = "low score";
    public static final String EXTRA_HIGH_SCORE = "high score";
    private int lowScore;
    private int highScore;
    Achievements achievementLvls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        etNumPlayers = findViewById(R.id.etTempNumPlayers);
        etNumPlayers.addTextChangedListener(playerNumWatcher);
        list = (ListView) findViewById(R.id.lvList);

        list.setVisibility(View.INVISIBLE);

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

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            list.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                num = Integer.parseInt(etNumPlayers.getText().toString());

                if (num <= 0) {
                    list.setVisibility(View.INVISIBLE);
                    Toast.makeText(AchievementsActivity.this, "Number of players must" +
                                    "be greater than zero",
                                    Toast.LENGTH_SHORT).show();
                    throw new IllegalArgumentException();
                }
                else {
                    list.setVisibility(View.VISIBLE);
                    populateListView(num);
                }
            }
            catch (Exception ex) {
                list.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void populateListView(int num) {
        String[] achievements = {"Master Macadamia", "Amazing Almond", "Pretty Pecan",
                "Crazy CornNut", "Wacky Walnut", "Savvy Soynut", "Crafty Cashew", "Happy Hazelnut",
                "Playful Pistachio", "Pleasant Peanut"};
        int arrSize = 10;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.achievement_list,
                achievements);

        // "Min score required" text
        achievements[0] += ": Maximum achievement level";
        for (int i = 1; i < arrSize - 1; i++) {
            String index = Integer.toString(arrSize - 1 - i);
            achievements[i] += ": Minimum Score Required: " + (num * achievementLvls.getLevel(index).getMin());
        }
        achievements[9] += ": Minimum achievement level";

        list.setAdapter(adapter);
    }
}