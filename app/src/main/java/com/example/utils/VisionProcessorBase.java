package com.example.utils;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata.Builder;

import java.nio.ByteBuffer;

public abstract class VisionProcessorBase<T> implements VisionImageProcessor {

    private ByteBuffer latestImage;
    private FrameMetaData latestImageMetaData;
    private ByteBuffer processingImage;
    private FrameMetaData processingMetaData;

    @Override
    public synchronized void process(
            ByteBuffer data, FrameMetaData FrameMetaData, GraphicOverlay graphicOverlay) {

        latestImage = data;
        latestImageMetaData = FrameMetaData;

        if (processingImage == null && processingMetaData == null) {
            processLatestImage(graphicOverlay);
        }
    }

    @Override
    public void process(Bitmap bitmap, GraphicOverlay graphicOverlay) {
        // Not implemented in this abstract class
    }

    private synchronized void processLatestImage(GraphicOverlay graphicOverlay) {
        processingImage = latestImage;
        processingMetaData = latestImageMetaData;
        latestImage = null;
        latestImageMetaData = null;

        if (processingImage != null && processingMetaData != null) {
            processImage(processingImage, processingMetaData, graphicOverlay);
        }
    }

    private void processImage(
            ByteBuffer data, FrameMetaData FrameMetaData, GraphicOverlay graphicOverlay) {

        FirebaseVisionImageMetadata metadata = new Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setWidth(FrameMetaData.getWidth())
                .setHeight(FrameMetaData.getHeight())
                .setRotation(FrameMetaData.getRotation())
                .build();

        Bitmap bitmap = BitmapUtils.getBitmap(data, FrameMetaData);
        FirebaseVisionImage image = FirebaseVisionImage.fromByteBuffer(data, metadata);
        detectInVisionImage(bitmap, image, FrameMetaData, graphicOverlay);
    }

    private void detectInVisionImage(
            Bitmap originalCameraImage, FirebaseVisionImage image, FrameMetaData metadata,
            GraphicOverlay graphicOverlay) {

        Task<T> task = detectInImage(image);
        task.addOnSuccessListener(
                        results -> {
                            onSuccess(originalCameraImage, results, metadata, graphicOverlay);
                            processLatestImage(graphicOverlay);
                        })
                .addOnFailureListener(
                        e -> onFailure(e)
                );
    }

    @Override
    public void stop() {
        // Not implemented in this abstract class
    }

    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    protected abstract void onSuccess(
            Bitmap originalCameraImage, T results, FrameMetaData FrameMetaData,
            GraphicOverlay graphicOverlay);

    protected abstract void onFailure(Exception e);
}
