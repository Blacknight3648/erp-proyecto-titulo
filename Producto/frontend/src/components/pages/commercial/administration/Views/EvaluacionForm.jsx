import {
    Truck,
    ShieldCheck,
    Calendar,
    Wallet,
    CreditCard,
    MapPin,
    Clock,
    Percent
} from 'lucide-react';

const OPCIONES_FORMA_PAGO = [
    '30 Días',
    '60 Días',
    '90 Días',
    'Contado',
    'Anticipo 50% / Saldo Entrega',
    'Anticipo 100%',
];

const OPCIONES_LUGAR_ENTREGA = [
    'Instalaciones Cliente',
    'Bodega Antuan',
    'Dirección Facturación',
    'Lugar a convenir',
];

const OPCIONES_VALIDEZ = [
    '7 Días',
    '15 Días',
    '30 Días',
    '60 Días',
];

// Bloque reutilizable de sección
function SeccionCard({ color, icon: Icon, titulo, children }) {
    const paleta = {
        indigo: {
            wrap: 'bg-indigo-50/60 border-indigo-100',
            icon: 'bg-indigo-100',
            iconColor: 'text-indigo-600',
            bar: 'bg-indigo-600',
            label: 'text-indigo-400',
        },
        blue: {
            wrap: 'bg-blue-50/60 border-blue-100',
            icon: 'bg-blue-100',
            iconColor: 'text-blue-600',
            bar: 'bg-blue-600',
            label: 'text-blue-400',
        },
        green: {
            wrap: 'bg-emerald-50/60 border-emerald-100',
            icon: 'bg-emerald-100',
            iconColor: 'text-emerald-600',
            bar: 'bg-emerald-600',
            label: 'text-emerald-400',
        },
        amber: {
            wrap: 'bg-amber-50/60 border-amber-100',
            icon: 'bg-amber-100',
            iconColor: 'text-amber-600',
            bar: 'bg-amber-500',
            label: 'text-amber-500',
        },
    };
    const p = paleta[color] || paleta.indigo;

    return (
        <div className={`p-6 rounded-2xl border ${p.wrap}`}>
            <div className="flex items-center gap-3 mb-5">
                <div className={`w-8 h-8 ${p.icon} rounded-xl flex items-center justify-center shrink-0`}>
                    <Icon className={`w-4 h-4 ${p.iconColor}`} />
                </div>
                <h4 className="text-[11px] font-black text-gray-800 uppercase tracking-widest">{titulo}</h4>
            </div>
            <div className="space-y-4">
                {children}
            </div>
        </div>
    );
}

function CampoLabel({ children, color = 'gray' }) {
    const c = {
        gray:   'text-gray-400',
        indigo: 'text-indigo-400',
        blue:   'text-blue-400',
        green:  'text-emerald-500',
        amber:  'text-amber-500',
    }[color] || 'text-gray-400';
    return (
        <label className={`text-[9px] font-black ${c} uppercase tracking-widest ml-0.5 mb-1.5 block`}>
            {children}
        </label>
    );
}

const inputCls = "w-full px-4 py-2.5 bg-white border border-gray-200 rounded-xl text-[11px] font-bold text-gray-700 focus:ring-2 focus:ring-indigo-200 focus:border-indigo-300 transition-all outline-none";
const selectCls = "w-full px-4 py-2.5 bg-white border border-gray-200 rounded-xl text-[11px] font-bold text-gray-700 focus:ring-2 focus:ring-indigo-200 focus:border-indigo-300 transition-all outline-none cursor-pointer";

