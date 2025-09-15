package com.batallanaval.Service;

import com.batallanaval.Model.Partida;
import com.batallanaval.Model.ResultadoAtaque;
import com.batallanaval.Model.Tablero;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JuegoService {

    private final Map<String, Partida> partidas = new HashMap<>();

    public Partida crearNuevaPartida(String codigo) {
        if (partidas.containsKey(codigo)) {
            throw new IllegalArgumentException("El código de partida ya existe.");
        }
        Partida partida = new Partida(codigo);
        partidas.put(codigo, partida);
        return partida;
    }

    public Partida getPartida(String codigo) {
        return partidas.get(codigo);
    }

    public UUID agregarSegundoJugador(String codigo) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");
        if (partida.getJugador2Id() != null) throw new IllegalStateException("La partida ya tiene dos jugadores.");
        UUID jugador2Id = UUID.randomUUID();
        partida.setJugador2Id(jugador2Id);
        return jugador2Id;
    }

    public Tablero getTableroPropio(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");
        return partida.getTablero(jugadorId);
    }

    public Tablero getTableroOponente(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");
        return partida.getTableroOponente(jugadorId);
    }

    // CORREGIDO: Implementar correctamente el método confirmar
    public void confirmar(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");

        Tablero tablero = partida.getTablero(jugadorId);
        if (tablero == null) throw new IllegalArgumentException("Jugador no encontrado.");

        // Si ya está confirmado, no hacer nada
        if (tablero.isConfirmado()) return;

        // Confirmar el tablero
        tablero.setConfirmado(true);

        // Verificar si ambos jugadores han confirmado y iniciar el juego
        if (ambosJugadoresConfirmados(partida)) {
            iniciarJuego(partida);
        }
    }

    // NUEVO: Método para verificar si ambos jugadores confirmaron
    private boolean ambosJugadoresConfirmados(Partida partida) {
        if (partida.getJugador2Id() == null) return false;

        Tablero tablero1 = partida.getTablero(partida.getJugador1Id());
        Tablero tablero2 = partida.getTablero(partida.getJugador2Id());

        return tablero1 != null && tablero1.isConfirmado() &&
                tablero2 != null && tablero2.isConfirmado();
    }

    // NUEVO: Iniciar el juego cuando ambos confirmen
    private void iniciarJuego(Partida partida) {
        // Si no hay turno asignado, asignar uno aleatorio
        if (partida.getTurnoActual() == null) {
            UUID turnoInicial = Math.random() < 0.5 ?
                    partida.getJugador1Id() : partida.getJugador2Id();
            partida.setTurnoActual(turnoInicial);
        }
    }

    public void shuffle(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");

        Tablero tablero = partida.getTablero(jugadorId);
        if (tablero != null && !tablero.isConfirmado()) {
            tablero.shuffle();
        }
    }

    public ResultadoAtaque atacar(String codigo, UUID jugadorId, int fila, int col) {
        Partida partida = getPartida(codigo);
        if (partida == null) {
            throw new IllegalArgumentException("Partida no encontrada.");
        }

        // NUEVO: Verificar que ambos jugadores hayan confirmado
        if (!ambosJugadoresConfirmados(partida)) {
            return ResultadoAtaque.JUEGO_NO_INICIADO;
        }

        if (!partida.esTurnoDe(jugadorId)) {
            return ResultadoAtaque.NO_ES_TU_TURNO;
        }

        Tablero oponenteTablero = partida.getTableroOponente(jugadorId);
        if (oponenteTablero == null) {
            return ResultadoAtaque.ERROR_TABLERO;
        }

        String resultadoDisparo = oponenteTablero.disparar(fila, col);
        ResultadoAtaque resultado = ResultadoAtaque.valueOf(resultadoDisparo);

        // Determinar si el ataque fue exitoso (impacto o hundido)
        boolean ataqueExitoso = (resultado == ResultadoAtaque.IMPACTO || resultado == ResultadoAtaque.HUNDIDO);
        partida.setUltimoAtaqueExitoso(ataqueExitoso);

        // Cambiar turno solo si no fue exitoso o ya fue atacada
        if (resultado != ResultadoAtaque.YA_ATACADA) {
            partida.cambiarTurno();
        }

        // Verificar si el juego terminó
        if (oponenteTablero.todosLasBarcosHundidos()) {
            partida.setJuegoTerminado(true);
            partida.setGanador(jugadorId.toString());
        }

        return resultado;
    }
}