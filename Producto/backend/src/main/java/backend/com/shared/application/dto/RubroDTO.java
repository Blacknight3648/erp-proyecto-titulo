package backend.com.shared.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubroDTO {
    private Long rubroId;
    private String descripcionRubro;
    private String nombreRubro;
}
