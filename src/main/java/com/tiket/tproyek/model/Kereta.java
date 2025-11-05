package com.tiket.tproyek.model;

public class Kereta extends Transportasi {
    private String tipeKelas;

    public Kereta() {
    }

    public Kereta(String id, String nama, int jumlahKursi, double hargaAwal, String tipeKelas) {
        super(id, nama, jumlahKursi, hargaAwal);
        this.tipeKelas = tipeKelas;
    }

    public String getTipeKelas() {
        return tipeKelas;
    }

    @Override
    public double hitungTotal() {
        double total = getHargaAwal();
        if (tipeKelas != null) {
            switch (tipeKelas.toLowerCase()) {
                case "eksekutif":
                    total *= 1.5;
                    break;
                case "bisnis":
                    total *= 1.3;
                    break;
                case "ekonomi":
                default:
                    break;
            }
        }
        return total;
    }

    @Override
    public String getTipe() {
        return "Kereta " + (tipeKelas != null ? tipeKelas : "");
    }
}