export default function EvaluacionForm({ data, onChange, porcentajeComision, onComisionChange, disabled = false }) {
    const c = data.condiciones || {};

    const set = (field, value) => {
        if (disabled) return;
        onChange({ ...data, condiciones: { ...c, [field]: value } });
    };

    return (
        <div className="space-y-6">
            {/* Fila superior: Pagos + Forma de Cobro */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">

                {/* Estructura de Pagos */}
                <SeccionCard color="indigo" icon={Wallet} titulo="Estructura de Pagos">
                    <div>
                        <div className="flex justify-between items-center mb-2">
                            <CampoLabel color="indigo">Anticipo / Pie</CampoLabel>
                            <span className="text-xs font-black text-indigo-600">{c.anticipo ?? 50}%</span>
                        </div>
                        <input
                            type="range" min="0" max="100" step="5"
                            value={c.anticipo ?? 50}
                            onChange={(e) => set('anticipo', parseInt(e.target.value))}
                            className="w-full h-1.5 bg-indigo-100 rounded-lg appearance-none cursor-pointer accent-indigo-600 disabled:opacity-60 disabled:cursor-default"
                            disabled={disabled}
                        />
                        <div className="mt-3 bg-white border border-indigo-100 rounded-xl px-4 py-2.5 flex justify-between items-center">
                            <span className="text-[10px] font-bold text-gray-400 uppercase">Saldo a Entrega</span>
                            <span className="text-xs font-black text-gray-700">{100 - (c.anticipo ?? 50)}%</span>
                        </div>
                    </div>
                    <div>
                        <CampoLabel color="indigo">Forma de Pago</CampoLabel>
                        <select className={selectCls} value={c.formaPago ?? ''} onChange={(e) => set('formaPago', e.target.value)} disabled={disabled}>
                            <option value="">Seleccionar...</option>
                            {OPCIONES_FORMA_PAGO.map(o => <option key={o} value={o}>{o}</option>)}
                        </select>
                    </div>
                </SeccionCard>

                {/* Logística y Distribución */}
                <SeccionCard color="blue" icon={Truck} titulo="Logística y Distribución">
                    <div>
                        <CampoLabel color="blue">Condición de Flete</CampoLabel>
                        <select className={selectCls} value={c.flete ?? ''} onChange={(e) => set('flete', e.target.value)} disabled={disabled}>
                            <option value="Cliente">Flete por cuenta del Cliente</option>
                            <option value="Incluido">Flete Incluido en el precio</option>
                            <option value="Por cobrar">Envío por cobrar (Starken / Chileexpress)</option>
                        </select>
                    </div>
                    <div>
                        <CampoLabel color="blue">Lugar de Entrega</CampoLabel>
                        <select className={selectCls} value={c.lugarEntrega ?? ''} onChange={(e) => set('lugarEntrega', e.target.value)} disabled={disabled}>
                            <option value="">Seleccionar...</option>
                            {OPCIONES_LUGAR_ENTREGA.map(o => <option key={o} value={o}>{o}</option>)}
                        </select>
                    </div>
                    <div>
                        <CampoLabel color="blue">Validez de la Oferta</CampoLabel>
                        <select className={selectCls} value={c.validezOferta ?? ''} onChange={(e) => set('validezOferta', e.target.value)} disabled={disabled}>
                            <option value="">Seleccionar...</option>
                            {OPCIONES_VALIDEZ.map(o => <option key={o} value={o}>{o}</option>)}
                        </select>
                    </div>
                </SeccionCard>

                {/* Entrega y Garantía */}
                <SeccionCard color="green" icon={ShieldCheck} titulo="Entrega y Garantía">
                    <div>
                        <CampoLabel color="green">Fecha Compromiso de Entrega</CampoLabel>
                        <div className="relative">
                            <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-300 pointer-events-none" />
                            <input
                                type="date"
                                className={`${inputCls} pl-10`}
                                value={c.plazoEntrega ?? ''}
                                onChange={(e) => set('plazoEntrega', e.target.value)}
                                disabled={disabled}
                            />
                        </div>
                    </div>
                    <div>
                        <CampoLabel color="green">Garantía del Producto</CampoLabel>
                        <div className="relative">
                            <Clock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-300 pointer-events-none" />
                            <input
                                type="text"
                                className={`${inputCls} pl-10`}
                                placeholder="Ej: 30 días de corrido..."
                                value={c.garantia ?? ''}
                                onChange={(e) => set('garantia', e.target.value.toUpperCase())}
                                disabled={disabled}
                            />
                        </div>
                    </div>
                    {/* % Comisión ejecutivo — editable aquí para no ir a sidebar */}
                    {onComisionChange !== undefined && (
                        <div>
                            <CampoLabel color="green">% Comisión Ejecutivo</CampoLabel>
                            <div className="relative">
                                <Percent className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-300 pointer-events-none" />
                                <input
                                    type="number"
                                    min="0" max="100" step="0.5"
                                    className={`${inputCls} pl-10`}
                                    placeholder="5"
                                    value={porcentajeComision !== undefined ? porcentajeComision : ''}
                                    onChange={(e) => onComisionChange(parseFloat(e.target.value || 0))}
                                    disabled={disabled}
                                />
                            </div>
                        </div>
                    )}
                </SeccionCard>
            </div>

            {/* Resumen de condiciones (read-only chips) */}
            <div className="flex flex-wrap gap-2 pt-2">
                {c.formaPago && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-indigo-50 border border-indigo-100 rounded-full text-[10px] font-black text-indigo-600 uppercase">
                        <CreditCard className="w-3 h-3" /> {c.formaPago}
                    </span>
                )}
                {c.flete && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-blue-50 border border-blue-100 rounded-full text-[10px] font-black text-blue-600 uppercase">
                        <Truck className="w-3 h-3" /> {c.flete}
                    </span>
                )}
                {c.lugarEntrega && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-blue-50 border border-blue-100 rounded-full text-[10px] font-black text-blue-600 uppercase">
                        <MapPin className="w-3 h-3" /> {c.lugarEntrega}
                    </span>
                )}
                {c.validezOferta && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-gray-50 border border-gray-200 rounded-full text-[10px] font-black text-gray-500 uppercase">
                        <Clock className="w-3 h-3" /> Validez: {c.validezOferta}
                    </span>
                )}
                {c.garantia && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-emerald-50 border border-emerald-100 rounded-full text-[10px] font-black text-emerald-600 uppercase">
                        <ShieldCheck className="w-3 h-3" /> Garantía: {c.garantia}
                    </span>
                )}
            </div>
        </div>
    );
}
