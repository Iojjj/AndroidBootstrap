package iojjj.android.bootstrap.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Various utils for determining screen size, converting sizes between different formats, etc
 */
public class ScreenUtils {

    private ScreenUtils() {

    }

    public static float dpToPx(@NonNull final Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static float pxToDp(@NonNull final Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    @SuppressWarnings("deprecation")
    public static void getScreenSize(@NonNull Context context, @NonNull final Point point) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            windowManager.getDefaultDisplay().getSize(point);
        } else {
            point.x = windowManager.getDefaultDisplay().getWidth();
            point.y = windowManager.getDefaultDisplay().getHeight();
        }
    }

    public static void getScreenSize(@NonNull Context context, @NonNull DisplayMetrics displayMetrics) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }
}