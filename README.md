🎮 Sudoku Game - JavaFX Application
Una aplicación de escritorio para jugar Sudoku 6x6 desarrollada en JavaFX, con generación automática de puzzles, validación en tiempo real y un sistema de sugerencias inteligente.

✨ Características
Generación de puzzles único: Cada partida genera un nuevo puzzle con solución única garantizada
Validación en tiempo real: Los números se validan según las reglas de Sudoku mientras escribes
Sistema de sugerencias: Obtén pistas cuando las necesites sin afectar tu progreso
Interfaz gráfica intuitiva: Diseño limpio y fácil de usar con feedback visual
Pantalla de victoria: Celebra tu logro al completar el puzzle
Pantalla de ayuda: Tutorial con las reglas del juego
Gestión de usuarios: Sistema de nicknames para personalizar la experiencia
📋 Requisitos
Java: JDK 11 o superior
JavaFX: SDK 11 o superior
Maven: Para gestionar dependencias (opcional, depende de tu configuración)
🚀 Instalación y Ejecución
1. Clonar o descargar el proyecto
bash
git clone <tu-repositorio>
cd SudokuFinal
2. Configurar JavaFX
Asegúrate de que tu IDE (IntelliJ, Eclipse, VS Code) tenga configurado correctamente el SDK de JavaFX.

En IntelliJ IDEA:

Ve a File → Project Structure → Libraries
Agrega el SDK de JavaFX
3. Ejecutar la aplicación
bash
mvn javafx:run
O desde tu IDE, ejecuta directamente la clase Main.java

🎯 Uso
Pantalla de Bienvenida
Ingresa tu nickname
Haz clic en "Jugar" para comenzar una nueva partida
Accede a las reglas desde el botón de ayuda
Durante el Juego
Ingresa números del 1 al 6 en las celdas vacías
Las celdas con números iniciales están bloqueadas (no editables)
Haz clic en "Ayuda" para obtener una sugerencia
Completa el puzzle sin repetir números en filas, columnas y bloques 2×3
Al completar correctamente, aparecerá la pantalla de victoria
Navegación
Usa el botón "Menú" para volver a la pantalla de bienvenida en cualquier momento
🏗️ Estructura del Proyecto
src/main/java/com/example/demosudoku/
├── controller/
│   ├── SudokuWelcomeController.java    # Pantalla de bienvenida
│   ├── SudokuGameController.java       # Controlador principal del juego
│   ├── SudokuFinalController.java      # Pantalla de victoria
│   └── SudokuHelpController.java       # Pantalla de ayuda
├── model/
│   ├── board/
│   │   ├── Board.java                  # Lógica del tablero
│   │   ├── BoardAdapter.java           # Patrón adapter
│   │   └── IBoard.java                 # Interfaz del tablero
│   ├── game/
│   │   ├── Game.java                   # Lógica del juego
│   │   ├── GameAbstract.java           # Clase abstracta base
│   │   ├── GameAdapter.java            # Patrón adapter
│   │   └── IGame.java                  # Interfaz del juego
│   └── user/
│       └── User.java                   # Modelo de usuario
├── utils/
│   ├── AlertBox.java                   # Utilidad para alertas
│   └── IAlertBox.java                  # Interfaz de alertas
├── view/
│   ├── SudokuWelcomeStage.java         # Ventana de bienvenida (singleton)
│   ├── SudokuGameStage.java            # Ventana del juego (singleton)
│   ├── SudokuFinalStage.java           # Ventana final (singleton)
│   └── SudokuHelpStage.java            # Ventana de ayuda
└── Main.java                           # Punto de entrada
🧩 Componentes Principales
Board.java
Gestiona la lógica del tablero Sudoku:

Generación de puzzles con solución única garantizada
Validación de números según reglas de Sudoku (filas, columnas, bloques 2×3)
Sistema de celdas bloqueadas
Sugerencias basadas en la solución
Game.java
Controla la lógica del juego:

Inicialización del tablero visual
Validación en tiempo real de entradas
Motor de sugerencias
Detección de victoria
SudokuGameController.java
Gestor principal de la interfaz del juego:

Manejo de eventos del usuario
Hilo de verificación de victoria
Transiciones entre pantallas
Gestión de usuarios
🎮 Reglas del Sudoku 6×6
Completa la cuadrícula de 6×6 con números del 1 al 6
Cada fila debe contener los números del 1 al 6 sin repetir
Cada columna debe contener los números del 1 al 6 sin repetir
Cada bloque 2×3 debe contener los números del 1 al 6 sin repetir
🔧 Patrones de Diseño Utilizados
Singleton: Para las ventanas principales (Stage)
Adapter: Para Board y Game (interfaces IBoard e IGame)
MVC: Separación entre controladores, modelos y vistas
Abstract Factory: GameAbstract como clase base
📝 Validación de Entrada
Solo se aceptan números del 1 al 6
Se valida instantáneamente conforme escribes
Se proporciona feedback visual (colores) para valores válidos e inválidos
Los números con conflicto se marcan en rojo
🧵 Hilo de Verificación de Victoria
La aplicación ejecuta un hilo daemon que verifica cada 2 segundos si el puzzle está completo y válido. Cuando se detecta la victoria:

Se detiene el hilo de verificación
Se transiciona a la pantalla de victoria
Se muestra un mensaje personalizado con el nickname del jugador
🐛 Solución de Problemas
El juego no inicia

Verifica que JavaFX esté correctamente configurado
Comprueba la ruta de los recursos FXML
Las celdas no se validan

Asegúrate de que solo ingreses números del 1 al 6
Verifica que la celda no esté bloqueada
El hilo de victoria no se detiene

Este es un comportamiento esperado al cambiar de pantalla
El hilo está configurado como daemon para evitar que la aplicación quede colgada
🤝 Contribuciones
Las contribuciones son bienvenidas. Por favor:

Haz fork del proyecto
Crea una rama para tu feature (git checkout -b feature/AmazingFeature)
Commit tus cambios (git commit -m 'Add some AmazingFeature')
Push a la rama (git push origin feature/AmazingFeature)
Abre un Pull Request
📄 Licencia
Este proyecto está bajo la licencia MIT. Consulta el archivo LICENSE para más detalles.

👨‍💻 Autores
Proyecto desarrollado por:

[William May]
[Juan Fernando Marmolejo]
Como una aplicación de escritorio interactiva para jugar Sudoku.

Diviértete jugando Sudoku 🎯

