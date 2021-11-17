package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Standings extends AppCompatActivity {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    ArrayList<Player> playerListArray = new ArrayList<Player>();

    String pattern = "dd MMM yyyy - h:mm:ss a";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    String date = dateFormat.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        readFile();

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

            String sLine = null;

            try {
                while ((sLine = br.readLine()) != null) {
                    String[] tempPlayer = sLine.split(",");
                    playerListArray.add(0, new Player(tempPlayer[0]));
                    playerListArray.get(0).wins = Integer.valueOf(tempPlayer[1]);
                    playerListArray.get(0).playedGames = Integer.valueOf(tempPlayer[2]);
                    playerListArray.get(0).lastPlayedGame = tempPlayer[3];
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