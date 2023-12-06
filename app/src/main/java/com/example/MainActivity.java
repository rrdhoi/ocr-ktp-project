package com.example;

import static com.example.pemission.PermissionUtils.askPermissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ocr_ktp_project.R;
import com.example.pemission.PermissionCallbacksDSL;
import com.example.utils.FieldDetector;
import com.example.utils.KtpData;
import com.example.utils.StringConstant;
import com.example.utils.TextNormalizer;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
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

    String resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(new Toolbar(this));

        findViewById(R.id.buttonAdd).setOnClickListener(view -> ImagePicker.with(this)
                .start());
        findViewById(R.id.buttonDetect).setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Result", resultData);
            clipboard.setPrimaryClip(clip);
            showToast("Teks berhasil disalin ke clipboard");
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
                                        Log.d("Rect detect ::::", "rtrw rect field detected");
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


                                if (FieldDetector.isInside(data.getBoundingBox(), namaRect)) {
                                    String text = data.getText().toLowerCase().trim();
                                    if (!StringConstant.FIELD_NAMA.contains(text)) {
                                        ktpData.setNama(TextNormalizer.normalizeNamaText(data.getText()));
                                        Log.d("ktp data result ::::", "nama field detected ------" + ktpData.getNama());
                                    }
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), alamatRect)) {
                                    String text = data.getText().toLowerCase().trim();
                                    if (!StringConstant.FIELD_ALAMAT.contains(text)) {
                                        ktpData.setAlamat(TextNormalizer.normalizeAlamatText(data.getText()));
                                        Log.d("ktp data result ::::", "alamat field detected -------" + ktpData.getAlamat());
                                    }
                                }

                                if (FieldDetector.isInside3Rect(data.getBoundingBox(), alamatRect, rtrwRect)) {
                                    String text = data.getText().toLowerCase().trim();
                                    if (!StringConstant.FIELD_ALAMAT.contains(text) && !TextNormalizer.normalizeAlamatText(data.getText()).equals(ktpData.getAlamat())) {
                                        ktpData.setAlamat(ktpData.getAlamat() != null ? ktpData.getAlamat() + " " + TextNormalizer.normalizeAlamatText(data.getText()) : "");
                                        Log.d("ktp data result ::::", "alamat 2 field detected -------" + ktpData.getAlamat());
                                    }
                                }

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

                                    Log.d("ktp equals data ::::::::  )",data.getText() + " || " + ktpData.getRtrw());
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), kelDesaRect)) {
                                    String text = data.getText().toLowerCase().trim();

                                    if (!StringConstant.FIELD_KEL_DESA.contains(text)) {
                                        ktpData.setKeldesa(TextNormalizer.normalizeDesaKelText(data.getText()));
                                        Log.d("ktp data result ::::", "kel/desa field detected -------" + ktpData.getKeldesa());
                                    }
                                }

                                if (FieldDetector.isInside(data.getBoundingBox(), kecamatanRect)) {
                                    String text = data.getText().toLowerCase().trim();

                                    if (!StringConstant.FIELD_KECAMATAN.contains(text)) {
                                        ktpData.setKecamatan(TextNormalizer.normalizeKecamatanText(data.getText()));
                                        Log.d("ktp data result ::::", "kecamatan field detected -------" + ktpData.getKecamatan());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        ((TextView) findViewById(R.id.textoutput)).setText("Errorrrrrr" + e.getMessage());
                    }

                    StringBuilder rawDataText = new StringBuilder().append("\n========= RAW DATA =========" + "\n\n");

                    for (Text.TextBlock blockText : visionText.getTextBlocks()) {
                        Log.d("Detector execute ->", "block -> " + blockText.getText());
                        int index = visionText.getTextBlocks().indexOf(blockText);
                        rawDataText.append("block [").append(index).append("]: ").append(blockText.getText()).append("\n");

                        String regexNikPattern = "\\d{16}";

                        // replacing value
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

                    String data =  "nik = " + ktpData.getNik() + "\n" +
                            "nama = " + ktpData.getNama() + "\n" +
                            "alamat = " + ktpData.getAlamat() + "\n" +
                            "rt/rw = " + ktpData.getRtrw() + "\n" +
                            "kel/desa = " + ktpData.getKeldesa() + "\n" +
                            "kecamatan = " + ktpData.getKecamatan() + "\n"+
                            rawDataText;

                    resultData = data;
                    ((TextView) findViewById(R.id.textoutput)).setText(data);
                }).addOnFailureListener(
                e -> {
                    Log.d("Detector execute error ->", e.toString());
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            try {
                uriPath = data.getData();
                ImageView imageView = findViewById(R.id.iv_ktp);
                Glide.with(this)
                        .load(uriPath)
                        .into(imageView);
                runKtpRecognize();
            } catch (Exception e) {
                Log.d("Recognition Error :::: ", e.toString());

            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
