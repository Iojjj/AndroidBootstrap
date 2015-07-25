package iojjj.androidbootstrap.utils.misc;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import iojjj.androidbootstrap.R;


/**
 * Rate app helper
 */
public class RateApp {

    private static final String PREFERENCES_NAME = "rate_app";
    private static final String KEY_LAST_LAUNCHED = "last_launched";
    private static final String KEY_LAUNCHES_COUNT = "launches_count";
    private static final String KEY_DO_NO_ASK_AGAIN = "do_not_ask_again";
    private static final long DAY_MS = 1000 * 60 * 60 * 24;

    private final SharedPreferences preferences;
    private Builder builder;

    protected RateApp(@NotNull final Builder builder) {
        this.preferences = builder.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.builder = builder;
    }

    public void check() {
        if (preferences.getBoolean(KEY_DO_NO_ASK_AGAIN, false))
            return;
        int launchesCount = preferences.getInt(KEY_LAUNCHES_COUNT, 0);
        long lastLaunched = preferences.getLong(KEY_LAST_LAUNCHED, 0);
        if (launchesCount >= builder.launchTimes) {
            showDialog();
            return;
        }
        if (lastLaunched != 0 && Math.abs(System.currentTimeMillis() - lastLaunched) >= builder.repeatPeriod * DAY_MS) {
            showDialog();
            return;
        }
        preferences.edit()
                .putInt(KEY_LAUNCHES_COUNT, launchesCount + 1)
                .putLong(KEY_LAST_LAUNCHED, lastLaunched == 0 ? System.currentTimeMillis() : lastLaunched)
                .apply();
    }

    private void showDialog() {
        preferences.edit()
                .putInt(KEY_LAUNCHES_COUNT, 0)
                .putLong(KEY_LAST_LAUNCHED, System.currentTimeMillis())
                .apply();

        getDialog(builder.title, builder.message, builder.positiveText, builder.negativeText, builder.neutralText, new Runnable() {

            @Override
            public void run() {
                preferences.edit()
                        .putBoolean(KEY_DO_NO_ASK_AGAIN, true)
                        .apply();
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + builder.packageName));
                int flag;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    flag = Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                else
                    //noinspection deprecation
                    flag = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | flag | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PackageManager manager = builder.context.getPackageManager();
                List<ResolveInfo> infos = manager.queryIntentActivities(marketIntent, 0);
                if (infos.isEmpty()) {
                    marketIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + builder.packageName));
                }
                builder.context.startActivity(marketIntent);
            }
        },new Runnable() {

            @Override
            public void run() {
                preferences.edit()
                        .putBoolean(KEY_DO_NO_ASK_AGAIN, true)
                        .apply();
            }
        }).show();
    }

    protected Dialog getDialog(@NotNull String title, @NotNull String content,
                               @NotNull String positiveText, @NotNull String negativeText, @NotNull String neutralText,
                               @NotNull final Runnable onPositive, @NotNull final Runnable onNeutral) {
        return new AlertDialog.Builder(builder.context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPositive.run();
                    }
                })
                .setNegativeButton(negativeText, null)
                .setNeutralButton(neutralText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onNeutral.run();
                    }
                })
                .create();
    }

    protected Context getContext() {
        return builder.context;
    }

    public static class Builder {
        private Context context;
        private int launchTimes;
        private int repeatPeriod;
        private String appName;
        private String packageName;
        private String title;
        private String message;
        private String positiveText;
        private String negativeText;
        private String neutralText;

        public Builder(@NotNull final Context context, @NotNull final String appName, @NotNull final String packageName) {
            this.context = context;
            this.appName = appName;
            this.packageName = packageName;
        }

        public Builder(@NotNull final Context context, @StringRes  final int appNameId, @StringRes final int packageNameId) {
            this.context = context;
            this.appName = context.getString(appNameId);
            this.packageName = context.getString(packageNameId);
        }

        public Builder setLaunchTimes(int launchTimes) {
            this.launchTimes = launchTimes;
            return this;
        }

        public Builder setRepeatPeriod(int repeatPeriod) {
            this.repeatPeriod = repeatPeriod;
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setNeutralText(String neutralText) {
            this.neutralText = neutralText;
            return this;
        }

        public Builder setPositiveText(@StringRes int positiveText) {
            return setPositiveText(context.getString(positiveText));
        }

        public Builder setNegativeText(@StringRes int negativeText) {
            return setNegativeText(context.getString(negativeText));
        }

        public Builder setNeutralText(@StringRes int neutralText) {
            return setNeutralText(context.getString(neutralText));
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTitle(@StringRes int title) {
            this.title = context.getString(title);
            return this;
        }

        public Builder setMessage(@StringRes int message) {
            this.message = context.getString(message);
            return this;
        }

        public RateApp build() throws IllegalArgumentException {
            if (!(context instanceof ContextThemeWrapper))
                throw new IllegalArgumentException("Context must extend " + ContextThemeWrapper.class.getSimpleName() + ".");
            if (TextUtils.isEmpty(appName))
                throw new IllegalArgumentException("Application's name must not be empty.");
            if (TextUtils.isEmpty(packageName))
                throw new IllegalArgumentException("Application's package name must not be empty.");
            if (TextUtils.isEmpty(positiveText)) {
                positiveText = context.getString(R.string.rate_now);
            }
            if (TextUtils.isEmpty(neutralText)) {
                neutralText = context.getString(R.string.rate_never);
            }
            if (TextUtils.isEmpty(negativeText)) {
                negativeText = context.getString(R.string.rate_later);
            }
            if (TextUtils.isEmpty(title)) {
                title = context.getString(R.string.rate_title, appName);
            }

            if (TextUtils.isEmpty(message)) {
                message = context.getString(R.string.rate_content, appName);
            }
            return new RateApp(this);
        }
    }
}
