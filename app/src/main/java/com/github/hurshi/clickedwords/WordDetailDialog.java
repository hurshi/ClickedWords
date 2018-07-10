package com.github.hurshi.clickedwords;

import android.view.View;
import android.widget.TextView;

public class WordDetailDialog extends com.github.hurshi.clickedwordslib.WordDetailDialog {
    public WordDetailDialog() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_words;
    }

    @Override
    public void setUpView(View v, String word) {
//        Toast.makeText(getContext(), word, Toast.LENGTH_SHORT).show();
        ((TextView) (v.findViewById(R.id.textview))).setText(word);
    }

    @Override
    public String getFragmentTag() {
        return "WordDetailDialog";
    }
}
