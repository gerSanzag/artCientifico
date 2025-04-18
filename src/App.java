import controller.ArtCientificoController;
import view.ArtCientificoView;
import service.ArtCientificoServiceFactory;
import service.ArtCientificoService;

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
        
        // Obtener el servicio a través de su factory
        ArtCientificoService servicio = ArtCientificoServiceFactory.getServicio();
        
        // Crear la vista
        ArtCientificoView vista = new ArtCientificoView();
        
        // Crear el controlador con la vista y el servicio
        ArtCientificoController controlador = new ArtCientificoController(vista, servicio);
        
        // Iniciar la aplicación
        controlador.iniciar();
    }
} 