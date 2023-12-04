package com.example.utils;

import android.graphics.Rect;

public class FieldChecks {

    public static boolean checkNikField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("nik");
    }

    public static boolean checkNamaField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("nama") || text.equals("nema") || text.equals("name");
    }

    public static boolean checkTglLahirField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("lahir") ||
                text.equals("tempat") ||
                text.equals("tempatigllahir") ||
                text.equals("empatgllahir") ||
                text.equals("tempat/tgl");
    }

    public static boolean checkJenisKelaminField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kelamin") || text.equals("jenis");
    }

    public static boolean checkAlamatField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("alamat") ||
                text.equals("lamat") ||
                text.equals("alaahom") ||
                text.equals("alama") ||
                text.equals("alamao") ||
                text.equals("alamarw");
    }

    public static boolean checkRtRwField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("rt/rw") || text.equals("rw ") || text.equals("rt") || text.equals("rtirw");
    }

    public static boolean checkKelDesaField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kel/desa") || text.equals("helldesa") || text.equals("kelldesa");
    }

    public static boolean checkKecamatanField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kecamatan") || dataText.contains("kecamatan");
    }

    public static boolean checkAgamaField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("agama") || text.equals("gama");
    }

    public static boolean checkKawinField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kawin") || text.equals("perkawinan") || text.equals("perkawinan:");
    }

    public static boolean checkPekerjaanField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kerja") || text.equals("pekerjaan");
    }

    public static boolean checkKewarganegaraanField(String dataText) {
        String text = dataText.toLowerCase().trim();
        return text.equals("kewarganegaraan") ||
                text.equals("negaraan") ||
                text.equals("kewarganegaraan:");
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
