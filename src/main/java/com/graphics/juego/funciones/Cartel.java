package com.graphics.juego.funciones;

import com.graphics.juego.gfx.Malla;
import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.gfx.Vertices;

/**
 * Cartel urbano o valla publicitaria: dos postes con un panel de color y un
 * texto en el mundo. El texto se genera una sola vez en una {@link Malla}
 * estática y se dibuja sin iluminación para que siempre sea legible.
 */
public final class Cartel {

    /** Dirección hacia la que mira la cara legible del cartel. */
    public enum Direccion {
        NORTE, SUR, ESTE, OESTE
    }

    private final float x;
    private final float z;
    private final float ancho;
    private final float alto;
    private final float yBase;
    private final Direccion direccion;
    private final String texto;
    private final float escala;
    private final float rojo;
    private final float verde;
    private final float azul;
    private final float tRojo;
    private final float tVerde;
    private final float tAzul;

    private Malla textoMalla;

    public Cartel(float x, float z, float ancho, float alto, float yBase, Direccion direccion,
                  String texto, float escala, float rojo, float verde, float azul,
                  float tRojo, float tVerde, float tAzul) {
        this.x = x;
        this.z = z;
        this.ancho = ancho;
        this.alto = alto;
        this.yBase = yBase;
        this.direccion = direccion;
        this.texto = texto;
        this.escala = escala;
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
        this.tRojo = tRojo;
        this.tVerde = tVerde;
        this.tAzul = tAzul;
    }

    /** Genera la geometría del texto. Requiere el contexto OpenGL activo. */
    public void iniciar() {
        float rx = direccion == Direccion.SUR ? 1f : direccion == Direccion.NORTE ? -1f : 0f;
        float rz = direccion == Direccion.ESTE ? -1f : direccion == Direccion.OESTE ? 1f : 0f;

        float anchoTexto = Fuente.ancho2D(texto, escala);
        float origenX = x - rx * (anchoTexto / 2f);
        float origenZ = z - rz * (anchoTexto / 2f);
        float origenY = yBase + (alto + Fuente.ALTO * escala) / 2f;

        float separacion = 0.16f;
        float frenteX = direccion == Direccion.ESTE ? separacion
                : direccion == Direccion.OESTE ? -separacion : 0f;
        float frenteZ = direccion == Direccion.SUR ? separacion
                : direccion == Direccion.NORTE ? -separacion : 0f;

        Vertices vertices = new Vertices();
        Fuente.escribirPlano(texto, origenX + frenteX, origenY, origenZ + frenteZ,
                rx, rz, escala, vertices);

        textoMalla = new Malla(vertices.aArreglo(), new Malla.Atributo[] {
            new Malla.Atributo(0, 3),
            new Malla.Atributo(1, 3)
        });
    }

    /** Dibuja postes y panel con el pintor de cajas. */
    public void dibujar(PintorCajas pintor) {
        float medio = ancho / 2f - 0.3f;
        boolean caraZ = direccion == Direccion.SUR || direccion == Direccion.NORTE;
        if (caraZ) {
            pintor.caja(x - medio, yBase / 2f, z, 0.25f, yBase, 0.25f, 0.25f, 0.26f, 0.30f);
            pintor.caja(x + medio, yBase / 2f, z, 0.25f, yBase, 0.25f, 0.25f, 0.26f, 0.30f);
            pintor.caja(x, yBase + alto / 2f, z, ancho, alto, 0.25f, rojo, verde, azul);
        } else {
            pintor.caja(x, yBase / 2f, z - medio, 0.25f, yBase, 0.25f, 0.25f, 0.26f, 0.30f);
            pintor.caja(x, yBase / 2f, z + medio, 0.25f, yBase, 0.25f, 0.25f, 0.26f, 0.30f);
            pintor.caja(x, yBase + alto / 2f, z, 0.25f, alto, ancho, rojo, verde, azul);
        }
    }

    /** Dibuja el texto en el mundo (sin iluminación) y restaura el estado. */
    public void dibujarTexto(ProgramaShader shader) {
        shader.setInt("uIluminado", 0);
        shader.setVec3("uColor", tRojo, tVerde, tAzul);
        shader.setVec3("uPos", 0f, 0f, 0f);
        shader.setVec3("uEscala", 1f, 1f, 1f);
        shader.setFloat("uGiro", 0f);
        shader.setFloat("uEmision", 0f);
        textoMalla.enlazar();
        textoMalla.dibujar();
        shader.setInt("uIluminado", 1);
    }

    public void limpiar() {
        if (textoMalla != null) {
            textoMalla.eliminar();
        }
    }
}
