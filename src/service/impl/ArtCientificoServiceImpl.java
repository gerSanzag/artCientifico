package service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import dto.ArtCientificoDTO;
import common.types.TipoEvento;
import repository.ArtCientificoRepository;
import repository.ArtCientificoRepositoryFactory;
import repository.EventoHistorial;
import service.ArtCientificoService;

/**
 * Implementación del servicio de artículos científicos
 * Coordina la lógica de negocio y utiliza el repositorio para la persistencia
 */
public class ArtCientificoServiceImpl implements ArtCientificoService {
    
    // Repositorio que maneja la persistencia de los artículos
    private final ArtCientificoRepository repositorio;
    
    /**
     * Constructor que obtiene el repositorio a través de su factory
     */
    public ArtCientificoServiceImpl() {
        this.repositorio = ArtCientificoRepositoryFactory.getRepositorio();
    }
    
    /**
     * Constructor que permite inyectar un repositorio específico (útil para testing)
     * @param repositorio el repositorio a utilizar
     */
    public ArtCientificoServiceImpl(ArtCientificoRepository repositorio) {
        this.repositorio = repositorio;
    }
    
    @Override
    public Optional<ArtCientificoDTO> guardar(Optional<ArtCientificoDTO> articuloOpt) {
        return articuloOpt.flatMap(articuloDTO -> 
            articuloDTO.getId()
                .map(id -> repositorio.actualizar(Optional.of(articuloDTO)))
                .orElseGet(() -> repositorio.crearNuevo(articuloDTO))
        );
    }
    
    @Override
    public Optional<ArtCientificoDTO> buscarPorId(Optional<Long> idOpt) {
        return repositorio.buscarPorId(idOpt);
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> buscarPorCriterio(Predicate<ArtCientificoDTO> predicado) {
        return repositorio.buscarPorCriterio(predicado);
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> obtenerTodos() {
        return repositorio.obtenerTodos();
    }
    
    @Override
    public Optional<Boolean> eliminar(Optional<Long> idOpt) {
        // Verificamos que el artículo exista antes de intentar eliminarlo
        return idOpt.flatMap(id -> 
            repositorio.buscarPorId(Optional.of(id))
                .map(existente -> repositorio.eliminar(Optional.of(id)))
                .orElse(Optional.of(false)) // Si no existe, indicamos que no se eliminó
        );
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurarArticulo(Optional<Long> idOpt) {
        return repositorio.restaurarArticulo(idOpt);
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialEventos() {
        return repositorio.obtenerHistorialEventos();
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorTipo(Optional<TipoEvento> tipoEventoOpt) {
        return repositorio.obtenerHistorialPorTipo(tipoEventoOpt);
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorArticulo(Optional<Long> idOpt) {
        return repositorio.obtenerHistorialPorArticulo(idOpt);
    }
} 