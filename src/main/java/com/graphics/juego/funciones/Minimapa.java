package com.graphics.juego.funciones;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.gfx.Renderer;
import com.graphics.juego.objetos.Ciudad;
import com.graphics.juego.objetos.Destino;
import com.graphics.juego.objetos.Vehiculo;

/**
 * Minimapa: vista superior de la ciudad en una esquina. Usa recorte (scissor)
 * para no borrar ni alterar la vista principal y se reencuadra al redimensionar.
 * El norte queda hacia arriba y muestra el auto (con su orientación) y el destino.
 */
public final class Minimapa {

    private boolean visible = true;

    public void alternar() {
        visible = !visible;
    }

    public boolean visible() {
        return visible;
    }

    public void dibujar(Renderer renderer, ProgramaShader shader, PintorCajas pintor,
                        Ciudad ciudad, Vehiculo vehiculo, Destino destino, int ancho, int alto) {
        if (!visible || ancho <= 0 || alto <= 0) {
            return;
        }

        int lado = Math.min(ancho, alto) / 3;
        int margen = 16;
        int borde = 3;
        int x = ancho - lado - margen;
        int y = alto - lado - margen;

        // Marco y fondo, limpiando solo la región del minimapa.
        renderer.recortar(x - borde, y - borde, lado + 2 * borde, lado + 2 * borde);
        renderer.limpiar(0.75f, 0.78f, 0.82f);
        renderer.recortar(x, y, lado, lado);
        renderer.limpiar(0.05f, 0.06f, 0.09f);
        renderer.viewport(x, y, lado, lado);

        shader.setInt("uMapa", 1);
        shader.setInt("uIluminado", 0);
        pintor.preparar();
        ciudad.dibujar(pintor, false);

        // Marcadores por encima de todo (y alta => más cerca en profundidad).
        pintor.cajaGirada(vehiculo.autoX(), 70.0f, vehiculo.autoZ(),
                4.0f, 2.0f, 5.0f, 1.0f, 0.25f, 0.20f, vehiculo.angulo());
        if (destino != null) {
            pintor.caja(destino.x(), 70.0f, destino.z(), 3.0f, 2.0f, 3.0f, 0.20f, 0.90f, 1.0f);
        }

        shader.setInt("uMapa", 0);
        shader.setInt("uIluminado", 1);
        renderer.desactivarRecorte();
        renderer.viewport(0, 0, ancho, alto);
    }
}
