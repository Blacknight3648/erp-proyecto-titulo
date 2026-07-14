import { ClipboardList } from "lucide-react";
import { useClientes } from "../../../hooks/useClientes.js";
import { useVendedores } from "../../../hooks/useVendedores.js";
import { usePlantillas, FIELD_LABELS } from "../../../hooks/usePlantillas.js";
import ComboField from "../../../components/shared/ComboField";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../../../ui/select";

const generateId = () => {
    try { return crypto.randomUUID(); }
    catch (e) { return Math.random().toString(36).substring(2, 15); }
};

const inputStyles = `w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm font-medium text-foreground outline-none focus:bg-card focus:ring-2 focus:ring-primary/10 focus:border-primary transition-all placeholder:text-muted-foreground/70`;
const labelStyles = `block text-xs font-semibold text-muted-foreground mb-1.5 tracking-wide`;

export default function InformacionSolicitudPanel({ formData, setFormData, readOnly }) {
    const { clientes } = useClientes();
    const { vendedores } = useVendedores();
    const { plantillas, getCamposForArticulo } = usePlantillas();

    const clienteOptions = (clientes || []).map(c => ({
        value: String(c.clienteId),
        label: c.razonSocial,
    }));

    const vendedorOptions = (vendedores || []).map(v => ({
        value: String(v.id),
        label: v.nombreVendedor || v.nombreUsuario || '',
    }));

    const plantillaOptions = [
        ...(plantillas || []).map(p => ({
            value: p.nombreArticulo?.toUpperCase() ?? '',
            label: p.nombreArticulo?.toUpperCase() ?? '',
        })),
        { value: 'OTRO', label: 'OTRO' },
    ];

    const handlePrendaChange = async (textValue) => {
        if (readOnly) return;
        const cleanValue = (textValue || '').toUpperCase();
        const configMaster = (plantillas || []).find(p => p.nombreArticulo?.toUpperCase() === cleanValue);

        if (configMaster) {
            const camposSet = await getCamposForArticulo(cleanValue);
            const camposActivos = camposSet ? [...camposSet] : [];

            const newPlantilla = {
                id: generateId(),
                nombre: configMaster.nombreArticulo,
                descripcion: configMaster.nombreArticulo,
                nombrePrenda: configMaster.nombreArticulo,
                genero: "",
                camposActivos,
                detallesPrenda: {},
                telas: [],
                accesorios: [],
                logotipos: [],
                cintas: [],
                vinculos: []
            };
            setFormData(prev => ({
                ...prev,
                articuloDescripcion: cleanValue,
                nombrePrenda: configMaster.nombreArticulo,
                esPrendaNueva: false,
                plantillas: [newPlantilla]
            }));
        } else if (cleanValue === 'OTRO') {
            // El usuario indicó que es una prenda nueva — el nombre real viene de nombrePrenda
            setFormData(prev => ({
                ...prev,
                articuloDescripcion: 'OTRO',
                esPrendaNueva: true,
                plantillas: [{
                    id: generateId(),
                    camposActivos: Object.keys(FIELD_LABELS),
                    detallesPrenda: {},
                    telas: prev.plantillas?.[0]?.telas || [],
                    accesorios: prev.plantillas?.[0]?.accesorios || [],
                    logotipos: prev.plantillas?.[0]?.logotipos || [],
                    cintas: [],
                    vinculos: []
                }]
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                articuloDescripcion: cleanValue,
                nombrePrenda: textValue,
                esPrendaNueva: cleanValue !== ''
            }));
        }
    };

    return (
        <div className={`bg-card rounded-2xl border border-border shadow-sm overflow-hidden ${readOnly ? "opacity-90" : ""}`}>
            <div className="bg-sidebar px-6 py-4 flex items-center gap-2.5">
                <ClipboardList className="w-4 h-4 text-sidebar-primary" />
                <h3 className="text-sm font-semibold tracking-wide text-white">
                    Información de Solicitud
                </h3>
            </div>

            <div className="p-6 md:p-8 space-y-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div>
                        <label className={labelStyles}>Cliente</label>
                        <ComboField
                            value={formData.clienteId ? String(formData.clienteId) : ''}
                            onChange={(value) => setFormData(prev => ({ ...prev, clienteId: value }))}
                            options={clienteOptions}
                            placeholder="Buscar cliente..."
                            readOnly={readOnly}
                        />
                    </div>

                    <div>
                        <label className={labelStyles}>Vendedor</label>
                        <ComboField
                            value={formData.vendedorId ? String(formData.vendedorId) : ''}
                            onChange={(value) => setFormData(prev => ({ ...prev, vendedorId: value }))}
                            options={vendedorOptions}
                            placeholder="Buscar vendedor..."
                            readOnly={readOnly}
                        />
                    </div>

                    <div>
                        <label className={labelStyles}>Tipo de prenda (Plantilla)</label>
                        <ComboField
                            value={formData.articuloDescripcion || ''}
                            onChange={(value) => handlePrendaChange(value)}
                            options={plantillaOptions}
                            placeholder="Ej: Polerón, Parka..."
                            readOnly={readOnly}
                        />
                    </div>

                    <div>
                        <label className={labelStyles}>Cantidad total solicitada</label>
                        <input
                            type="number"
                            min="1"
                            value={formData.cantidad || ""}
                            disabled={readOnly}
                            onChange={(e) => {
                                if (readOnly) return;
                                const val = parseInt(e.target.value);
                                setFormData(prev => ({ ...prev, cantidad: isNaN(val) || val < 1 ? "" : val }));
                            }}
                            className={`${inputStyles} font-semibold`}
                        />
                    </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 pt-2">
                    <div className="lg:col-span-2">
                        <label className={labelStyles}>Nombre de la prenda (Modelo)</label>
                        <input
                            type="text"
                            value={formData.nombrePrenda || ""}
                            disabled={readOnly}
                            onChange={(e) => setFormData(prev => ({ ...prev, nombrePrenda: e.target.value }))}
                            className={inputStyles}
                            placeholder="Ej: Chaqueta impermeable corporativa"
                        />
                    </div>

                    <div>
                        <label className={labelStyles}>Género</label>
                        <Select
                            value={formData.genero || ""}
                            onValueChange={(val) => setFormData(prev => ({ ...prev, genero: val }))}
                            disabled={readOnly}
                        >
                            <SelectTrigger className={inputStyles}>
                                <SelectValue placeholder="Seleccionar género" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="FEMENINO">Femenino</SelectItem>
                                <SelectItem value="MASCULINO">Masculino</SelectItem>
                                <SelectItem value="UNISEX">Unisex</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>

                    <div>
                        <label className={labelStyles}>Tallaje</label>
                        <Select
                            value={formData.tallaje || ""}
                            onValueChange={(val) => setFormData(prev => ({ ...prev, tallaje: val }))}
                            disabled={readOnly}
                        >
                            <SelectTrigger className={inputStyles}>
                                <SelectValue placeholder="Seleccionar tallaje" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="Antuan SA">Antuan SA</SelectItem>
                                <SelectItem value="Cliente">Cliente</SelectItem>
                                <SelectItem value="Proveedor SC">Proveedor SC</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                </div>

                <div className="flex gap-8 pt-5 border-t border-border">
                    <div className="flex items-center gap-3">
                        <span className="text-xs font-semibold text-muted-foreground">Muestra física</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData(prev => ({ ...prev, esMuestra: !prev.esMuestra }))}
                            className={`relative w-9 h-5 rounded-full flex items-center transition-colors duration-200 ${formData.esMuestra ? "bg-warning" : "bg-muted"}`}
                        >
                            <div className={`w-3.5 h-3.5 rounded-full bg-card shadow transform transition-transform duration-200 ${formData.esMuestra ? "translate-x-4.5" : "translate-x-1"}`} />
                        </button>
                        <span className="text-xs font-medium text-muted-foreground">{formData.esMuestra ? "Sí" : "No"}</span>
                    </div>

                    <div className="flex items-center gap-3">
                        <span className="text-xs font-semibold text-muted-foreground">Lleva logotipo</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData(prev => ({ ...prev, hasLogo: !prev.hasLogo }))}
                            className={`relative w-9 h-5 rounded-full flex items-center transition-colors duration-200 ${formData.hasLogo ? "bg-success" : "bg-muted"}`}
                        >
                            <div className={`w-3.5 h-3.5 rounded-full bg-card shadow transform transition-transform duration-200 ${formData.hasLogo ? "translate-x-4.5" : "translate-x-1"}`} />
                        </button>
                        <span className="text-xs font-medium text-muted-foreground">{formData.hasLogo ? "Sí" : "No"}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
