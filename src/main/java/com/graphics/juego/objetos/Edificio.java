package com.graphics.juego.objetos;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Edificio de una manzana. Hay tres tipos con forma, altura y color propios:
 * pequeño, mediano y rascacielos. Dibuja su acera, su cuerpo y sus ventanas
 * (que aparentan iluminación durante la noche).
 */
public final class Edificio {

    public enum Tipo { PEQUENO, MEDIANO, RASCACIELOS }

    private final float x;
    private final float z;
    private final Tipo tipo;
    private final float altura;
    private final float rojo;
    private final float verde;
    private final float azul;

    public Edificio(float x, float z, Tipo tipo, float altura, float rojo, float verde, float azul) {
        this.x = x;
        this.z = z;
        this.tipo = tipo;
        this.altura = altura;
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    public void dibujar(PintorCajas pintor, boolean noche) {
        pintor.caja(x, 0.15f, z, 10, 0.3f, 10, 0.60f, 0.64f, 0.66f);
        if (tipo == Tipo.RASCACIELOS) {
            dibujarRascacielos(pintor, noche);
        } else {
            dibujarBloque(pintor, noche);
        }
    }

    /** Bloque bajo o mediano: cuerpo, cubierta y rejilla de ventanas. */
    private void dibujarBloque(PintorCajas pintor, boolean noche) {
        float ancho = (tipo == Tipo.PEQUENO) ? 6.0f : 7.0f;
        pintor.caja(x, altura / 2 + 0.3f, z, ancho, altura, ancho, rojo, verde, azul);
        pintor.caja(x, altura + 0.45f, z, ancho + 0.3f, 0.3f, ancho + 0.3f, 0.20f, 0.26f, 0.32f);
        dibujarVentanas(pintor, noche, ancho / 2 + 0.06f, 1.6f, 1.8f, altura - 0.5f, ancho);
    }

    /** Rascacielos: tres cuerpos escalonados de vidrio y una antena. */
    private void dibujarRascacielos(PintorCajas pintor, boolean noche) {
        float base = 0.3f;
        float h1 = altura * 0.40f;
        float h2 = altura * 0.35f;
        float h3 = altura * 0.25f;

        pintor.caja(x, base + h1 / 2, z, 7.0f, h1, 7.0f, 0.30f, 0.45f, 0.65f);
        pintor.caja(x, base + h1 + h2 / 2, z, 5.4f, h2, 5.4f, 0.35f, 0.52f, 0.72f);
        pintor.caja(x, base + h1 + h2 + h3 / 2, z, 4.0f, h3, 4.0f, 0.40f, 0.60f, 0.80f);
        pintor.caja(x, altura + 2.0f, z, 0.25f, 4.0f, 0.25f, 0.70f, 0.72f, 0.78f);

        dibujarFranjas(pintor, noche, 3.56f, base + h1 / 2, h1, 7.0f);
        dibujarFranjas(pintor, noche, 2.76f, base + h1 + h2 / 2, h2, 5.4f);
        dibujarFranjas(pintor, noche, 2.06f, base + h1 + h2 + h3 / 2, h3, 4.0f);
    }

    /** Ventanas cuadradas repartidas por pisos en las cuatro fachadas. */
    private void dibujarVentanas(PintorCajas pintor, boolean noche, float offset,
                                 float yInicio, float paso, float yFin, float ancho) {
        float r = noche ? 1.00f : 0.22f;
        float g = noche ? 0.85f : 0.30f;
        float b = noche ? 0.45f : 0.40f;
        float emision = noche ? 1.0f : 0.0f;
        float medio = ancho / 2 - 1.0f;

        for (float y = yInicio; y <= yFin; y += paso) {
            for (float d : new float[] {-medio, medio}) {
                pintor.caja(x + d, y, z + offset, 0.9f, 0.9f, 0.12f, r, g, b, emision);
                pintor.caja(x + d, y, z - offset, 0.9f, 0.9f, 0.12f, r, g, b, emision);
                pintor.caja(x + offset, y, z + d, 0.12f, 0.9f, 0.9f, r, g, b, emision);
                pintor.caja(x - offset, y, z + d, 0.12f, 0.9f, 0.9f, r, g, b, emision);
            }
        }
    }

    /** Franjas verticales de vidrio para el rascacielos. */
    private void dibujarFranjas(PintorCajas pintor, boolean noche, float offset,
                                float yCentro, float altoBloque, float ancho) {
        float r = noche ? 1.00f : 0.22f;
        float g = noche ? 0.85f : 0.30f;
        float b = noche ? 0.45f : 0.40f;
        float emision = noche ? 1.0f : 0.0f;
        float medio = ancho / 2 - 0.5f;

        for (float d : new float[] {-medio, 0.0f, medio}) {
            pintor.caja(x + d, yCentro, z + offset, 0.35f, altoBloque, 0.12f, r, g, b, emision);
            pintor.caja(x + d, yCentro, z - offset, 0.35f, altoBloque, 0.12f, r, g, b, emision);
            pintor.caja(x + offset, yCentro, z + d, 0.12f, altoBloque, 0.35f, r, g, b, emision);
            pintor.caja(x - offset, yCentro, z + d, 0.12f, altoBloque, 0.35f, r, g, b, emision);
        }
    }
}
