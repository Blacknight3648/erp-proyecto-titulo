package backend.com.comercial.domain.model;

import lombok.Getter;
import java.util.List;
import java.util.ArrayList;

@Getter
public class SCOSPlantilla {
    private Long id;
    private String nombre;
    private String descripcion;
    
    // Nuevos campos técnicos opcionales
    private String nombrePrenda;
    private String forro;
    private String relleno;
    private String gorro;
    private String cuello;
    private String abotonaduraCierre;
    private String cortesAplicaciones;
    private String fuelles;
    private String mangas;
    private String pretinasRuedo;
    private String bolsillos;
    private String cintaDetalle;
    private String logoDetalle;
    private String colorForro;
    private String accesoriosDetalle;
    private String obsModelo;
    private String genero;
    private String customFields;
    private List<String> camposActivos;

    private List<PlantillaTela> plantillaTelas;
    private List<PlantillaAccesorio> plantillaAccesorios;
    private List<SCOSLogotipo> plantillaLogotipos;
    private List<SCOSPlantillaMaterialVinculo> vinculos;

    // Mano de Obra
    private java.math.BigDecimal moPrenda;
    private java.math.BigDecimal moCosturaSellada;
    private java.math.BigDecimal moAcolchado;

    public SCOSPlantilla(Long id, String nombre, String descripcion, String nombrePrenda, String forro, String relleno, String gorro, String cuello, String abotonaduraCierre, String cortesAplicaciones, String fuelles, String mangas, String pretinasRuedo, String bolsillos, String cintaDetalle, String logoDetalle, String colorForro, String accesoriosDetalle, String obsModelo, String genero, String customFields, List<String> camposActivos, List<PlantillaTela> plantillaTelas, List<PlantillaAccesorio> plantillaAccesorios, List<SCOSLogotipo> plantillaLogotipos, List<SCOSPlantillaMaterialVinculo> vinculos, java.math.BigDecimal moPrenda, java.math.BigDecimal moCosturaSellada, java.math.BigDecimal moAcolchado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombrePrenda = nombrePrenda;
        this.forro = forro;
        this.relleno = relleno;
        this.gorro = gorro;
        this.cuello = cuello;
        this.abotonaduraCierre = abotonaduraCierre;
        this.cortesAplicaciones = cortesAplicaciones;
        this.fuelles = fuelles;
        this.mangas = mangas;
        this.pretinasRuedo = pretinasRuedo;
        this.bolsillos = bolsillos;
        this.cintaDetalle = cintaDetalle;
        this.logoDetalle = logoDetalle;
        this.colorForro = colorForro;
        this.accesoriosDetalle = accesoriosDetalle;
        this.obsModelo = obsModelo;
        this.genero = genero;
        this.customFields = customFields;
        this.camposActivos = camposActivos != null ? camposActivos : new ArrayList<>();
        this.plantillaTelas = plantillaTelas != null ? plantillaTelas : new ArrayList<>();
        this.plantillaAccesorios = plantillaAccesorios != null ? plantillaAccesorios : new ArrayList<>();
        this.plantillaLogotipos = plantillaLogotipos != null ? plantillaLogotipos : new ArrayList<>();
        this.vinculos = vinculos != null ? vinculos : new ArrayList<>();
        this.moPrenda = moPrenda != null ? moPrenda : java.math.BigDecimal.ZERO;
        this.moCosturaSellada = moCosturaSellada != null ? moCosturaSellada : java.math.BigDecimal.ZERO;
        this.moAcolchado = moAcolchado != null ? moAcolchado : java.math.BigDecimal.ZERO;
    }
}
