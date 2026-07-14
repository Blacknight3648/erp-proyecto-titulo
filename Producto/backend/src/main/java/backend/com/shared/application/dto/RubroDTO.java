package backend.com.shared.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubroDTO {
    private Long rubroId;
    private String descripcionRubro;
    private String nombreRubro;
    private String siglaRubro;
}
