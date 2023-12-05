package com.example.ocr_ktp_project;

import static com.example.pemission.PermissionUtils.askPermissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pemission.PermissionCallbacksDSL;
import com.example.utils.FieldDetector;
import com.example.utils.KtpData;
import com.example.utils.text_matcher.TextNormalizer;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.marchinram.rxgallery.RxGallery;

import org.michaelbel.bottomsheet.BottomSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Uri uriPath;
    Rect nikRect;
    Rect namaRect;
    Rect tempatTanggalLahirRect;

    Rect alamatRect;
    Rect rtrwRect;
    Rect kelDesaRect;
    Rect kecamatanRect;
/*
    String nikResult;
    String namaResult;
    String alamatResult;
    String rtrwResult;
    String kelDesaResult;
    String kecamatanResult;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(new Toolbar(this));
        FirebaseApp.initializeApp(this);

//        findViewById(R.id.buttonAdd).setOnClickListener(view -> showBottomView());
        findViewById(R.id.buttonAdd).setOnClickListener(view -> ImagePicker.with(this)
                .start());
        findViewById(R.id.buttonDetect).setOnClickListener(view -> {
            if (uriPath != null) {
                try {
//                    runKtpDataDetector();
                    runKtpRecognize();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        /*findViewById(R.id.liveCheck).setOnClickListener(view -> {
            Intent intent = new Intent(this, LivePreviewActivity.class);
            startActivity(intent);
        });*/
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

    @SuppressLint("SetTextI18n")
    private void runKtpRecognize() throws IOException {
        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromFilePath(this, uriPath);
        KtpData ktpData = new KtpData();

        recognizer.process(image).addOnSuccessListener(
                visionText -> {
                    try {
                        for (int i = 0; i < visionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < visionText.getTextBlocks().get(i).getLines().size(); j++) {
                                for (int k = 0; k < visionText.getTextBlocks().get(i).getLines().get(j).getElements().size(); k++) {
                                    Text.Element data = visionText.getTextBlocks().get(i).getLines().get(j).getElements().get(k);

                                    if (FieldDetector.checkNikField(data.getText())) {
                                        nikRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "nik Rect detected");
                                    }

                                    if (FieldDetector.checkNamaField(data.getText())) {
                                        namaRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "nameRect Rect detected");
                                    }

                                    if (FieldDetector.checkTglLahirField(data.getText())) {
                                        tempatTanggalLahirRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "tempatTanggalLahirRect field detected");
                                    }

                                    if (FieldDetector.checkAlamatField(data.getText())) {
                                        alamatRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "alamat rect field detected");
                                    }

                                    if (FieldDetector.checkRtRwField(data.getText())) {
                                        rtrwRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "alamat rect field detected");
                                    }

                                    if (FieldDetector.checkKelDesaField(data.getText())) {
                                        kelDesaRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "kel/desa rect detected");
                                    }

                                    if (FieldDetector.checkKecamatanField(data.getText())) {
                                        kecamatanRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "kecamatan rect detected");
                                    }

                                }
                            }
                        }

                        for (int i = 0; i < visionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < visionText.getTextBlocks().get(i).getLines().size(); j++) {
                                Text.Line data = visionText.getTextBlocks().get(i).getLines().get(j);

                              /*  if (FieldDetector.isInside(data.getBoundingBox(), nikRect)) {
                                    String regexNIK = "[0-9]{8,16}";
                                    String textNormalize = TextNormalizer.normalizeNikText(data.getText());

                                    Pattern pattern = Pattern.compile(regexNIK);
                                    Matcher matcher = pattern.matcher(data.getText());

                                    ktpData.setNik(matcher.find() ? textNormalize : null);
                                    Log.d("ktp data result ::::", "nik field detected ------" + nikResult + "-----" + data.getText() + "---" + matcher.find());
                                }*/

//                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), namaRect, tempatTanggalLahirRect)) {
                                if (FieldDetector.isInside(data.getBoundingBox(), namaRect)) {
                                    if (!data.getText().equalsIgnoreCase("nama")) {
                                        ktpData.setNama(TextNormalizer.normalizeNamaText(data.getText()));
                                        Log.d("ktp data result ::::", "nama field detected ------" + ktpData.getNama());
                                    }
                                }

//                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), alamatRect, rtrwRect)) {
                                if (FieldDetector.isInside(data.getBoundingBox(), alamatRect)) {
                                    if (!data.getText().equalsIgnoreCase("alamat")) {
                                        ktpData.setAlamat(TextNormalizer.normalizeAlamatText(data.getText()));
                                        Log.d("ktp data result ::::", "alamat field detected -------" + ktpData.getAlamat());
                                    }
                                }

                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), alamatRect, rtrwRect)) {
                                    if (!data.getText().equalsIgnoreCase("alamat") && !TextNormalizer.normalizeAlamatText(data.getText()).equals(ktpData.getAlamat())) {
                                        ktpData.setAlamat(ktpData.getAlamat() != null ? ktpData.getAlamat() + " " : "" + TextNormalizer.normalizeAlamatText(data.getText()));
                                        Log.d("ktp data result ::::", "alamat 2 field detected -------" + ktpData.getAlamat());
                                    }
                                }

