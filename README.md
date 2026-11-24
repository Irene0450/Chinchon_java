# Chinchón_java

Proyecto Java del juego de cartas **Chinchón** con objetivo a largo plazo de jugarlo online en la web.

**Importante: trabajo en proceso.** La lógica del juego está casi completa, pero la parte
online (DTOs, web, WebSocket, salas, etc.) aún no existe. El objetivo de este repositorio es documentar el progreso.

## Stack

- Java 17
- Maven
- Spring Boot 3.5.x

## Estructura del proyecto

```
src/main/java
├── ChinchonApplication.java          # Main class
└── chinchon/game
    ├── model
    │   ├── Card.java                 # Carta: número, valor y palo.
    │   ├── Player.java               # Jugador: id, nombre, mano y puntos
    │   └── Suit.java                 # Enum de palos: OROS, COPAS, ESPADAS, BASTOS
    └── logic
        ├── CloseType.java            # Enum de cierres: NORMAL, MINUS_TEN, CHINCHON
        ├── CombinationValidator.java # Tríos, cuartetos, escaleras, chinchón y -10
        ├── Game.java                 # Partida completa: suma de rondas, eliminados y ganador
        ├── Round.java                # Ronda: reparto, turnos, robar/descartar y cierre
        └── ScoreCalculator.java      # Puntuación por jugador al terminar la ronda
```

## Reglas

Se utiliza una baraja española de 40 cartas (sin 8s, 9s ni comodines). Por tanto, en las escaleras el 7 y la sota son consecutivos.

Al comienzo de la ronda se reparten 7 cartas a cada jugador y se forman 2 mazos: el de descarte (con una carta boca arriba) y el de robo (con las cartas sobrantes boca abajo). El objetivo del juego es casar la mano y acumular el menor número de puntos posible. Las combinaciones son: trío/cuarteto (3/4 cartas del mismo número) y escalera (mínimo 3 cartas consecutivas del mismo palo). 

Cada ronda comienza un jugador distinto (ronda 1 el primero, ronda 2 el segundo...). En cada turno, se puede robar del mazo de robo o descarte, y seguidamente se descartará una carta de la mano. Si se robó del mazo de robo, la carta recién cogida podrá ser la descartada. Sin embargo, si se robó del mazo de descartes, esta deberá obligatoriamente permanecer en la mano del jugador con el fin de evitar hacer equipo con otros miembros. Si en algún momento se termina el mazo de robo, se barajan todas las cartas del mazo de descartes excepto la visible.

Se podrá cerrar al momento del descarte. Para ello, es necesario que el jugador dispuesto a cerrar acumule como máximo 5 puntos. En ese momento, todos los jugadores muestran su mano y añadirán a su puntuación el valor de las cartas no agrupadas. El valor de todas las figuras es 10. Si las 7 cartas de la mano de algún jugador están casadas (por ejemplo, trío + escalera de 4), se restan 10 puntos a dicho jugador. Es posible obtener una puntuación negativa. Otra posibilidad es que se haya formado una escalera de 7 números del mismo palo. Es lo que se conoce como "chinchón". En ese caso, el jugador gana automáticamente y se termina la partida.

Las rondas continuarán hasta que todos los jugadores excepto uno alcancen 100 puntos. Si 2 o más jugadores llegan a este umbral en la misma ronda, gana quien menos puntos acumule. No habrá mecánica de reenganche. Si un jugador es eliminado, se mantendrá en modo espectador hasta que finalice la partida.

- **Nota**: el servidor se encarga de calcular la puntuación de cada jugador automáticamente, así como detectar las combinaciones existentes y seleccionar la mejor. También se encarga de determinar la fase de cada turno; por tanto, se garantiza que un jugador no pueda robar dos veces.
El número de puntos máximo para cerrar es susceptible a cambio (ver CombinationValidator.MAX_CLOSE_POINTS).

## Roadmap

### Hecho
- Modelo (Player, Card, Suit)
- Lógica de ronda y partida
- Validación de combinaciones y cálculo de puntos

### Pendiente
- DTOs
- Web (HTML, CSS, JavaScript)
- WebSocket
- Sistema de salas: Room y RoomManagement
- Tests
- Diagrama de clases
- Diagrama de casos de uso.
- ¿Chat? (pendiente de valorar)

### Problemas conocidos
- Actualmente los jugadores suman toda su mano al final de la ronda. En el juego original, las cartas no casadas pueden añadirse a grupos ya formados de otros jugadores.
- No habrá reenganche aunque tradicionalmnte exista.
- No se guarda el progreso.
- No hay diseño de interfaz ni pantallas.
- No se ha codificado el modo espectador.

## Cómo ejecutarlo

Requisitos: JDK 17 o superior y Maven.

```bash
mvn clean package
java -jar target/Chinchon_java-1.0-SNAPSHOT.jar
```

## Licencia

GPLv3
