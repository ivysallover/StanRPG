package com.batallanaval.Model;

public class ResultadoAtaque {
    private String resultado;
    private boolean gameOver;
    private boolean victoria;

    public ResultadoAtaque(String resultado) {
        this.resultado = resultado;
        this.gameOver = false;
        this.victoria = false;
    }

    public ResultadoAtaque(String resultado, boolean gameOver, boolean victoria) {
        this.resultado = resultado;
        this.gameOver = gameOver;
        this.victoria = victoria;
    }

    // Getters y setters
    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    @Override
    public String toString() {
        return "ResultadoAtaque{" +
                "resultado='" + resultado + '\'' +
                ", gameOver=" + gameOver +
                ", victoria=" + victoria +
                '}';
    }
}