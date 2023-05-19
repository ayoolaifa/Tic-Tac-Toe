package com.example.tictactoe;
// Java Class to hold the data gotten from leaderboard table.
public class Score {
    String difficulty;
    String username;
    int pscore;
    int cscore;

    double winPercentage = 0;

    public Score(String d, String u, int p, int c){
        this.difficulty = d;
        this.username = u;
        this.pscore = p;
        this.cscore = c;
        this.winPercentage = this.CalculateWinPercentage();
    }

    public int getCscore() {
        return cscore;
    }

    public int getPscore() {
        return pscore;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getUsername() {
        return username;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public double CalculateWinPercentage(){
        double winPercentage = 0;
        if (Double.parseDouble(String.valueOf(this.pscore + this.cscore)) != 0){
            winPercentage = Double.parseDouble(String.valueOf(this.pscore)) /Double.parseDouble(String.valueOf(this.pscore + this.cscore));
            winPercentage *= 100;
            winPercentage = Math.ceil(winPercentage*Math.pow(10,1)) / Math.pow(10, 1);
        }

        return winPercentage;
    }
    @Override
    public String toString() {
        return "Score{" +
                "difficulty='" + difficulty + '\'' +
                ", username='" + username + '\'' +
                ", pscore=" + pscore +
                ", cscore=" + cscore +
                '}';
    }
}
