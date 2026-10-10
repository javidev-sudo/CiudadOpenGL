package com.graphics.juego.funciones;

import com.graphics.juego.gfx.MallaDinamica;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.gfx.Vertices;

import static org.lwjgl.opengl.GL11.*;

/**
 * Interfaz gráfica superpuesta dentro de la ventana: panel con velocidad y
 * estado de luces, ayuda de controles (H) y menú de pausa navegable (P). Todos
 * los cuadros se agrupan por color y se dibujan con unas pocas llamadas, sin
 * prueba de profundidad y con mezcla alfa para los fondos.
 */
public final class Interfaz {

    private static final int FONDO = 0;
    private static final int TEXTO = 1;
    private static final int ETIQUETA = 2;
    private static final int OK = 3;
    private static final int ALERTA = 4;
    private static final int TITULO = 5;
    private static final int SELECCION = 6;
    private static final int GRUPOS = 7;

    /** Color RGBA por grupo (el alfa se aplica al fondo de los paneles). */
    private static final float[][] COLORES = {
        {0.04f, 0.05f, 0.08f, 0.78f},
        {0.94f, 0.95f, 1.00f, 1.00f},
        {0.60f, 0.64f, 0.72f, 1.00f},
        {0.30f, 0.95f, 0.45f, 1.00f},
        {0.95f, 0.40f, 0.30f, 1.00f},
        {1.00f, 0.82f, 0.30f, 1.00f},
        {0.25f, 0.70f, 1.00f, 1.00f},
    };

    private static final String[] CONTROLES = {
        "W A S D / FLECHAS : CONDUCIR",
        "ESPACIO : FRENO",
        "C : CAMBIO DE CAMARA",
        "N : DIA / NOCHE",
        "F : FAROS",
        "M : MINIMAPA",
        "R : REINICIAR",
        "P : PAUSA",
        "H : AYUDA",
        "ESC : SALIR",
    };

    private static final String[] OPCIONES = {"REANUDAR", "REINICIAR", "SALIR"};

    private final Vertices[] grupos = new Vertices[GRUPOS];
    private MallaDinamica malla;

    private boolean ayuda = false;
    private boolean pausado = false;
    private int opcion = 0;

    public Interfaz() {
        for (int i = 0; i < GRUPOS; i++) {
            grupos[i] = new Vertices();
        }
    }

    public void iniciar() {
        malla = new MallaDinamica();
    }

    public void limpiar() {
        if (malla != null) {
            malla.eliminar();
        }
    }

    public boolean ayuda() {
        return ayuda;
    }

    public boolean pausado() {
        return pausado;
    }

    public int opcion() {
        return opcion;
    }

    public void alternarAyuda() {
        ayuda = !ayuda;
    }

    public void alternarPausa() {
        pausado = !pausado;
        opcion = 0;
    }

    public void reanudar() {
        pausado = false;
    }

    public void mover(int delta) {
        opcion = Math.floorMod(opcion + delta, OPCIONES.length);
    }

    public void dibujar(ProgramaShader shader, int ancho, int alto,
                        int velocidad, boolean faros, boolean dia, String sector) {
        for (Vertices grupo : grupos) {
            grupo.limpiar();
        }

        panel(velocidad, faros, dia, sector);
        if (ayuda) {
            ayuda(ancho, alto);
        }
        if (pausado) {
            menu(ancho, alto);
        }

        shader.setInt("uMapa", 3);
        shader.setInt("uIluminado", 0);
        shader.setVec2("uPantalla", ancho, alto);
        shader.setVec3("uPos", 0f, 0f, 0f);
        shader.setVec3("uEscala", 1f, 1f, 1f);
        shader.setFloat("uGiro", 0f);
        shader.setFloat("uEmision", 0f);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i < GRUPOS; i++) {
            if (grupos[i].vacio()) {
                continue;
            }
            float[] color = COLORES[i];
            shader.setVec3("uColor", color[0], color[1], color[2]);
            shader.setFloat("uAlfa", color[3]);
            malla.actualizar(grupos[i].aArreglo());
            malla.dibujar();
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        shader.setFloat("uAlfa", 1f);
        shader.setInt("uMapa", 0);
        shader.setInt("uIluminado", 1);
    }

