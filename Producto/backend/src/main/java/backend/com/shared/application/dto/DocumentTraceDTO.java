package backend.com.shared.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DocumentTraceDTO {
    private String tipoDocumento;
    private Long id;
    private String numero;
    private String estado;
    private LocalDate fecha;
}
