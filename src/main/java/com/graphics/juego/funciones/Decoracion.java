package com.graphics.juego.funciones;

import java.util.ArrayList;
import java.util.List;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.objetos.Arbol;
import com.graphics.juego.objetos.Banco;
import com.graphics.juego.objetos.Ciudad;

/**
 * Decoración urbana: árboles y bancos en los parques y pasos peatonales junto a
 * las manzanas. Las ventanas de los edificios las dibuja cada {@code Edificio}.
 */
public final class Decoracion {

    private static final int PARQUE = 2;

    private final List<Arbol> arboles = new ArrayList<>();
    private final List<Banco> bancos = new ArrayList<>();

    public Decoracion() {
        for (int fila = 0; fila < Ciudad.MAPA.length; fila++) {
            for (int columna = 0; columna < Ciudad.MAPA[fila].length; columna++) {
                if (Ciudad.MAPA[fila][columna] != PARQUE) {
                    continue;
                }
                float x = Ciudad.centro(columna);
                float z = Ciudad.centro(fila);
                arboles.add(new Arbol(x - 2.5f, z - 2.5f, 1.0f));
                arboles.add(new Arbol(x + 2.5f, z - 2.0f, 0.85f));
                arboles.add(new Arbol(x - 1.5f, z + 2.5f, 1.1f));
                bancos.add(new Banco(x + 2.0f, z + 2.5f));
            }
        }
    }

    /** Cruces peatonales que se conservan aunque no tengan semáforo encima. */
    private static final float[][] CRUCES_SIN_SEMAFORO = {
        {40f, 10f},
        {10f, -20f},
    };

    public void dibujar(PintorCajas pintor, Semaforos semaforos) {
        dibujarPasos(pintor, semaforos);
        for (Arbol arbol : arboles) {
            arbol.dibujar(pintor);
        }
        for (Banco banco : bancos) {
            banco.dibujar(pintor);
        }
    }

    private void dibujarPasos(PintorCajas pintor, Semaforos semaforos) {
        for (int fila = 0; fila < Ciudad.MAPA.length; fila++) {
            for (int columna = 0; columna < Ciudad.MAPA[fila].length; columna++) {
                if (Ciudad.MAPA[fila][columna] == 0) {
                    continue;
                }
                float x = Ciudad.centro(columna);
                float z = Ciudad.centro(fila);

                // Calle horizontal al norte: franjas que cruzan la calle en Z.
                // Se omite cuando la calle cae en el borde del mapa.
                int calleNorte = fila - 1;
                if (calleNorte > 0 && Ciudad.MAPA[calleNorte][columna] == 0
                        && seDibujaCruce(x, z - Ciudad.CELDA, semaforos)) {
                    dibujarCruce(pintor, x, z - Ciudad.CELDA, true);
                }
                // Calle vertical al oeste: franjas que cruzan la calle en X.
                int calleOeste = columna - 1;
                if (calleOeste > 0 && Ciudad.MAPA[fila][calleOeste] == 0
                        && seDibujaCruce(x - Ciudad.CELDA, z, semaforos)) {
                    dibujarCruce(pintor, x - Ciudad.CELDA, z, false);
                }
            }
        }
    }

    /** Solo se pinta la cebra si tiene un semáforo encima, o si es una de las excepciones. */
    private boolean seDibujaCruce(float x, float z, Semaforos semaforos) {
        if (semaforos.hayEn(x, z)) {
            return true;
        }
        for (float[] cruce : CRUCES_SIN_SEMAFORO) {
            if (Math.abs(cruce[0] - x) < 0.01f && Math.abs(cruce[1] - z) < 0.01f) {
                return true;
            }
        }
        return false;
    }

    /** Cruce peatonal tipo cebra, centrado en la calle (cx, cz) y sobre el asfalto.
     *  Las franjas van en el sentido de circulación (verticales desde el auto). */
    private void dibujarCruce(PintorCajas pintor, float cx, float cz, boolean calleHorizontal) {
        int franjas = 7;
        float grosor = 0.55f;
        float largo = 2.6f;
        float separacion = 1.15f;
        for (int i = 0; i < franjas; i++) {
            float d = (i - (franjas - 1) / 2.0f) * separacion;
            if (calleHorizontal) {
                // La calle corre en X: franjas alargadas en X, repetidas a lo ancho (Z).
                pintor.caja(cx, 0.06f, cz + d, largo, 0.04f, grosor, 0.96f, 0.96f, 0.96f);
            } else {
                // La calle corre en Z: franjas alargadas en Z, repetidas a lo ancho (X).
                pintor.caja(cx + d, 0.06f, cz, grosor, 0.04f, largo, 0.96f, 0.96f, 0.96f);
            }
        }
    }
}
