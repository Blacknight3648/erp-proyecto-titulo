package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Comuna;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComunaResponse {

    private Long comunaId;
    private String nombreComuna;
    private RegionResponse region;

    public static ComunaResponse fromDomain(Comuna comuna) {
        if (comuna == null) return null;
        return ComunaResponse.builder()
                .comunaId(comuna.getComunaId())
                .nombreComuna(comuna.getNombreComuna())
                .region(RegionResponse.fromDomain(comuna.getRegion()))
                .build();
    }
}
