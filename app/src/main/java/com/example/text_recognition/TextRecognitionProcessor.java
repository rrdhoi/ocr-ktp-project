package com.example.text_recognition;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.utils.CameraImageGraphic;
import com.example.utils.FrameMetaData;
import com.example.utils.GraphicOverlay;
import com.example.utils.VisionProcessorBase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private final FirebaseVisionTextRecognizer detector;
    private TextListener listener;

    public TextRecognitionProcessor() {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.d(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(Bitmap originalCameraImage, FirebaseVisionText results, FrameMetaData frameMetadata, GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (FirebaseVisionText.TextBlock block : results.getTextBlocks()) {
            for (FirebaseVisionText.Line line : block.getLines()) {
                for (FirebaseVisionText.Element element : line.getElements()) {
                    TextGraphic textGraphic = new TextGraphic(graphicOverlay, element);
                    graphicOverlay.add(textGraphic);
                    String regexKtpPattern = "[0-9]{16}";
                    Pattern pattern = Pattern.compile(regexKtpPattern);
                    Matcher matcher = pattern.matcher(element.getText());
                    if (matcher.find()) {
                        if (listener != null) {
                            listener.onText(matcher.group());
                        }
                    }
                }
            }
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(Exception e) {
        Log.w(TAG, "Text detection failed: " + e);
    }

    public void readingText(TextListener textListener) {
        listener = textListener;
    }

    public interface TextListener {
        void onText(String text);
    }

    private static final String TAG = "TextRecProc";
}
