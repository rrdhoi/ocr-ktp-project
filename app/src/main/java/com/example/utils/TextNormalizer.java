package com.example.utils;

import java.util.Objects;

public class TextNormalizer {

    public static String normalizeNikText(String text) {
        String result = text.toUpperCase();
        result = result
                .replaceAll("NIK", "")
                .replaceAll(":", "")
                .trim();
        return result;
    }

    public static String normalizeNamaText(String text) {
        String result = text.toUpperCase();

        for (String element : StringConstant.FIELD_NAMA) {
            result = result.replaceAll(element.toUpperCase(), "");
        }

        result = result
                .replaceAll("NIK", "")
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll("1", "I")
                .replaceAll("\\s+", " ")
                .trim();

        result = fixAsciiCharacters(result);

        return result.isEmpty() ? null : result;
    }

    public static String normalizeAlamatText(String text) {
        String result = text.toUpperCase();

        for (String element : StringConstant.FIELD_ALAMAT) {
            result = result.replaceAll(element.toUpperCase(), "");
        }

        result = result.replaceAll("RI/KEILDESAA", "")
                .replaceAll("RTKELIIDESAA", "")
                .replaceAll("TIKEL/LDESA", "")
                .replaceAll(":", "")
                .replaceAll("=", "")
                .replaceAll("\\s+", " ")
                .trim();

        result = fixAsciiCharacters(result);
        return result;
    }

    public static String normalizeRtRwText(String text) {
        String result = text.toUpperCase();

        for (String element : StringConstant.FIELD_RT_RW) {
            result = result.replaceAll(element.toUpperCase(), "");
        }

        result = result
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll("=", "")
                .replaceAll("\\s+", " ")
                .replaceAll("O", "0")
                .trim();

        result = addSlashEveryThreeDigits(result);
        return Objects.equals(result, "") ? null : result;
    }

    public static String normalizeDesaKelText(String text) {
        String result = text.toUpperCase();

        for (String element : StringConstant.FIELD_KEL_DESA) {
            result = result.replaceAll(element.toUpperCase(), "");
        }

        result = result
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll("=", "")
                .replaceAll("\\s+", " ")
                .trim();

        result = fixAsciiCharacters(result);
        return result;
    }

    public static String normalizeKecamatanText(String text) {
        String result = text.toUpperCase();

        for (String element : StringConstant.FIELD_KECAMATAN) {
            result = result.replaceAll(element.toUpperCase(), "");
        }

        result = result
                .replaceAll(":", "")
                .replaceAll("-", "")
                .replaceAll("=", "")
                .replaceAll("\\s+", " ")
                .trim();

        result = fixAsciiCharacters(result);
        return result;
    }

    public static String addSlashEveryThreeDigits(String text) {
        StringBuilder result = new StringBuilder();
        int count = 0;

        if (!text.contains("/")) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                result.append(c);

                if (Character.isDigit(c)) {
                    count++;

                    if (count == 3 && i < text.length() - 1 && text.charAt(i + 1) != '/') {
                        result.append("/");
                        count = 0;
                    }
                } else if (c == '/') {
                    count = 0;
                }
            }
        } else {
            return text;
        }

        if (result.indexOf("/") == 0) {
            return null;
        }

        return result.toString();
    }

    public static String fixAsciiCharacters(String text) {

        return text
                .replaceAll("Ä", "A")
                .replaceAll("Ü", "U")
                .replaceAll("ü", "u")
                .replaceAll("Ö", "O")
                .replaceAll("ö", "o")
                .replaceAll("Ñ", "N")
                .replaceAll("Ë", "E")
                .replaceAll("ë", "e")
                .replaceAll("ÿ", "y")
                .replaceAll("ï", "i");
    }
}
