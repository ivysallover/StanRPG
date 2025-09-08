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
    private final Map<UUID, Partida> partidas = new HashMap<>();

    public Partida crearNuevaPartida() {
        UUID partidaId = UUID.randomUUID();
        Partida nuevaPartida = new Partida(partidaId);
        partidas.put(partidaId, nuevaPartida);
        return nuevaPartida;
    }

    public Partida getPartida(UUID partidaId) {
        return partidas.get(partidaId);
    }

    public ResultadoAtaque atacar(UUID partidaId, UUID atacanteId, int fila, int col) {
        Partida partida = partidas.get(partidaId);

        if (partida == null) {
            return new ResultadoAtaque("PARTIDA_NO_ENCONTRADA");
        }

        if (partida.isJuegoTerminado()) {
            return new ResultadoAtaque("JUEGO_TERMINADO");
        }

        if (!partida.esTurnoDe(atacanteId)) {
            return new ResultadoAtaque("NO_ES_TU_TURNO");
        }

        Tablero tableroOponente = partida.getTableroOponente(atacanteId);
        if (tableroOponente == null) {
            return new ResultadoAtaque("OPONENTE_NO_EXISTE");
        }

        String resultado = tableroOponente.disparar(fila, col);

        // Solo cambia el turno si el ataque fue válido
        if (!resultado.equals("YA_ATACADA") && !resultado.equals("FUERA_DE_RANGO")) {
            // Verificar si el juego ha terminado
            if (tableroOponente.todosLasBarcosHundidos()) {
                partida.setJuegoTerminado(true);
                partida.setGanador(atacanteId.toString());
            } else {
                partida.cambiarTurno();
            }
        }

        return new ResultadoAtaque(resultado);
    }

    public void shuffle(UUID partidaId, UUID jugadorId) {
        Partida partida = partidas.get(partidaId);
        if (partida != null) {
            Tablero tablero = partida.getTablero(jugadorId);
            if (tablero != null) {
                tablero.shuffle();
            }
        }
    }

    public void confirmar(UUID partidaId, UUID jugadorId) {
        Partida partida = partidas.get(partidaId);
        if (partida != null) {
            Tablero tablero = partida.getTablero(jugadorId);
            if (tablero != null) {
                tablero.setConfirmado(true);
            }

            // 👇 Solo empieza el juego cuando AMBOS jugadores confirmaron
            Tablero t1 = partida.getTablero(partida.getJugador1Id());
            Tablero t2 = partida.getTablero(partida.getJugador2Id());

            if (t1 != null && t2 != null && t1.isConfirmado() && t2.isConfirmado()) {
                // Si todavía no hay turno asignado, lo define aleatoriamente en Partida
                if (partida.getTurnoActual() == null) {
                    partida.cambiarTurno();
                }
            }
        }
    }

    public Tablero getTableroPropio(UUID partidaId, UUID jugadorId) {
        Partida partida = partidas.get(partidaId);
        if (partida == null) return null;
        return partida.getTablero(jugadorId);
    }

    public Tablero getTableroOponente(UUID partidaId, UUID jugadorId) {
        Partida partida = partidas.get(partidaId);
        if (partida == null) return null;
        return partida.getTableroOponente(jugadorId);
    }
}
