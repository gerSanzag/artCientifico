package controller;

import java.util.List;
import java.util.Optional;
import dto.ArtCientificoDTO;
import service.ArtCientificoService;
import service.ArtCientificoServiceFactory;
import view.ArtCientificoView;

/**
 * Controlador para artículos científicos
 * Implementa la lógica de la aplicación conectando vista y servicio
 */
public class ArtCientificoController {
    
    private final ArtCientificoService servicio;
    private final ArtCientificoView vista;
    
    /**
     * Constructor que inicializa el controlador con el servicio y la vista
     * @param vista la vista para interactuar con el usuario
     */
    public ArtCientificoController(ArtCientificoView vista) {
        this.servicio = ArtCientificoServiceFactory.getServicio();
        this.vista = vista;
    }
    
    /**
     * Crea un nuevo artículo científico con datos proporcionados por el usuario
     */
    public void crearArticulo() {
        vista.mostrarMensaje("=== CREACIÓN DE ARTÍCULO CIENTÍFICO ===");
        
        // Crear un builder para el DTO
        ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO();
        
        // Solicitar datos al usuario
        String nombre = vista.solicitarDato("Ingrese el nombre del artículo", true);
        builder.conNombre(nombre);
        
        String autor = vista.solicitarDato("Ingrese el autor del artículo", true);
        builder.conAutor(autor);
        
        List<String> palabrasClaves = vista.solicitarPalabrasClaves();
        builder.conPalabrasClaves(palabrasClaves);
        
        Integer anio = vista.solicitarEntero("Ingrese el año de publicación", false);
        builder.conAnio(anio);
        
        String resumen = vista.solicitarDato("Ingrese el resumen del artículo", false);
        builder.conResumen(resumen);
        
        // Construir el DTO y guardarlo
        ArtCientificoDTO dto = builder.build();
        Optional<ArtCientificoDTO> resultado = servicio.guardarArticulo(Optional.of(dto));
        
        // Mostrar resultado al usuario
        if (resultado.isPresent()) {
            vista.mostrarMensaje("Artículo guardado exitosamente: " + resultado.get().getNombre().orElse(""));
        } else {
            vista.mostrarMensaje("No se pudo guardar el artículo. Es posible que ya exista uno con ese nombre.");
        }
    }
    
    /**
     * Busca un artículo por su nombre
     */
    public void buscarArticulo() {
        vista.mostrarMensaje("=== BÚSQUEDA DE ARTÍCULO CIENTÍFICO ===");
        
        String nombre = vista.solicitarDato("Ingrese el nombre del artículo a buscar", true);
        Optional<ArtCientificoDTO> resultado = servicio.buscarArticuloPorNombre(nombre);
        
        if (resultado.isPresent()) {
            ArtCientificoDTO dto = resultado.get();
            vista.mostrarMensaje("=== INFORMACIÓN DEL ARTÍCULO ===");
            dto.getNombre().ifPresent(n -> vista.mostrarMensaje("Nombre: " + n));
            dto.getAutor().ifPresent(a -> vista.mostrarMensaje("Autor: " + a));
            dto.getPalabrasClaves().ifPresent(pc -> vista.mostrarMensaje("Palabras clave: " + String.join(", ", pc)));
            dto.getAnio().ifPresent(a -> vista.mostrarMensaje("Año: " + a));
            dto.getResumen().ifPresent(r -> vista.mostrarMensaje("Resumen: " + r));
        } else {
            vista.mostrarMensaje("No se encontró ningún artículo con ese nombre.");
        }
    }
    
    /**
     * Lista todos los artículos
     */
    public void listarArticulos() {
        vista.mostrarMensaje("=== LISTA DE ARTÍCULOS CIENTÍFICOS ===");
        
        List<ArtCientificoDTO> articulos = servicio.obtenerTodosLosArticulos();
        
        if (articulos.isEmpty()) {
            vista.mostrarMensaje("No hay artículos registrados.");
        } else {
            articulos.forEach(dto -> 
                dto.getNombre().ifPresent(nombre -> 
                    dto.getAutor().ifPresent(autor -> 
                        vista.mostrarMensaje("- " + nombre + " (Autor: " + autor + ")")
                    )
                )
            );
        }
    }
    
