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
            // Ejemplo de uso de la vista
            vista.mostrarMenuPrincipal();
            vista.leerOpcion().ifPresentOrElse(
                opcion -> vista.mostrarExito("Has seleccionado la opción: " + opcion),
                () -> vista.mostrarError("Opción no válida")
            );
            
            if (vista.confirmar("¿Deseas continuar?")) {
                vista.mostrarExito("Confirmado");
            } else {
                vista.mostrarError("Operación cancelada");
            }
        } finally {
            // Cerrar el scanner al finalizar
            scanner.close();
        }
    }
} 