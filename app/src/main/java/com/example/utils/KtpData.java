package com.example.utils;

public class KtpData {
    private String nik;
    private String nama;
    private String alamat;
    private String rtrw;
    private String keldesa;
    private String kecamatan;

    public KtpData() {
        this.nik = null;
        this.nama = null;
        this.alamat = null;
        this.rtrw = null;
        this.keldesa = null;
        this.kecamatan = null;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getRtrw() {
        return rtrw;
    }

    public void setRtrw(String rtrw) {
        this.rtrw = rtrw;
    }

    public String getKeldesa() {
        return keldesa;
    }

    public void setKeldesa(String keldesa) {
        this.keldesa = keldesa;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }
}
