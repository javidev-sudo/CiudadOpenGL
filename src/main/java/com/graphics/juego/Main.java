package com.graphics.juego;

import com.graphics.juego.core.Aplicacion;
import com.graphics.juego.escena.EscenaCiudad;
import com.graphics.juego.gfx.Ventana;

/**
 * Punto de entrada de la versión ampliada.
 * Ejecutar con: mvn exec:java -Dexec.mainClass=com.graphics.juego.Main
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Ventana ventana = new Ventana(1100, 760, "Ciudad 11x11 | WASD: conducir | C: camara | R: reiniciar");
        new Aplicacion(ventana, new EscenaCiudad(ventana)).run();
    }
}
