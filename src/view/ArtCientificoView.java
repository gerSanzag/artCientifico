package view;

import java.util.Optional;
import java.util.List;
import dto.ArtCientificoDTO;
import view.io.EntradaSalidaIO;

/**
 * Vista principal para la gestión de artículos científicos
 * Encapsula la interacción con el usuario utilizando la interfaz EntradaSalidaIO
 */
public class ArtCientificoView {
    
    private final EntradaSalidaIO io;
    
    /**
     * Constructor que recibe una implementación de EntradaSalidaIO
     * @param io la implementación de EntradaSalidaIO a utilizar
     */
    public ArtCientificoView(EntradaSalidaIO io) {
        this.io = io;
    }
    
    /**
     * Muestra el menú principal de la aplicación
     */
    public void mostrarMenuPrincipal() {
        io.mostrarMensaje("\n===== SISTEMA DE GESTIÓN DE ARTÍCULOS CIENTÍFICOS =====");
        io.mostrarMensaje("1. Crear nuevo artículo");
        io.mostrarMensaje("2. Buscar artículo por ID");
        io.mostrarMensaje("3. Listar todos los artículos");
        io.mostrarMensaje("4. Actualizar artículo");
        io.mostrarMensaje("5. Eliminar artículo");
        io.mostrarMensaje("6. Restaurar artículo eliminado");
        io.mostrarMensaje("7. Ver historial de eventos");
        io.mostrarMensaje("0. Salir");
        io.mostrarMensaje("Seleccione una opción: ");
    }
    
    /**
     * Lee la opción seleccionada por el usuario
     * @return un Optional con la opción seleccionada o vacío si el input no es válido
     */
    public Optional<Integer> leerOpcion() {
        return io.leerEntrada()
                .flatMap(input -> {
                    try {
                        return Optional.of(Integer.parseInt(input));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
    }
    
    /**
     * Muestra un mensaje de error
     * @param mensaje el mensaje de error a mostrar
     */
    public void mostrarError(String mensaje) {
        io.mostrarMensaje("ERROR: " + mensaje);
    }
    
    /**
     * Muestra un mensaje de éxito
     * @param mensaje el mensaje de éxito a mostrar
     */
    public void mostrarExito(String mensaje) {
        io.mostrarMensaje("ÉXITO: " + mensaje);
    }
    
    /**
     * Muestra los detalles de un artículo científico
     * @param articuloOpt el artículo a mostrar
     */
    public void mostrarArticulo(Optional<ArtCientificoDTO> articuloOpt) {
        articuloOpt.ifPresentOrElse(
            articulo -> {
                io.mostrarMensaje("\n=== DETALLES DEL ARTÍCULO ===");
                articulo.getId().ifPresent(id -> io.mostrarMensaje("ID: " + id));
                articulo.getNombre().ifPresent(nombre -> io.mostrarMensaje("Nombre: " + nombre));
                articulo.getAutor().ifPresent(autor -> io.mostrarMensaje("Autor: " + autor));
                articulo.getAnio().ifPresent(anio -> io.mostrarMensaje("Año: " + anio));
                
                articulo.getPalabrasClaves().ifPresent(palabras -> {
                    io.mostrarMensaje("Palabras clave: " + String.join(", ", palabras));
                });
                
                articulo.getResumen().ifPresent(resumen -> {
                    io.mostrarMensaje("Resumen: " + resumen);
                });
            },
            () -> io.mostrarMensaje("No se encontró el artículo o no existe.")
        );
    }
    
    /**
     * Muestra una lista de artículos científicos
     * @param articulosOpt la lista de artículos a mostrar
     */
    public void mostrarListaArticulos(Optional<List<ArtCientificoDTO>> articulosOpt) {
        articulosOpt.ifPresentOrElse(
            articulos -> {
                io.mostrarMensaje("\n=== LISTA DE ARTÍCULOS ===");
                if (articulos.isEmpty()) {
                    io.mostrarMensaje("No hay artículos para mostrar.");
                } else {
                    articulos.forEach(articulo -> {
                        io.mostrarMensaje("----------------------------");
                        articulo.getId().ifPresent(id -> io.mostrarMensaje("ID: " + id));
                        articulo.getNombre().ifPresent(nombre -> io.mostrarMensaje("Nombre: " + nombre));
                        articulo.getAutor().ifPresent(autor -> io.mostrarMensaje("Autor: " + autor));
                        articulo.getAnio().ifPresent(anio -> io.mostrarMensaje("Año: " + anio));
                    });
                }
            },
            () -> io.mostrarMensaje("No hay artículos para mostrar.")
        );
    }
    
    /**
     * Solicita al usuario que ingrese el ID de un artículo
     * @return un Optional con el ID ingresado o vacío si el input no es válido
     */
    public Optional<Long> solicitarId() {
        io.mostrarMensaje("Ingrese el ID del artículo: ");
        return io.leerEntrada()
                .flatMap(input -> {
                    try {
                        return Optional.of(Long.parseLong(input));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
    }
    
    /**
     * Solicita al usuario un valor con un mensaje personalizado
     * @param mensaje el mensaje a mostrar
     * @return un Optional con el valor ingresado
     */
    public Optional<String> solicitarValor(String mensaje) {
        io.mostrarMensaje(mensaje);
        return io.leerEntrada();
    }
    
    /**
     * Solicita confirmación al usuario
     * @param mensaje el mensaje de confirmación
     * @return true si el usuario confirma (S/s), false en caso contrario
     */
    public boolean confirmar(String mensaje) {
        io.mostrarMensaje(mensaje + " (S/N): ");
        return io.leerEntrada()
                .map(respuesta -> respuesta.equalsIgnoreCase("S"))
                .orElse(false);
    }
} 