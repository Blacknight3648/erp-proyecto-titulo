package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoTecnico {

    private Integer idAtributo;
    private String codigoAtributo;
    private String clasificacion;
    private String descripcionTecnica;
    private String impactoErp;
}