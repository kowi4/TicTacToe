package com.example.android.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Dominic on 2017-07-14.
 */

public class TicTacToe {

    public static int oPlayer = R.drawable.ic_circle_black_50dp;
    public static int xPlayer = R.drawable.ic_cross_black_50dp;
    public int size = 9;
    public int width = 100;
    public int moveCount = 0;
    public int ticTacToeSwitch = R.drawable.ic_circle_black_50dp;
    public int[] score = new int[size];
    public int computerMove;
    private ImageAdapter imageAdapter;
    private Context mContext;

    public TicTacToe(Context c, ImageAdapter imageAdapter, int size, int width) {
        mContext = c;
        this.size = size;
        this.width = width;
        this.imageAdapter = imageAdapter;

        for (int i = 0; i < size; i++) {
            score[i] = 0;
        }
    }

    // Algorytm MiniMax
//-----------------
    int[] minimax(int player, int level) {
        int counter = 0;
        int w = 0;
        // check for winner
        for (int i = 0; i < 9; i++)
            if (score[i] == 0) {
                score[i] = player;
                w = i;  // gdyby był remis
                counter++;     // zliczamy wolne pola

                boolean test = checkForWinner(player);

                score[i] = 0;
                if (test) {
                    if (level == 0) {
                        //score[i] = player;
                        computerMove = i;
                    }
                    return new int[]{player == xPlayer ? -1 : 1, w};
                }
            }

        // sprawdzamy, czy jest remis

        if (counter == 1) {
            if (level == 0) {
                //score[w] = player;
                computerMove = w;
            }
            return new int[]{0, w};
        }

        // wybieramy najkorzystniejszy ruch dla gracza

        int v[];
        int vmax;

        vmax = player == xPlayer ? 2 : -2;

        for (int i = 0; i < 9; i++)
            if (score[i] == 0) {
                score[i] = player;
                v = minimax(player == xPlayer ? oPlayer : xPlayer, level + 1);
                score[i] = 0;

                if (((player == xPlayer) && (v[0] < vmax)) || ((player == oPlayer) && (v[0] > vmax))) {
                    vmax = v[0];
                    w = i;
                }
            }

        if (level == 0) {
            //score[w] = player;
            computerMove = w;

        }
        return new int[]{vmax, w};
    }

    // Method for checking for winner only 3x3 grid
    public boolean checkForWinner(int player) {

        // diagonal

        if ((score[0] == player) && (score[4] == player) && (score[8] == player)) {
            return true;
        }

        if ((score[2] == player) && (score[4] == player) && (score[6] == player)) {
            return true;
        }

        for (int i = 0; i <=6; i += 3) {
            if ((score[i] == player) && (score[i + 1] == player) && (score[i + 2] == player)) {
                return true;
            }
        }
        for (int j = 0; j <=2; j++) {
            if ((score[j] == player) && (score[j + 3] == player) && (score[j + 6] == player)) {
                return true;
            }
        }

        return false;
    }

    // Method for showing Toast with winner or draw
    public void showWinner(int winner) {

        if (winner == R.drawable.ic_circle_black_50dp) {
            Toast.makeText(mContext, "The winner is: Circle ", Toast.LENGTH_SHORT).show();
            imageAdapter.isNotGridClicable = true;
            newGameAlert();
        } else if (winner == R.drawable.ic_cross_black_50dp) {
            Toast.makeText(mContext, "The winner is: Cross ", Toast.LENGTH_SHORT).show();
            imageAdapter.isNotGridClicable = true;
            newGameAlert();
        } else if (winner == 0) {
            Toast.makeText(mContext, "Draw", Toast.LENGTH_SHORT).show();
            imageAdapter.isNotGridClicable = true;
            newGameAlert();
        }
    }

    // Method for showing alert if Yes it starts new game, No close app
    public void newGameAlert() {
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

    // Method for switching image cross/circle
    public int ticTacToeSwitch() {
        if (ticTacToeSwitch == xPlayer) {
            ticTacToeSwitch = oPlayer;
        } else {
            ticTacToeSwitch = xPlayer;
        }
        return ticTacToeSwitch;
    }

    public int multiplayerSwitch(boolean mode) {
        if (mode) {
            ticTacToeSwitch = R.drawable.ic_cross_black_50dp;
        } else {
            ticTacToeSwitch = R.drawable.ic_circle_black_50dp;
        }
        return ticTacToeSwitch;
    }

 /*
*/
}
