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
    
    @Override
    public Optional<ArtCientificoDTO> guardar(Optional<ArtCientificoDTO> articuloDTOOpt) {
        if (articuloDTOOpt.isEmpty()) {
            return Optional.empty();
        }
        
        ArtCientificoDTO articuloDTO = articuloDTOOpt.get();
        
        // Si el artículo no tiene ID, le asignamos uno nuevo
        if (articuloDTO.getId().isEmpty()) {
            Long nuevoId = idGenerator.getAndIncrement();
            articuloDTO = new ArtCientificoDTO.BuilderDTO()
                .conId(nuevoId)
                .conNombre(articuloDTO.getNombre().orElse(null))
                .conAutor(articuloDTO.getAutor().orElse(null))
                .conAnio(articuloDTO.getAnio().orElse(null))
                .conPalabrasClaves(articuloDTO.getPalabrasClaves().orElse(null))
                .conResumen(articuloDTO.getResumen().orElse(null))
                .build();
        }
        
        // Verificar si el ID ya existe
        Long id = articuloDTO.getId().get();
        if (articulos.containsKey(id)) {
            // Si ya existe, no sobreescribimos y devolvemos vacío
            return Optional.empty();
        }
        
        // Guardar el artículo
        articulos.put(id, articuloDTO);
        
        // Registrar evento de creación
        registrarEvento(articuloDTO, TipoEvento.CREACION);
        
        return Optional.of(articuloDTO);
    }
    
    @Override
    public List<ArtCientificoDTO> buscarPor(Predicate<ArtCientificoDTO> predicado, boolean soloUno) {
        return articulos.values().stream()
                .filter(predicado)
                .limit(soloUno ? 1 : Long.MAX_VALUE)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ArtCientificoDTO> obtenerTodos() {
        return new ArrayList<>(articulos.values());
    }
    
    @Override
    public Optional<ArtCientificoDTO> actualizar(Optional<Long> id, Optional<ArtCientificoDTO> articuloDTOOpt) {
        if (id.isEmpty() || articuloDTOOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Long idArticulo = id.get();
        if (!articulos.containsKey(idArticulo)) {
            return Optional.empty();
        }
        
        ArtCientificoDTO articuloExistente = articulos.get(idArticulo);
        ArtCientificoDTO nuevoArticulo = articuloDTOOpt.get();
        
        // Crear un nuevo DTO con los datos actualizados pero manteniendo el ID original
        ArtCientificoDTO articuloActualizado = new ArtCientificoDTO.BuilderDTO()
            .conId(idArticulo)
            .conNombre(nuevoArticulo.getNombre().isEmpty() ? 
                articuloExistente.getNombre().orElse(null) : 
                nuevoArticulo.getNombre().orElse(null))
            .conAutor(nuevoArticulo.getAutor().isEmpty() ? 
                articuloExistente.getAutor().orElse(null) : 
                nuevoArticulo.getAutor().orElse(null))
            .conAnio(nuevoArticulo.getAnio().isEmpty() ? 
                articuloExistente.getAnio().orElse(null) : 
                nuevoArticulo.getAnio().orElse(null))
            .conPalabrasClaves(nuevoArticulo.getPalabrasClaves().isEmpty() ? 
                articuloExistente.getPalabrasClaves().orElse(null) : 
                nuevoArticulo.getPalabrasClaves().orElse(null))
            .conResumen(nuevoArticulo.getResumen().isEmpty() ? 
                articuloExistente.getResumen().orElse(null) : 
                nuevoArticulo.getResumen().orElse(null))
            .build();
        
        // Registrar evento de actualización con el artículo antes de ser modificado
        registrarEvento(articuloExistente, TipoEvento.ACTUALIZACION);
        
        // Actualizar el artículo
        articulos.put(idArticulo, articuloActualizado);
        
        return Optional.of(articuloActualizado);
    }
    
    @Override
    public Optional<Boolean> eliminar(Optional<Long> id) {
        if (id.isEmpty()) {
            return Optional.empty();
        }
        
        Long idArticulo = id.get();
        ArtCientificoDTO articuloEliminado = articulos.remove(idArticulo);
        
        if (articuloEliminado != null) {
            // Registrar evento de eliminación
            registrarEvento(articuloEliminado, TipoEvento.ELIMINACION);
            return Optional.of(true);
        }
        
        return Optional.of(false);
    }
    
    @Override
    public Map<ArtCientificoDTO, LocalDateTime> obtenerHistorialEliminados() {
        return historialEventos.stream()
            .filter(evento -> evento.getTipoEvento() == TipoEvento.ELIMINACION)
            .collect(Collectors.toMap(
                EventoHistorial::getArticulo,
                EventoHistorial::getFechaEvento,
                (fecha1, fecha2) -> fecha1.isAfter(fecha2) ? fecha1 : fecha2 // En caso de duplicados, nos quedamos con la fecha más reciente
            ));
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurarArticulo(Long id) {
        // Buscamos el artículo eliminado más reciente con el ID especificado
        Optional<EventoHistorialImpl> eventoEliminacion = historialEventos.stream()
            .filter(evento -> evento.getTipoEvento() == TipoEvento.ELIMINACION)
            .filter(evento -> evento.getArticulo().getId().isPresent() && evento.getArticulo().getId().get().equals(id))
            .max((e1, e2) -> e1.getFechaEvento().compareTo(e2.getFechaEvento()));
        
        if (eventoEliminacion.isEmpty()) {
            return Optional.empty();
        }
        
        ArtCientificoDTO articuloEliminado = eventoEliminacion.get().getArticulo();
        
        // Verificar si el ID ya existe (conflicto)
        if (articulos.containsKey(id)) {
            // No podemos restaurar si el ID ya está en uso
            return Optional.empty();
        }
        
        // Restaurar el artículo
        articulos.put(id, articuloEliminado);
        
        // Registrar evento de restauración
        registrarEvento(articuloEliminado, TipoEvento.RESTAURACION);
        
        return Optional.of(articuloEliminado);
    }

    @Override
    public List<EventoHistorial> obtenerHistorialEventos() {
        return new ArrayList<>(historialEventos);
    }

    @Override
    public List<EventoHistorial> obtenerHistorialPorTipo(TipoEvento tipoEvento) {
        return historialEventos.stream()
            .filter(evento -> evento.getTipoEvento() == tipoEvento)
            .collect(Collectors.toList());
    }

    @Override
    public List<EventoHistorial> obtenerHistorialPorArticulo(Long id) {
        return historialEventos.stream()
            .filter(evento -> evento.getArticulo().getId().isPresent() && 
                   evento.getArticulo().getId().get().equals(id))
            .collect(Collectors.toList());
    }
} 