package com.arny.ideanote.Utils;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class LayoutManagerHelper {
    public static RecyclerView.LayoutManager fromString(Context context, String layoutManagerString) {
        if (layoutManagerString.equals("GridLayoutManager")) {
            return new GridLayoutManager(context, 1);
        } else if (layoutManagerString.equals("StaggeredGridLayoutManager")) {
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            return null;
        }
    }

    public static String toString(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager)
            return "GridLayoutManager";
        else if (layoutManager instanceof StaggeredGridLayoutManager)
            return "StaggeredGridLayoutManager";
        else return null;
    }
}
