package iojjj.android.bootstrap.core.ui.widgets.flowermenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
* View that blocks user interaction if {@link iojjj.android.bootstrap.core.ui.widgets.flowermenu.FlowerMenu} is opened
*/
public class TouchView extends View {

    private FlowerMenu flowerMenu;

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFlowerMenu(FlowerMenu flowerMenu) {
        this.flowerMenu = flowerMenu;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return flowerMenu != null && flowerMenu.isOpened() || super.onTouchEvent(event);
    }
}
