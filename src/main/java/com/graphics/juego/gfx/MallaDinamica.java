package com.graphics.juego.gfx;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Malla cuyos vértices se reescriben con frecuencia (interfaz/HUD). Mantiene un
 * único VAO/VBO y sube los datos con {@code GL_DYNAMIC_DRAW}. El formato es el
 * mismo que {@link Malla}: posición (3) + normal (3) por vértice.
 */
public final class MallaDinamica {

    private static final int FLOTANTES_POR_VERTICE = 6;

    private final int vao;
    private final int vbo;
    private int vertices;

    public MallaDinamica() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        int bytesPorVertice = FLOTANTES_POR_VERTICE * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, bytesPorVertice, 0L);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, bytesPorVertice, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /** Reemplaza los vértices actuales por el arreglo dado. */
    public void actualizar(float[] datos) {
        vertices = datos.length / FLOTANTES_POR_VERTICE;
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, datos, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void dibujar() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertices);
        glBindVertexArray(0);
    }

    public void eliminar() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}
