package repository;

import java.util.List;
import java.util.Optional;

import dto.ArtCientificoDTO;
import common.types.TipoEvento;

/**
 * Interfaz para el repositorio de artículos científicos
 * Define operaciones CRUD fundamentales usando programación funcional
 * Trabaja directamente con DTOs para desacoplar la capa de persistencia
 */
public interface ArtCientificoRepository {
    
    /**
     * Crea un nuevo artículo científico en el repositorio
     * @param articuloDTO el DTO del artículo a crear 
     * @return Optional con el DTO del artículo creado con su ID generado, o vacío si no se pudo crear
     */
    Optional<ArtCientificoDTO> crearNuevo(ArtCientificoDTO articuloDTO);
    
    /**
     * Obtiene todos los artículos científicos
     * @return Optional con la lista de DTOs de todos los artículos, o vacío si no hay artículos
     */
    Optional<List<ArtCientificoDTO>> obtenerTodos();

    /**
     * Actualiza un artículo existente
     * @param articuloActualizado los datos del artículo a actualizar
     * @return Optional con el DTO del artículo actualizado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> actualizar(ArtCientificoDTO articuloActualizado);
    
    /**
     * Elimina un artículo 
     * @param id el ID del artículo a eliminar
     * @return Optional que indica si se eliminó correctamente o no
     */
    Optional<Boolean> eliminar(Long id);
    
    /**
     * Obtiene el historial completo de eventos
     * @return Optional con la lista de todos los eventos registrados o vacío si no hay eventos
     */
    Optional<List<EventoHistorial>> obtenerHistorialEventos();
    
    /**
     * Registra un evento en el historial
     * @param articuloDTO El artículo relacionado con el evento
     * @param tipoEvento El tipo de evento a registrar
     */
    void registrarEvento(ArtCientificoDTO articuloDTO, TipoEvento tipoEvento);
} 