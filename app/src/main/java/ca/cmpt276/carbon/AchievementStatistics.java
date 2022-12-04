package ca.cmpt276.carbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import ca.cmpt276.carbon.model.GameConfig;

public class AchievementStatistics extends AppCompatActivity {

    BarChart barChart;
    //Singleton object
    private GameConfig gameConfiguration;

    public static final String SESSION_INDEX="INDEX";

    private int gameIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_statistics);
        extractDataFromIntent();
        gameConfiguration = GameConfig.getInstance();

        barGraphStatistics();
    }

    //This function manages the required functionality of the Bar Graph
    private void barGraphStatistics()
    {
        int[] countSessions =new int[10];
        for(int i=0;i<countSessions.length;i++) {
            countSessions[i]=0;
        }
        barChart = findViewById(R.id.barGraph);
        ArrayList<BarEntry> barEntries =new ArrayList<>();
        for(int i=0;i<gameConfiguration.getGamesList().get(gameIndex).getSize();i++) {
            int players = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getPlayers();
            int score = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getTotalScore();
            String level = gameConfiguration.getGame(gameIndex).getSessionAtIndex(i).getAchievementLevel().getAchievement(score, players).getName();
            if(level.equals("Odourish Orc")|| level.equals("Swearing Sam") || level.equals("Pleasant Peanut") || level.equals("Level 1"))
            {
                countSessions[0]++;
            }
            else if (level.equals("Greasy Gargoyle")|| level.equals("Crying Crabby") || level.equals("Playful Pistachio") || level.equals("Level 2"))
            {
                countSessions[1]++;
            }
            else if(level.equals("Easy Elf")|| level.equals("Worried Wart") || level.equals("Happy Hazelnut") || level.equals("Level 3"))
            {
                countSessions[2]++;
            }
            else if(level.equals("Viscous Viking")|| level.equals("Bored Bobby") || level.equals("Crafty Cashew") || level.equals("Level 4"))
            {
                countSessions[3]++;
            }
            else if(level.equals("Soulful Spirit")|| level.equals("Smiley Sally") || level.equals("Savvy SoyNut") || level.equals("Level 5"))
            {
                countSessions[4]++;
            }
            else if (level.equals("Humble Human")|| level.equals("Sassy Sarah") || level.equals("Wacky Walnut") || level.equals("Level 6"))
            {
                countSessions[5]++;
            }
            else if(level.equals("Brave Bird")|| level.equals("Nerdy Ned") || level.equals("Crazy CornNut") || level.equals("Level 7"))
            {
                countSessions[6]++;
            }
            else if(level.equals("Shy Sidekick")|| level.equals("Happy Hank") || level.equals("Pretty Pecan") || level.equals("Level 8"))
            {
                countSessions[7]++;
            }
            else if(level.equals("Festive Fairy")|| level.equals("Cool Catherine") || level.equals("Amazing Almond") || level.equals("Level 9"))
            {
                countSessions[8]++;
            }
            else
                countSessions[9]++;
        }
        for(int i=0; i<countSessions.length ;i++){
            int value = countSessions[i];
            BarEntry barEntry = new BarEntry((i+1), value);
            barEntries.add(barEntry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Achievements");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(16f);
        barChart.setFitBars(true);
        barDataSet.setDrawValues(true);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(5000);
        barChart.getDescription().setText("LV1 LV2 LV3 LV4 LV5 LV6 LV7 LV8 LV9 LV10");
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setTextColor(Color.BLACK);
        barChart.getDescription().setTextSize(17.5f);
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        gameIndex= intent.getIntExtra(SESSION_INDEX,0);
    }

    public static Intent makeIntent(Context context,int index) {
        Intent intent = new Intent(context, AchievementStatistics.class);
        intent.putExtra(SESSION_INDEX,index);
        return intent;
    }
}