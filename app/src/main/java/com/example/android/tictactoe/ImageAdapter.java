package com.example.android.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominic on 2017-07-13.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public boolean isNotGridClicable = false;
    private int width;
    private List<Integer> mThumbIds = new ArrayList<>();

    public ImageAdapter(Context c, int tab, int width) {
        mContext = c;
        this.width = width;

        for (int i = 0; i< tab; i++) {
            mThumbIds.add(R.drawable.grid_border_inside);
        }
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //16 75// 25 60 // 36 50 // 49 43 // 64 37 / 81 33 / 100
            int x = MainActivity.getPixelsFromDPs((Activity) mContext, width);
            imageView.setLayoutParams(new GridView.LayoutParams(x, x));

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundResource(mThumbIds.get(position));

        return imageView;

    }


    @Override
    public boolean isEnabled(int position) {
        if(isNotGridClicable){
            return false;
        }
        else{
            return super.isEnabled(position);
        }

    }

}
