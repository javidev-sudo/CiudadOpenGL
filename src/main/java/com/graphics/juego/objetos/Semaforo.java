package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Semáforo tipo pórtico: dos postes a ambos lados de la calle unidos por un
 * travesaño horizontal del que cuelga el bloque de luces. El eje indica sobre
 * qué dirección cruza el pórtico (X o Z). Su secuencia es visual (no detiene al
 * auto): rojo 6 s, verde 6 s, amarillo 2 s.
 */
public final class Semaforo {

    /** Dirección sobre la que se extiende el travesaño que cruza la calle. */
    public enum Eje {
        X, Z
    }

    private static final float CICLO = 14.0f;
    public static final float ANCHO_CALLE = 10.0f;
    private static final float MEDIA = ANCHO_CALLE / 2.0f;
    private static final float ALTURA_POSTE = 6.0f;
    private static final float GROSOR = 0.30f;

    private final float x;
    private final float z;
    private final Eje eje;
    private float tiempo = 0;

    public Semaforo(float x, float z, Eje eje) {
        this.x = x;
        this.z = z;
        this.eje = eje;
    }

    public void actualizar(float deltaTime) {
        tiempo = (tiempo + deltaTime) % CICLO;
    }

    public float x() {
        return x;
    }

    public float z() {
        return z;
    }

    public Eje eje() {
        return eje;
    }

    /** Dibuja los dos postes, el travesaño que cruza la calle y las luces colgadas. */
    public void dibujar(PintorCajas pintor) {
        if (eje == Eje.X) {
            // Postes a cada lado en X; travesaño que cruza la calle en X.
            pintor.caja(x - MEDIA, ALTURA_POSTE / 2, z, GROSOR, ALTURA_POSTE, GROSOR, 0.15f, 0.15f, 0.18f);
            pintor.caja(x + MEDIA, ALTURA_POSTE / 2, z, GROSOR, ALTURA_POSTE, GROSOR, 0.15f, 0.15f, 0.18f);
            pintor.caja(x, ALTURA_POSTE, z, ANCHO_CALLE, GROSOR, GROSOR, 0.15f, 0.15f, 0.18f);
            dibujarLuces(pintor, 0.5f, 0.85f);
        } else {
            // Postes a cada lado en Z; travesaño que cruza la calle en Z.
            pintor.caja(x, ALTURA_POSTE / 2, z - MEDIA, GROSOR, ALTURA_POSTE, GROSOR, 0.15f, 0.15f, 0.18f);
            pintor.caja(x, ALTURA_POSTE / 2, z + MEDIA, GROSOR, ALTURA_POSTE, GROSOR, 0.15f, 0.15f, 0.18f);
            pintor.caja(x, ALTURA_POSTE, z, GROSOR, GROSOR, ANCHO_CALLE, 0.15f, 0.15f, 0.18f);
            dibujarLuces(pintor, 0.85f, 0.5f);
        }
    }

    /** Bloque de luces que cuelga del centro del travesaño y mira hacia la calle. */
    private void dibujarLuces(PintorCajas pintor, float sx, float sz) {
        boolean rojo = tiempo < 6.0f;
        boolean amarillo = tiempo >= 12.0f;
        boolean verde = !rojo && !amarillo;

        float y = ALTURA_POSTE - 0.75f;
        pintor.caja(x, y - 0.6f, z, 0.5f, 1.9f, 0.5f, 0.10f, 0.10f, 0.12f);
        pintor.caja(x, y, z, sx, 0.5f, sz, 1.0f, 0.15f, 0.10f, rojo ? 1.0f : 0.0f);
        pintor.caja(x, y - 0.6f, z, sx, 0.5f, sz, 1.0f, 0.80f, 0.10f, amarillo ? 1.0f : 0.0f);
        pintor.caja(x, y - 1.2f, z, sx, 0.5f, sz, 0.10f, 0.90f, 0.20f, verde ? 1.0f : 0.0f);
    }
}
