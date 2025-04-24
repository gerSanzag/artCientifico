package view.io;

import java.util.Optional;
import java.util.Scanner;
import java.util.Objects;

/**
 * Implementación de la interfaz EntradaSalidaIO para operaciones de entrada/salida
 * Utiliza la consola para mostrar mensajes y leer entrada del usuario
 */
public class EntradaSalidaImpl implements EntradaSalidaIO {
    
    private final Scanner scanner;
    
    /**
     * Constructor que recibe un Scanner para ser utilizado en las operaciones de entrada
     * @param scanner el Scanner a utilizar (no puede ser null)
     * @throws NullPointerException si el scanner es null
     */
    public EntradaSalidaImpl(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "El scanner no puede ser null");
    }
    
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    @Override
    public Optional<String> leerEntrada() {
        try {
            return Optional.ofNullable(scanner.nextLine());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
} 