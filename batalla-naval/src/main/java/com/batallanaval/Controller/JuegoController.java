package com.batallanaval.Controller;

import com.batallanaval.Model.Partida;
import com.batallanaval.Model.ResultadoAtaque;
import com.batallanaval.Model.Tablero;
import com.batallanaval.Service.JuegoService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/juego")
@CrossOrigin(origins = "*")
public class JuegoController {
    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping("/nuevo")
    public Partida crearPartida() {
        return juegoService.crearNuevaPartida();
    }

    @PostMapping("/{partidaId}/unirse")
    public String unirseAPartida(@PathVariable UUID partidaId) {
        Partida partida = juegoService.getPartida(partidaId);
        if (partida != null && partida.getJugador2Id() == null) {
            UUID nuevoJugador2 = UUID.randomUUID();
            partida.setJugador2Id(nuevoJugador2);
            return nuevoJugador2.toString();
        }
        return "ERROR_NO_PUEDE_UNIRSE";
    }

    @PostMapping("/{partidaId}/atacar")
    public ResultadoAtaque atacar(@PathVariable UUID partidaId,
                                  @RequestParam UUID atacanteId,
                                  @RequestParam int fila,
                                  @RequestParam int col) {
        return juegoService.atacar(partidaId, atacanteId, fila, col);
    }

    @PostMapping("/{partidaId}/shuffle")
    public void shuffle(@PathVariable UUID partidaId, @RequestParam UUID jugadorId) {
        juegoService.shuffle(partidaId, jugadorId);
    }

    @PostMapping("/{partidaId}/confirmar")
    public void confirmar(@PathVariable UUID partidaId, @RequestParam UUID jugadorId) {
        juegoService.confirmar(partidaId, jugadorId);
    }

    @GetMapping("/{partidaId}/tablero-propio")
    public Tablero getTableroPropio(@PathVariable UUID partidaId, @RequestParam UUID jugadorId) {
        return juegoService.getTableroPropio(partidaId, jugadorId);
    }

    @GetMapping("/{partidaId}/tablero-oponente")
    public Tablero getTableroOponente(@PathVariable UUID partidaId, @RequestParam UUID jugadorId) {
        return juegoService.getTableroOponente(partidaId, jugadorId);
    }

    @GetMapping("/{partidaId}/estado")
    public Partida getEstadoPartida(@PathVariable UUID partidaId) {
        return juegoService.getPartida(partidaId);
    }
}
