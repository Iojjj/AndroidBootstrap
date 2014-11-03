package iojjj.androidbootstrap.utils.multimedia;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Interface for audio recorder
 */
public interface IAudioRecorder {
    public void startRecord();
    public void cancelRecord();
    @NonNull
    public Uri finishRecord();
    public boolean isRecording();
}
