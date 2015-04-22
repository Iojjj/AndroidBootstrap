package iojjj.androidbootstrap.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import iojjj.androidbootstrap.R;

/**
 * Circular progress bar
 */
public class AbstractCircularProgressBar extends View {

    private final RectF borderRect = new RectF();
    private final Paint borderPaint = new Paint();
    private final PointF center = new PointF();
    private int progress = 0;
    private int max = 0;

    public AbstractCircularProgressBar(Context context) {
        super(context);
    }

    public AbstractCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AbstractCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NotNull Context context, @NotNull AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        try {
            borderPaint.setStrokeWidth(array.getDimension(R.styleable.CircleProgressBar_cpb_borderStrokeWidth, 6));
            borderPaint.setColor(array.getColor(R.styleable.CircleProgressBar_cpb_borderColor, context.getResources().getColor(R.color.cpb_progress_border)));
            progress = array.getInteger(R.styleable.CircleProgressBar_cpb_progress, 50);
            max = array.getInteger(R.styleable.CircleProgressBar_cpb_max, 100);
        } finally {
            array.recycle();
        }
        int padding = (int) borderPaint.getStrokeWidth();
        setPadding(padding, padding, padding, padding);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderRect.left = getPaddingLeft() >> 1;
        borderRect.top = getPaddingTop() >> 1;

        if (getWidth() > 0) {
            center.x = getWidth() >> 1;
            center.y = getHeight() >> 1;
            borderRect.right = getWidth() - (getPaddingRight() >> 1);
            borderRect.bottom = getHeight() - (getPaddingBottom() >> 1);
        } else {
            center.x = getMeasuredWidth() >> 1;
            center.y = getMeasuredHeight() >> 1;
            borderRect.right = getMeasuredWidth() - (getPaddingRight() >> 1);
            borderRect.bottom = getMeasuredHeight() - (getPaddingBottom() >> 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float degPerMax = 360f / max;
        canvas.drawArc(borderRect, -90, progress * degPerMax, false, borderPaint);
    }

    public AbstractCircularProgressBar setProgress(int progress) {
        this.progress = progress;
        invalidate();
        return this;
    }

    public AbstractCircularProgressBar setMax(int max) {
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

    private enum Side {
        LEFT, RIGHT
    }
}
