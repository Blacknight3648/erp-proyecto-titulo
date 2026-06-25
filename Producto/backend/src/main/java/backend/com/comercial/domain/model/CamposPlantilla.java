package backend.com.comercial.domain.model;

import lombok.*;

/**
 * Catálogo de campos disponibles para describir una plantilla de prenda.
 * Ej: "Forro", "Mangas", "Capucha", "Cuello", "Bolsillos".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamposPlantilla {

    private Long idPlantilla;
    private String nombreCampo;

    @Builder.Default
    private boolean activo = true;
}
