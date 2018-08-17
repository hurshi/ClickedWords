package com.github.hurshi.clickedwordslib;

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
    private BreakIterator wordIterator;

    private BreakIterator getWordIterator() {
        if (null == wordIterator) {
            wordIterator = BreakIterator.getWordInstance(Locale.US);
        }
        return wordIterator;
    }

    ClickedWords(final Builder builder) {
        if (null != builder.getTextView()) {
            toBuild(builder, builder.getTextView());
        }
        if (null != builder.getTextViews() && builder.getTextViews().length > 0) {
            for (TextView tv : builder.getTextViews()) {
                if (null != tv)
                    toBuild(builder, tv);
            }
        }
    }

    private void toBuild(final Builder builder, final TextView textView) {
        textView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = motionEvent.getX();
                    downY = motionEvent.getY();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs(downX - motionEvent.getX()) + Math.abs(downY - motionEvent.getY()) < 25) {
                        int offset = textView.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                        Pair<Integer, Integer> positions = getWord(textView.getText().toString(), offset);
                        if (positions.first >= 0 && positions.first <= textView.getText().length() && positions.second >= 0 && positions.second <= textView.getText().length()) {
                            String words = textView.getText().toString().substring(positions.first, positions.second);
                            if (null != builder.getListener()) {
                                builder.getListener().wordsClicked(words);
                            }
                            if (null != builder.getWordDetailDialog()) {
                                showWordDetail(builder, textView, positions, words);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private Pair<Integer, Integer> getWord(String string, int offset) {
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

    private void showWordDetail(final Builder builder, final TextView textView, Pair<Integer, Integer> positions, String words) {
        builder.getWordDetailDialog().clear();
        if (builder.getWordDetailDialog().setWords(words)) {
            final CharSequence spannableTxt = textView.getText();
            setTextViewClicked(builder, textView, positions);
            builder.getWordDetailDialog().setListener(new WordDetailDialog.OnBottomDialogDismissListener() {
                @Override
                public void onDismiss() {
                    setTextViewNormal(textView, spannableTxt);
                }
            });
            builder.getWordDetailDialog().show(builder.getFragmentManager());
        }
    }

    private void setTextViewClicked(Builder builder, TextView textView, Pair<Integer, Integer> positions) {
        SpannableString spannableString = new SpannableString(textView.getText());
        int fgColor = builder.getFocusedFgColor();
        if (fgColor > 0) {
            spannableString.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(fgColor)), positions.first, positions.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        int bgColor = builder.getFocusedBgColor();
        if (bgColor > 0) {
            spannableString.setSpan(new BackgroundColorSpan(textView.getContext().getResources().getColor(bgColor)), positions.first, positions.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        textView.setText(spannableString);
    }

    private void setTextViewNormal(TextView textView, CharSequence txt) {
        textView.setText(txt);
    }

    public static class Builder {
        private FragmentManager fragmentManager;
        private TextView textView;
        private OnWordsClickedListener listener;
        private WordDetailDialog wordDetailDialog;
        private int focusedBgColor;
        private int focusedFgColor;
        private TextView[] textViews;

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

        private WordDetailDialog getWordDetailDialog() {
            return wordDetailDialog;
        }

        public Builder setWordDetailDialog(WordDetailDialog wordDetailDialog) {
            this.wordDetailDialog = wordDetailDialog;
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

        private TextView[] getTextViews() {
            return textViews;
        }

        public Builder setTextViews(TextView[] textViews) {
            this.textViews = textViews;
            return this;
        }

        public ClickedWords build() {
            if (null == textView && (null == textViews || textViews.length <= 0)) {
                throw new IllegalArgumentException("TextView or TextView array can not be null");
            }
            if (null == fragmentManager && null == wordDetailDialog) {
                throw new IllegalArgumentException("WordDetailDialog need FragmentManager not be null");
            }
            return new ClickedWords(this);

        }
    }


}
