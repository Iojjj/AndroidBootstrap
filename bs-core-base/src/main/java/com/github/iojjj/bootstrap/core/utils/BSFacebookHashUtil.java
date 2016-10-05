package com.github.iojjj.bootstrap.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.BSOptional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for generating Facebook key hash.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSFacebookHashUtil {

    private BSFacebookHashUtil() {
        //no instance
    }

    /**
     * Get a key hash for Facebook.
     *
     * @param context instance of Context
     *
     * @return optional key hash
     */
    public static BSOptional<String> generate(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        return generate(context, context.getPackageName());
    }

    /**
     * Get a key hash for Facebook.
     *
     * @param context     instance of Context
     * @param packageName package name
     *
     * @return optional key hash
     */
    public static BSOptional<String> generate(@NonNull Context context, @NonNull String packageName) {
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotEmpty(packageName, "packageName");
        String keyHash = null;
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (info.signatures.length != 0) {
                final Signature signature = info.signatures[0];
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return BSOptional.from(keyHash);
    }
}
