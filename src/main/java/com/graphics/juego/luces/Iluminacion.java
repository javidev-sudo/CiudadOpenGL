package com.graphics.juego.luces;

import java.util.ArrayList;
import java.util.List;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.objetos.Ciudad;
import com.graphics.juego.objetos.Farola;

/**
 * Estado global de iluminación: modo día/noche, luz ambiental, luz direccional
 * (sol/luna) y las farolas de la ciudad. Sube sus valores al shader cada cuadro.
 */
public final class Iluminacion {

    /** Debe coincidir con MAX_FAROLAS del shader. */
    private static final int MAX_FAROLAS = 20;

    /** Distancia desde el centro de la manzana hasta su esquina. */
    private static final float ESQUINA = 4.5f;

    /** Dirección (unitaria) hacia el sol de día y hacia la luna de noche. */
    private static final float[] CUERPO_DIA = {0.70f, 0.38f, 0.61f};
    private static final float[] CUERPO_NOCHE = {-0.68f, 0.38f, -0.63f};

    private boolean modoDia = true;
    private final List<Farola> farolas = new ArrayList<>();

    public Iluminacion() {
        // Dos farolas por parque, en esquinas opuestas en diagonal.
        for (int fila = 0; fila < Ciudad.MAPA.length; fila++) {
            for (int columna = 0; columna < Ciudad.MAPA[fila].length; columna++) {
                if (Ciudad.MAPA[fila][columna] == 2) {
                    float x = Ciudad.centro(columna);
                    float z = Ciudad.centro(fila);
                    farolas.add(new Farola(x + ESQUINA, z + ESQUINA, 1.0f, 0.85f, 0.55f));
                    farolas.add(new Farola(x - ESQUINA, z - ESQUINA, 1.0f, 0.85f, 0.55f));
                }
            }
        }
    }

    public void alternarModo() {
        modoDia = !modoDia;
    }

    public boolean esDia() {
        return modoDia;
    }

    /** Color base del cielo (fondo) según el modo. */
    public float[] colorCielo() {
        return modoDia
                ? new float[] {0.45f, 0.62f, 0.85f}
                : new float[] {0.02f, 0.03f, 0.08f};
    }

    /** Dirección (unitaria) hacia el sol o la luna según el modo. */
    public float[] direccionCuerpo() {
        return modoDia ? CUERPO_DIA : CUERPO_NOCHE;
    }

    /** Envía ambiente, direccional y farolas al programa de shaders. */
    public void enviarA(ProgramaShader shader) {
        if (modoDia) {
            shader.setVec3("uAmbiente", 0.35f, 0.37f, 0.40f);
            shader.setVec3("uSolColor", 0.95f, 0.90f, 0.78f);
        } else {
            shader.setVec3("uAmbiente", 0.015f, 0.018f, 0.035f);
            shader.setVec3("uSolColor", 0.06f, 0.07f, 0.12f);
        }

        // La luz direccional viaja en sentido opuesto al cuerpo celeste.
        float[] cuerpo = direccionCuerpo();
        shader.setVec3("uSolDireccion", -cuerpo[0], -cuerpo[1], -cuerpo[2]);

        float[] posiciones = new float[MAX_FAROLAS * 3];
        float[] colores = new float[MAX_FAROLAS * 3];
        int total = Math.min(farolas.size(), MAX_FAROLAS);
        for (int i = 0; i < total; i++) {
            farolas.get(i).escribir(posiciones, colores, i);
        }

        // Las farolas solo aportan luz de noche.
        shader.setInt("uNumFarolas", modoDia ? 0 : total);
        shader.setVec3Array("uFarolaPos", posiciones);
        shader.setVec3Array("uFarolaColor", colores);
        shader.setVec3("uFarolaDir", 0.0f, -1.0f, 0.0f);
        shader.setFloat("uFarolaAlcance", 36.0f);
    }

    /** Dibuja postes y bombillas; las bombillas emiten solo de noche. */
    public void dibujarFarolas(PintorCajas pintor) {
        float emision = modoDia ? 0.0f : 1.0f;
        for (Farola farola : farolas) {
            farola.dibujar(pintor, emision);
        }
    }
}
