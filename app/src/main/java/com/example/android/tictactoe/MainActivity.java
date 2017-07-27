package com.example.android.tictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

import pl.droidsonroids.gif.GifImageView;

import static com.example.android.tictactoe.R.id.yourTournTextView;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    private static final String PREFERENCES_NAME = "ticTacToePreferences";
    public boolean mode;
    int size = 9;
    int width = 100;
    // 9 100 //16 75// 25 60 // 36 50 // 49 43 // 64 37 / 81 33 / 100 30
    ImageAdapter imageAdapter = new ImageAdapter(this, size, width);
    TicTacToe ticTacToe = new TicTacToe(this, imageAdapter, size, width);

    private SharedPreferences preferences;

    // Method for getting view from GridView by position //stackoverflow
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

    // Method for saving data to SharedPreferences
    private void saveDataToSharedPreferences(String tag, String data) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(tag, data);
        preferencesEditor.apply();
    }

    //Actions for AI vs player
    private void actionsForPlayerVsComputer(ImageView v, int position, ImageButton yourTurnButton, final TextView yourTournTextView, final GridView boardGridView) {

        int player = ticTacToe.ticTacToeSwitch;
        v.setImageResource(player);
        v.setOnClickListener(null);
        ticTacToe.board[position] = player;
        if (ticTacToe.checkForWinner(player)) {
            ticTacToe.showWinner(player);
        }

        ticTacToe.moveCount++;
        if (ticTacToe.moveCount == 5) {
            ticTacToe.showWinner(0);
            return;
        }
        imageAdapter.isNotGridClicable = true;
        yourTournTextView.setText("Computer Move");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                int[] temp = ticTacToe.minimax(TicTacToe.xPlayer, 0);
                int computerMove = temp[1];
                int computer = ticTacToe.ticTacToeSwitch();
                ticTacToe.board[computerMove] = computer;
                ImageView imageView = (ImageView) getViewByPosition(computerMove, boardGridView);
                imageView.setImageResource(computer);
                imageView.setOnClickListener(null);
                if (ticTacToe.checkForWinner(computer)) {
                    ticTacToe.showWinner(computer);
                    return;
                }
                ticTacToe.ticTacToeSwitch();
                Log.d("Minimax", temp[0] + " " + computerMove);
                imageAdapter.isNotGridClicable = false;
                yourTournTextView.setText("Your Move");
            }

        }, 700); // 7000ms delay
    }

    //Actions for 2 players on one device
    private void actionForTwoPlayers(ImageView v, int position, ImageButton yourTurnButton, GifImageView winImage) {
        int player = ticTacToe.ticTacToeSwitch;
        v.setImageResource(player);
        v.setOnClickListener(null);
        ticTacToe.board[position] = player;
        if (ticTacToe.checkForWinner(player)) {
            //winImage.setImageResource(R.drawable.transparent_line);
            //winImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //winImage.setVisibility(View.VISIBLE);
            ticTacToe.showWinner(player);
            Log.d("WinningPattern", ticTacToe.placeForWinningBar + "");
        }
        ticTacToe.moveCount++;
        if (ticTacToe.moveCount == 9) {
            ticTacToe.showWinner(0);
        }
        yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        mode = parseBoolean(preferences.getString("mode", "true"));

        //Setting yourTurnButton
        final ImageButton yourTurnButton = (ImageButton) findViewById(R.id.yourTurnButton);
        yourTurnButton.setImageResource(ticTacToe.ticTacToeSwitch);

        final TextView yourTurnTextView = (TextView) findViewById(yourTournTextView);
        //Gif for crossing the winner TODO
        final GifImageView winImage = (GifImageView) findViewById(R.id.winImage);
        //Switching game mode
        Button switchModeButton = (Button) findViewById(R.id.switchModeButton);
        if (mode) {
            switchModeButton.setText("Player vs Player");
        } else {
            switchModeButton.setText("Player vs Computer");
            yourTurnButton.setVisibility(View.GONE);
        }
        switchModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = !mode;
                saveDataToSharedPreferences("mode", mode + "");
                MainActivity.this.recreate();
            }
        });

        //Drawing a board
        final GridView boardGridView = (GridView) findViewById(R.id.gridview);
        boardGridView.setNumColumns((int) sqrt(size));
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
        //Setting onClickListener for boardGridView
        boardGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mode) {
                    actionForTwoPlayers((ImageView) v, position, yourTurnButton, winImage);
                } else {
                    actionsForPlayerVsComputer((ImageView) v, position, yourTurnButton, yourTurnTextView, boardGridView);
                }
            }
        });
    }
}