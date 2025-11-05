package com.tiket.tproyek.model;

public abstract class Transportasi {
    private String id;
    private String nama;
    private int jumlahKursi;
    private double hargaAwal;

    public Transportasi() {
    }

    public Transportasi(String id, String nama, int jumlahKursi, double hargaAwal) {
        this.id = id;
        this.nama = nama;
        if (jumlahKursi <= 0) {
            throw new IllegalArgumentException("Jumlah kursi harus lebih besar dari 0");
        }
        if (hargaAwal < 0) {
            throw new IllegalArgumentException("Harga harus lebih besar atau sama dengan 0");
        }
        this.jumlahKursi = jumlahKursi;
        this.hargaAwal = hargaAwal;
    }

    public String getNama() {
        return nama;
    }

    public void setJumlahKursi(int jumlahKursi) {
        if (jumlahKursi <= 0) {
            throw new IllegalArgumentException("Jumlah kursi harus lebih besar dari 0");
        }
        this.jumlahKursi = jumlahKursi;
    }

    public double getHargaAwal() {
        return hargaAwal;
    }

    public void setHargaAwal(double harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga harus lebih besar atau sama dengan 0");
        }
        this.hargaAwal = harga;
    }

    public abstract double hitungTotal();

    public abstract String getTipe();

    public String getId() {
        return id;
    }

    public int getJumlahKursi() {
        return jumlahKursi;
    }
}
