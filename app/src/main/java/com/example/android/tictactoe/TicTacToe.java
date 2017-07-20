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

    private ImageAdapter imageAdapter;
    private Context mContext;
    public int size = 9;
    public int width = 100;
    public int moveCount = 0;
    public int ticTacToeSwitch = R.drawable.ic_cross_black_50dp;
    public int[] score = new int[size];
    //DatabaseReference databaseReference;

    public TicTacToe(Context c, ImageAdapter imageAdapter, int size, int width) {
        mContext = c;
        this.size = size;
        this.width = width;
        this.imageAdapter = imageAdapter;

        for (int i = 0; i < size; i++) {
            score[i] = 0;
        }

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // this.databaseReference = database.getReference("TicTacToeInstance");
    }

    // Method for checking for winner only 3x3 grid
    public void checkForWinner() {

        if (score[0] == score[1] && score[1] == score[2] && score[0] != 0) {
            showWinner(score[0]);
            moveCount = 0;
        }
        if (score[3] == score[4] && score[4] == score[5] && score[3] != 0) {
            showWinner(score[3]);
            moveCount = 0;
        }
        if (score[6] == score[7] && score[7] == score[8] && score[8] != 0) {
            showWinner(score[6]);
            moveCount = 0;
        }
        if (score[0] == score[3] && score[3] == score[6] && score[6] != 0) {
            showWinner(score[0]);
            moveCount = 0;
        }
        if (score[1] == score[4] && score[4] == score[7] && score[7] != 0) {
            showWinner(score[1]);
            moveCount = 0;
        }
        if (score[2] == score[5] && score[5] == score[8] && score[8] != 0) {
            showWinner(score[2]);
            moveCount = 0;
        }
        if (score[0] == score[4] && score[4] == score[8] && score[8] != 0) {
            showWinner(score[0]);
            moveCount = 0;
        }
        if (score[2] == score[4] && score[4] == score[6] && score[6] != 0) {
            showWinner(score[2]);
            moveCount = 0;
        }
        moveCount++;
    }

    public void checkForDrow() {
        if (moveCount == size/2+1) {
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
        alertBuild.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) mContext).finish();
            }
        });
        alertBuild.setOnCancelListener(null);
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
            ticTacToeSwitch =R.drawable.ic_circle_black_50dp;
        }
        return ticTacToeSwitch;
    }

 /*
*/
}
