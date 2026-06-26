import { Calculator, TrendingUp, DollarSign, Package, Award } from 'lucide-react';

function FilaResumen({ label, value, colorValue = 'text-white', size = 'text-xl', border = true }) {
    return (
        <div className={`flex justify-between items-end ${border ? 'border-b border-white/10 pb-4' : ''}`}>
            <p className="text-[10px] font-black text-gray-400 uppercase leading-tight">{label}</p>
            <p className={`${size} font-black ${colorValue} tracking-tighter tabular-nums`}>{value}</p>
        </div>
    );
}

export default function EVNResumenSidebar({ totals, otrosCostos }) {
    const comisionPct  = otrosCostos?.porcentajeComision || 0;
    const comisionMonto = (totals.totalNeto || 0) * (comisionPct / 100);
    const margenNum    = parseFloat(totals.margenPorc || 0);
    const colorMargen  = margenNum >= 25 ? 'text-green-400' : margenNum >= 15 ? 'text-yellow-400' : 'text-red-400';

    return (
        <div className="bg-gray-900 rounded-[2rem] p-7 text-white shadow-2xl relative overflow-hidden group sticky top-24">
            <div className="absolute top-0 right-0 w-36 h-36 bg-white/5 rounded-full -mr-10 -mt-10 group-hover:scale-125 transition-transform duration-1000" />
            <div className="relative z-10 space-y-5">

                <div className="flex items-center gap-2 mb-2">
                    <Calculator className="w-4 h-4 text-indigo-400" />
                    <p className="text-[10px] font-black text-indigo-400 uppercase tracking-widest">Resumen Estructural</p>
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
                    colorValue="text-gray-300"
                    size="text-sm"
                />

                <FilaResumen
                    label="Costo Total General"
                    value={`$${(totals.totalCostoGeneral || 0).toLocaleString('es-CL')}`}
                    colorValue="text-gray-400"
                    size="text-sm"
                />

                <FilaResumen
                    label="Prorrateo Logístico / u"
                    value={`$${(totals.prorrateoLineal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}`}
                    colorValue="text-indigo-300"
                    size="text-sm"
                />

                {/* Comisión */}
                <div className="bg-amber-500/10 border border-amber-500/20 rounded-xl p-4">
                    <p className="text-[9px] font-black text-amber-400 uppercase tracking-widest mb-1">
                        Comisión ejecutivo ({comisionPct.toFixed(1)}%)
                    </p>
                    <p className="text-2xl font-black text-amber-400 tabular-nums">
                        ${comisionMonto.toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                    </p>
                </div>

                {/* Semáforo de margen */}
                <div className="pt-2">
                    <p className="text-[9px] font-black text-gray-500 uppercase tracking-widest mb-2">Estado del Margen</p>
                    <div className="flex gap-2">
                        {[
                            { label: '≥ 25%', active: margenNum >= 25, color: 'bg-green-500' },
                            { label: '≥ 15%', active: margenNum >= 15 && margenNum < 25, color: 'bg-yellow-500' },
                            { label: '< 15%', active: margenNum < 15,  color: 'bg-red-500' },
                        ].map(({ label, active, color }) => (
                            <div key={label} className={`flex-1 h-1.5 rounded-full transition-all ${active ? color : 'bg-white/10'}`} title={label} />
                        ))}
                    </div>
                    <p className="text-[8px] text-gray-500 mt-1.5 text-center">
                        {margenNum >= 25 ? 'Margen saludable' : margenNum >= 15 ? 'Margen ajustado' : 'Margen bajo — revisar costos'}
                    </p>
                </div>
            </div>
        </div>
    );
}
