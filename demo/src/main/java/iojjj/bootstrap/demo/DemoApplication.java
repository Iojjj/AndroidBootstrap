package iojjj.bootstrap.demo;

import android.support.annotation.Nullable;

import iojjj.androidbootstrap.FocusableApplication;
import iojjj.androidbootstrap.receivers.ForegroundStateReceiver;

/**
 * Demo application
 */
public class DemoApplication extends FocusableApplication {

    @Nullable
    @Override
    protected ForegroundStateReceiver getForegroundTaskReceiver() {
        return null;
    }
}
