package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Standings extends AppCompatActivity {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    ArrayList<PlayerInfo> playerListArray = new ArrayList<PlayerInfo>();

    String pattern = "dd MMM yyyy - h:mm:ss a";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    String date = dateFormat.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        PlayerInfo Tyler = new PlayerInfo("Tyler");
        PlayerInfo Mark = new PlayerInfo("Mark");
        playerListArray.add(Tyler);
        playerListArray.add(Mark);
        Tyler.setWins(2);
        Tyler.setLastPlayedGame(date);

        ListView standingsListView = findViewById(R.id.standings_listView);
        PlayerStandingsAdapter adapter = new PlayerStandingsAdapter(this, playerListArray);
        standingsListView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Standings.this, MainActivity.class);
        //intent.putExtra("arrayListToSend", (Serializable) timestampArrayList);
        startActivity(intent);
        finish();
    }

    //reads file and updates ListView in Verification activity
    private void readFile() {

        if (fileExists(SAVED_PLAYER)) {
            FileInputStream fis = null;
            try {
                fis = openFileInput(SAVED_PLAYER);

            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String playerInfo = null;

            try {
                while ((playerInfo = br.readLine()) != null) {
                    //timestampArrayList.add(sLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //checks if file exists
    public boolean fileExists (String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }
}