package com.github.hurshi.clickedwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hurshi.clickedwordslib.ClickedWords;
import com.github.hurshi.clickedwordslib.OnWordsClickedListener;

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
                .setBottomDialog(wordDetailDialog)
                .setFragmentManager(getSupportFragmentManager())
                .setFocusedBgColor(R.color.focusedBgColor)
                .setFocusedFgColor(R.color.focusedFgColor)
                .build();


    }
}
