package com.github.iojjj.bootstrap.camera;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cvetl on 20.08.2016.
 */

public class VideoCaptureOptions {

    private int mAudioChannels;
    @AudioEncoder
    private int mAudioEncoder;
    private int mAudioSamplingRate;
    private double mLatitude;
    private double mLongitude;
    private int mMaxDurationInMs;
    private long mMaxFileSizeBytes;
    @OutputFormat
    private int mOutputFormat;
    @VideoEncoder
    private int mVideoEncoder;
    private int mVideoEncodingBitrate;
    private int mVideoFrameRate;

    private VideoCaptureOptions(@NonNull Builder builder) {
        mAudioEncoder = builder.mAudioEncoder;
        mVideoEncoder = builder.mVideoEncoder;
        mOutputFormat = builder.mOutputFormat;
        mVideoEncodingBitrate = builder.mVideoEncodingBitrate;
        mVideoFrameRate = builder.mVideoFrameRate;
        mAudioChannels = builder.mAudioChannels;
        mAudioSamplingRate = builder.mAudioSamplingRate;
        mMaxDurationInMs = builder.mMaxDurationInMs;
        mMaxFileSizeBytes = builder.mMaxFileSizeBytes;
        mLatitude = builder.mLatitude;
        mLongitude = builder.mLongitude;
    }

    public int getAudioChannels() {
        return mAudioChannels;
    }

    public int getAudioEncoder() {
        return mAudioEncoder;
    }

    public int getAudioSamplingRate() {
        return mAudioSamplingRate;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getMaxDurationInMs() {
        return mMaxDurationInMs;
    }

    public long getMaxFileSizeBytes() {
        return mMaxFileSizeBytes;
    }

    public int getOutputFormat() {
        return mOutputFormat;
    }

    public int getVideoEncoder() {
        return mVideoEncoder;
    }

    public int getVideoEncodingBitrate() {
        return mVideoEncodingBitrate;
    }

    public int getVideoFrameRate() {
        return mVideoFrameRate;
    }

    @SuppressLint("InlinedApi")
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MediaRecorder.AudioEncoder.DEFAULT, MediaRecorder.AudioEncoder.AAC,
            MediaRecorder.AudioEncoder.AAC_ELD, MediaRecorder.AudioEncoder.AMR_NB,
            MediaRecorder.AudioEncoder.AMR_WB, MediaRecorder.AudioEncoder.HE_AAC,
            MediaRecorder.AudioEncoder.VORBIS})
    public @interface AudioEncoder {
    }

    @SuppressLint("InlinedApi")
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MediaRecorder.VideoEncoder.DEFAULT, MediaRecorder.VideoEncoder.H263,
            MediaRecorder.VideoEncoder.H264, MediaRecorder.VideoEncoder.MPEG_4_SP,
            MediaRecorder.VideoEncoder.VP8})
    public @interface VideoEncoder {
    }

    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MediaRecorder.OutputFormat.DEFAULT, MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP})
    public @interface OutputFormat {
    }

    public static final class Builder {

        private int mAudioChannels;
        @AudioEncoder
        private int mAudioEncoder;
        private int mAudioSamplingRate;
        private double mLatitude;
        private double mLongitude;
        private int mMaxDurationInMs;
        private long mMaxFileSizeBytes;
        @OutputFormat
        private int mOutputFormat;
        @VideoEncoder
        private int mVideoEncoder;
        private int mVideoEncodingBitrate;
        private int mVideoFrameRate;

        public VideoCaptureOptions build() {
            return new VideoCaptureOptions(this);
        }

        public Builder setAudioChannels(int audioChannels) {
            mAudioChannels = audioChannels;
            return this;
        }

        public Builder setAudioEncoder(@AudioEncoder int audioEncoder) {
            mAudioEncoder = audioEncoder;
            return this;
        }

        public Builder setAudioSamplingRate(int audioSamplingRate) {
            mAudioSamplingRate = audioSamplingRate;
            return this;
        }

        public Builder setLocation(double latitude, double longitude) {
            mLatitude = latitude;
            mLongitude = longitude;
            return this;
        }

        public Builder setMaxDurationInMs(int maxDurationInMs) {
            mMaxDurationInMs = maxDurationInMs;
            return this;
        }

        public Builder setMaxFileSizeBytes(long maxFileSizeBytes) {
            mMaxFileSizeBytes = maxFileSizeBytes;
            return this;
        }

        public Builder setOutputFormat(@OutputFormat int outputFormat) {
            mOutputFormat = outputFormat;
            return this;
        }

        public Builder setVideoEncoder(@VideoEncoder int videoEncoder) {
            mVideoEncoder = videoEncoder;
            return this;
        }

        public Builder setVideoEncodingBitrate(int videoEncodingBitrate) {
            mVideoEncodingBitrate = videoEncodingBitrate;
            return this;
        }

        public Builder setVideoFrameRate(int videoFrameRate) {
            mVideoFrameRate = videoFrameRate;
            return this;
        }
    }
}
