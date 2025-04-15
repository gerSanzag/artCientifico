import controller.ArtCientificoController;
import view.ArtCientificoView;

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
        
        // Crear la vista
        ArtCientificoView vista = new ArtCientificoView();
        
        // Crear el controlador con la vista
        ArtCientificoController controlador = new ArtCientificoController(vista);
        
        // Iniciar la aplicación
        controlador.iniciar();
    }
} 