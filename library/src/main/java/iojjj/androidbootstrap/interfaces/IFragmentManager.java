package iojjj.androidbootstrap.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Base interface for fragment management
 */
public interface IFragmentManager {
    void addFragment(@NonNull final Fragment fragment);
    void addFragment(@NonNull final Fragment fragment, @Nullable final String tag, final boolean addToBackStack);
    void addFragment(@NonNull Fragment fragment, int containerId);
    void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId);
    void replaceFragment(@NonNull final Fragment fragment);
    void replaceFragment(@NonNull Fragment fragment, int containerId);
    void replaceFragment(@NonNull final Fragment fragment, @Nullable final String tag, final boolean addToBackStack);
    void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId);
}
