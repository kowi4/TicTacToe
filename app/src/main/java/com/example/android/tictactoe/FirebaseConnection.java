package com.example.android.tictactoe;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Dominic on 2017-07-18.
 */

public class FirebaseConnection extends Application{
    FirebaseDatabase database;
    public static DatabaseReference databaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("TicTacToeInstance");
//        databaseReference.setValue("Test", "Hello World");
    }
}
