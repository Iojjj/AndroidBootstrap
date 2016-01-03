package iojjj.android.bootstrap.animations;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Custom animation listener that prevents issue when
 * {@link Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation) AnimationListener.onAnimationEnd}
 * not called after animation is ended
 */
public class AnimationListener implements Animation.AnimationListener {

    private final Handler handler;
    private boolean onStartCalled, onEndCalled, onRepeatCalled;
    private Runnable endRunnable;

    public AnimationListener(@NonNull Handler mainHandler) {
        this.handler = mainHandler;
    }

    @Override
    public final void onAnimationStart(Animation animation) {
        if (onStartCalled)
            return;
        onStartCalled = true;
        onEndCalled = false;
        onRepeatCalled = false;
        onAnimationStartImpl(animation);
    }

    @Override
    public final void onAnimationEnd(Animation animation) {
        if (onEndCalled)
            return;
        if (endRunnable != null) {
            handler.removeCallbacks(endRunnable);
        }
        onEndCalled = true;
        onStartCalled = false;
        handler.removeCallbacks(null);
        onAnimationEndImpl(animation);
    }

    @Override
    public final void onAnimationRepeat(Animation animation) {
        if (onRepeatCalled)
            return;
        onRepeatCalled = true;
        onStartCalled = false;
        onEndCalled = false;
        onAnimationRepeatImpl(animation);
    }

    public void startAnimation(@NonNull final View view, @NonNull final Animation animation) {
        AssertionUtils.assertNotNull(view, "View");
        AssertionUtils.assertNotNull(animation, "Animation");
        animation.setAnimationListener(this);
        view.startAnimation(animation);
        int repeatCount = animation.getRepeatCount();
        if (repeatCount == 0) {
            repeatCount = 1;
        }
        if (repeatCount > Animation.INFINITE) {
            handler.postDelayed(endRunnable = newEndRunnable(view, animation), animation.getDuration() * repeatCount);
        }
    }

    private Runnable newEndRunnable(final View view, final Animation animation) {
        return new Runnable() {
            @Override
            public void run() {
                view.clearAnimation();
                onAnimationEnd(animation);
            }
        };
    }

    protected void onAnimationStartImpl(Animation animation) {

    }

    protected void onAnimationEndImpl(Animation animation) {

    }

    protected void onAnimationRepeatImpl(Animation animation) {

    }
}
