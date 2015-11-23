package iojjj.android.bootstrap.core.utils.misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;

/**
 * Helper class for working with images
 */
public class ImageUtils {

    public static Bitmap roundImage(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setColor(Color.MAGENTA);
        paint.setAntiAlias(true);
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle((bitmap.getWidth()) / 2f, (bitmap.getHeight()) / 2f, (bitmap.getWidth()) / 2f, paint);
        return circleBitmap;
    }

    public static float dpToPx(@NonNull final Context context, int dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }
}
