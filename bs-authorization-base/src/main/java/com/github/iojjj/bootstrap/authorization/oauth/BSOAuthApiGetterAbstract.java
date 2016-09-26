package com.github.iojjj.bootstrap.authorization.oauth;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.scribejava.core.builder.api.BaseApi;

/**
 * Abstract factory that creates an instances of {@link BaseApi}.
 *
 * @since 1.0
 */
public abstract class BSOAuthApiGetterAbstract implements Parcelable {

    /**
     * Get an instance of {@link BaseApi}.
     *
     * @return instance of BaseApi
     */
    @NonNull
    abstract BaseApi getApi();
}
