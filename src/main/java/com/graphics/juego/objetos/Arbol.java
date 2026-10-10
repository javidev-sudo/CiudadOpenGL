package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Árbol decorativo de parque: tronco y follaje.
 */
public final class Arbol {

    private final float x;
    private final float z;
    private final float escala;

    public Arbol(float x, float z, float escala) {
        this.x = x;
        this.z = z;
        this.escala = escala;
    }

    public void dibujar(PintorCajas pintor) {
        pintor.caja(x, 0.75f * escala, z, 0.35f * escala, 1.5f * escala, 0.35f * escala,
                0.32f, 0.22f, 0.12f);
        pintor.caja(x, 2.0f * escala, z, 1.6f * escala, 1.6f * escala, 1.6f * escala,
                0.15f, 0.45f, 0.20f);
    }
}
