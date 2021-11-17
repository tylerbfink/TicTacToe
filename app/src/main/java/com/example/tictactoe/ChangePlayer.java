package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChangePlayer extends AppCompatActivity
        implements NewPlayerDialog.DialogListener {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    String newName;
    int wins = 0;
    int playedGames = 0;
    String lastPlayedGame;

    ArrayList<Player> playerListArray = new ArrayList<>();

    Button add_player;
    Button remove_player;
    AlertDialog.Builder deleteAlert;

    PlayerNamesAdapter adapter;
    ListView playerListView;
    int listViewSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_player);

        readFile();

        playerListView = findViewById(R.id.existing_player_list);
        adapter = new PlayerNamesAdapter(this, playerListArray);
        playerListView.setAdapter(adapter);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewSelected = i;
            }
        });

        add_player = (Button) findViewById(R.id.add_player);
        add_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPlayer();
            }
        });

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

    //writes player data to file
    private void writeFile() {
        try {
            FileOutputStream fos = openFileOutput(SAVED_PLAYER, MODE_PRIVATE | MODE_APPEND);

            //build name/wins/games played/last play date string
            String pattern = "dd MMM yyyy - h:mm:ss a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            lastPlayedGame = dateFormat.format(new Date());

            String saveInfo = playerListArray.get(0).name + "," +
                                playerListArray.get(0).wins + "," +
                                playerListArray.get(0).playedGames + "," +
                                playerListArray.get(0).lastPlayedGame + "\n";

            fos.write(saveInfo.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //opens the add player editText
    public void openAddPlayer() {
        NewPlayerDialog newPlayerDialog = new NewPlayerDialog();
        newPlayerDialog.setCancelable(false);
        newPlayerDialog.show(getSupportFragmentManager(), "New Player");
    }

    //gets and saves the new player name from the NewPlayerDialog class
    @Override
    public void getNewPlayer(String newPlayerName) {

            playerListArray.add(0, new Player(newPlayerName));
            Toast.makeText(getApplicationContext(),
                    newPlayerName + " added!", Toast.LENGTH_SHORT).show();
            writeFile();
            adapter.notifyDataSetChanged();

    }
}