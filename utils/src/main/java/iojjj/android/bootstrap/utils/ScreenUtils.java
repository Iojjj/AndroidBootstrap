package iojjj.android.bootstrap.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import iojjj.android.bootstrap.assertions.AssertionUtils;

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
    public static void getScreenDimensions(@NonNull Context context, @NonNull final Point point) {
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

    @ScreenSize
    public static int getScreenSize(@NonNull Context context) {
        AssertionUtils.assertNotNull(context, "Context");
        Configuration config = context.getResources().getConfiguration();
        //noinspection ResourceType
        return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
    }

    @IntDef(flag = true, value = {
            Configuration.SCREENLAYOUT_SIZE_UNDEFINED,
            Configuration.SCREENLAYOUT_SIZE_SMALL,
            Configuration.SCREENLAYOUT_SIZE_NORMAL,
            Configuration.SCREENLAYOUT_SIZE_LARGE,
            Configuration.SCREENLAYOUT_SIZE_XLARGE
    })
    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @interface ScreenSize {

    }
}