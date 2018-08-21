package com.github.hurshi.clickedwordslib.ui;

import android.widget.PopupWindow;
import android.widget.TextView;

public interface OnWordsDisplayListener {
    void wordDisplay(String words);

    void showPopupWindow(PopupWindow popupWindow, TextView textView, int offsetX, int offsetY);
}
