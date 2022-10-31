package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.TextWatcherAdapter;

import ca.cmpt276.carbon.model.Game;
import ca.cmpt276.carbon.model.GameConfig;

public class GameConfigActivity extends AppCompatActivity {
    GameConfig gameConfiguration;
    EditText EditTextname;
    EditText EditTextlowscore;
    EditText EditTexthighscore;
    Game g;
    int low_score;
    int high_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);
        gameConfiguration = GameConfig.getInstance();
        enterData();
    }
    private void enterData() {
        EditTextname = findViewById(R.id.EditTextName);
        EditTextlowscore = findViewById(R.id.EditTextLowScore);
        EditTexthighscore = findViewById(R.id.EditTextHighScore);
        Button btn = findViewById(R.id.SaveButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EditTextname.getText().toString();
                String score1=EditTextlowscore.getText().toString();
                String score2=EditTexthighscore.getText().toString();
                if(!EditTextname.getText().toString().equals("") && !EditTextlowscore.getText().toString().equals(""))
                {
                    low_score = Integer.parseInt(score1);
                    high_score = Integer.parseInt(score2);
                }
                g= new Game(name,low_score,high_score);
                gameConfiguration.insertGame(g);
               Intent intent = new Intent(GameConfigActivity.this, MainActivity.class);
               startActivity(intent);
            }
        });
    }
}
