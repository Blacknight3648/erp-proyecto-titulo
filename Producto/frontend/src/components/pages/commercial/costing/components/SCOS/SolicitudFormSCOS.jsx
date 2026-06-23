import { useState, useEffect, useCallback } from "react";
import SolicitudHeader from "../../../../../../components/layout/SolicitudHeader.jsx"
import { useClientes } from "../../../../../../hooks/useClientes.js";
import { useVendedores } from "../../../../../../hooks/useVendedores.js";
import { usePlantillas } from "../../../../../../hooks/usePlantillas.js";
import LogotipoPanel from "../../../../../../components/ui/shared/LogotipoPanel.jsx";
import TelasSCOSPanel from "../../../../../../components/ui/shared/TelasSCOSPanel.jsx";
import AccesoriosSCOSPanel from "../../../../../../components/ui/shared/AccesoriosSCOSPanel.jsx";
import PlantillasPanel from "../../../../../../components/ui/shared/PlantillasPanel.jsx";
import { ClipboardList, Shirt } from "lucide-react";

const generateId = () => {
    try {
        return crypto.randomUUID();
    } catch (e) {
        return Math.random().toString(36).substring(2, 15);
    }
};

export default function SolicitudFormSCOS({
    formData,
    setFormData,
    onSave,
    onBack,
    isEditing = false,
    setIsEditing
}) {
    const readOnly = !isEditing;
    const { clientes } = useClientes();
    const { vendedores } = useVendedores();
    const { plantillas } = usePlantillas();

    // 1. Aseguramos que exista al menos un objeto de plantilla base
    useEffect(() => {
        if (!readOnly && (!formData.plantillas || formData.plantillas.length === 0)) {
            const defaultSpec = {
                id: generateId(),
                nombre: "", descripcion: "",
                nombrePrenda: "",
                forro: "", relleno: "", colorForro: "",
                gorro: "", cuello: "", abotonaduraCierre: "",
                cortesAplicaciones: "", fuelles: "", mangas: "",
                pretinasRuedo: "", bolsillos: "", cintaDetalle: "",
                logoDetalle: "", accesoriosDetalle: "", obsModelo: ""
            };
            setFormData(prev => ({ ...prev, plantillas: [defaultSpec] }));
        }
    }, [readOnly, formData.plantillas, setFormData]);

    // 2. Lógica de Autocompletado de Plantilla Integral
    const handlePrendaChange = (val) => {
        if (readOnly) return;
        const newDesc = val === "OTRO" ? "OTRO - " : val;

        if (val !== "OTRO" && val !== "") {
            // Ahora plantillas contiene "Articulos" devueltos por la API
            const configMaster = (plantillas || []).find(p => p.nombreArticulo?.toUpperCase() === val);

            if (configMaster) {
                const newPlantilla = {
                    id: generateId(),
                    nombre: configMaster.nombreArticulo || val,
                    descripcion: configMaster.nombreArticulo || val,
                    nombrePrenda: configMaster.nombreArticulo || val,
                    genero: prev => prev.genero || "",
                    camposActivos: [], // Se resolverán asincrónicamente mediante getCamposForArticulo
                    telas: [],         // El backend ya no tiene telas predefinidas
                    accesorios: [],    // El backend ya no tiene accesorios predefinidos
                    logotipos: [],
                    cintas: [],
                    vinculos: []
                };

                setFormData(prev => ({
                    ...prev,
                    articuloDescripcion: newDesc,
                    nombrePrenda: val,
                    plantillas: [newPlantilla]
                }));
            } else {
                setFormData(prev => ({ 
                    ...prev, 
                    articuloDescripcion: newDesc,
                    nombrePrenda: val 
                }));
            }
        } else {
            setFormData(prev => ({ ...prev, articuloDescripcion: newDesc }));
        }
    };

    const handleAddItem = (section) => {
        if (readOnly) return;
        const baseItem = { id: generateId() };
        let newItem = { ...baseItem };

        if (section === "telas") {
            newItem = { ...baseItem, aplicacion: "", nombre: "", composicion: "", color: "", peso: 0, unidadMedida: "MTRS" };
        } else if (section === "accesorios") {
            newItem = { ...baseItem, nombreAccesorio: "", tipo: "", cantidad: 0 };
        } else if (section === "logotipo") {
            newItem = { ...baseItem, tipo: "", nombre: "", ubicacion: "", color: "", tamanio: 0, cantidad: 0, precio: 0 };
        } else if (section === "plantillas") {
            newItem = { ...baseItem, nombrePrenda: "", telas: [], accesorios: [], logotipos: [], cintas: [], vinculos: [] };
        }

        // Si es de costeo (telas, accesorios, logotipo), lo agregamos a la primera plantilla
        if (["telas", "accesorios", "logotipo"].includes(section)) {
            setFormData((prev) => {
                const updatedPlantillas = [...(prev.plantillas || [])];
                if (updatedPlantillas.length === 0) {
                    updatedPlantillas.push({ id: generateId(), telas: [], accesorios: [], logotipos: [], cintas: [] });
                }
                const primary = { ...updatedPlantillas[0] };
                const subSection = section === "logotipo" ? "logotipos" : section;
                primary[subSection] = [...(primary[subSection] || []), newItem];
                updatedPlantillas[0] = primary;
                return { ...prev, plantillas: updatedPlantillas };
            });
        } else {
            setFormData((prev) => ({
                ...prev,
                [section]: [...(prev[section] || []), newItem],
            }));
        }
    };

    const handleUpdateItem = (section, id, field, value) => {
        if (readOnly) return;

        if (["telas", "accesorios", "logotipo"].includes(section)) {
            setFormData((prev) => {
                const updatedPlantillas = [...(prev.plantillas || [])];
                if (updatedPlantillas.length > 0) {
                    const primary = { ...updatedPlantillas[0] };
                    const subSection = section === "logotipo" ? "logotipos" : section;
                    primary[subSection] = (primary[subSection] || []).map(item =>
                        item.id === id ? { ...item, [field]: value } : item
                    );
                    updatedPlantillas[0] = primary;
                }
                return { ...prev, plantillas: updatedPlantillas };
            });
        } else {
            setFormData((prev) => ({
                ...prev,
                [section]: (prev[section] || []).map((item) =>
                    item.id === id ? { ...item, [field]: value } : item
                ),
            }));
        }
    };

    const handleRemoveItem = (section, id) => {
        if (readOnly) return;

        if (["telas", "accesorios", "logotipo"].includes(section)) {
            setFormData((prev) => {
                const updatedPlantillas = [...(prev.plantillas || [])];
                if (updatedPlantillas.length > 0) {
                    const primary = { ...updatedPlantillas[0] };
                    const subSection = section === "logotipo" ? "logotipos" : section;
                    primary[subSection] = (primary[subSection] || []).filter(item => item.id !== id);
                    updatedPlantillas[0] = primary;
                }
                return { ...prev, plantillas: updatedPlantillas };
            });
        } else {
            setFormData((prev) => ({
                ...prev,
                [section]: (prev[section] || []).filter((item) => item.id !== id),
            }));
        }
    };

    return (
        <div className="max-w-6xl mx-auto p-6 space-y-10 animate-in fade-in duration-500">
            <SolicitudHeader
                formData={formData}
                onBack={onBack}
                onSave={onSave}
                readOnly={readOnly}
                isEditing={isEditing}
                setIsEditing={setIsEditing}
            />

            {/* Datos Generales */}
            <div className={`bg-white p-8 rounded-[3rem] shadow-sm border-2 border-gray-50 space-y-8 ${readOnly ? "opacity-90" : ""}`}>

                <h3 className="text-xs font-black text-blue-600 uppercase tracking-[0.2em] flex items-center gap-2 mb-2">
                    <ClipboardList className="w-4 h-4" /> I. Información de Solicitud
                </h3>

                {/* Fila 1: Cliente / Vendedor */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Cliente</label>
                        <select
                            value={formData.clienteId || ""}
                            onChange={(e) => setFormData({ ...formData, clienteId: e.target.value })}
                            disabled={readOnly}
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "cursor-not-allowed" : ""}`}
                        >
                            <option value="">Seleccione Cliente</option>
                            {(clientes || []).map((c) => (
                                <option key={c.clienteId} value={c.clienteId}>
                                    {c.razonSocial}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Vendedor</label>
                        <select
                            value={formData.vendedorId || ""}
                            onChange={(e) => setFormData({ ...formData, vendedorId: e.target.value })}
                            disabled={readOnly}
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "cursor-not-allowed" : ""}`}
                        >
                            <option value="">Seleccione Vendedor</option>
                            {(vendedores || []).map((v) => (
                                <option key={v.id} value={v.id}>
                                    {v.nombreUsuario} {v.apellidosUsuario}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Fila 2: Tipo de Prenda / Cantidad Total */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-4">
                        <div className="space-y-2">
                            <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Tipo de Prenda (Basado en Plantilla)</label>
                            <select
                                value={(() => {
                                    const desc = (formData.articuloDescripcion || "").split(" - ")[0];
                                    const dbTypes = (plantillas || []).map(p => p.nombreArticulo?.toUpperCase());
                                    return dbTypes.includes(desc) ? desc : formData.articuloDescripcion ? "OTRO" : "";
                                })()}
                                disabled={readOnly}
                                onChange={(e) => handlePrendaChange(e.target.value)}
                                className={`w-full p-4 rounded-2xl bg-blue-50/30 border-2 border-blue-100 text-sm font-black text-blue-700 outline-none focus:ring-4 focus:ring-blue-100 transition-all ${readOnly ? "cursor-not-allowed" : ""}`}
                            >
                                <option value="">Seleccione Tipo</option>
                                {(plantillas || []).map(p => (
                                    <option key={p.idArticulo || p.id} value={p.nombreArticulo?.toUpperCase()}>
                                        {p.nombreArticulo}
                                    </option>
                                ))}
                                <option value="OTRO">Otro (Personalizar)</option>
                            </select>
                        </div>

                        {(formData.articuloDescripcion || "").startsWith("OTRO") && (
                            <div className="space-y-2 animate-in slide-in-from-top-2 duration-300">
                                <label className="text-[10px] font-black uppercase tracking-widest text-blue-400">Especifique Prenda</label>
                                <input
                                    type="text"
                                    value={formData.articuloDescripcion.replace("OTRO - ", "")}
                                    readOnly={readOnly}
                                    onChange={(e) =>
                                        setFormData({ ...formData, articuloDescripcion: `OTRO - ${e.target.value.toUpperCase()}` })
                                    }
                                    className={`w-full p-3 rounded-xl bg-blue-50/50 border border-blue-100 text-sm font-bold text-blue-600 uppercase outline-none focus:ring-2 focus:ring-blue-500 placeholder-blue-300 ${readOnly ? "cursor-default" : ""}`}
                                    placeholder="Ej: Calza, Gorro, Bolso..."
                                />
                            </div>
                        )}
                    </div>

                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Cantidad Total Solicitada</label>
                        <input
                            type="number"
                            value={formData.cantidad || 0}
                            readOnly={readOnly}
                            onChange={(e) =>
                                !readOnly && setFormData({ ...formData, cantidad: parseInt(e.target.value) || 0 })
                            }
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-lg font-black text-gray-700 outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "opacity-70 cursor-not-allowed" : ""}`}
                        />
                    </div>
                </div>

                {/* Fila 3: Nombre Prenda / Género */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Nombre de la Prenda (Modelo)</label>
                        <input
                            type="text"
                            value={formData.nombrePrenda || ""}
                            readOnly={readOnly}
                            onChange={(e) => setFormData({ ...formData, nombrePrenda: e.target.value.toUpperCase() })}
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-sm font-bold uppercase outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "cursor-not-allowed" : ""}`}
                            placeholder="Ej: Chaqueta impermeable 2024"
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Género</label>
                        <select
                            value={formData.genero || ""}
                            disabled={readOnly}
                            onChange={(e) => setFormData({ ...formData, genero: e.target.value })}
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "cursor-default opacity-70" : "cursor-pointer"}`}
                        >
                            <option value="">Seleccionar Género</option>
                            <option value="FEMENINO">FEMENINO</option>
                            <option value="MASCULINO">MASCULINO</option>
                            <option value="UNISEX">UNISEX</option>
                        </select>
                    </div>
                </div>

                {/* Fila 4: Tallaje */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Tallaje</label>
                        <select
                            value={formData.tallaje || ""}
                            disabled={readOnly}
                            onChange={(e) => setFormData({ ...formData, tallaje: e.target.value })}
                            className={`w-full p-4 rounded-2xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 transition-all ${readOnly ? "cursor-default opacity-70" : "cursor-pointer"}`}
                        >
                            <option value="">sin seleccionar</option>
                            <option value="Antuan SA">Antuan SA</option>
                            <option value="Cliente">Cliente</option>
                            <option value="Proveedor SC">Proveedor SC</option>
                        </select>
                    </div>
                </div>

                {/* Fila 4: Es Muestra / Switch Logo */}
                <div className="flex flex-wrap gap-12 pt-6 border-t border-gray-50">
                    <div className="flex flex-col">
                        <span className="text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1">Muestra Física</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData({ ...formData, esMuestra: !formData.esMuestra })}
                            className={`relative w-16 h-8 rounded-full flex items-center transition-all duration-300 ${formData.esMuestra ? "bg-orange-500" : "bg-gray-200"
                                } ${readOnly ? "cursor-not-allowed opacity-70" : "cursor-pointer"}`}
                        >
                            <span className={`absolute left-2 text-[9px] font-black uppercase transition-opacity duration-300 ${formData.esMuestra ? "opacity-100 text-white" : "opacity-0"}`}>Sí</span>
                            <span className={`absolute right-2 text-[9px] font-black uppercase transition-opacity duration-300 ${formData.esMuestra ? "opacity-0" : "opacity-100 text-gray-400"}`}>No</span>
                            <div className={`absolute w-6 h-6 rounded-full bg-white shadow-md transform transition-transform duration-300 ${formData.esMuestra ? "translate-x-9" : "translate-x-1"}`} />
                        </button>
                    </div>

                    <div className="flex flex-col">
                        <span className="text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1">Logotipo</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData({ ...formData, hasLogo: !formData.hasLogo })}
                            className={`relative w-16 h-8 rounded-full flex items-center transition-all duration-300 ${formData.hasLogo ? "bg-green-600" : "bg-gray-200"
                                } ${readOnly ? "cursor-not-allowed opacity-70" : "cursor-pointer"}`}
                        >
                            <span className={`absolute left-2 text-[9px] font-black uppercase transition-opacity duration-300 ${formData.hasLogo ? "opacity-100 text-white" : "opacity-0"}`}>Si</span>
                            <span className={`absolute right-2 text-[9px] font-black uppercase transition-opacity duration-300 ${formData.hasLogo ? "opacity-0" : "opacity-100 text-gray-400"}`}>No</span>
                            <div className={`absolute w-6 h-6 rounded-full bg-white shadow-md transform transition-transform duration-300 ${formData.hasLogo ? "translate-x-9" : "translate-x-1"}`} />
                        </button>
                    </div>
                </div>
            </div>

            {/* SECCIÓN LOGO (Mantenido arriba según requerimiento) */}
            {formData.hasLogo && (
                <div className="animate-in slide-in-from-top-4 duration-500">
                    <LogotipoPanel
                        data={formData.plantillas?.[0]?.logotipos || []}
                        onAdd={() => !readOnly && handleAddItem("logotipo")}
                        onUpdate={(id, field, value) => !readOnly && handleUpdateItem("logotipo", id, field, value)}
                        onRemove={(id) => !readOnly && handleRemoveItem("logotipo", id)}
                        readOnly={readOnly}
                    />
                </div>
            )}

            {/* FICHA INTEGRAL DE PRENDA */}
            <div className="space-y-8">
                <div className="flex items-center gap-4 ml-6">
                    <div className="w-1.5 h-6 bg-blue-600 rounded-full" />
                    <h2 className="text-xl font-black text-gray-800 uppercase tracking-widest flex items-center gap-3">
                        <Shirt className="w-6 h-6 text-blue-600" />
                        II. Ficha Integral de la Prenda
                    </h2>
                </div>

                <div className="space-y-12 bg-gray-50/50 p-8 rounded-[4rem] border-2 border-dashed border-gray-100">
                    {/* A. Telas */}
                    <TelasSCOSPanel
                        data={formData.plantillas?.[0]?.telas || []}
                        onAdd={() => !readOnly && handleAddItem("telas")}
                        onUpdate={(id, field, value) => !readOnly && handleUpdateItem("telas", id, field, value)}
                        onRemove={(id) => !readOnly && handleRemoveItem("telas", id)}
                        readOnly={readOnly}
                    />

                    {/* B. Accesorios */}
                    <AccesoriosSCOSPanel
                        data={formData.plantillas?.[0]?.accesorios || []}
                        onAdd={() => !readOnly && handleAddItem("accesorios")}
                        onUpdate={(id, field, value) => !readOnly && handleUpdateItem("accesorios", id, field, value)}
                        onRemove={(id) => !readOnly && handleRemoveItem("accesorios", id)}
                        readOnly={readOnly}
                    />

                    {/* D. Especificaciones Técnicas */}
                    <PlantillasPanel
                        data={formData.plantillas}
                        articuloDescripcion={formData.articuloDescripcion || ""}
                        telas={formData.plantillas?.[0]?.telas || []}
                        accesorios={formData.plantillas?.[0]?.accesorios || []}
                        onAdd={() => !readOnly && handleAddItem("plantillas")}
                        onUpdate={(id, field, value) => !readOnly && handleUpdateItem("plantillas", id, field, value)}
                        onRemove={(id) => !readOnly && handleRemoveItem("plantillas", id)}
                        readOnly={readOnly}
                    />
                </div>
            </div>
        </div>
    );
}
