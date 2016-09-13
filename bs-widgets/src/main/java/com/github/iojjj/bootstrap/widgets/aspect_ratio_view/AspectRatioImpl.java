package com.github.iojjj.bootstrap.widgets.aspect_ratio_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.iojjj.bootstrap.widgets.R;

/**
 * Created by cvetl on 16.07.2016.
 */

class AspectRatioImpl implements AspectRatio {

    private float mRatioX = -1;
    private float mRatioY = -1;

    void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioView);
        try {
            mRatioX = array.getFloat(R.styleable.AspectRatioView_arv_aspectX, -1);
            mRatioY = array.getFloat(R.styleable.AspectRatioView_arv_aspectY, -1);
        } finally {
            array.recycle();
        }
    }

    public void setAspectRatio(float ratioX, float ratioY) {
        mRatioX = ratioX;
        mRatioY = ratioY;
    }

    @Override
    public float getRatioX() {
        return mRatioX;
    }

    @Override
    public float getRatioY() {
        return mRatioY;
    }

    void onMeasure(@NonNull AspectRatioView view, int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatioX < 0 || mRatioY < 0) {
            view.onMeasureImpl(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        final int width;
        final int height;
        if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.EXACTLY) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
            height = (int) (1f * width * mRatioY / mRatioX);
        } else if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
            height = View.MeasureSpec.getSize(heightMeasureSpec);
            width = (int) (1f * height * mRatioX / mRatioY);
        } else {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
            height = View.MeasureSpec.getSize(heightMeasureSpec);
        }
        view.setMeasuredDimensionImpl(width, height);
    }
}
