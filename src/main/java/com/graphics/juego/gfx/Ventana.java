package com.graphics.juego.gfx;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import org.lwjgl.glfw.Callbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

/**
 * Ciclo de vida de la ventana GLFW y su contexto OpenGL.
 */
public final class Ventana {

    private final int anchoInicial;
    private final int altoInicial;
    private final String titulo;
    private final int[] ancho = new int[1];
    private final int[] alto = new int[1];

    private long id;
    private GLFWErrorCallback errores;
    private IntConsumer alPresionar = key -> { };
    private DoubleConsumer alScroll = desplazamiento -> { };

    public Ventana(int ancho, int alto, String titulo) {
        this.anchoInicial = ancho;
        this.altoInicial = alto;
        this.titulo = titulo;
        this.ancho[0] = ancho;
        this.alto[0] = alto;
    }

    /** Inicia GLFW, crea la ventana, activa el contexto y carga OpenGL. */
    public void crear() {
        errores = GLFWErrorCallback.createPrint(System.err);
        errores.set();

        if (!glfwInit()) {
            throw new IllegalStateException("No se pudo iniciar GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        id = glfwCreateWindow(anchoInicial, altoInicial, titulo, 0, 0);
        if (id == 0) {
            throw new IllegalStateException("No se pudo crear la ventana OpenGL");
        }
        glfwMakeContextCurrent(id);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glfwSetKeyCallback(id, (ventanaEvento, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                alPresionar.accept(key);
            }
        });
        glfwSetScrollCallback(id, (ventanaEvento, xoffset, yoffset) -> alScroll.accept(yoffset));
    }

    public void alPresionarTecla(IntConsumer manejador) {
        this.alPresionar = manejador;
    }

    public void alDesplazarScroll(DoubleConsumer manejador) {
        this.alScroll = manejador;
    }

    public boolean teclaPulsada(int key) {
        return glfwGetKey(id, key) == GLFW_PRESS;
    }

    public boolean EstaCerrado() {
        return glfwWindowShouldClose(id);
    }

    public void solicitarCierre() {
        glfwSetWindowShouldClose(id, true);
    }

    public void procesarEventos() {
        glfwPollEvents();
    }

    public void intercambiar() {
        glfwSwapBuffers(id);
    }

    public void titulo(String texto) {
        glfwSetWindowTitle(id, texto);
    }

    public double tiempo() {
        return glfwGetTime();
    }

    public void actualizarTamano() {
        glfwGetFramebufferSize(id, ancho, alto);
    }

    public int ancho() {
        return ancho[0];
    }

    public int alto() {
        return alto[0];
    }

    /** Libera callbacks, ventana y recursos globales de GLFW. */
    public void destruir() {
        if (id != 0) {
            Callbacks.glfwFreeCallbacks(id);
            glfwDestroyWindow(id);
            id = 0;
        }
        glfwTerminate();
        if (errores != null) {
            glfwSetErrorCallback(null);
            errores.free();
            errores = null;
        }
    }
}
