package com.graphics.juego.funciones;

import java.util.ArrayList;
import java.util.List;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.gfx.ProgramaShader;

/**
 * Conjunto de carteles y vallas publicitarias de la ciudad. Incluye un cartel
 * de entrada por sector y algunas vallas. La geometría del texto se genera en
 * {@link #iniciar()} porque necesita el contexto OpenGL.
 */
public final class Carteles {

    private final List<Cartel> lista = new ArrayList<>();

    public Carteles() {
        // Carteles de sector en parques de cada cuadrante. El ancho (9) cabe
        // dentro de la celda del parque (10) con margen a cada lado.
        lista.add(new Cartel(-40f, -23.5f, 9f, 1.8f, 3.0f, Cartel.Direccion.SUR,
                "NOROESTE", 0.16f, 0.20f, 0.35f, 0.55f, 0.90f, 0.94f, 1.00f));
        lista.add(new Cartel(40f, -43.5f, 9f, 1.8f, 3.0f, Cartel.Direccion.SUR,
                "NORESTE", 0.16f, 0.20f, 0.45f, 0.40f, 0.90f, 0.94f, 1.00f));
        lista.add(new Cartel(-40f, 36.5f, 9f, 1.8f, 3.0f, Cartel.Direccion.SUR,
                "SUROESTE", 0.16f, 0.50f, 0.35f, 0.20f, 0.90f, 0.94f, 1.00f));
        lista.add(new Cartel(40f, 36.5f, 9f, 1.8f, 3.0f, Cartel.Direccion.SUR,
                "SURESTE", 0.16f, 0.55f, 0.25f, 0.35f, 0.90f, 0.94f, 1.00f));

        // Vallas publicitarias en parques céntricos, también dentro de la celda.
        lista.add(new Cartel(0f, -43.5f, 9f, 1.8f, 3.5f, Cartel.Direccion.SUR,
                "PROG.GRAFICA", 0.10f, 0.15f, 0.15f, 0.18f, 1.00f, 0.82f, 0.30f));
        lista.add(new Cartel(20f, -3.5f, 9f, 1.8f, 3.5f, Cartel.Direccion.SUR,
                "BIENVENIDO", 0.10f, 0.15f, 0.15f, 0.18f, 0.30f, 0.95f, 0.45f));
        lista.add(new Cartel(0f, 16.5f, 9f, 1.8f, 3.5f, Cartel.Direccion.SUR,
                "PARQUE CENTRAL", 0.10f, 0.15f, 0.15f, 0.18f, 0.30f, 0.75f, 1.00f));
    }

    public void iniciar() {
        for (Cartel cartel : lista) {
            cartel.iniciar();
        }
    }

    public void dibujar(PintorCajas pintor, ProgramaShader shader) {
        for (Cartel cartel : lista) {
            cartel.dibujar(pintor);
        }
        for (Cartel cartel : lista) {
            cartel.dibujarTexto(shader);
        }
    }

    public void limpiar() {
        for (Cartel cartel : lista) {
            cartel.limpiar();
        }
    }
}
