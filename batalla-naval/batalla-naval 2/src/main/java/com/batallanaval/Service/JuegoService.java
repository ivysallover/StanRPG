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

    public void confirmar(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");

        Tablero tablero = partida.getTablero(jugadorId);
        if (tablero == null) throw new IllegalArgumentException("Jugador no encontrado.");

        if (tablero.isConfirmado()) return;

        tablero.setConfirmado(true);

        if (ambosJugadoresConfirmados(partida)) {
            iniciarJuego(partida);
        }
    }

    private boolean ambosJugadoresConfirmados(Partida partida) {
        if (partida.getJugador2Id() == null) return false;

        Tablero tablero1 = partida.getTablero(partida.getJugador1Id());
        Tablero tablero2 = partida.getTablero(partida.getJugador2Id());

        return tablero1 != null && tablero1.isConfirmado() &&
                tablero2 != null && tablero2.isConfirmado();
    }

    private void iniciarJuego(Partida partida) {
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

        if (!ambosJugadoresConfirmados(partida)) {
            return ResultadoAtaque.JUEGO_NO_INICIADO;
        }

        return partida.atacar(jugadorId, fila, col);
    }
}