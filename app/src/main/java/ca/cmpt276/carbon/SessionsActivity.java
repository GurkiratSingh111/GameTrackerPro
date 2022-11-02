package ca.cmpt276.carbon;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SessionsActivity extends AppCompatActivity {

    // Index passed by GameConfigActivity on creating new/editing existing session
    private int index;

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

        // TODO - get extraIntent on whether it is add/edit session
        // Get Intent
        Intent i = getIntent();
        index = i.getIntExtra("SESSION_INDEX", 0);

        // If index is -1, go to add new session screen
        if (index == -1) {
            // Set title to New Session
            getSupportActionBar().setTitle("New Session");
        }
        else {
            // Set title to Edit Session
            getSupportActionBar().setTitle("Edit Session");
        }

    }
}
