package controller;

import java.util.Scanner;
import java.util.function.Supplier;

import service.ArtCientificoService;
import service.ArtCientificoServiceFactory;
import view.ArtCientificoView;
import view.io.EntradaSalidaFactory;
import view.io.EntradaSalidaIO;

/**
 * Fábrica para la creación de instancias del controlador y sus dependencias
 * Implementa el patrón Proveedor (Provider) mediante interfaces funcionales
 */
public class ArtCientificoControllerFactory {
    
    private ArtCientificoControllerFactory() {
        // Constructor privado para evitar instanciación directa
    }
    
    /**
     * Proveedor funcional para crear un controlador con todas sus dependencias
     * Utiliza interfaces funcionales para implementar el patrón proveedor
     * @return Un Supplier que proporciona una instancia completa del controlador
     */
    public static Supplier<ArtCientificoController> proveedorControlador() {
        return () -> {
            // Crear el scanner
            Scanner scanner = new Scanner(System.in);
            
            // Crear la interfaz de entrada/salida
            EntradaSalidaIO io = EntradaSalidaFactory.crearEntradaSalida(scanner);
            
            // Crear la vista
            ArtCientificoView vista = new ArtCientificoView(io);
            
            // Obtener el servicio
            ArtCientificoService servicio = ArtCientificoServiceFactory.getServicio();
            
            // Crear y devolver el controlador
            return new ArtCientificoController(vista, servicio);
        };
    }
    
    /**
     * Crea un controlador con todas sus dependencias
     * @return una instancia del controlador lista para usar
     */
    public static ArtCientificoController crearControlador() {
        return proveedorControlador().get();
    }
} 