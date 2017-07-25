package com.example.android.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Dominic on 2017-07-14.
 */

public class TicTacToe {

    private ImageAdapter imageAdapter;
    private Context mContext;
    public int size = 9;
    public int width = 100;
    public int moveCount = 0;
    public int ticTacToeSwitch = R.drawable.ic_circle_black_50dp;
    public int[] score = new int[size];

    public TicTacToe(Context c, ImageAdapter imageAdapter, int size, int width) {
        mContext = c;
        this.size = size;
        this.width = width;
        this.imageAdapter = imageAdapter;

        for (int i = 0; i < size; i++) {
            score[i] = 0;
        }
    }

    public int computerMove() {
        Random random = new Random();
        int r = random.nextInt(9);
        int count = 0;
        while (score[r] != 0) {
            r = random.nextInt(9);
            if (count++ == 100) {
                break;
            }
        }
        return r;

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
        moveCount++;
        return false;
    }

    public void checkForDrow() {
        if (moveCount == size) {
            Toast.makeText(mContext, "draw", Toast.LENGTH_SHORT).show();
            newGameAlert();
        }

    }

    // Method for showing Toast with winner
    public void showWinner(int winner) {
        if (winner == R.drawable.ic_circle_black_50dp) {
            Toast.makeText(mContext, "The winner is: Circle ", Toast.LENGTH_SHORT).show();
            imageAdapter.isNotGridClicable = true;
            newGameAlert();
        } else {
            Toast.makeText(mContext, "The winner is: Cross ", Toast.LENGTH_SHORT).show();
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
        if (ticTacToeSwitch == R.drawable.ic_cross_black_50dp) {
            ticTacToeSwitch = R.drawable.ic_circle_black_50dp;
        } else {
            ticTacToeSwitch = R.drawable.ic_cross_black_50dp;
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
