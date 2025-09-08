package com.batallanaval.Model;

import java.util.*;

public class Tablero {
    private static final int TAMANO_DEFAULT = 10;
    private final int tamano;
    private Celda[][] grilla;
    private Map<Integer, List<Celda>> barcos;
    private boolean confirmado;
    private int siguienteBarcoId;

    public Tablero() {
        this(TAMANO_DEFAULT);
    }

    public Tablero(int tamano) {
        this.tamano = tamano;
        this.grilla = new Celda[tamano][tamano];
        this.barcos = new HashMap<>();
        this.confirmado = false;
        this.siguienteBarcoId = 1;
        inicializarTablero();
        colocarBarcos();
    }

    private void inicializarTablero() {
        for (int fila = 0; fila < tamano; fila++) {
            for (int col = 0; col < tamano; col++) {
                grilla[fila][col] = new Celda();
            }
        }
    }

    private void colocarBarcos() {
        // Colocar diferentes tipos de barcos
        colocarBarco(5); // Portaaviones
        colocarBarco(4); // Acorazado
        colocarBarco(3); // Crucero
        colocarBarco(3); // Submarino
        colocarBarco(2); // Destructor
    }

    private void colocarBarco(int longitud) {
        Random random = new Random();
        boolean colocado = false;
        int intentos = 0;
        int maxIntentos = 100;

        while (!colocado && intentos < maxIntentos) {
            int fila = random.nextInt(tamano);
            int col = random.nextInt(tamano);
            boolean horizontal = random.nextBoolean();

            if (puedeColocarBarco(fila, col, longitud, horizontal)) {
                List<Celda> celdas = new ArrayList<>();

                for (int i = 0; i < longitud; i++) {
                    int f = horizontal ? fila : fila + i;
                    int c = horizontal ? col + i : col;

                    grilla[f][c].setEstado(EstadoCelda.BARCO);
                    grilla[f][c].setBarcoId(siguienteBarcoId);
                    celdas.add(grilla[f][c]);
                }

                barcos.put(siguienteBarcoId, celdas);
                siguienteBarcoId++;
                colocado = true;
            }
            intentos++;
        }
    }

    private boolean puedeColocarBarco(int fila, int col, int longitud, boolean horizontal) {
        // Verificar si el barco cabe en el tablero
        if (horizontal && col + longitud > tamano) return false;
        if (!horizontal && fila + longitud > tamano) return false;

        // Verificar si no hay conflictos con otros barcos
        for (int i = 0; i < longitud; i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;

            if (grilla[f][c].getEstado() != EstadoCelda.AGUA) return false;

            // Verificar celdas adyacentes
            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nf = f + df;
                    int nc = c + dc;
                    if (nf >= 0 && nf < tamano && nc >= 0 && nc < tamano) {
                        if (grilla[nf][nc].getEstado() == EstadoCelda.BARCO) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void shuffle() {
        if (!confirmado) {
            // Limpiar tablero
            for (int fila = 0; fila < tamano; fila++) {
                for (int col = 0; col < tamano; col++) {
                    grilla[fila][col] = new Celda();
                }
            }
            barcos.clear();
            siguienteBarcoId = 1;

            // Volver a colocar barcos
            colocarBarcos();
        }
    }

    public String disparar(int fila, int col) {
        if (fila < 0 || fila >= tamano || col < 0 || col >= tamano) {
            return "FUERA_DE_RANGO";
        }

        com.batallanaval.Model.Celda celda = grilla[fila][col];

        if (celda.fueAtacada()) {
            return "YA_ATACADA";
        }

        if (celda.getEstado() == EstadoCelda.AGUA) {
            celda.setEstado(EstadoCelda.AGUA_ATACADA);
            return "AGUA";
        } else if (celda.getEstado() == EstadoCelda.BARCO) {
            celda.setEstado(EstadoCelda.IMPACTO);

            // Verificar si el barco fue hundido
            if (esBarcoHundido(celda.getBarcoId())) {
                marcarBarcoHundido(celda.getBarcoId());
                return "HUNDIDO";
            } else {
                return "IMPACTO";
            }
        }

        return "ERROR";
    }

    private boolean esBarcoHundido(int barcoId) {
        List<Celda> celdas = barcos.get(barcoId);
        if (celdas == null) return false;

        for (Celda celda : celdas) {
            if (celda.getEstado() == EstadoCelda.BARCO) {
                return false;
            }
        }
        return true;
    }

    private void marcarBarcoHundido(int barcoId) {
        List<Celda> celdas = barcos.get(barcoId);
        if (celdas != null) {
            for (Celda celda : celdas) {
                celda.setEstado(EstadoCelda.HUNDIDO);
            }
        }
    }

    public boolean todosLasBarcosHundidos() {
        for (int fila = 0; fila < tamano; fila++) {
            for (int col = 0; col < tamano; col++) {
                if (grilla[fila][col].getEstado() == EstadoCelda.BARCO) {
                    return false;
                }
            }
        }
        return true;
    }

    // Getters y setters
    public int getTamano() {
        return tamano;
    }

    public Celda getCelda(int fila, int col) {
        if (fila >= 0 && fila < tamano && col >= 0 && col < tamano) {
            return grilla[fila][col];
        }
        return null;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public Celda[][] getGrilla() {
        return grilla;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < tamano; fila++) {
            for (int col = 0; col < tamano; col++) {
                switch (grilla[fila][col].getEstado()) {
                    case AGUA:
                        sb.append("~ ");
                        break;
                    case BARCO:
                        sb.append("B ");
                        break;
                    case AGUA_ATACADA:
                        sb.append("O ");
                        break;
                    case IMPACTO:
                        sb.append("X ");
                        break;
                    case HUNDIDO:
                        sb.append("# ");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
