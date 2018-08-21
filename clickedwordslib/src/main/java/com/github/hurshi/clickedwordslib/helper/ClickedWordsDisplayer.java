package com.github.hurshi.clickedwordslib.helper;

import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.hurshi.clickedwordslib.core.ClickedWords;
import com.github.hurshi.clickedwordslib.listener.OnWordsClickedListener;
import com.github.hurshi.clickedwordslib.listener.OnWordsDisplayListener;
import com.github.hurshi.clickedwordslib.util.ClickWordsUtils;

public class ClickedWordsDisplayer {
    public static void showAsPopupWindow(ClickedWords.Builder builder, final OnWordsDisplayListener listener, final int focusedFgColor, final int focusedBgColor) {
        builder.addListener(new OnWordsClickedListener() {
            @Override
            public void wordsClicked(TextView textView, String words, Pair<Integer, Integer> index, Rect focusedRect, int[] locationInScreen) {
                int x = (int) (locationInScreen[0] + focusedRect.left + (focusedRect.right - focusedRect.left - ClickWordsUtils.getScreenW(textView.getContext())) / 2.0f);
                int y = locationInScreen[1] + focusedRect.bottom;

                if (null != listener) {
                    PopupWindow popupWindow = listener.getInitedPopupWindow();
                    listener.wordFetched(popupWindow, words);
                    listener.showPopupWindow(popupWindow, textView, x, y);
                    setClickedStyle(textView, popupWindow, index, focusedFgColor, focusedBgColor);
                }
            }
        });
        builder.build();
    }

    private static void setClickedStyle(final TextView textView, final PopupWindow popupWindow, Pair<Integer, Integer> indexs, int focusedFgColor, int focusedBgColor) {
        if (null == popupWindow) {
            return;
        }
        final CharSequence spannableTxt = textView.getText();
        setTextViewClicked(textView, indexs, focusedFgColor, focusedBgColor);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.setOnDismissListener(null);
                setTextViewNormal(textView, spannableTxt);
            }
        });
    }

    private static void setTextViewClicked(TextView textView, Pair<Integer, Integer> indexs, int focusedFgColor, int focusedBgColor) {
        if (null == indexs || null == indexs.first || null == indexs.second) {
            return;
        }
        SpannableString spannableString = new SpannableString(textView.getText());
        if (focusedFgColor > 0) {
            spannableString.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(focusedFgColor)), indexs.first, indexs.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        if (focusedBgColor > 0) {
            spannableString.setSpan(new BackgroundColorSpan(textView.getContext().getResources().getColor(focusedBgColor)), indexs.first, indexs.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        textView.setText(spannableString);
    }

    private static void setTextViewNormal(TextView textView, CharSequence txt) {
        textView.setText(txt);
    }

}
