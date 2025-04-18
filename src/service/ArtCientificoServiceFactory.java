package service;

import service.impl.ArtCientificoServiceImpl;

/**
 * Fábrica para obtener instancias del servicio de artículos científicos
 * Implementa el patrón Singleton para asegurar una única instancia
 */
public class ArtCientificoServiceFactory {
    
    private static ArtCientificoService instancia;
    
    private ArtCientificoServiceFactory() {
        // Constructor privado para evitar instanciación directa
    }
    
    /**
     * Obtiene la instancia única del servicio (patrón Singleton)
     * @return la instancia del servicio
     */
    public static synchronized ArtCientificoService getServicio() {
        if (instancia == null) {
            instancia = new ArtCientificoServiceImpl();
        }
        return instancia;
    }
} 