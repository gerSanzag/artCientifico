package repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Override
    public void registrarEvento(ArtCientificoDTO articulo, TipoEvento tipoEvento) {
        historialEventos.add(new EventoHistorialImpl(articulo, tipoEvento));
    }
    
    /**
     * Crea un nuevo artículo científico con un ID generado automáticamente
     * @param articuloDTO el DTO del artículo sin ID
     * @return el DTO del artículo con ID generado
     */
    @Override
    public Optional<ArtCientificoDTO> crearNuevo(ArtCientificoDTO articuloDTO) {
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
    public Optional<List<ArtCientificoDTO>> obtenerTodos() {
        List<ArtCientificoDTO> todosLosArticulos = new ArrayList<>(articulos.values());
        return todosLosArticulos.isEmpty() ? Optional.empty() : Optional.of(todosLosArticulos);
    }
    
    @Override
    public Optional<ArtCientificoDTO> actualizar(ArtCientificoDTO articuloDTO) {
        return articuloDTO.getId()
            .flatMap(id -> {
                // Definimos la función de fusión localmente
                java.util.function.BiFunction<ArtCientificoDTO, ArtCientificoDTO, ArtCientificoDTO> fusionarArticulos = 
                    (base, actualizaciones) -> {
                        return new ArtCientificoDTO.BuilderDTO()
                            .conId(base.getId().orElse(null))
                            .conNombre(actualizaciones.getNombre().isPresent() ? 
                                      actualizaciones.getNombre().get() : base.getNombre().orElse(null))
                            .conAutor(actualizaciones.getAutor().isPresent() ? 
                                     actualizaciones.getAutor().get() : base.getAutor().orElse(null))
                            .conAnio(actualizaciones.getAnio().isPresent() ? 
                                    actualizaciones.getAnio().get() : base.getAnio().orElse(null))
                            .conPalabrasClaves(actualizaciones.getPalabrasClaves().isPresent() ? 
                                              actualizaciones.getPalabrasClaves().get() : base.getPalabrasClaves().orElse(null))
                            .conResumen(actualizaciones.getResumen().isPresent() ? 
                                       actualizaciones.getResumen().get() : base.getResumen().orElse(null))
                            .build();
                    };
                
                return Optional.ofNullable(articulos.get(id))
                    .map(articuloExistente -> {
                        // Combinar el artículo existente con las actualizaciones
                        ArtCientificoDTO articuloActualizado = fusionarArticulos.apply(articuloExistente, articuloDTO);
                        
                        // Registrar evento y actualizar
                        registrarEvento(articuloExistente, TipoEvento.ACTUALIZACION);
                        articulos.put(id, articuloActualizado);
                        
                        return articuloActualizado;
                    });
            });
    }
    
    @Override
    public Optional<Boolean> eliminar(Long id) {
        ArtCientificoDTO articuloEliminado = articulos.remove(id);
        
        return Optional.ofNullable(articuloEliminado)
            .map(articulo -> {
                // Registrar evento de eliminación
                registrarEvento(articulo, TipoEvento.ELIMINACION);
                return true;
            })
            .or(() -> Optional.of(false));
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurar(ArtCientificoDTO articuloDTO) {
        return articuloDTO.getId()
            .map(id -> {
                // Comprobar si ya existe (por si acaso, aunque el servicio ya lo hace)
                if (articulos.containsKey(id)) {
                    return Optional.<ArtCientificoDTO>empty(); // Ya existe, no se puede restaurar sobre sí mismo
                }
                // Insertar directamente con el ID proporcionado
                articulos.put(id, articuloDTO);
                // No registramos evento aquí, el servicio lo hará después
                return Optional.of(articuloDTO);
            })
            .orElse(Optional.empty()); // Si el DTO no tiene ID, no podemos restaurar
    }
    
    @Override
    public Optional<List<EventoHistorial>> obtenerHistorialEventos() {
        List<EventoHistorial> eventos = new ArrayList<>(historialEventos);
        return eventos.isEmpty() ? Optional.empty() : Optional.of(eventos);
    }
} 