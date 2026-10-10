package com.graphics.juego.escena;

/**
 * Código GLSL de la escena de la ciudad. El vertex shader ya entrega
 * posición de mundo y normal, base para la iluminación de fases posteriores.
 */
public final class Shaders {

    private Shaders() {
    }

    public static String vertex() {
        return """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec3 aNormal;
            uniform vec3 uPos;
            uniform vec3 uEscala;
            uniform vec3 uOjo;
            uniform vec3 uObjetivo;
            uniform float uGiro;
            uniform float uAspecto;
            uniform int uMapa;
            uniform vec2 uPantalla;
            out vec3 vMundo;
            out vec3 vNormal;

            void main() {
                float coseno = cos(uGiro);
                float seno = sin(uGiro);
                mat3 giro = mat3(
                    coseno, 0.0, -seno,
                    0.0, 1.0, 0.0,
                    seno, 0.0, coseno
                );
                vMundo = giro * (aPos * uEscala) + uPos;
                vNormal = normalize(giro * (aNormal / uEscala));

                if (uMapa == 3) {
                    // Interfaz: las coordenadas ya vienen en píxeles (origen
                    // arriba-izquierda) y se llevan directo a clip-space.
                    gl_Position = vec4(
                        (vMundo.x / uPantalla.x) * 2.0 - 1.0,
                        1.0 - (vMundo.y / uPantalla.y) * 2.0,
                        0.0,
                        1.0);
                } else if (uMapa == 1) {
                    float pantallaX = vMundo.x / 57.0;
                    float pantallaY = -vMundo.z / 57.0;
                    float profundidad = -vMundo.y / 100.0;
                    gl_Position = vec4(pantallaX, pantallaY, profundidad, 1.0);
                } else {
                    vec3 mundo = vMundo;
                    if (uMapa == 2) {
                        // Cuerpos celestes: anclados a la camara para simular una
                        // distancia "infinita" (sin paralaje) y respetar la perspectiva.
                        mundo += uOjo;
                        vMundo = mundo;
                    }
                    vec3 frente = normalize(uObjetivo - uOjo);
                    vec3 derecha = normalize(cross(frente, vec3(0.0, 1.0, 0.0)));
                    vec3 arriba = cross(derecha, frente);
                    vec3 diferencia = mundo - uOjo;
                    float vistaX = dot(diferencia, derecha);
                    float vistaY = dot(diferencia, arriba);
                    float vistaZ = -dot(diferencia, frente);
                    float factor = 1.0 / tan(radians(55.0) * 0.5);
                    float cerca = 0.1;
                    float lejos = 250.0;
                    float clipX = vistaX * factor / uAspecto;
                    float clipY = vistaY * factor;
                    float clipZ = (lejos + cerca) / (cerca - lejos) * vistaZ;
                    clipZ += 2.0 * lejos * cerca / (cerca - lejos);
                    gl_Position = vec4(clipX, clipY, clipZ, -vistaZ);
                }
            }
            """;
    }

    public static String fragment() {
        return """
            #version 330 core
            in vec3 vMundo;
            in vec3 vNormal;

            uniform vec3 uColor;
            uniform float uEmision;
            uniform float uAlfa;
            uniform int uIluminado;

            uniform vec3 uAmbiente;
            uniform vec3 uSolDireccion;
            uniform vec3 uSolColor;

            const int MAX_FAROLAS = 20;
            uniform int uNumFarolas;
            uniform vec3 uFarolaPos[MAX_FAROLAS];
            uniform vec3 uFarolaColor[MAX_FAROLAS];
            uniform vec3 uFarolaDir;
            uniform float uFarolaAlcance;

            uniform vec3 uFarosPos[2];
            uniform vec3 uFarosDir[2];
            uniform vec3 uFarosColor;
            uniform int uFarosActivos;
            uniform float uFaroAlcance;

            out vec4 color;

            void main() {
                if (uIluminado == 0) {
                    color = vec4(uColor, uAlfa);
                    return;
                }

                vec3 N = normalize(vNormal);
                vec3 luz = uAmbiente;

                luz += uSolColor * max(dot(N, -normalize(uSolDireccion)), 0.0);

                for (int i = 0; i < MAX_FAROLAS; i++) {
                    if (i < uNumFarolas) {
                        vec3 L = uFarolaPos[i] - vMundo;
                        float d = length(L);
                        if (d < uFarolaAlcance) {
                            vec3 Ld = normalize(L);
                            float cosAng = dot(-Ld, normalize(uFarolaDir));
                            float cono = smoothstep(0.35, 0.75, cosAng);
                            float aten = clamp(1.0 - d / uFarolaAlcance, 0.0, 1.0);
                            luz += uFarolaColor[i] * cono * aten * max(dot(N, Ld), 0.0);
                        }
                    }
                }

                if (uFarosActivos == 1) {
                    for (int j = 0; j < 2; j++) {
                        vec3 L = uFarosPos[j] - vMundo;
                        float d = length(L);
                        if (d < uFaroAlcance) {
                            vec3 Ld = normalize(L);
                            float cosAng = dot(-Ld, normalize(uFarosDir[j]));
                            float cono = smoothstep(0.86, 0.95, cosAng);
                            float aten = clamp(1.0 - d / uFaroAlcance, 0.0, 1.0);
                            luz += uFarosColor * cono * aten * max(dot(N, Ld), 0.0);
                        }
                    }
                }

                color = vec4(uColor * luz + uColor * uEmision, uAlfa);
            }
            """;
    }
}
