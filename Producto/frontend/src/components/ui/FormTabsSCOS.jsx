import TabButton from "./TabButton";
import PlantillasPanel from "./PlantillasPanel";
import LogotipoPanel from "./LogotipoPanel";
import TelasSCOSPanel from "./TelasSCOSPanel";
import AccesoriosSCOSPanel from "./AccesoriosSCOSPanel";

export default function FormTabsSCOS({
                                         activeTab,
                                         setActiveTab,
                                         formData,
                                         setFormData,
                                         handleAddItem,
                                         handleUpdateItem,
                                         handleRemoveItem,
                                         readOnly = false
                                     }) {
    const tabs = [
        { id: "plantillas",  label: "Especificación Técnica" },
        { id: "telas",       label: "Telas" },
        { id: "accesorios",  label: "Accesorios" },
    ];

    if (formData.hasLogo) {
        tabs.push({ id: "logotipo", label: "Logotipo" });
    }

    return (
        <div className="space-y-8">
            {/* Botones de tabs */}
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

            {/* Tab: Especificación Técnica */}
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

            {/* Tab: Telas */}
            {activeTab === "telas" && (
                <TelasSCOSPanel
                    data={formData.telas}
                    onAdd={() => !readOnly && handleAddItem("telas")}
                    onUpdate={(id, field, value) =>
                        !readOnly && handleUpdateItem("telas", id, field, value)
                    }
                    onRemove={(id) => !readOnly && handleRemoveItem("telas", id)}
                    readOnly={readOnly}
                />
            )}

            {/* Tab: Accesorios */}
            {activeTab === "accesorios" && (
                <AccesoriosSCOSPanel
                    data={formData.accesorios}
                    onAdd={() => !readOnly && handleAddItem("accesorios")}
                    onUpdate={(id, field, value) =>
                        !readOnly && handleUpdateItem("accesorios", id, field, value)
                    }
                    onRemove={(id) => !readOnly && handleRemoveItem("accesorios", id)}
                    readOnly={readOnly}
                />
            )}

            {/* Tab: Logotipo (solo si hasLogo) */}
            {activeTab === "logotipo" && formData.hasLogo && (
                <LogotipoPanel
                    data={formData.logotipo}
                    onAdd={() => !readOnly && handleAddItem("logotipo")}
                    onUpdate={(id, field, value) =>
                        !readOnly && handleUpdateItem("logotipo", id, field, value)
                    }
                    onRemove={(id) => !readOnly && handleRemoveItem("logotipo", id)}
                    readOnly={readOnly}
                />
            )}
        </div>
    );
}
