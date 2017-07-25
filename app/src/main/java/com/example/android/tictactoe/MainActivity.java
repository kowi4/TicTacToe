package com.example.android.tictactoe;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.android.tictactoe.FirebaseConnection.databaseReference;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    // 9 100 //16 75// 25 60 // 36 50 // 49 43 // 64 37 / 81 33 / 100 30

    public boolean mode;
    ImageAdapter imageAdapter = new ImageAdapter(this, 9, 100);
    TicTacToe ticTacToe = new TicTacToe(this,imageAdapter,9,100);
    //public GridView boardGridView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageButton yourTurnButton = (ImageButton) findViewById(R.id.yourTurnButton);
        yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch);

        //drawing a board
        final GridView boardGridView = (GridView) findViewById(R.id.gridview);
        boardGridView.setNumColumns((int) sqrt(9));
        boardGridView.setLayoutParams(new LinearLayout.LayoutParams(getPixelsFromDPs(MainActivity.this, 300),
                                                                    getPixelsFromDPs(MainActivity.this, 300)));
        boardGridView.setAdapter(imageAdapter);
        boardGridView.setVerticalScrollBarEnabled(false);
        //Disable scrolling on touch
        boardGridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });

        //onClick action for 2 players on one device
//        boardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//
//                int player = ticTacToe.ticTacToeSwitch;
//                ImageView view = (ImageView) v;
//                view.setImageResource(player);
//                view.setOnClickListener(null);
//                ticTacToe.score[position] = player;
//                if (ticTacToe.checkForWinner(player)) {
//                    ticTacToe.showWinner(player);
//                }
//                ticTacToe.checkForDrow();
//                yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch());
//            }
//        });
        //on click action for AI vs player


        boardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                int player = ticTacToe.ticTacToeSwitch;
                ImageView view = (ImageView) v;
                view.setImageResource(player);
                view.setOnClickListener(null);
                ticTacToe.score[position] = player;
                if(ticTacToe.checkForWinner(player)){
                    ticTacToe.showWinner(player);
                }
                ticTacToe.checkForDrow();
                yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch());
                //ticTacToe.minimax(R.drawable.ic_cross_black_50dp,0);
                //ticTacToe.score[computerMove] = ticTacToe.ticTacToeSwitch;
                //ImageView imageView = (ImageView) getViewByPosition(computerMove, boardGridView);
                //imageView.setImageResource(ticTacToe.ticTacToeSwitch);
                //ticTacToe.ticTacToeSwitch();
            }

        });

    }


    public static View getViewByPosition(int pos, GridView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    // Method for converting DP value to pixels //stackoverflow
    public static int getPixelsFromDPs(Activity activity, int dps) {
        Resources r = activity.getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

}