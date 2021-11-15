package com.example.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerNamesAdapter extends ArrayAdapter<Player> {
    public PlayerNamesAdapter(Context context, ArrayList<Player> playerData) {
        super(context, 0, playerData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get player from array
        Player player = getItem(position);

        // inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.change_player_view, parent, false);
        }

        TextView playerName = (TextView) convertView.findViewById(R.id.change_player_name);
        playerName.setText(player.name);

        return convertView;
    }
}