package com.github.hurshi.clickedwordslib.util;

import android.content.Context;

public class ClickWordsUtils {
    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context aty) {
        return aty.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Context aty) {
        return aty.getResources().getDisplayMetrics().heightPixels;
    }
}
