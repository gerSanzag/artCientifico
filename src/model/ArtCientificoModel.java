package model;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase que representa un artículo científico
 */
public class ArtCientificoModel {
    
    private final String nombre;
    private final String autor;
    private final List<String> palabrasClaves;
    private final Integer anio;
    private final String resumen;
    
    private ArtCientificoModel(Builder builder) {
        this.nombre = builder.nombre;
        this.autor = builder.autor;
        this.palabrasClaves = builder.palabrasClaves != null ? builder.palabrasClaves : new ArrayList<>();
        this.anio = builder.anio;
        this.resumen = builder.resumen;
    }
    
    public Optional<String> getNombre() {
        return Optional.ofNullable(nombre);
    }
    
    public Optional<String> getAutor() {
        return Optional.ofNullable(autor);
    }
    
    public Optional<List<String>> getPalabrasClaves() {
        return Optional.ofNullable(palabrasClaves)
                      .map(ArrayList::new);
    }
    
    public Optional<Integer> getAnio() {
        return Optional.ofNullable(anio);
    }
    
    public Optional<String> getResumen() {
        return Optional.ofNullable(resumen);
    }
    
    @Override
    public String toString() {
        return "ArtCientificoModel{" +
                "nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", palabrasClaves=" + palabrasClaves +
                ", anio=" + anio +
                ", resumen='" + resumen + '\'' +
                '}';
    }
    
    /**
     * Builder para crear instancias de ArtCientificoModel
     */
    public static class Builder {
        private String nombre;
        private String autor;
        private List<String> palabrasClaves;
        private Integer anio;
        private String resumen;
        
        public Builder() {
            this.palabrasClaves = new ArrayList<>();
        }
        
        /**
         * Establece el nombre del artículo científico
         * @param nombre el nombre del artículo (encapsulado en Optional)
         * @return el builder para encadenamiento
         */
        public Builder conNombre(Optional<String> nombre) {
            nombre.ifPresent(n -> this.nombre = n);
            return this;
        }
        
        /**
         * Establece el autor del artículo científico
         * @param autor el autor del artículo (encapsulado en Optional)
         * @return el builder para encadenamiento
         */
        public Builder conAutor(Optional<String> autor) {
            autor.ifPresent(a -> this.autor = a);
            return this;
        }
        
        /**
         * Establece la lista de palabras clave del artículo científico
         * @param palabrasClaves lista de palabras clave (encapsulada en Optional)
         * @return el builder para encadenamiento
         */
        public Builder conPalabrasClaves(Optional<List<String>> palabrasClaves) {
            palabrasClaves.ifPresent(lista -> this.palabrasClaves = new ArrayList<>(lista));
            return this;
        }
        
        /**
         * Establece el año del artículo científico
         * @param anio el año (encapsulado en Optional)
         * @return el builder para encadenamiento
         */
        public Builder conAnio(Optional<Integer> anio) {
            anio.ifPresent(a -> this.anio = a);
            return this;
        }
        
        /**
         * Establece el resumen del artículo científico
         * @param resumen el resumen (encapsulado en Optional)
         * @return el builder para encadenamiento
         */
        public Builder conResumen(Optional<String> resumen) {
            resumen.ifPresent(r -> this.resumen = r);
            return this;
        }
        
        public ArtCientificoModel build() {
            return new ArtCientificoModel(this);
        }
    }
} 