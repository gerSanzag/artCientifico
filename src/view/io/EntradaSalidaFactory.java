package view.io;

import java.util.Scanner;

/**
 * Factory para crear instancias de EntradaSalidaIO
 * Permite centralizar la creación de implementaciones de la interfaz
 */
public class EntradaSalidaFactory {
    
    private EntradaSalidaFactory() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Crea una instancia de EntradaSalidaIO
     * @param scanner el Scanner a utilizar para la entrada
     * @return una implementación de EntradaSalidaIO
     */
    public static EntradaSalidaIO crearEntradaSalida(Scanner scanner) {
        return new EntradaSalidaImpl(scanner);
    }
} 