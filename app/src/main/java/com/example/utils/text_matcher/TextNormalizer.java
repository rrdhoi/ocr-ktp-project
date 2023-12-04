package com.example.utils.text_matcher;

public class TextNormalizer {

    public static String normalizeNikText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("NIK", "").replaceAll(":", "").trim();
        return result;
    }

    public static String normalizeNamaText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("NEMA", "")
                .replaceAll("NAME", "")
                .replaceAll(":", "")
                .trim();
        return result;
    }

    public static String normalizeJenisKelaminText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("GOL. DARAHO", "")
                .replaceAll("GOL. DARAH", "")
                .replaceAll("GOL DARAH", "")
                .replaceAll("LAKFEARI", "")
                .replaceAll("LAKFLAK", "")
                .replaceAll("KELAMIN", "")
                .replaceAll("KEIAMIN", "")
                .replaceAll("JENIS", "")
                .replaceAll("DENIS", "")
                .replaceAll("DARAH ", "")
                .replaceAll("ENIS", "")
                .replaceAll("DARA", "")
                .replaceAll("GO", "")
                .replaceAll("L. ", "")
                .replaceAll(" H0", "")
                .replaceAll(" HO", "")
                .replaceAll(":", "")
                .replaceAll(" 0", "")
                .replaceAll(" O", "")
                .trim();

        if (result.equals("LAK-LAK") ||
                result.equals("LAKI-LAK") ||
                result.equals("AK-LAK") ||
                result.equals("LAKFLAKI") ||
                result.equals("LAKHLAK") ||
                result.equals("LAKFEAKI") ||
                result.equals("LAKELAKI") ||
                result.equals("LAKELAK") ||
                result.equals("LAKHLAKI") ||
                result.equals("LAKHEAK") ||
                result.equals("LAK-LAKI") ||
                result.equals("LAKHEAKI") ||
                result.equals("LAKIFEAK") ||
                result.equals("LAKFEAKE") ||
                result.equals("LAKIFEAKI") ||
                result.equals("LAKFEAR") ||
                result.equals("LAKFLAK") ||
                result.equals("LAK-LAKE") ||
                result.equals("LAK-EAK") ||
                result.equals("LAKFEAK") ||
                result.equals("LAK-EAKI") ||
                result.equals("LAKELAKE")) {
            return "Laki-Laki";
        }
        return result;
    }

    public static String normalizeAlamatText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("RI/KEILDESAA", "")
                .replaceAll("RTKELIIDESAA", "")
                .replaceAll("TIKEL/LDESA", "")
                // ... (Pola penggantian dilanjutkan)
                .replaceAll(":", "")
                .replaceAll("=", "")
                .replaceAll("  ", " ")
                .trim();
        System.out.println("result result result result result " + result);
        return result;
    }

    public static String normalizeKawinText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("PERKAWINAN", "")
                .replaceAll("PERKAWINA", "")
                .replaceAll("STATUS", "")
                .replaceAll("TATUS", "")
                .replaceAll("STAFUS", "")
                .replaceAll("R ", "")
                .replaceAll("T ", "")
                .replaceAll(":", "")
                .trim();
        return result;
    }

    public static String normalizePekerjaanText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("PEKERJAAN", "").replaceAll(":", "").trim();
        if (result.equals("PELAJARIMAHASISSWA") ||
                result.equals("PELAJARIMAHASISWA") ||
                result.equals("PELAJARIMAHASISVWA") ||
                result.equals("PELAJARMAHASISWA")) {
            return "Pelajar/Mahasiswa";
        }
        return result;
    }

    public static String normalizeKewarganegaraanText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("KEWARGANEGARAAN", "")
                .replaceAll("EUMUR", "")
                .replaceAll("HDUP", "")
                .replaceAll("H ", "")
                .replaceAll("N ", "")
                .replaceAll(":", "")
                .trim();
        return result;
    }

    public static String normalizeAgamaText(String text) {
        String result = text.toUpperCase();
        result = result.replaceAll("AGAMA", "")
                .replaceAll(":", "")
                .replaceAll("GAMA", "")
                .trim();

        if (result.equals("SLAM") ||
                result.equals("AM") ||
                result.equals("SLA AM") ||
                result.equals("ISLU AM") ||
                result.equals("SL LAM") ||
                result.equals("ISLAME") ||
                result.equals("SLA M") ||
                result.equals("ISL AM") ||
                result.equals("ISLA AM") ||
                result.equals("S AM") ||
                result.equals("SLL AM") ||
                result.equals("SL AM") ||
                result.equals("SE AM") ||
                result.equals("1SLAM") ||
                result.equals("ISLAMM") ||
                result.equals("SLA") ||
                result.equals("LAM")) {
            result = "Islam";
        }
        if (result.trim().isEmpty()) {
            return "";
        } else {
            return result;
        }
    }
}
