package ca.cmpt276.carbon;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmpt276.carbon.databinding.ActivityMainBinding;
import ca.cmpt276.carbon.model.GameConfig;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GameConfig gameConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameConfiguration=GameConfig.getInstance();
        populateListView();
        setupPlusButton();

    }

//    private void clickGameList() {
//        ListView list = findViewById(R.id.listview);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                TextView textView = (TextView) viewClicked;
//                String message = "You clicked # " + position + ", which is string: " + textView.getText().toString();
//                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
//                Intent intent = GameConfigActivity.makeIntent(MainActivity.this,gameConfiguration.getGamesList().get(position));
//                startActivity(intent);
//            }
//        });
//    }

    private void setupPlusButton() {
        FloatingActionButton btn = findViewById(R.id.floatingBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,GameConfigActivity.class);
                startActivity(intent);
            }
        });
    }


    private void populateListView() {
        ArrayList<String> gameList=gameConfiguration.gameStr();
        //String[] gameList= {"Blue","Green","Purple","Red"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,gameList);
        ListView list = findViewById(R.id.listview);
        list.setAdapter(adapter);
    }
}