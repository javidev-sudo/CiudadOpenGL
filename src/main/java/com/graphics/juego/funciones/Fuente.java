package com.graphics.juego.funciones;

import com.graphics.juego.gfx.Vertices;

/**
 * Fuente de mapa de bits 5×7 para dibujar texto sin dependencias externas.
 * Cada carácter se compone de píxeles; cada píxel encendido se emite como un
 * cuad (dos triángulos). Se usa tanto en pantalla (HUD) como en el mundo
 * (carteles y vallas).
 */
public final class Fuente {

    public static final int ANCHO = 5;
    public static final int ALTO = 7;
    private static final int ESPACIO = 1;

    private Fuente() {
    }

    /** Ancho en píxeles que ocupará el texto con la escala dada. */
    public static float ancho2D(String texto, float escala) {
        return texto.length() * (ANCHO + ESPACIO) * escala;
    }

    /** Escribe texto en coordenadas de pantalla (origen arriba-izquierda). */
    public static void escribir2D(String texto, float x, float y, float escala, Vertices salida) {
        String t = texto.toUpperCase();
        float avance = (ANCHO + ESPACIO) * escala;
        for (int i = 0; i < t.length(); i++) {
            String[] glifo = glifo(t.charAt(i));
            float gx = x + i * avance;
            for (int fila = 0; fila < ALTO; fila++) {
                for (int columna = 0; columna < ANCHO; columna++) {
                    if (glifo[fila].charAt(columna) == '#') {
                        quad2D(salida, gx + columna * escala, y + fila * escala, escala, escala);
                    }
                }
            }
        }
    }

    /**
     * Escribe texto sobre un plano vertical del mundo. El origen (ox, oy, oz) es
     * la esquina superior izquierda leída; (rx, rz) es la dirección horizontal
     * unitaria en la que avanza el texto y la "y" crece hacia arriba.
     */
    public static void escribirPlano(String texto, float ox, float oy, float oz,
                                     float rx, float rz, float escala, Vertices salida) {
        String t = texto.toUpperCase();
        float avance = (ANCHO + ESPACIO) * escala;
        for (int i = 0; i < t.length(); i++) {
            String[] glifo = glifo(t.charAt(i));
            float u0 = i * avance;
            for (int fila = 0; fila < ALTO; fila++) {
                for (int columna = 0; columna < ANCHO; columna++) {
                    if (glifo[fila].charAt(columna) != '#') {
                        continue;
                    }
                    float u = u0 + columna * escala;
                    float v = fila * escala;
                    float x1 = ox + rx * u;
                    float z1 = oz + rz * u;
                    float x2 = ox + rx * (u + escala);
                    float z2 = oz + rz * (u + escala);
                    float ySup = oy - v;
                    float yInf = oy - (v + escala);
                    verticePlano(salida, x1, ySup, z1);
                    verticePlano(salida, x2, ySup, z2);
                    verticePlano(salida, x2, yInf, z2);
                    verticePlano(salida, x1, ySup, z1);
                    verticePlano(salida, x2, yInf, z2);
                    verticePlano(salida, x1, yInf, z1);
                }
            }
        }
    }

    private static void quad2D(Vertices salida, float x, float y, float ancho, float alto) {
        vertice2D(salida, x, y);
        vertice2D(salida, x + ancho, y);
        vertice2D(salida, x + ancho, y + alto);
        vertice2D(salida, x, y);
        vertice2D(salida, x + ancho, y + alto);
        vertice2D(salida, x, y + alto);
    }

    private static void vertice2D(Vertices salida, float x, float y) {
        salida.agregar(x, y, 0f, 0f, 0f, 1f);
    }

    private static void verticePlano(Vertices salida, float x, float y, float z) {
        salida.agregar(x, y, z, 0f, 0f, 1f);
    }

