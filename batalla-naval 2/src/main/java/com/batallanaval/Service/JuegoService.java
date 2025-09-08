package com.batallanaval.Service;

import com.batallanaval.Model.ResultadoAtaque;
import com.batallanaval.Model.Tablero;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JuegoService {
    private final Map<UUID, Tablero> tableros = new HashMap<>();

    public UUID crearTablero(int tamaño) {
        Tablero tablero = new Tablero(tamaño);
        tablero.shuffle();
        UUID id = UUID.randomUUID();
        tableros.put(id, tablero);
        return id;
    }

    public Tablero getTablero(UUID id) {
        return tableros.get(id);
    }

    public void shuffle(UUID id) {
        Tablero tablero = tableros.get(id);
        if (tablero != null && !tablero.isConfirmado()) {
            tablero.shuffle();
        }
    }

    public void confirmar(UUID id) {
        Tablero tablero = tableros.get(id);
        if (tablero != null) {
            tablero.setConfirmado(true);
        }
    }

    public ResultadoAtaque atacar(UUID id, int fila, int col) {
        Tablero tablero = tableros.get(id);
        if (tablero != null) {
            String res = tablero.disparar(fila, col);
            return new ResultadoAtaque(res);
        }
        return null;
    }
}