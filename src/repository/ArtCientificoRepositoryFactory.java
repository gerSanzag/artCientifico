package repository;

import repository.impl.ArtCientificoRepositoryImpl;

/**
 * Fábrica para obtener instancias del repositorio
 * Implementa el patrón Singleton para asegurar una única instancia
 */
public class ArtCientificoRepositoryFactory {
    
    private static ArtCientificoRepository instancia;
    
    private ArtCientificoRepositoryFactory() {
        // Constructor privado para evitar instanciación directa
    }
    
    /**
     * Obtiene la instancia única del repositorio (patrón Singleton)
     * @return la instancia del repositorio
     */
    public static synchronized ArtCientificoRepository getRepositorio() {
        if (instancia == null) {
            instancia = new ArtCientificoRepositoryImpl();
        }
        return instancia;
    }
} 