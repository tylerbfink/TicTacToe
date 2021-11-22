package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME = 4000;
    String currentPlayer = null;

    TextView tic_tac_toe, welcome_back, welcome_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tic_tac_toe = (TextView) findViewById(R.id.header_text);
        welcome_back = (TextView) findViewById(R.id.welcome_player_text);
        welcome_player = (TextView) findViewById(R.id.welcome_player_name);

        //loads previous player if available
        if (ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext()) != null) {
            currentPlayer = ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext());
            welcome_back.setText("Welcome Back:");
            welcome_player.setText(currentPlayer);
        }
        else {
            welcome_back.setText("Welcome");
            welcome_player.setText("New Player!");
        }


        //switches to MainActivity after 4 sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
}