package com.tiket.tproyek.model;

public class Pesawat extends Transportasi {
    private boolean penerbanganInter;

    public Pesawat() {
    }

    public Pesawat(String id, String nama, int jumlahKursi, double hargaAwal, boolean penerbanganInter) {
        super(id, nama, jumlahKursi, hargaAwal);
        this.penerbanganInter = penerbanganInter;
    }

    public boolean isPenerbanganInter() {
        return penerbanganInter;
    }

    @Override
    public double hitungTotal() {
        double total = getHargaAwal();
        if (penerbanganInter) {
            total += 500000; // Additional charge for international flights
        }
        return total;
    }

    @Override
    public String getTipe() {
        return "Pesawat " + (penerbanganInter ? "(Internasional)" : "(Domestik)");
    }
}
