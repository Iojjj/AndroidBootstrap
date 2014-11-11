package iojjj.androidbootstrap.utils.misc;

import android.content.Context;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Keyboard utils
 */
public class KeyboardUtils {

    /**
     * Show keyboard
     * @param context context
     * @param editText view requested keyboard
     * @param result callback for operation
     */
	public static void showKeyboard(@Nullable final Context context,
                                    @Nullable final View editText,
                                    @Nullable final ResultReceiver result) {
		if (editText == null || context == null) {
			return;
		}
		final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		editText.post(new Runnable() {
			@Override
			public void run() {
                editText.requestFocus();
				imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT, result);
			}
		});
	}

    /**
     * Hide keyboard
     * @param context context
     * @param editText view requested keyboard
     * @param result callback for operation
     */
	public static void hideKeyboard(@Nullable final Context context,
                                    @Nullable final View editText,
                                    @Nullable final ResultReceiver result) {
		if (editText == null || context == null) {
			return;
		}
		final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		editText.post(new Runnable() {
			@Override
			public void run() {
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0, result);
				editText.clearFocus();
			}
		});

	}

    /**
     * Show keyboard
     * @param context context
     * @param editText view requested keyboard
     */
    public static void showKeyboard(@Nullable final Context context, @Nullable final View editText) {
		showKeyboard(context, editText, null);
    }

    /**
     * Hide keyboard
     * @param context context
     * @param editText view requested keyboard
     */
    public static void hideKeyboard(@Nullable Context context, @Nullable final View editText) {
		hideKeyboard(context, editText, null);
    }


}
