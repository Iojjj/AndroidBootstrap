package iojjj.androidbootstrap.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Александр on 12.11.2014.
 */
public class ObservableScrollView extends ScrollView {

    private static final IOnScrollChanged DEFAULT = new IOnScrollChanged() {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {

        }
    };

    private IOnScrollChanged onScrollChanged;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ObservableScrollView setOnScrollChanged(IOnScrollChanged onScrollChanged) {
        if (onScrollChanged == null)
            onScrollChanged = DEFAULT;
        this.onScrollChanged = onScrollChanged;
        return this;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        onScrollChanged.onScrollChanged(l, t, oldl, oldt);
    }

    public interface IOnScrollChanged {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
