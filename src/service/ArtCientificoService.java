package service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import dto.ArtCientificoDTO;
import common.types.TipoEvento;
import repository.EventoHistorial;

/**
 * Interfaz para el servicio de artículos científicos
 * Define operaciones de alto nivel y coordina la lógica de negocio
 */
public interface ArtCientificoService {
    
    /**
     * Guarda un artículo científico, decidiendo si crear uno nuevo o actualizar uno existente
     * @param articuloOpt el DTO del artículo a guardar (encapsulado en Optional)
     * @return Optional con el DTO del artículo guardado o vacío si no se pudo guardar
     */
    Optional<ArtCientificoDTO> guardar(Optional<ArtCientificoDTO> articuloOpt);
    
    /**
     * Busca un artículo por su ID
     * @param idOpt el ID del artículo a buscar (encapsulado en Optional)
     * @return Optional con el DTO del artículo si se encuentra, o vacío si no existe
     */
    Optional<ArtCientificoDTO> buscarPorId(Optional<Long> idOpt);
    
    /**
     * Busca artículos que cumplan con un predicado específico
     * @param predicado el criterio de búsqueda (función que evalúa cada artículo)
     * @return Optional con la lista de DTOs de artículos que cumplen con el predicado, o vacío si no hay resultados
     */
    Optional<List<ArtCientificoDTO>> buscarPorCriterio(Predicate<ArtCientificoDTO> predicado);
    
    /**
     * Obtiene todos los artículos científicos
     * @return Optional con la lista de DTOs de todos los artículos, o vacío si no hay artículos
     */
    Optional<List<ArtCientificoDTO>> obtenerTodos();
    
    /**
     * Elimina un artículo 
     * @param idOpt el ID del artículo a eliminar (encapsulado en Optional)
     * @return Optional que indica si se eliminó correctamente o no
     */
    Optional<Boolean> eliminar(Optional<Long> idOpt);
    
    /**
     * Restaura un artículo eliminado por su ID
     * @param idOpt ID del artículo a restaurar (encapsulado en Optional)
     * @return Optional con el artículo restaurado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> restaurarArticulo(Optional<Long> idOpt);
    
    /**
     * Obtiene el historial completo de eventos
     * @return Optional con la lista de todos los eventos registrados o vacío si no hay eventos
     */
    Optional<List<EventoHistorial>> obtenerHistorialEventos();
    
    /**
     * Obtiene el historial de eventos de un tipo específico
     * @param tipoEventoOpt el tipo de evento a filtrar (encapsulado en Optional)
     * @return Optional con la lista de eventos del tipo especificado o vacío si no hay eventos
     */
    Optional<List<EventoHistorial>> obtenerHistorialPorTipo(Optional<TipoEvento> tipoEventoOpt);
    
    /**
     * Obtiene el historial de eventos relacionados con un artículo específico
     * @param idOpt ID del artículo (encapsulado en Optional)
     * @return Optional con la lista de eventos relacionados con el artículo o vacío si no hay eventos
     */
    Optional<List<EventoHistorial>> obtenerHistorialPorArticulo(Optional<Long> idOpt);
} 