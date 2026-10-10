package com.graphics.juego.funciones;

/**
 * Sectores de la ciudad: divide el mapa en cuatro cuadrantes según la posición
 * y devuelve el nombre legible del sector en el que se encuentra el vehículo.
 */
public final class Sectores {

    private Sectores() {
    }

    public static String nombre(float x, float z) {
        if (x < 0) {
            return z < 0 ? "NOROESTE" : "SUROESTE";
        }
        return z < 0 ? "NORESTE" : "SURESTE";
    }
}
