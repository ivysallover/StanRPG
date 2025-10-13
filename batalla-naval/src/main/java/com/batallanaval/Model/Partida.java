package com.batallanaval.Model;

import java.util.UUID;
import java.util.Random;

public class Partida {

    private final String partidaId;
    private final Tablero jugador1Tablero;
    private final Tablero jugador2Tablero;
    private final UUID jugador1Id;
    private UUID jugador2Id;
    private UUID turnoActual;
    private boolean juegoTerminado;
    private String ganador;
    private boolean ultimoAtaqueExitoso; // Nuevo campo para la lógica del turno repetido

    public Partida(String partidaId) {
        this.partidaId = partidaId;          // código ingresado por el usuario
        this.jugador1Tablero = new Tablero();
        this.jugador2Tablero = new Tablero();
        this.jugador1Id = UUID.randomUUID();
        this.jugador2Id = null;
        this.turnoActual = null;
        this.juegoTerminado = false;
        this.ganador = null;
        this.ultimoAtaqueExitoso = false; // Inicializar en false
    }

    public ResultadoAtaque atacar(UUID jugadorId, int fila, int col) {
        if (!esTurnoDe(jugadorId)) {
            return ResultadoAtaque.NO_ES_TU_TURNO;
        }

        Tablero oponenteTablero = getTableroOponente(jugadorId);
        if (oponenteTablero == null) {
            return ResultadoAtaque.ERROR_TABLERO;
        }

        String resultadoDisparo = oponenteTablero.disparar(fila, col);
        ResultadoAtaque resultado = ResultadoAtaque.valueOf(resultadoDisparo);

        boolean ataqueExitoso = (resultado == ResultadoAtaque.IMPACTO || resultado == ResultadoAtaque.HUNDIDO);
        this.setUltimoAtaqueExitoso(ataqueExitoso);

        if (resultado != ResultadoAtaque.YA_ATACADA) {
            this.cambiarTurno();
        }

        if (oponenteTablero.todosLasBarcosHundidos()) {
            this.setJuegoTerminado(true);
            this.setGanador(jugadorId.toString());
        }

        return resultado;
    }

    public void cambiarTurno() {
        if (ultimoAtaqueExitoso) {
            this.ultimoAtaqueExitoso = false;
            return;
        }
        if (jugador2Id == null || turnoActual == null) return;
        turnoActual = turnoActual.equals(jugador1Id) ? jugador2Id : jugador1Id;
    }

    public boolean esTurnoDe(UUID jugadorId) {
        return turnoActual != null && turnoActual.equals(jugadorId);
    }

    public String getPartidaId() {
        return partidaId;
    }

    public Tablero getTablero(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) return jugador1Tablero;
        if (jugadorId.equals(jugador2Id)) return jugador2Tablero;
        return null;
    }

    public Tablero getTableroOponente(UUID jugadorId) {
        if (jugadorId.equals(jugador1Id)) return jugador2Tablero;
        if (jugadorId.equals(jugador2Id)) return jugador1Tablero;
        return null;
    }

    public Tablero getTableroJugador1() {
        return jugador1Tablero;
    }

    public Tablero getTableroJugador2() {
        return jugador2Tablero;
    }

    public UUID getJugador1Id() {
        return jugador1Id;
    }

    public UUID getJugador2Id() {
        return jugador2Id;
    }

    public void setJugador2Id(UUID jugador2Id) {
        this.jugador2Id = jugador2Id;
        if (turnoActual == null) {
            turnoActual = new Random().nextBoolean() ? jugador1Id : jugador2Id;

        }
    }
    public UUID getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(UUID turnoActual) {
        this.turnoActual = turnoActual;
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

    public boolean getUltimoAtaqueExitoso() {
        return ultimoAtaqueExitoso;
    }

    public void setUltimoAtaqueExitoso(boolean ultimoAtaqueExitoso) {
        this.ultimoAtaqueExitoso = ultimoAtaqueExitoso;
    }
}}