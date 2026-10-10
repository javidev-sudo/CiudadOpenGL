package com.graphics.juego.gfx;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

/**
 * Consulta de entrada sobre {@link Ventana} sin exponer GLFW a las escenas.
 */
public final class Entrada {

    private final Ventana ventana;

    public Entrada(Ventana ventana) {
        this.ventana = ventana;
    }

    public boolean pulsada(int key) {
        return ventana.teclaPulsada(key);
    }

    public void alPresionar(IntConsumer manejador) {
        ventana.alPresionarTecla(manejador);
    }

    public void alScroll(DoubleConsumer manejador) {
        ventana.alDesplazarScroll(manejador);
    }
}
