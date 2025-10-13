package com.batallanaval.ModelTests;

import com.batallanaval.Model.Partida;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {
    @Nested
    class ConstructoresYAccesores {

        @Test
        void constructor_por_defecto() {
            Partida p = new Partida("kkkk");
            assertEquals("kkkk", p.getPartidaId());
            assertNotNull(p.getTableroJugador1());
            assertNotNull(p.getTableroJugador2());
            assertNotSame(p.getTableroJugador1(), p.getTableroJugador2());
            assertNotNull(p.getJugador1Id());
            assertNull(p.getJugador2Id());
            assertNull(p.getTurnoActual());
            assertFalse(p.isJuegoTerminado());
            assertNull(p.getGanador());
            assertFalse(p.getUltimoAtaqueExitoso());
        }

        @Test
        void setters_y_getters() {
            Partida p = new Partida("kkkk");
            p.setTurnoActual(p.getJugador1Id());
            assertSame(p.getJugador1Id(), p.getTurnoActual());
        }
    }

    @Nested
    class Tableros {
        @Test
        void tablero() {
            Partida p = new Partida("kkkk");
            p.setJugador2Id(UUID.randomUUID());
            assertSame(p.getTableroJugador1(), p.getTablero(p.getJugador1Id()));
            assertSame(p.getTableroJugador2(), p.getTablero(p.getJugador2Id()));
        }

        @Test
        void tablero_oponente() {
            Partida p = new Partida("kkkk");
            p.setJugador2Id(UUID.randomUUID());
            assertSame(p.getTableroJugador2(), p.getTableroOponente(p.getJugador1Id()));
            assertSame(p.getTableroJugador1(), p.getTableroOponente(p.getJugador2Id()));
        }
    }
}
