package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Banco decorativo de parque: asiento, respaldo y patas.
 */
public final class Banco {

    private final float x;
    private final float z;

    public Banco(float x, float z) {
        this.x = x;
        this.z = z;
    }

    public void dibujar(PintorCajas pintor) {
        pintor.caja(x, 0.55f, z, 1.8f, 0.15f, 0.6f, 0.55f, 0.35f, 0.18f);
        pintor.caja(x, 0.9f, z - 0.25f, 1.8f, 0.6f, 0.12f, 0.55f, 0.35f, 0.18f);
        pintor.caja(x - 0.7f, 0.25f, z, 0.12f, 0.5f, 0.12f, 0.30f, 0.30f, 0.32f);
        pintor.caja(x + 0.7f, 0.25f, z, 0.12f, 0.5f, 0.12f, 0.30f, 0.30f, 0.32f);
    }
}
