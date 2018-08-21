package com.github.hurshi.clickedwordslib.listener;

import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.widget.TextView;

public interface OnWordsClickedListener {
    void wordsClicked(TextView textView, String words, Pair<Integer, Integer> index, Rect focusedRect, int[] locationInScreen);
}
