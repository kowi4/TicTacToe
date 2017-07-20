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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setNumColumns((int) sqrt(9));
        gridview.setLayoutParams(new LinearLayout.LayoutParams(getPixelsFromDPs(MainActivity.this, 300),
                getPixelsFromDPs(MainActivity.this, 300)));
        gridview.setAdapter(imageAdapter);
        gridview.setVerticalScrollBarEnabled(false);
        //Disable scrolling on touch
        gridview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("demo")){
                    databaseReference.child("demo").removeValue();
                    mode = false;
                } else {
                    databaseReference.child("demo").setValue("KURWA GRAMY");
                    mode = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        waitForPlayerMove(gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                ImageView view = (ImageView) v;
                //view.setImageResource(ticTacToe.multiplayerSwitch(mode));
                SCore scoreToSend = new SCore(ticTacToe.multiplayerSwitch(mode),position) ;
                databaseReference.child("demo").push().setValue(scoreToSend).addOnCompleteListener(databaseOnCompleteListener);
                imageAdapter.isNotGridClicable = true;
                view.setOnClickListener(null);
                ticTacToe.score[position] = ticTacToe.multiplayerSwitch(mode);
                ticTacToe.checkForWinner();
                ticTacToe.checkForDrow();
                //waitForPlayerMove(gridview);

            }
        });
    }


    private void waitForPlayerMove(final GridView gridview) {
        databaseReference.child("demo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SCore sd = dataSnapshot.getValue(SCore.class);
                //imageAdapter.isNotGridClicable =false;
                if (sd.resourceId.equals("circle")) {
                    imageAdapter.isNotGridClicable = !mode;
                    ImageView view1 = (ImageView) getViewByPosition(sd.position, gridview);
                    view1.setImageResource(R.drawable.ic_circle_black_50dp);
                    view1.setOnClickListener(null);
                    //Toast.makeText(MainActivity.this, "get Cirlce", Toast.LENGTH_SHORT).show();
                } else {
                    imageAdapter.isNotGridClicable = mode;
                    ImageView view1 = (ImageView) getViewByPosition(sd.position, gridview);
                    view1.setImageResource(R.drawable.ic_cross_black_50dp);
                    view1.setOnClickListener(null);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("chuj", "onComplete: ");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                 MainActivity.this.recreate();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("chuj", "onComplete: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("chuj", "onComplete: ");
            }
        });
        /////////////////////////
    }

    OnCompleteListener<Void> databaseOnCompleteListener =  new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            Log.d("chuj", "onComplete: ");

        }
    };

    public View getViewByPosition(int pos, GridView listView) {
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