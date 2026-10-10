package com.graphics.juego.funciones;

import java.util.Random;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.objetos.Destino;
import com.graphics.juego.objetos.Vehiculo;

/**
 * Gestiona la ruta de entregas: solo hay un punto de entrega activo a la vez.
 * Al pasar cerca de él, desaparece y aparece en otro punto de la ciudad hasta
 * completar el total; entonces la ruta queda finalizada. El reinicio comienza
 * un nuevo recorrido.
 */
public final class GestorEntregas {

    /** Número de entregas que forman un recorrido completo. */
    public static final int TOTAL = 5;

    /** Centros de las calles, tanto en X como en Z. */
    private static final float[] CALLES = {-50, -30, -10, 10, 30, 50};

    private final Random random = new Random();
    private Destino activo;
    private int completadas = 0;
    private float tiempo = 0;

    public GestorEntregas() {
        activo = new Destino(-50, -50);
    }

    public void actualizar(float deltaTime, Vehiculo vehiculo) {
        tiempo += deltaTime;
        if (completado()) {
            return;
        }
        if (activo.contiene(vehiculo.autoX(), vehiculo.autoZ())) {
            completadas++;
            if (!completado()) {
                activo = nuevoDestino(vehiculo.autoX(), vehiculo.autoZ());
            }
        }
    }

    public void reiniciar() {
        completadas = 0;
        activo = new Destino(-50, -50);
    }

    public boolean completado() {
        return completadas >= TOTAL;
    }

    public int completadas() {
        return completadas;
    }

    public int total() {
        return TOTAL;
    }

    public Destino destinoActivo() {
        return completado() ? null : activo;
    }

    public void dibujar(PintorCajas pintor) {
        if (!completado()) {
            activo.dibujar(pintor, tiempo, true);
        }
    }

    /** Punto aleatorio de la ciudad, lejos de (x, z) para que la entrega reaparezca en otro sitio. */
    private Destino nuevoDestino(float x, float z) {
        float nx;
        float nz;
        do {
            nx = CALLES[random.nextInt(CALLES.length)];
            nz = CALLES[random.nextInt(CALLES.length)];
        } while (Math.abs(nx - x) < 20 && Math.abs(nz - z) < 20);
        return new Destino(nx, nz);
    }
}
