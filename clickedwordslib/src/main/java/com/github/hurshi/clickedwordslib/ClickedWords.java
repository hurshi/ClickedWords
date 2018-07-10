package com.github.hurshi.clickedwordslib;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.Locale;

public class ClickedWords {
    private static BreakIterator wordIterator;

    private static BreakIterator getWordIterator() {
        if (null == wordIterator) {
            wordIterator = BreakIterator.getWordInstance(Locale.US);
        }
        return wordIterator;
    }

    private static void setUpClickedWords(final Builder builder) {
        builder.getTextView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int offset = builder.getTextView().getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                    Pair<Integer, Integer> positions = getWord(builder.getTextView().getText().toString(), offset);
                    String words = builder.getTextView().getText().toString().substring(positions.first, positions.second);

                    if (null != builder.getListener()) {
                        builder.getListener().wordsClicked(words);
                    }
                    if (null != builder.getBottomDialog()) {
                        showWordDetail(builder, positions, words);
                    }
                }
                return false;
            }
        });
    }


    private static Pair<Integer, Integer> getWord(String string, int offset) {
        BreakIterator breakIterator = getWordIterator();
        breakIterator.setText(string);

        int start = breakIterator.first();
        int end = breakIterator.next();
        while (end != BreakIterator.DONE) {
            if (offset >= start && offset <= end) {
                break;
            }
            start = end;
            end = breakIterator.next();
        }
        return new Pair<>(start, end);
    }

    private static void showWordDetail(final Builder builder, Pair<Integer, Integer> positions, String words) {
        setTextViewClicked(builder, positions);
        builder.getBottomDialog().setWords(words);
        builder.getBottomDialog().setListener(new BottomDialog.OnBottomDialogDismissListener() {
            @Override
            public void onDismiss() {
                setTextViewNormal(builder.getTextView());
            }
        });
        builder.getBottomDialog().show(builder.getFragmentManager());
    }

    private static void setTextViewClicked(Builder builder, Pair<Integer, Integer> positions) {
        SpannableString spannableString = new SpannableString(builder.getTextView().getText().toString());
        int fgColor = builder.getFocusedFgColor();
        if (fgColor > 0) {
            spannableString.setSpan(new ForegroundColorSpan(builder.getTextView().getContext().getResources().getColor(fgColor)), positions.first, positions.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        int bgColor = builder.getFocusedBgColor();
        if (bgColor > 0) {
            spannableString.setSpan(new BackgroundColorSpan(builder.getTextView().getContext().getResources().getColor(bgColor)), positions.first, positions.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        builder.getTextView().setText(spannableString);
    }

    private static void setTextViewNormal(TextView textView) {
        textView.setText(textView.getText().toString());
    }

    public static class Builder {
        private FragmentManager fragmentManager;
        private TextView textView;
        private OnWordsClickedListener listener;
        private BottomDialog bottomDialog;
        private int focusedBgColor;
        private int focusedFgColor;

        private FragmentManager getFragmentManager() {
            return fragmentManager;
        }

        public Builder setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            return this;
        }

        private TextView getTextView() {
            return textView;
        }

        public Builder setTextView(TextView textView) {
            this.textView = textView;
            return this;
        }

        private OnWordsClickedListener getListener() {
            return listener;
        }

        public Builder setListener(OnWordsClickedListener listener) {
            this.listener = listener;
            return this;
        }

        private BottomDialog getBottomDialog() {
            return bottomDialog;
        }

        public Builder setBottomDialog(BottomDialog bottomDialog) {
            this.bottomDialog = bottomDialog;
            return this;
        }

        private int getFocusedBgColor() {
            return focusedBgColor;
        }

        public Builder setFocusedBgColor(int focusedBgColor) {
            this.focusedBgColor = focusedBgColor;
            return this;
        }

        private int getFocusedFgColor() {
            return focusedFgColor;
        }

        public Builder setFocusedFgColor(int focusedFgColor) {
            this.focusedFgColor = focusedFgColor;
            return this;
        }

        public void build() {
            if (null == textView) {
                throw new IllegalArgumentException("TextView can not be null");
            }
            if (null == fragmentManager && null == bottomDialog) {
                throw new IllegalArgumentException("BottomDialog need FragmentManager not be null");
            }
            setUpClickedWords(this);

        }
    }


}
