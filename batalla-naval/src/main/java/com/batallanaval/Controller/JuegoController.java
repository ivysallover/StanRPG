package com.batallanaval.Controller;

import com.batallanaval.Model.Partida;
import com.batallanaval.Model.ResultadoAtaque;
import com.batallanaval.Model.Tablero;
import com.batallanaval.Service.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/juego")
@CrossOrigin(origins = "*")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @PostMapping("/nuevo")
    public Partida crearNuevaPartida(@RequestParam String codigo) {
        return juegoService.crearNuevaPartida(codigo);
    }

    @PostMapping("/{codigo}/unirse")
    public ResponseEntity<String> unirsePartida(@PathVariable String codigo) {
        try {
            UUID jugador2Id = juegoService.agregarSegundoJugador(codigo);
            return ResponseEntity.ok(jugador2Id.toString());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{codigo}/atacar")
    public Map<String, Object> atacar(
            @PathVariable String codigo,
            @RequestParam UUID jugadorId,
            @RequestParam int fila,
            @RequestParam int col) {
        ResultadoAtaque resultado = juegoService.atacar(codigo, jugadorId, fila, col);
        Partida partida = juegoService.getPartida(codigo);
        return Map.of("resultado", resultado, "partida", partida);
    }

    @GetMapping("/{codigo}/tablero")
    public ResponseEntity<?> getTableros(@PathVariable String codigo, @RequestParam UUID jugadorId) {
        try {
            Tablero tableroPropio = juegoService.getTableroPropio(codigo, jugadorId);
            Tablero tableroOponente = juegoService.getTableroOponente(codigo, jugadorId);

            Map<String, Tablero> tableros = Map.of(
                    "jugador", tableroPropio,
                    "enemigo", tableroOponente
            );
            return ResponseEntity.ok(tableros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{codigo}/confirmar")
    public ResponseEntity<Void> confirmar(
            @PathVariable String codigo,
            @RequestParam UUID jugadorId) {
        try {
            juegoService.confirmar(codigo, jugadorId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{codigo}/shuffle")
    public ResponseEntity<Void> shuffle(
            @PathVariable String codigo,
            @RequestParam UUID jugadorId) {
        try {
            juegoService.shuffle(codigo, jugadorId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> getEstado(@PathVariable String codigo, @RequestParam UUID jugadorId) {
        try {
            Partida partida = juegoService.getPartida(codigo);
            if (partida == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida no encontrada.");
            }

            boolean soyJugador1 = partida.getJugador1Id().equals(jugadorId);
            Tablero miTablero = soyJugador1 ? partida.getTableroJugador1() : partida.getTableroJugador2();
            Tablero tableroEnemigo = soyJugador1 ? partida.getTableroJugador2() : partida.getTableroJugador1();

            boolean miConfirmacion = miTablero != null && miTablero.isConfirmado();
            boolean confirmacionEnemiga = tableroEnemigo != null && tableroEnemigo.isConfirmado();
            boolean ambosConfirmados = miConfirmacion && confirmacionEnemiga;

            String mensaje;
            if (partida.isJuegoTerminado()) {
                if (partida.getGanador().equals(jugadorId.toString())) {
                    mensaje = "🏆 ¡Ganaste!";
                } else {
                    mensaje = "😢 ¡Perdiste!";
                }
            } else if (partida.getJugador2Id() == null) {
                mensaje = "Juego creado. Esperando al segundo jugador...";
            } else if (!ambosConfirmados) {
                if (!miConfirmacion && !confirmacionEnemiga) {
                    mensaje = "⏳ Ambos jugadores deben confirmar sus tableros para comenzar.";
                } else if (!miConfirmacion) {
                    mensaje = "⏳ Esperando que confirmes tu tablero.";
                } else {
                    mensaje = "⏳ Esperando que el oponente confirme su tablero.";
                }
            } else if (partida.esTurnoDe(jugadorId)) {
                mensaje = "¡Es tu turno! Elige una celda para atacar.";
            } else {
                mensaje = "Esperando el turno del oponente...";
            }

            Map<String, Object> estado = Map.of(
                    "partida", partida,
                    "mensaje", mensaje,
                    "ambosConfirmados", ambosConfirmados,
                    "miConfirmacion", miConfirmacion,
                    "confirmacionEnemiga", confirmacionEnemiga
            );
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener estado.");
        }
    }
}