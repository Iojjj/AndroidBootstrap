package iojjj.androidbootstrap.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Helper class for custom font's typeface loading
 */
public class TypefacedUtils {
    /**
     * List of used typefaces
     */
    private static final HashMap<String, Typeface> typefaces = new HashMap<String, Typeface>();

    public static Typeface getTypeface(@NonNull final Context context,
                                       @NonNull final String name) {
        if (typefaces.containsKey(name)) {
            return typefaces.get(name);
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), name);
        typefaces.put(name, typeface);
        return typeface;
    }
}
