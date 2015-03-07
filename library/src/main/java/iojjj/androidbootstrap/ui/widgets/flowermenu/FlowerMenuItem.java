package iojjj.androidbootstrap.ui.widgets.flowermenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;

/**
 * Item of {@link iojjj.androidbootstrap.ui.widgets.flowermenu.FlowerMenu}
 */
public class FlowerMenuItem extends ImageButton {

    private IFlowerMenu flowerMenu;

    public FlowerMenuItem(Context context) {
        super(context);
    }

    public FlowerMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowerMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowerMenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        return flowerMenu != null && flowerMenu.isAnimationInProgress() || super.onTouchEvent(event);
    }

    public void setFlowerMenu(IFlowerMenu flowerMenu) {
        this.flowerMenu = flowerMenu;
    }

    public interface IFlowerMenu {
        boolean isAnimationInProgress();
    }
}
