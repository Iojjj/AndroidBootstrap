package iojjj.androidbootstrap.utils.multimedia;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import iojjj.androidbootstrap.utils.threading.PriorityRunnable;

/**
 * Helper class for audio recording and saving as .wav
 */
public abstract class AudioRecorder implements IAudioRecorder {

	public static final int RECORDER_SAMPLE_RATE    = 8000;
	public static final int RECORDER_CHANNELS       = AudioFormat.CHANNEL_OUT_MONO;
	public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


	private static final int BUFFER_BYTES_ELEMENTS    = 1024;
	private static final int BUFFER_BYTES_PER_ELEMENT = RECORDER_AUDIO_ENCODING;
	private static final int RECORDER_CHANNELS_IN     = AudioFormat.CHANNEL_IN_MONO;


	@NonNull
	public abstract File getRecordFile();


	public static final int RECORDER_STATE_FAILURE  = -1;
	public static final int RECORDER_STATE_IDLE     = 0;
	public static final int RECORDER_STATE_STARTING = 1;
	public static final int RECORDER_STATE_STOPPING = 2;
	public static final int RECORDER_STATE_BUSY     = 3;

	private volatile int recorderState;

	private final Object recorderStateMonitor = new Object();

    @Nullable
	private File record;
    @Nullable
	private File recordTmp;


	@SuppressWarnings("ResultOfMethodCallIgnored")
    private void onRecordFailure() {
		recorderState = RECORDER_STATE_FAILURE;

        if (record != null) {
            record.delete();
        }
        if (recordTmp != null) {
            recordTmp.delete();
        }
        record = null;
	}

	@Override
	public void startRecord() {
		if (recorderState != RECORDER_STATE_IDLE) {
			return;
		}

		record = getRecordFile();
		recordTmp = new File(record.getParent(), record.getName() + ".tmp");

		try {
			recorderState = RECORDER_STATE_STARTING;

			startRecordThread();
		} catch (FileNotFoundException e) {
			onRecordFailure();
			e.printStackTrace();
		}
	}

	private void startRecordThread() throws FileNotFoundException {
        assert recordTmp != null;
        final OutputStream os = new FileOutputStream(recordTmp);

		new Thread(new PriorityRunnable(Process.THREAD_PRIORITY_AUDIO) {

			private void onExit() {
				synchronized (recorderStateMonitor) {
					recorderState = RECORDER_STATE_IDLE;
					recorderStateMonitor.notifyAll();
				}
			}


			@SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
			public void runImpl() {
				int bufferSize = Math.max(BUFFER_BYTES_ELEMENTS * BUFFER_BYTES_PER_ELEMENT,
						AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING));

				AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING, bufferSize);

				try {
					if (recorderState == RECORDER_STATE_STARTING) {
						recorderState = RECORDER_STATE_BUSY;
					}
					recorder.startRecording();

					byte recordBuffer[] = new byte[bufferSize];
					do {
						int bytesRead = recorder.read(recordBuffer, 0, bufferSize);

						if (bytesRead > 0) {
							try {
								os.write(recordBuffer, 0, bytesRead);
							} catch (IOException e) {
								e.printStackTrace();
								onRecordFailure();
							}
						} else {
							Log.e(AudioRecorder.class.getSimpleName(), "error: " + bytesRead);
							onRecordFailure();
						}
					} while (recorderState == RECORDER_STATE_BUSY);
				} finally {
					recorder.release();
				}

				try {
					os.close();

					InputStream is = new FileInputStream(recordTmp);

					try {
                        assert record != null;
                        OutputStream os = new FileOutputStream(record);

						writeHeader(os, recordTmp.length());

						int read;
						final byte[] buf = new byte[1024];
						while ((read = is.read(buf)) > 0) {
							os.write(buf, 0, read);
						}
						os.close();
					} finally {
						is.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
					onRecordFailure();
				}
				recordTmp.delete();

				onExit();
			}
		}).start();
	}

	@Override
	public void cancelRecord() {
		int recorderStateLocal = recorderState;
		if (recorderStateLocal != RECORDER_STATE_IDLE) {
			synchronized (recorderStateMonitor) {
				recorderStateLocal = recorderState;
				if (recorderStateLocal == RECORDER_STATE_STARTING
						|| recorderStateLocal == RECORDER_STATE_BUSY) {

					recorderStateLocal = recorderState = RECORDER_STATE_STOPPING;
				}

				do {
					try {
						if (recorderStateLocal != RECORDER_STATE_IDLE) {
							recorderStateMonitor.wait();
						}
					} catch (InterruptedException ignore) {
						/* Nothing to do */
					}
					recorderStateLocal = recorderState;
				} while (recorderStateLocal == RECORDER_STATE_STOPPING);
			}
		}
	}

	@NonNull
	@Override
	public Uri finishRecord() {
		cancelRecord();

		if (record != null && record.isFile()) {
			return Uri.parse(record.getAbsolutePath());
		}
		return Uri.EMPTY;
	}


	@Override
	public boolean isRecording() {
		return recorderState != RECORDER_STATE_IDLE;
	}


	private static int getRecorderChannelsInCount(int channels) {
		switch (channels) {
			case AudioFormat.CHANNEL_IN_MONO:
				return 1;

			case AudioFormat.CHANNEL_IN_STEREO:
				return 2;

			default:
				throw new IllegalArgumentException("Bad recorder 'channels' constant was specified!");
		}
	}


	private static int getRecorderAudioEncodingBitWidth(int audioEncoding) {
		switch (audioEncoding) {
			case AudioFormat.ENCODING_PCM_16BIT:
				return 16;

			case AudioFormat.ENCODING_PCM_8BIT:
				return 8;

			default:
				throw new IllegalArgumentException("Bad recorder 'audioEncoding' was specified!");
		}
	}


	/**
	 * Write a WAVE file header.
	 *
	 * @param out {@link java.io.OutputStream} to receive the header.
	 * @throws java.io.IOException
	 */
	private static void writeHeader(OutputStream out, long totalAudioLen) throws IOException {

		final int channels = getRecorderChannelsInCount(RECORDER_CHANNELS_IN);
		final int bitrate = getRecorderAudioEncodingBitWidth(RECORDER_AUDIO_ENCODING);

		long sampleRate = RECORDER_SAMPLE_RATE;
		long byteRate = channels * bitrate * sampleRate / 8;
		long totalDataLen = totalAudioLen + 44;

		byte[] header = new byte[44];
		header[0] = 'R';
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >>> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >>> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >>> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';   // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;	// format sub-chunk following metadata length in bytes.
//        header[17] = 0;
//        header[18] = 0;
//        header[19] = 0;
		header[20] = 1;     // 1 for PCM
//        header[21] = 0;
		header[22] = (byte) channels;
//        header[23] = 0;
		header[24] = (byte) (sampleRate & 0xff);
		header[25] = (byte) ((sampleRate >>> 8) & 0xff);
		header[26] = (byte) ((sampleRate >>> 16) & 0xff);
		header[27] = (byte) ((sampleRate >>> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >>> 8) & 0xff);
		header[30] = (byte) ((byteRate >>> 16) & 0xff);
		header[31] = (byte) ((byteRate >>> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);                   // block align
//        header[33] = 0;
		header[34] = (byte) bitrate;                        // bits per sample
//        header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >>> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >>> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >>> 24) & 0xff);

		out.write(header);
	}

}
