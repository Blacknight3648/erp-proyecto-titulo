import { Calculator, FileText, X } from 'lucide-react';

export default function EVNVinculacionesPanel({
    isReadOnly,
    solicitudesCostos,
    vinculados,
    setVinculados,
    selectedSCOSIds,
    selectedSCOTIds,
    toggleDocSelection,
    onBulkLink
}) {


    return (
        <>
            {(vinculados.scos.length > 0 || vinculados.scot.length > 0) && (
                <div className="px-8 py-4 bg-gray-50/50 border-b border-gray-100 flex gap-6 overflow-x-auto animate-in fade-in duration-500">
                    {vinculados.scos.map(doc => (
                        <div key={doc.id} className="flex items-center gap-2 bg-amber-100 text-amber-700 px-3 py-1.5 rounded-full text-[9px] font-black uppercase whitespace-nowrap shadow-sm border border-amber-200">
                            <Calculator className="w-3 h-3" /> {doc.numero}
                            {!isReadOnly && (
                                <button
                                    onClick={() => setVinculados(p => ({ ...p, scos: p.scos.filter(d => d.id !== doc.id) }))}
                                    className="hover:scale-125 transition-transform"
                                >
                                    <X className="w-3 h-3" />
                                </button>
                            )}
                        </div>
                    ))}
                    {vinculados.scot.map(doc => (
                        <div key={doc.id} className="flex items-center gap-2 bg-blue-100 text-blue-700 px-3 py-1.5 rounded-full text-[9px] font-black uppercase whitespace-nowrap shadow-sm border border-blue-200">
                            <FileText className="w-3 h-3" /> {doc.numero}
                            {!isReadOnly && (
                                <button
                                    onClick={() => setVinculados(p => ({ ...p, scot: p.scot.filter(d => d.id !== doc.id) }))}
                                    className="hover:scale-125 transition-transform"
                                >
                                    <X className="w-3 h-3" />
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </>
    );
}

