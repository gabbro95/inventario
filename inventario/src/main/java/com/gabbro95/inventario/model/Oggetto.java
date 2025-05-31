package com.gabbro95.inventario.model;

import java.time.LocalDate;

public class Oggetto {
    private int id;
    private String nome;
    private int numero;
    private LocalDate dataInserimento;
    private int contenitoreId;

    public Oggetto(int id, String nome, int numero, LocalDate dataInserimento, int contenitoreId) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.dataInserimento = dataInserimento;
        this.contenitoreId = contenitoreId;
    }
    // Getter e Setter
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
}
