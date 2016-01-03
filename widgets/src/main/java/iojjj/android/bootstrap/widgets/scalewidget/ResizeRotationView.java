package iojjj.android.bootstrap.widgets.scalewidget;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import iojjj.android.bootstrap.widgets.R;

/**
 * View that user can rotate, scale or drag over screen
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ResizeRotationView extends FrameLayout {

    private static final int DEFAULT_COLOR = Color.BLACK;

    private final View[] lines = new View[4];
    private final View[] dots = new View[4];
    private int initWidth, initHeight;
    private float scaleFactor = 1f;
    private float degrees = 0;
    private FrameLayout container;
    private int lineColor;
    private Drawable dotDrawable;
    @Nullable
    private OnScaleListener onScaleListener;

    private RotationGestureDetector rotationGestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetectorCompat gestureDetectorCompat;

    public ResizeRotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ResizeRotationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ResizeRotationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Initialize all gesture detectors
     * @param context instance of context
     */
    private void init(@NonNull final Context context, @NonNull AttributeSet attrs) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float newScaleFactor = scaleFactor * detector.getScaleFactor();
                newScaleFactor = Math.max(1f, Math.min(newScaleFactor, 3.0f));
                if (Float.compare(scaleFactor, newScaleFactor) == 0)
                    return true;
                scaleFactor = newScaleFactor;
                container.setScaleX(scaleFactor);
                container.setScaleY(scaleFactor);
                ViewGroup.LayoutParams params = container.getLayoutParams();
                params.width = (int) (initWidth * scaleFactor);
                params.height = (int) (initHeight * scaleFactor);
                container.setLayoutParams(params);
                if (onScaleListener != null)
                    onScaleListener.onScaled(scaleFactor);
                return true;
            }
        });
        rotationGestureDetector = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            public void onRotation(RotationGestureDetector rotationDetector) {
                degrees -= rotationDetector.getAngle();
                setRotation(degrees);
            }
        });
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new CustomDragShadowBuilder(ResizeRotationView.this);
                startDrag(data, shadowBuilder, ResizeRotationView.this, 0);
                setVisibility(View.INVISIBLE);
            }
        });
        gestureDetectorCompat.setIsLongpressEnabled(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ResizeRotationView);
        try {
            lineColor = array.getColor(R.styleable.ResizeRotationView_line_color, DEFAULT_COLOR);
            int drawableId = array.getResourceId(R.styleable.ResizeRotationView_dot_drawable, 0);
            if (drawableId == 0)
                dotDrawable = new ColorDrawable(DEFAULT_COLOR);
            else
                dotDrawable = getResources().getDrawable(drawableId);
        } finally {
            array.recycle();
        }
    }

    /**
     * Initialize drag and drop listener on parent.
     * This method should be called right after view was created.
     */
    public void initDragAndDrop() {
        ((ViewGroup)getParent()).setOnDragListener(new OnDragListener(this));
    }

    public void setOnScaleListener(@Nullable OnScaleListener onScaleListener) {
        this.onScaleListener = onScaleListener;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_resize, this, false);
        lines[0] = view.findViewById(R.id.line1);
        lines[1] = view.findViewById(R.id.line2);
        lines[2] = view.findViewById(R.id.line3);
        lines[3] = view.findViewById(R.id.line4);
        dots[0] = view.findViewById(R.id.dot_lt);
        dots[1] = view.findViewById(R.id.dot_rt);
        dots[2] = view.findViewById(R.id.dot_lb);
        dots[3] = view.findViewById(R.id.dot_rb);
        for (View line : lines)
            line.setBackgroundColor(lineColor);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (View dot : dots)
                dot.setBackgroundDrawable(dotDrawable);
        } else {
            for (View dot : dots)
                dot.setBackground(dotDrawable);
        }
        container = (FrameLayout) view.findViewById(R.id.resize_holder);
        int size = getChildCount();
        for (int i=0; i<size; i++) {
            View v = getChildAt(i);
            removeView(v);
            container.addView(v);
        }
        addView(view);
        int spec = MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST);
        container.measure(spec, spec);
        initWidth = container.getMeasuredWidth();
        initHeight = container.getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        rotationGestureDetector.onTouchEvent(event);
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    /**
     * Drag listener for parent view.
     */
    private static class OnDragListener implements View.OnDragListener {

        private View draggedView;

        private OnDragListener(View draggedView) {
            this.draggedView = draggedView;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DROP:
                    draggedView.setX(event.getX() - (draggedView.getWidth() >> 1));
                    draggedView.setY(event.getY() - (draggedView.getHeight() >> 1));
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    draggedView.setVisibility(VISIBLE);
                default:
                    break;
            }
            return true;
        }
    }

    /**
     * Drag shadow builder for a view.
     */
    private class CustomDragShadowBuilder extends DragShadowBuilder {

        private final int width;
        private final int height;
        private final int w;
        private final int h;
        private final int halfW;
        private final int halfH;

        private CustomDragShadowBuilder(View view) {
            super(view);
            final double radians = Math.toRadians(degrees);
            final double cos = Math.abs(Math.cos(radians));
            final double sin = Math.abs(Math.sin(radians));
            width = (int) (view.getWidth() * scaleFactor);
            height = (int) (view.getHeight() * scaleFactor);
            w = (int) (width * cos + height * sin);
            h = (int) (width * sin + height * cos);
            halfW = w >> 1;
            halfH = h >> 1;
        }

        @Override
        public void onProvideShadowMetrics(@NonNull Point shadowSize, @NonNull Point shadowTouchPoint) {
            shadowSize.set(w, h);
            shadowTouchPoint.set(w >> 1, h >> 1);
        }

        @Override
        public void onDrawShadow(@NonNull Canvas canvas) {
            View v = getView();
            canvas.rotate(degrees, halfW, halfH);
            canvas.translate((w - v.getWidth()) >> 1, (h - v.getHeight()) >> 1);
            v.draw(canvas);
        }
    }

    public interface OnScaleListener {
        void onScaled(float scaleFactor);
    }
}

