package com.batallanaval.ModelTests;

import com.batallanaval.Model.Celda;
import com.batallanaval.Model.EstadoCelda;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class CeldaTest {

    @Nested
    class ConstructoresYAccesores {
        @Test
        void constructorPorDefecto_inicializaAguaYBarcoIdMenosUno() {
            Celda c = new Celda();
            Assertions.assertEquals(EstadoCelda.AGUA, c.getEstado());
            assertEquals(-1, c.getBarcoId());
        }

        @Test
        void constructorConEstado_respetaEstadoYBarcoIdMenosUno() {
            Celda c = new Celda(EstadoCelda.BARCO);
            assertEquals(EstadoCelda.BARCO, c.getEstado());
            assertEquals(-1, c.getBarcoId());
        }

        @Test
        void gettersSetters_funcionan() {
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
        void false_siNoTieneIdDeBarco() {
            Celda c = new Celda(EstadoCelda.BARCO);
            c.setBarcoId(-1);
            assertFalse(c.tieneBarco());
        }

        @Test
        void false_siEstadoNoEsDeBarco_aunqueTengaId() {
            Celda c = new Celda(EstadoCelda.AGUA);
            c.setBarcoId(3);
            assertFalse(c.tieneBarco());
        }

        @Test
        void true_enBARCO_conId() {
            Celda c = new Celda(EstadoCelda.BARCO);
            c.setBarcoId(5);
            assertTrue(c.tieneBarco());
        }

        @Test
        void true_enIMPACTO_conId() {
            Celda c = new Celda(EstadoCelda.IMPACTO);
            c.setBarcoId(1);
            assertTrue(c.tieneBarco());
        }

        @Test
        void true_enHUNDIDO_conId() {
            Celda c = new Celda(EstadoCelda.HUNDIDO);
            c.setBarcoId(2);
            assertTrue(c.tieneBarco());
        }
    }

    @Nested
    class FueAtacada {
        @ParameterizedTest
        @EnumSource(value = EstadoCelda.class, names = {"AGUA_ATACADA", "IMPACTO", "HUNDIDO"})
        void true_enEstadosAtacados(EstadoCelda estado) {
            Celda c = new Celda(estado);
            assertTrue(c.fueAtacada());
        }

        @ParameterizedTest
        @EnumSource(value = EstadoCelda.class, names = {"AGUA", "BARCO"})
        void false_enEstadosNoAtacados(EstadoCelda estado) {
            Celda c = new Celda(estado);
            assertFalse(c.fueAtacada());
        }
    }

    @Test
    void toString_incluyeEstadoYBarcoId() {
        Celda c = new Celda(EstadoCelda.BARCO);
        c.setBarcoId(9);
        String s = c.toString();
        assertNotNull(s);
        assertTrue(s.contains("estado="));
        assertTrue(s.contains("barcoId="));
    }
}

