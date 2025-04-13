package dto;

import model.ArtCientificoModel;
import java.util.Optional;
import java.util.List;

/**
 * Clase utilitaria para convertir entre objetos DTO y objetos del modelo
 */
public class ArtCientificoConverter {
    
    private ArtCientificoConverter() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Convierte un DTO a un objeto del modelo
     * @param dto el objeto DTO a convertir
     * @return un objeto del modelo
     */
    public static ArtCientificoModel dtoToModel(ArtCientificoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ArtCientificoModel.Builder builder = new ArtCientificoModel.Builder();
        
        // Envolvemos los valores en Optional antes de pasarlos al builder
        builder.conNombre(Optional.ofNullable(dto.getNombre()));
        builder.conAutor(Optional.ofNullable(dto.getAutor()));
        
        // Envolvemos la lista de palabras clave en un Optional
        Optional<List<String>> palabrasClaves = Optional.ofNullable(dto.getPalabrasClaves())
                                                        .filter(lista -> !lista.isEmpty());
        builder.conPalabrasClaves(palabrasClaves);
        
        builder.conAnio(Optional.ofNullable(dto.getAnio()));
        builder.conResumen(Optional.ofNullable(dto.getResumen()));
                
        return builder.build();
    }
    
    /**
     * Convierte un objeto del modelo a un DTO
     * @param model el objeto del modelo a convertir
     * @return un objeto DTO
     */
    public static ArtCientificoDTO modelToDto(ArtCientificoModel model) {
        if (model == null) {
            return null;
        }
        
        // Obtenemos los valores del modelo
        String nombre = model.getNombre().orElse(null);
        String autor = model.getAutor().orElse(null);
        Integer anio = model.getAnio().orElse(null);
        
        // Creamos el DTO con los campos finales
        ArtCientificoDTO dto = new ArtCientificoDTO(nombre, autor, anio);
        
        // Establecemos los campos no finales
        dto.setPalabrasClaves(model.getPalabrasClaves());
        
        model.getResumen()
             .ifPresent(dto::setResumen);
             
        return dto;
    }
} 