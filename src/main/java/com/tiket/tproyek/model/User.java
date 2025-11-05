package com.tiket.tproyek.model;

public class User {
    private String nama;
    private String email;

    public User() {
    }

    public User(String nama, String email) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email harus mengandung karakter @");
        }
        this.nama = nama;
        this.email = email;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
        this.nama = nama;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email harus mengandung karakter @");
        }
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }
}
