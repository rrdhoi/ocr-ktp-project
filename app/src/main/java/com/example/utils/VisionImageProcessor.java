package com.example.utils;

import android.graphics.Bitmap;

import com.example.utils.FrameMetaData;
import com.example.utils.GraphicOverlay;
import com.google.firebase.ml.common.FirebaseMLException;
import java.nio.ByteBuffer;

public interface VisionImageProcessor {

    /**
     * Processes the images with the underlying machine learning models.
     *
     * @param data            ByteBuffer containing the image data
     * @param frameMetadata   Metadata of the image frame
     * @param graphicOverlay  Overlay to draw graphics
     * @throws FirebaseMLException Exception if processing fails
     */
    void process(ByteBuffer data, FrameMetaData frameMetadata, GraphicOverlay graphicOverlay)
            throws FirebaseMLException;

    /**
     * Processes the bitmap images.
     *
     * @param bitmap          Bitmap image data
     * @param graphicOverlay  Overlay to draw graphics
     */
    void process(Bitmap bitmap, GraphicOverlay graphicOverlay);

    /**
     * Stops the underlying machine learning model and releases resources.
     */
    void stop();
}

