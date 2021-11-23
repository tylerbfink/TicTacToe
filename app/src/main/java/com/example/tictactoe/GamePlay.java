package com.example.tictactoe;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {

    Button button_zero, button_one, button_two;
    Button button_three, button_four, button_five;
    Button button_six, button_seven, button_eight;
    Button button_reset_board;

    TextView play_text, player_one_text, player_two_text;

    ImageView cat_image;

    boolean twoPlayer = false;
    boolean playerOneTurn = true;
    boolean difficultyOn = false;

    String playerNameOne;
    String playerNameTwo = "Android";

    int playerID, playerTwoID;
    int playerArrayPosition, playerTwoArrayPosition;

    int playerOneWins = 0;
    int playerTwoWins = 0;

    int[] playList = {0, 0, 0, 0, 0, 0, 0, 0, 0};  //played squares array
    int[] winningsSquares; //winnings squares to set highlight on win
    boolean winner = false;
    int playCount = 0;

    int nextPlay = 0;

    PlayerIO playerData = new PlayerIO(); //new instance of player input/output
    ArrayList<Player> playerListArray; //common app array that holds players & stats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        //retrieves players from saved data file
        playerListArray = playerData.readFile(getApplicationContext());

        //retrieves difficulty on from shared preferences
        if (Options.getDifficultyOn("DIFFICULTY_ON", getBaseContext()) != false) {
            difficultyOn = true;
        }

        //retrieves current player from shared preferences or array if no shared preference
        if (ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext()) != null) {
            playerNameOne = ChangePlayer.getPlayerName("CURRENT_PLAYER", getBaseContext());
            playerID = ChangePlayer.getPlayerID("CURRENT_ID", getBaseContext());

            //gets player position in playerListArray by playerID
            for (int index = 0; index < playerListArray.size(); index++) {
                if (playerListArray.get(index).getPlayerID() == playerID) {
                    playerArrayPosition = index;
                }
            }
        }

        else {
            playerNameOne = playerListArray.get(0).getName();
            playerArrayPosition = 0;
        }

        //retrieves player two if two player is selected
        if (Options.getPlayerTwoSelected("TWO_PLAYER", getBaseContext())) {
            twoPlayer = true;

            playerNameTwo = Options.getPlayerTwoName("PLAYER_TWO_NAME", getBaseContext());
            playerTwoID = Options.getPlayerTwoID("PLAYER_TWO_ID", getBaseContext());

            //gets player position in playerListArray by playerID
            for (int index = 0; index < playerListArray.size(); index++) {
                if (playerListArray.get(index).getPlayerID() == playerTwoID) {
                    playerTwoArrayPosition = index;
                }
            }
        }

        //sets cat image to transparent
        cat_image = (ImageView) findViewById(R.id.cat_image);
        cat_image.setAlpha(0);

        //textView for score
        player_one_text = (TextView) findViewById(R.id.player_one_wins_text);
        player_two_text = (TextView) findViewById(R.id.player_two_wins_text);

        player_one_text.setText(playerNameOne + " wins: " + playerOneWins);
        player_two_text.setText(playerNameTwo + " wins: " + playerTwoWins);

        //text to indicate the players turn
        play_text = findViewById(R.id.play_text);
        play_text.setText(playerNameOne + getString(R.string.your_turn));

        //assigns button from .xml buttons
        button_zero = findViewById(R.id.button_zero);
        button_one = findViewById(R.id.button_one);
        button_two = findViewById(R.id.button_two);
        button_three = findViewById(R.id.button_three);
        button_four = findViewById(R.id.button_four);
        button_five = findViewById(R.id.button_five);
        button_six = findViewById(R.id.button_six);
        button_seven = findViewById(R.id.button_seven);
        button_eight = findViewById(R.id.button_eight);
        button_reset_board = findViewById(R.id.button_reset_board);

        //onclick for play grid
        button_zero.setOnClickListener(this);
        button_one.setOnClickListener(this);
        button_two.setOnClickListener(this);
        button_three.setOnClickListener(this);
        button_four.setOnClickListener(this);
        button_five.setOnClickListener(this);
        button_six.setOnClickListener(this);
        button_seven.setOnClickListener(this);
        button_eight.setOnClickListener(this);
        button_reset_board.setOnClickListener(this);

        //restores state of game
        if (savedInstanceState != null) {
            playList = savedInstanceState.getIntArray("PLAYLIST");
            restoreSquares();

            playerOneTurn = savedInstanceState.getBoolean("PLAYER_ONE_TURN");
            if (playerOneTurn == true) {
                play_text.setText(playerNameOne + getString(R.string.your_turn));
            }
            else if (twoPlayer && !playerOneTurn) {
                play_text.setText(playerNameTwo + getString(R.string.your_turn));
            }

            winner = savedInstanceState.getBoolean("WINNER");
            winningsSquares = savedInstanceState.getIntArray("WINNING_SQUARES");
            if (winner) {
                setWinningSquares();

                if (playList[winningsSquares[0]] == 1) {
                    play_text.setText(playerNameOne + " Wins!");
                }
                else if (playList[winningsSquares[0]] == 2) {
                    play_text.setText(playerNameTwo + " Wins!");
                }
            }

            playerOneWins = savedInstanceState.getInt("PLAYER_ONE_WINS");
            player_one_text.setText(playerNameOne + " wins: " + playerOneWins);

            playerTwoWins = savedInstanceState.getInt("PLAYER_TWO_WINS");
            player_two_text.setText(playerNameTwo + " wins: " + playerTwoWins);

            playCount = savedInstanceState.getInt("PLAY_COUNT");

            nextPlay = savedInstanceState.getInt("NEXT_PLAY");
        }
    }

    @Override
    public void onBackPressed() {
        playerData.writeFile(getApplicationContext(), playerListArray);
        Intent intent = new Intent(GamePlay.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_reset_board) {  //resets game
            resetBoard();
        }

        else if (playCount < 9 && checkIfOpen(view)) { //places players mark on board
            switch (view.getId()) {
                case R.id.button_zero:
                    if (playerOneTurn) {
                        button_zero.setText("X");
                        playList[0] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_zero.setText("O");
                        playList[0] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_one:
                    if (playerOneTurn) {
                        button_one.setText("X");
                        playList[1] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_one.setText("O");
                        playList[1] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_two:
                    if (playerOneTurn) {
                        button_two.setText("X");
                        playList[2] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer) {
                        button_two.setText("O");
                        playList[2] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_three:
                    if (playerOneTurn) {
                        button_three.setText("X");
                        playList[3] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_three.setText("O");
                        playList[3] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_four:
                    if (playerOneTurn) {
                        button_four.setText("X");
                        playList[4] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_four.setText("O");
                        playList[4] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_five:
                    if (playerOneTurn) {
                        button_five.setText("X");
                        playList[5] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_five.setText("O");
                        playList[5] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_six:
                    if (playerOneTurn) {
                        button_six.setText("X");
                        playList[6] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_six.setText("O");
                        playList[6] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_seven:
                    if (playerOneTurn) {
                        button_seven.setText("X");
                        playList[7] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_seven.setText("O");
                        playList[7] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
                case R.id.button_eight:
                    if (playerOneTurn) {
                        button_eight.setText("X");
                        playList[8] = 1;
                        playerOneTurn = false;
                        if (twoPlayer) {
                            play_text.setText(playerNameTwo + getString(R.string.your_turn));
                        };
                    }
                    else if (!playerOneTurn && twoPlayer == true) {
                        button_eight.setText("O");
                        playList[8] = 2;
                        playerOneTurn = true;
                        play_text.setText(playerNameOne + getString(R.string.your_turn));
                    }
                    playCount++;
                    break;
            }
            checkForWinner();

            if (twoPlayer == false) {
                computerPlay();
                playerOneTurn = true;
            }
        }
    }

    // calculates androids move
    public void computerPlay() {
        if (playCount < 9) {
            AndroidGetPlay androidPlay = new AndroidGetPlay();
            androidPlay.execute();
        }

        else if (winner == false) { //checks for no winner if full board
            disableButtons();
            play_text.setText("Cat's Game!");
            cat_image.setAlpha(1000);

            String pattern = "dd MMM yyyy - h:mm:ss a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            String lastPlayedGame = dateFormat.format(new Date());

            int tempPlayed = playerListArray.get(playerArrayPosition).getPlayedGames();
            tempPlayed++;
            playerListArray.get(playerArrayPosition).setPlayedGames(tempPlayed);
            playerListArray.get(playerArrayPosition).setLastPlayedGame(lastPlayedGame);
        }
    }

    private void processComputerPlay() {
        switch (nextPlay) {  //sets androids play on grid
            case 0:
                button_zero.setText("O");
                playList[0] = 2;
                break;
            case 1:
                button_one.setText("O");
                playList[1] = 2;
                break;
            case 2:
                button_two.setText("O");
                playList[2] = 2;
                break;
            case 3:
                button_three.setText("O");
                playList[3] = 2;
                break;
            case 4:
                button_four.setText("O");
                playList[4] = 2;
                break;
            case 5:
                button_five.setText("O");
                playList[5] = 2;
                break;
            case 6:
                button_six.setText("O");
                playList[6] = 2;
                break;
            case 7:
                button_seven.setText("O");
                playList[7] = 2;
                break;
            case 8:
                button_eight.setText("O");
                playList[8] = 2;
                break;
        }
        playCount++;
        checkForWinner();
    }


    //resets game board for new game
    private void resetBoard() {
        cat_image.setAlpha(0);

        enableButtons();

        playerOneTurn = true;

        for (int index = 0; index < 9; index++) {
            playList[index] = 0;
        }

        playCount = 0;
        winner = false;
        //winningsSquares = new int[]{0, 4, 8};

        button_zero.setText(R.string.blank);
        button_one.setText(R.string.blank);
        button_two.setText(R.string.blank);
        button_three.setText(R.string.blank);
        button_four.setText(R.string.blank);
        button_five.setText(R.string.blank);
        button_six.setText(R.string.blank);
        button_seven.setText(R.string.blank);
        button_eight.setText(R.string.blank);

        resetSquareColours();

        play_text.setText(playerNameOne + getString(R.string.your_turn));
    }

    //checks game board for possible winner
    private void checkForWinner() {

        if (playList[0] != 0 && playList[0] == playList[1] && playList[0] == playList[2]) {
            winningsSquares = new int[]{0, 1, 2};
            winner = true;
        }
        else if (playList[0] != 0 && playList[0] == playList[4] && playList[0] == playList[8]) {
            winningsSquares = new int[]{0, 4, 8};
            winner = true;
        }
        else if (playList[0] != 0 && playList[0] == playList[3] && playList[0] == playList[6]) {
            winningsSquares = new int[]{0, 3, 6};
            winner = true;
        }
        else if (playList[1] != 0 && playList[1] == playList[4] && playList[1] == playList[7]) {
            winningsSquares = new int[]{1, 4, 7};
            winner = true;
        }

        else if (playList[2] != 0 && playList[2] == playList[5] && playList[2] == playList[8]) {
            winningsSquares = new int[]{2, 5, 8};
            winner = true;
        }

        else if (playList[2] != 0 &&  playList[2] == playList[4] && playList[2] == playList[6]) {
            winningsSquares = new int[]{2, 4, 6};
            winner = true;
        }

        else if (playList[3] != 0 && playList[3] == playList[4] && playList[3] == playList[5]) {
            winningsSquares = new int[]{3, 4, 5};
            winner = true;
        }

        else if (playList[6] != 0 && playList[6] == playList[7] && playList[6] == playList[8]) {
            winningsSquares = new int[]{6, 7, 8};
            winner = true;
        }

        if (winner) {  //items to process if winner
            playCount = 9;

            setWinningSquares();  //highlights winnings squares

            String pattern = "dd MMM yyyy - h:mm:ss a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            String lastPlayedGame = dateFormat.format(new Date());

            if (playList[winningsSquares[0]] == 1) {
                play_text.setText(playerNameOne + " Wins!");
                playerOneWins++;
                player_one_text.setText(playerNameOne + " wins: " + playerOneWins);

                //adds won game/played game/last played date to playerOne stats
                int tempWon = playerListArray.get(playerArrayPosition).getWins();
                int tempPlayed = playerListArray.get(playerArrayPosition).getPlayedGames();
                tempWon++;
                tempPlayed++;
                playerListArray.get(playerArrayPosition).setWins(tempWon);
                playerListArray.get(playerArrayPosition).setPlayedGames(tempPlayed);
                playerListArray.get(playerArrayPosition).setLastPlayedGame(lastPlayedGame);

                if (twoPlayer) {
                    int tempPlayerTwoPlayed = playerListArray.get(playerTwoArrayPosition).getPlayedGames();
                    tempPlayerTwoPlayed++;
                    playerListArray.get(playerTwoArrayPosition).setPlayedGames(tempPlayerTwoPlayed);
                    playerListArray.get(playerTwoArrayPosition).setLastPlayedGame(lastPlayedGame);
                }
            }
            else if (playList[winningsSquares[0]] == 2) {
                play_text.setText(playerNameTwo + " Wins!");
                playerTwoWins++;
                player_two_text.setText(playerNameTwo + " wins: " + playerTwoWins);

                //adds played game/last played date to playerTwo stats if not android
                if (twoPlayer) {
                    int tempWon = playerListArray.get(playerTwoArrayPosition).getWins();
                    int tempPlayed = playerListArray.get(playerTwoArrayPosition).getPlayedGames();
                    tempWon++;
                    tempPlayed++;
                    playerListArray.get(playerTwoArrayPosition).setWins(tempWon);
                    playerListArray.get(playerTwoArrayPosition).setPlayedGames(tempPlayed);
                    playerListArray.get(playerTwoArrayPosition).setLastPlayedGame(lastPlayedGame);
                }

                //adds played game/last played date to playerOne stats
                int tempPlayerOnePlayed = playerListArray.get(playerArrayPosition).getPlayedGames();
                tempPlayerOnePlayed++;
                playerListArray.get(playerArrayPosition).setPlayedGames(tempPlayerOnePlayed);
                playerListArray.get(playerArrayPosition).setLastPlayedGame(lastPlayedGame);
            }
        }
    }

    //sets winnings squares stand out colours
    private void setWinningSquares() {
        for (int index = 0; index < 3; index++) {
            switch (winningsSquares[index]) {
                case 0:
                    button_zero.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 1:
                    button_one.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 2:
                    button_two.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 3:
                    button_three.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 4:
                    button_four.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 5:
                    button_five.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 6:
                    button_six.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 7:
                    button_seven.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
                case 8:
                    button_eight.setBackgroundTintList(ColorStateList.valueOf
                            (Color.parseColor("#E04C59")));
                    break;
            }
        }
    }

    //checks to see if square clicked is available to play
    private boolean checkIfOpen(View view) {
        boolean result = false;

        switch (view.getId()) {
            case R.id.button_zero:
                if (playList[0] == 0)
                    result = true;
                break;
            case R.id.button_one:
                if (playList[1] == 0)
                    result = true;
                break;
            case R.id.button_two:
                if (playList[2] == 0)
                    result = true;
                break;
            case R.id.button_three:
                if (playList[3] == 0)
                    result = true;
                break;
            case R.id.button_four:
                if (playList[4] == 0)
                    result = true;
                break;
            case R.id.button_five:
                if (playList[5] == 0)
                    result = true;
                break;
            case R.id.button_six:
                if (playList[6] == 0)
                    result = true;
                break;
            case R.id.button_seven:
                if (playList[7] == 0)
                    result = true;
                break;
            case R.id.button_eight:
                if (playList[8] == 0)
                    result = true;
                break;
        }
        return result;
    }

    //resets all squares for new game
    private void resetSquareColours() {
        button_zero.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_one.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_two.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_three.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_four.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_five.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_six.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_seven.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
        button_eight.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#179981")));
    }

    //public class AndroidGetPlay extends AsyncTask<Integer, String, Integer> {
    public class AndroidGetPlay extends AsyncTask<Integer, String, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integer) {

            Random generator = new Random();

            if (!difficultyOn) {
                nextPlay = generator.nextInt(9);
                while (playList[nextPlay] != 0) {
                    nextPlay = generator.nextInt(9);
                }
            }

            else {
                ArrayList<Integer> blockList = new ArrayList();
                ArrayList<Integer> winList = new ArrayList();

                //searches for wins
                if (playList[0] == 2) {
                    if (playList[1] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                    if (playList[2] == 2 && playList[1] == 0) {
                        winList.add(1);
                    }
                    if (playList[3] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                    if (playList[4] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[6] == 2 && playList[3] == 0) {
                        winList.add(3);
                    }
                    if (playList[8] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                }
                if (playList[1] == 2) {
                    if (playList[0] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                    if (playList[2] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                    if (playList[4] == 2 && playList[7] == 0) {
                        winList.add(7);
                    }
                    if (playList[7] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                }
                if (playList[2] == 2) {
                    if (playList[0] == 2 && playList[1] == 0) {
                        winList.add(1);
                    }
                    if (playList[1] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                    if (playList[4] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                    if (playList[5] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[6] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[8] == 2 && playList[5] == 0) {
                        winList.add(5);
                    }
                }
                if (playList[3] == 2) {
                    if (playList[0] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                    if (playList[4] == 2 && playList[5] == 0) {
                        winList.add(5);
                    }
                    if (playList[5] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[6] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                }
                if (playList[4] == 2) {
                    if (playList[0] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[1] == 2 && playList[7] == 0) {
                        winList.add(7);
                    }
                    if (playList[2] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                    if (playList[3] == 2 && playList[5] == 0) {
                        winList.add(5);
                    }
                    if (playList[5] == 2 && playList[3] == 0) {
                        winList.add(3);
                    }
                    if (playList[6] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                    if (playList[7] == 2 && playList[1] == 0) {
                        winList.add(1);
                    }
                    if (playList[8] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                }
                if (playList[5] == 2) {
                    if (playList[2] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[3] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[4] == 2 && playList[3] == 0) {
                        winList.add(3);
                    }
                    if (playList[8] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                }
                if (playList[6] == 2) {
                    if (playList[0] == 2 && playList[3] == 0) {
                        winList.add(3);
                    }
                    if (playList[2] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[3] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                    if (playList[4] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                    if (playList[7] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[8] == 2 && playList[7] == 0) {
                        winList.add(7);
                    }
                }
                if (playList[7] == 2) {
                    if (playList[1] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[4] == 2 && playList[1] == 0) {
                        winList.add(1);
                    }
                    if (playList[6] == 2 && playList[8] == 0) {
                        winList.add(8);
                    }
                    if (playList[8] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                }
                if (playList[8] == 2) {
                    if (playList[0] == 2 && playList[4] == 0) {
                        winList.add(4);
                    }
                    if (playList[2] == 2 && playList[5] == 0) {
                        winList.add(5);
                    }
                    if (playList[4] == 2 && playList[0] == 0) {
                        winList.add(0);
                    }
                    if (playList[5] == 2 && playList[2] == 0) {
                        winList.add(2);
                    }
                    if (playList[6] == 2 && playList[7] == 0) {
                        winList.add(7);
                    }
                    if (playList[7] == 2 && playList[6] == 0) {
                        winList.add(6);
                    }
                }

                //searches for blocks
                if (playList[0] == 1) {
                    if (playList[1] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                    if (playList[2] == 1 && playList[1] == 0) {
                        blockList.add(1);
                    }
                    if (playList[3] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                    if (playList[4] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[6] == 1 && playList[3] == 0) {
                        blockList.add(3);
                    }
                    if (playList[8] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                }
                if (playList[1] == 1) {
                    if (playList[0] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                    if (playList[2] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                    if (playList[4] == 1 && playList[7] == 0) {
                        blockList.add(7);
                    }
                    if (playList[7] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                }
                if (playList[2] == 1) {
                    if (playList[0] == 1 && playList[1] == 0) {
                        blockList.add(1);
                    }
                    if (playList[1] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                    if (playList[4] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                    if (playList[5] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[6] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[8] == 1 && playList[5] == 0) {
                        blockList.add(5);
                    }
                }
                if (playList[3] == 1) {
                    if (playList[0] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                    if (playList[4] == 1 && playList[5] == 0) {
                        blockList.add(5);
                    }
                    if (playList[5] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[6] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                }
                if (playList[4] == 1) {
                    if (playList[0] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[1] == 1 && playList[7] == 0) {
                        blockList.add(7);
                    }
                    if (playList[2] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                    if (playList[3] == 1 && playList[5] == 0) {
                        blockList.add(5);
                    }
                    if (playList[5] == 1 && playList[3] == 0) {
                        blockList.add(3);
                    }
                    if (playList[6] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                    if (playList[7] == 1 && playList[1] == 0) {
                        blockList.add(1);
                    }
                    if (playList[8] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                }
                if (playList[5] == 1) {
                    if (playList[2] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[3] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[4] == 1 && playList[3] == 0) {
                        blockList.add(3);
                    }
                    if (playList[8] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                }
                if (playList[6] == 1) {
                    if (playList[0] == 1 && playList[3] == 0) {
                        blockList.add(3);
                    }
                    if (playList[2] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[3] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                    if (playList[4] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                    if (playList[7] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[8] == 1 && playList[7] == 0) {
                        blockList.add(7);
                    }
                }
                if (playList[7] == 1) {
                    if (playList[1] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[4] == 1 && playList[1] == 0) {
                        blockList.add(1);
                    }
                    if (playList[6] == 1 && playList[8] == 0) {
                        blockList.add(8);
                    }
                    if (playList[8] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                }
                if (playList[8] == 1) {
                    if (playList[0] == 1 && playList[4] == 0) {
                        blockList.add(4);
                    }
                    if (playList[2] == 1 && playList[5] == 0) {
                        blockList.add(5);
                    }
                    if (playList[4] == 1 && playList[0] == 0) {
                        blockList.add(0);
                    }
                    if (playList[5] == 1 && playList[2] == 0) {
                        blockList.add(2);
                    }
                    if (playList[6] == 1 && playList[7] == 0) {
                        blockList.add(7);
                    }
                    if (playList[7] == 1 && playList[6] == 0) {
                        blockList.add(6);
                    }
                }

                if (!winList.isEmpty()) {
                    int tempNextPlay = generator.nextInt(winList.size());
                    nextPlay = winList.get(tempNextPlay);
                }
                else if (!blockList.isEmpty()) {
                    int tempNextPlay = generator.nextInt(blockList.size());
                    nextPlay = blockList.get(tempNextPlay);
                }
                else {
                    nextPlay = generator.nextInt(9);
                    while (playList[nextPlay] != 0) {
                        nextPlay = generator.nextInt(9);
                    };
                }
            }
                return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            processComputerPlay();
        }
    }

    //disables all play buttons
    public void disableButtons() {
        button_zero.setEnabled(false);
        button_one.setEnabled(false);
        button_two.setEnabled(false);
        button_three.setEnabled(false);
        button_four.setEnabled(false);
        button_five.setEnabled(false);
        button_six.setEnabled(false);
        button_seven.setEnabled(false);
        button_eight.setEnabled(false);
    }

    //enables all play buttons
    public void enableButtons() {
        button_zero.setEnabled(true);
        button_one.setEnabled(true);
        button_two.setEnabled(true);
        button_three.setEnabled(true);
        button_four.setEnabled(true);
        button_five.setEnabled(true);
        button_six.setEnabled(true);
        button_seven.setEnabled(true);
        button_eight.setEnabled(true);
    }

    public void restoreSquares() {
        for (int index = 0; index < 9; index++) {
            if (index == 0 && playList[index] == 1) {
                button_zero.setText("X");
            }
            else if (index == 0 && playList[index] == 2) {
                button_zero.setText("O");
            }
            else if (index == 1 && playList[index] == 1) {
                button_one.setText("X");
            }
            else if (index == 1 && playList[index] == 2) {
                button_one.setText("O");
            }
            else if (index == 2 && playList[index] == 1) {
                button_two.setText("X");
            }
            else if (index == 2 && playList[index] == 2) {
                button_two.setText("O");
            }
            else if (index == 3 && playList[index] == 1) {
                button_three.setText("X");
            }
            else if (index == 3 && playList[index] == 2) {
                button_three.setText("O");
            }
            else if (index == 4 && playList[index] == 1) {
                button_four.setText("X");
            }
            else if (index == 4 && playList[index] == 2) {
                button_four.setText("O");
            }
            else if (index == 5 && playList[index] == 1) {
                button_five.setText("X");
            }
            else if (index == 5 && playList[index] == 2) {
                button_five.setText("O");
            }
            else if (index == 6 && playList[index] == 1) {
                button_six.setText("X");
            }
            else if (index == 6 && playList[index] == 2) {
                button_six.setText("O");
            }
            else if (index == 7 && playList[index] == 1) {
                button_seven.setText("X");
            }
            else if (index == 7 && playList[index] == 2) {
                button_seven.setText("O");
            }
            else if (index == 8 && playList[index] == 1) {
                button_eight.setText("X");
            }
            else if (index == 8 && playList[index] == 2) {
                button_eight.setText("O");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //saves play data
        savedInstanceState.putIntArray("PLAYLIST", playList);
        savedInstanceState.putIntArray("WINNING_SQUARES", winningsSquares);

        savedInstanceState.putInt("PLAYER_ONE_WINS", playerOneWins);
        savedInstanceState.putInt("PLAYER_TWO_WINS", playerTwoWins);
        savedInstanceState.putInt("PLAY_COUNT", playCount);
        savedInstanceState.putInt("NEXT_PLAY", nextPlay);

        savedInstanceState.putBoolean("PLAYER_ONE_TURN", playerOneTurn);
        savedInstanceState.putBoolean("WINNER", winner);
    }
}