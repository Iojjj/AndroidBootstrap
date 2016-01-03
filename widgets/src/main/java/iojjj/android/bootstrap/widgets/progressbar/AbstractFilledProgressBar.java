package iojjj.android.bootstrap.widgets.progressbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import iojjj.android.bootstrap.utils.ScreenUtils;
import iojjj.android.bootstrap.utils.VersionUtils;
import iojjj.android.bootstrap.widgets.R;

/**
 * Filled progress bar
 */
public abstract class AbstractFilledProgressBar extends View {

    private final RectF borderRect = new RectF();
    private final Paint borderPaint = new Paint();
    private final Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
    private final Paint textPaintWhite = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
    private final Paint arcPaint = new Paint();
    private final Paint arcPaintTransparent = new Paint();
    private final Rect canvasRect = new Rect();
    private int progress = 0;
    private int max = 0;

    public AbstractFilledProgressBar(Context context) {
        super(context);
    }

    public AbstractFilledProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AbstractFilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractFilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FilledProgressBar);
        try {
            borderPaint.setStrokeWidth(array.getDimension(R.styleable.FilledProgressBar_fpb_borderStrokeWidth, 6));
            float textSize = array.getDimension(R.styleable.FilledProgressBar_fpb_textSize, 60);
            textPaint.setTextSize(textSize);
            textPaintWhite.setTextSize(textSize);
            borderPaint.setColor(array.getColor(R.styleable.FilledProgressBar_fpb_borderColor, VersionUtils.getColor(context.getResources(), R.color.fpb_progress_border)));
            textPaintWhite.setColor(array.getColor(R.styleable.FilledProgressBar_fpb_textColorFilled, VersionUtils.getColor(context.getResources(), R.color.fpb_progress_textFilled)));
            textPaint.setColor(array.getColor(R.styleable.FilledProgressBar_fpb_textColorEmpty, VersionUtils.getColor(context.getResources(), R.color.fpb_progress_textEmpty)));
            progress = array.getInteger(R.styleable.FilledProgressBar_fpb_progress, 50);
            max = array.getInteger(R.styleable.FilledProgressBar_fpb_max, 100);
            int resourceId = array.getResourceId(R.styleable.FilledProgressBar_fpb_fillWith, 0);
            Drawable drawable = array.getDrawable(R.styleable.FilledProgressBar_fpb_fillWith);
            if (resourceId > 0) {
                if (drawable == null) {
                    arcPaint.setColor(array.getColor(R.styleable.FilledProgressBar_fpb_fillWith, VersionUtils.getColor(context.getResources(), R.color.fpb_progress_fill)));
                } else {
                    int width = array.getDimensionPixelSize(R.styleable.FilledProgressBar_fpb_shaderWidth, (int) ScreenUtils.dpToPx(context, 144));
                    int height = array.getDimensionPixelSize(R.styleable.FilledProgressBar_fpb_shaderHeight, (int) ScreenUtils.dpToPx(context, 144));
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, width, height);
                    drawable.draw(canvas);
                    BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    arcPaint.setShader(shader);
                }
            } else {
                arcPaint.setColor(array.getColor(R.styleable.FilledProgressBar_fpb_fillWith, VersionUtils.getColor(context.getResources(), R.color.fpb_progress_fill)));
            }
        } finally {
            array.recycle();
        }
        int padding = (int) borderPaint.getStrokeWidth();
        setPadding(padding, padding, padding, padding);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setShadowLayer(2, 2, 2, Color.BLACK);
        textPaintWhite.setStyle(Paint.Style.FILL);
        textPaintWhite.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaintWhite.setShadowLayer(2, 2, 2, Color.BLACK);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaintTransparent.setAntiAlias(true);
        arcPaintTransparent.setStyle(Paint.Style.FILL);
        arcPaintTransparent.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderRect.left = getPaddingLeft() >> 1;
        borderRect.top = getPaddingTop() >> 1;
        canvasRect.left = 0;
        canvasRect.top = 0;

        if (getWidth() > 0) {
            borderRect.right = getWidth() - (getPaddingRight() >> 1);
            borderRect.bottom = getHeight() - (getPaddingBottom() >> 1);
            canvasRect.right = getWidth();
            canvasRect.bottom = getHeight();
        } else {
            borderRect.right = getMeasuredWidth() - (getPaddingRight() >> 1);
            borderRect.bottom = getMeasuredHeight() - (getPaddingBottom() >> 1);
            canvasRect.right = getMeasuredWidth();
            canvasRect.bottom = getMeasuredHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float percent = 1.0f * progress / max;
        String text = getText(progress);
        float textWidth = textPaint.measureText(text);
        float textHeight = (textPaint.descent() + textPaint.ascent());
        float xPos = (borderRect.width() - textWidth) / 2f;
        float yPos = (borderRect.height() - textHeight) / 2f;
        float h = borderRect.bottom * (1 - percent);
        canvas.save();
        canvas.clipRect(borderRect.left, borderRect.top, borderRect.right, h);
        canvas.drawArc(borderRect, 0, 360, false, arcPaintTransparent);
        canvas.drawText(text, xPos, yPos, textPaint);
        canvas.restore();
        canvas.save();
        canvas.clipRect(borderRect.left, h + borderRect.top, borderRect.right, borderRect.bottom);
        canvas.drawArc(borderRect, 0, 360, false, arcPaint);
        canvas.drawText(text, xPos, yPos, textPaintWhite);
        canvas.restore();
        canvas.drawArc(borderRect, 0, 360, false, borderPaint);
    }

    protected abstract String getText(int progress);

    public AbstractFilledProgressBar setProgress(int progress) {
        this.progress = progress;
        invalidate();
        return this;
    }

    public AbstractFilledProgressBar setMax(int max) {
        this.max = max;
        invalidate();
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }
}
