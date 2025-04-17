package repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import dto.ArtCientificoDTO;
import repository.ArtCientificoRepository;
import repository.EventoHistorial;
import common.types.TipoEvento;

/**
 * Implementación del repositorio que maneja los artículos científicos en memoria
 */
public class ArtCientificoRepositoryImpl implements ArtCientificoRepository {
    
    // Almacena los artículos científicos en memoria
    private final Map<Long, ArtCientificoDTO> articulos = new HashMap<>();
    
    // Historial de eventos para artículos (creación, actualización, eliminación)
    private final List<EventoHistorialImpl> historialEventos = new ArrayList<>();
    
    // Generador de IDs para los artículos
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * Implementación de EventoHistorial como clase interna
     */
    private class EventoHistorialImpl implements EventoHistorial {
        private final ArtCientificoDTO articulo;
        private final TipoEvento tipoEvento;
        private final LocalDateTime fechaEvento;
        
        public EventoHistorialImpl(ArtCientificoDTO articulo, TipoEvento tipoEvento) {
            this.articulo = articulo;
            this.tipoEvento = tipoEvento;
            this.fechaEvento = LocalDateTime.now();
        }
        
        @Override
        public ArtCientificoDTO getArticulo() {
            return articulo;
        }
        
        @Override
        public TipoEvento getTipoEvento() {
            return tipoEvento;
        }
        
        @Override
        public LocalDateTime getFechaEvento() {
            return fechaEvento;
        }
    }
    
    /**
     * Registra un evento en el historial
     */
    private void registrarEvento(ArtCientificoDTO articulo, TipoEvento tipoEvento) {
        historialEventos.add(new EventoHistorialImpl(articulo, tipoEvento));
    }
    
    /**
     * Crea un nuevo artículo científico con un ID generado automáticamente
     * @param articuloDTO el DTO del artículo sin ID
     * @return el DTO del artículo con ID generado
     */
    private Optional<ArtCientificoDTO> crearNuevo(ArtCientificoDTO articuloDTO) {
        // Generar nuevo ID
        Long nuevoId = idGenerator.getAndIncrement();
        
        // Crear artículo con el nuevo ID
        ArtCientificoDTO nuevoArticulo = new ArtCientificoDTO.BuilderDTO()
            .conId(nuevoId)
            .conNombre(articuloDTO.getNombre().orElse(null))
            .conAutor(articuloDTO.getAutor().orElse(null))
            .conAnio(articuloDTO.getAnio().orElse(null))
            .conPalabrasClaves(articuloDTO.getPalabrasClaves().orElse(null))
            .conResumen(articuloDTO.getResumen().orElse(null))
            .build();
        
        // Guardar en el repositorio
        articulos.put(nuevoId, nuevoArticulo);
        
        // Registrar evento de creación
        registrarEvento(nuevoArticulo, TipoEvento.CREACION);
        
        return Optional.of(nuevoArticulo);
    }
    
