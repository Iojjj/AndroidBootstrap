package com.github.iojjj.bootstrap.core.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

// TODO: 23.09.2016 documentation
public class BSScreenUtil {

    private BSScreenUtil() {
        //no instance
    }

    public static float dpToPx(@NonNull final Context context, float dp) {
        BSAssertions.assertNotNull(context, "context");
        return context.getResources().getDisplayMetrics().density * dp;
    }

    @SuppressWarnings("deprecation")
    public static void getScreenDimensions(@NonNull Context context, @NonNull final Point point) {
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotNull(point, "point");
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(point);
        } else {
            point.x = windowManager.getDefaultDisplay().getWidth();
            point.y = windowManager.getDefaultDisplay().getHeight();
        }
    }

    public static void getScreenDimensions(@NonNull Context context, @NonNull DisplayMetrics displayMetrics) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    public static float pxToDp(@NonNull final Context context, float px) {
        BSAssertions.assertNotNull(context, "context");
        return px / context.getResources().getDisplayMetrics().density;
    }
}
