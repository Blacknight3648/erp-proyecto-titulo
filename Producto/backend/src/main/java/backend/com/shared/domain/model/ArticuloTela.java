package backend.com.shared.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloTela {

    private Integer id;
    private FamiliaTela familiaTela;
    private ClasificacionTecnica clasificacionTecnica;
    private Composicion composicion;
    private GramajeTela gramaje;
    private String abreviaturasHistoricas;
    private String usoTipico;
    private String observacionProveedor;
    private List<ColorTela> colores;
    private List<AtributoTecnico> atributosTecnicos;
}