package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangePlayer extends AppCompatActivity {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    String name = "Tyler";
    int wins = 0;
    int playedGames = 0;
    String lastPlayedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_player);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePlayer.this, MainActivity.class);
        //intent.putExtra("arrayListToSend", (Serializable) timestampArrayList);
        startActivity(intent);
        finish();
    }

    //writes player data to file
    private void writeFile() {
        try {
            FileOutputStream fos = openFileOutput(SAVED_PLAYER, MODE_PRIVATE | MODE_APPEND);

            //build name/wins/games played/last play date string
            String pattern = "dd MMM yyyy - h:mm:ss a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            lastPlayedGame = dateFormat.format(new Date());

            String saveInfo = name + "," + wins + "," + playedGames + "," + lastPlayedGame;

            fos.write(saveInfo.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}