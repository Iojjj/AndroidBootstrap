package com.github.iojjj.bootstrap.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for generating Facebook key hash.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSFacebookHashGenerator {

    private BSFacebookHashGenerator() {
        //no instance
    }

    public static String generate(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        return generate(context, context.getPackageName());
    }

    public static String generate(@NonNull Context context, @NonNull String packageName) {
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotEmpty(packageName, "packageName");
        String keyHash;
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (info.signatures.length == 0) {
                keyHash = "Error: there are no signatures for package " + packageName;
            } else {
                final Signature signature = info.signatures[0];
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            keyHash = "Error: " + e.getMessage();
        }
        return keyHash;
    }
}
