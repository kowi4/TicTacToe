package com.example.android.tictactoe;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    // 9 100 //16 75// 25 60 // 36 50 // 49 43 // 64 37 / 81 33 / 100 30

    public boolean mode = true;
    ImageAdapter imageAdapter = new ImageAdapter(this, 9, 100);
    TicTacToe ticTacToe = new TicTacToe(this,imageAdapter,9,100);

    public static View getViewByPosition(int pos, GridView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting yourTurnButton
        final ImageButton yourTurnButton = (ImageButton) findViewById(R.id.yourTurnButton);
        yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch);

        Button switchModeButton = (Button) findViewById(R.id.switchModeButton);

        //Drawing a board
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

        switchModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                button.setText("Player vs Computer");
                mode = false;
                for (int i = 0; i < 9; i++) {
                    ticTacToe.score[i] = 0;
                    ImageView imageView = (ImageView) getViewByPosition(i, boardGridView);
                    imageView.setImageResource(R.drawable.ic_background_black_50dp);
                }

            }
        });



        boardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (mode) {
                    //onClick action for 2 players on one device
                    int player = ticTacToe.ticTacToeSwitch;
                    ImageView view = (ImageView) v;
                    view.setImageResource(player);
                    view.setOnClickListener(null);
                    ticTacToe.score[position] = player;
                    if (ticTacToe.checkForWinner(player)) {
                        ticTacToe.showWinner(player);
                    }
                    ticTacToe.moveCount++;
                    if (ticTacToe.moveCount == 9) {
                        ticTacToe.showWinner(0);
                    }
                    yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch());
                } else {

                    //On click action for AI vs player
                    int player = ticTacToe.ticTacToeSwitch;
                    ImageView view = (ImageView) v;
                    view.setImageResource(player);
                    view.setOnClickListener(null);
                    ticTacToe.score[position] = player;
                    if (ticTacToe.checkForWinner(player)) {
                        ticTacToe.showWinner(player);
                    }

                    ticTacToe.moveCount++;
                    if (ticTacToe.moveCount == 5) {
                        ticTacToe.showWinner(0);
                    }
                    yourTurnButton.setImageResource(player);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            int[] temp = ticTacToe.minimax(TicTacToe.xPlayer, 0);
                            int computerMove = temp[1];
                            int computer = ticTacToe.ticTacToeSwitch();
                            ticTacToe.score[computerMove] = computer;
                            ImageView imageView = (ImageView) getViewByPosition(computerMove, boardGridView);
                            imageView.setImageResource(computer);
                            if (ticTacToe.checkForWinner(computer)) {
                                ticTacToe.showWinner(computer);
                            }
                            ticTacToe.ticTacToeSwitch();
                        }

                    }, 700); // 5000ms delay

                }
            }
        });


    }

}