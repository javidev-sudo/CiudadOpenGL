package com.graphics.juego.funciones;

import java.util.ArrayList;
import java.util.List;

import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.objetos.Semaforo;

/**
 * Conjunto de semáforos de la ciudad. Cada pórtico se coloca alineado con un
 * paso peatonal (cebra), de modo que el travesaño cruza la calle justo por
 * encima de la franja. No se ponen en todas las calles: se reparten en una
 * selección de pasos, alternando el eje según la calle que cruzan.
 */
public final class Semaforos {

    private final List<Semaforo> lista = new ArrayList<>();

    public Semaforos() {
        // Repartidos para que ninguno quede cerca de otro (separación mínima ~32).
        // Calles horizontales (la calle corre en X): el pórtico cruza en Z.
        lista.add(new Semaforo(-40, -30, Semaforo.Eje.Z));
        lista.add(new Semaforo(40, -30, Semaforo.Eje.Z));
        lista.add(new Semaforo(-40, 30, Semaforo.Eje.Z));
        lista.add(new Semaforo(40, 30, Semaforo.Eje.Z));
        lista.add(new Semaforo(0, 10, Semaforo.Eje.Z));

        // Calles verticales (la calle corre en Z): el pórtico cruza en X.
        lista.add(new Semaforo(-10, -40, Semaforo.Eje.X));
        lista.add(new Semaforo(10, 40, Semaforo.Eje.X));
        lista.add(new Semaforo(-30, 0, Semaforo.Eje.X));
        lista.add(new Semaforo(30, 0, Semaforo.Eje.X));
    }

    /** Indica si hay un semáforo en la posición (x, z); sirve para alinear las cebras. */
    public boolean hayEn(float x, float z) {
        for (Semaforo semaforo : lista) {
            if (Math.abs(semaforo.x() - x) < 0.01f && Math.abs(semaforo.z() - z) < 0.01f) {
                return true;
            }
        }
        return false;
    }

    public void actualizar(float deltaTime) {
        for (Semaforo semaforo : lista) {
            semaforo.actualizar(deltaTime);
        }
    }

    public void dibujar(PintorCajas pintor) {
        for (Semaforo semaforo : lista) {
            semaforo.dibujar(pintor);
        }
    }
}