    /** Panel superior izquierdo con velocidad y estado de luces. */
    private void panel(int velocidad, boolean faros, boolean dia, String sector) {
        float x = 16f;
        float y = 16f;
        float ancho = 272f;
        float alto = 122f;
        rect(FONDO, x, y, ancho, alto);
        Fuente.escribir2D("VEHICULO", x + 12f, y + 10f, 2.4f, grupos[TITULO]);

        float etiquetaX = x + 12f;
        float valorX = x + 150f;
        float fila = y + 40f;
        float escala = 2f;

        etiqueta("VELOCIDAD", etiquetaX, fila, escala);
        Fuente.escribir2D(velocidad + " KM/H", valorX, fila, escala, grupos[TEXTO]);
        fila += 20f;

        etiqueta("FAROS", etiquetaX, fila, escala);
        Fuente.escribir2D(faros ? "ON" : "OFF", valorX, fila, escala, grupos[faros ? OK : ALERTA]);
        fila += 20f;

        etiqueta("MODO", etiquetaX, fila, escala);
        Fuente.escribir2D(dia ? "DIA" : "NOCHE", valorX, fila, escala, grupos[dia ? TITULO : SELECCION]);
        fila += 20f;

        etiqueta("SECTOR", etiquetaX, fila, escala);
        Fuente.escribir2D(sector, valorX, fila, escala, grupos[TEXTO]);
    }

    private void ayuda(int ancho, int alto) {
        float escala = 2f;
        float anchoCaja = 540f;
        float altoCaja = 52f + CONTROLES.length * 22f;
        float x = (ancho - anchoCaja) / 2f;
        float y = (alto - altoCaja) / 2f;
        rect(FONDO, x, y, anchoCaja, altoCaja);
        Fuente.escribir2D("CONTROLES", x + 20f, y + 14f, 2.6f, grupos[TITULO]);

        float fila = y + 48f;
        for (String control : CONTROLES) {
            Fuente.escribir2D(control, x + 24f, fila, escala, grupos[TEXTO]);
            fila += 22f;
        }
    }

    private void menu(int ancho, int alto) {
        float escala = 2.6f;
        float anchoCaja = 360f;
        float altoCaja = 208f;
        float x = (ancho - anchoCaja) / 2f;
        float y = (alto - altoCaja) / 2f;
        rect(FONDO, x, y, anchoCaja, altoCaja);
        Fuente.escribir2D("PAUSA", x + 24f, y + 18f, 3.2f, grupos[TITULO]);

        float fila = y + 80f;
        for (int i = 0; i < OPCIONES.length; i++) {
            boolean seleccionada = i == opcion;
            if (seleccionada) {
                Fuente.escribir2D(">", x + 30f, fila, escala, grupos[SELECCION]);
            }
            Fuente.escribir2D(OPCIONES[i], x + 56f, fila, escala,
                    grupos[seleccionada ? SELECCION : TEXTO]);
            fila += 34f;
        }
    }

    private void etiqueta(String texto, float x, float y, float escala) {
        Fuente.escribir2D(texto, x, y, escala, grupos[ETIQUETA]);
    }

    private void rect(int grupo, float x, float y, float ancho, float alto) {
        Vertices salida = grupos[grupo];
        salida.agregar(x, y, 0f, 0f, 0f, 1f);
        salida.agregar(x + ancho, y, 0f, 0f, 0f, 1f);
        salida.agregar(x + ancho, y + alto, 0f, 0f, 0f, 1f);
        salida.agregar(x, y, 0f, 0f, 0f, 1f);
        salida.agregar(x + ancho, y + alto, 0f, 0f, 0f, 1f);
        salida.agregar(x, y + alto, 0f, 0f, 0f, 1f);
    }
}
