import { Shirt } from "lucide-react";
import LogotipoPanel from "../../../components/shared/LogotipoPanel";
import TelasSCOSPanel from "../../../components/shared/TelasSCOSPanel";
import AccesoriosSCOSPanel from "../../../components/shared/AccesoriosSCOSPanel";
import PlantillasPanel from "../../../components/shared/PlantillasPanel";

export default function FichaPrendaPanel({ formData, readOnly, onAddItem, onUpdateItem, onRemoveItem }) {
    return (
        <div className="space-y-8">
            {formData.hasLogo && (
                <div className="animate-in slide-in-from-top-4 duration-300 bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden p-6 md:p-8 bg-slate-50/50">
                    <LogotipoPanel
                        data={formData.plantillas?.[0]?.logotipos || []}
                        onAdd={() => !readOnly && onAddItem("logotipo")}
                        onUpdate={(id, field, value) => !readOnly && onUpdateItem("logotipo", id, field, value)}
                        onRemove={(id) => !readOnly && onRemoveItem("logotipo", id)}
                        readOnly={readOnly}
                        nombrePrenda={formData.nombrePrenda}
                    />
                </div>
            )}

            <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
                <div className="bg-slate-950 px-6 py-4 flex items-center gap-2.5">
                    <Shirt className="w-4 h-4 text-blue-400" />
                    <h2 className="text-sm font-semibold tracking-wide text-white">
                        Ficha Técnica
                    </h2>
                </div>

                <div className="p-6 md:p-8 space-y-8 bg-slate-50/50">
                    <TelasSCOSPanel
                        data={formData.plantillas?.[0]?.telas || []}
                        onAdd={() => !readOnly && onAddItem("telas")}
                        onUpdate={(id, field, value) => !readOnly && onUpdateItem("telas", id, field, value)}
                        onRemove={(id) => !readOnly && onRemoveItem("telas", id)}
                        readOnly={readOnly}
                    />

                    <AccesoriosSCOSPanel
                        data={formData.plantillas?.[0]?.accesorios || []}
                        onAdd={() => !readOnly && onAddItem("accesorios")}
                        onUpdate={(id, field, value) => !readOnly && onUpdateItem("accesorios", id, field, value)}
                        onRemove={(id) => !readOnly && onRemoveItem("accesorios", id)}
                        readOnly={readOnly}
                    />

                    <PlantillasPanel
                        data={formData.plantillas}
                        articuloDescripcion={formData.articuloDescripcion || ""}
                        telas={formData.plantillas?.[0]?.telas || []}
                        accesorios={formData.plantillas?.[0]?.accesorios || []}
                        onAdd={() => !readOnly && onAddItem("plantillas")}
                        onUpdate={(id, field, value) => !readOnly && onUpdateItem("plantillas", id, field, value)}
                        onRemove={(id) => !readOnly && onRemoveItem("plantillas", id)}
                        readOnly={readOnly}
                    />
                </div>
            </div>
        </div>
    );
}
