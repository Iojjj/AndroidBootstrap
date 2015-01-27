package iojjj.androidbootstrap.ui.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * TextView that doesn't acquire touches
 */
public class NotPressableTypefacedTextView extends TypefacedTextView {

    public NotPressableTypefacedTextView(Context context) {
        super(context);
    }

    public NotPressableTypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }
}
