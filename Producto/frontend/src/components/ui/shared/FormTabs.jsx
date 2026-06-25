import DetailTable from "../../layout/DetailTable";
import CostosFijosPanel from "./CostosFijosPanel";
import PlantillasPanel from "./PlantillasPanel";
import LogotipoPanel from "./LogotipoPanel";
import CintasPanel from "./CintasPanel";
import ProductosCotizacionPanel from "./ProductosCotizacionPanel";
import Articulos from "../../pages/commercial/costing/components/items/Articulos";
import TabButton from "../TabButton";

export default function FormTabs({
  activeTab,
  setActiveTab,
  formData,
  setFormData,
  handleAddItem,
  handleUpdateItem,
  handleRemoveItem,
  readOnly = false
}) {
  const tabs = [];
  
  if (formData.tipo === "COTIZACION" || formData.tipo === "SCOT") {
    tabs.push({ id: "prendas", label: "Productos" });
  } else {
    tabs.push({ id: "materias_primas", label: "Materias Primas" });
  }

  if (formData.tipo !== "COTIZACION" && formData.tipo !== "SCOT") {
    tabs.push({ id: "costos_fijos", label: "Costos Fijos" });
    tabs.push({ id: "plantillas", label: "Plantillas" });

    if (formData.hasLogo) tabs.push({ id: "logotipo", label: "Logotipo" });
    if (formData.hasCintas) tabs.push({ id: "cintas", label: "Cintas" });
  }

  return (
    <div className="space-y-8">
      <div className="flex gap-2 flex-wrap">
        {tabs.map(tab => (
          <TabButton
            key={tab.id}
            active={activeTab === tab.id}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </TabButton>
        ))}
      </div>

      {activeTab === "materias_primas" && (
        <div className="space-y-10">
          <DetailTable
            title="Telas"
            data={formData.telas}
            onAdd={() => !readOnly && handleAddItem("telas")}
            onUpdate={(id, field, value) =>
              !readOnly && handleUpdateItem("telas", id, field, value)
            }
            onRemove={(id) => !readOnly && handleRemoveItem("telas", id)}
            readOnly={readOnly}
          />

          <DetailTable
            title="Accesorios"
            data={formData.accesorios}
            onAdd={() => !readOnly && handleAddItem("accesorios")}
            onUpdate={(id , field, value) =>
              !readOnly && handleUpdateItem("accesorios", id, field, value)
            }
            onRemove={(id) => !readOnly && handleRemoveItem("accesorios", id)}
            readOnly={readOnly}
          />
        </div>
      )}

      {activeTab === "prendas" && (
        formData.tipo === "SCOT" ? (
          <Articulos
            data={formData.prendas}
            onAdd={() => !readOnly && handleAddItem("prendas")}
            onUpdate={(id, field, value) =>
              !readOnly && handleUpdateItem("prendas", id, field, value)
            }
            onRemove={(id) => !readOnly && handleRemoveItem("prendas", id)}
            readOnly={readOnly}
          />
        ) : (
          <ProductosCotizacionPanel
            data={formData.prendas}
            onAdd={() => !readOnly && handleAddItem("prendas")}
            onUpdate={(id, field, value) =>
              !readOnly && handleUpdateItem("prendas", id, field, value)
            }
            onRemove={(id) => !readOnly && handleRemoveItem("prendas", id)}
            readOnly={readOnly}
          />
        )
      )}

      {activeTab === "costos_fijos" && (
        <CostosFijosPanel
          costos={formData.costosFijos}
          readOnly={readOnly}
          onChange={(field, value) =>
            !readOnly && setFormData((prev) => ({
              ...prev,
              costosFijos: {
                ...prev.costosFijos,
                [field]: value
              }
            }))
          }
        />
      )}

      {activeTab === "plantillas" && (
        <PlantillasPanel
          data={formData.plantillas}
          articuloDescripcion={formData.articuloDescripcion || ""}
          onAdd={() => !readOnly && handleAddItem("plantillas")}
          onUpdate={(id, field, value) =>
            !readOnly && handleUpdateItem("plantillas", id, field, value)
          }
          onRemove={(id) => !readOnly && handleRemoveItem("plantillas", id)}
          readOnly={readOnly}
        />
      )}

      {activeTab === "logotipo" && formData.hasLogo && (
        <LogotipoPanel
          data={formData.logotipo}
          onAdd={() => !readOnly && handleAddItem("logotipo")}
          onUpdate={(id, field, value) =>
            !readOnly && handleUpdateItem("logotipo", id, field, value)
          }
          onRemove={(id) => !readOnly && handleRemoveItem("logotipo", id)}
          readOnly={readOnly}
          nombrePrenda={formData.nombrePrenda || formData.nombre || formData.articuloDescripcion}
        />
      )}

      {activeTab === "cintas" && formData.hasCintas && (
        <CintasPanel
          data={formData.cintas}
          onAdd={() => !readOnly && handleAddItem("cintas")}
          onUpdate={(id, field, value) =>
            !readOnly && handleUpdateItem("cintas", id, field, value)
          }
          onRemove={(id) => !readOnly && handleRemoveItem("cintas", id)}
          readOnly={readOnly}
        />
      )}
    </div>
  );
}
