package dto;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * DTO (Data Transfer Object) para transportar datos de artículos científicos
 * entre las capas de la aplicación
 */
public class ArtCientificoDTO {
    
    private final String nombre;
    private final String autor;
    private List<String> palabrasClaves;
    private final Integer anio;
    private String resumen;
    
    /**
     * Constructor para crear un DTO con los campos obligatorios
     * @param nombre nombre del artículo (puede ser null)
     * @param autor autor del artículo (puede ser null)
     * @param anio año de publicación (puede ser null)
     */
    public ArtCientificoDTO(String nombre, String autor, Integer anio) {
        this.nombre = nombre;
        this.autor = autor;
        this.palabrasClaves = new ArrayList<>();
        this.anio = anio;
        this.resumen = null;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public List<String> getPalabrasClaves() {
        return new ArrayList<>(palabrasClaves);
    }
    
    /**
     * Establece la lista de palabras clave
     * @param palabrasClaves lista de palabras clave (encapsulada en Optional)
     */
    public void setPalabrasClaves(Optional<List<String>> palabrasClaves) {
        this.palabrasClaves = palabrasClaves
            .map(ArrayList::new)
            .orElseGet(ArrayList::new);
    }
    
    /**
     * Método alternativo que acepta directamente la lista de palabras clave
     * @param palabrasClaves lista de palabras clave
     */
    public void setPalabrasClaves(List<String> palabrasClaves) {
        setPalabrasClaves(Optional.ofNullable(palabrasClaves));
    }
    
    /**
     * Añade una palabra clave
     * @param palabraClave palabra clave (encapsulada en Optional)
     */
    public void addPalabraClave(Optional<String> palabraClave) {
        if (this.palabrasClaves == null) {
            this.palabrasClaves = new ArrayList<>();
        }
        palabraClave.ifPresent(this.palabrasClaves::add);
    }
    
    /**
     * Método alternativo que acepta directamente la palabra clave
     * @param palabraClave palabra clave
     */
    public void addPalabraClave(String palabraClave) {
        addPalabraClave(Optional.ofNullable(palabraClave));
    }
    
    public Integer getAnio() {
        return anio;
    }
    
    public String getResumen() {
        return resumen;
    }
    
    /**
     * Establece el resumen
     * @param resumen resumen (encapsulado en Optional)
     */
    public void setResumen(Optional<String> resumen) {
        this.resumen = resumen.orElse(null);
    }
    
    /**
     * Método alternativo que acepta directamente el resumen
     * @param resumen resumen
     */
    public void setResumen(String resumen) {
        setResumen(Optional.ofNullable(resumen));
    }
    
    @Override
    public String toString() {
        return "ArtCientificoDTO{" +
                "nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", palabrasClaves=" + palabrasClaves +
                ", anio=" + anio +
                ", resumen='" + resumen + '\'' +
                '}';
    }
} 