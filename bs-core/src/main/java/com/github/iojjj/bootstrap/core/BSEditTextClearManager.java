/**
 * Copyright 2014 Alex Yanchenko
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.iojjj.bootstrap.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

/**
 * Helper class for adding functionality of clearing of EditText.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSEditTextClearManager {

    private EditText mEditText;
    private Drawable mClearDrawable;
    private OnClearListener mOnClearListener;
    private OnTouchListener mOnTouchListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    private BSEditTextClearManager(@NonNull EditText editText, @NonNull Drawable clearDrawable) {
        mEditText = editText;
        mClearDrawable = clearDrawable;
        init();
    }

    private void init() {
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        mEditText.setOnTouchListener((v, event) -> {
            if (mEditText.getCompoundDrawables()[2] != null) {
                boolean tappedX = event.getX() > (mEditText.getWidth() - mEditText.getPaddingRight() - mClearDrawable.getIntrinsicWidth());
                if (tappedX) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mEditText.setText("");
                        if (mOnClearListener != null) {
                            mOnClearListener.didClearText();
                        }
                    }
                    return true;
                }
            }
            return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
        });
        mEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setClearIconVisible(!TextUtils.isEmpty(mEditText.getText()));
            } else {
                setClearIconVisible(false);
            }
            if (mOnFocusChangeListener != null) {
                mOnFocusChangeListener.onFocusChange(v, hasFocus);
            }
        });
        mEditText.addTextChangedListener(new BSSimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.isFocused()) {
                    setClearIconVisible(!TextUtils.isEmpty(mEditText.getText()));
                }
            }
        });
    }

    public void setClearIconVisible(boolean visible) {
        Drawable x = visible ? mClearDrawable : null;
        mEditText.setCompoundDrawables(mEditText.getCompoundDrawables()[0],
                mEditText.getCompoundDrawables()[1], x, mEditText.getCompoundDrawables()[3]);
    }

    public void setOnClearListener(OnClearListener onClearListener) {
        mOnClearListener = onClearListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    public interface OnClearListener {
        void didClearText();
    }

    public static class Builder {
        private EditText mEditText;
        private Drawable mClearDrawable;
        private Context mContext;

        public Builder(@NonNull Context context) throws AssertionError {
            BSAssertions.assertNotNull(context, "context");
            mContext = context;
        }

        public Builder setEditText(@NonNull EditText editText) throws AssertionError {
            BSAssertions.assertNotNull(mEditText, "editText");
            mEditText = editText;
            return this;
        }

        public Builder setClearDrawable(@NonNull Drawable clearDrawable) throws AssertionError {
            BSAssertions.assertNotNull(clearDrawable, "clearDrawable");
            mClearDrawable = clearDrawable;
            return this;
        }

        public Builder setClearDrawable(@DrawableRes int drawableId) {
            mClearDrawable = ContextCompat.getDrawable(mContext, drawableId);
            return this;
        }

        public BSEditTextClearManager build() {
            return new BSEditTextClearManager(mEditText, mClearDrawable);
        }
    }
}