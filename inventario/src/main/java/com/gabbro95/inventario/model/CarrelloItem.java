// File: src/main/java/com/gabbro95/inventario/model/CarrelloItem.java
package com.gabbro95.inventario.model;

public class CarrelloItem {
    
    private int oggettoId;
    private String oggettoNome;
    private int quantitaAttuale;

    // Costruttori, Getter e Setter
    public CarrelloItem() {}

    public int getOggettoId() { return oggettoId; }
    public void setOggettoId(int oggettoId) { this.oggettoId = oggettoId; }
    public String getOggettoNome() { return oggettoNome; }
    public void setOggettoNome(String oggettoNome) { this.oggettoNome = oggettoNome; }
    public int getQuantitaAttuale() { return quantitaAttuale; }
    public void setQuantitaAttuale(int quantitaAttuale) { this.quantitaAttuale = quantitaAttuale; }
}