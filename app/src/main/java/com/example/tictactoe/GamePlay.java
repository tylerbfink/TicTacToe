package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {

    Button button_zero, button_one, button_two;
    Button button_three, button_four, button_five;
    Button button_six, button_seven, button_eight;
    Button button_reset_board;

    int[] playList = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int playCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

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
        if (view.getId() == R.id.button_reset_board) {
            resetBoard();
        }
        else if (playCount < 9 && checkIfOpen(view)) {
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

    public void computerPlay() {
        if (playCount < 9) {
            Random generator = new Random();
            int nextPlay = generator.nextInt(9);
            while (playList[nextPlay] != 0) {
                nextPlay = generator.nextInt(9);
            }
            switch (nextPlay) {
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

    private void resetBoard() {
        for (int index = 0; index < 9; index++) {
            playList[index] = 0;
        }
        playCount = 0;
        button_zero.setText(R.string.blank);
        button_one.setText(R.string.blank);
        button_two.setText(R.string.blank);
        button_three.setText(R.string.blank);
        button_four.setText(R.string.blank);
        button_five.setText(R.string.blank);
        button_six.setText(R.string.blank);
        button_seven.setText(R.string.blank);
        button_eight.setText(R.string.blank);
    }

    private void checkForWinner() {
        if (playList[0] != 0 && playList[0] == playList[1] && playList[0] == playList[2] ||
                playList[0] != 0 && playList[0] == playList[4] && playList[0] == playList[8] ||
                playList[0] != 0 && playList[0] == playList[3] && playList[0] == playList[6] ||
                playList[1] != 0 && playList[1] == playList[4] && playList[1] == playList[7] ||
                playList[2] != 0 && playList[2] == playList[5] && playList[2] == playList[8] ||
                playList[2] != 0 &&  playList[2] == playList[4] && playList[2] == playList[6] ||
                playList[3] != 0 && playList[3] == playList[4] && playList[3] == playList[5] ||
                playList[6] != 0 && playList[6] == playList[7] && playList[6] == playList[8]) {
            playCount = 9;
            Toast.makeText(getApplicationContext(), "Winner!",
                    Toast.LENGTH_SHORT).show();
        }
    }

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
}