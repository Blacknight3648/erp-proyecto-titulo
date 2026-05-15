import { Calculator } from 'lucide-react';

export default function EVNResumenSidebar({ totals, otrosCostos }) {
    const comisionPct = (otrosCostos?.porcentajeComision || 0) * 100;
    const comisionMonto = (totals.totalNeto || 0) * (otrosCostos?.porcentajeComision || 0);

    return (
        <div className="bg-gray-900 rounded-[2.5rem] p-8 text-white shadow-2xl relative overflow-hidden group">
            <div className="absolute top-0 right-0 w-40 h-40 bg-white/5 rounded-full -mr-10 -mt-10 group-hover:scale-125 transition-transform duration-1000" />
            <div className="relative z-10">
                <p className="text-[10px] font-black text-indigo-400 uppercase tracking-widest mb-8 flex items-center">
                    <Calculator className="w-4 h-4 mr-2" />
                    Resumen Estructural
                </p>
                <div className="space-y-6">
                    <div className="flex justify-between items-end border-b border-white/10 pb-4">
                        <p className="text-[10px] font-black text-gray-400 uppercase">Margen Real</p>
                        <p className="text-2xl font-black text-green-400 tracking-tighter">{totals.margenPorc}%</p>
                    </div>
                    <div className="flex justify-between items-end border-b border-white/10 pb-4">
                        <p className="text-[10px] font-black text-gray-400 uppercase">Utilidad Proyectada</p>
                        <p className="text-2xl font-black text-white tracking-tighter">
                            ${(totals.margenPesos || 0).toLocaleString('es-CL')}
                        </p>
                    </div>
                    <div className="flex justify-between items-end border-b border-white/10 pb-4">
                        <p className="text-[10px] font-black text-gray-400 uppercase">Prorrateo Logístico/u</p>
                        <p className="text-sm font-black text-indigo-300 tracking-tight">
                            ${(totals.prorrateoLineal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                        </p>
                    </div>
                    <div className="pt-4">
                        <p className="text-[10px] font-black text-gray-400 uppercase mb-2">
                            Comisión Proyectada ({comisionPct}%)
                        </p>
                        <p className="text-xl font-black text-amber-400">
                            ${comisionMonto.toLocaleString('es-CL')}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}
