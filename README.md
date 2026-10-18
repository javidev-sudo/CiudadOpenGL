# Ciudad OpenGL

Demo de conducción en una ciudad generada por código, escrita en **Java + Maven +
LWJGL + OpenGL 3.3**. Toda la geometría se dibuja con cajas unitarias reutilizadas;
la iluminación (día/noche, farolas y faros) se calcula en un shader propio.

---

## 1. Requisitos

- **JDK 21** (el `pom.xml` compila con `release 21`).
- **Maven 3.6+**.
- **Windows** (las dependencias nativas del `pom.xml` son `natives-windows`; en otro
  sistema habría que cambiar el *classifier* de los nativos de LWJGL).
- Conexión a internet **la primera vez** (Maven descarga LWJGL 3.3.3 y sus nativos).

---

## 2. Instrucciones de ejecución

Desde la **raíz del proyecto** (la carpeta que contiene `pom.xml`):

```bash
mvn compile exec:java
```

- `compile` es necesario porque `exec:java` **no** compila por sí solo.
- El `main` se toma del `pom.xml` (`com.graphics.juego.Main`), así que no hace falta
  pasar `-Dexec.mainClass`.
- Si ya descargaste las dependencias, puedes ejecutar sin internet:

```bash
mvn -o compile exec:java
```

Para un arranque de prueba que se cierre solo tras N cuadros (útil para verificación):

```bash
mvn -o compile exec:java -Ddemo.frames=120
```

> Nota: `mvn exec:java` **a secas** puede fallar con `ClassNotFoundException` si el
> proyecto aún no está compilado. Usa siempre `mvn compile exec:java`.

---

## 3. Diseño de la ciudad

- **Mapa 11×11** de celdas de 10×10 unidades. La matriz `MAPA` usa:
  - `0` = calle, `1` = edificio, `2` = parque.
- Las **calles** ocupan las filas y columnas pares (forman una rejilla).
- Hay **16 edificios** de tres tipos (pequeño, mediano y **rascacielos**, con 3 en total),
  con altura y color propios y **ventanas que se iluminan de noche**.
- Hay **9 parques** con árboles y bancos.
- **Pasos peatonales (cebra)** tipo cebra: solo aparecen bajo un semáforo, salvo
  **dos excepciones** sin semáforo.
- **Sectores**: la ciudad se divide en cuatro cuadrantes (Noroeste, Noreste, Suroeste,
  Sureste) según la posición del auto.
- **Señalización y vallas**: 4 carteles de sector y 3 vallas publicitarias con texto
  en el mundo (`CIUDAD 11X11`, `BIENVENIDO`, `PARQUE CENTRAL`), dentro del margen de su parque.
- **Semáforos tipo pórtico**: dos postes a los lados de la calle unidos por un travesaño
  del que cuelgan las luces. Hay **9**, bien repartidos (separación mínima ~32 unidades).
- **Farolas**: solo en los parques, **dos por parque en esquinas opuestas en diagonal**
  (18 en total). Solo emiten luz de noche.
- **Cielo**: el sol de día y la luna con estrellas de noche, como geometría 3D que los
  edificios pueden ocultar.

---

## 4. Controles

| Tecla | Acción |
|---|---|
| `W` / `↑` | Acelerar |
| `S` / `↓` | Reversa |
| `A` / `←` | Girar a la izquierda |
| `D` / `→` | Girar a la derecha |
| `Espacio` | Freno |
| `C` | Cambiar cámara (3ª persona → 1ª persona → aérea) |
| `N` | Día / Noche (de noche enciende los faros, de día los apaga) |
| `F` | Encender / apagar faros |
| `M` | Mostrar / ocultar minimapa |
| `R` | Reiniciar el recorrido (auto y entregas) |
| `H` | Mostrar / ocultar la **ayuda de controles** |
| `P` | **Pausar / reanudar** (abre el menú de pausa) |
| `Esc` | Salir |

**En vista aérea**, `←` / `→` giran la cámara alrededor de la ciudad.

**En el menú de pausa** (`P`): `↑` / `↓` para moverte por las opciones y `Enter` para
elegir entre **Reanudar**, **Reiniciar** y **Salir**.

---

## 5. Funciones implementadas (y cómo se usan)

- **Conducción con física y colisiones** — se usa con `W/A/S/D` o flechas; el auto no
  atraviesa manzanas ni sale del mapa. El movimiento se actualiza con el **tiempo entre
  cuadros** (`deltaTime`), por lo que la velocidad no depende de los FPS.
- **Tres cámaras** — se cambian **presionando `C`** (persecución, primera persona y aérea).
- **Iluminación día/noche** — se alterna **presionando `N`**; cambia el color del cielo,
  la luz ambiental y la direccional (sol/luna).
- **Faros del auto** — se encienden/apagan **presionando `F`** (proyectan un cono de luz).
- **Minimapa** — se muestra/oculta **presionando `M`**; vista superior con el auto y el destino.
- **Entregas** — ruta de **5 entregas**: hay **una sola activa** a la vez; al pasar cerca
  de ella **desaparece y reaparece en otro punto** de la ciudad hasta completar las 5.
  Al completarlas muestra “entregas completadas”. Se reinicia **presionando `R`**.
