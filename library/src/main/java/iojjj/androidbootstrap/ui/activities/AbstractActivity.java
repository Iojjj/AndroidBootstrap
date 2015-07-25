package iojjj.androidbootstrap.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import iojjj.androidbootstrap.R;
import iojjj.androidbootstrap.interfaces.IBackPressable;
import iojjj.androidbootstrap.interfaces.IFragmentManager;
import iojjj.androidbootstrap.utils.misc.MiscellaneousUtils;

public abstract class AbstractActivity extends AppCompatActivity implements IFragmentManager {

    private static ImageView rotationImage;
    private static Animation rotationAnimation;
    private BroadcastReceiver toastReceiver;
    private Toast toast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldSubscribeForToast()) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            toastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    showToast(intent.getStringExtra(MiscellaneousUtils.EXTRA_TEXT), intent.getIntExtra(MiscellaneousUtils.EXTRA_DURATION, Toast.LENGTH_SHORT));
                }
            };
            MiscellaneousUtils.registerToastReceiver(this, toastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        if (shouldSubscribeForToast())
            MiscellaneousUtils.unregisterToastReceiver(this, toastReceiver);
        super.onDestroy();
    }

    private void showToast(String text, int duration) {
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    @Override
    public void addFragment(@NonNull Fragment fragment) {
        addFragment(fragment, getContainerId());
    }

    public void addFragment(@NonNull Fragment fragment, int containerId) {
        addFragment(fragment, null, false, containerId);
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        addFragment(fragment, tag, addToBackStack, getContainerId());
    }

    public void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId) {
        changeFragment(fragment, tag, addToBackStack, false, containerId);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment) {
        replaceFragment(fragment, getContainerId());
    }

    public void replaceFragment(@NonNull Fragment fragment, int containerId) {
        replaceFragment(fragment, null, true, containerId);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        replaceFragment(fragment, tag, addToBackStack, getContainerId());
    }

    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId) {
        changeFragment(fragment, tag, addToBackStack, true, containerId);
    }

    protected void changeFragment(@NonNull Fragment fragment, @Nullable String tag, final boolean addToStack, final boolean isReplace, final int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isReplace) {
            transaction.replace(containerId, fragment);
        } else {
            transaction.add(containerId, fragment);
        }
        if (addToStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * Get id of fragment's container
     * @return id of container
     */
    protected abstract int getContainerId();

    /**
     * Start rotation of menu item's action view
     * @param item menu item
     */
    protected void startRotation(@Nullable MenuItem item) {
        if (item == null)
            return;
        if (rotationImage == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rotationImage = (ImageView) inflater.inflate(R.layout.widget_rotated_image, null);
        }
        if (rotationAnimation == null) {
            rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation_360);
            rotationAnimation.setRepeatCount(Animation.INFINITE);
        }
        rotationImage.startAnimation(rotationAnimation);
        MenuItemCompat.setActionView(item, rotationImage);
    }

    /**
     * Stop rotation of menu item's action view
     * @param item menu item
     */
    protected void stopRotation(@Nullable MenuItem item) {
        if (item == null)
            return;
        View view = MenuItemCompat.getActionView(item);
        if (view != null)
            view.clearAnimation();
        MenuItemCompat.setActionView(item, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // default behavior for home button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerId());
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerId());
        if (fragment instanceof IBackPressable && ((IBackPressable) fragment).onBackPressed())
            return;
        super.onBackPressed();
    }

    protected abstract boolean shouldSubscribeForToast();
}
