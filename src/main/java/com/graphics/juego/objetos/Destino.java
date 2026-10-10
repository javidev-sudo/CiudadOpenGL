package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Punto de entrega: el auto completa la entrega al pasar cerca.
 */
public final class Destino {

    private static final float RADIO = 3.0f;

    private final float x;
    private final float z;

    public Destino(float x, float z) {
        this.x = x;
        this.z = z;
    }

    public boolean contiene(float px, float pz) {
        float dx = px - x;
        float dz = pz - z;
        return dx * dx + dz * dz <= RADIO * RADIO;
    }

    /** Dibuja el marcador; si está activo, late suavemente para llamar la atención. */
    public void dibujar(PintorCajas pintor, float tiempo, boolean activo) {
        float pulso = activo ? 0.55f + 0.45f * (float) Math.sin(tiempo * 6.0) : 0.3f;
        pintor.caja(x, 0.5f, z, 2.4f, 1.0f, 2.4f, 0.15f, 0.75f, 0.95f, pulso);
        pintor.caja(x, 1.6f, z, 0.4f, 2.2f, 0.4f, 0.9f, 0.9f, 0.95f, activo ? 1.0f : 0.25f);
    }

    public float x() {
        return x;
    }

    public float z() {
        return z;
    }
}