- **Reinicio** — se hace **presionando `R`** (vuelve el auto al inicio y reinicia el recorrido).
- **Panel dentro de la ventana** — muestra en pantalla **velocidad**, **estado de los faros**,
  **modo día/noche** y **sector actual**. No requiere tecla: se actualiza solo.
- **Ayuda de controles** — se muestra/oculta **presionando `H`**.
- **Menú de pausa navegable** — se abre **presionando `P`**; congela la simulación.
- **Sectores y señalización** — los carteles de sector y las vallas aparecen en la ciudad;
  el sector actual se muestra en el panel.
- **Redimensionado de la ventana** — la vista 3D, el minimapa y la interfaz se adaptan
  al tamaño real de la ventana.

> El **indicador básico** (cámara, día/noche, faros, minimapa, entregas y km/h) también
> se muestra en el **título de la ventana**, como en el proyecto de referencia.

---

## 6. Novedades de esta entrega

- Rediseño del semáforo como **pórtico de dos postes** alineado con los pasos peatonales.
- **9 semáforos** repartidos sin quedar pegados entre sí.
- **Cebras** únicamente bajo semáforos (más dos excepciones sin semáforo).
- **Farolas solo en parques**, dos por parque en esquinas diagonales opuestas.
- **Entregas finitas** de 5 con una sola activa a la vez.
- **Interfaz superpuesta dentro de la ventana**: panel, ayuda (`H`) y menú de pausa (`P`).
- **Fuente de texto propia (bitmap 5×7)** y **malla dinámica** para dibujar la interfaz
  sin librerías externas de texto.
- **Sectores** y **carteles/vallas** con texto en el mundo.

---

## 7. Estructura del proyecto

```
src/main/java/com/graphics/juego/
├── Main.java                 Punto de entrada (comando de ejecución)
├── core/                     Aplicacion (bucle), Escena y Camara (interfaces)
├── gfx/                      Ventana, Entrada, shaders, mallas, pintor, renderer
│   ├── Malla.java            Geometría estática
│   ├── MallaDinamica.java    Geometría dinámica (interfaz)
│   ├── Vertices.java         Constructor de vértices
│   └── PintorCajas.java      Dibujo de cajas reutilizando un cubo
├── escena/                   EscenaCiudad (raíz de composición), CamaraCiudad, Shaders (GLSL)
├── objetos/                  Ciudad, Vehiculo, Chata, Edificio, Destino, Semaforo,
│                             Farola, Arbol, Banco
├── luces/                    Iluminacion (día/noche, farolas) y FarosAuto
└── funciones/                Cielo, Decoracion, GestorEntregas, Semaforos, Minimapa,
                              Sectores, Cartel, Carteles, Interfaz, Fuente
```

**`EscenaCiudad`** es la raíz de composición: crea y conecta todos los módulos y define
`iniciar / actualizar / dibujar / tecla`.

---

## 8. Limitaciones

- Los **semáforos son visuales**: muestran el ciclo rojo/verde/amarillo pero **no detienen
  al auto**.
- La **fuente de texto es propia y básica**: solo **mayúsculas**, dígitos y unos pocos
  símbolos (`: / . , - ! ? >`); cualquier otro carácter sale en blanco.
- **Sin texturas, modelos ni audio**: toda la geometría son cajas y los shaders van
  embebidos en el código (no hay archivos `.glsl`).
- Las **dependencias nativas son de Windows** (`natives-windows`); en Linux/macOS habría
  que cambiar los *classifiers* en el `pom.xml`.
- La **colisión** es un círculo contra las celdas de edificio/parque; no hay colisión con
  semáforos, farolas ni carteles.
- La **chata** usa la misma colisión que el auto; si golpea una esquina, el vehículo se
  frena y revierte su movimiento de ese cuadro hasta que se aparte (tampoco choca con
  semáforos, farolas ni carteles, y **no se dibuja en el minimapa**).
- Las **entregas** reaparecen en posiciones aleatorias entre las intersecciones; no hay
  cálculo de ruta ni guía de navegación.
- Se requiere **JDK 21** y **Maven**; no se entrega un `.jar` ejecutable autónomo (las
  dependencias se descargan con Maven y no se incluyen).

---

## 9. Recursos y fragmentos externos utilizados

- **LWJGL 3.3.3** (`lwjgl`, `lwjgl-glfw`, `lwjgl-opengl` + nativos de Windows): enlace a
  **GLFW** y **OpenGL**. Licencia **BSD-3-Clause** (LWJGL); GLFW bajo **zlib/libpng**.
  Se declaran como dependencias en `pom.xml`; **no** se incluyen en el repositorio.
- **OpenGL 3.3 Core**: API de dibujo (Khronos Group).
- **JUnit 3.8.1** (`src/test/java/com/graphics/AppTest.java`): prueba de plantilla
  generada por el **arquetipo de Maven**; no es código propio y no prueba lógica del juego.
- **Sin recursos de arte de terceros**: no se usan texturas, modelos, fuentes ni sonidos
  externos. La **fuente bitmap 5×7** (`funciones/Fuente.java`) y los **shaders**
  (`escena/Shaders.java`) fueron escritos para este proyecto.

---

## 10. Autores

Proyecto de Programación Gráfica — primer parcial.
