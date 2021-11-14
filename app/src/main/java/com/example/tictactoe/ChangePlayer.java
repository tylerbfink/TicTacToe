package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChangePlayer extends AppCompatActivity {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    String newName;
    int wins = 0;
    int playedGames = 0;
    String lastPlayedGame;

    ArrayList<PlayerInfo> playerListArray = new ArrayList<PlayerInfo>();

    Button add_player;
    Button remove_player;
    AlertDialog.Builder deleteAlert;
    AlertDialog.Builder addPlayerAlert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_player);

        PlayerInfo Tyler = new PlayerInfo("Tyler");
        PlayerInfo Zoey = new PlayerInfo("Zoey");
        PlayerInfo Shannon = new PlayerInfo("Shannon");
        PlayerInfo Lynn = new PlayerInfo("Lynn");
        playerListArray.add(Tyler);
        playerListArray.add(Zoey);
        playerListArray.add(Shannon);
        playerListArray.add(Lynn);

        ListView playerListView = findViewById(R.id.existing_player_list);
        PlayerNamesAdapter adapter = new PlayerNamesAdapter(this, playerListArray);
        playerListView.setAdapter(adapter);

        addPlayerAlert = new AlertDialog.Builder(this);
        add_player = (Button) findViewById(R.id.add_player);

        final EditText new_player_edittext = (EditText) findViewById(R.id.name_edittext);
        final View nameView = getLayoutInflater().inflate(R.layout.name_adder,null);

        add_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPlayerAlert.setMessage("Enter New Player Name");
                addPlayerAlert.setView(nameView)
                        .setPositiveButton("Add Player", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                newName = new_player_edittext.getText().toString();
                                Toast.makeText(getApplicationContext(),
                                        "Welcome", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                addPlayerAlert.show();
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
                                Toast.makeText(getApplicationContext(),"need to delete player",
                                        Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                deleteAlert.create();
                //alert.setTitle(R.string.confirm_delete);
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

            String saveInfo = newName + "," + wins + "," + playedGames + "," + lastPlayedGame;

            fos.write(saveInfo.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}