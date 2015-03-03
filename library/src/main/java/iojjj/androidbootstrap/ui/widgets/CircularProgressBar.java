package iojjj.androidbootstrap.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import iojjj.androidbootstrap.R;

/**
 * Circular progress bar
 */
public class CircularProgressBar extends View {

    private final RectF borderRect = new RectF();
    private final RectF arcRect = new RectF();
    private final Paint borderPaint = new Paint();
    private final Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
    private final Paint textPaintWhite = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
    private final Paint arcPaint = new Paint();
    private final Path path = new Path();
    private final Rect textBounds = new Rect();
    private final PointF center = new PointF();
    private final PointF start = new PointF();
    private final PointF end = new PointF();
    private int progress = 0;

    public CircularProgressBar(Context context) {
        super(context);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NotNull Context context, @NotNull AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);
        try {
            borderPaint.setStrokeWidth(array.getDimension(R.styleable.CircularProgressBar_cpb_border_stroke_width, 6));
            float textSize = array.getDimension(R.styleable.CircularProgressBar_cpb_text_size, 60);
            textPaint.setTextSize(textSize);
            textPaintWhite.setTextSize(textSize);
            borderPaint.setColor(array.getColor(R.styleable.CircularProgressBar_cpb_border_color,
                    context.getResources().getColor(R.color.progress_border)));
            textPaint.setColor(array.getColor(R.styleable.CircularProgressBar_cpb_text_color,
                    context.getResources().getColor(R.color.progress_text)));
            arcPaint.setColor(array.getColor(R.styleable.CircularProgressBar_cpb_fill_color,
                    context.getResources().getColor(R.color.progress_fill)));
            progress = array.getInteger(R.styleable.CircularProgressBar_cpb_progress, 50);
        } finally {
            array.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaintWhite.setColor(Color.WHITE);
        textPaintWhite.setStyle(Paint.Style.FILL);
        textPaintWhite.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderRect.left = getPaddingLeft() >> 1;
        borderRect.top = getPaddingTop() >> 1;
        borderRect.right = getMeasuredWidth() - (getPaddingRight() >> 1);
        borderRect.bottom = getMeasuredHeight() - (getPaddingBottom() >> 1);
        center.x = getWidth() >> 1;
        center.y = getHeight() >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float percent = progress / 100f;
        float percent180 = 180 * percent;
        float percent360 = 360 * percent;
        pointOnCircle((int) borderRect.width() >> 1, 90 + percent180, center, start);
        pointOnCircle((int) borderRect.width() >> 1, 270 + percent180, center, end);
        float angle = getSemicircle(start.x, start.y, end.x, end.y, arcRect, Side.RIGHT);
        path.reset();
        path.addArc(arcRect, -angle, percent360);
        canvas.drawPath(path, arcPaint);
        canvas.drawArc(borderRect, 0, 360, false, borderPaint);
        String text = getContext().getString(R.string.percent, progress);
        int textHeight = ((int)(textPaint.descent() + textPaint.ascent()) >> 1);
        int textWidth = (int) textPaint.measureText(text);
        int xPos = canvas.getWidth() - textWidth >> 1;
        int yPos = ((canvas.getHeight() >> 1) - textHeight) ;
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.save();
        canvas.clipRect(xPos, 0, xPos + textWidth, canvas.getHeight() * (1 - percent));
        canvas.drawText(text, xPos, yPos, textPaint);
        canvas.restore();
        canvas.save();
        canvas.clipRect(xPos, canvas.getHeight() * (1 - percent), xPos + textWidth, canvas.getHeight());
        canvas.drawText(text, xPos, yPos, textPaintWhite);
        canvas.restore();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public static void pointOnCircle(float radius, float angleInDegrees, PointF origin, PointF fill) {
        float x = (float)(radius * Math.cos(Math.toRadians(angleInDegrees))) + origin.x;
        float y = (float)(radius * Math.sin(Math.toRadians(angleInDegrees))) + origin.y;
        fill.x = x;
        fill.y = y;
    }

    /**
     *
     * @param xStart vector start point
     * @param yStart
     * @param xEnd vector end point
     * @param yEnd
     * @param ovalRectOUT RectF to store result
     * @param direction direction left/right
     * @return start angle
     */
    public static float getSemicircle(float xStart, float yStart, float xEnd,
                                      float yEnd, RectF ovalRectOUT, Side direction) {

        float centerX = xStart + ((xEnd - xStart) / 2);
        float centerY = yStart + ((yEnd - yStart) / 2);

        double xLen = (xEnd - xStart);
        double yLen = (yEnd - yStart);
        float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

        RectF oval = new RectF(centerX - radius,
                centerY - radius, centerX + radius,
                centerY + radius);

        ovalRectOUT.set(oval);

        double radStartAngle;
        if (direction == Side.LEFT) {
            radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
        } else {
            radStartAngle = Math.atan2(yEnd - centerY, xEnd - centerX);
        }

        return (float) Math.toDegrees(radStartAngle);
    }

    private enum Side {
        LEFT, RIGHT
    }
}
