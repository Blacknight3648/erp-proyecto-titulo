package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rubro {

    private Long rubroId;
    private String nombreRubro;
    private String descripcionRubro;
    private String siglaRubro;

}
