package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import dto.ArtCientificoDTO;
import common.types.TipoEvento;
import repository.EventoHistorial;

/**
 * Interfaz para el servicio de artículos científicos
 * Define operaciones de negocio usando programación funcional
 * Trabaja con DTOs para mantener la consistencia en todas las capas
 */
public interface ArtCientificoService {
    
    /**
     * Guarda un nuevo artículo científico
     * @param articuloDTOOpt el DTO del artículo a guardar (encapsulado en Optional)
     * @return Optional con el DTO del artículo guardado o vacío si no se pudo guardar
     */
    Optional<ArtCientificoDTO> guardarArticulo(Optional<ArtCientificoDTO> articuloDTOOpt);
    
    /**
     * Busca artículos por un predicado específico
     * @param predicado función que evalúa si un artículo cumple con ciertos criterios
     * @param soloUno indica si solo se quiere recuperar el primer artículo que cumpla el predicado
     * @return lista de artículos que cumplen con el predicado, o una lista vacía si no hay coincidencias
     */
    List<ArtCientificoDTO> buscarPor(Predicate<ArtCientificoDTO> predicado, boolean soloUno);
    
    /**
     * Método auxiliar para buscar un único artículo y devolverlo como Optional
     * @param predicado función que evalúa si un artículo cumple con ciertos criterios
     * @return Optional con el primer artículo que cumpla con el predicado, o vacío si no hay coincidencias
     */
    Optional<ArtCientificoDTO> buscarUno(Predicate<ArtCientificoDTO> predicado);
    
    /**
     * Obtiene todos los artículos científicos
     * @return lista con los DTOs de todos los artículos
     */
    List<ArtCientificoDTO> obtenerTodosLosArticulos();
    
    /**
     * Actualiza un artículo existente
     * @param id el ID del artículo a actualizar
     * @param articuloDTOOpt los nuevos datos del artículo (encapsulado en Optional)
     * @return Optional con el DTO del artículo actualizado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> actualizarArticulo(Optional<Long> id, Optional<ArtCientificoDTO> articuloDTOOpt);
    
    /**
     * Elimina un artículo
     * @param id el ID del artículo a eliminar
     * @return Optional que indica si se eliminó correctamente o no
     */
    Optional<Boolean> eliminarArticulo(Optional<Long> id);
    
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
     * Obtiene el historial de artículos eliminados
     * @return un mapa con los artículos eliminados y su fecha de eliminación
     * @deprecated Usar obtenerHistorialPorTipo(TipoEvento.ELIMINACION) en su lugar
     */
    @Deprecated
    Map<ArtCientificoDTO, LocalDateTime> obtenerHistorialEliminados();
    
    /**
     * Restaura un artículo eliminado por su ID
     * @param id ID del artículo a restaurar
     * @return Optional con el artículo restaurado o vacío si no se encontró
     */
    Optional<ArtCientificoDTO> restaurarArticulo(Long id);
} 