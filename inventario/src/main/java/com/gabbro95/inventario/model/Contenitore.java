package com.gabbro95.inventario.model;

public class Contenitore {
    private int id;
    private String nome;
    private int utenteId; // <-- MODIFICA: da String emailUtente a int utenteId

    public Contenitore() {}

    // Costruttore aggiornato
    public Contenitore(int id, String nome, int utenteId) {
        this.id = id;
        this.nome = nome;
        this.utenteId = utenteId;
    }

    // Costruttore aggiornato
    public Contenitore(String nome, int utenteId) {
        this.nome = nome;
        this.utenteId = utenteId;
    }

    // Metodi Get/Set standard
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter aggiornati
    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }
}