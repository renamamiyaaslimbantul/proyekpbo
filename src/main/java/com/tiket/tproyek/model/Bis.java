package com.tiket.tproyek.model;

public class Bis extends Transportasi {
    private boolean adaAC;

    public Bis() {
    }

    public Bis(String id, String nama, int jumlahKursi, double hargaAwal, boolean adaAC) {
        super(id, nama, jumlahKursi, hargaAwal);
        this.adaAC = adaAC;
    }

    public boolean isAdaAC() {
        return adaAC;
    }

    @Override
    public double hitungTotal() {
        double total = getHargaAwal();
        if (adaAC) {
            total += 50000; // Additional charge for AC
        }
        return total;
    }

    @Override
    public String getTipe() {
        return "Bus " + (adaAC ? "(AC)" : "(Non-AC)");
    }
}
