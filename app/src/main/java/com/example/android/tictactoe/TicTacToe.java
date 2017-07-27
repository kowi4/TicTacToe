package com.example.android.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Dominic on 2017-07-14.
 */

public class TicTacToe {

    public static int oPlayer = R.drawable.ic_circle_black_50dp;
    public static int xPlayer = R.drawable.ic_cross_black_50dp;
    public int size;
    public int width;
    public int moveCount = 0;
    public int ticTacToeSwitch = R.drawable.ic_circle_black_50dp;
    public int[] board;
    public String placeForWinningBar;
    private ImageAdapter mImageAdapter;
    private Context mContext;

    public TicTacToe(Context c, ImageAdapter imageAdapter, int size, int width) {
        mContext = c;
        mImageAdapter = imageAdapter;
        this.size = size;
        this.width = width;
        board = new int[size];
        for (int i = 0; i < size; i++) {
            board[i] = 0;
        }
    }

    // based on http://eduinf.waw.pl/inf/utils/001_2008/0415.php
    // minimax algorithm returning int[] {bestScore, computerMove]
    // return bestScore =  1 if first player  wins
    // return bestScore = -1 if second player loose
    // return bestScore =  0 if first player doing best move
    int[] minimax(int player, int level) {
        int drawCounter = 0;
        int computerMove = -1;
        // check for game over with winner
        for (int i = 0; i < size; i++)
            if (board[i] == 0) {
                board[i] = player;
                computerMove = i;
                drawCounter++;
                boolean test = checkForWinner(player);
                board[i] = 0;
                if (test) {
                    return new int[]{player == xPlayer ? -1 : 1, computerMove};
                }
            }

        // check for game over with draw
        if (drawCounter == 1) {
            return new int[]{0, computerMove};
        }

        // looking for best move
        int score[];
        int bestScore;

        bestScore = player == xPlayer ? 2 : -2;

        for (int i = 0; i < size; i++)
            if (board[i] == 0) {
                board[i] = player;
                score = minimax(player == xPlayer ? oPlayer : xPlayer, level + 1);
                board[i] = 0;

                if (((player == xPlayer) && (score[0] < bestScore)) || ((player == oPlayer) && (score[0] > bestScore))) {
                    bestScore = score[0];
                    computerMove = i;
                }
            }
        return new int[]{bestScore, computerMove};
    }

    // Method for checking for winner only 3x3 grid
    public boolean checkForWinner(int player) {

        // "\"
        if ((board[0] == player) && (board[4] == player) && (board[8] == player)) {
            placeForWinningBar = "148";
            return true;
        }
        // "/"
        if ((board[2] == player) && (board[4] == player) && (board[6] == player)) {
            placeForWinningBar = "246";
            return true;
        }
        // "---"
        for (int i = 0; i <=6; i += 3) {
            if ((board[i] == player) && (board[i + 1] == player) && (board[i + 2] == player)) {
                placeForWinningBar = "" + i + (i + 1) + (i + 2) + "";
                return true;
            }
        }
        //  "|"
        for (int j = 0; j <=2; j++) {
            if ((board[j] == player) && (board[j + 3] == player) && (board[j + 6] == player)) {
                placeForWinningBar = "" + j + (j + 3) + (j + 6) + "";
                return true;
            }
        }

        return false;
    }

    // Method for showing Toast with winner or draw
    public void showWinner(int winner) {

        if (winner == R.drawable.ic_circle_black_50dp) {
            Toast.makeText(mContext, "The winner is: Circle ", Toast.LENGTH_SHORT).show();
            mImageAdapter.isNotGridClicable = true;
            newGameAlert();
        } else if (winner == R.drawable.ic_cross_black_50dp) {
            Toast.makeText(mContext, "The winner is: Cross ", Toast.LENGTH_SHORT).show();
            mImageAdapter.isNotGridClicable = true;
            newGameAlert();
        } else if (winner == 0) {
            Toast.makeText(mContext, "Draw", Toast.LENGTH_SHORT).show();
            mImageAdapter.isNotGridClicable = true;
            newGameAlert();
        }
    }

    // Method for showing alert if Yes it starts new game, No close app
    public void newGameAlert() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder alertBuild = new AlertDialog.Builder(mContext);
                alertBuild.setTitle("Again");
                alertBuild.setMessage("Do you wanna play again?");
                alertBuild.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) mContext).recreate();
                    }
                });
                alertBuild.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) mContext).finish();
                    }
                });
                alertBuild.setCancelable(false);

                alertBuild.show();
            }
        }, 1500); // 1500ms delay
    }

    // Method for switching image cross/circle
    public int ticTacToeSwitch() {
        if (ticTacToeSwitch == xPlayer) {
            ticTacToeSwitch = oPlayer;
        } else {
            ticTacToeSwitch = xPlayer;
        }
        return ticTacToeSwitch;
    }

    //TODO
    public int crossWinner(String placeForWinningBar) {

        return 0;
    }
}
