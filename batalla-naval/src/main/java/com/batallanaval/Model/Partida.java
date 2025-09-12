package com.batallanaval.Model;

import java.util.UUID;
import java.util.Random;

public class Partida {

    private final String partidaId;          // código de ingreso de la partida
    private final Tablero jugador1Tablero;
    private final Tablero jugador2Tablero;
    private final UUID jugador1Id;
    private UUID jugador2Id;
    private UUID turnoActual;
    private boolean juegoTerminado;
    private String ganador;

    // Constructor recibe String como código de partida
    public Partida(String partidaId) {
        this.partidaId = partidaId;          // código ingresado por el usuario
        this.jugador1Tablero = new Tablero();
        this.jugador2Tablero = new Tablero();
        this.jugador1Id = UUID.randomUUID();
        this.jugador2Id = null;
        this.turnoActual = null;
        this.juegoTerminado = false;
        this.ganador = null;
    }

    public String getPartidaId() {
        return partidaId;
    }

    public Tablero getTablero(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) return jugador1Tablero;
        if (jugador2Id != null && jugadorId.equals(jugador2Id)) return jugador2Tablero;
        return null;
    }

    public Tablero getTableroOponente(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) return jugador2Tablero;
        if (jugador2Id != null && jugadorId.equals(jugador2Id)) return jugador1Tablero;
        return null;
    }

    public UUID getJugador1Id() {
        return jugador1Id;
    }

    public UUID getJugador2Id() {
        return jugador2Id;
    }

    public UUID getTurnoActual() {
        return turnoActual;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    // Cambiar turno entre jugador1 y jugador2
    public void cambiarTurno() {
        if (jugador2Id == null || turnoActual == null) return; // Evitar NPE
        turnoActual = turnoActual.equals(jugador1Id) ? jugador2Id : jugador1Id;
    }

    // Verifica si es el turno del jugador
    public boolean esTurnoDe(UUID jugadorId) {
        return turnoActual != null && turnoActual.equals(jugadorId);
    }

    // Agregar el segundo jugador con UUID automático
    public void agregarSegundoJugador() {
        if (this.jugador2Id == null) {
            this.jugador2Id = UUID.randomUUID();
            // Asignar turno si aún no había
            if (turnoActual == null) {
                turnoActual = new Random().nextBoolean() ? jugador1Id : jugador2Id;
            }
        }
    }

    // ✅ Setter explícito para el jugador2Id (usado en JuegoService)
    public void setJugador2Id(UUID jugador2Id) {
        this.jugador2Id = jugador2Id;
        if (turnoActual == null) {
            turnoActual = new Random().nextBoolean() ? jugador1Id : jugador2Id;
        }
    }
}
