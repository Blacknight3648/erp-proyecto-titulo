package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Region;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionResponse {

    private Long regionId;
    private String nombreRegion;
    private PaisResponse pais;

    public static RegionResponse fromDomain(Region region) {
        if (region == null) return null;
        return RegionResponse.builder()
                .regionId(region.getRegionId())
                .nombreRegion(region.getNombreRegion())
                .pais(PaisResponse.fromDomain(region.getPais()))
                .build();
    }
}
