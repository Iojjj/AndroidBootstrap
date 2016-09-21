package com.github.iojjj.bootstrap.core;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

/**
 * Android-style constants generator.<br /><br />
 * Before using any of static methods you must apply {@link #setPackageName(String)} first.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSConstantGenerator {

    private static final String EXTRA_IN_PREFIX = "EXTRA_";
    private static final String EXTRA_OUT_PREFIX = "RESULT_";
    private static final String ACTION_PREFIX = "ACTION_";
    private static final String EXPRESSION = "(.)(\\p{Upper})";
    private static final String REPLACEMENT = "$1_$2";

    private static String sPackageName = "";

    private BSConstantGenerator() {
        //no instance
    }

    private static String toUpper(@NonNull String in) {
        BSAssertions.assertNotNull(in, "in");
        return in.replaceAll(EXPRESSION, REPLACEMENT).toUpperCase();
    }

    private static String toLower(@NonNull String in) {
        BSAssertions.assertNotNull(in, "in");
        return in.replaceAll(EXPRESSION, REPLACEMENT).toLowerCase();
    }


    public static String result(@NonNull String extraName) {
        BSAssertions.assertNotEmpty(extraName, "extraName");
        return sPackageName + EXTRA_OUT_PREFIX + toUpper(extraName);
    }

    public static String extra(@NonNull String extraName) {
        BSAssertions.assertNotEmpty(extraName, "extraName");
        return sPackageName + EXTRA_IN_PREFIX + toUpper(extraName);
    }

    public static String action(@NonNull String actionName) {
        BSAssertions.assertNotEmpty(actionName, "actionName");
        return sPackageName + ACTION_PREFIX + toUpper(actionName);
    }

    public static String column(@NonNull String columnName) {
        BSAssertions.assertNotEmpty(columnName, "columnName");
        return toLower(columnName);
    }

    public static String table(@NonNull String tableName) {
        BSAssertions.assertNotEmpty(tableName, "tableName");
        return toLower(tableName);
    }

    public static void setPackageName(@NonNull String packageName) {
        BSAssertions.assertNotNull(packageName, "packageName");
        sPackageName = packageName + ".";
    }
}
