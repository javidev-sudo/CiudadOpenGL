package com.graphics.juego.objetos;

import java.util.ArrayList;
import java.util.List;

import com.graphics.juego.gfx.PintorCajas;

/**
 * Modelo de la ciudad ampliada (11×11): el mapa, su geometría y las consultas
 * de colisión. Los edificios se delegan a objetos {@link Edificio} independientes.
 */
public final class Ciudad {

    public static final float LIMITE = 55;
    public static final float CELDA = 10;

    /** Matriz del mapa: 0 = calle, 1 = edificio, 2 = parque (16 edificios, 9 parques). */
    public static final int[][] MAPA = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0, 2, 0, 1, 0, 2, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 2, 0, 1, 0, 1, 0, 1, 0, 2, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0, 2, 0, 1, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 2, 0, 1, 0, 1, 0, 1, 0, 2, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    private final List<Edificio> edificios = new ArrayList<>();

    public Ciudad() {
        for (int fila = 0; fila < MAPA.length; fila++) {
            for (int columna = 0; columna < MAPA[fila].length; columna++) {
                if (MAPA[fila][columna] == 1) {
                    edificios.add(crearEdificio(fila, columna));
                }
            }
        }
    }

    /** Convierte el índice de una fila o columna al centro de su celda. */
    public static float centro(int indice) {
        return -LIMITE + CELDA * (indice + 0.5f);
    }

    /** Tipo de edificio según su celda: 3 rascacielos y mezcla de pequeños/medianos. */
    public static Edificio.Tipo tipoEdificio(int fila, int columna) {
        if ((fila == 1 && columna == 3) || (fila == 7 && columna == 7) || (fila == 5 && columna == 1)) {
            return Edificio.Tipo.RASCACIELOS;
        }
        if (((fila + columna) / 2) % 3 == 0) {
            return Edificio.Tipo.PEQUENO;
        }
        return Edificio.Tipo.MEDIANO;
    }

    private Edificio crearEdificio(int fila, int columna) {
        Edificio.Tipo tipo = tipoEdificio(fila, columna);
        float altura;
        float rojo;
        float verde;
        float azul;

        if (tipo == Edificio.Tipo.RASCACIELOS) {
            altura = 30 + (fila * 3 + columna * 7) % 16;
            rojo = 0.30f;
            verde = 0.45f;
            azul = 0.65f;
        } else if (tipo == Edificio.Tipo.PEQUENO) {
            altura = 4 + (fila + columna) % 3;
            rojo = 0.45f + columna * 0.03f;
            verde = 0.42f;
            azul = 0.30f + fila * 0.02f;
        } else {
            altura = 8 + (fila * 3 + columna * 7) % 7;
            rojo = 0.28f + columna * 0.045f;
            verde = 0.40f;
            azul = 0.48f + fila * 0.025f;
        }

        return new Edificio(centro(columna), centro(fila), tipo, altura, rojo, verde, azul);
    }

    /** Dibuja el suelo, las calles, los parques y luego cada edificio. */
    public void dibujar(PintorCajas pintor, boolean noche) {
        pintor.caja(0, -0.25f, 0, 110, 0.5f, 110, 0.16f, 0.19f, 0.23f);
        for (int fila = 0; fila < MAPA.length; fila++) {
            for (int columna = 0; columna < MAPA[fila].length; columna++) {
                float x = centro(columna);
                float z = centro(fila);
                int tipo = MAPA[fila][columna];
                if (tipo == 0) {
                    dibujarMarcasCalle(pintor, fila, columna, x, z);
                } else if (tipo == 2) {
                    pintor.caja(x, 0.15f, z, 10, 0.3f, 10, 0.60f, 0.64f, 0.66f);
                    pintor.caja(x, 0.32f, z, 9, 0.1f, 9, 0.20f, 0.45f, 0.28f);
                }
            }
        }
        for (Edificio edificio : edificios) {
            edificio.dibujar(pintor, noche);
        }
    }

    /** Dibuja las líneas discontinuas de las calles dejando los cruces despejados. */
    private void dibujarMarcasCalle(PintorCajas pintor, int fila, int columna, float x, float z) {
        if (fila % 2 == 0 && columna % 2 == 1) {
            for (int desplazamiento = -3; desplazamiento <= 3; desplazamiento += 3) {
                pintor.caja(x + desplazamiento, 0.025f, z, 1.6f, 0.03f, 0.13f, 1, 0.84f, 0.35f);
            }
        }
        if (columna % 2 == 0 && fila % 2 == 1) {
            for (int desplazamiento = -3; desplazamiento <= 3; desplazamiento += 3) {
                pintor.caja(x, 0.025f, z + desplazamiento, 0.13f, 0.03f, 1.6f, 1, 0.84f, 0.35f);
            }
        }
    }

    /** Comprueba si un círculo de radio dado cabe sin tocar manzanas ni bordes. */
    public boolean puedeCircular(float x, float z, float radio) {
        float limitePermitido = LIMITE - radio;
        if (Math.abs(x) > limitePermitido || Math.abs(z) > limitePermitido) {
            return false;
        }

        for (int fila = 0; fila < MAPA.length; fila++) {
            for (int columna = 0; columna < MAPA[fila].length; columna++) {
                if (MAPA[fila][columna] == 0) {
                    continue;
                }
                float centroX = centro(columna);
                float centroZ = centro(fila);
                float cercaX = Math.max(centroX - 5, Math.min(x, centroX + 5));
                float cercaZ = Math.max(centroZ - 5, Math.min(z, centroZ + 5));
                float distanciaX = x - cercaX;
                float distanciaZ = z - cercaZ;
                float distanciaCuadrada = distanciaX * distanciaX + distanciaZ * distanciaZ;
                if (distanciaCuadrada < radio * radio) {
                    return false;
                }
            }
        }
        return true;
    }
}
