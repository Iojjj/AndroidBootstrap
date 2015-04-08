package iojjj.androidbootstrap.animations;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Animation utils
 */
public class AnimationUtils {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SimpleAnimatorListener implements Animator.AnimatorListener {

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
