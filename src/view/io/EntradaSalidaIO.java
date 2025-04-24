package view.io;

import java.util.Optional;

/**
 * Interfaz para operaciones básicas de entrada/salida
 * Define los métodos necesarios para mostrar información y recibir entrada del usuario
 */
public interface EntradaSalidaIO {
    
    /**
     * Muestra un mensaje en la salida estándar
     * @param mensaje el mensaje a mostrar
     */
    void mostrarMensaje(String mensaje);
    
    /**
     * Lee una línea de texto desde la entrada estándar
     * @return un Optional que contiene la línea leída o vacío si ocurre un error
     */
    Optional<String> leerEntrada();
} 