package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RecepcionOCItem {

    private Long idRecepcionItem;
    private Long recepcionId;
    private Long ocItemId;
    private BigDecimal cantidadRecibida;
    private BigDecimal cantidadConforme;
    private BigDecimal cantidadRechazada;
    private String motivoRechazo;

    public RecepcionOCItem(Long idRecepcionItem, Long recepcionId, Long ocItemId,
            BigDecimal cantidadRecibida, BigDecimal cantidadConforme,
            BigDecimal cantidadRechazada, String motivoRechazo) {
        this.idRecepcionItem = idRecepcionItem;
        this.recepcionId = recepcionId;
        this.ocItemId = ocItemId;
        this.cantidadRecibida = cantidadRecibida != null ? cantidadRecibida : BigDecimal.ZERO;
        this.cantidadConforme = cantidadConforme != null ? cantidadConforme : this.cantidadRecibida;
        this.cantidadRechazada = cantidadRechazada != null ? cantidadRechazada : BigDecimal.ZERO;
        this.motivoRechazo = motivoRechazo;
    }
}
