package com.gabbro95.inventario.model;

import java.time.LocalDate;

public class Oggetto {
    private int id;
    private String nome;
    private int numero;
    private LocalDate dataInserimento;
    private int contenitoreId;
    private int sogliaMinima;

    // Costruttori, getter e setter...

    public Oggetto() {}

    public Oggetto(String nome, int numero, int contenitoreId) {
        this.nome = nome;
        this.numero = numero;
        this.contenitoreId = contenitoreId;
    }
    
    // Includi tutti i getter e setter per ogni campo
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public LocalDate getDataInserimento() { return dataInserimento; }
    public void setDataInserimento(LocalDate dataInserimento) { this.dataInserimento = dataInserimento; }
    public int getContenitoreId() { return contenitoreId; }
    public void setContenitoreId(int contenitoreId) { this.contenitoreId = contenitoreId; }
    public int getSogliaMinima() { return sogliaMinima; }
    public void setSogliaMinima(int sogliaMinima) { this.sogliaMinima = sogliaMinima; }
}