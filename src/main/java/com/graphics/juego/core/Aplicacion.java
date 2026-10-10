package com.graphics.juego.core;

import com.graphics.juego.gfx.Ventana;

/**
 * Ciclo de vida genérico: crear ventana, iniciar la escena, iterar
 * eventos/actualización/dibujo y liberar recursos.
 */
public final class Aplicacion {

    private final Ventana ventana;
    private final Escena escena;

    public Aplicacion(Ventana ventana, Escena escena) {
        this.ventana = ventana;
        this.escena = escena;
    }

    /** Arranca la aplicación y garantiza la limpieza aunque ocurra un error. */
    public void run() {
        ventana.crear();
        try {
            escena.iniciar();
            loop();
        } finally {
            escena.limpiar();
            ventana.destruir();
        }
    }

    /** Mantiene el ciclo continuo hasta que se solicita cerrar la ventana. */
    private void loop() {
        double tiempoAnterior = ventana.tiempo();
        int cuadros = 0;
        int maxCuadros = Integer.getInteger("demo.frames", 0);

        while (!ventana.EstaCerrado()) {
            ventana.procesarEventos();

            double tiempoActual = ventana.tiempo();
            float deltaTime = (float) Math.min(tiempoActual - tiempoAnterior, 0.05);
            tiempoAnterior = tiempoActual;

            escena.actualizar(deltaTime);

            ventana.actualizarTamano();
            if (ventana.ancho() > 0 && ventana.alto() > 0) {
                escena.dibujar();
            }

            ventana.intercambiar();

            cuadros++;
            if (maxCuadros > 0 && cuadros >= maxCuadros) {
                ventana.solicitarCierre();
            }
        }
    }
}
