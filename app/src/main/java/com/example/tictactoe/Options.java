package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Options extends AppCompatActivity {

    String playerTwoName = "Not Selected";
    int playerTwoID;

    boolean twoPlayer = false;

    SwitchCompat two_player;
    Button use_selected;
    TextView player_two_text;

    PlayerIO playerData = new PlayerIO(); //new instance of player input/output
    ArrayList<Player> playerListArray; //common app array that holds players & stats

    PlayerNamesAdapter adapter;
    ListView playerListView;
    int listViewSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        playerListView = findViewById(R.id.existing_player_list);
        use_selected = (Button) findViewById(R.id.use_selected);

        player_two_text = (TextView) findViewById(R.id.player_two_text);
        player_two_text.setText("Player Two: " + playerTwoName);

        two_player = (SwitchCompat) findViewById(R.id.two_player_switch);

        //restores two player switch position
        if (getPlayerTwoSelected("TWO_PLAYER", getBaseContext()) == true) {
            //retrieves players from saved data file
            playerListArray = playerData.readFile(getApplicationContext());

            two_player.performClick();
            use_selected.setVisibility(View.VISIBLE);

            twoPlayer = true;

            //builds player listView
            adapter = new PlayerNamesAdapter(getBaseContext(), playerListArray);
            playerListView.setAdapter(adapter);
            playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listViewSelected = i;
                    playerTwoName = playerListArray.get(i).getName();
                    playerTwoID = playerListArray.get(i).getPlayerID();
                }
            });
        }

        //retrieves player two if selected
        if (getPlayerTwoName("PLAYER_TWO_NAME", getBaseContext()) != null) {
            playerTwoName = getPlayerTwoName("PLAYER_TWO_NAME", getBaseContext());
            player_two_text.setText("Player Two: " + playerTwoName);
        }

        //two player switch listener
        two_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (two_player.isChecked()){
                    use_selected.setVisibility(View.VISIBLE);
                    twoPlayer = true;

                    //retrieves players from saved data file
                    playerListArray = playerData.readFile(getApplicationContext());

                    //builds player listView
                    adapter = new PlayerNamesAdapter(getBaseContext(), playerListArray);
                    playerListView.setAdapter(adapter);
                    playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            listViewSelected = i;
                            playerTwoName = playerListArray.get(i).getName();
                            playerTwoID = playerListArray.get(i).getPlayerID();
                        }
                    });
                }

                else {
                    twoPlayer = false;

                    //clears player listView
                    playerListArray.clear();
                    adapter = new PlayerNamesAdapter(getBaseContext(), playerListArray);
                    playerListView.setAdapter(adapter);

                    use_selected.setVisibility(View.INVISIBLE);
                }

                saveTwoPlayerOn("TWO_PLAYER", twoPlayer, getBaseContext());
            }
        });

        //use button selected listener
        use_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewSelected != -1) {
                    savePlayerTwo("PLAYER_TWO_NAME", playerTwoName,
                            "PLAYER_TWO_ID", playerTwoID, getBaseContext());

                    player_two_text.setText("Player Two: " + playerTwoName);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select a player first!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //saves player name and ID in sharedPreference
    public static void savePlayerTwo(String playerTwoNameKey, String playerTwoName,
                                  String playerTwoIDKey, int playerTwoID, Context context) {
        SharedPreferences playerNameTwoShared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences playerIDTwoShared = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor playerNameEditor = playerNameTwoShared.edit();
        SharedPreferences.Editor playerIDEditor = playerIDTwoShared.edit();

        playerNameEditor.putString(playerTwoNameKey, playerTwoName);
        playerIDEditor.putInt(playerTwoIDKey, playerTwoID);

        playerNameEditor.commit();
        playerIDEditor.commit();
    }

    //saves if two player on in sharedPreference
    public static void saveTwoPlayerOn(String twoPlayerKey, Boolean twoPLayer, Context context) {
        SharedPreferences twoPlayerShared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor twoPlayerEditor = twoPlayerShared.edit();
        twoPlayerEditor.putBoolean(twoPlayerKey, twoPLayer);
        twoPlayerEditor.commit();
    }

    //retrieves if two player selected
    public static Boolean getPlayerTwoSelected(String key, Context context) {
        SharedPreferences playerNameTwoShared = PreferenceManager.getDefaultSharedPreferences(context);
        return playerNameTwoShared.getBoolean(key, false );
    }

    //retrieves selected user name
    public static String getPlayerTwoName(String key, Context context) {
        SharedPreferences playerNameTwoShared = PreferenceManager.getDefaultSharedPreferences(context);
        return playerNameTwoShared.getString(key, null );
    }

    //retrieves selected user ID
    public static int getPlayerTwoID(String key, Context context) {
        SharedPreferences playerTwoIDShared = PreferenceManager.getDefaultSharedPreferences(context);
        return playerTwoIDShared.getInt(key, 0 );
    }

    @Override
    public void onBackPressed() {
        String tempPlayer = "";

        if (getPlayerTwoName("PLAYER_TWO_NAME", getBaseContext()) != null) {
            tempPlayer = getPlayerTwoName("PLAYER_TWO_NAME", getBaseContext());
        }

        if (twoPlayer != true || (twoPlayer == true && tempPlayer.equals(playerTwoName))) {
            Intent intent = new Intent(Options.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),"Select player or turn off two player first!",
                    Toast.LENGTH_LONG).show();
        }
    }
}