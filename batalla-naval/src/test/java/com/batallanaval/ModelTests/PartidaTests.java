package com.batallanaval.ModelTests;

import com.batallanaval.Model.Partida;
import com.batallanaval.Model.ResultadoAtaque;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.batallanaval.Model.Tablero;
import com.batallanaval.Model.Celda;
import com.batallanaval.Model.EstadoCelda;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {
    @Nested
    class Constructor {

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
    }

    @Nested
    class Tableros {
        @Test
        void test_get_tablero() {
            Partida p = new Partida("kkkk");
            p.setJugador2Id(UUID.randomUUID());
            assertSame(p.getTableroJugador1(), p.getTablero(p.getJugador1Id()));
            assertSame(p.getTableroJugador2(), p.getTablero(p.getJugador2Id()));
        }

        @Test
        void test_get_tablero_oponente() {
            Partida p = new Partida("kkkk");
            p.setJugador2Id(UUID.randomUUID());
            assertSame(p.getTableroJugador2(), p.getTableroOponente(p.getJugador1Id()));
            assertSame(p.getTableroJugador1(), p.getTableroOponente(p.getJugador2Id()));
        }
    }

    @Nested
    class Turno {
        @Test
        void turno_actual() {
            Partida p = new Partida("kkkk");
            p.setJugador2Id(UUID.randomUUID());
            p.setTurnoActual(p.getJugador1Id());
            assertSame(p.getJugador1Id(), p.getTurnoActual());
            p.setTurnoActual(p.getJugador2Id());
            assertSame(p.getJugador2Id(), p.getTurnoActual());
        }

        @Test
        void es_turno_de() {
            Partida q = new Partida("kkk");
            q.setJugador2Id(UUID.randomUUID());
            q.setTurnoActual(q.getJugador2Id());
            assertFalse(q.esTurnoDe(q.getJugador1Id()));
        }
    }

    @Nested
    class Ataque_y_Cambio_de_Turno {

        private static int[] buscar_barco_cualquiera(Tablero t) {
            for (int f = 0; f < t.getTamano(); f++) {
                for (int c = 0; c < t.getTamano(); c++) {
                    Celda cel = t.getCelda(f, c);
                    if (cel.getEstado() == EstadoCelda.BARCO) return new int[]{f, c};
                }
            }
            fail("No se encontró ninguna celda con BARCO");
            return null;
        }

        private int[] buscar_agua_cualquiera(Tablero t) {
            for (int f = 0; f < t.getTamano(); f++) {
                for (int c = 0; c < t.getTamano(); c++) {
                    Celda cel = t.getCelda(f, c);
                    if (cel.getEstado() == EstadoCelda.AGUA) return new int[]{f, c};
                }
            }
            fail("No se encontró ninguna celda con AGUA");
            return null;
        }

        private java.util.List<int[]> barco_completo(Tablero t, int barcoId) {
            java.util.List<int[]> res = new java.util.ArrayList<>();
            for (int f = 0; f < t.getTamano(); f++) {
                for (int c = 0; c < t.getTamano(); c++) {
                    Celda cel = t.getCelda(f, c);
                    if (cel.getBarcoId() == barcoId) res.add(new int[]{f, c});
                }
            }
            return res;
        }

        @Test
        void atacar_turno_incorrecto(){
            Partida p = new Partida("p");
            p.setJugador2Id(UUID.randomUUID());
            assertNotNull(p.getTableroJugador2());
            p.setTurnoActual(p.getJugador1Id());
            assertSame(ResultadoAtaque.NO_ES_TU_TURNO, p.atacar(p.getJugador2Id(), 2, 3));
        }

        @Test
        void atacar_error_tablero(){
            Partida p = new Partida("p") {
                @Override
                public Tablero getTableroOponente(UUID jugadorId) {
                    return null;
                }
            };
            p.setTurnoActual(p.getJugador1Id());
            assertSame(ResultadoAtaque.ERROR_TABLERO, p.atacar(p.getJugador1Id(), 2, 3));
        }

        @Test
        void atacar_impacto(){
            Partida p = new Partida("p");
            p.setTurnoActual(p.getJugador1Id());
            Tablero tablero1 = p.getTableroJugador2();
            int[] pos_barco = this.buscar_barco_cualquiera(tablero1);
            assertSame(ResultadoAtaque.IMPACTO, p.atacar(p.getJugador1Id(), pos_barco[0], pos_barco[1]));
            assertSame(p.getJugador1Id(), p.getTurnoActual());
        }

        @Test
        void atacar_agua(){
            Partida p = new Partida("p");
            p.setJugador2Id(UUID.randomUUID());
            p.setTurnoActual(p.getJugador1Id());
            Tablero tablero2 = p.getTableroJugador2();
            int[] pos_agua = this.buscar_agua_cualquiera(tablero2);
            assertSame(ResultadoAtaque.AGUA, p.atacar(p.getJugador1Id(), pos_agua[0], pos_agua[1]));
            assertSame(p.getJugador2Id(), p.getTurnoActual());
        }

        @Test
        void atacar_hundido(){
            Partida p = new Partida("p");
            p.setTurnoActual(p.getJugador1Id());
            Tablero tablero2 = p.getTableroJugador2();
            int[] pos_barco = this.buscar_barco_cualquiera(tablero2);
            int idBarco = tablero2.getCelda(pos_barco[0], pos_barco[1]).getBarcoId();
            java.util.List<int[]> coordenadas = this.barco_completo(tablero2, idBarco);

            ResultadoAtaque ultimo = null;
            for (int[] xy : coordenadas) {
                ultimo = p.atacar(p.getJugador1Id(), xy[0], xy[1]);
            }
            assertSame(ResultadoAtaque.HUNDIDO, ultimo);
            assertSame(p.getJugador1Id(), p.getTurnoActual());
        }

        @Test
        void atacar_ya_atacada(){
            Partida p = new Partida("p");
            p.setTurnoActual(p.getJugador1Id());
            p.atacar(p.getJugador1Id(), 0, 1);
            assertSame(ResultadoAtaque.YA_ATACADA, p.atacar(p.getJugador1Id(), 0, 1));
            assertSame(p.getJugador1Id(), p.getTurnoActual());
        }
    }
}
