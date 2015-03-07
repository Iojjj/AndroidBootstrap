package iojjj.androidbootstrap.ui.widgets.flowermenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;

import iojjj.androidbootstrap.R;

/**
 * Flower menu
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class FlowerMenu extends FrameLayout implements FlowerMenuItem.IFlowerMenu {

    private static final int DEFAULT_LEAVES_COUNT = 6;
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private static final int DEFAULT_RADIUS_SPACING = 100;
    private static final int ADDITIONAL_SPACING = 5;
    private static final int DEFAULT_START_ANGLE = -90;
    private static final int DEFAULT_END_ANGLE = 270;
    private static final int SPEC_W = MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST);
    private static final int SPEC_H = MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST);

    private CenterMenuItem centerLeaf;
    private FlowerMenuItem[] leaves;

    private boolean animationInProgress;
    private boolean menuOpened;
    private boolean staticMenu;
    private int leavesCount;
    private int animationDuration;
    private int animationDelay;
    private int radiusSpacing;
    private float startAngle;
    private float endAngle;
    private float angleStep;
    private float additionalSpacing;
    private OnLeafClickListener onLeafClickListener;
    private OnLeafLongClickListener onLeafLongClickListener;
    private FlowerMenuListener flowerMenuListener;


    public FlowerMenu(Context context) {
        super(context);
    }

    public FlowerMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlowerMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowerMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private OnClickListener centerLeafClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            openCloseMenu();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 2 * radiusSpacing;
        int height = 2 * radiusSpacing;
        if (leaves != null && leaves.length > 0) {
            FlowerMenuItem leaf = leaves[0];
            if (leaf.getMeasuredWidth() == 0 || leaf.getMeasuredHeight() == 0) {
                int spec = MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.AT_MOST);
                leaf.measure(spec, spec);
            }
            width += leaf.getMeasuredWidth();
            height += leaf.getMeasuredHeight();
        }
        width += additionalSpacing;
        height += additionalSpacing;
        int specW = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int specH = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(specW, specH);
        setMeasuredDimension(width, height);
    }

    public static float dpToPx(@NotNull final Context context, int dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    private AnimatorSet makeAnimation(int i) {
        final FlowerMenuItem leaf = leaves[i];
        if (leaf.getMeasuredWidth() == 0 || leaf.getMeasuredHeight() == 0) {
            leaf.measure(SPEC_W, SPEC_H);
        }
        float angle = startAngle + i * angleStep;
        if (menuOpened)
            angle = (angle + 180) % 360;
        final float angleRad = (float) Math.toRadians(angle);
        final int k = menuOpened ? -1 : 1;
        return leafAnimation(
                leaf,
                k * radiusSpacing * (float) Math.cos(angleRad),
                k * radiusSpacing * (float) Math.sin(angleRad),
                !menuOpened, true);
    }

    private AnimatorSet leafAnimation(final FlowerMenuItem leaf, final float dX, final float dY, final boolean show, final boolean useTransition) {
        final AnimatorSet animatorSet = new AnimatorSet();
        final int fromScale = show ? 0 : 1;
        final int toScale = show ? 1 : 0;
        final float fromX = show ? 0 : dX;
        final float fromY = show ? 0 : dY;
        final float toX = show ? dX : 0;
        final float toY = show ? dY : 0;
        ObjectAnimator xScale = ObjectAnimator.ofFloat(leaf, "scaleX", fromScale , toScale);
        ObjectAnimator yScale = ObjectAnimator.ofFloat(leaf, "scaleY", fromScale, toScale);
        animatorSet.setTarget(leaf);
        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new SimpleAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if (show)
                    leaf.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show)
                    leaf.setVisibility(GONE);
            }
        });
        if (useTransition) {
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(leaf, "translationX", fromX, toX);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(leaf, "translationY", fromY, toY);
            animatorSet.playTogether(xAnimator, yAnimator, xScale, yScale);
        } else {
            animatorSet.playTogether(xScale, yScale);
        }
        return animatorSet;
    }

    @SuppressLint("InflateParams")
    private void init(@NotNull final Context context, @NotNull AttributeSet attrs) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        boolean showCenterItem = true;
        int centerItemId = R.layout.widget_flower_menu_center;
        int leafItemId = R.layout.widget_flower_menu_leaf;
        leavesCount = DEFAULT_LEAVES_COUNT;
        animationDuration = DEFAULT_ANIMATION_DURATION;
        radiusSpacing = DEFAULT_RADIUS_SPACING;
        startAngle = DEFAULT_START_ANGLE;
        endAngle = DEFAULT_END_ANGLE;
        additionalSpacing = dpToPx(context, ADDITIONAL_SPACING);
        staticMenu = true;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowerMenu);
        if (array != null) {
            try {
                showCenterItem = array.getBoolean(R.styleable.FlowerMenu_fm_showCenterItem, true);
                centerItemId = array.getResourceId(R.styleable.FlowerMenu_fm_centerItemLayout, R.layout.widget_flower_menu_center);
                leafItemId = array.getResourceId(R.styleable.FlowerMenu_fm_leafItemLayout, R.layout.widget_flower_menu_leaf);
                leavesCount = array.getInt(R.styleable.FlowerMenu_fm_leavesCount, DEFAULT_LEAVES_COUNT);
                animationDuration = array.getInt(R.styleable.FlowerMenu_fm_animationDuration, DEFAULT_ANIMATION_DURATION);
                animationDelay = array.getInt(R.styleable.FlowerMenu_fm_leafDisplayDelay, 0);
                radiusSpacing = array.getDimensionPixelSize(R.styleable.FlowerMenu_fm_radiusSpacing, DEFAULT_RADIUS_SPACING);
                staticMenu = array.getBoolean(R.styleable.FlowerMenu_fm_staticMenu, true);
                startAngle = array.getFloat(R.styleable.FlowerMenu_fm_startAngle, DEFAULT_START_ANGLE);
                endAngle = array.getFloat(R.styleable.FlowerMenu_fm_endAngle, DEFAULT_END_ANGLE);
            } finally {
                array.recycle();
            }
        }

        if (leavesCount <= 0)
            throw new IllegalArgumentException("Invalid value for fm_leavesCount attribute");
        if (animationDuration <= 0)
            throw new IllegalArgumentException("Invalid value for fm_animationDuration attribute");
        if (startAngle >= endAngle)
            throw new IllegalArgumentException("Invalid values for startAngle and endAngle");

        angleStep = (endAngle - startAngle) / leavesCount;
        animationDelay = animationDuration / leavesCount;

        leaves = new FlowerMenuItem[leavesCount];
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        for (int i=0; i<leavesCount; i++) {
            leaves[i] = (FlowerMenuItem) inflater.inflate(leafItemId, null);
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            leaves[i].setLayoutParams(params);
            leaves[i].setVisibility(GONE);
            leaves[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLeafClickListener != null)
                        onLeafClickListener.onLeafClick(indexOf(v));
                }
            });
            leaves[i].setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onLeafLongClickListener != null && onLeafLongClickListener.onLeafLongClick(indexOf(v));
                }
            });
            leaves[i].setFlowerMenu(this);
            initLeaf(leaves[i], i);
            addView(leaves[i]);
        }
        // add center as last item to be drawn over other leaves
        if (showCenterItem) {
            centerLeaf = (CenterMenuItem) inflater.inflate(centerItemId, null);
            centerLeaf.setLayoutParams(params);
            centerLeaf.setFlowerMenu(this);
            addView(centerLeaf);
            centerLeaf.setOnClickListener(centerLeafClickListener);
            initCenter(centerLeaf);
        }
    }

    protected abstract void initCenter(FlowerMenuItem centerLeaf);

    protected abstract void initLeaf(FlowerMenuItem leaf, int position);

    private int indexOf(View leaf) {
        int i = 0;
        for (FlowerMenuItem _leaf : leaves) {
            if (leaf == _leaf)
                return i;
            i++;
        }
        return -1;
    }

    public void setOnLeafClickListener(OnLeafClickListener onLeafClickListener) {
        this.onLeafClickListener = onLeafClickListener;
    }

    public void setOnLeafLongClickListener(OnLeafLongClickListener onLeafLongClickListener) {
        this.onLeafLongClickListener = onLeafLongClickListener;
    }

    public void setFlowerMenuListener(FlowerMenuListener flowerMenuListener) {
        this.flowerMenuListener = flowerMenuListener;
    }

    @Override
    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public void openMenu() {
        if (menuOpened)
            return;
        openCloseMenu();
    }

    private void openCloseMenu() {
        final AnimatorSet globalAnimator = new AnimatorSet();
        final AnimatorSet[] animations = new AnimatorSet[leavesCount];
        globalAnimator.addListener(new SimpleAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if (flowerMenuListener != null) {
                    if (menuOpened)
                        flowerMenuListener.onMenuBeginClosing();
                    else
                        flowerMenuListener.onMenuBeginOpening();
                }
                animationInProgress = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationInProgress = false;
                menuOpened = !menuOpened;
                if (flowerMenuListener != null) {
                    if (menuOpened)
                        flowerMenuListener.onMenuOpened();
                    else
                        flowerMenuListener.onMenuClosed();
                }
            }
        });
        if (menuOpened) {
            for (int i = leavesCount - 1; i >= 0; i--) {
                animations[i] = makeAnimation(i);
                animations[i].setStartDelay((leavesCount - i - 1) * animationDelay);
            }
        } else {
            for (int i = 0; i < leavesCount; i++) {
                animations[i] = makeAnimation(i);
                animations[i].setStartDelay(i * animationDelay);
            }
        }
        if (!staticMenu) {
            AnimatorSet centerLeafAnimation = leafAnimation(centerLeaf, 0, 0, !menuOpened, false);
            if (menuOpened) {
                AnimatorSet.Builder builder = globalAnimator.play(animations[0]);
                for (int i=0; i<animations.length - 1; i++)
                    builder.with(animations[i + 1]);
                builder.before(centerLeafAnimation);
            } else {
                AnimatorSet.Builder builder = globalAnimator.play(centerLeafAnimation);
                for (AnimatorSet animatorSet : animations) {
                    builder.before(animatorSet);
                }
            }
        } else {
            globalAnimator.playTogether(animations);
        }
        globalAnimator.start();
    }

    public void closeMenu() {
        if (!menuOpened)
            return;
        openCloseMenu();
    }

    public boolean isOpened() {
        return menuOpened;
    }


    public interface OnLeafClickListener {
        void onLeafClick(int i);
    }

    public interface OnLeafLongClickListener {
        boolean onLeafLongClick(int i);
    }

    public interface FlowerMenuListener {
        void onMenuBeginOpening();
        void onMenuOpened();
        void onMenuBeginClosing();
        void onMenuClosed();
    }

    /**
     * Implementation of {@link android.animation.Animator.AnimatorListener}
     */
    private static class SimpleAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
