package com.batallanaval.Model;

public class Celda {
    private EstadoCelda estado;
    private int barcoId; // ID del barco al que pertenece esta celda (-1 si no hay barco)

    public Celda() {
        this.estado = EstadoCelda.AGUA;
        this.barcoId = -1;
    }

    public Celda(EstadoCelda estado) {
        this.estado = estado;
        this.barcoId = -1;
    }

    public Celda(EstadoCelda estado, int barcoId) {
        this.estado = estado;
        this.barcoId = barcoId;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public int getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(int barcoId) {
        this.barcoId = barcoId;
    }

    public boolean tieneBarco() {
        return barcoId != -1 && (estado == EstadoCelda.BARCO || estado == EstadoCelda.IMPACTO || estado == EstadoCelda.HUNDIDO);
    }

    public boolean fueAtacada() {
        return estado == EstadoCelda.AGUA_ATACADA || estado == EstadoCelda.IMPACTO || estado == EstadoCelda.HUNDIDO;
    }


}