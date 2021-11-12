package com.example.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerStandingsAdapter extends ArrayAdapter<PlayerInfo> {
    public PlayerStandingsAdapter(Context context, ArrayList<PlayerInfo> playerData) {
        super(context, 0, playerData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get player from array
        PlayerInfo player = getItem(position);

        // inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.standings_view, parent, false);
        }

        TextView playerName = (TextView) convertView.findViewById(R.id.standings_name);
        TextView playerWins = (TextView) convertView.findViewById(R.id.standings_games_won);
        TextView playerGamesPlayed = (TextView) convertView.findViewById(R.id.standings_games_played);
        TextView playerLastPlayed = (TextView) convertView.findViewById(R.id.standings_last_played);

        playerName.setText(player.name);
        playerWins.setText(String.valueOf(player.wins));
        playerGamesPlayed.setText(String.valueOf(player.playedGames));
        playerLastPlayed.setText("Last played: " + player.lastPlayedGame);

        return convertView;
    }
}