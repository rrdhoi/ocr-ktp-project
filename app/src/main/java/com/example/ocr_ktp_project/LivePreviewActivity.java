package com.example.ocr_ktp_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.text_recognition.TextRecognitionProcessor;
import com.example.utils.CameraSource;
import com.example.utils.CameraSourcePreview;
import com.example.utils.GraphicOverlay;
import com.google.android.gms.common.annotation.KeepName;
import java.io.IOException;
import java.util.Objects;

/** Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source.  */
@KeepName
public class LivePreviewActivity extends AppCompatActivity {

    private CameraSource cameraSource;
    private CameraSourcePreview firePreview;
    private GraphicOverlay fireFaceOverlay;
    private TextRecognitionProcessor textRecognitionProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LivePreviewActivity ->","onCreate");
        setContentView(R.layout.activity_live_preview);
        textRecognitionProcessor = new TextRecognitionProcessor();
        firePreview = findViewById(R.id.firePreview);
        fireFaceOverlay = findViewById(R.id.fireFaceOverlay);
        createCameraSource();
        observeText();
    }

    @SuppressLint("MissingPermission")
    private void observeText() {
        textRecognitionProcessor.readingText(new TextRecognitionProcessor.TextListener() {
            @Override
            public void onText(String text) {
                if (!text.isEmpty()) {
                    cameraSource.stop();
                    new AlertDialog.Builder(Objects.requireNonNull(peekAvailableContext()))
                            .setTitle("Alert")
                            .setMessage(text)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                try {
                                    cameraSource.start();
                                } catch (IOException e) {
                                    Log.e("error click button", Objects.requireNonNull(e.getMessage()));
                                    throw new RuntimeException(e);
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, findViewById(R.id.fireFaceOverlay));
            cameraSource.setMachineLearningFrameProcessor(textRecognitionProcessor);
        }
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (findViewById(R.id.fireFaceOverlay) == null) {
                    Log.d("start camera source","resume: Preview is null");
                }

                if (findViewById(R.id.fireFaceOverlay) != null) {
                    firePreview.start(cameraSource, fireFaceOverlay);
                }
            } catch (IOException e) {
                Log.e("error", Objects.requireNonNull(e.getMessage()));
                if (cameraSource != null) {
                    cameraSource.release();
                    cameraSource = null;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("On resume","onResume");
        startCameraSource();
    }

    /** Stops the camera.  */
    @Override
    protected void onPause() {
        super.onPause();
        if (firePreview != null) {
            firePreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }
}
