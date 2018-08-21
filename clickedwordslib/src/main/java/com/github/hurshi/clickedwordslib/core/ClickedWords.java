package com.github.hurshi.clickedwordslib.core;

import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
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
                        Pair<Integer, Integer> indexs = getWord(textView.getText().toString(), offset);
                        if (indexs.first >= 0 && indexs.first <= textView.getText().length() && indexs.second >= 0 && indexs.second <= textView.getText().length()) {
                            String words = textView.getText().toString().substring(indexs.first, indexs.second);
                            if (null != builder.getListeners() && builder.getListeners().size() > 0) {
                                int[] outLocation = new int[2];
                                textView.getLocationOnScreen(outLocation);
                                Rect r = getSelectedTxtRect(textView, indexs);
                                for (OnWordsClickedListener l : builder.getListeners()) {
                                    if (null != l) {
                                        l.wordsClicked(textView, words, indexs, r, outLocation);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private Rect getSelectedTxtRect(TextView textView, Pair<Integer, Integer> indexs) {
        Rect r = new Rect();
        if (null != indexs && null != indexs.first && null != indexs.second) {
            int lineStart = textView.getLayout().getLineForOffset(indexs.first);
            int lineEnd = textView.getLayout().getLineForOffset(indexs.second);
            r.top = textView.getLayout().getLineTop(lineStart);
            r.bottom = textView.getLayout().getLineBottom(lineEnd);
            r.left = (int) textView.getLayout().getPrimaryHorizontal(indexs.first);
            r.right = (int) textView.getLayout().getPrimaryHorizontal(indexs.second);

            int paddingLeft = textView.getCompoundPaddingLeft();
            int paddingTop = textView.getExtendedPaddingTop();
//        if ((textView.getGravity() & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
//            paddingTop += textView.getVerticalOffset(false);
//        }
            r.offset(paddingLeft, paddingTop);
            int paddingBottom = textView.getExtendedPaddingBottom();
            r.bottom += paddingBottom;
        }
        return r;
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

    public static class Builder {
        private TextView textView;
        private List<OnWordsClickedListener> listeners;
        private TextView[] textViews;


        public TextView getTextView() {
            return textView;
        }

        public Builder setTextView(TextView textView) {
            this.textView = textView;
            return this;
        }

        public List<OnWordsClickedListener> getListeners() {
            return listeners;
        }

        public Builder addListener(OnWordsClickedListener listener) {
            if (null == listeners) {
                listeners = new ArrayList<>();
            }
            if (null != listener && !listeners.contains(listener)) {
                listeners.add(listener);
            }
            return this;
        }

        public TextView[] getTextViews() {
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
            return new ClickedWords(this);

        }
    }


}
