package iojjj.androidbootstrap.utils.multimedia;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

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
        c.drawCircle((bitmap.getWidth()) / 2, (bitmap.getHeight()) / 2, (bitmap.getWidth()) / 2, paint);
        return circleBitmap;
    }
}