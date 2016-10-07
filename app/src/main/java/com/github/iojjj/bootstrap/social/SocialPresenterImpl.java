package com.github.iojjj.bootstrap.social;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.iojjj.bootstrap.mvp.BSMvpPresenterImpl;


class SocialPresenterImpl extends BSMvpPresenterImpl<SocialView> implements SocialPresenter {

    private final long mId;

    SocialPresenterImpl(long id) {
        mId = id;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("KEY", mId);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d("RESTORE", String.valueOf(savedInstanceState.getLong("KEY")));
            final SocialView view = getView();
            if (view != null) {
                view.sowToast();
            }
        }
    }
}
