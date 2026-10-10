package com.graphics.juego.objetos;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import com.graphics.juego.gfx.Entrada;
import com.graphics.juego.gfx.PintorCajas;

/**
 * Vehículo conducido por el usuario. Compone su comportamiento con {@link Ciudad}
 * (colisiones) y {@link Entrada} (teclado).
 */
public final class Vehiculo {

    private static final float RADIO = 1.65f;

    private float autoX = -30;
    private float autoZ = 30;
    private float angulo = 0;
    private float velocidad = 0;

    /** Atiende acciones que deben ocurrir una sola vez por pulsación. */
    public void tecla(int key) {
        if (key == GLFW_KEY_R) {
            reiniciar();
        }
    }

    /** Coloca nuevamente el auto en su punto de partida. */
    public void reiniciar() {
        autoX = -30;
        autoZ = 30;
        angulo = 0;
        velocidad = 0;
    }

    /** Actualiza la conducción con los segundos transcurridos entre cuadros. */
    public void actualizar(float deltaTime, Ciudad ciudad, Entrada entrada) {
        float acelerador = 0;
        if (entrada.pulsada(GLFW_KEY_W) || entrada.pulsada(GLFW_KEY_UP)) {
            acelerador += 1;
        }
        if (entrada.pulsada(GLFW_KEY_S) || entrada.pulsada(GLFW_KEY_DOWN)) {
            acelerador -= 1;
        }

        velocidad += acelerador * 9 * deltaTime;
        float resistencia = entrada.pulsada(GLFW_KEY_SPACE) ? 7 : 0.7f;

        float factorFrenado = (float) Math.exp(-resistencia * deltaTime);
        velocidad *= factorFrenado;
        velocidad = Math.max(-6, Math.min(16, velocidad));
        float direccion = 0;

        if (entrada.pulsada(GLFW_KEY_A) || entrada.pulsada(GLFW_KEY_LEFT)) {
            direccion += 1;
        }
        if (entrada.pulsada(GLFW_KEY_D) || entrada.pulsada(GLFW_KEY_RIGHT)) {
            direccion -= 1;
        }

        angulo += direccion * velocidad * 0.11f * deltaTime;
        float frenteX = -(float) Math.sin(angulo);
        float frenteZ = -(float) Math.cos(angulo);
        float siguienteX = autoX + frenteX * velocidad * deltaTime;
        float siguienteZ = autoZ + frenteZ * velocidad * deltaTime;

        if (ciudad.puedeCircular(siguienteX, siguienteZ, RADIO)) {
            autoX = siguienteX;
            autoZ = siguienteZ;
        } else {
            velocidad = 0;
        }
    }

    /** Dibuja el modelo del vehículo sobre la ciudad. */
    public void dibujar(PintorCajas pintor) {
        pintor.cajaGirada(mundoX(0, 0), 0.65f, mundoZ(0, 0), 1.65f, 0.55f, 2.6f, 0.95f, 0.24f, 0.12f, angulo);
        pintor.cajaGirada(mundoX(0, 0.12f), 1.12f, mundoZ(0, 0.12f), 1.3f, 0.55f, 1.25f, 0.22f, 0.65f, 0.78f, angulo);

        float[] ladosRuedas = {-0.88f, 0.88f};
        float[] ejesRuedas = {-0.82f, 0.82f};
        for (float x : ladosRuedas) {
            for (float z : ejesRuedas) {
                pintor.cajaGirada(mundoX(x, z), 0.38f, mundoZ(x, z), 0.24f, 0.58f, 0.6f,
                        0.055f, 0.065f, 0.08f, angulo);
            }
        }

        float[] ladosFaros = {-0.55f, 0.55f};
        for (float x : ladosFaros) {
            pintor.cajaGirada(mundoX(x, -1.32f), 0.68f, mundoZ(x, -1.32f), 0.38f, 0.2f, 0.07f,
                    1, 0.95f, 0.65f, angulo);
            pintor.cajaGirada(mundoX(x, 1.32f), 0.68f, mundoZ(x, 1.32f), 0.35f, 0.17f, 0.07f,
                    0.85f, 0.05f, 0.05f, angulo);
        }
    }

    private float mundoX(float x, float z) {
        return autoX + (float) Math.cos(angulo) * x + (float) Math.sin(angulo) * z;
    }

    private float mundoZ(float x, float z) {
        return autoZ - (float) Math.sin(angulo) * x + (float) Math.cos(angulo) * z;
    }

    public int kilometrosPorHora() {
        return Math.round(Math.abs(velocidad) * 3.6f);
    }

    public float autoX() {
        return autoX;
    }

    public float autoZ() {
        return autoZ;
    }

    public float angulo() {
        return angulo;
    }
}
