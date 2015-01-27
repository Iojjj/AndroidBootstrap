package iojjj.androidbootstrap.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import iojjj.androidbootstrap.R;
import iojjj.androidbootstrap.interfaces.IFragmentManager;


public abstract class AbstractActivity extends ActionBarActivity implements IFragmentManager {

    private static ImageView rotationImage;
    private static Animation rotationAnimation;

    @Override
    public void addFragment(@NonNull Fragment fragment) {
        addFragment(fragment, null, true);
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        changeFragment(fragment, tag, addToBackStack, false);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment) {
        replaceFragment(fragment, null, true);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        changeFragment(fragment, tag, addToBackStack, true);
    }

    private void changeFragment(@NonNull Fragment fragment, @Nullable String tag, final boolean addToStack, final boolean isReplace) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isReplace)
            transaction.replace(getContainerId(), fragment);
        else
            transaction.add(getContainerId(), fragment);
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
}