//                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), rtrwRect, kelDesaRect)) {
                                if (FieldDetector.isInside(data.getBoundingBox(), rtrwRect)) {
                                    String filteredRtRw = TextNormalizer.normalizeRtRwText(data.getText());
                                    String regexRtRwPattern = "\\d{3}/\\d{3}";
                                    Pattern pattern = Pattern.compile(regexRtRwPattern);
                                    if (filteredRtRw != null) {
                                        Matcher matcher = pattern.matcher(filteredRtRw);
                                        if (matcher.find()) {
                                            ktpData.setRtrw(matcher.group());
                                        }
                                    }

                                    Log.d("ktp data result ::::", "rt/rw field detected -------" + ktpData.getRtrw());
                                }

//                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), kelDesaRect, kecamatanRect)) {
                                if (FieldDetector.isInside(data.getBoundingBox(), kelDesaRect)) {
                                    ktpData.setKeldesa(TextNormalizer.normalizeDesaKelText(data.getText()));

                                    Log.d("ktp data result ::::", "kel/desa field detected -------" + ktpData.getKeldesa());
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), kecamatanRect)) {
                                    ktpData.setKecamatan(TextNormalizer.normalizeKecamatanText(data.getText()));
                                    Log.d("ktp data result ::::", "kecamatan field detected -------" + ktpData.getKecamatan());
                                }
                            }
                        }
                    } catch (Exception e) {
                        ((TextView) findViewById(R.id.textoutput)).setText("Errorrrrrr" + e.getMessage());
                    }

                    StringBuilder rawDataText = new StringBuilder().append("========= RAW DATA =========" + "\n");

                    for (Text.TextBlock blockText : visionText.getTextBlocks()) {
                        Log.d("Detector execute ->", "block -> " + blockText.getText());
                        int index = visionText.getTextBlocks().indexOf(blockText);
                        rawDataText.append("block [").append(index).append("]: ").append(blockText.getText()).append("\n");

                        String regexNikPattern = "\\d{16}";
                        String filteredNik = blockText.getText().replaceAll("O", "0")
                                .replaceAll("l", "1")
                                .replaceAll("b", "6")
                                .replaceAll("B", "8")
                                .replaceAll("\\?", "7")
                                .replaceAll(" ", "");
                        Pattern pattern = Pattern.compile(regexNikPattern);
                        Matcher matcher = pattern.matcher(filteredNik);
                        if (matcher.find()) {
                            ktpData.setNik(matcher.group());
                        }
                    }

                    ((TextView) findViewById(R.id.textoutput)).setText(
                            "nik = " + ktpData.getNik() + "\n" +
                                    "nama = " + ktpData.getNama() + "\n" +
                                    "alamat = " + ktpData.getAlamat() + "\n" +
                                    "rt/rw = " + ktpData.getRtrw() + "\n" +
                                    "kel/desa = " + ktpData.getKeldesa() + "\n" +
                                    "kecamatan = " + ktpData.getKecamatan() + "\n\n\n" +
                                    "---------------------------" + "\n\n\n" +
                                    rawDataText
                    );
                }).addOnFailureListener(
                e -> {
                    Log.d("Detector execute error ->", e.toString());
                });
    }

