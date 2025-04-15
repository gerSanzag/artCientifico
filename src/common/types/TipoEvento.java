package common.types;

/**
 * Enum que define los tipos de eventos que se pueden registrar
 * en el historial de artículos científicos
 */
public enum TipoEvento {
    /**
     * Representa la creación de un nuevo artículo
     */
    CREACION,
    
    /**
     * Representa la modificación de un artículo existente
     */
    MODIFICACION,
    
    /**
     * Representa la eliminación de un artículo
     */
    ELIMINACION,
    
    /**
     * Representa la actualización de un artículo existente
     */
    ACTUALIZACION,
    
    /**
     * Representa la restauración de un artículo previamente eliminado
     */
    RESTAURACION
} 