package com.example.tictactoe;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

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

public class PlayerIO  {

    private final String SAVED_PLAYER = "SavedPlayer.txt";

    ArrayList<Player> playerListArray = new ArrayList();

    String lastPlayedGame;

    //reads file and updates ListView in Verification activity
    public ArrayList<Player> readFile(Context context) {

        if (fileExists(context, SAVED_PLAYER)) {

            FileInputStream fis = null;
            try {
                fis = context.openFileInput(SAVED_PLAYER);

            } catch (IOException e) {
                e.printStackTrace();
            }


            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String sLine = null;

            try {
                while ((sLine = br.readLine()) != null) {

                    String[] tempReadPlayer = sLine.split(",");

                    Player tempPlayer = new Player(Integer.valueOf(tempReadPlayer[0]),
                            tempReadPlayer[1]);
                    tempPlayer.setWins(Integer.valueOf(tempReadPlayer[2]));
                    tempPlayer.setPlayedGames(Integer.valueOf(tempReadPlayer[3]));
                    tempPlayer.setLastPlayedGame(tempReadPlayer[4]);

                    playerListArray.add(0, tempPlayer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //if save file does not exist creates generic 'New Player' for usage
        else {
            playerListArray.add(0, new Player(0, "New Player"));
        }

        return playerListArray;
    }

    //checks if file exists
    public boolean fileExists(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }


    //writes player data to file
    public void writeFile(Context context, ArrayList<Player> playerListArray) {
        this.playerListArray = playerListArray;

        try {
            FileOutputStream fos = context.openFileOutput(SAVED_PLAYER, MODE_PRIVATE);

            //build name/wins/games played/last play date string
            for (int count = playerListArray.size() - 1; count >= 0; count--) {
                String pattern = "dd MMM yyyy - h:mm:ss a";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                lastPlayedGame = dateFormat.format(new Date());

                Player tempPlayer = (Player) playerListArray.get(count);

                String saveInfo = tempPlayer.getPlayerID() + "," +
                        tempPlayer.getName() + "," +
                        tempPlayer.getWins() + "," +
                        tempPlayer.getPlayedGames() + "," +
                        tempPlayer.getLastPlayedGame() + "\n";

                fos.write(saveInfo.getBytes());

            }
            fos.close();

        } catch(FileNotFoundException e){
                e.printStackTrace();
        } catch(IOException e){
                e.printStackTrace();
        }

    }

    public int generatePlayerID() {
        return playerListArray.size() + 1;
    }

    public void first() {
        String newPlayerName = "Player1";
        playerListArray.add(0, new Player(generatePlayerID(), newPlayerName));
    }
}

