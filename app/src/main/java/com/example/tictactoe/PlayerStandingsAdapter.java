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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_view, parent, false);
        }

        TextView playerName = (TextView) convertView.findViewById(R.id.main_options_view);

        playerName.setText(String.valueOf(player.wins));

        return convertView;
    }
}