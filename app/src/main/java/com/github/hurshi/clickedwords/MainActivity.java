package com.github.hurshi.clickedwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.hurshi.clickedwordslib.ClickedWords;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    WordDetailDialog wordDetailDialog = new WordDetailDialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);

        new ClickedWords.Builder()
                .setTextView(textView)
                .setWordDetailDialog(wordDetailDialog)
                .setFragmentManager(getSupportFragmentManager())
                .setFocusedBgColor(R.color.focusedBgColor)
                .setFocusedFgColor(R.color.focusedFgColor)
                .build();


    }
}
