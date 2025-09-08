package com.batallanaval.Controller;

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

    @PostMapping("/nuevo/{tamaño}")
    public UUID nuevoJuego(@PathVariable int tamaño) {
        return juegoService.crearTablero(tamaño);
    }

    @PostMapping("/{id}/shuffle")
    public void shuffle(@PathVariable UUID id) {
        juegoService.shuffle(id);
    }

    @PostMapping("/{id}/confirmar")
    public void confirmar(@PathVariable UUID id) {
        juegoService.confirmar(id);
    }

    @PostMapping("/{id}/atacar")
    public ResultadoAtaque atacar(@PathVariable UUID id,
                                  @RequestParam int fila,
                                  @RequestParam int col) {
        return juegoService.atacar(id, fila, col);
    }

    @GetMapping("/{id}")
    public Tablero getTablero(@PathVariable UUID id) {
        return juegoService.getTablero(id);
    }
}
