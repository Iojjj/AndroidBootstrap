package iojjj.androidbootstrap.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
* Allowed values for {@link iojjj.androidbootstrap.utils.storage.FileUtils#copyFile(java.io.File, java.io.File) FileUtils.copyFile} method
*/
@IntDef(flag = true, value = { CopyFileResult.RESULT_SUCCESS, CopyFileResult.RESULT_GENERAL_ERROR, CopyFileResult.RESULT_SRC_NOT_EXISTS })
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface CopyFileResult {
    public static final int RESULT_SUCCESS = 0x00;
    public static final int RESULT_GENERAL_ERROR = 0x01;
    public static final int RESULT_SRC_NOT_EXISTS = 0x02;
}
