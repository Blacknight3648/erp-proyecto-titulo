package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorTela {

    private Integer idColor;
    private String codigoColor;
    private String descripcionColor;
    private Boolean esPantone;
}