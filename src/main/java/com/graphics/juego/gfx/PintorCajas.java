package com.graphics.juego.gfx;

/**
 * Dibuja cajas unitarias reutilizando un único cubo en la GPU.
 * Aplica posición, escala, color y giro mediante uniforms del shader.
 */
public final class PintorCajas {

    private final ProgramaShader shader;
    private final Malla cubo;

    public PintorCajas(ProgramaShader shader) {
        this.shader = shader;
        this.cubo = crearCubo();
    }

    /** Activa la malla del cubo; debe llamarse una vez antes de dibujar las cajas. */
    public void preparar() {
        cubo.enlazar();
    }

    /** Dibuja una caja sin giro: posición XYZ, tamaño XYZ y color RGB entre 0 y 1. */
    public void caja(float x, float y, float z, float sx, float sy, float sz,
                     float r, float g, float b) {
        caja(x, y, z, sx, sy, sz, r, g, b, 0);
    }

    /** Igual que {@link #caja} pero con emisión visual (bombillas, ventanas encendidas). */
    public void caja(float x, float y, float z, float sx, float sy, float sz,
                     float r, float g, float b, float emision) {
        cajaGirada(x, y, z, sx, sy, sz, r, g, b, 0, emision);
    }

    /** Dibuja el cubo unitario con escala, giro alrededor de Y, posición y color. */
    public void cajaGirada(float x, float y, float z, float sx, float sy, float sz,
                           float r, float g, float b, float angulo) {
        cajaGirada(x, y, z, sx, sy, sz, r, g, b, angulo, 0);
    }

    /** Igual que {@link #cajaGirada} pero con emisión visual. */
    public void cajaGirada(float x, float y, float z, float sx, float sy, float sz,
                           float r, float g, float b, float angulo, float emision) {
        shader.setVec3("uPos", x, y, z);
        shader.setVec3("uEscala", sx, sy, sz);
        shader.setVec3("uColor", r, g, b);
        shader.setFloat("uGiro", angulo);
        shader.setFloat("uEmision", emision);
        cubo.dibujar();
    }

    public void eliminar() {
        cubo.eliminar();
    }

    /** Cubo explícito con posición XYZ y normal XYZ por vértice. */
    private static Malla crearCubo() {
        float[] vertices = {
            -0.5f, -0.5f, 0.5f, 0, 0, 1,
            0.5f, -0.5f, 0.5f, 0, 0, 1,
            0.5f, 0.5f, 0.5f, 0, 0, 1,
            0.5f, 0.5f, 0.5f, 0, 0, 1,
            -0.5f, 0.5f, 0.5f, 0, 0, 1,
            -0.5f, -0.5f, 0.5f, 0, 0, 1,

            -0.5f, -0.5f, -0.5f, 0, 0, -1,
            -0.5f, 0.5f, -0.5f, 0, 0, -1,
            0.5f, 0.5f, -0.5f, 0, 0, -1,
            0.5f, 0.5f, -0.5f, 0, 0, -1,
            0.5f, -0.5f, -0.5f, 0, 0, -1,
            -0.5f, -0.5f, -0.5f, 0, 0, -1,

            -0.5f, 0.5f, 0.5f, -1, 0, 0,
            -0.5f, 0.5f, -0.5f, -1, 0, 0,
            -0.5f, -0.5f, -0.5f, -1, 0, 0,
            -0.5f, -0.5f, -0.5f, -1, 0, 0,
            -0.5f, -0.5f, 0.5f, -1, 0, 0,
            -0.5f, 0.5f, 0.5f, -1, 0, 0,

            0.5f, 0.5f, 0.5f, 1, 0, 0,
            0.5f, -0.5f, 0.5f, 1, 0, 0,
            0.5f, -0.5f, -0.5f, 1, 0, 0,
            0.5f, -0.5f, -0.5f, 1, 0, 0,
            0.5f, 0.5f, -0.5f, 1, 0, 0,
            0.5f, 0.5f, 0.5f, 1, 0, 0,

            -0.5f, 0.5f, -0.5f, 0, 1, 0,
            -0.5f, 0.5f, 0.5f, 0, 1, 0,
            0.5f, 0.5f, 0.5f, 0, 1, 0,
            0.5f, 0.5f, 0.5f, 0, 1, 0,
            0.5f, 0.5f, -0.5f, 0, 1, 0,
            -0.5f, 0.5f, -0.5f, 0, 1, 0,

            -0.5f, -0.5f, -0.5f, 0, -1, 0,
            0.5f, -0.5f, -0.5f, 0, -1, 0,
            0.5f, -0.5f, 0.5f, 0, -1, 0,
            0.5f, -0.5f, 0.5f, 0, -1, 0,
            -0.5f, -0.5f, 0.5f, 0, -1, 0,
            -0.5f, -0.5f, -0.5f, 0, -1, 0
        };
        return new Malla(vertices, new Malla.Atributo[] {
            new Malla.Atributo(0, 3),
            new Malla.Atributo(1, 3)
        });
    }
}
