package ca.cmpt276.carbon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.carbon.model.GameConfig;
import ca.cmpt276.carbon.model.Session;

public class SessionsActivity extends AppCompatActivity {

    // Variables
    private int index;                  // For add/edit sessions
    private int configIndex;            // Game index of gameConfig
    private EditText totalPlayers;      // Total players of a single session
    private EditText totalScore;        // Total score of all players in a session
    private Button saveBtn;             // Save button for saving data to List
    private int intPlayers;             // Integer of total players
    private int intScore;               // Integer of total score

    // Objects
    private Session session;            // Session for add session

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


        // Get Intent
        Intent i = getIntent();
        index = i.getIntExtra("SESSION_INDEX", 0);
        configIndex = i.getIntExtra("gameIndex: ", -1);

        // If index is -1, go to add new session screen
        if (index == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");

            // Create new Session
            session = new Session();

            // Get fields
            totalPlayers = findViewById(R.id.totalPlayers);
            totalScore = findViewById(R.id.totalScore);

            saveBtn = findViewById(R.id.saveSessionBtn);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String players = totalPlayers.getText().toString();
                    String score = totalScore.getText().toString();

                    if (!totalPlayers.getText().toString().equals("") && !totalScore.getText().toString().equals("")) {
                        intPlayers = Integer.parseInt(players);
                        intScore = Integer.parseInt(score);
                    }

                    // Create new session and add to List
                    session = new Session(intPlayers, intScore);
                    gameConfiguration.getGame(configIndex).addSession(session);

                    finish();
                }
            });



        }
        else {
            // Set title to Edit Session
            getSupportActionBar().setTitle("Edit Session");
        }

    }

}
