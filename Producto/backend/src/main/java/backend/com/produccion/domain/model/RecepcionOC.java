package backend.com.produccion.domain.model;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class RecepcionOC {

    private Long idRecepcion;
    private Long ocId;
    private LocalDate fechaRecepcion;
    private String numeroGuia;
    private String responsable;
    private String observaciones;
    private List<RecepcionOCItem> items = new ArrayList<>();

    public RecepcionOC(Long idRecepcion, Long ocId, LocalDate fechaRecepcion, String numeroGuia,
            String responsable, String observaciones, List<RecepcionOCItem> items) {
        this.idRecepcion = idRecepcion;
        this.ocId = ocId;
        this.fechaRecepcion = fechaRecepcion != null ? fechaRecepcion : LocalDate.now();
        this.numeroGuia = numeroGuia;
        this.responsable = responsable;
        this.observaciones = observaciones;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static RecepcionOC crear(Long ocId, LocalDate fechaRecepcion, String numeroGuia,
            String responsable, String observaciones) {
        return new RecepcionOC(null, ocId,
                fechaRecepcion != null ? fechaRecepcion : LocalDate.now(),
                numeroGuia, responsable, observaciones, new ArrayList<>());
    }

    public void addItem(RecepcionOCItem item) {
        this.items.add(item);
    }

    public List<RecepcionOCItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
