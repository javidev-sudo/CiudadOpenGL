package com.graphics.juego.gfx;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Geometría almacenada en la GPU (VAO + VBO) con atributos genéricos.
 */
public final class Malla {

    /** Atributo de vértice: índice de layout y cantidad de componentes. */
    public static final class Atributo {
        private final int indice;
        private final int componentes;

        public Atributo(int indice, int componentes) {
            this.indice = indice;
            this.componentes = componentes;
        }
    }

    private final int vao;
    private final int vbo;
    private final int vertices;
    private final int modo;

    public Malla(float[] datos, Atributo[] atributos) {
        this(datos, atributos, GL_TRIANGLES);
    }

    public Malla(float[] datos, Atributo[] atributos, int modo) {
        int flotantesPorVertice = 0;
        for (Atributo atributo : atributos) {
            flotantesPorVertice += atributo.componentes;
        }
        this.vertices = datos.length / flotantesPorVertice;
        this.modo = modo;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, datos, GL_STATIC_DRAW);

        int bytesPorVertice = flotantesPorVertice * Float.BYTES;
        long desplazamiento = 0;
        for (Atributo atributo : atributos) {
            glVertexAttribPointer(atributo.indice, atributo.componentes, GL_FLOAT, false,
                    bytesPorVertice, desplazamiento * Float.BYTES);
            glEnableVertexAttribArray(atributo.indice);
            desplazamiento += atributo.componentes;
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void enlazar() {
        glBindVertexArray(vao);
    }

    public void desenlazar() {
        glBindVertexArray(0);
    }

    public void dibujar() {
        glDrawArrays(modo, 0, vertices);
    }

    public void eliminar() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}
