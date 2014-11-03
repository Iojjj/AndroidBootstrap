package iojjj.androidbootstrap.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import iojjj.androidbootstrap.R;

/**
 * Implementation of {@link android.widget.EditText} with custom font's typeface
 */
public class TypefacedEditText extends EditText {

    public TypefacedEditText(Context context) {
        super(context);
    }

    public TypefacedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TypefacedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = TypefacedUtils.getTypeface(context, fontName);
            setTypeface(typeface);
        }
    }


	private static final OnPreImeEventListener PRE_IME_EVENT_LISTENER_STUB = new OnPreImeEventListener() {
		@Override
		public boolean onPreImeEvent(int keyCode, KeyEvent event) {
			return false;
		}
	};

	private OnPreImeEventListener mPreImeEventListener = PRE_IME_EVENT_LISTENER_STUB;

	public void setOnPreImeEventListener(OnPreImeEventListener preImeEventListener) {
		mPreImeEventListener = preImeEventListener;
	}


	@Override
	public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
		return mPreImeEventListener.onPreImeEvent(keyCode, event)
			|| super.onKeyPreIme(keyCode, event);
	}


	public interface OnPreImeEventListener {
		public boolean onPreImeEvent(int keyCode, KeyEvent event);
	}
}
