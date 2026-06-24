import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { Check, ChevronDown, Search, Plus, Loader2 } from 'lucide-react';
import { useArticulosSearch } from '../../../hooks/useArticulosSearch';

export default function ComboSearchField({ value = '', onChange, tipo, placeholder = 'Buscar...', readOnly = false, className = '' }) {
    const [isOpen, setIsOpen] = useState(false);
    const [query, setQuery] = useState('');
    const [dropdownPos, setDropdownPos] = useState(null);
    const inputRef = useRef(null);
    const triggerRef = useRef(null);
    const containerRef = useRef(null);

    const { loading, search, createArticulo } = useArticulosSearch(tipo);
    const filtered = search(query);
    const exactMatch = filtered.some(a => a.nombreArticulo.toUpperCase() === query.trim().toUpperCase());
    const showCreate = query.trim().length > 0 && !exactMatch;

    useEffect(() => {
        if (!isOpen) return;
        const handler = (e) => {
            if (containerRef.current && !containerRef.current.contains(e.target)) {
                setIsOpen(false);
                setQuery('');
            }
        };
        document.addEventListener('mousedown', handler);
        return () => document.removeEventListener('mousedown', handler);
    }, [isOpen]);

    const open = () => {
        if (readOnly) return;
        if (triggerRef.current) {
            const r = triggerRef.current.getBoundingClientRect();
            setDropdownPos({ top: r.bottom + 4, left: r.left, width: Math.max(r.width, 220) });
        }
        setQuery('');
        setIsOpen(true);
        setTimeout(() => inputRef.current?.focus(), 50);
    };

    const select = (nombre) => {
        onChange(nombre.toUpperCase());
        setIsOpen(false);
        setQuery('');
    };

    const handleCreate = async () => {
        const nombre = query.trim();
        if (!nombre) return;
        await createArticulo(nombre);
        onChange(nombre.toUpperCase());
        setIsOpen(false);
        setQuery('');
    };

    return (
        <div ref={containerRef} className={`relative ${className}`}>
            <button
                ref={triggerRef}
                type="button"
                onClick={open}
                disabled={readOnly}
                className={[
                    'w-full flex items-center justify-between gap-2 px-3 py-2 rounded-lg border text-left text-[11px] font-semibold transition-all outline-none',
                    readOnly
                        ? 'bg-gray-50 border-gray-100 text-gray-500 cursor-default'
                        : 'bg-white border-gray-200 text-gray-800 hover:border-blue-300 cursor-pointer focus:border-blue-400 focus:ring-2 focus:ring-blue-50',
                    isOpen ? 'border-blue-400 ring-2 ring-blue-50' : '',
                ].join(' ')}
            >
                <span className={`truncate uppercase ${!value ? 'text-gray-400 font-normal' : ''}`}>
                    {value || placeholder}
                </span>
                {loading
                    ? <Loader2 className="w-3.5 h-3.5 text-gray-300 animate-spin flex-shrink-0" />
                    : <ChevronDown className={`w-3.5 h-3.5 text-gray-400 flex-shrink-0 transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
                }
            </button>

            {isOpen && dropdownPos && createPortal(
                <div
                    style={{
                        position: 'fixed',
                        top: dropdownPos.top,
                        left: dropdownPos.left,
                        width: dropdownPos.width,
                        zIndex: 9999,
                    }}
                    className="bg-white border border-gray-200 rounded-xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
                >
                    <div className="px-3 py-2.5 border-b border-gray-100 bg-gray-50/80">
                        <div className="flex items-center gap-2">
                            <Search className="w-3.5 h-3.5 text-gray-400 flex-shrink-0" />
                            <input
                                ref={inputRef}
                                type="text"
                                value={query}
                                onChange={(e) => setQuery(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Escape') { setIsOpen(false); setQuery(''); }
                                    if (e.key === 'Enter') {
                                        if (filtered.length === 1) { select(filtered[0].nombreArticulo); }
                                        else if (showCreate) { handleCreate(); }
                                    }
                                }}
                                placeholder="Escribir para buscar..."
                                className="flex-1 bg-transparent text-[11px] text-gray-700 outline-none placeholder:text-gray-400"
                            />
                        </div>
                    </div>

                    <div className="max-h-52 overflow-y-auto">
                        {loading && (
                            <div className="flex items-center justify-center gap-2 py-5">
                                <Loader2 className="w-3.5 h-3.5 text-gray-400 animate-spin" />
                                <span className="text-[10px] text-gray-400">Cargando maestros...</span>
                            </div>
                        )}

                        {!loading && filtered.length === 0 && !showCreate && (
                            <p className="text-center text-[10px] text-gray-400 py-6">
                                {query ? 'Sin resultados. Escribe para crear.' : 'Sin registros en maestros.'}
                            </p>
                        )}

                        {!loading && filtered.map((a) => {
                            const isSelected = a.nombreArticulo.toUpperCase() === value.toUpperCase();
                            return (
                                <button
                                    key={a.idArticulo}
                                    type="button"
                                    onClick={() => select(a.nombreArticulo)}
                                    className={`w-full flex items-center gap-3 px-4 py-2.5 text-left transition-colors
                                        ${isSelected ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'}`}
                                >
                                    <div className={`w-4 h-4 rounded-full flex-shrink-0 flex items-center justify-center border ${isSelected ? 'bg-blue-600 border-blue-600' : 'border-gray-300'}`}>
                                        {isSelected && <Check className="w-2.5 h-2.5 text-white stroke-[3]" />}
                                    </div>
                                    <span className="text-[11px] font-medium uppercase truncate">
                                        {a.nombreArticulo}
                                    </span>
                                </button>
                            );
                        })}
                    </div>

                    {showCreate && !loading && (
                        <div className="border-t border-gray-100 p-2">
                            <button
                                type="button"
                                onClick={handleCreate}
                                className="w-full flex items-center gap-2.5 px-3 py-2.5 bg-gray-900 hover:bg-blue-700 text-white rounded-lg transition-colors text-left"
                            >
                                <div className="w-5 h-5 rounded bg-white/10 flex items-center justify-center flex-shrink-0">
                                    <Plus className="w-3 h-3" />
                                </div>
                                <div className="min-w-0">
                                    <p className="text-[9px] font-black uppercase tracking-widest text-white/60 leading-none mb-0.5">
                                        Registrar en maestros y agregar
                                    </p>
                                    <p className="text-[11px] font-bold uppercase text-white truncate">
                                        {query.trim()}
                                    </p>
                                </div>
                            </button>
                        </div>
                    )}

                    {value && !query && (
                        <div className="border-t border-gray-100 px-3 py-1.5">
                            <button
                                type="button"
                                onClick={() => { onChange(''); setIsOpen(false); }}
                                className="w-full text-center text-[10px] text-gray-400 hover:text-red-500 transition-colors py-1"
                            >
                                Limpiar selección
                            </button>
                        </div>
                    )}
                </div>,
                document.body
            )}
        </div>
    );
}
