package dto;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase DTO (Data Transfer Object) para artículos científicos
 */
public class ArtCientificoDTO {
    
    private final Optional<String> nombre;
    private final Optional<String> autor;
    private final Optional<List<String>> palabrasClaves;
    private final Optional<Integer> anio;
    private final Optional<String> resumen;
    
    /**
     * Constructor privado para ArtCientificoDTO (usado por el BuilderDTO)
     */
    private ArtCientificoDTO(BuilderDTO builder) {
        this.nombre = builder.nombre;
        this.autor = builder.autor;
        this.palabrasClaves = builder.palabrasClaves;
        this.anio = builder.anio;
        this.resumen = builder.resumen;
    }
    
    /**
     * Obtiene el nombre del artículo científico
     * @return Optional que contiene el nombre o vacío si no existe
     */
    public Optional<String> getNombre() {
        return nombre;
    }
    
    /**
     * Obtiene el autor del artículo científico
     * @return Optional que contiene el autor o vacío si no existe
     */
    public Optional<String> getAutor() {
        return autor;
    }
    
    /**
     * Obtiene las palabras clave del artículo científico
     * @return Optional que contiene la lista de palabras clave o vacío si no existe
     */
    public Optional<List<String>> getPalabrasClaves() {
        return palabrasClaves.map(lista -> new ArrayList<>(lista));
    }
    
    /**
     * Obtiene el año de publicación del artículo científico
     * @return Optional que contiene el año o vacío si no existe
     */
    public Optional<Integer> getAnio() {
        return anio;
    }
    
    /**
     * Obtiene el resumen del artículo científico
     * @return Optional que contiene el resumen o vacío si no existe
     */
    public Optional<String> getResumen() {
        return resumen;
    }
    
    @Override
    public String toString() {
        return "ArtCientificoDTO{" +
                "nombre=" + nombre +
                ", autor=" + autor +
                ", palabrasClaves=" + palabrasClaves +
                ", anio=" + anio +
                ", resumen=" + resumen +
                '}';
    }
    
    /**
     * BuilderDTO para crear instancias de ArtCientificoDTO
     */
    public static class BuilderDTO {
        private Optional<String> nombre = Optional.empty();
        private Optional<String> autor = Optional.empty();
        private Optional<List<String>> palabrasClaves = Optional.empty();
        private Optional<Integer> anio = Optional.empty();
        private Optional<String> resumen = Optional.empty();
        
        public BuilderDTO() {
        }
        
        /**
         * Establece el nombre del artículo científico
         * @param nombre el nombre del artículo
         * @return el builder para encadenamiento
         */
        public BuilderDTO conNombre(String nombre) {
            this.nombre = Optional.ofNullable(nombre);
            return this;
        }
        
        /**
         * Establece el autor del artículo científico
         * @param autor el autor del artículo
         * @return el builder para encadenamiento
         */
        public BuilderDTO conAutor(String autor) {
            this.autor = Optional.ofNullable(autor);
            return this;
        }
        
        /**
         * Establece la lista de palabras clave del artículo científico
         * @param palabrasClaves lista de palabras clave
         * @return el builder para encadenamiento
         */
        public BuilderDTO conPalabrasClaves(List<String> palabrasClaves) {
            this.palabrasClaves = Optional.ofNullable(palabrasClaves)
                                         .map(lista -> new ArrayList<>(lista));
            return this;
        }
        
        /**
         * Establece el año del artículo científico
         * @param anio el año
         * @return el builder para encadenamiento
         */
        public BuilderDTO conAnio(Integer anio) {
            this.anio = Optional.ofNullable(anio);
            return this;
        }
        
        /**
         * Establece el resumen del artículo científico
         * @param resumen el resumen
         * @return el builder para encadenamiento
         */
        public BuilderDTO conResumen(String resumen) {
            this.resumen = Optional.ofNullable(resumen);
            return this;
        }
        
        /**
         * Construye y devuelve una instancia de ArtCientificoDTO
         * @return la instancia de ArtCientificoDTO construida
         */
        public ArtCientificoDTO build() {
            return new ArtCientificoDTO(this);
        }
    }
} 