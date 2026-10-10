package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Farola: un punto de luz en la ciudad. Su poste y su bombilla son geometría;
 * el aporte de luz al entorno se resuelve en el shader como luz puntual.
 */
public final class Farola {

    private static final float ALTURA = 4.0f;

    private final float x;
    private final float z;
    private final float rojo;
    private final float verde;
    private final float azul;

    public Farola(float x, float z, float rojo, float verde, float azul) {
        this.x = x;
        this.z = z;
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    /** Escribe posición (a la altura de la bombilla) y color en los arreglos del shader. */
    public void escribir(float[] posiciones, float[] colores, int indice) {
        posiciones[indice * 3] = x;
        posiciones[indice * 3 + 1] = ALTURA;
        posiciones[indice * 3 + 2] = z;
        colores[indice * 3] = rojo;
        colores[indice * 3 + 1] = verde;
        colores[indice * 3 + 2] = azul;
    }

    /** Dibuja poste y bombilla. La bombilla emite visualmente según la emisión recibida. */
    public void dibujar(PintorCajas pintor, float emision) {
        pintor.caja(x, ALTURA / 2, z, 0.22f, ALTURA, 0.22f, 0.25f, 0.26f, 0.30f);
        pintor.caja(x, ALTURA + 0.2f, z, 0.7f, 0.4f, 0.7f, 1.0f, 0.95f, 0.70f, emision);
    }
}
