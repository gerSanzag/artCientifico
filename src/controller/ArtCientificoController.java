package controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dto.ArtCientificoDTO;
import view.ArtCientificoView;
import service.ArtCientificoService;
import common.types.TipoEvento;
import repository.EventoHistorial;

/**
 * Controlador para la gestión de artículos científicos
 * Implementa la lógica de control utilizando programación funcional
 * Coordina las interacciones entre la vista y el servicio
 */
public class ArtCientificoController {
    
    private final ArtCientificoView vista;
    private final ArtCientificoService servicio;
    
    // Operaciones del controlador como interfaces funcionales
    public final Runnable mostrarMenuPrincipal;
    public final Runnable crearArticulo;
    public final Runnable buscarArticuloPorId;
    public final Runnable listarArticulos;
    public final Runnable actualizarArticulo;
    public final Runnable eliminarArticulo;
    public final Runnable restaurarArticulo;
    public final Runnable mostrarHistorialEventos;
    public final Supplier<Boolean> procesarOpcionMenu;
    public final Runnable iniciarAplicacion;
    
    /**
     * Constructor que inicializa el controlador con la vista y servicio
     * @param vista la vista de la aplicación
     * @param servicio el servicio de artículos científicos
     */
    public ArtCientificoController(ArtCientificoView vista, ArtCientificoService servicio) {
        this.vista = vista;
        this.servicio = servicio;
        
        // Inicialización de las interfaces funcionales en orden para evitar referencias circulares
        
        // 1. Primero inicializamos las operaciones básicas
        this.mostrarMenuPrincipal = vista.mostrarMenuPrincipal;
        
        /**
         * Crea un nuevo artículo científico
         */
        this.crearArticulo = () -> {
            vista.mostrarMensaje.accept("\n=== CREAR NUEVO ARTÍCULO ===");
            
            // Recopilar datos del artículo
            Optional<String> nombre = vista.solicitarValor.apply("Ingrese el nombre del artículo: ");
            Optional<String> autor = vista.solicitarValor.apply("Ingrese el autor del artículo: ");
            Optional<Integer> anio = vista.solicitarValor.apply("Ingrese el año de publicación: ")
                .flatMap(input -> {
                    try {
                        return Optional.of(Integer.parseInt(input));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
            
            Optional<String> palabrasInput = vista.solicitarValor.apply("Ingrese palabras clave (separadas por comas): ");
            Optional<List<String>> palabrasClaves = palabrasInput.map(input -> 
                List.of(input.split(",")).stream()
                    .map(String::trim)
                    .filter(palabra -> !palabra.isEmpty())
                    .collect(Collectors.toList())
            );
            
            Optional<String> resumen = vista.solicitarValor.apply("Ingrese el resumen del artículo: ");
            
            // Crear el DTO
            ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO();
            nombre.ifPresent(builder::conNombre);
            autor.ifPresent(builder::conAutor);
            anio.ifPresent(builder::conAnio);
            palabrasClaves.ifPresent(builder::conPalabrasClaves);
            resumen.ifPresent(builder::conResumen);
            
            // Guardar el artículo
            Optional<ArtCientificoDTO> nuevoArticulo = servicio.guardar(Optional.of(builder.build()));
            
            // Mostrar resultado
            nuevoArticulo.ifPresentOrElse(
                articulo -> {
                    vista.mostrarExito.accept("Artículo creado correctamente");
                    vista.mostrarArticulo.accept(Optional.of(articulo));
                },
                () -> vista.mostrarError.accept("No se pudo crear el artículo")
            );
        };
        
        /**
         * Busca un artículo científico por su ID
         */
        this.buscarArticuloPorId = () -> {
            vista.mostrarMensaje.accept("\n=== BUSCAR ARTÍCULO POR ID ===");
            Optional<Long> id = vista.solicitarId.get();
            
            servicio.buscarPorId(id)
                .ifPresentOrElse(
                    articulo -> vista.mostrarArticulo.accept(Optional.of(articulo)),
                    () -> vista.mostrarError.accept("No se encontró un artículo con el ID proporcionado")
                );
        };
        
        /**
         * Lista todos los artículos científicos
         */
        this.listarArticulos = () -> {
            vista.mostrarMensaje.accept("\n=== LISTAR TODOS LOS ARTÍCULOS ===");
            Optional<List<ArtCientificoDTO>> articulos = servicio.obtenerTodos();
            vista.mostrarListaArticulos.accept(articulos);
        };
        
        /**
         * Actualiza un artículo científico existente
         */
        this.actualizarArticulo = () -> {
            vista.mostrarMensaje.accept("\n=== ACTUALIZAR ARTÍCULO ===");
            Optional<Long> id = vista.solicitarId.get();
            
            // Buscar el artículo existente
            servicio.buscarPorId(id).ifPresentOrElse(
                articuloExistente -> {
                    vista.mostrarArticulo.accept(Optional.of(articuloExistente));
                    
                    if (vista.confirmar.apply("¿Desea actualizar este artículo?")) {
                        // Recopilar nuevos datos
                        Optional<String> nombre = vista.solicitarValor.apply("Nuevo nombre (deje en blanco para mantener el actual): ");
                        Optional<String> autor = vista.solicitarValor.apply("Nuevo autor (deje en blanco para mantener el actual): ");
                        Optional<Integer> anio = vista.solicitarValor.apply("Nuevo año (deje en blanco para mantener el actual): ")
                            .flatMap(input -> {
                                if (input.isEmpty()) return Optional.empty();
                                try {
                                    return Optional.of(Integer.parseInt(input));
                                } catch (NumberFormatException e) {
                                    return Optional.empty();
                                }
                            });
                        
                        Optional<String> palabrasInput = vista.solicitarValor.apply("Nuevas palabras clave separadas por comas (deje en blanco para mantener las actuales): ");
                        Optional<List<String>> palabrasClaves = palabrasInput
                            .filter(input -> !input.isEmpty())
                            .map(input -> 
                                List.of(input.split(",")).stream()
                                    .map(String::trim)
                                    .filter(palabra -> !palabra.isEmpty())
                                    .collect(Collectors.toList())
                            );
                        
                        Optional<String> resumen = vista.solicitarValor.apply("Nuevo resumen (deje en blanco para mantener el actual): ");
                        
                        // Construir el DTO actualizado
                        ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO();
                        
                        // Mantener el mismo ID
                        articuloExistente.getId().ifPresent(builder::conId);
                       
                        // Actualizar o mantener el nombre
                        nombre.filter(n -> !n.isEmpty())
                              .ifPresentOrElse(
                                  builder::conNombre,
                                  () -> articuloExistente.getNombre().ifPresent(builder::conNombre)
                              );
                        
                        // Actualizar o mantener el autor
                        autor.filter(a -> !a.isEmpty())
                             .ifPresentOrElse(
                                 builder::conAutor,
                                 () -> articuloExistente.getAutor().ifPresent(builder::conAutor)
                             );
                        
                        // Actualizar o mantener el año
                        anio.ifPresentOrElse(
                            builder::conAnio,
                            () -> articuloExistente.getAnio().ifPresent(builder::conAnio)
                        );
                        
                        // Actualizar o mantener las palabras clave
                        palabrasClaves.ifPresentOrElse(
                            builder::conPalabrasClaves,
                            () -> articuloExistente.getPalabrasClaves().ifPresent(builder::conPalabrasClaves)
                        );
                        
                        // Actualizar o mantener el resumen
                        resumen.filter(r -> !r.isEmpty())
                               .ifPresentOrElse(
                                   builder::conResumen,
                                   () -> articuloExistente.getResumen().ifPresent(builder::conResumen)
                               );
                        
                        // Guardar el artículo actualizado
                        Optional<ArtCientificoDTO> articuloActualizado = servicio.guardar(Optional.of(builder.build()));
                        
                        // Mostrar resultado
                        articuloActualizado.ifPresentOrElse(
                            articulo -> {
                                vista.mostrarExito.accept("Artículo actualizado correctamente");
                                vista.mostrarArticulo.accept(Optional.of(articulo));
                            },
                            () -> vista.mostrarError.accept("No se pudo actualizar el artículo")
                        );
                    }
                },
                () -> vista.mostrarError.accept("No se encontró un artículo con el ID proporcionado")
            );
        };
        
        /**
         * Elimina un artículo científico por su ID
         */
        this.eliminarArticulo = () -> {
            vista.mostrarMensaje.accept("\n=== ELIMINAR ARTÍCULO ===");
            Optional<Long> idOpt = vista.solicitarId.get();

            // Encadenar operaciones usando Optional
            idOpt.ifPresentOrElse(id -> {
                servicio.buscarPorId(Optional.of(id)).ifPresentOrElse(
                    articulo -> {
                        vista.mostrarArticulo.accept(Optional.of(articulo));

                        // Encadenar confirmación y eliminación
                        Optional.of(articulo)
                            .filter(art -> vista.confirmar.apply("¿Está seguro que desea eliminar este artículo?"))
                            .ifPresentOrElse(
                                artConfirmado -> {
                                    // Intentar eliminar y mapear resultado a acciones
                                    servicio.eliminar(Optional.of(id))
                                        .map(exito -> exito ?
                                             (Runnable)() -> vista.mostrarExito.accept("Artículo eliminado correctamente")
                                             : (Runnable)() -> vista.mostrarError.accept("No se pudo eliminar el artículo (fallo lógico)")
                                        )
                                        .ifPresentOrElse(
                                            Runnable::run,
                                            () -> vista.mostrarError.accept("Error durante la eliminación (servicio devolvió vacío)")
                                        );
                                },
                                () -> vista.mostrarMensaje.accept("Operación cancelada por el usuario")
                            );
                    },
                    () -> vista.mostrarError.accept("No se encontró un artículo con el ID proporcionado")
                );
            }, () -> vista.mostrarError.accept("ID no válido o no proporcionado."));
        };
        
        /**
         * Restaura un artículo científico eliminado
         */
        this.restaurarArticulo = () -> {
            vista.mostrarMensaje.accept("\n=== RESTAURAR ARTÍCULO ELIMINADO ===");
            Optional<Long> id = vista.solicitarId.get();
            
            servicio.restaurarArticulo(id).ifPresentOrElse(
                articulo -> {
                    vista.mostrarExito.accept("Artículo restaurado correctamente");
                    vista.mostrarArticulo.accept(Optional.of(articulo));
                },
                () -> vista.mostrarError.accept("No se pudo restaurar el artículo con el ID proporcionado")
            );
        };
        
        /**
         * Muestra el historial de eventos del sistema
         */
        this.mostrarHistorialEventos = () -> {
            vista.mostrarMensaje.accept("\n=== HISTORIAL DE EVENTOS ===");
            
            // Opciones para filtrar el historial
            vista.mostrarMensaje.accept("1. Ver todo el historial");
            vista.mostrarMensaje.accept("2. Filtrar por tipo de evento");
            vista.mostrarMensaje.accept("3. Filtrar por artículo");
            vista.mostrarMensaje.accept("0. Volver al menú principal");
            
            // Definir las acciones para cada opción usando un Map
            java.util.Map<Integer, Runnable> accionesHistorial = new java.util.HashMap<>();
            accionesHistorial.put(1, this::mostrarTodoHistorial);
            accionesHistorial.put(2, this::mostrarHistorialPorTipo);
            accionesHistorial.put(3, this::mostrarHistorialPorArticulo);
            accionesHistorial.put(0, () -> {}); // Opción 0: No hacer nada, vuelve al menú principal

            // Acción por defecto si la opción no es válida
            Runnable accionPorDefecto = () -> vista.mostrarError.accept("Opción no válida");
            
            vista.leerOpcion.get().ifPresentOrElse(
                opcion -> {
                    // Obtener la acción del mapa o la acción por defecto, y ejecutarla
                    accionesHistorial.getOrDefault(opcion, accionPorDefecto).run();
                },
                () -> vista.mostrarError.accept("Entrada inválida")
            );
        };
        
        // 2. Después inicializamos el procesador de opciones que usa todas las opciones definidas previamente
        /**
         * Procesa la opción seleccionada del menú
         * @return true para continuar, false para salir
         */
        this.procesarOpcionMenu = () -> {
            return vista.leerOpcion.get()
                .map(opcion -> {
                    // Usar switch con expresión lambda (operador flecha)
                    // y 'yield' para devolver valores desde bloques
                    return switch (opcion) {
                        case 1 -> { crearArticulo.run(); yield true; }
                        case 2 -> { buscarArticuloPorId.run(); yield true; }
                        case 3 -> { listarArticulos.run(); yield true; }
                        case 4 -> { actualizarArticulo.run(); yield true; }
                        case 5 -> { eliminarArticulo.run(); yield true; }
                        case 6 -> { restaurarArticulo.run(); yield true; }
                        case 7 -> { mostrarHistorialEventos.run(); yield true; }
                        case 0 -> false; // Devuelve false directamente
                        default -> {
                            vista.mostrarError.accept("Opción no válida");
                            yield true; // Devuelve true para continuar
                        }
                    };
                })
                .orElseGet(() -> {
                    vista.mostrarError.accept("Entrada inválida");
                    return true;
                });
        };
        
        // 3. Finalmente, inicializamos la aplicación que usa el procesador de opciones
        /**
         * Inicia la aplicación mostrando el menú principal y procesando opciones
         */
        this.iniciarAplicacion = () -> {
            boolean continuar = true;
            while (continuar) {
                mostrarMenuPrincipal.run();
                continuar = procesarOpcionMenu.get();
            }
            vista.mostrarExito.accept("¡Gracias por usar el sistema de gestión de artículos científicos!");
        };
    }
    
    /**
     * Muestra todo el historial de eventos
     */
    private void mostrarTodoHistorial() {
        servicio.obtenerHistorialEventos().ifPresentOrElse(
            this::mostrarEventos,
            () -> vista.mostrarMensaje.accept("No hay eventos registrados en el historial")
        );
    }
    
    /**
     * Muestra el historial filtrado por tipo de evento
     */
    private void mostrarHistorialPorTipo() {
        vista.mostrarMensaje.accept("Tipos de eventos disponibles:");
        vista.mostrarMensaje.accept("1. CREACION");
        vista.mostrarMensaje.accept("2. ACTUALIZACION");
        vista.mostrarMensaje.accept("3. ELIMINACION");
        vista.mostrarMensaje.accept("4. RESTAURACION");
        
        // Mapear opciones a Tipos de Evento
        java.util.Map<Integer, TipoEvento> mapaTipos = new java.util.HashMap<>();
        mapaTipos.put(1, TipoEvento.CREACION);
        mapaTipos.put(2, TipoEvento.ACTUALIZACION);
        mapaTipos.put(3, TipoEvento.ELIMINACION);
        mapaTipos.put(4, TipoEvento.RESTAURACION);
        
        vista.leerOpcion.get()
            .flatMap(opcion -> Optional.ofNullable(mapaTipos.get(opcion))) // Obtener el TipoEvento del mapa como Optional
            .ifPresentOrElse(
                tipoEvento -> { // Si se encontró un TipoEvento válido
                    servicio.obtenerHistorialPorTipo(Optional.of(tipoEvento))
                        .ifPresentOrElse(
                            this::mostrarEventos,
                            () -> vista.mostrarMensaje.accept("No hay eventos del tipo " + tipoEvento + " registrados")
                        );
                },
                () -> vista.mostrarError.accept("Opción no válida") // Si la opción no estaba en el mapa o la entrada era inválida
            );
    }
    
    /**
     * Muestra el historial filtrado por artículo
     */
    private void mostrarHistorialPorArticulo() {
        Optional<Long> id = vista.solicitarId.get();
        
        servicio.obtenerHistorialPorArticulo(id).ifPresentOrElse(
            this::mostrarEventos,
            () -> vista.mostrarMensaje.accept("No hay eventos registrados para el artículo con ID " + id.orElse(0L))
        );
    }
    
    /**
     * Muestra la lista de eventos en la consola
     * @param eventos lista de eventos a mostrar
     */
    private void mostrarEventos(List<EventoHistorial> eventos) {
        if (eventos.isEmpty()) {
            vista.mostrarMensaje.accept("No hay eventos para mostrar");
            return;
        }
        
        vista.mostrarMensaje.accept("\n--- LISTADO DE EVENTOS ---");
        eventos.forEach(evento -> {
            vista.mostrarMensaje.accept("----------------------------");
            // Usar los métodos definidos en la interfaz EventoHistorial
            ArtCientificoDTO articulo = evento.getArticulo();
            
            vista.mostrarMensaje.accept("Tipo: " + evento.getTipoEvento());
            vista.mostrarMensaje.accept("Fecha: " + evento.getFechaEvento());
            
            // Mostrar información del artículo
            articulo.getId().ifPresent(id -> 
                vista.mostrarMensaje.accept("ID Artículo: " + id)
            );
            
            articulo.getNombre().ifPresent(nombre -> 
                vista.mostrarMensaje.accept("Nombre: " + nombre)
            );
            
            vista.mostrarMensaje.accept("Descripción: " + obtenerDescripcionEvento(evento));
        });
    }
    
    /**
     * Obtiene una descripción legible del evento
     * @param evento el evento a describir
     * @return una descripción del evento
     */
    private String obtenerDescripcionEvento(EventoHistorial evento) {
        // Usar switch expression para obtener la descripción del tipo
        String tipoDescripcion = switch (evento.getTipoEvento()) {
            case CREACION -> "creación";
            case ACTUALIZACION -> "actualización";
            case ELIMINACION -> "eliminación";
            case RESTAURACION -> "restauración";
            default -> "tipo desconocido";
        };
        
        // Construir la descripción final usando StringBuilder
        StringBuilder descripcion = new StringBuilder("Evento de ");
        descripcion.append(tipoDescripcion);
        descripcion.append(" realizado el ");
        descripcion.append(evento.getFechaEvento().toLocalDate());
        descripcion.append(" a las ");
        descripcion.append(evento.getFechaEvento().toLocalTime());
        
        return descripcion.toString();
    }
} 