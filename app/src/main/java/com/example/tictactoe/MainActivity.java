package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    String currentPlayer = "New Player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load listView with options
        ListView mainMenuOptions = findViewById(R.id.option_listView);
        Resources res = getResources();
        String[] options = res.getStringArray(R.array.main_menu_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_view, options);
        mainMenuOptions.setAdapter(adapter);

        TextView welcomeBack = (TextView) findViewById(R.id.welcome_back_text);

        if (ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext()) != null) {
            currentPlayer = ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext());
        }
        welcomeBack.setText("Player: " + currentPlayer);


        //listener for click of listView item
        mainMenuOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, GamePlay.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, ChangePlayer.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, Standings.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, Options.class);
                        break;
                }
                startActivity(intent);
                finish();
            }
        });
    }


}

