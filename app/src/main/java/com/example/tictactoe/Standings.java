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

    PlayerIO playerData = new PlayerIO(); //new instance of player input/output
    ArrayList<Player> playerListArray = new ArrayList();

    //parser for last played date
    String pattern = "dd MMM yyyy - h:mm:ss a";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    String date = dateFormat.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        //retrieves players from saved data file
        playerListArray = playerData.readFile(getApplicationContext());

        //fills standings listView
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
}