package iojjj.android.bootstrap.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * TextView that doesn't acquire touches
 */
public class NotPressableTextView extends TextView {

    public NotPressableTextView(Context context) {
        super(context);
    }

    public NotPressableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }
}
