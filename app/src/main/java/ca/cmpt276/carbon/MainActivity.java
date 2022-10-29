package ca.cmpt276.carbon;

import android.os.Bundle;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GameConfig gameConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameConfiguration=GameConfig.getInstance();
        populateListView();

    }

    private void populateListView() {
        ArrayList<String> gameList=gameConfiguration.gameStr();
        gameList.add("Chess");
        gameList.add("Monopoly");
        //String[] gameList= {"Blue","Green","Purple","Red"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,gameList);
        ListView list = findViewById(R.id.listview);
        list.setAdapter(adapter);
    }
}