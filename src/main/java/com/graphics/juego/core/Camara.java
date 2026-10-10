package com.graphics.juego.core;

import com.graphics.juego.gfx.ProgramaShader;

/**
 * Abstracción de una cámara: envía su estado de vista al shader activo.
 */
public interface Camara {

    /** Envía posición, objetivo y proporción al programa de shaders. */
    void aplicar(ProgramaShader shader, int ancho, int alto);
}
