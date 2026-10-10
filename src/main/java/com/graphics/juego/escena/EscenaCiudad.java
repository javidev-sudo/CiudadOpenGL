package com.graphics.juego.escena;

import com.graphics.juego.core.Escena;
import com.graphics.juego.funciones.Carteles;
import com.graphics.juego.funciones.Cielo;
import com.graphics.juego.funciones.Decoracion;
import com.graphics.juego.funciones.GestorEntregas;
import com.graphics.juego.funciones.Interfaz;
import com.graphics.juego.funciones.Minimapa;
import com.graphics.juego.funciones.Sectores;
import com.graphics.juego.funciones.Semaforos;
import com.graphics.juego.gfx.Entrada;
import com.graphics.juego.gfx.PintorCajas;
import com.graphics.juego.gfx.ProgramaShader;
import com.graphics.juego.gfx.Renderer;
import com.graphics.juego.gfx.Ventana;
import com.graphics.juego.luces.FarosAuto;
import com.graphics.juego.luces.Iluminacion;
import com.graphics.juego.objetos.Ciudad;
import com.graphics.juego.objetos.Vehiculo;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Escena de la ciudad: raíz de composición que conecta ventana, entrada, shader,
 * pintor, ciudad, vehículo, cámara, iluminación y las funciones adicionales.
 */
public final class EscenaCiudad implements Escena {

    private final Ventana ventana;
    private final Entrada entrada;
    private final Renderer renderer = new Renderer();
    private final Ciudad ciudad = new Ciudad();
    private final Vehiculo vehiculo = new Vehiculo();
    private final CamaraCiudad camara = new CamaraCiudad(vehiculo);
    private final Iluminacion iluminacion = new Iluminacion();
    private final FarosAuto faros = new FarosAuto();
    private final GestorEntregas entregas = new GestorEntregas();
    private final Cielo cielo = new Cielo();
    private final Decoracion decoracion = new Decoracion();
    private final Semaforos semaforos = new Semaforos();
    private final Minimapa minimapa = new Minimapa();
    private final Carteles carteles = new Carteles();
    private final Interfaz interfaz = new Interfaz();

    private ProgramaShader shader;
    private PintorCajas pintor;

    public EscenaCiudad(Ventana ventana) {
        this.ventana = ventana;
        this.entrada = new Entrada(ventana);
    }

    @Override
    public void iniciar() {
        renderer.habilitarProfundidad();
        shader = new ProgramaShader(Shaders.vertex(), Shaders.fragment());
        pintor = new PintorCajas(shader);
        carteles.iniciar();
        interfaz.iniciar();
        entrada.alPresionar(this::tecla);
    }

    @Override
    public void actualizar(float deltaTime) {
        if (!interfaz.pausado()) {
            camara.actualizar(deltaTime, entrada);
            vehiculo.actualizar(deltaTime, ciudad, entrada);
            entregas.actualizar(deltaTime, vehiculo);
            semaforos.actualizar(deltaTime);
        }

        String progreso = entregas.completado()
                ? "entregas completadas"
                : "entrega " + (entregas.completadas() + 1) + "/" + entregas.total();

        ventana.titulo("Ciudad 11x11 | WASD: conducir | Espacio: freno | C: camara ("
                + nombreCamara() + ") | N: "
                + (iluminacion.esDia() ? "dia" : "noche")
                + " | F: faros " + (faros.encendidos() ? "ON" : "off")
                + " | M: mapa " + (minimapa.visible() ? "ON" : "off")
                + " | R: reiniciar | " + progreso
                + " | " + vehiculo.kilometrosPorHora() + " km/h");
    }

    private String nombreCamara() {
        return switch (camara.modo()) {
            case PERSEGUIR -> "3a persona";
            case PRIMERA_PERSONA -> "1a persona";
            case AEREA -> "aerea";
        };
    }

    @Override
    public void dibujar() {
        int ancho = ventana.ancho();
        int alto = ventana.alto();

        renderer.viewport(ancho, alto);
        float[] fondo = iluminacion.colorCielo();
        renderer.limpiar(fondo[0], fondo[1], fondo[2]);
        shader.usar();
        shader.setFloat("uAlfa", 1.0f);

        // Cuerpos celestes como geometría 3D; respetan perspectiva y oclusión.
        shader.setInt("uMapa", 2);
        shader.setInt("uIluminado", 0);
        camara.aplicar(shader, ancho, alto);
        pintor.preparar();
        cielo.dibujar(pintor, !iluminacion.esDia(), iluminacion.direccionCuerpo());

        shader.setInt("uMapa", 0);
        shader.setInt("uIluminado", 1);
        iluminacion.enviarA(shader);
        faros.enviarA(shader, vehiculo);
        camara.aplicar(shader, ancho, alto);

        pintor.preparar();
        ciudad.dibujar(pintor, !iluminacion.esDia());
        decoracion.dibujar(pintor, semaforos);
        iluminacion.dibujarFarolas(pintor);
        carteles.dibujar(pintor, shader);
        pintor.preparar();
        semaforos.dibujar(pintor);
        entregas.dibujar(pintor);
        vehiculo.dibujar(pintor);

        renderer.verificarError();

        minimapa.dibujar(renderer, shader, pintor, ciudad, vehiculo, entregas.destinoActivo(), ancho, alto);

        interfaz.dibujar(shader, ancho, alto, vehiculo.kilometrosPorHora(),
                faros.encendidos(), iluminacion.esDia(),
                Sectores.nombre(vehiculo.autoX(), vehiculo.autoZ()));
    }

    @Override
    public void tecla(int key) {
        if (key == GLFW_KEY_ESCAPE) {
            ventana.solicitarCierre();
            return;
        }
        if (key == GLFW_KEY_H) {
            interfaz.alternarAyuda();
            return;
        }
        if (key == GLFW_KEY_P) {
            interfaz.alternarPausa();
            return;
        }
        if (interfaz.pausado()) {
            if (key == GLFW_KEY_UP) {
                interfaz.mover(-1);
            }
            if (key == GLFW_KEY_DOWN) {
                interfaz.mover(1);
            }
            if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
                seleccionarOpcion();
            }
            return;
        }
        if (key == GLFW_KEY_N) {
            iluminacion.alternarModo();
            if (iluminacion.esDia()) {
                faros.apagar();
            } else {
                faros.encender();
            }
        }
        if (key == GLFW_KEY_F) {
            faros.alternar();
        }
        if (key == GLFW_KEY_M) {
            minimapa.alternar();
        }
        if (key == GLFW_KEY_C) {
            camara.ciclar();
        }
        if (key == GLFW_KEY_R) {
            vehiculo.reiniciar();
            entregas.reiniciar();
        }
        vehiculo.tecla(key);
    }

    /** Ejecuta la opción elegida en el menú de pausa. */
    private void seleccionarOpcion() {
        switch (interfaz.opcion()) {
            case 0 -> interfaz.reanudar();
            case 1 -> {
                vehiculo.reiniciar();
                entregas.reiniciar();
                interfaz.reanudar();
            }
            default -> ventana.solicitarCierre();
        }
    }

    @Override
    public void limpiar() {
        carteles.limpiar();
        interfaz.limpiar();
        if (pintor != null) {
            pintor.eliminar();
        }
        if (shader != null) {
            shader.eliminar();
        }
    }
}
