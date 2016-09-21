package com.github.iojjj.bootstrap.core;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

/**
 * Created by cvetl on 06.06.2016.
 */
public class BSScreenManager {

    private BSScreenManager() {
        //no instance
    }

    public static float dpToPx(@NonNull final Context context, float dp) {
        BSAssertions.assertNotNull(context, "Context");
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static float pxToDp(@NonNull final Context context, float px) {
        BSAssertions.assertNotNull(context, "Context");
        return px / context.getResources().getDisplayMetrics().density;
    }

    @SuppressWarnings("deprecation")
    public static void getScreenDimensions(@NonNull Context context, @NonNull final Point point) {
        BSAssertions.assertNotNull(context, "Context");
        BSAssertions.assertNotNull(point, "Point");
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
}
