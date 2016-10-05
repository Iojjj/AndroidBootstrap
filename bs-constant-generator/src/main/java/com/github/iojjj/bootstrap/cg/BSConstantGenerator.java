package com.github.iojjj.bootstrap.cg;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

/**
 * Android-style constants generator.<br /><br />
 * Before using any of static methods you must call {@link #setPackageName(String)} first.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSConstantGenerator {

    private static final String ACTION_PREFIX = "ACTION_";
    private static final String EXPRESSION = "(.)(\\p{Upper})";
    private static final String EXTRA_IN_PREFIX = "EXTRA_";
    private static final String EXTRA_OUT_PREFIX = "RESULT_";
    private static final String REPLACEMENT = "$1_$2";

    private static String sPackageName = "";

    private BSConstantGenerator() {
        //no instance
    }

    /**
     * Create a new ACTION_ constant.
     *
     * @param actionName action field name
     *
     * @return a new ACTION_ constant in SNAKE_CASE
     */
    public static String action(@NonNull String actionName) {
        BSAssertions.assertNotEmpty(actionName, "actionName");
        return sPackageName + ACTION_PREFIX + toUpper(actionName);
    }

    /**
     * Create a new table's column constant.
     *
     * @param columnName column name
     *
     * @return a new table's column constant in snake_case
     */
    public static String column(@NonNull String columnName) {
        BSAssertions.assertNotEmpty(columnName, "columnName");
        return toLower(columnName);
    }

    /**
     * Create a new EXTRA_ constant.
     *
     * @param extraName extra field name
     *
     * @return a new EXTRA_ constant in SNAKE_CASE
     */
    public static String extra(@NonNull String extraName) {
        BSAssertions.assertNotEmpty(extraName, "extraName");
        return sPackageName + EXTRA_IN_PREFIX + toUpper(extraName);
    }

    /**
     * Create a new RESULT_ constant.
     *
     * @param extraName result field name
     *
     * @return a new RESULT_ constant in SNAKE_CASE
     */
    public static String result(@NonNull String extraName) {
        BSAssertions.assertNotEmpty(extraName, "extraName");
        return sPackageName + EXTRA_OUT_PREFIX + toUpper(extraName);
    }

    /**
     * Set package name.
     *
     * @param packageName some package name
     */
    public static void setPackageName(@NonNull String packageName) {
        BSAssertions.assertNotNull(packageName, "packageName");
        sPackageName = packageName + ".";
    }

    /**
     * Create a new table name constant.
     *
     * @param tableName table name
     *
     * @return a new table name constant in snake_case
     */
    public static String table(@NonNull String tableName) {
        BSAssertions.assertNotEmpty(tableName, "tableName");
        return toLower(tableName);
    }

    private static String toLower(@NonNull String in) {
        BSAssertions.assertNotNull(in, "in");
        return in.replaceAll(EXPRESSION, REPLACEMENT).toLowerCase();
    }

    private static String toUpper(@NonNull String in) {
        BSAssertions.assertNotNull(in, "in");
        return in.replaceAll(EXPRESSION, REPLACEMENT).toUpperCase();
    }
}
