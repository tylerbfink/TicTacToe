package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChangePlayer extends AppCompatActivity
        implements NewPlayerDialog.DialogListener {

    String currentPlayerName;
    int currentPlayerID;

    PlayerIO playerData = new PlayerIO(); //new instance of player input/output
    ArrayList<Player> playerListArray; //common app array that holds players & stats

    Button add_player, remove_player, use_selected;

    AlertDialog.Builder deleteAlert;

    PlayerNamesAdapter adapter;
    ListView playerListView;
    int listViewSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_player);

        //retrieves players from saved data file
        playerListArray = playerData.readFile(getApplicationContext());

        //builds player listView
        playerListView = findViewById(R.id.existing_player_list);
        adapter = new PlayerNamesAdapter(this, playerListArray);
        playerListView.setAdapter(adapter);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewSelected = i;
                currentPlayerName = playerListArray.get(i).getName();
                currentPlayerID = playerListArray.get(i).getPlayerID();
            }
        });

        //use selected listener
        use_selected = (Button) findViewById(R.id.use_selected);
        use_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewSelected != -1) {
                    savePlayer("CURRENT_PLAYER", currentPlayerName,
                            "CURRENT_ID", currentPlayerID, getBaseContext());

                    onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select a player first!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //add player listener
        add_player = (Button) findViewById(R.id.add_player);
        add_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPlayer();
            }
        });

        //pop up alert to confirm delete player
        deleteAlert = new AlertDialog.Builder(this);
        remove_player = (Button) findViewById(R.id.remove_player);
        remove_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlert.setMessage(R.string.confirm_sure)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (listViewSelected != -1) {
                                    String nameToDelete = playerListArray.get(listViewSelected).getName();
                                    Toast.makeText(getApplicationContext(), nameToDelete + " deleted!",
                                            Toast.LENGTH_SHORT).show();
                                    playerListArray.remove(listViewSelected);
                                    playerData.writeFile(getApplicationContext(), playerListArray);
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Select a player first.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                deleteAlert.create();
                deleteAlert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePlayer.this, MainActivity.class);
        //intent.putExtra("arrayListToSend", (Serializable) timestampArrayList);
        startActivity(intent);
        finish();
    }

    //opens the add player editText
    public void openAddPlayer() {
        NewPlayerDialog newPlayerDialog = new NewPlayerDialog();
        newPlayerDialog.setCancelable(false);
        newPlayerDialog.show(getSupportFragmentManager(), "New Player");
    }

    //gets and saves the new player name from the NewPlayerDialog class
    @Override
    public void getNewPlayer(String newPlayerName) {

            playerListArray.add(0, new Player(playerData.generatePlayerID(), newPlayerName));
            Toast.makeText(getApplicationContext(),
                    newPlayerName + " added!", Toast.LENGTH_SHORT).show();
            playerData.writeFile(getApplicationContext(), playerListArray);
            adapter.notifyDataSetChanged();
    }

    //saves player name and ID in sharedPreference
    public static void savePlayer(String playerNameKey, String playerName,
                                  String playerIDKey, int playerID, Context context) {
        SharedPreferences playerNameShared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences playerIDShared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor playerNameEditor = playerNameShared.edit();
        SharedPreferences.Editor playerIDEditor = playerIDShared.edit();
        playerNameEditor.putString(playerNameKey, playerName);
        playerIDEditor.putInt(playerIDKey, playerID);
        playerNameEditor.commit();
        playerIDEditor.commit();
    }

    //retrieves selected user name
    public static String getPlayerName(String key, Context context) {
        SharedPreferences playerNameShared = PreferenceManager.getDefaultSharedPreferences(context);
        return playerNameShared.getString(key, null );
    }

    //retrieves selected user ID
    public static int getPlayerID(String key, Context context) {
        SharedPreferences playerIDShared = PreferenceManager.getDefaultSharedPreferences(context);
        return playerIDShared.getInt(key, 0 );
    }
}