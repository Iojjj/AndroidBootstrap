package iojjj.android.bootstrap.core.animations;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Simple implementation of {@link android.animation.Animator.AnimatorListener}
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SimpleAnimatorListener implements Animator.AnimatorListener {

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
