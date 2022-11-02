package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ca.cmpt276.carbon.model.AchievementLevel;

public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        populateListView();
    }

    public static Intent makeLaunchIntent(Context c) {
        Intent intent = new Intent(c, AchievementsActivity.class);
        return intent;
    }

    private void populateListView() {
        String[] achievements = {"Master Macadamia", "Amazing Almond", "Pretty Pecan",
                "Crazy CornNut", "Wacky Walnut", "Savvy Soynut", "Crafty Cashew", "Happy Hazelnut",
                "Playful Pistachio", "Pleasant Peanut"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.achievement_list,
                achievements);

        ListView list = (ListView) findViewById(R.id.lvList);
        list.setAdapter(adapter);
    }
}