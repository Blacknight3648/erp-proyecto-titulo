import { useState } from 'react';
import { Calculator, FileText, Search, X, Plus } from 'lucide-react';

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
    const [showSCOSSelector, setShowSCOSSelector] = useState(false);
    const [showSCOTSelector, setShowSCOTSelector] = useState(false);
    const [searchSCOS, setSearchSCOS] = useState('');
    const [searchSCOT, setSearchSCOT] = useState('');

    return (
        <>
            {!isReadOnly && (
                <div className="flex items-center gap-3 px-6 py-4 border-b border-gray-100 bg-gray-50/30">
                    <button
                        onClick={() => setShowSCOSSelector(v => !v)}
                        className={`flex items-center gap-2 px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest border transition-all shadow-sm ${showSCOSSelector ? 'bg-amber-600 text-white border-amber-600' : 'bg-amber-50 hover:bg-amber-100 text-amber-700 border-amber-200'}`}
                    >
                        <Calculator className="w-3.5 h-3.5" />
                        Vincular Costeo Técnico (SCOS)
                    </button>
                    <button
                        onClick={() => setShowSCOTSelector(v => !v)}
                        className={`flex items-center gap-2 px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest border transition-all shadow-sm ${showSCOTSelector ? 'bg-blue-600 text-white border-blue-600' : 'bg-blue-50 hover:bg-blue-100 text-blue-700 border-blue-200'}`}
                    >
                        <FileText className="w-3.5 h-3.5" />
                        Vincular Cotización Técnico (SCOT)
                    </button>
                </div>
            )}

            {showSCOSSelector && (
                <DocSelector
                    title="Listado de Costeos (SCOS) disponibles"
                    accent="amber"
                    placeholder="Filtrar por Nro SCOS o descripción..."
                    searchTerm={searchSCOS}
                    setSearchTerm={setSearchSCOS}
                    selectedIds={selectedSCOSIds}
                    onToggle={(id) => toggleDocSelection(id, 'SCOS')}
                    onBulkLink={() => onBulkLink('SCOS')}
                    onClose={() => setShowSCOSSelector(false)}
                    filterFn={(s) => {
                        const typeMatch = s.tipo?.toUpperCase() === 'SCOS' || s.tipo?.toUpperCase() === 'COSTEO';
                        const statusMatch = s.estado === 'Costeado' || s.estado === 'COSTEO REALIZADO' || s.estado === 'Costo Aprobado' || s.tipo?.toUpperCase() === 'SCOS';
                        return typeMatch && statusMatch;
                    }}
                    docs={solicitudesCostos}
                />
            )}

            {showSCOTSelector && (
                <DocSelector
                    title="Listado de Cotizaciones (SCOT) disponibles"
                    accent="blue"
                    placeholder="Filtrar por Nro SCOT o descripción..."
                    searchTerm={searchSCOT}
                    setSearchTerm={setSearchSCOT}
                    selectedIds={selectedSCOTIds}
                    onToggle={(id) => toggleDocSelection(id, 'SCOT')}
                    onBulkLink={() => onBulkLink('SCOT')}
                    onClose={() => setShowSCOTSelector(false)}
                    filterFn={(s) => s.tipo?.toUpperCase() === 'SCOT'}
                    docs={solicitudesCostos}
                />
            )}

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

function DocSelector({
    title,
    accent,
    placeholder,
    searchTerm,
    setSearchTerm,
    selectedIds,
    onToggle,
    onBulkLink,
    onClose,
    filterFn,
    docs
}) {
    const palette = accent === 'amber'
        ? { bg: 'bg-amber-50/50', border: 'border-amber-100', textStrong: 'text-amber-800', textNum: 'text-amber-600', btn: 'bg-amber-600 hover:bg-amber-700', focus: 'focus:ring-amber-500', cardBorder: 'border-amber-100 hover:border-amber-300', cardSelected: 'border-amber-500 bg-amber-50/30', check: 'bg-amber-600 border-amber-600', iconColor: 'text-amber-400', inputBorder: 'border-amber-200' }
        : { bg: 'bg-blue-50/50', border: 'border-blue-100', textStrong: 'text-blue-800', textNum: 'text-blue-600', btn: 'bg-blue-600 hover:bg-blue-700', focus: 'focus:ring-blue-500', cardBorder: 'border-blue-100 hover:border-blue-300', cardSelected: 'border-blue-500 bg-blue-50/30', check: 'bg-blue-600 border-blue-600', iconColor: 'text-blue-400', inputBorder: 'border-blue-200' };

    const filtered = docs.filter(s => {
        if (!filterFn(s)) return false;
        const q = searchTerm.toUpperCase();
        if (!q) return true;
        return (
            String(s.numero || '').toUpperCase().includes(q) ||
            String(s.articuloDescripcion || '').toUpperCase().includes(q)
        );
    });

    return (
        <div className={`p-6 border-b animate-in slide-in-from-top-4 duration-300 ${palette.bg} ${palette.border}`}>
            <div className="flex items-center justify-between mb-4">
                <h4 className={`text-[10px] font-black uppercase tracking-widest flex items-center gap-2 ${palette.textStrong}`}>
                    <Search className="w-3.5 h-3.5" />
                    {title}
                </h4>
                <div className="flex items-center gap-4">
                    {selectedIds.size > 0 && (
                        <button
                            onClick={onBulkLink}
                            className={`px-4 py-1.5 rounded-lg text-[10px] font-black uppercase tracking-widest text-white shadow-lg animate-in zoom-in duration-200 ${palette.btn}`}
                        >
                            Vincular {selectedIds.size} seleccionados
                        </button>
                    )}
                    <button onClick={onClose} className={`${palette.textStrong} hover:scale-110 transition-transform`}>
                        <X className="w-4 h-4" />
                    </button>
                </div>
            </div>
            <div className="relative mb-4">
                <Search className={`absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 ${palette.iconColor}`} />
                <input
                    type="text"
                    placeholder={placeholder}
                    className={`w-full pl-10 pr-4 py-2 bg-white border rounded-xl text-xs font-bold outline-none ${palette.inputBorder} focus:ring-2 ${palette.focus}`}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3 max-h-[200px] overflow-y-auto pr-2">
                {filtered.map(doc => (
                    <div
                        key={doc.id}
                        onClick={() => onToggle(doc.id)}
                        className={`bg-white p-3 rounded-xl border cursor-pointer transition-all flex items-start gap-3 hover:shadow-md ${selectedIds.has(doc.id) ? palette.cardSelected : palette.cardBorder}`}
                    >
                        <div className={`mt-1 w-4 h-4 rounded border flex items-center justify-center transition-all ${selectedIds.has(doc.id) ? palette.check : 'border-gray-200 bg-white'}`}>
                            {selectedIds.has(doc.id) && <Plus className="w-3 h-3 text-white" />}
                        </div>
                        <div className="flex-1">
                            <div className="flex justify-between items-start mb-1">
                                <span className={`text-[9px] font-black uppercase ${palette.textNum}`}>
                                    {doc.numero || `ID: ${doc.id}`}
                                </span>
                                <span className="text-[8px] font-bold text-gray-400 italic">
                                    ${(doc.costoTotal || 0).toLocaleString('es-CL')}
                                </span>
                            </div>
                            <p className="text-[10px] font-black text-gray-700 uppercase line-clamp-1">
                                {doc.articuloDescripcion}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
