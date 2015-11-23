package iojjj.android.bootstrap.core.animations;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;

import iojjj.android.bootstrap.core.utils.threading.ThreadUtils;

/**
 * Custom animation listener that prevents issue when
 * {@link Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation) AnimationListener.onAnimationEnd}
 * not called after animation is ended
 */
public class AnimationListener implements Animation.AnimationListener {

    private final Handler handler;
    private boolean onStartCalled, onEndCalled, onRepeatCalled;

    public AnimationListener() {
        this.handler = ThreadUtils.getMainHandler();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (onStartCalled)
            return;
        onStartCalled = true;
        onEndCalled = false;
        onRepeatCalled = false;
        onAnimationStartImpl(animation);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (onEndCalled)
            return;
        onEndCalled = true;
        onStartCalled = false;
        onAnimationEndImpl(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        if (onRepeatCalled)
            return;
        onRepeatCalled = true;
        onStartCalled = false;
        onEndCalled = false;
        onAnimationRepeatImpl(animation);
    }

    public void startAnimation(@NonNull final View view, @NonNull final Animation animation) {
        animation.setAnimationListener(this);
        view.startAnimation(animation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.clearAnimation();
                onAnimationEnd(animation);
            }
        }, animation.getDuration());
    }

    protected void onAnimationStartImpl(Animation animation) {

    }

    protected void onAnimationEndImpl(Animation animation) {

    }

    protected void onAnimationRepeatImpl(Animation animation) {

    }
}