/*    private void runKtpDataDetector() throws IOException {
        FirebaseApp.initializeApp(this);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this, uriPath);
        KtpData ktpData = new KtpData();

//        Bitmap imageBitmap = getBitmapFromUri(uriPath);
//        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriPath);
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
//        KtpData ktpData = new KtpData();

        detector.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    try {
                        for (int i = 0; i < firebaseVisionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < firebaseVisionText.getTextBlocks().get(i).getLines().size(); j++) {
                                for (int k = 0; k < firebaseVisionText.getTextBlocks().get(i).getLines().get(j).getElements().size(); k++) {
                                    FirebaseVisionText.Element data = firebaseVisionText.getTextBlocks().get(i).getLines().get(j).getElements().get(k);

                                    if (FieldDetector.checkNikField(data.getText())) {
                                        nikRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "nik Rect detected");
                                    }

                                    if (FieldDetector.checkNamaField(data.getText())) {
                                        namaRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "nameRect Rect detected");
                                    }

                                    if (FieldDetector.checkTglLahirField(data.getText())) {
                                        tempatTanggalLahirRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::","tempatTanggalLahirRect field detected");
                                    }

                                    if (FieldDetector.checkAlamatField(data.getText())) {
                                        alamatRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::","alamat rect field detected");
                                    }

                                    if (FieldDetector.checkRtRwField(data.getText())) {
                                        rtrwRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::","alamat rect field detected");
                                    }

                                    if (FieldDetector.checkKelDesaField(data.getText())) {
                                        kelDesaRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "kel/desa rect detected");
                                    }

                                    if (FieldDetector.checkKecamatanField(data.getText())) {
                                        kecamatanRect = data.getBoundingBox();
                                        Log.d("Rect detect ::::", "kecamatan rect detected");
                                    }

                                }
                            }
                        }

                        for (int i = 0; i < firebaseVisionText.getTextBlocks().size(); i++) {
                            for (int j = 0; j < firebaseVisionText.getTextBlocks().get(i).getLines().size(); j++) {
                                FirebaseVisionText.Line data = firebaseVisionText.getTextBlocks().get(i).getLines().get(j);

                              *//*  if (FieldDetector.isInside(data.getBoundingBox(), nikRect)) {
                                    String regexNIK = "[0-9]{8,16}";
                                    String textNormalize = TextNormalizer.normalizeNikText(data.getText());

                                    Pattern pattern = Pattern.compile(regexNIK);
                                    Matcher matcher = pattern.matcher(data.getText());

                                    ktpData.setNik(matcher.find() ? textNormalize : null);
                                    Log.d("ktp data result ::::", "nik field detected ------" + nikResult + "-----" + data.getText() + "---" + matcher.find());
                                }*//*

                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), namaRect, tempatTanggalLahirRect)) {
                                    namaResult = TextNormalizer.normalizeNamaText(data.getText());
                                    ktpData.setNama(namaResult);
                                    Log.d("ktp data result ::::", "nama field detected ------" + namaResult);
//                                    if (!data.getText().equalsIgnoreCase("nama")) {
//
//                                    }
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), alamatRect)) {
                                    if (!data.getText().equalsIgnoreCase("alamat")) {
                                        alamatResult = TextNormalizer.normalizeAlamatText(data.getText());
                                        ktpData.setAlamat(alamatResult);
                                        Log.d("ktp data result ::::", "alamat field detected -------" + data.getText());
                                    }
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), rtrwRect)) {
                                    rtrwResult = data.getText();
                                    ktpData.setRtrw(rtrwResult);
                                    Log.d("ktp data result ::::", "rt/rw field detected -------" + data.getText());
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), kelDesaRect)) {
                                    kelDesaResult = data.getText();
                                    ktpData.setKeldesa(kelDesaResult);

                                    Log.d("ktp data result ::::", "kel/desa field detected -------" + data.getText());
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), kecamatanRect)) {
                                    kecamatanResult = data.getText();
                                    ktpData.setKecamatan(kecamatanResult);
                                    Log.d("ktp data result ::::", "kecamatan field detected -------" + data.getText());
                                }
                            }
                        }

                        ((TextView) findViewById(R.id.textoutput)).setText(
                                "nik = " + ktpData.getNik() + "\n" +
                                        "nama = " + ktpData.getNama() + "\n" +
                                        "alamat = " + ktpData.getAlamat() + "\n" +
                                        "rt/rw = " + ktpData.getRtrw() + "\n" +
                                        "kel/desa = " + ktpData.getKeldesa() + "\n" +
                                        "kecamatan = " + ktpData.getKecamatan()
                        );

                    } catch (Exception e) {
                        ((TextView) findViewById(R.id.textoutput)).setText("Errorrrrrr" + e.getMessage());
                    }

                    for (FirebaseVisionText.TextBlock blockText : firebaseVisionText.getTextBlocks()) {
                        Log.d("Detector execute ->", "block -> " + blockText.getText());
//                        String regexKtpPattern = "((1[1-9])|(21)|([37][1-6])|(5[1-4])|(6[1-5])|([8-9][1-2]))[0-9]{2}[0-9]{2}(([0-6][0-9])|(7[0-1]))((0[1-9])|(1[0-2]))([0-9]{2})[0-9]{4}}";

//                        String regexNIK = "[0-9]{8,16}";
//                        String textNormalize = TextNormalizer.normalizeNikText(blockText.getText());
//
//                        Pattern pattern = Pattern.compile(regexNIK);
//                        Matcher matcher = pattern.matcher(blockText.getText());
                        String regexKtpPattern = "[0-9]{8,16}";
                        Pattern pattern = Pattern.compile(regexKtpPattern);
                        Matcher matcher = pattern.matcher(blockText.getText());
                        if (matcher.find()) {
                            ktpData.setNik(matcher.group());
                        }

//                        ktpData.setNik(matcher.find() ? TextNormalizer.normalizeNikText(matcher.group()) : null);
                    }
                })
                .addOnFailureListener(e -> Log.d("Detector execute error ->", e.toString()));
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            uriPath = data.getData();
        }
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
