package view;

import java.util.Optional;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import dto.ArtCientificoDTO;
import view.io.EntradaSalidaIO;

/**
 * Vista principal para la gestión de artículos científicos
 * Encapsula la interacción con el usuario utilizando la interfaz EntradaSalidaIO
 * Implementa un enfoque puramente funcional utilizando interfaces funcionales
 */
public class ArtCientificoView {
    
    private final EntradaSalidaIO io;
    
    // Declaro los miembros funcionales
    public final Runnable mostrarMenuPrincipal;
    public final Supplier<Optional<Integer>> leerOpcion;
    public final Consumer<String> mostrarError;
    public final Consumer<String> mostrarExito;
    public final Consumer<Optional<ArtCientificoDTO>> mostrarArticulo;
    public final Consumer<Optional<List<ArtCientificoDTO>>> mostrarListaArticulos;
    public final Supplier<Optional<Long>> solicitarId;
    public final Function<String, Optional<String>> solicitarValor;
    public final Function<String, Boolean> confirmar;
    public final Consumer<String> mostrarMensaje;
    
    /**
     * Constructor que recibe una implementación de EntradaSalidaIO
     * @param io la implementación de EntradaSalidaIO a utilizar
     */
    public ArtCientificoView(EntradaSalidaIO io) {
        this.io = io;
        
        // Inicializo las interfaces funcionales
        
        /**
         * Consumer básico para mostrar mensajes
         */
        this.mostrarMensaje = io::mostrarMensaje;
        
        /**
         * Runnable que muestra el menú principal
         */
        this.mostrarMenuPrincipal = () -> {
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
        };
        
        /**
         * Supplier que lee la opción seleccionada por el usuario
         */
        this.leerOpcion = () -> 
            io.leerEntrada()
                .flatMap(input -> {
                    try {
                        return Optional.of(Integer.parseInt(input));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
        
        /**
         * Consumer que muestra un mensaje de error
         */
        this.mostrarError = mensaje -> 
            io.mostrarMensaje("ERROR: " + mensaje);
        
        /**
         * Consumer que muestra un mensaje de éxito
         */
        this.mostrarExito = mensaje -> 
            io.mostrarMensaje("ÉXITO: " + mensaje);
        
        /**
         * Consumer que muestra los detalles de un artículo científico
         */
        this.mostrarArticulo = articuloOpt -> 
            articuloOpt.ifPresentOrElse(
                articulo -> {
                    io.mostrarMensaje("\n=== DETALLES DEL ARTÍCULO ===");
                    articulo.getId().ifPresent(id -> io.mostrarMensaje("ID: " + id));
                    articulo.getNombre().ifPresent(nombre -> io.mostrarMensaje("Nombre: " + nombre));
                    articulo.getAutor().ifPresent(autor -> io.mostrarMensaje("Autor: " + autor));
                    articulo.getAnio().ifPresent(anio -> io.mostrarMensaje("Año: " + anio));
                    
                    articulo.getPalabrasClaves().ifPresent(palabras -> 
                        io.mostrarMensaje("Palabras clave: " + String.join(", ", palabras))
                    );
                    
                    articulo.getResumen().ifPresent(resumen -> 
                        io.mostrarMensaje("Resumen: " + resumen)
                    );
                },
                () -> io.mostrarMensaje("No se encontró el artículo o no existe.")
            );
        
        /**
         * Consumer que muestra una lista de artículos científicos
         */
        this.mostrarListaArticulos = articulosOpt -> 
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
                            articulo.getPalabrasClaves().ifPresent(palabras -> 
                                io.mostrarMensaje("Palabras clave: " + String.join(", ", palabras))
                            );
                            articulo.getResumen().ifPresent(resumen -> 
                                io.mostrarMensaje("Resumen: " + resumen)
                            );
                        });
                    }
                },
                () -> io.mostrarMensaje("No hay artículos para mostrar.")
            );
        
        /**
         * Supplier que solicita al usuario que ingrese el ID de un artículo
         */
        this.solicitarId = () -> {
            io.mostrarMensaje("Ingrese el ID del artículo: ");
            return io.leerEntrada()
                    .flatMap(input -> {
                        try {
                            return Optional.of(Long.parseLong(input));
                        } catch (NumberFormatException e) {
                            return Optional.empty();
                        }
                    });
        };
        
        /**
         * Function que solicita al usuario un valor con un mensaje personalizado
         */
        this.solicitarValor = mensaje -> {
            io.mostrarMensaje(mensaje);
            return io.leerEntrada();
        };
        
        /**
         * Function que solicita confirmación al usuario
         */
        this.confirmar = mensaje -> {
            io.mostrarMensaje(mensaje + " (S/N): ");
            return io.leerEntrada()
                    .map(respuesta -> respuesta.equalsIgnoreCase("S"))
                    .orElse(false);
        };
    }

    // --- Nuevos métodos para solicitar entradas numéricas validadas ---

    /**
     * Solicita al usuario un ID y asegura que sea un número Long válido.
     * Repite la solicitud hasta obtener una entrada válida.
     * @return el ID Long válido ingresado por el usuario.
     */
    public Long solicitarIdValido() {
        while (true) { // Bucle hasta obtener entrada válida
            io.mostrarMensaje("Ingrese el ID del artículo: ");
            Optional<String> inputOpt = io.leerEntrada();
            
            if (inputOpt.isPresent()) {
                String input = inputOpt.get();
                try {
                    return Long.parseLong(input); // Intentar convertir a Long
                } catch (NumberFormatException e) {
                    mostrarError.accept("Entrada inválida. Por favor, ingrese un número válido para el ID.");
                    // El bucle continuará
                }
            } else {
                // Esto podría pasar si leerEntrada tiene un problema, aunque es raro con System.in
                mostrarError.accept("No se pudo leer la entrada.");
            }
        }
    }

    /**
     * Solicita al usuario un año opcional y asegura que sea un número Integer válido o esté vacío.
     * Repite la solicitud hasta obtener una entrada válida o vacía.
     * @param mensaje El mensaje a mostrar al usuario para solicitar el año opcional.
     * @return un Optional<Integer> que contiene el año si se ingresó un número válido, 
     *         o un Optional vacío si el usuario dejó la entrada en blanco.
     */
    public Optional<Integer> solicitarAnioValidoOpcional(String mensaje) {
        while (true) { // Bucle hasta obtener entrada válida
            io.mostrarMensaje(mensaje);
            Optional<String> inputOpt = io.leerEntrada();

            if (inputOpt.isPresent()) {
                String input = inputOpt.get().trim(); // Quitar espacios
                if (input.isEmpty()) {
                    return Optional.empty(); // El usuario decidió omitir, es válido
                }
                try {
                    return Optional.of(Integer.parseInt(input)); // Intentar convertir a Integer
                } catch (NumberFormatException e) {
                    mostrarError.accept("Entrada inválida. Por favor, ingrese un número válido para el año o déjelo en blanco.");
                    // El bucle continuará
                }
            } else {
                mostrarError.accept("No se pudo leer la entrada.");
            }
        }
    }
} 