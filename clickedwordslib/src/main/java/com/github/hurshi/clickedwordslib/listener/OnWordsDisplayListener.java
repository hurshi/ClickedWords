package com.github.hurshi.clickedwordslib.listener;

import android.widget.PopupWindow;
import android.widget.TextView;

public interface OnWordsDisplayListener {

    PopupWindow getInitedPopupWindow();

    String getCleanedWord(String word);

    void wordFetched(PopupWindow popupWindow, String words);

    void showPopupWindow(PopupWindow popupWindow, TextView textView, int offsetX, int offsetY);

    void hidePopupWindow(PopupWindow popupWindow);
}
