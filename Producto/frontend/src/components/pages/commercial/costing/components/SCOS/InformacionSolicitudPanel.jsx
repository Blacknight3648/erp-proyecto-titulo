import { useState, useEffect } from "react";
import { ClipboardList } from "lucide-react";
import { useClientes } from "../../../../../../hooks/useClientes.js";
import { useVendedores } from "../../../../../../hooks/useVendedores.js";
import { usePlantillas } from "../../../../../../hooks/usePlantillas.js";

const generateId = () => {
    try { return crypto.randomUUID(); }
    catch (e) { return Math.random().toString(36).substring(2, 15); }
};

const inputStyles = `w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium text-slate-700 outline-none focus:bg-white focus:ring-2 focus:ring-blue-500/10 focus:border-blue-500 transition-all placeholder:text-slate-400/80`;
const labelStyles = `block text-xs font-semibold text-slate-500 mb-1.5 tracking-wide`;

export default function InformacionSolicitudPanel({ formData, setFormData, readOnly }) {
    const { clientes } = useClientes();
    const { vendedores } = useVendedores();
    const { plantillas } = usePlantillas();

    const [clienteInput, setClienteInput] = useState("");
    const [vendedorInput, setVendedorInput] = useState("");

    useEffect(() => {
        if (formData.clienteId && clientes?.length) {
            const match = clientes.find(c => String(c.clienteId) === String(formData.clienteId));
            if (match) setClienteInput(match.razonSocial);
        }
        if (formData.vendedorId && vendedores?.length) {
            const match = vendedores.find(v => String(v.id) === String(formData.vendedorId));
            if (match) setVendedorInput(`${match.nombreUsuario} ${match.apellidosUsuario}`);
        }
    }, [formData.clienteId, formData.vendedorId, clientes, vendedores]);

    const handlePrendaChange = (textValue) => {
        if (readOnly) return;
        const cleanValue = textValue.toUpperCase();
        const configMaster = (plantillas || []).find(p => p.nombreArticulo?.toUpperCase() === cleanValue);

        if (configMaster) {
            const newPlantilla = {
                id: generateId(),
                nombre: configMaster.nombreArticulo,
                descripcion: configMaster.nombreArticulo,
                nombrePrenda: configMaster.nombreArticulo,
                genero: "",
                camposActivos: [],
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
                plantillas: [newPlantilla]
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                articuloDescripcion: cleanValue,
                nombrePrenda: textValue
            }));
        }
    };

    return (
        <div className={`bg-white rounded-2xl border border-slate-200/80 shadow-sm overflow-hidden ${readOnly ? "bg-slate-50/40" : ""}`}>
            <div className="bg-slate-950 px-6 py-4 flex items-center gap-2.5">
                <ClipboardList className="w-4 h-4 text-blue-400" />
                <h3 className="text-sm font-semibold tracking-wide text-white">
                    Información de Solicitud
                </h3>
            </div>

            <div className="p-6 md:p-8 space-y-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div>
                        <label className={labelStyles}>Cliente</label>
                        <input
                            type="text"
                            list="lista-clientes"
                            value={clienteInput}
                            disabled={readOnly}
                            placeholder="Escriba para buscar cliente..."
                            className={inputStyles}
                            onChange={(e) => {
                                const val = e.target.value;
                                setClienteInput(val);
                                const match = (clientes || []).find(c => c.razonSocial?.toUpperCase() === val.toUpperCase());
                                setFormData(prev => ({ ...prev, clienteId: match ? match.clienteId : "" }));
                            }}
                        />
                        <datalist id="lista-clientes">
                            {(clientes || []).map((c) => (
                                <option key={c.clienteId} value={c.razonSocial} />
                            ))}
                        </datalist>
                    </div>

                    <div>
                        <label className={labelStyles}>Vendedor</label>
                        <input
                            type="text"
                            list="lista-vendedores"
                            value={vendedorInput}
                            disabled={readOnly}
                            placeholder="Escriba para buscar vendedor..."
                            className={inputStyles}
                            onChange={(e) => {
                                const val = e.target.value;
                                setVendedorInput(val);
                                const match = (vendedores || []).find(v =>
                                    `${v.nombreUsuario} ${v.apellidosUsuario}`.toUpperCase() === val.toUpperCase()
                                );
                                setFormData(prev => ({ ...prev, vendedorId: match ? match.id : "" }));
                            }}
                        />
                        <datalist id="lista-vendedores">
                            {(vendedores || []).map((v) => (
                                <option key={v.id} value={`${v.nombreUsuario} ${v.apellidosUsuario}`} />
                            ))}
                        </datalist>
                    </div>

                    <div>
                        <label className={labelStyles}>Tipo de prenda (Plantilla)</label>
                        <input
                            type="text"
                            list="lista-plantillas"
                            value={formData.articuloDescripcion || ""}
                            disabled={readOnly}
                            placeholder="Ej: Polerón, Parka, Pantalón..."
                            className={`${inputStyles} border-blue-100 bg-blue-50/20 font-medium text-blue-900`}
                            onChange={(e) => handlePrendaChange(e.target.value)}
                        />
                        <datalist id="lista-plantillas">
                            {(plantillas || []).map(p => (
                                <option key={p.idArticulo || p.id} value={p.nombreArticulo?.toUpperCase()} />
                            ))}
                            <option value="OTRO" />
                        </datalist>
                    </div>

                    <div>
                        <label className={labelStyles}>Cantidad total solicitada</label>
                        <input
                            type="number"
                            value={formData.cantidad || 0}
                            disabled={readOnly}
                            onChange={(e) =>
                                !readOnly && setFormData(prev => ({ ...prev, cantidad: parseInt(e.target.value) || 0 }))
                            }
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
                        <select
                            value={formData.genero || ""}
                            disabled={readOnly}
                            onChange={(e) => setFormData(prev => ({ ...prev, genero: e.target.value }))}
                            className={inputStyles}
                        >
                            <option value="">Seleccionar género</option>
                            <option value="FEMENINO">Femenino</option>
                            <option value="MASCULINO">Masculino</option>
                            <option value="UNISEX">Unisex</option>
                        </select>
                    </div>

                    <div>
                        <label className={labelStyles}>Tallaje</label>
                        <select
                            value={formData.tallaje || ""}
                            disabled={readOnly}
                            onChange={(e) => setFormData(prev => ({ ...prev, tallaje: e.target.value }))}
                            className={inputStyles}
                        >
                            <option value="">Sin seleccionar</option>
                            <option value="Antuan SA">Antuan SA</option>
                            <option value="Cliente">Cliente</option>
                            <option value="Proveedor SC">Proveedor SC</option>
                        </select>
                    </div>
                </div>

                <div className="flex gap-8 pt-5 border-t border-slate-100">
                    <div className="flex items-center gap-3">
                        <span className="text-xs font-semibold text-slate-500">Muestra física</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData(prev => ({ ...prev, esMuestra: !prev.esMuestra }))}
                            className={`relative w-9 h-5 rounded-full flex items-center transition-colors duration-200 ${formData.esMuestra ? "bg-amber-500" : "bg-slate-200"}`}
                        >
                            <div className={`w-3.5 h-3.5 rounded-full bg-white shadow transform transition-transform duration-200 ${formData.esMuestra ? "translate-x-4.5" : "translate-x-1"}`} />
                        </button>
                        <span className="text-xs font-medium text-slate-600">{formData.esMuestra ? "Sí" : "No"}</span>
                    </div>

                    <div className="flex items-center gap-3">
                        <span className="text-xs font-semibold text-slate-500">Lleva logotipo</span>
                        <button
                            type="button"
                            disabled={readOnly}
                            onClick={() => !readOnly && setFormData(prev => ({ ...prev, hasLogo: !prev.hasLogo }))}
                            className={`relative w-9 h-5 rounded-full flex items-center transition-colors duration-200 ${formData.hasLogo ? "bg-emerald-600" : "bg-slate-200"}`}
                        >
                            <div className={`w-3.5 h-3.5 rounded-full bg-white shadow transform transition-transform duration-200 ${formData.hasLogo ? "translate-x-4.5" : "translate-x-1"}`} />
                        </button>
                        <span className="text-xs font-medium text-slate-600">{formData.hasLogo ? "Sí" : "No"}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
