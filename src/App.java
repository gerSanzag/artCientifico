import controller.ArtCientificoController;
import controller.ArtCientificoControllerFactory;

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
        
        try {
            // Crear el controlador utilizando la fábrica
            ArtCientificoController controlador = ArtCientificoControllerFactory.crearControlador();
            
            // Iniciar la aplicación
            controlador.iniciarAplicacion.run();
        } catch (Exception e) {
            System.err.println("Error en la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 