package repository;

import java.time.LocalDateTime;
import dto.ArtCientificoDTO;
import common.types.TipoEvento;

/**
 * Interfaz que define el contrato para eventos históricos en el sistema.
 * Representa cualquier evento que ocurra sobre un artículo científico.
 */
public interface EventoHistorial {
    
    /**
     * Obtiene el artículo científico asociado al evento
     * @return el DTO del artículo
     */
    ArtCientificoDTO getArticulo();
    
    /**
     * Obtiene el tipo de evento
     * @return el tipo de evento (CREACION, MODIFICACION, ELIMINACION)
     */
    TipoEvento getTipoEvento();
    
    /**
     * Obtiene la fecha y hora en que ocurrió el evento
     * @return la fecha y hora del evento
     */
    LocalDateTime getFechaEvento();
} 