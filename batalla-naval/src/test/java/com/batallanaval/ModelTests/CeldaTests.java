package com.batallanaval.ModelTests;

import com.batallanaval.Model.Celda;
import com.batallanaval.Model.EstadoCelda;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CeldaTest {

    @Nested
    class ConstructoresYAccesores {
        @Test
        void constructor1() {
            Celda c = new Celda();
            Assertions.assertEquals(EstadoCelda.AGUA, c.getEstado());
            assertEquals(-1, c.getBarcoId());
        }

        @Test
        void constructor2() {
            Celda c = new Celda(EstadoCelda.BARCO);
            assertEquals(EstadoCelda.BARCO, c.getEstado());
            assertEquals(-1, c.getBarcoId());
        }

        @Test
        void gettersSetters() {
            Celda c = new Celda();
            c.setEstado(EstadoCelda.BARCO);
            c.setBarcoId(7);
            assertEquals(EstadoCelda.BARCO, c.getEstado());
            assertEquals(7, c.getBarcoId());
        }
    }

    @Nested
    class TieneBarco {
        @Test
        void no_id() {
            Celda c = new Celda(EstadoCelda.BARCO);
            c.setBarcoId(-1);
            assertFalse(c.tieneBarco());
        }

        @Test
        void no_estado() {
            Celda c = new Celda(EstadoCelda.AGUA);
            c.setBarcoId(3);
            assertFalse(c.tieneBarco());
        }

        @Test
        void si_barco() {
            Celda c = new Celda(EstadoCelda.BARCO);
            c.setBarcoId(5);
            assertTrue(c.tieneBarco());
        }

        @Test
        void si_impacto() {
            Celda c = new Celda(EstadoCelda.IMPACTO);
            c.setBarcoId(1);
            assertTrue(c.tieneBarco());
        }

        @Test
        void si_hundido() {
            Celda c = new Celda(EstadoCelda.HUNDIDO);
            c.setBarcoId(2);
            assertTrue(c.tieneBarco());
        }
    }

    @Nested
    class FueAtacada {

        @Test
        void si_atacado() {
            Celda c1 = new Celda(EstadoCelda.AGUA_ATACADA);
            assertTrue(c1.fueAtacada());
            Celda c2 = new Celda(EstadoCelda.IMPACTO);
            assertTrue(c2.fueAtacada());
            Celda c3 = new Celda(EstadoCelda.HUNDIDO);
            assertTrue(c3.fueAtacada());
        }

        @Test
        void no_atacado() {
            Celda c1 = new Celda(EstadoCelda.AGUA);
            assertFalse(c1.fueAtacada());
            Celda c2 = new Celda(EstadoCelda.BARCO);
            assertFalse(c2.fueAtacada());
        }
    }
}

