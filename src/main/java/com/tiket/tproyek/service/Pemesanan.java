package com.tiket.tproyek.service;

import com.tiket.tproyek.model.Tiket;
import com.tiket.tproyek.model.Transportasi;
import com.tiket.tproyek.model.User;

import java.util.UUID;

public class Pemesanan {

    public Tiket pemesananTiket(Transportasi transportasi, User user, String tanggal, int nomorKursi) {
        // Cek ketersediaan kursi
        if (nomorKursi > transportasi.getJumlahKursi()) {
            throw new IllegalArgumentException("Nomor kursi tidak tersedia. Jumlah kursi maksimal: " + transportasi.getJumlahKursi());
        }

        // Generate unique ticket ID
        String tiketID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Create and return new ticket
        Tiket tiket = new Tiket(tiketID, transportasi, user, tanggal, nomorKursi);

        return tiket;
    }
}
