package com.gabbro95.inventario.model;

public class Contenitore {
    private int id;
    private String nome;
    private String emailUtente;

    public Contenitore() {}

    public Contenitore(int id, String nome, String emailUtente) {
        this.id = id;
        this.nome = nome;
        this.emailUtente = emailUtente;
    }

    public Contenitore(String nome, String emailUtente) {
        this.nome = nome;
        this.emailUtente = emailUtente;
    }

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

    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }
}
