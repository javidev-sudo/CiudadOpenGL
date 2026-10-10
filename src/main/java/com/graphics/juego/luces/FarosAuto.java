package com.graphics.juego.luces;

import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.objetos.Vehiculo;

/**
 * Dos focos delanteros del automóvil. Siguen la posición y orientación del
 * vehículo y se proyectan hacia delante con un cono y alcance limitados.
 */
public final class FarosAuto {

    private boolean encendidos = false;

    public void encender() {
        encendidos = true;
    }

    public void apagar() {
        encendidos = false;
    }

    public void alternar() {
        encendidos = !encendidos;
    }

    public boolean encendidos() {
        return encendidos;
    }

    /** Envía la posición y dirección de ambos focos al shader. */
    public void enviarA(ProgramaShader shader, Vehiculo vehiculo) {
        if (!encendidos) {
            shader.setInt("uFarosActivos", 0);
            return;
        }

        shader.setInt("uFarosActivos", 1);
        shader.setVec3("uFarosColor", 1.0f, 0.95f, 0.75f);
        shader.setFloat("uFaroAlcance", 34.0f);

        float autoX = vehiculo.autoX();
        float autoZ = vehiculo.autoZ();
        float angulo = vehiculo.angulo();
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float[] posiciones = new float[6];
        float[] direcciones = new float[6];
        float[] lados = {-0.55f, 0.55f};

        for (int i = 0; i < 2; i++) {
            float lado = lados[i];
            posiciones[i * 3] = autoX + coseno * lado - seno * 1.3f;
            posiciones[i * 3 + 1] = 0.7f;
            posiciones[i * 3 + 2] = autoZ - seno * lado - coseno * 1.3f;
            direcciones[i * 3] = -seno;
            direcciones[i * 3 + 1] = 0.0f;
            direcciones[i * 3 + 2] = -coseno;
        }

        shader.setVec3Array("uFarosPos", posiciones);
        shader.setVec3Array("uFarosDir", direcciones);
    }
}
