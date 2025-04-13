package controller;

import model.ArtCientificoModel;
import view.ArtCientificoView;
import dto.ArtCientificoDTO;
import dto.ArtCientificoConverter;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador para manejar la lógica de creación de artículos científicos
 */
public class ArtCientificoController {
    
    private final ArtCientificoView vista;
    private final List<ArtCientificoModel> articulos;
    
    public ArtCientificoController(ArtCientificoView vista) {
        this.vista = vista;
        this.articulos = new ArrayList<>();
    }
    
    /**
     * Método para crear un nuevo artículo científico
     */
    public void crearArticulo() {
        vista.mostrarMensaje("\n=== CREAR NUEVO ARTÍCULO CIENTÍFICO ===\n");
        
        // Solicitar datos básicos al usuario para crear el DTO
        String nombre = vista.solicitarDato("Ingrese el nombre del artículo", false);
        String autor = vista.solicitarDato("Ingrese el autor", false);
        Integer anio = vista.solicitarEntero("Ingrese el año de publicación", false);
        
        // Crear el DTO con los campos obligatorios
        ArtCientificoDTO dto = new ArtCientificoDTO(nombre, autor, anio);
        
        // Solicitar palabras clave
        List<String> palabrasClaves = vista.solicitarPalabrasClaves();
        if (!palabrasClaves.isEmpty()) {
            // Envolvemos la lista en Optional antes de pasarla al DTO
            dto.setPalabrasClaves(Optional.of(palabrasClaves));
        }
        
        // Solicitar resumen
        Optional.ofNullable(vista.solicitarDato("Ingrese el resumen del artículo", false))
               .ifPresent(resumen -> dto.setResumen(Optional.of(resumen)));
        
        // Convertir DTO a Model y guardar
        ArtCientificoModel nuevoArticulo = ArtCientificoConverter.dtoToModel(dto);
        articulos.add(nuevoArticulo);
        
        vista.mostrarMensaje("\nArtículo creado exitosamente!");
        mostrarDetallesArticulo(nuevoArticulo);
    }
    
    /**
     * Método para mostrar los detalles de un artículo científico
     * @param articulo artículo a mostrar
     */
    private void mostrarDetallesArticulo(Optional<ArtCientificoModel> articuloOpt) {
        if (articuloOpt.isEmpty()) {
            vista.mostrarMensaje("No se puede mostrar el artículo: no existe.");
            return;
        }
        
        ArtCientificoModel articulo = articuloOpt.get();
        
        // Convertimos a DTO para mostrar los datos
        ArtCientificoDTO dto = ArtCientificoConverter.modelToDto(articulo);
        
        vista.mostrarMensaje("\n=== DETALLES DEL ARTÍCULO ===\n");
        
        Optional.ofNullable(dto.getNombre())
                .ifPresentOrElse(
                    nombre -> vista.mostrarMensaje("Nombre: " + nombre),
                    () -> vista.mostrarMensaje("Nombre: No especificado")
                );
        
        Optional.ofNullable(dto.getAutor())
                .ifPresentOrElse(
                    autor -> vista.mostrarMensaje("Autor: " + autor),
                    () -> vista.mostrarMensaje("Autor: No especificado")
                );
        
        List<String> palabrasClaves = dto.getPalabrasClaves();
        if (palabrasClaves.isEmpty()) {
            vista.mostrarMensaje("Palabras clave: No especificadas");
        } else {
            vista.mostrarMensaje("Palabras clave: " + String.join(", ", palabrasClaves));
        }
        
        Optional.ofNullable(dto.getAnio())
                .ifPresentOrElse(
                    anio -> vista.mostrarMensaje("Año: " + anio),
                    () -> vista.mostrarMensaje("Año: No especificado")
                );
        
        Optional.ofNullable(dto.getResumen())
                .ifPresentOrElse(
                    resumen -> vista.mostrarMensaje("Resumen: " + resumen),
                    () -> vista.mostrarMensaje("Resumen: No especificado")
                );
    }
    
    /**
     * Sobrecarga del método que acepta un artículo directamente
     * @param articulo artículo a mostrar
     */
    private void mostrarDetallesArticulo(ArtCientificoModel articulo) {
        mostrarDetallesArticulo(Optional.ofNullable(articulo));
    }
    
    /**
     * Método para listar todos los artículos
     */
    public void listarArticulos() {
        if (articulos.isEmpty()) {
            vista.mostrarMensaje("No hay artículos registrados.");
            return;
        }
        
        vista.mostrarMensaje("\n=== LISTA DE ARTÍCULOS ===\n");
        
        // Convertimos todos los modelos a DTOs usando streams
        List<ArtCientificoDTO> dtos = articulos.stream()
                                           .map(ArtCientificoConverter::modelToDto)
                                           .collect(Collectors.toList());
        
        for (int i = 0; i < dtos.size(); i++) {
            ArtCientificoDTO dto = dtos.get(i);
            vista.mostrarMensaje("Artículo #" + (i + 1));
            vista.mostrarMensaje("Título: " + Optional.ofNullable(dto.getNombre()).orElse("No especificado"));
            vista.mostrarMensaje("Autor: " + Optional.ofNullable(dto.getAutor()).orElse("No especificado"));
            vista.mostrarMensaje("---------------------");
        }
    }
    
    /**
     * Método para iniciar la aplicación
     */
    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            crearArticulo();
            continuar = vista.confirmar("\n¿Desea crear otro artículo?");
        }
        
        if (!articulos.isEmpty()) {
            listarArticulos();
        }
        
        vista.mostrarMensaje("\n¡Gracias por usar la aplicación!");
        vista.cerrar();
    }
} 