package backend.com.shared.application.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloTelaDTO {

    private Integer id;
    private FamiliaTelaDTO familiaTela;
    private ClasificacionTecnicaDTO clasificacionTecnica;
    private ComposicionDTO composicion;
    private GramajeTelaDTO gramaje;
    private String abreviaturasHistoricas;
    private String usoTipico;
    private String observacionProveedor;
    private List<ColorTelaDTO> colores;
    private List<AtributoTecnicoDTO> atributosTecnicos;
}