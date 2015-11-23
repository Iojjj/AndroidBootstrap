package iojjj.android.bootstrap.core.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
* Allowed values for {@link iojjj.android.bootstrap.core.utils.storage.FileUtils#copyFile(java.io.File, java.io.File) FileUtils.copyFile} method
*/
@IntDef(flag = true, value = { CopyFileResult.RESULT_SUCCESS, CopyFileResult.RESULT_GENERAL_ERROR, CopyFileResult.RESULT_SRC_NOT_EXISTS })
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface CopyFileResult {
    int RESULT_SUCCESS = 0x00;
    int RESULT_GENERAL_ERROR = 0x01;
    int RESULT_SRC_NOT_EXISTS = 0x02;
}
