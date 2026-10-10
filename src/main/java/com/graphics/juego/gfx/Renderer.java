package com.graphics.juego.gfx;

import static org.lwjgl.opengl.GL11.*;

/**
 * Estado y operaciones de dibujo de OpenGL (viewport, limpieza, dibujo, errores).
 */
public final class Renderer {

    public void habilitarProfundidad() {
        glEnable(GL_DEPTH_TEST);
    }

    public void viewport(int ancho, int alto) {
        glViewport(0, 0, ancho, alto);
    }

    /** Define un viewport rectangular (para el minimapa u otras vistas). */
    public void viewport(int x, int y, int ancho, int alto) {
        glViewport(x, y, ancho, alto);
    }

    /** Activa el recorte a un rectángulo para no alterar el resto de la pantalla. */
    public void recortar(int x, int y, int ancho, int alto) {
        glEnable(GL_SCISSOR_TEST);
        glScissor(x, y, ancho, alto);
    }

    public void desactivarRecorte() {
        glDisable(GL_SCISSOR_TEST);
    }

    public void limpiar(float r, float g, float b) {
        glClearColor(r, g, b, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void usar(ProgramaShader shader) {
        shader.usar();
    }

    public void dibujar(Malla malla) {
        malla.enlazar();
        malla.dibujar();
    }

    public void verificarError() {
        if (glGetError() != GL_NO_ERROR) {
            throw new IllegalStateException("Error OpenGL al dibujar");
        }
    }
}
