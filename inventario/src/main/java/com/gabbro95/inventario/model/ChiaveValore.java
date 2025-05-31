package com.gabbro95.inventario.model;

public class ChiaveValore {
    private int id;
    private String chiave;
    private String valore;
    private int oggettoId;

    public ChiaveValore() {}

    public ChiaveValore(int id, String chiave, String valore, int oggettoId) {
        this.id = id;
        this.chiave = chiave;
        this.valore = valore;
        this.oggettoId = oggettoId;
    }

    public ChiaveValore(String chiave, String valore, int oggettoId) {
        this.chiave = chiave;
        this.valore = valore;
        this.oggettoId = oggettoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChiave() {
        return chiave;
    }

    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public int getOggettoId() {
        return oggettoId;
    }

    public void setOggettoId(int oggettoId) {
        this.oggettoId = oggettoId;
    }
}
