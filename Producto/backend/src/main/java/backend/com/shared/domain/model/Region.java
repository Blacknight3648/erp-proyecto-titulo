package backend.com.shared.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    private Long regionId;
    private String nombreRegion;
    private Pais pais;
}
