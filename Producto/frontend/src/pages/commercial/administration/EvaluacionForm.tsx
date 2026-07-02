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

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../ui/select';
import { Input } from '../../../ui/input';

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
            wrap: 'bg-brand-indigo/10 border-brand-indigo/20',
            icon: 'bg-brand-indigo/20',
            iconColor: 'text-brand-indigo',
            bar: 'bg-brand-indigo',
            label: 'text-brand-indigo',
        },
        blue: {
            wrap: 'bg-primary/10 border-primary/20',
            icon: 'bg-primary/20',
            iconColor: 'text-primary',
            bar: 'bg-primary',
            label: 'text-primary',
        },
        green: {
            wrap: 'bg-success/10 border-success/20',
            icon: 'bg-success/20',
            iconColor: 'text-success',
            bar: 'bg-success',
            label: 'text-success',
        },
        amber: {
            wrap: 'bg-warning/10 border-warning/20',
            icon: 'bg-warning/20',
            iconColor: 'text-warning',
            bar: 'bg-warning',
            label: 'text-warning',
        },
    };
    const p = paleta[color] || paleta.indigo;

    return (
        <div className={`p-6 rounded-2xl border ${p.wrap}`}>
            <div className="flex items-center gap-3 mb-5">
                <div className={`w-8 h-8 ${p.icon} rounded-xl flex items-center justify-center shrink-0`}>
                    <Icon className={`w-4 h-4 ${p.iconColor}`} />
                </div>
                <h4 className="text-[11px] font-black text-foreground uppercase tracking-widest">{titulo}</h4>
            </div>
            <div className="space-y-4">
                {children}
            </div>
        </div>
    );
}

function CampoLabel({ children, color = 'gray' }) {
    const c = {
        gray:   'text-muted-foreground',
        indigo: 'text-brand-indigo',
        blue:   'text-primary',
        green:  'text-success',
        amber:  'text-warning',
    }[color] || 'text-muted-foreground';
    return (
        <label className={`text-[9px] font-black ${c} uppercase tracking-widest ml-0.5 mb-1.5 block`}>
            {children}
        </label>
    );
}

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
                            <span className="text-xs font-black text-brand-indigo">{c.anticipo ?? 50}%</span>
                        </div>
                        <input
                            type="range" min="0" max="100" step="5"
                            value={c.anticipo ?? 50}
                            onChange={(e) => set('anticipo', parseInt(e.target.value))}
                            className="w-full h-1.5 bg-brand-indigo/20 rounded-lg appearance-none cursor-pointer accent-brand-indigo disabled:opacity-60 disabled:cursor-default"
                            disabled={disabled}
                        />
                        <div className="mt-3 bg-card border border-brand-indigo/20 rounded-xl px-4 py-2.5 flex justify-between items-center">
                            <span className="text-[10px] font-bold text-muted-foreground uppercase">Saldo a Entrega</span>
                            <span className="text-xs font-black text-foreground">{100 - (c.anticipo ?? 50)}%</span>
                        </div>
                    </div>
                    <div>
                        <CampoLabel color="indigo">Forma de Pago</CampoLabel>
                        <Select value={c.formaPago ?? ''} onValueChange={(val) => set('formaPago', val)} disabled={disabled}>
                            <SelectTrigger>
                                <SelectValue placeholder="Seleccionar..." />
                            </SelectTrigger>
                            <SelectContent>
                                {OPCIONES_FORMA_PAGO.map(o => <SelectItem key={o} value={o}>{o}</SelectItem>)}
                            </SelectContent>
                        </Select>
                    </div>
                </SeccionCard>

                {/* Logística y Distribución */}
                <SeccionCard color="blue" icon={Truck} titulo="Logística y Distribución">
                    <div>
                        <CampoLabel color="blue">Condición de Flete</CampoLabel>
                        <Select value={c.flete ?? ''} onValueChange={(val) => set('flete', val)} disabled={disabled}>
                            <SelectTrigger>
                                <SelectValue placeholder="Seleccionar..." />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="Cliente">Flete por cuenta del Cliente</SelectItem>
                                <SelectItem value="Incluido">Flete Incluido en el precio</SelectItem>
                                <SelectItem value="Por cobrar">Envío por cobrar (Starken / Chileexpress)</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div>
                        <CampoLabel color="blue">Lugar de Entrega</CampoLabel>
                        <Select value={c.lugarEntrega ?? ''} onValueChange={(val) => set('lugarEntrega', val)} disabled={disabled}>
                            <SelectTrigger>
                                <SelectValue placeholder="Seleccionar..." />
                            </SelectTrigger>
                            <SelectContent>
                                {OPCIONES_LUGAR_ENTREGA.map(o => <SelectItem key={o} value={o}>{o}</SelectItem>)}
                            </SelectContent>
                        </Select>
                    </div>
                    <div>
                        <CampoLabel color="blue">Validez de la Oferta</CampoLabel>
                        <Select value={c.validezOferta ?? ''} onValueChange={(val) => set('validezOferta', val)} disabled={disabled}>
                            <SelectTrigger>
                                <SelectValue placeholder="Seleccionar..." />
                            </SelectTrigger>
                            <SelectContent>
                                {OPCIONES_VALIDEZ.map(o => <SelectItem key={o} value={o}>{o}</SelectItem>)}
                            </SelectContent>
                        </Select>
                    </div>
                </SeccionCard>

                {/* Entrega y Garantía */}
                <SeccionCard color="green" icon={ShieldCheck} titulo="Entrega y Garantía">
                    <div>
                        <CampoLabel color="green">Fecha Compromiso de Entrega</CampoLabel>
                        <div className="relative">
                            <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground/50 pointer-events-none z-10" />
                            <Input
                                type="date"
                                className="pl-10"
                                value={c.plazoEntrega ?? ''}
                                onChange={(e) => set('plazoEntrega', e.target.value)}
                                disabled={disabled}
                            />
                        </div>
                    </div>
                    <div>
                        <CampoLabel color="green">Garantía del Producto</CampoLabel>
                        <div className="relative">
                            <Clock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground/50 pointer-events-none z-10" />
                            <Input
                                type="text"
                                className="pl-10"
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
                                <Percent className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground/50 pointer-events-none z-10" />
                                <Input
                                    type="number"
                                    min="0" max="100" step="0.5"
                                    className="pl-10"
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
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-brand-indigo/10 border border-brand-indigo/20 rounded-full text-[10px] font-black text-brand-indigo uppercase">
                        <CreditCard className="w-3 h-3" /> {c.formaPago}
                    </span>
                )}
                {c.flete && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-primary/10 border border-primary/20 rounded-full text-[10px] font-black text-primary uppercase">
                        <Truck className="w-3 h-3" /> {c.flete}
                    </span>
                )}
                {c.lugarEntrega && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-primary/10 border border-primary/20 rounded-full text-[10px] font-black text-primary uppercase">
                        <MapPin className="w-3 h-3" /> {c.lugarEntrega}
                    </span>
                )}
                {c.validezOferta && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-muted border border-border rounded-full text-[10px] font-black text-muted-foreground uppercase">
                        <Clock className="w-3 h-3" /> Validez: {c.validezOferta}
                    </span>
                )}
                {c.garantia && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-success/10 border border-success/20 rounded-full text-[10px] font-black text-success uppercase">
                        <ShieldCheck className="w-3 h-3" /> Garantía: {c.garantia}
                    </span>
                )}
            </div>
        </div>
    );
}
