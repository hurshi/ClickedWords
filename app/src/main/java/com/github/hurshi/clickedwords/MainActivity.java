package com.github.hurshi.clickedwords;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.hurshi.clickedwordslib.core.ClickedWords;
import com.github.hurshi.clickedwordslib.helper.ClickedWordsDisplayer;
import com.github.hurshi.clickedwordslib.ui.OnWordsDisplayListener;

public class MainActivity extends AppCompatActivity implements OnWordsDisplayListener {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private PopupWindow popupWindow;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textview2);
        textView3 = (TextView) findViewById(R.id.textview3);

        setTextViewSpanStr();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.view_words, null);
        tv = (TextView) popupView.findViewById(R.id.textview);

        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        if (Build.VERSION.SDK_INT >= 21)
            popupWindow.setElevation(20);

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

    @Override
    public void wordDisplay(String words) {
        tv.setText(words);
    }

    @Override
    public void showPopupWindow(PopupWindow popupWindow, TextView textView, int offsetX, int offsetY) {
        popupWindow.showAtLocation(textView, Gravity.CENTER_HORIZONTAL | Gravity.TOP, offsetX, offsetY);
    }

    private void setClickedWords(TextView textView) {
        ClickedWordsDisplayer.showAsPopupWindow(
                new ClickedWords.Builder()
                        .setTextView(textView),
                popupWindow,
                this,
                R.color.focusedFgColor,
                R.color.focusedBgColor
        );
    }

}
