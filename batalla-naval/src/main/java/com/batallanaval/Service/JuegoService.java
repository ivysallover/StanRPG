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
    }

    public void shuffle(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");
        Tablero tablero = partida.getTablero(jugadorId);
        if (tablero != null) tablero.shuffle();
    }

    public ResultadoAtaque atacar(String codigo, UUID jugadorId, int fila, int col) {
        Partida partida = getPartida(codigo);
        if (partida == null) {
            throw new IllegalArgumentException("Partida no encontrada.");
        }

        if (!partida.esTurnoDe(jugadorId)) {
            return ResultadoAtaque.NO_ES_TU_TURNO;
        }

        Tablero oponenteTablero = partida.getTableroOponente(jugadorId);
        if (oponenteTablero == null) {
            return ResultadoAtaque.ERROR_TABLERO;
        }

        ResultadoAtaque resultado = ResultadoAtaque.valueOf(oponenteTablero.disparar(fila, col));

        if (resultado != ResultadoAtaque.YA_ATACADA) {
            partida.cambiarTurno();
        }

        if (oponenteTablero.todosLasBarcosHundidos()) {
            partida.setJuegoTerminado(true);
            partida.setGanador(jugadorId.toString());
        }

        return resultado;
    }
}