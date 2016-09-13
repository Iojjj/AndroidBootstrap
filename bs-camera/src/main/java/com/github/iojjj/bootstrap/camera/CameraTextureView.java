package com.github.iojjj.bootstrap.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by cvetl on 11.08.2016.
 */

public class CameraTextureView extends TextureView {

    private int mRatioWidth;
    private int mRatioHeight;
    private ScaleType mScaleType = ScaleType.NORMAL;

    public CameraTextureView(Context context) {
        this(context, null);
    }

    public CameraTextureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraTextureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mRatioWidth == 0 || mRatioHeight == 0) {
            // this will reset view's width and height to be as large as parent
            // this will fix stretching of front camera preview
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE / 2, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE / 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                if (mScaleType == ScaleType.NORMAL) {
                    setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
                } else {
                    setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
                }
            } else {
                if (mScaleType == ScaleType.NORMAL) {
                    setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
                } else {
                    setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
                }
            }
        }
    }

    public enum ScaleType {
        CENTER_CROP,
        NORMAL
    }
}
