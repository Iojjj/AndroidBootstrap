package iojjj.androidbootstrap.utils.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Александр on 03.11.2014.
 */
public class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

    private IOnItemClickListener onItemClickListener;
    private GestureDetectorCompat gestureDetector;

    public RecyclerViewClickListener(@NonNull final Context context, @NonNull final IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (view != null && gestureDetector.onTouchEvent(motionEvent))
            onItemClickListener.onItemClick(view, recyclerView.getChildPosition(view));
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

    public interface IOnItemClickListener {
        void onItemClick(@NonNull View view, int position);
    }
}
