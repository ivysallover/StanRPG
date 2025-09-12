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
        // Lógica de confirmación de barcos
    }

    public void shuffle(String codigo, UUID jugadorId) {
        Partida partida = partidas.get(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");
        Tablero tablero = partida.getTablero(jugadorId);
        if (tablero != null) tablero.shuffle();
    }

    // ✅ CORRECCIÓN: Este método ahora retorna un Map con el resultado y el objeto Partida.
    public Map<String, Object> atacar(String codigo, UUID jugadorId, int fila, int col) {
        Partida partida = getPartida(codigo);
        if (partida == null) throw new IllegalArgumentException("Partida no encontrada.");

        // Validaciones previas al ataque
        if (partida.isJuegoTerminado()) {
            return Map.of("resultado", ResultadoAtaque.Gameover, "partida", partida);
        }
        if (!partida.esTurnoDe(jugadorId)) {
            return Map.of("resultado", ResultadoAtaque.NO_ES_TU_TURNO, "partida", partida);
        }

        Tablero oponenteTablero = partida.getTableroOponente(jugadorId);
        if (oponenteTablero == null) {
            return Map.of("resultado", ResultadoAtaque.ERROR_TABLERO, "partida", partida);
        }

        ResultadoAtaque resultado = oponenteTablero.disparar(fila, col);

        // ✅ Lógica de cambio de turno: solo cambia si el ataque no fue una celda ya disparada
        if (resultado != ResultadoAtaque.YA_ATACADA) {
            partida.cambiarTurno();
        }

        // Lógica para determinar si el juego ha terminado
        if (oponenteTablero.todosLasBarcosHundidos()) {
            partida.setJuegoTerminado(true);
            partida.setGanador(jugadorId.toString());
        }

        return Map.of("resultado", resultado, "partida", partida);
    }
}