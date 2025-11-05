package com.tiket.tproyek.database;

import com.tiket.tproyek.model.Tiket;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHandler class to manage ticket data storage
 * This is a simple in-memory implementation
 */
public class DatabaseHandler {
    private List<Tiket> tiketList;

    public DatabaseHandler() {
        this.tiketList = new ArrayList<>();
    }

    /**
     * Save ticket to database
     * @param tiket The ticket to save
     */
    public void saveTiket(Tiket tiket) {
        tiketList.add(tiket);
    }

    /**
     * Get all tickets from database
     * @return List of all tickets
     */
    public List<Tiket> getAllTiket() {
        return new ArrayList<>(tiketList);
    }

    /**
     * Find ticket by ID
     * @param tiketID The ticket ID to search
     * @return Tiket object if found, null otherwise
     */
    public Tiket findTiketById(String tiketID) {
        for (Tiket tiket : tiketList) {
            if (tiket.getTiketID().equals(tiketID)) {
                return tiket;
            }
        }
        return null;
    }

    /**
     * Get total number of tickets
     * @return Number of tickets in database
     */
    public int getTotalTiket() {
        return tiketList.size();
    }

    /**
     * Check if a seat is already booked for a specific transportation and date
     * @param transportasiId Transportation ID
     * @param tanggal Date
     * @param nomorKursi Seat number
     * @return true if seat is already booked, false otherwise
     */
    public boolean isSeatBooked(String transportasiId, String tanggal, int nomorKursi) {
        for (Tiket tiket : tiketList) {
            if (tiket.getTransportasi().getId().equals(transportasiId) &&
                tiket.getTanggal().equals(tanggal) &&
                tiket.getNomorKursi() == nomorKursi) {
                return true;
            }
        }
        return false;
    }
}
