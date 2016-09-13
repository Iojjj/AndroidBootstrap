package com.github.iojjj.bootstrap.widgets.aspect_ratio_view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by cvetl on 16.07.2016.
 */

public class BSAspectRatioFrameLayout extends FrameLayout implements AspectRatioView, AspectRatio {

    private final AspectRatioImpl mAspectRatio = new AspectRatioImpl();

    public BSAspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    public BSAspectRatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BSAspectRatioFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAspectRatio.init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BSAspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mAspectRatio.init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mAspectRatio.onMeasure(this, widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("WrongCall")
    @Override
    public void onMeasureImpl(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setMeasuredDimensionImpl(int measuredWidth, int measuredHeight) {
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void setAspectRatio(float ratioX, float ratioY) {
        mAspectRatio.setAspectRatio(ratioX, ratioY);
    }

    @Override
    public float getRatioX() {
        return mAspectRatio.getRatioX();
    }

    @Override
    public float getRatioY() {
        return mAspectRatio.getRatioY();
    }
}
