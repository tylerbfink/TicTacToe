package com.example.tictactoe;

//class to build players held in player array
public class Player {

    int playerID;
    String name;
    int wins;
    int playedGames;
    String lastPlayedGame;

    public Player(int playerID, String name) {
        this.playerID = playerID;
        this.name = name;
        this.wins = 0;
        this.playedGames = 0;
        this.lastPlayedGame = "No games played yet!";
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
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
