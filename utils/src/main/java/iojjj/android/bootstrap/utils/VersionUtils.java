package iojjj.android.bootstrap.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Helper class that performs Android version specific actions
 */
@SuppressWarnings("deprecation")
public class VersionUtils {

    private VersionUtils() {}

    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int drawableId) throws AssertionError {
        AssertionUtils.assertNotEquals(drawableId, 0, "DrawableId");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return resources.getDrawable(drawableId);
        return resources.getDrawable(drawableId, null);
    }

    public static int getColor(@NonNull Resources resources, @ColorRes int colorId) throws AssertionError {
        AssertionUtils.assertNotEquals(colorId, 0, "ColorId");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return resources.getColor(colorId);
        return resources.getColor(colorId, null);
    }
}