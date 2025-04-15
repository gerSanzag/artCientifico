package mapper;

import java.util.Optional;
import java.util.ArrayList;
import java.util.function.Function;

import model.ArtCientificoModel;
import dto.ArtCientificoDTO;

/**
 * Clase para mapear entre Model y DTO de artículos científicos
 * usando un enfoque funcional
 */
public class ArtCientificoMapper {
    
    private ArtCientificoMapper() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Función que convierte un Model a DTO
     */
    public static final Function<Optional<ArtCientificoModel>, Optional<ArtCientificoDTO>> toDTO = 
        modelOpt -> modelOpt.map(model -> 
            new ArtCientificoDTO.BuilderDTO()
                .conId(model.getId().orElse(null))
                .conNombre(model.getNombre().orElse(null))
                .conAutor(model.getAutor().orElse(null))
                .conPalabrasClaves(model.getPalabrasClaves().orElse(null))
                .conAnio(model.getAnio().orElse(null))
                .conResumen(model.getResumen().orElse(null))
                .build()
        );
    
    /**
     * Función que convierte un DTO a Model
     */
    public static final Function<Optional<ArtCientificoDTO>, Optional<ArtCientificoModel>> toModel = 
        dtoOpt -> dtoOpt.map(dto -> 
            new ArtCientificoModel.BuilderModel()
                .conId(dto.getId())
                .conNombre(dto.getNombre())
                .conAutor(dto.getAutor())
                .conPalabrasClaves(dto.getPalabrasClaves())
                .conAnio(dto.getAnio())
                .conResumen(dto.getResumen())
                .build()
        );
    
    /**
     * Función que crea un BuilderDTO pre-cargado con los valores del DTO
     * Útil para modificar/actualizar un artículo científico ya existente
     * manteniendo sus valores originales y cambiando solo los necesarios
     */
    public static final Function<Optional<ArtCientificoDTO>, ArtCientificoDTO.BuilderDTO> toDTOBuilder = 
        dtoOpt -> {
            ArtCientificoDTO.BuilderDTO builder = new ArtCientificoDTO.BuilderDTO();
            
            dtoOpt.ifPresent(dto -> {
                dto.getId().ifPresent(builder::conId);
                dto.getNombre().ifPresent(builder::conNombre);
                dto.getAutor().ifPresent(builder::conAutor);
                dto.getPalabrasClaves().ifPresent(builder::conPalabrasClaves);
                dto.getAnio().ifPresent(builder::conAnio);
                dto.getResumen().ifPresent(builder::conResumen);
            });
            
            return builder;
        };
    
    /**
     * Función que crea un BuilderModel pre-cargado con los valores del Model
     * Útil para modificar/actualizar un artículo científico ya existente
     * manteniendo sus valores originales y cambiando solo los necesarios
     */
    public static final Function<Optional<ArtCientificoModel>, ArtCientificoModel.BuilderModel> toModelBuilder = 
        modelOpt -> {
            ArtCientificoModel.BuilderModel builder = new ArtCientificoModel.BuilderModel();
            
            modelOpt.ifPresent(model -> {
                model.getId().ifPresent(i -> builder.conId(Optional.of(i)));
                model.getNombre().ifPresent(n -> builder.conNombre(Optional.of(n)));
                model.getAutor().ifPresent(a -> builder.conAutor(Optional.of(a)));
                model.getPalabrasClaves().ifPresent(p -> builder.conPalabrasClaves(Optional.of(new ArrayList<>(p))));
                model.getAnio().ifPresent(a -> builder.conAnio(Optional.of(a)));
                model.getResumen().ifPresent(r -> builder.conResumen(Optional.of(r)));
            });
            
            return builder;
        };
} 