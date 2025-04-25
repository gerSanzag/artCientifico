import view.ArtCientificoView;
import view.io.EntradaSalidaIO;
import view.io.EntradaSalidaFactory;
import java.util.Scanner;

/**
 * Clase principal de la aplicación
 */
public class App {
    
    /**
     * Método principal que inicia la aplicación
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestión de Artículos Científicos...");
        
        // Crear el scanner para la entrada de datos
        Scanner scanner = new Scanner(System.in);
        
        // Crear la interfaz de entrada/salida
        EntradaSalidaIO io = EntradaSalidaFactory.crearEntradaSalida(scanner);
        
        // Crear la vista con la interfaz de entrada/salida
        ArtCientificoView vista = new ArtCientificoView(io);
        
        try {
            // Ejemplo de uso de la vista con interfaces funcionales
            vista.mostrarMenuPrincipal.run();
            
            vista.leerOpcion.get().ifPresentOrElse(
                opcion -> vista.mostrarExito.accept("Has seleccionado la opción: " + opcion),
                () -> vista.mostrarError.accept("Opción no válida")
            );
            
            if (vista.confirmar.apply("¿Deseas continuar?")) {
                vista.mostrarExito.accept("Confirmado");
            } else {
                vista.mostrarError.accept("Operación cancelada");
            }
        } finally {
            // Cerrar el scanner al finalizar
            scanner.close();
        }
    }
} 