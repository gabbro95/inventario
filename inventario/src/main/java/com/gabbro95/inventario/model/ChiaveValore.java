package com.gabbro95.inventario.model;

public class ChiaveValore {
    private int id;
    private String chiave;
    private String valore;
    private int oggettoId;
    
    // Costruttori, Getter e Setter
    public ChiaveValore() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getChiave() { return chiave; }
    public void setChiave(String chiave) { this.chiave = chiave; }
    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }
    public int getOggettoId() { return oggettoId; }
    public void setOggettoId(int oggettoId) { this.oggettoId = oggettoId; }
}