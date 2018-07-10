package com.github.hurshi.clickedwordslib;

import android.view.View;

public interface WordsDetailLayout {
    int getLayoutResId();

    void setupView(View view, String word);

}
