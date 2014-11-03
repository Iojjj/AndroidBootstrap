package iojjj.androidbootstrap.animations;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;

import iojjj.androidbootstrap.utils.threading.ThreadUtils;

/**
 * Custom animation listener that prevents issue when
 * {@link Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation) AnimationListener.onAnimationEnd}
 * not called after animation is ended
 */
public class AnimationListener implements Animation.AnimationListener {

    private final Handler handler;
    private final View view;

    public AnimationListener(@NonNull final View view) {
        this.handler = ThreadUtils.getMainHandler();
        this.view = view;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        onAnimationStartImpl(animation);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimationEndImpl(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        onAnimationRepeatImpl(animation);
    }

    public void startAnimation(@NonNull final Animation animation) {
        view.startAnimation(animation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.clearAnimation();
                onAnimationEndImpl(animation);
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
