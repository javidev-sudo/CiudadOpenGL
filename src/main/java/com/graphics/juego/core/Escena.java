package com.graphics.juego.core;

/**
 * Abstracción de una escena ejecutable por {@link Aplicacion}.
 */
public interface Escena {

    /** Prepara los recursos de la escena. El contexto OpenGL ya debe estar activo. */
    void iniciar();

    /** Actualiza el estado con los segundos transcurridos entre cuadros. */
    void actualizar(float deltaTime);

    /** Dibuja un cuadro completo de la escena. */
    void dibujar();

    /** Atiende una tecla recién presionada. */
    void tecla(int key);

    /** Libera los recursos de la escena. */
    default void limpiar() {
    }
}
