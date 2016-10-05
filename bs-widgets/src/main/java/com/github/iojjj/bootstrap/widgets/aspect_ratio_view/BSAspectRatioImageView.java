package com.github.iojjj.bootstrap.widgets.aspect_ratio_view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cvetl on 16.07.2016.
 */

public class BSAspectRatioImageView extends ImageView implements AspectRatioView, AspectRatio {

    private final AspectRatioImpl mHelper = new AspectRatioImpl();

    public BSAspectRatioImageView(Context context) {
        this(context, null);
    }

    public BSAspectRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BSAspectRatioImageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHelper.init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BSAspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mHelper.init(context, attrs);
    }

    @Override
    public float getRatioX() {
        return mHelper.getRatioX();
    }

    @Override
    public float getRatioY() {
        return mHelper.getRatioY();
    }

    @Override
    public void setAspectRatio(float ratioX, float ratioY) {
        mHelper.setAspectRatio(ratioX, ratioY);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHelper.onMeasure(this, widthMeasureSpec, heightMeasureSpec);
    }
}
