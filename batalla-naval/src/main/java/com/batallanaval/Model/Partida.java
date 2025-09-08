package com.batallanaval.Model;

import java.util.UUID;
import java.util.Random;

public class Partida {
    private final UUID partidaId;
    private final Tablero jugador1Tablero;
    private final Tablero jugador2Tablero;
    private final UUID jugador1Id;
    private UUID jugador2Id; // 👈 ya no es final
    private UUID turnoActual;
    private boolean juegoTerminado;
    private String ganador;

    public Partida(UUID partidaId) {
        this.partidaId = partidaId;
        this.jugador1Tablero = new Tablero();
        this.jugador2Tablero = new Tablero();
        this.jugador1Id = UUID.randomUUID();
        this.jugador2Id = null; // 👈 al principio NO existe el jugador2
        this.turnoActual = new Random().nextBoolean() ? this.jugador1Id : null;
        this.juegoTerminado = false;
        this.ganador = null;
    }

    public UUID getPartidaId() {
        return partidaId;
    }

    public Tablero getTablero(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) {
            return jugador1Tablero;
        } else if (jugador2Id != null && jugadorId.equals(jugador2Id)) {
            return jugador2Tablero;
        }
        return null;
    }

    public Tablero getTableroOponente(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) {
            return jugador2Tablero;
        } else if (jugador2Id != null && jugadorId.equals(jugador2Id)) {
            return jugador1Tablero;
        }
        return null;
    }

    public void cambiarTurno() {
        if (jugador2Id == null) return; // 👈 aún no hay jugador2
        turnoActual = (turnoActual.equals(jugador1Id)) ? jugador2Id : jugador1Id;
    }

    public boolean esTurnoDe(UUID jugadorId) {
        return turnoActual != null && turnoActual.equals(jugadorId);
    }

    public UUID getJugador1Id() {
        return jugador1Id;
    }

    public UUID getJugador2Id() {
        return jugador2Id;
    }

    public void setJugador2Id(UUID jugador2Id) {
        this.jugador2Id = jugador2Id;
        // si no había turno asignado aún, lo asigno ahora
        if (turnoActual == null) {
            turnoActual = new Random().nextBoolean() ? jugador1Id : jugador2Id;
        }
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
}
