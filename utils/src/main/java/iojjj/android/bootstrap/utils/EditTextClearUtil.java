/**
 * Copyright 2014 Alex Yanchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package iojjj.android.bootstrap.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Helper class for adding functionality of clearing of EditText.
 */
public class EditTextClearUtil {

    private EditText editText;
    private Drawable clearDrawable;
    private OnClearListener onClearListener;
    private OnTouchListener onTouchListener;
    private OnFocusChangeListener onFocusChangeListener;

    private EditTextClearUtil(@NonNull EditText editText, @NonNull Drawable clearDrawable) {
        this.editText = editText;
        this.clearDrawable = clearDrawable;
        init();
    }

    private void init() {
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editText.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (editText.getWidth() - editText.getPaddingRight() - clearDrawable.getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            editText.setText("");
                            if (onClearListener != null) {
                                onClearListener.didClearText();
                            }
                        }
                        return true;
                    }
                }
                return onTouchListener != null && onTouchListener.onTouch(v, event);
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setClearIconVisible(!TextUtils.isEmpty(editText.getText()));
                } else {
                    setClearIconVisible(false);
                }
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.isFocused()) {
                    setClearIconVisible(!TextUtils.isEmpty(editText.getText()));
                }
            }
        });
    }

    public void setClearIconVisible(boolean visible) {
        Drawable x = visible ? clearDrawable : null;
        editText.setCompoundDrawables(editText.getCompoundDrawables()[0],
                editText.getCompoundDrawables()[1], x, editText.getCompoundDrawables()[3]);
    }

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public interface OnClearListener {
        void didClearText();
    }

    public static class Builder {
        private EditText editText;
        private Drawable clearDrawable;
        private Context context;

        public Builder(@NonNull Context context) throws AssertionError {
            AssertionUtils.assertNotNull(context, "Context");
            this.context = context;
        }

        public Builder setEditText(@NonNull EditText editText) {
            this.editText = editText;
            return this;
        }

        public Builder setClearDrawable(@NonNull Drawable clearDrawable) {
            this.clearDrawable = clearDrawable;
            return this;
        }

        public Builder setClearDrawable(@DrawableRes int drawableId) {
            this.clearDrawable = VersionUtils.getDrawable(context.getResources(), drawableId);
            return this;
        }

        public EditTextClearUtil build() throws AssertionError {
            AssertionUtils.assertNotNull(editText, "EditText");
            AssertionUtils.assertNotNull(editText, "ClearDrawable");
            return new EditTextClearUtil(editText, clearDrawable);
        }
    }
}