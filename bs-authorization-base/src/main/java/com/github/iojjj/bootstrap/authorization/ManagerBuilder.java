package com.github.iojjj.bootstrap.authorization;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Scope;

// TODO: 22.09.2016 documentation
public interface ManagerBuilder<T extends GoogleSignInManager> {

    ManagerBuilder<T> setServerClientId(@NonNull String serverClientId);

    ManagerBuilder<T> setCallback(@Nullable GoogleSignInManager.Callback callback);

    ManagerBuilder<T> setScopes(@NonNull Scope first, Scope... otherScopes);

    ManagerBuilder<T> setRequestCode(int requestCode);

    T build();
}
