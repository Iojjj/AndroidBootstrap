package iojjj.android.bootstrap.widgets.flowermenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Center menu item for {@link iojjj.android.bootstrap.widgets.flowermenu.FlowerMenu}
 */
public class CenterMenuItem extends FlowerMenuItem {

    private final List<OnClickListener> onClickListeners = new ArrayList<>();
    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for (OnClickListener onClickListener : onClickListeners)
                onClickListener.onClick(v);
        }
    };

    public CenterMenuItem(Context context) {
        super(context);
    }

    public CenterMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CenterMenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(onClickListener);
        addOnClickListener(l);
    }

    public void addOnClickListener(OnClickListener onClickListener) {
        if (!onClickListeners.contains(onClickListener))
            onClickListeners.add(onClickListener);
    }

    public void removeOnClickListener(OnClickListener onClickListener) {
        onClickListeners.remove(onClickListener);
    }
}
