package com.example.utils;

import android.graphics.Rect;

public class FieldDetector {

    public static boolean checkNikField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("nik");
    }

    public static boolean checkNamaField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_NAMA.contains(text);
    }

    public static boolean checkTglLahirField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_TEMPAT_TGL_LAHIR.contains(text);
    }

    public static boolean checkAlamatField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_ALAMAT.contains(text);
    }

    public static boolean checkRtRwField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_RT_RW.contains(text);
    }

    public static boolean checkKelDesaField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_KEL_DESA.contains(text);
    }

    public static boolean checkKecamatanField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return StringConstant.FIELD_KECAMATAN.contains(text);
    }

    public static boolean isInside(Rect rect, Rect isInside) {
        if (rect == null || isInside == null) {
            return false;
        }

        return rect.centerY() <= isInside.bottom &&
                rect.centerY() >= isInside.top &&
                rect.centerY() >= isInside.right &&
                rect.centerX() >= isInside.left;
    }

    public static boolean isInside3Rect(Rect isThisRect, Rect isInside, Rect andAbove) {
        if (isThisRect == null || isInside == null || andAbove == null) {
            return false;
        }

        return isThisRect.centerY() <= andAbove.top &&
                isThisRect.centerY() >= isInside.top &&
                isThisRect.centerX() >= isInside.left;
    }
}
