package com.graphics.juego.funciones;

import java.util.Random;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Cuerpos celestes del cielo como geometría 3D real: el sol durante el día y la
 * luna con estrellas durante la noche. Se sitúan a gran distancia en la
 * dirección del cuerpo y quedan anclados a la cámara (distancia "infinita"), de
 * modo que respetan la perspectiva y la profundidad: los edificios los ocultan
 * como a cualquier otro objeto.
 */
public final class Cielo {

    /** Distancia a la que se sitúan los cuerpos (dentro del plano lejano). */
    private static final float DISTANCIA = 200.0f;
    private static final float TAMANO_SOL = 16.0f;
    private static final float TAMANO_LUNA = 11.0f;
    private static final float TAMANO_ESTRELLA = 1.1f;
    private static final int NUM_ESTRELLAS = 140;

    /** Cada estrella: posición (x, y, z) y brillo. */
    private final float[] estrellas = new float[NUM_ESTRELLAS * 4];

    public Cielo() {
        Random azar = new Random(1234);
        for (int i = 0; i < NUM_ESTRELLAS; i++) {
            // Dirección repartida por el hemisferio superior (y > 0).
            float altura = 0.05f + azar.nextFloat() * 0.95f;
            float angulo = azar.nextFloat() * (float) (Math.PI * 2.0);
            float radio = (float) Math.sqrt(1.0f - altura * altura);
            estrellas[i * 4] = (float) Math.cos(angulo) * radio * DISTANCIA;
            estrellas[i * 4 + 1] = altura * DISTANCIA;
            estrellas[i * 4 + 2] = (float) Math.sin(angulo) * radio * DISTANCIA;
            estrellas[i * 4 + 3] = 0.6f + azar.nextFloat() * 0.4f;
        }
    }

    /** Dibuja el cuerpo y las estrellas del modo indicado (requiere uMapa=2). */
    public void dibujar(PintorCajas pintor, boolean noche, float[] direccionCuerpo) {
        if (noche) {
            dibujarEstrellas(pintor);
            dibujarCuerpo(pintor, direccionCuerpo, TAMANO_LUNA, 0.92f, 0.93f, 0.85f);
        } else {
            dibujarCuerpo(pintor, direccionCuerpo, TAMANO_SOL, 1.0f, 0.90f, 0.45f);
        }
    }

    /** Coloca el sol o la luna en la dirección indicada, a distancia constante. */
    private void dibujarCuerpo(PintorCajas pintor, float[] direccion, float tamano,
                               float r, float g, float b) {
        pintor.caja(direccion[0] * DISTANCIA, direccion[1] * DISTANCIA, direccion[2] * DISTANCIA,
                tamano, tamano, tamano, r, g, b);
    }

    private void dibujarEstrellas(PintorCajas pintor) {
        for (int i = 0; i < NUM_ESTRELLAS; i++) {
            float brillo = estrellas[i * 4 + 3];
            pintor.caja(estrellas[i * 4], estrellas[i * 4 + 1], estrellas[i * 4 + 2],
                    TAMANO_ESTRELLA, TAMANO_ESTRELLA, TAMANO_ESTRELLA, brillo, brillo, brillo);
        }
    }
}
