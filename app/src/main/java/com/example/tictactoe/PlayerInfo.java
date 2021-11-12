package com.example.tictactoe;

public class PlayerInfo {

    String name;
    int wins;
    int playedGames;
    String lastPlayedGame;

    public PlayerInfo(String name, int wins, int playedGames, String lastPlayedGame) {
        this.name = name;
        this.wins = wins;
        this.playedGames = playedGames;
        this.lastPlayedGame = lastPlayedGame;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setLastPlayedGame(String lastPlayedGame) {
        this.lastPlayedGame = lastPlayedGame;
    }

    public  String getLastPlayedGame() {
        return lastPlayedGame;
    }

}