    private static String[] glifo(char c) {
        return switch (c) {
            case 'A' -> new String[] {" ### ",
                                     "#   #",
                                     "#   #",
                                     "#####",
                                     "#   #",
                                     "#   #",
                                     "#   #"};
            case 'B' -> new String[] {"#### ", "#   #", "#   #", "#### ", "#   #", "#   #", "#### "};
            case 'C' -> new String[] {" ####", "#    ", "#    ", "#    ", "#    ", "#    ", " ####"};
            case 'D' -> new String[] {"#### ", "#   #", "#   #", "#   #", "#   #", "#   #", "#### "};
            case 'E' -> new String[] {"#####", "#    ", "#    ", "#### ", "#    ", "#    ", "#####"};
            case 'F' -> new String[] {"#####", "#    ", "#    ", "#### ", "#    ", "#    ", "#    "};
            case 'G' -> new String[] {" ####", "#    ", "#    ", "#  ##", "#   #", "#   #", " ####"};
            case 'H' -> new String[] {"#   #", "#   #", "#   #", "#####", "#   #", "#   #", "#   #"};
            case 'I' -> new String[] {"#####", "  #  ", "  #  ", "  #  ", "  #  ", "  #  ", "#####"};
            case 'J' -> new String[] {"   ##", "    #", "    #", "    #", "#   #", "#   #", " ### "};
            case 'K' -> new String[] {"#   #", "#  # ", "# #  ", "##   ", "# #  ", "#  # ", "#   #"};
            case 'L' -> new String[] {"#    ", "#    ", "#    ", "#    ", "#    ", "#    ", "#####"};
            case 'M' -> new String[] {"#   #", "## ##", "# # #", "#   #", "#   #", "#   #", "#   #"};
            case 'N' -> new String[] {"#   #", "##  #", "# # #", "#  ##", "#   #", "#   #", "#   #"};
            case 'O' -> new String[] {" ### ", "#   #", "#   #", "#   #", "#   #", "#   #", " ### "};
            case 'P' -> new String[] {"#### ", "#   #", "#   #", "#### ", "#    ", "#    ", "#    "};
            case 'Q' -> new String[] {" ### ", "#   #", "#   #", "#   #", "# # #", "#  # ", " ## #"};
            case 'R' -> new String[] {"#### ", "#   #", "#   #", "#### ", "# #  ", "#  # ", "#   #"};
            case 'S' -> new String[] {" ####", "#    ", "#    ", " ### ", "    #", "    #", "#### "};
            case 'T' -> new String[] {"#####", "  #  ", "  #  ", "  #  ", "  #  ", "  #  ", "  #  "};
            case 'U' -> new String[] {"#   #", "#   #", "#   #", "#   #", "#   #", "#   #", " ### "};
            case 'V' -> new String[] {"#   #", "#   #", "#   #", "#   #", "#   #", " # # ", "  #  "};
            case 'W' -> new String[] {"#   #", "#   #", "#   #", "# # #", "# # #", "## ##", "#   #"};
            case 'X' -> new String[] {"#   #", "#   #", " # # ", "  #  ", " # # ", "#   #", "#   #"};
            case 'Y' -> new String[] {"#   #", "#   #", " # # ", "  #  ", "  #  ", "  #  ", "  #  "};
            case 'Z' -> new String[] {"#####", "    #", "   # ", "  #  ", " #   ", "#    ", "#####"};
            case '0' -> new String[] {" ### ", "#   #", "#  ##", "# # #", "##  #", "#   #", " ### "};
            case '1' -> new String[] {"  #  ", " ##  ", "  #  ", "  #  ", "  #  ", "  #  ", " ### "};
            case '2' -> new String[] {" ### ", "#   #", "    #", "   # ", "  #  ", " #   ", "#####"};
            case '3' -> new String[] {"#####", "   # ", "  #  ", "   # ", "    #", "#   #", " ### "};
            case '4' -> new String[] {"   # ", "  ## ", " # # ", "#  # ", "#####", "   # ", "   # "};
            case '5' -> new String[] {"#####", "#    ", "#### ", "    #", "    #", "#   #", " ### "};
            case '6' -> new String[] {" ### ", "#   #", "#    ", "#### ", "#   #", "#   #", " ### "};
            case '7' -> new String[] {"#####", "    #", "   # ", "  #  ", " #   ", " #   ", " #   "};
            case '8' -> new String[] {" ### ", "#   #", "#   #", " ### ", "#   #", "#   #", " ### "};
            case '9' -> new String[] {" ### ", "#   #", "#   #", " ####", "    #", "#   #", " ### "};
            case ':' -> new String[] {"     ", "  #  ", "  #  ", "     ", "  #  ", "  #  ", "     "};
            case '/' -> new String[] {"    #", "    #", "   # ", "  #  ", " #   ", "#    ", "#    "};
            case '.' -> new String[] {"     ", "     ", "     ", "     ", "     ", " ##  ", " ##  "};
            case ',' -> new String[] {"     ", "     ", "     ", "     ", "  ## ", "  #  ", " #   "};
            case '-' -> new String[] {"     ", "     ", "     ", "#####", "     ", "     ", "     "};
            case '!' -> new String[] {"  #  ", "  #  ", "  #  ", "  #  ", "  #  ", "     ", "  #  "};
            case '>' -> new String[] {"#    ", " #   ", "  #  ", "   # ", "  #  ", " #   ", "#    "};
            case '?' -> new String[] {" ### ", "#   #", "    #", "   # ", "  #  ", "     ", "  #  "};
            default -> new String[] {"     ", "     ", "     ", "     ", "     ", "     ", "     "};
        };
    }
}
