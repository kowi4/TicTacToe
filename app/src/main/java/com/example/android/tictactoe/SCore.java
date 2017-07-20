package com.example.android.tictactoe;

/**
 * Created by Dominic on 2017-07-19.
 */

public class SCore {

    public SCore(int resourceId, int position) {
        this.position = position;

        if (resourceId==R.drawable.ic_circle_black_50dp){
            this.resourceId = "circle";
        }
        else {
            this.resourceId = "cross";
        }
    }

    public SCore(){}

    public int position;
    public String resourceId;
}
