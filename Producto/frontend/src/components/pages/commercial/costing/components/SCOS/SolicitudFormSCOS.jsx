import { useEffect } from "react";
import SolicitudHeader from "../../../../../../components/layout/SolicitudHeader.jsx";
import InformacionSolicitudPanel from "./InformacionSolicitudPanel.jsx";
import FichaPrendaPanel from "./FichaPrendaPanel.jsx";

const generateId = () => {
    try { return crypto.randomUUID(); }
    catch (e) { return Math.random().toString(36).substring(2, 15); }
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

    useEffect(() => {
        if (!readOnly && (!formData.plantillas || formData.plantillas.length === 0)) {
            const defaultSpec = {
                id: generateId(),
                gorro: "", cuello: "", abotonaduraCierre: "",
                cortesAplicaciones: "", fuelles: "", mangas: "", puños: "",
                pretinasRuedo: "", bolsillos: "", obsModelo: "",
                detallesPrenda: {},
                camposActivos: [],
                camposPersonalizados: {},
                vinculos: []
            };
            setFormData(prev => ({ ...prev, plantillas: [defaultSpec] }));
        }
    }, [readOnly, formData.plantillas, setFormData]);

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
        <div className="w-full max-w-[1600px] mx-auto px-4 xl:px-10 py-6 space-y-8 text-slate-800 antialiased font-sans animate-in fade-in duration-500">
            <SolicitudHeader
                formData={formData}
                onBack={onBack}
                onSave={onSave}
                readOnly={readOnly}
                isEditing={isEditing}
                setIsEditing={setIsEditing}
            />

            <InformacionSolicitudPanel
                formData={formData}
                setFormData={setFormData}
                readOnly={readOnly}
            />

            <FichaPrendaPanel
                formData={formData}
                readOnly={readOnly}
                onAddItem={handleAddItem}
                onUpdateItem={handleUpdateItem}
                onRemoveItem={handleRemoveItem}
            />
        </div>
    );
}
