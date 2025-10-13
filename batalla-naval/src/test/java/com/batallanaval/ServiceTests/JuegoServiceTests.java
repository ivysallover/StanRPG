package com.batallanaval.ServiceTests;

import com.batallanaval.Model.Partida;
import com.batallanaval.Model.ResultadoAtaque;
import com.batallanaval.Service.JuegoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class JuegoServiceTests {

    @Nested
    class CrearPartida {

        @Test
        void crearNuevaPartida_ok() {
            JuegoService s = new JuegoService();
            Partida p = s.crearNuevaPartida("p");
            assertNotNull(p);
            assertSame(p, s.getPartida("p"));
        }

        @Test
        void crearNuevaPartida_yaexistecodigo() {
            JuegoService s = new JuegoService();
            s.crearNuevaPartida("p");
            assertThrows(IllegalArgumentException.class, () -> s.crearNuevaPartida("p"));
        }
    }

    @Nested
    class Jugador2 {

        @Test
        void agregarSegundoJugador_ok() {
            JuegoService s = new JuegoService();
            s.crearNuevaPartida("ppp");
            s.agregarSegundoJugador("ppp");
            assertNotNull(s.getPartida("ppp").getJugador2Id());
        }

        @Test
        void agregarSegundoJugador_noexistepartida() {
            JuegoService s = new JuegoService();
            assertThrows(IllegalArgumentException.class, () -> s.agregarSegundoJugador("pp"));
        }

        @Test
        void agregarSegundoJugador_yahayuno() {
            JuegoService s = new JuegoService();
            s.crearNuevaPartida("p");
            s.agregarSegundoJugador("p");
            assertThrows(IllegalStateException.class, () -> s.agregarSegundoJugador("p"));
        }
    }

    @Nested
    class Tablero {

        @Test
        void getTableroInexistente() {
            JuegoService s = new JuegoService();
            assertThrows(IllegalArgumentException.class, () -> s.getTableroPropio("p", UUID.randomUUID()));
        }
    }

    @Nested
    class Confirmaciones{

        @Test
        void confirma_posicion_j1_pero_no_j2() {
            JuegoService s = new JuegoService();
            Partida p = s.crearNuevaPartida("p");
            s.confirmar("p", p.getJugador1Id());
            assertTrue(s.getTableroPropio("p", p.getJugador1Id()).isConfirmado());
            assertNull(p.getTurnoActual());
        }

        @Test
        void confirmar_ambosInicianTurno() {
            JuegoService s = new JuegoService();
            Partida p = s.crearNuevaPartida("p");
            s.agregarSegundoJugador("p");

            s.confirmar("p", p.getJugador1Id());
            s.confirmar("p", p.getJugador2Id());
            assertTrue(s.getTableroPropio("p", p.getJugador1Id()).isConfirmado());
            assertTrue(s.getTableroPropio("p", p.getJugador2Id()).isConfirmado());

            assertNotNull(p.getTurnoActual());
        }

        @Test
        void shuffle() {
            JuegoService s = new JuegoService();
            Partida p = s.crearNuevaPartida("p");

            String antes = s.getTableroPropio("p", p.getJugador1Id()).toString();
            s.shuffle("p", p.getJugador1Id());
            String despues = s.getTableroPropio("p", p.getJugador1Id()).toString();
            assertNotEquals(antes, despues);

            s.confirmar("p", p.getJugador1Id());
            String antes2 = s.getTableroPropio("p", p.getJugador1Id()).toString();
            s.shuffle("p", p.getJugador1Id());
            String despues2 = s.getTableroPropio("p", p.getJugador1Id()).toString();
            assertEquals(antes2, despues2);
        }
    }

    @Nested
    class Atacar {

        @Test
        void atacar_sinconfirmar() {
            JuegoService s = new JuegoService();
            Partida p = s.crearNuevaPartida("p");
            s.agregarSegundoJugador("p");

            ResultadoAtaque r1 = s.atacar("p", p.getJugador1Id(), 0, 0);
            assertSame(ResultadoAtaque.JUEGO_NO_INICIADO, r1);
        }

        @Test
        void atacar_partidainexistente() {
            JuegoService s = new JuegoService();
            assertThrows(IllegalArgumentException.class, () -> s.atacar("p", UUID.randomUUID(), 0, 0));
        }
    }
}

