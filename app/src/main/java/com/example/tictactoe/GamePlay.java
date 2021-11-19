package com.example.tictactoe;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {

    Button button_zero, button_one, button_two;
    Button button_three, button_four, button_five;
    Button button_six, button_seven, button_eight;
    Button button_reset_board;

    TextView play_text;

    String playerNameOne = "Tyler";
    String playerNameTwo = "Android";

    int[] playList = {0, 0, 0, 0, 0, 0, 0, 0, 0};  //played squares array
    int[] winningsSquares; //winnings squares to set highlight on win
    boolean winner = false;
    int playCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GamePlay.this, MainActivity.class);
        //intent.putExtra("arrayListToSend", (Serializable) timestampArrayList);
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
                    button_zero.setText("X");
                    playList[0] = 1;
                    break;
                case R.id.button_one:
                    button_one.setText("X");
                    playList[1] = 1;
                    break;
                case R.id.button_two:
                    button_two.setText("X");
                    playList[2] = 1;
                    break;
                case R.id.button_three:
                    button_three.setText("X");
                    playList[3] = 1;
                    break;
                case R.id.button_four:
                    button_four.setText("X");
                    playList[4] = 1;
                    break;
                case R.id.button_five:
                    button_five.setText("X");
                    playList[5] = 1;
                    break;
                case R.id.button_six:
                    button_six.setText("X");
                    playList[6] = 1;
                    break;
                case R.id.button_seven:
                    button_seven.setText("X");
                    playList[7] = 1;
                    break;
                case R.id.button_eight:
                    button_eight.setText("X");
                    playList[8] = 1;
                    break;
            }
            playCount++;
            checkForWinner();
            computerPlay();
        }
    }

    // calculates androids move
    public void computerPlay() {
        if (playCount < 9) {
            Random generator = new Random();
            int nextPlay = generator.nextInt(9);
            while (playList[nextPlay] != 0) {
                nextPlay = generator.nextInt(9);
            }
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
    }

    //resets game board for new game
    private void resetBoard() {
        for (int index = 0; index < 9; index++) {
            playList[index] = 0;
        }
        playCount = 0;
        winner = false;
        winningsSquares = new int[]{0, 4, 8};

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
            String winningToken = "";
            playCount = 9;

            setWinningSquares();  //highlights winnings squares

            if (playList[winningsSquares[0]] == 1) {
                winningToken = "X's win!";
                play_text.setText(playerNameOne + " Wins!");
            }
            else if (playList[winningsSquares[0]] == 2) {
                winningToken = "O's win!";
                play_text.setText(playerNameTwo + " Wins!");
            }

            Toast.makeText(getApplicationContext(), winningToken,
                    Toast.LENGTH_SHORT).show();
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
}