    @Override
    public Optional<ArtCientificoDTO> guardar(Optional<ArtCientificoDTO> articuloDTOOpt) {
        // Procesamos el artículo solo si existe (flatMap)
        return articuloDTOOpt.flatMap(articuloDTO -> 
            // Verificamos si tiene un ID y si ese ID ya existe en nuestro repositorio
            articuloDTO.getId()
                .filter(articulos::containsKey)
                // Si tiene ID y existe, actualizamos
                .map(id -> actualizar(Optional.of(articuloDTO)))
                // Si no tiene ID o tiene uno que no existe, creamos uno nuevo
                .orElseGet(() -> crearNuevo(articuloDTO))
        );
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> obtenerTodos() {
        List<ArtCientificoDTO> todosLosArticulos = new ArrayList<>(articulos.values());
        return todosLosArticulos.isEmpty() ? Optional.empty() : Optional.of(todosLosArticulos);
    }
    
    @Override
    public Optional<ArtCientificoDTO> actualizar(Optional<ArtCientificoDTO> articuloDTOOpt) {
        return articuloDTOOpt.flatMap(articuloDTO -> 
            articuloDTO.getId().flatMap(id -> 
                Optional.ofNullable(articulos.get(id))
                    .map(articuloExistente -> {
                        // Crear un builder que comienza con los valores existentes
                        ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO()
                            .conId(id);
                        
                        // Aplicar los valores del DTO existente
                        articuloExistente.getNombre().ifPresent(builder::conNombre);
                        articuloExistente.getAutor().ifPresent(builder::conAutor);
                        articuloExistente.getAnio().ifPresent(builder::conAnio);
                        articuloExistente.getPalabrasClaves().ifPresent(builder::conPalabrasClaves);
                        articuloExistente.getResumen().ifPresent(builder::conResumen);
                        
                        // Sobrescribir con valores nuevos si están presentes
                        articuloDTO.getNombre().ifPresent(builder::conNombre);
                        articuloDTO.getAutor().ifPresent(builder::conAutor);
                        articuloDTO.getAnio().ifPresent(builder::conAnio);
                        articuloDTO.getPalabrasClaves().ifPresent(builder::conPalabrasClaves);
                        articuloDTO.getResumen().ifPresent(builder::conResumen);
                        
                        // Construir el DTO actualizado
                        ArtCientificoDTO articuloActualizado = builder.build();
                        
                        // Registrar evento y actualizar
                        registrarEvento(articuloExistente, TipoEvento.ACTUALIZACION);
                        articulos.put(id, articuloActualizado);
                        
                        return articuloActualizado;
                    })
            )
        );
    }
    
    @Override
    public Optional<Boolean> eliminar(Optional<Long> id) {
        // Usar flatMap para trabajar solo si el Optional tiene valor
        return id.flatMap(idArticulo -> {
            ArtCientificoDTO articuloEliminado = articulos.remove(idArticulo);
            
            return Optional.ofNullable(articuloEliminado)
                .map(articulo -> {
                    // Registrar evento de eliminación
                    registrarEvento(articulo, TipoEvento.ELIMINACION);
                    return true;
                })
                .or(() -> Optional.of(false));
        });
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurarArticulo(Optional<Long> idOpt) {
        // Usar flatMap para trabajar solo si existe el ID
        return idOpt.flatMap(id -> 
            // Buscar el evento de eliminación más reciente
            historialEventos.stream()
                .filter(evento -> evento.getTipoEvento() == TipoEvento.ELIMINACION)
                .filter(evento -> evento.getArticulo().getId().isPresent() && 
                        evento.getArticulo().getId().get().equals(id))
                .max((e1, e2) -> e1.getFechaEvento().compareTo(e2.getFechaEvento()))
                .map(eventoEliminacion -> {
                    ArtCientificoDTO articuloEliminado = eventoEliminacion.getArticulo();
                    
                    // Verificar si el ID ya existe y si no existe, restaurar
                    return Optional.of(!articulos.containsKey(id))
                        .filter(Boolean::booleanValue)
                        .map(noExiste -> {
                            // Restaurar el artículo
                            articulos.put(id, articuloEliminado);
                            // Registrar evento de restauración
                            registrarEvento(articuloEliminado, TipoEvento.RESTAURACION);
                            return articuloEliminado;
                        });
                })
                .orElse(Optional.empty())
        );
    }

    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialEventos() {
        List<EventoHistorial> eventos = new ArrayList<>(historialEventos);
        return eventos.isEmpty() ? Optional.empty() : Optional.of(eventos);
    }

    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorTipo(Optional<TipoEvento> tipoEventoOpt) {
        if (tipoEventoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        TipoEvento tipoEvento = tipoEventoOpt.get();
        List<EventoHistorial> eventosFiltrados = historialEventos.stream()
            .filter(evento -> evento.getTipoEvento() == tipoEvento)
            .collect(Collectors.toList());
            
        return eventosFiltrados.isEmpty() ? Optional.empty() : Optional.of(eventosFiltrados);
    }

    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialPorArticulo(Optional<Long> idOpt) {
        if (idOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Long id = idOpt.get();
        List<EventoHistorial> eventosFiltrados = historialEventos.stream()
            .filter(evento -> evento.getArticulo().getId().isPresent() && 
                   evento.getArticulo().getId().get().equals(id))
            .collect(Collectors.toList());
            
        return eventosFiltrados.isEmpty() ? Optional.empty() : Optional.of(eventosFiltrados);
    }

    @Override
    public Optional<ArtCientificoDTO> buscarPorId(Optional<Long> idOpt) {
        return idOpt.flatMap(id -> 
            Optional.ofNullable(articulos.get(id))
        );
    }
    
    @Override
    public Optional<List<ArtCientificoDTO>> buscarPorCriterio(Predicate<ArtCientificoDTO> predicado) {
        List<ArtCientificoDTO> resultados = articulos.values().stream()
                .filter(predicado)
                .collect(Collectors.toList());
        
        return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados);
    }
} 