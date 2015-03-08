package iojjj.bootstrap.demo.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import iojjj.androidbootstrap.ui.fragments.AbstractFragment;
import iojjj.androidbootstrap.ui.widgets.scalewidget.ResizeRotationView;
import iojjj.bootstrap.demo.R;

/**
 * Example of {@link iojjj.androidbootstrap.ui.widgets.scalewidget.ResizeRotationView}
 */
public class ResizeFragment extends AbstractFragment {

    private ResizeRotationView rotationView;
    private ImageView imageView;
    private Button btnIcon;
    private Button btnLabel;
    private static final float DEFAULT_TEXT_SIZE = 40;
    private float textSize = DEFAULT_TEXT_SIZE;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resize, container, false);
        rotationView = (ResizeRotationView) v.findViewById(R.id.resize_view);
        imageView = (ImageView) v.findViewById(R.id.image_view);
        btnIcon = (Button) v.findViewById(R.id.btn_icon);
        btnLabel = (Button) v.findViewById(R.id.btn_label);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rotationView.initDragAndDrop();
        rotationView.setOnScaleListener(new ResizeRotationView.OnScaleListener() {
            @Override
            public void onScaled(float scaleFactor) {
                textSize = DEFAULT_TEXT_SIZE * scaleFactor;
//                setText();
            }
        });
        setText();
        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            }
        });
        btnLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText();
            }
        });
    }

    private void setText() {
        imageView.setImageBitmap(textAsBitmap(getActivity(), "Label Text", textSize, Color.BLACK));
    }

    public static Bitmap textAsBitmap(Context context, String text, float textSize, int bgColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize + 2.0f);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);

        Paint paintBackgroundText = new Paint();
        paintBackgroundText.setTextSize(textSize + 2.0f);
        paintBackgroundText.setColor(Color.WHITE);
        paintBackgroundText.setFakeBoldText(true);
        paintBackgroundText.setTextAlign(Paint.Align.LEFT);
        paintBackgroundText.setStyle(Paint.Style.STROKE);

        paintBackgroundText.setStrokeWidth(convertDpToPixel(context, 2));

        paint.setAntiAlias(true);
        Paint bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);
        bgPaint.setColor(Color.TRANSPARENT);
        int numberOfWords = text.split(" ").length;
        float longestWordWidth = 0.0f;
        String longestWord = "";

        for (int i = 0; i < numberOfWords; i++) {
            float currentWidth = paint.measureText(text.split(" ")[i]);
            if (currentWidth > longestWordWidth) {
                longestWord = text.split(" ")[i];
                longestWordWidth = currentWidth;
            }
        }

        int width = (int) (paint.measureText(longestWord) + 0.75f); // round
        float baseline = (int) (-paint.ascent() + 0.6f); // ascent() is negative
        int height = (int) ((baseline + paint.descent() + 0.5f) * (numberOfWords));

        if (height <= 1) {
            height = 100;
        }

        if (width <= 1) {
            width = 100;
        }

        Bitmap image = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int offset = (int) convertDpToPixel(context, 6);
        Bitmap bmp = Bitmap.createBitmap(image.getWidth() + offset, image.getHeight() + offset, Bitmap.Config.ARGB_8888);

        RectF rect = new RectF(0, 0, bmp.getWidth(), bmp.getHeight());

        Canvas canvas = new Canvas(bmp);
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(convertDpToPixel(context, 1));

        for (int i = numberOfWords - 1; i >= 0; i--) {
            String currentWord = text.split(" ")[i];
            canvas.drawText(currentWord, ((bmp.getWidth() / 2) - (paint.measureText(currentWord) / 2)), (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * i) + 0.7f, paintBackgroundText);
            canvas.drawText(currentWord, ((bmp.getWidth() / 2) - (paint.measureText(currentWord) / 2)), (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * i) + 0.7f, paint);

            if (i == numberOfWords - 1) {
                Paint fillpaint = new Paint();
                fillpaint.setAntiAlias(true);
                fillpaint.setColor(bgColor);
                fillpaint.setStyle(Paint.Style.FILL_AND_STROKE);
                fillpaint.setDither(false);
                fillpaint.setStrokeWidth(3);

                Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(bmp.getWidth() / 2 + (float) Math.pow(-1, i) * (i % 20), 12 + (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * (i + 1)) + 0.7f);
                path.lineTo(bmp.getWidth() / 2 - 22, rect.height());
                path.lineTo(bmp.getWidth() / 2 + 22, rect.height());
                path.lineTo(bmp.getWidth() / 2 + (float) Math.pow(-1, i) * (i % 20), 12 + (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * (i + 1)) + 0.7f);
                path.close();

                Path path2 = new Path();
                path2.setFillType(Path.FillType.EVEN_ODD);

                path2.moveTo(bmp.getWidth() / 2 + (float) Math.pow(-1, i) * (i % 20), 12 + (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * (i + 1)) + 0.7f);
                path2.lineTo(bmp.getWidth() / 2 - 22, rect.height());
                path2.moveTo(bmp.getWidth() / 2 + (float) Math.pow(-1, i) * (i % 20), 12 + (float) (baseline * 1.3 + (image.getHeight() / numberOfWords) * (i + 1)) + 0.7f);
                path2.lineTo(bmp.getWidth() / 2 + 22, rect.height());

                path2.close();
            }
        }

        return bmp;
    }

    private static float convertDpToPixel(@NotNull final Context context, final float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }


}
