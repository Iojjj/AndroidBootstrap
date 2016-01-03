package iojjj.android.bootstrap.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that displays image with specific aspect ratio.
 */
public class AspectImageView extends ImageView {

    private float aspectX = -1;
    private float aspectY = -1;

    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setAspect(float aspectX, float aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        invalidate();
    }

    private void init(final Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectImageView);
        aspectX = array.getFloat(R.styleable.AspectImageView_aiv_aspectX, -1);
        aspectY = array.getFloat(R.styleable.AspectImageView_aiv_aspectY, -1);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null || aspectX < 0 || aspectY < 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        final int width;
        final int height;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = (int) (1f * width * aspectY / aspectX);
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = (int) (1f * height * aspectX / aspectY);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    public float getAspectY() {
        return aspectY;
    }

    public float getAspectX() {
        return aspectX;
    }
}