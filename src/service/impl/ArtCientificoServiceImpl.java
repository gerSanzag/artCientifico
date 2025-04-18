package service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                .map(id -> repositorio.actualizar(articuloDTO))
                .orElseGet(() -> repositorio.crearNuevo(articuloDTO))
        );
    }
    
    @Override
    public Optional<ArtCientificoDTO> buscarPorId(Optional<Long> idOpt) {
        return idOpt.flatMap(id -> 
            repositorio.obtenerTodos()
                .flatMap(articulos -> articulos.stream()
                    .filter(art -> art.getId().isPresent() && art.getId().get().equals(id))
                    .findFirst()));
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> buscarPorCriterio(Predicate<ArtCientificoDTO> predicado) {
        return repositorio.obtenerTodos()
            .map(articulos -> articulos.stream()
                .filter(predicado)
                .collect(Collectors.toList()))
            .filter(lista -> !lista.isEmpty());
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> obtenerTodos() {
        return repositorio.obtenerTodos();
    }
    
    @Override
    public Optional<Boolean> eliminar(Optional<Long> idOpt) {
        // Verificamos que el artículo exista antes de intentar eliminarlo
        return idOpt.flatMap(id -> 
            buscarPorId(Optional.of(id))
                .map(existente -> repositorio.eliminar(id))
                .orElse(Optional.of(false)) // Si no existe, indicamos que no se eliminó
        );
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurarArticulo(Optional<Long> idOpt) {
        return idOpt.flatMap(id -> {
            // Verificar primero si el artículo ya existe
            boolean articuloExiste = repositorio.obtenerTodos()
                .map(articulos -> articulos.stream()
                    .anyMatch(art -> art.getId().isPresent() && art.getId().get().equals(id)))
                .orElse(false);
                
            if (articuloExiste) {
                return Optional.empty();
            }
            
            // Buscar el evento de eliminación más reciente
            return repositorio.obtenerHistorialEventos()
                .flatMap(eventos -> eventos.stream()
                    .filter(evento -> evento.getTipoEvento() == TipoEvento.ELIMINACION)
                    .filter(evento -> evento.getArticulo().getId()
                        .map(artId -> artId.equals(id))
                        .orElse(false))
                    .max((e1, e2) -> e1.getFechaEvento().compareTo(e2.getFechaEvento()))
                    .map(eventoEliminacion -> {
                        // Obtener el artículo del evento
                        ArtCientificoDTO articuloEliminado = eventoEliminacion.getArticulo();
                        
                        // Crear una copia del artículo con el ID original
                        ArtCientificoDTO articuloRestaurado = new ArtCientificoDTO.BuilderDTO()
                            .conId(id)
                            .conNombre(articuloEliminado.getNombre().orElse(null))
                            .conAutor(articuloEliminado.getAutor().orElse(null))
                            .conPalabrasClaves(articuloEliminado.getPalabrasClaves().orElse(null))
                            .conAnio(articuloEliminado.getAnio().orElse(null))
                            .conResumen(articuloEliminado.getResumen().orElse(null))
                            .build();
                        
                        // Usar crearNuevo para restaurar el artículo
                        Optional<ArtCientificoDTO> resultado = repositorio.crearNuevo(articuloRestaurado);
                        
                        // Registrar evento de restauración si se restauró correctamente
                        resultado.ifPresent(art -> 
                            repositorio.registrarEvento(art, TipoEvento.RESTAURACION));
                        
                        return resultado;
                    })
                    .orElse(Optional.empty())
                );
        });
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialEventos() {
        return repositorio.obtenerHistorialEventos();
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorTipo(Optional<TipoEvento> tipoEventoOpt) {
        return tipoEventoOpt.flatMap(tipoEvento -> 
            repositorio.obtenerHistorialEventos()
                .map(historial -> historial.stream()
                    .filter(evento -> evento.getTipoEvento() == tipoEvento)
                    .collect(Collectors.toList()))
                .filter(lista -> !lista.isEmpty())
        );
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorArticulo(Optional<Long> idOpt) {
        return idOpt.flatMap(id -> 
            repositorio.obtenerHistorialEventos()
                .map(historial -> historial.stream()
                    .filter(evento -> evento.getArticulo().getId()
                        .map(articuloId -> articuloId.equals(id))
                        .orElse(false))
                    .collect(Collectors.toList()))
                .filter(lista -> !lista.isEmpty())
        );
    }
} 