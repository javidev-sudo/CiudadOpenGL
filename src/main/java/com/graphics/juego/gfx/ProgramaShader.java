package com.graphics.juego.gfx;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Compila y enlaza un par de shaders; cachea y envía uniforms a la GPU.
 */
public final class ProgramaShader {

    private final int id;
    private final Map<String, Integer> uniforms = new HashMap<>();

    public ProgramaShader(String fuenteVertices, String fuenteFragmentos) {
        int shaderVertices = compilar(GL_VERTEX_SHADER, fuenteVertices);
        int shaderFragmentos = 0;
        int programa = 0;
        try {
            shaderFragmentos = compilar(GL_FRAGMENT_SHADER, fuenteFragmentos);
            programa = glCreateProgram();
            glAttachShader(programa, shaderVertices);
            glAttachShader(programa, shaderFragmentos);
            glLinkProgram(programa);
            if (glGetProgrami(programa, GL_LINK_STATUS) == GL_FALSE) {
                throw new IllegalStateException(glGetProgramInfoLog(programa));
            }
        } finally {
            glDeleteShader(shaderVertices);
            if (shaderFragmentos != 0) {
                glDeleteShader(shaderFragmentos);
            }
        }
        this.id = programa;
    }

    private static int compilar(int tipo, String fuente) {
        int shader = glCreateShader(tipo);
        glShaderSource(shader, fuente);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String error = glGetShaderInfoLog(shader);
            glDeleteShader(shader);
            throw new IllegalStateException(error);
        }
        return shader;
    }

    public void usar() {
        glUseProgram(id);
    }

    public int id() {
        return id;
    }

    /** Devuelve la ubicación de un uniform, buscándola una sola vez. */
    public int uniform(String nombre) {
        if (!uniforms.containsKey(nombre)) {
            uniforms.put(nombre, glGetUniformLocation(id, nombre));
        }
        return uniforms.get(nombre);
    }

    public void setVec2(String nombre, float x, float y) {
        glUniform2f(uniform(nombre), x, y);
    }

    public void setVec3(String nombre, float x, float y, float z) {
        glUniform3f(uniform(nombre), x, y, z);
    }

    public void setFloat(String nombre, float valor) {
        glUniform1f(uniform(nombre), valor);
    }

    public void setInt(String nombre, int valor) {
        glUniform1i(uniform(nombre), valor);
    }

    /** Envía un arreglo de vec3 (por ejemplo, posiciones o colores de luces). */
    public void setVec3Array(String nombre, float[] datos) {
        glUniform3fv(uniform(nombre), datos);
    }

    public void eliminar() {
        glDeleteProgram(id);
    }
}
