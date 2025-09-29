package com.batallanaval.ModelTests;

import com.batallanaval.Model.Partida;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}
