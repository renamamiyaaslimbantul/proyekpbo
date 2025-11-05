package com.tiket.tproyek.model;

public class Tiket {
    private String tiketID;
    private Transportasi transportasi;
    private User user;
    private String tanggal;
    private int nomorKursi;
    private double harga;

    public Tiket() {
    }

    public Tiket(String tiketID, Transportasi transportasi, User user, String tanggal, int nomorKursi) {
        if (nomorKursi <= 0) {
            throw new IllegalArgumentException("Nomor kursi harus lebih besar dari 0");
        }
        this.tiketID = tiketID;
        this.transportasi = transportasi;
        this.user = user;
        this.tanggal = tanggal;
        this.nomorKursi = nomorKursi;
        this.harga = transportasi.hitungTotal();
    }

    public String getTiketID() {
        return tiketID;
    }

    public Transportasi getTransportasi() {
        return transportasi;
    }

    public User getUser() {
        return user;
    }

    public String getTanggal() {
        return tanggal;
    }

    public int getNomorKursi() {
        return nomorKursi;
    }

    public double getHarga() {
        return harga;
    }
}