    /**
     * Actualiza un artículo existente
     */
    public void actualizarArticulo() {
        vista.mostrarMensaje("=== ACTUALIZACIÓN DE ARTÍCULO CIENTÍFICO ===");
        
        String nombreBusqueda = vista.solicitarDato("Ingrese el nombre del artículo a actualizar", true);
        Optional<ArtCientificoDTO> articuloExistente = servicio.buscarArticuloPorNombre(nombreBusqueda);
        
        if (articuloExistente.isEmpty()) {
            vista.mostrarMensaje("No se encontró ningún artículo con ese nombre.");
            return;
        }
        
        vista.mostrarMensaje("Artículo encontrado. Ingrese los nuevos datos (Enter para mantener el valor actual):");
        
        // Crear builder precargado con los datos actuales
        ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO();
        ArtCientificoDTO dto = articuloExistente.get();
        
        // Solicitar nuevos datos
        String nombre = vista.solicitarDato("Nombre [" + dto.getNombre().orElse("") + "]", false);
        if (nombre != null) {
            builder.conNombre(nombre);
        } else {
            dto.getNombre().ifPresent(builder::conNombre);
        }
        
        String autor = vista.solicitarDato("Autor [" + dto.getAutor().orElse("") + "]", false);
        if (autor != null) {
            builder.conAutor(autor);
        } else {
            dto.getAutor().ifPresent(builder::conAutor);
        }
        
        boolean actualizarPalabras = vista.confirmar("¿Desea actualizar las palabras clave?");
        if (actualizarPalabras) {
            List<String> palabrasClaves = vista.solicitarPalabrasClaves();
            builder.conPalabrasClaves(palabrasClaves);
        } else {
            dto.getPalabrasClaves().ifPresent(builder::conPalabrasClaves);
        }
        
        String anioStr = vista.solicitarDato("Año [" + dto.getAnio().map(String::valueOf).orElse("") + "]", false);
        if (anioStr != null) {
            try {
                Integer anio = Integer.parseInt(anioStr);
                builder.conAnio(anio);
            } catch (NumberFormatException e) {
                vista.mostrarMensaje("Valor no válido, se mantendrá el original.");
                dto.getAnio().ifPresent(builder::conAnio);
            }
        } else {
            dto.getAnio().ifPresent(builder::conAnio);
        }
        
        String resumen = vista.solicitarDato("Resumen [" + dto.getResumen().orElse("") + "]", false);
        if (resumen != null) {
            builder.conResumen(resumen);
        } else {
            dto.getResumen().ifPresent(builder::conResumen);
        }
        
        // Construir el DTO actualizado y enviarlo al servicio
        ArtCientificoDTO dtoActualizado = builder.build();
        Optional<ArtCientificoDTO> resultado = servicio.actualizarArticulo(nombreBusqueda, Optional.of(dtoActualizado));
        
        if (resultado.isPresent()) {
            vista.mostrarMensaje("Artículo actualizado exitosamente.");
        } else {
            vista.mostrarMensaje("No se pudo actualizar el artículo.");
        }
    }
    
    /**
     * Elimina un artículo
     */
    public void eliminarArticulo() {
        vista.mostrarMensaje("=== ELIMINACIÓN DE ARTÍCULO CIENTÍFICO ===");
        
        String nombre = vista.solicitarDato("Ingrese el nombre del artículo a eliminar", true);
        
        // Primero buscar para confirmar que existe
        Optional<ArtCientificoDTO> articuloExistente = servicio.buscarArticuloPorNombre(nombre);
        
        if (articuloExistente.isEmpty()) {
            vista.mostrarMensaje("No se encontró ningún artículo con ese nombre.");
            return;
        }
        
        // Confirmar la eliminación
        boolean confirmar = vista.confirmar("¿Está seguro de eliminar el artículo '" + nombre + "'?");
        
        if (confirmar) {
            boolean resultado = servicio.eliminarArticulo(nombre);
            
            if (resultado) {
                vista.mostrarMensaje("Artículo eliminado exitosamente.");
            } else {
                vista.mostrarMensaje("No se pudo eliminar el artículo.");
            }
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }
    
    /**
     * Inicia la aplicación mostrando un menú de opciones
     */
    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            vista.mostrarMensaje("\n=== SISTEMA DE GESTIÓN DE ARTÍCULOS CIENTÍFICOS ===");
            vista.mostrarMensaje("1. Crear artículo");
            vista.mostrarMensaje("2. Buscar artículo");
            vista.mostrarMensaje("3. Listar artículos");
            vista.mostrarMensaje("4. Actualizar artículo");
            vista.mostrarMensaje("5. Eliminar artículo");
            vista.mostrarMensaje("0. Salir");
            
            Integer opcion = vista.solicitarEntero("Seleccione una opción", true);
            
            switch (opcion) {
                case 1:
                    crearArticulo();
                    break;
                case 2:
                    buscarArticulo();
                    break;
                case 3:
                    listarArticulos();
                    break;
                case 4:
                    actualizarArticulo();
                    break;
                case 5:
                    eliminarArticulo();
                    break;
                case 0:
                    continuar = false;
                    vista.mostrarMensaje("¡Hasta pronto!");
                    vista.cerrar();
                    break;
                default:
                    vista.mostrarMensaje("Opción no válida, intente de nuevo.");
            }
        }
    }
} 