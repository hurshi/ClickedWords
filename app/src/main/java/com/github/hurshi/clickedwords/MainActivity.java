package com.github.hurshi.clickedwords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.github.hurshi.clickedwordslib.ClickedWords;

public class MainActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    WordDetailDialog wordDetailDialog = new WordDetailDialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textview2);
        textView3 = (TextView) findViewById(R.id.textview3);

        setTextViewSpanStr();
        setClickedWords(textView1);
        setClickedWords(textView2);
        setClickedWords(textView3);

    }

    private void setTextViewSpanStr() {
        SpannableString spannableString = new SpannableString(textView2.getText().toString());
        spannableString.setSpan(new ForegroundColorSpan(textView2.getContext().getResources().getColor(R.color.normal_txt_color)), 0, 10, 34);
        spannableString.setSpan(new BackgroundColorSpan(textView2.getContext().getResources().getColor(R.color.normal_txt_bg_color)), 0, 20, 34);
        textView2.setText(spannableString);
    }

    private void setClickedWords(TextView textView) {
        new ClickedWords.Builder()
                .setTextView(textView)
                .setWordDetailDialog(wordDetailDialog)
                .setFragmentManager(getSupportFragmentManager())
                .setFocusedBgColor(R.color.focusedBgColor)
                .setFocusedFgColor(R.color.focusedFgColor)
                .build();
    }
}
