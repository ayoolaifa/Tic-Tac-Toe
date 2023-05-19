package com.example.tictactoe;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;
// Class allows application to calculate the computers moves in the background
// This is by using a background service
public class ComputerMove extends Service {
    private IBinder binder = new ComputerMoveBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ComputerMoveBinder extends Binder{
        ComputerMove getService(){
            return ComputerMove.this;
        }
    }

    // Function that return the actual best move for the computer (based on difficulty)
    public String BestMove(String[][] field, int depth){
        int BestValue = -1000;
        int BestRowMove;
        int BestColumnMove;
        String Move = "";


        for (int i = 0; i < 3; i++){
            for (int j = 0; j< 3; j++){

                if (field[i][j].equals("")){ // checks if the computer can play in position
                    field[i][j] = "O";

                    // calls function that return the evaluation of move
                    int MoveValue = Minimax(field, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                    field[i][j] = "";

                    if (MoveValue > BestValue){ // checks if that is the most optimum move
                        BestRowMove = i;
                        BestColumnMove = j;
                        Move = BestRowMove + "," + BestColumnMove;
                        BestValue = MoveValue;
                    }
                }
            }
        }
        return Move;
    }

    // Function to check is a position in board that the computer can play on
    private boolean isMoveLeft(String[][] field){
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (field[i][j].equals(""))
                    return true;
        return false;
    }

    // Mimimax algorithm that returns the evaluation of the computer making its move on particular position on board
    // Improved algorithm by using alpha-beta pruning
    // This function is recursive until it finds a board where the computer has won, lost or drawn the game.
    // The Difficulty of the computer is set by changing the depth value.
    // The higher the depth, the more chances it has to find the best possible move
    private int Minimax (String[][] field, int depth, int alpha, int beta, Boolean isMax) {
        int Score = Evaluation(field); // checks the state of the game

        // returns the score when one of the three occur
        // The the computer has won (score variable is 10)
        // the depth is 0
        // their are no positions left on the board to play move
        if (Math.abs(Score) == 10 || depth == 0 || !(isMoveLeft(field))){ // returns the score when one of the three occur
            return Score;
        }

        // isMax is a variable that keeps track of who turn it is, computer or player
        if (isMax) {
            int HighestValue = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j].equals("")) {
                        field[i][j] = "O";
                        // returns the highest value between the highestValue set or the score that is about to be return
                        HighestValue = Math.max(HighestValue, Minimax(field, depth - 1, alpha, beta, false));
                        field[i][j] = "";
                        alpha = Math.max(alpha,HighestValue);
                        if (alpha >= beta){
                            return HighestValue;
                        }
                    }
                }
            }

            return HighestValue;
        } else {
            int LowestValue = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(field[i][j].equals("")){
                        String player = "X";
                        field[i][j] = player;
                        // returns the lowest value between the lowestValue set or the score that is about to be return
                        LowestValue = Math.min(LowestValue, Minimax(field, depth - 1, alpha, beta,true));
                        field[i][j] = "";
                        beta = Math.min(beta, LowestValue);
                        if (beta <= alpha){
                            return LowestValue;
                        }
                    }

                }
            }

            return LowestValue;
        }
    }

    // Function that returns the evaluation of the board state
    // returns 10 if computer has won
    // returns -10 is the player has won
    // returns 0 if it is a draw
    private int Evaluation(String[][] field){
        for (int i = 0; i < 3; i++){
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                if(field[i][0].equals("X")){
                    return -10;
                }else if (field[i][0].equals("O")){
                    return 10;
                }
            }

        }

        for (int i = 0; i < 3; i++){
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                if(field[0][i].equals("X")){
                    return -10;
                }else if (field[0][i].equals("O")){
                    return 10;
                }
            }

        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            if(field[1][1].equals("X")){
                return -10;
            }else if (field[1][1].equals("O")){
                return 10;
            }
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            if(field[0][2].equals("X")){
                return -10;
            }else if (field[0][2].equals("O")){
                return 10;
            }
        }

        return 0;
    }
}
