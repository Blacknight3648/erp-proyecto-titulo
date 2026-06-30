package backend.com.produccion.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOPResponse {
    private long opAtrasada;
    private long corteAtrasado;
    private long recepcionLogoAtrasado;
    private long envioAtrasado;
    private long devolucionTallerAtrasada;
    private long entregas7d;
}
