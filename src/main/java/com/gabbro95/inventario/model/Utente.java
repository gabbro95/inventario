package com.gabbro95.inventario.model;

import java.time.LocalDate;

public class Utente {
    private int id;
    private String email;
    private String nome;
    private String immagineProfilo;
    private LocalDate dataCreazione;

    public Utente() {}

    public Utente(String email, String nome, String immagineProfilo, LocalDate dataCreazione) {
        this.email = email;
        this.nome = nome;
        this.immagineProfilo = immagineProfilo;
        this.dataCreazione = dataCreazione;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getImmagineProfilo() { return immagineProfilo; }
    public void setImmagineProfilo(String immagineProfilo) { this.immagineProfilo = immagineProfilo; }

    public LocalDate getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDate dataCreazione) { this.dataCreazione = dataCreazione; }
}
