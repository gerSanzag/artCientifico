package service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Objects;

import dto.ArtCientificoDTO;
import repository.ArtCientificoRepository;
import repository.ArtCientificoRepositoryFactory;
import common.types.TipoEvento;
import repository.EventoHistorial;
import service.ArtCientificoService;

/**
 * Implementación del servicio de artículos científicos
 * Delega directamente al repositorio ya que ambos trabajan con DTOs
 */
public class ArtCientificoServiceImpl implements ArtCientificoService {
    
    private final ArtCientificoRepository repositorio;
    
    /**
     * Constructor que obtiene el repositorio mediante la fábrica
     */
    public ArtCientificoServiceImpl() {
        this.repositorio = ArtCientificoRepositoryFactory.getRepositorio();
    }
    
    @Override
    public Optional<ArtCientificoDTO> guardarArticulo(Optional<ArtCientificoDTO> articuloDTOOpt) {
        // Delegar directamente al repositorio
        return repositorio.guardar(articuloDTOOpt);
    }
    
    @Override
    public List<ArtCientificoDTO> buscarPor(Predicate<ArtCientificoDTO> predicado, boolean soloUno) {
        // Delegar directamente al repositorio
        return repositorio.buscarPor(predicado, soloUno);
    }
    
    @Override
    public Optional<ArtCientificoDTO> buscarUno(Predicate<ArtCientificoDTO> predicado) {
        // Usamos el método buscarPor con soloUno=true para obtener como máximo un resultado
        List<ArtCientificoDTO> resultado = buscarPor(predicado, true);
        // Convertimos el resultado a Optional
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
    }
    
    @Override
    public List<ArtCientificoDTO> obtenerTodosLosArticulos() {
        // Delegar directamente al repositorio
        return repositorio.obtenerTodos();
    }
    
    @Override
    public Optional<ArtCientificoDTO> actualizarArticulo(Optional<Long> id, Optional<ArtCientificoDTO> articuloDTOOpt) {
        // Delegar directamente al repositorio
        return repositorio.actualizar(id, articuloDTOOpt);
    }
    
    @Override
    public Optional<Boolean> eliminarArticulo(Optional<Long> id) {
        // Delegar directamente al repositorio
        return repositorio.eliminar(id);
    }
    
    @Override
    public List<EventoHistorial> obtenerHistorialEventos() {
        // Delegar directamente al repositorio
        return repositorio.obtenerHistorialEventos();
    }
    
    @Override
    public List<EventoHistorial> obtenerHistorialPorTipo(TipoEvento tipoEvento) {
        // Delegar directamente al repositorio
        return repositorio.obtenerHistorialPorTipo(tipoEvento);
    }
    
    @Override
    public List<EventoHistorial> obtenerHistorialPorArticulo(Long id) {
        // Delegar directamente al repositorio
        return repositorio.obtenerHistorialPorArticulo(id);
    }
    
    @Override
    @Deprecated
    public Map<ArtCientificoDTO, LocalDateTime> obtenerHistorialEliminados() {
        // Delegar directamente al repositorio
        return repositorio.obtenerHistorialEliminados();
    }
    
    @Override
    public Optional<ArtCientificoDTO> restaurarArticulo(Long id) {
        // Delegar directamente al repositorio
        return repositorio.restaurarArticulo(id);
    }
    
    /**
     * Compara un valor del artículo con un valor de búsqueda (si está presente)
     * @param valorArticulo valor a comparar del artículo
     * @param valorBuscado valor de búsqueda (opcional)
     * @return true si coinciden o si no se especificó un valor de búsqueda
     */
    private static <T> boolean coincide(Optional<T> valorArticulo, Optional<T> valorBuscado) {
        return valorBuscado.isEmpty() || 
               (valorArticulo.isPresent() && valorBuscado.isPresent() && 
                Objects.equals(valorArticulo.get(), valorBuscado.get()));
    }
    
    /**
     * Método de utilidad para crear predicados de búsqueda por campos comunes
     */
    public static Predicate<ArtCientificoDTO> crearPredicado(Optional<Long> id, Optional<String> nombre, 
                                                          Optional<String> autor, Optional<Integer> anio) {
        return articulo -> 
            coincide(articulo.getId(), id) && 
            coincide(articulo.getNombre(), nombre) && 
            coincide(articulo.getAutor(), autor) && 
            coincide(articulo.getAnio(), anio);
    }
} 