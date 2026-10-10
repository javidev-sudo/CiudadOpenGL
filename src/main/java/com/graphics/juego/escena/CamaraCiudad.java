package com.graphics.juego.escena;

import com.graphics.juego.core.Camara;
import com.graphics.juego.gfx.Entrada;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.objetos.Vehiculo;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Cámara de la ciudad con tres modos: persecución (detrás del auto), primera
 * persona (desde el vehículo) y aérea (órbita sobre el mapa). La tecla C cicla.
 */
public final class CamaraCiudad implements Camara {

    private final Vehiculo vehiculo;
    private ModoCamara modo = ModoCamara.PERSEGUIR;
    private float orbita = 0.6f;

    public CamaraCiudad(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /** Pasa al siguiente modo de cámara. */
    public void ciclar() {
        modo = switch (modo) {
            case PERSEGUIR -> ModoCamara.PRIMERA_PERSONA;
            case PRIMERA_PERSONA -> ModoCamara.AEREA;
            case AEREA -> ModoCamara.PERSEGUIR;
        };
    }

    public ModoCamara modo() {
        return modo;
    }

    /** Con las flechas (solo en vista aérea) gira la cámara alrededor de la ciudad. */
    public void actualizar(float deltaTime, Entrada entrada) {
        if (modo != ModoCamara.AEREA) {
            return;
        }
        if (entrada.pulsada(GLFW_KEY_LEFT)) {
            orbita -= deltaTime;
        }
        if (entrada.pulsada(GLFW_KEY_RIGHT)) {
            orbita += deltaTime;
        }
    }

    @Override
    public void aplicar(ProgramaShader shader, int ancho, int alto) {
        float autoX = vehiculo.autoX();
        float autoZ = vehiculo.autoZ();
        float angulo = vehiculo.angulo();
        float frenteX = -(float) Math.sin(angulo);
        float frenteZ = -(float) Math.cos(angulo);

        switch (modo) {
            case AEREA -> {
                float camaraX = (float) Math.sin(orbita) * 100;
                float camaraZ = (float) Math.cos(orbita) * 100;
                shader.setVec3("uOjo", camaraX, 90, camaraZ);
                shader.setVec3("uObjetivo", 0, 0, 0);
            }
            case PRIMERA_PERSONA -> {
                shader.setVec3("uOjo", autoX + frenteX, 1.7f, autoZ + frenteZ);
                shader.setVec3("uObjetivo", autoX + frenteX * 20, 1.0f, autoZ + frenteZ * 20);
            }
            default -> {
                float camaraX = autoX + (float) Math.sin(angulo) * 12;
                float camaraZ = autoZ + (float) Math.cos(angulo) * 12;
                shader.setVec3("uOjo", camaraX, 9, camaraZ);
                shader.setVec3("uObjetivo", autoX, 0.8f, autoZ);
            }
        }
        shader.setFloat("uAspecto", (float) ancho / alto);
    }
}
