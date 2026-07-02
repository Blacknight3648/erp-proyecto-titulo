import { Calculator, TrendingUp, DollarSign, Package, Award } from 'lucide-react';

function FilaResumen({ label, value, colorValue = 'text-white', size = 'text-xl', border = true }) {
    return (
        <div className={`flex justify-between items-end ${border ? 'border-b border-sidebar-border pb-4' : ''}`}>
            <p className="text-[10px] font-black text-sidebar-muted uppercase leading-tight">{label}</p>
            <p className={`${size} font-black ${colorValue} tracking-tighter tabular-nums`}>{value}</p>
        </div>
    );
}

export default function EVNResumenSidebar({ totals, otrosCostos }) {
    const comisionPct  = otrosCostos?.porcentajeComision || 0;
    const comisionMonto = (totals.totalNeto || 0) * (comisionPct / 100);
    const margenNum    = parseFloat(totals.margenPorc || 0);
    const colorMargen  = margenNum >= 25 ? 'text-success' : margenNum >= 15 ? 'text-warning' : 'text-destructive';

    return (
        <div className="bg-sidebar rounded-[2rem] p-7 text-white shadow-2xl relative overflow-hidden group sticky top-24">
            <div className="absolute top-0 right-0 w-36 h-36 bg-white/5 rounded-full -mr-10 -mt-10 group-hover:scale-125 transition-transform duration-1000" />
            <div className="relative z-10 space-y-5">

                <div className="flex items-center gap-2 mb-2">
                    <Calculator className="w-4 h-4 text-brand-indigo" />
                    <p className="text-[10px] font-black text-brand-indigo uppercase tracking-widest">Resumen Estructural</p>
                </div>

                <FilaResumen
                    label="Margen Real sobre Venta"
                    value={`${totals.margenPorc}%`}
                    colorValue={colorMargen}
                    size="text-3xl"
                />

                <FilaResumen
                    label="Utilidad Proyectada"
                    value={`$${(totals.margenPesos || 0).toLocaleString('es-CL')}`}
                    colorValue="text-white"
                    size="text-xl"
                />

                <FilaResumen
                    label="Subtotal Venta Neto"
                    value={`$${(totals.totalNeto || 0).toLocaleString('es-CL')}`}
                    colorValue="text-sidebar-foreground"
                    size="text-sm"
                />

                <FilaResumen
                    label="Costo Total General"
                    value={`$${(totals.totalCostoGeneral || 0).toLocaleString('es-CL')}`}
                    colorValue="text-sidebar-muted"
                    size="text-sm"
                />

                <FilaResumen
                    label="Prorrateo Logístico / u"
                    value={`$${(totals.prorrateoLineal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}`}
                    colorValue="text-brand-indigo"
                    size="text-sm"
                />

                {/* Comisión */}
                <div className="bg-warning/10 border border-warning/20 rounded-xl p-4">
                    <p className="text-[9px] font-black text-warning uppercase tracking-widest mb-1">
                        Comisión ejecutivo ({comisionPct.toFixed(1)}%)
                    </p>
                    <p className="text-2xl font-black text-warning tabular-nums">
                        ${comisionMonto.toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                    </p>
                </div>

                {/* Semáforo de margen */}
                <div className="pt-2">
                    <p className="text-[9px] font-black text-sidebar-muted uppercase tracking-widest mb-2">Estado del Margen</p>
                    <div className="flex gap-2">
                        {[
                            { label: '≥ 25%', active: margenNum >= 25, color: 'bg-success' },
                            { label: '≥ 15%', active: margenNum >= 15 && margenNum < 25, color: 'bg-warning' },
                            { label: '< 15%', active: margenNum < 15,  color: 'bg-destructive' },
                        ].map(({ label, active, color }) => (
                            <div key={label} className={`flex-1 h-1.5 rounded-full transition-all ${active ? color : 'bg-sidebar-border'}`} title={label} />
                        ))}
                    </div>
                    <p className="text-[8px] text-sidebar-muted mt-1.5 text-center">
                        {margenNum >= 25 ? 'Margen saludable' : margenNum >= 15 ? 'Margen ajustado' : 'Margen bajo — revisar costos'}
                    </p>
                </div>
            </div>
        </div>
    );
}
