package iojjj.android.bootstrap.core.multimedia.audio;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Interface for audio recorder
 */
public interface IAudioRecorder {
    void startRecord();
    void cancelRecord();
    @NonNull
    Uri finishRecord();
    boolean isRecording();
}
