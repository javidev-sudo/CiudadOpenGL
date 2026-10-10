package com.graphics.juego.gfx;

import java.util.Arrays;

/**
 * Constructor de vértices en memoria (crece solo). Acumula flotantes con el
 * formato posición (3) + normal (3) usado por las mallas del proyecto, evitando
 * el costo de autoboxing al generar geometría de interfaz o texto.
 */
public final class Vertices {

    private float[] datos = new float[1024];
    private int cantidad = 0;

    /** Agrega un vértice completo: posición XYZ y normal XYZ. */
    public void agregar(float x, float y, float z, float nx, float ny, float nz) {
        asegurar(6);
        datos[cantidad++] = x;
        datos[cantidad++] = y;
        datos[cantidad++] = z;
        datos[cantidad++] = nx;
        datos[cantidad++] = ny;
        datos[cantidad++] = nz;
    }

    public void limpiar() {
        cantidad = 0;
    }

    public boolean vacio() {
        return cantidad == 0;
    }

    public int longitud() {
        return cantidad;
    }

    /** Copia exacta de los flotantes acumulados, lista para subir a la GPU. */
    public float[] aArreglo() {
        return Arrays.copyOf(datos, cantidad);
    }

    private void asegurar(int extra) {
        if (cantidad + extra > datos.length) {
            int nuevo = Math.max(datos.length * 2, cantidad + extra);
            datos = Arrays.copyOf(datos, nuevo);
        }
    }
}
