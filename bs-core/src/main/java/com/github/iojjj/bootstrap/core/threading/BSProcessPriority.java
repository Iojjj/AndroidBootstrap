package com.github.iojjj.bootstrap.core.threading;


import android.os.Process;
import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allowed values for thread priority.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@IntDef(flag = true, value = {
        Process.THREAD_PRIORITY_BACKGROUND,
        Process.THREAD_PRIORITY_AUDIO,
        Process.THREAD_PRIORITY_DEFAULT,
        Process.THREAD_PRIORITY_DISPLAY,
        Process.THREAD_PRIORITY_URGENT_AUDIO,
        Process.THREAD_PRIORITY_URGENT_DISPLAY,
        Process.THREAD_PRIORITY_FOREGROUND,
        Process.THREAD_PRIORITY_LOWEST
})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface BSProcessPriority {
}
