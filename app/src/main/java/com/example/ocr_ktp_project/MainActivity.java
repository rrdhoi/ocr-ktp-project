package com.example.ocr_ktp_project;

import static com.example.pemission.PermissionUtils.askPermissions;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pemission.PermissionCallbacksDSL;
import com.example.utils.FieldChecks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.marchinram.rxgallery.RxGallery;

import org.michaelbel.bottomsheet.BottomSheet;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Uri uriPath;
    Rect nikRect;
    Rect namaRect;
    Rect tempatTanggalLahirRect;

    Rect alamatRect;
    Rect rtrwRect;
    Rect kelDesaRect;
    Rect kecamatanRect;


    String nikResult;
    String nameResult;
    String tempatTanggalLahirResult;
    Rect alamatResult;
    Rect rtrwResult;
    Rect kelDesaResult;
    Rect kecamatanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(new Toolbar(this));
        FirebaseApp.initializeApp(this);

        findViewById(R.id.buttonAdd).setOnClickListener(view -> showBottomView());
        findViewById(R.id.buttonDetect).setOnClickListener(view -> {
            if (uriPath != null) {
                try {
                    startOCR();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        findViewById(R.id.liveCheck).setOnClickListener(view -> {
            Intent intent = new Intent(this, LivePreviewActivity.class);
            startActivity(intent);
        });
        reqPermission();
    }

    private void reqPermission() {
        askPermissions(
                this,
                new PermissionCallbacksDSL() {{
                    onGranted(() -> showToast("permission on granted"));
                    onDenied(permissions -> showToast("permission on denied"));
                    onShowRationale(permissionRequest -> showToast("permission show rationale"));
                    onNeverAskAgain(permissions -> showToast("permission never ask again"));
                }},
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
    }

    private void startOCR() throws IOException {
        FirebaseApp.initializeApp(this);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this, uriPath);

        detector.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {

                    try {
                        for (int i = 0; i < firebaseVisionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < firebaseVisionText.getTextBlocks().get(i).getLines().size(); j++) {
                                for (int k = 0; k < firebaseVisionText.getTextBlocks().get(i).getLines().get(j).getElements().size(); k++) {
                                    FirebaseVisionText.Element data = firebaseVisionText.getTextBlocks().get(i).getLines().get(j).getElements().get(k);
                                    Log.d("01 ->>> ::::::: ","b" + i + " l" + j + " e" + k + " " +
                                            data.getText().toLowerCase().trim().replaceAll(" ", "") +
                                            " " +
                                            Objects.requireNonNull(data.getBoundingBox()).centerX() + "  ------- " + Objects.requireNonNull(data.getBoundingBox()).centerY());

                                    if (FieldChecks.checkNikField(data.getText())) {
                                        nikRect = data.getBoundingBox();
                                        Log.d("nik detect","nik field detected");
                                    }

                                    if (FieldChecks.checkNamaField(data.getText())) {
                                        namaRect = data.getBoundingBox();
                                        Log.d("name detect","nameRect field detected");
                                    }

                                    if (FieldChecks.checkTglLahirField(data.getText())) {
                                        tempatTanggalLahirRect = data.getBoundingBox();
                                        Log.d("tempat tanggal lahir detect","tempatTanggalLahirRect field detected");
                                    }


                                }
                            }
                        }

                        Log.d("---------","---------------------------");
                        Log.d("rect", "nik rect " + nikRect.toString());
                        Log.d("rect", "nama rect " + namaRect.toString());
                        Log.d("rect", "tggl lahir rect " + tempatTanggalLahirRect.toString());
                        Log.d("---------","---------------------------");

                        for (int i = 0; i < firebaseVisionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < firebaseVisionText.getTextBlocks().get(i).getLines().size(); j++) {
                                FirebaseVisionText.Line data = firebaseVisionText.getTextBlocks().get(i).getLines().get(j);
                                if (FieldChecks.isInside(data.getBoundingBox(), nikRect)) {
                                    nikResult = data.getText();
                                    Log.d("nik result","nik field detected ------" + nikResult);
                                    if (!nikResult.isEmpty()) {
                                        Log.d("nik result","nik field detected ------" + nikResult);
                                    }
                                }

                                if (FieldChecks.isInside3Rect(data.getBoundingBox(), namaRect, tempatTanggalLahirRect)) {
                                    if (!data.getText().equalsIgnoreCase("nama")) {
                                        nameResult = data.getText();
                                        if (!nameResult.isEmpty()){
//                                            Log.d("name result","nik field detected ------" + TextNormalizer.normalizeNamaText(nameResult));
                                            Log.d("name result","nik field detected ------" + nameResult);
                                        }
                                    }
                                }

                                if (FieldChecks.isInside(data.getBoundingBox(), tempatTanggalLahirRect)) {
                                    String temp = data.getText().replaceAll("Tempat/Tgi Lahir", "");
//                                    tempatTanggalLahirResult = temp.substring(0, temp.indexOf(',') + 1);
                                    tempatTanggalLahirResult = temp;
                                    if (!tempatTanggalLahirResult.isEmpty()) {
//                                        Log.d("tempat tanggal lahir result","nik field detected -------" + TextNormalizer.normalizeAlamatText(tempatTanggalLahirResult));
                                        Log.d("tempat tanggal lahir result","nik field detected -------" + temp);
                                    }
                                }
                            }
                        }

//                        ((TextView) findViewById(R.id.textoutput)).setText("nik : " + TextNormalizer.normalizeNikText(nikResult) + " --- nama : " +TextNormalizer.normalizeNamaText(nameResult));
                        ((TextView) findViewById(R.id.textoutput)).setText("nik : " + nikResult + " --- nama : " + nameResult + "---" + "tggl lahir : " + tempatTanggalLahirResult);

                    } catch (Exception e) {
                        ((TextView) findViewById(R.id.textoutput)).setText(e.getMessage());
                    }

                 /*   for (FirebaseVisionText.TextBlock blockText : firebaseVisionText.getTextBlocks()) {
                        Log.d("Detector execute ->", blockText.getText());
//                        String regexKtpPattern = "((1[1-9])|(21)|([37][1-6])|(5[1-4])|(6[1-5])|([8-9][1-2]))[0-9]{2}[0-9]{2}(([0-6][0-9])|(7[0-1]))((0[1-9])|(1[0-2]))([0-9]{2})[0-9]{4}}";
                        String regexKtpPattern = "[0-9]{8,16}";
                        Pattern pattern = Pattern.compile(regexKtpPattern);
                        Matcher matcher = pattern.matcher(blockText.getText()).usePattern(pattern);
                        if (matcher.find()) {
                            ((TextView) findViewById(R.id.textoutput)).setText(matcher.group());
                        }
                    }
                    new Thread(() -> {

                    });*/
                })
                .addOnFailureListener(e -> Log.d("Detector execute error ->", e.toString()));
    }

    private void showBottomView() {
        String[] items = {"Camera", "Galery"};
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setDarkTheme(false);
        builder.setWindowDimming(80);
        builder.setDividers(false);
        builder.setFullWidth(false);
        builder.setItems(
                items,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            RxGallery.photoCapture(this).subscribe(uriPhoto -> {
                                Log.d("take photo ->", uriPhoto.toString());
                                uriPath = uriPhoto;
                            }, failed -> {
                                if (failed.getMessage() != null) {
                                    showToast(failed.getMessage());
                                }
                            });
                            break;
                        case 1:
                            RxGallery.gallery(this, false, RxGallery.MimeType.IMAGE).subscribe(uriPhoto -> {
                                Log.d("take photo ->", uriPhoto.toString());
                                uriPath = uriPhoto.get(0);
                            }, failed -> {
                                if (failed.getMessage() != null) {
                                    showToast(failed.getMessage());
                                }
                            });
                            break;
                    }
                }
        );
        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
