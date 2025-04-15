package repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import dto.ArtCientificoDTO;
import common.types.TipoEvento;

/**
 * Interfaz para el repositorio de artículos científicos
 * Define operaciones CRUD usando programación funcional
 * Trabaja directamente con DTOs para desacoplar la capa de persistencia
 */
public interface ArtCientificoRepository {
    
    /**
     * Guarda un artículo científico en el repositorio
     * @param articuloOpt el DTO del artículo a guardar (encapsulado en Optional)
     * @return Optional con el DTO del artículo guardado o vacío si no se pudo guardar
     */
    Optional<ArtCientificoDTO> guardar(Optional<ArtCientificoDTO> articuloOpt);
    
    /**
     * Busca artículos que cumplan con un predicado específico
     * @param predicado el criterio de búsqueda (función que evalúa cada artículo)
     * @param soloUno indica si solo se quiere recuperar el primer artículo que cumpla el predicado
     * @return lista de DTOs de artículos que cumplen con el predicado, o una lista con un solo elemento si soloUno es true
     */
    List<ArtCientificoDTO> buscarPor(Predicate<ArtCientificoDTO> predicado, boolean soloUno);
    
    /**
     * Obtiene todos los artículos científicos
     * @return lista con los DTOs de todos los artículos
     */
    List<ArtCientificoDTO> obtenerTodos();
    
    /**
     * Actualiza un artículo existente
     * @param id el ID del artículo a actualizar
     * @param articuloActualizadoOpt los nuevos datos del artículo (encapsulado en Optional)
     * @return Optional con el DTO del artículo actualizado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> actualizar(Optional<Long> id, Optional<ArtCientificoDTO> articuloActualizadoOpt);
    
    /**
     * Elimina un artículo 
     * @param id el ID del artículo a eliminar
     * @return Optional que indica si se eliminó correctamente o no
     */
    Optional<Boolean> eliminar(Optional<Long> id);
    
    /**
     * Obtiene el historial completo de eventos
     * @return lista con todos los eventos registrados (creación, modificación, eliminación)
     */
    List<EventoHistorial> obtenerHistorialEventos();
    
    /**
     * Obtiene el historial de eventos de un tipo específico
     * @param tipoEvento el tipo de evento a filtrar
     * @return lista con los eventos del tipo especificado
     */
    List<EventoHistorial> obtenerHistorialPorTipo(TipoEvento tipoEvento);
    
    /**
     * Obtiene el historial de eventos relacionados con un artículo específico
     * @param id ID del artículo
     * @return lista con los eventos relacionados con el artículo
     */
    List<EventoHistorial> obtenerHistorialPorArticulo(Long id);
    
    /**
     * Obtiene el historial de artículos eliminados (mantenido por compatibilidad)
     * @return un mapa con los artículos eliminados y su fecha de eliminación
     * @deprecated Usar obtenerHistorialPorTipo(TipoEvento.ELIMINACION) en su lugar
     */
    @Deprecated
    default Map<ArtCientificoDTO, LocalDateTime> obtenerHistorialEliminados() {
        // Implementación por defecto para mantener compatibilidad
        // El método retorna un mapa vacío, la implementación concreta debe sobrescribirlo
        // si se necesita mantener la funcionalidad original
        return Map.of();
    }
    
    /**
     * Restaura un artículo eliminado por su ID
     * @param id ID del artículo a restaurar
     * @return Optional con el artículo restaurado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> restaurarArticulo(Long id);
} 