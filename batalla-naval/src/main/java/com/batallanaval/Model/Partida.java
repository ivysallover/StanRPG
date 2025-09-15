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
    private boolean ultimoAtaqueExitoso; // Nuevo campo para la lógica del turno repetido

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
        this.ultimoAtaqueExitoso = false; // Inicializar en false
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

    // NUEVO: Método para obtener tablero por número de jugador (para el controller)
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

    public UUID getTurnoActual() {
        return turnoActual;
    }

    // NUEVO: Setter para el turno actual
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

    // Nuevo setter para indicar si el último ataque fue exitoso
    public void setUltimoAtaqueExitoso(boolean ultimoAtaqueExitoso) {
        this.ultimoAtaqueExitoso = ultimoAtaqueExitoso;
    }

    // Cambiar turno entre jugador1 y jugador2
    public void cambiarTurno() {
        // El turno solo cambia si el último ataque no fue un impacto o hundimiento
        if (ultimoAtaqueExitoso) {
            this.ultimoAtaqueExitoso = false; // Reiniciar el estado para el próximo turno
            return; // No cambia el turno, el mismo jugador ataca de nuevo
        }
        if (jugador2Id == null || turnoActual == null) return; // Evitar errores si no hay 2 jugadores
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

    // Setter explícito para el jugador2Id (usado en JuegoService)
    public void setJugador2Id(UUID jugador2Id) {
        this.jugador2Id = jugador2Id;
        if (turnoActual == null) {
            turnoActual = new Random().nextBoolean() ? jugador1Id : jugador2Id;
        }
    }
}