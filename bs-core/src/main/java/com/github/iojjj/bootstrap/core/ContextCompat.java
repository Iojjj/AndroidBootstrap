package com.github.iojjj.bootstrap.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cvetl on 20.08.2016.
 */

public class ContextCompat {

    private ContextCompat() {
        //no instance
    }

    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int resId) {
        BSAssertions.assertNotNull(context, "context");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resId);
        }
        //noinspection deprecation
        return context.getResources().getColor(resId);
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        BSAssertions.assertNotNull(context, "context");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        //noinspection deprecation
        return context.getResources().getDrawable(resId);
    }
}
