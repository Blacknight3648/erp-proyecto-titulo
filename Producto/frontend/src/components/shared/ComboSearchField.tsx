import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { Check, ChevronDown, Search, Plus, Loader2 } from 'lucide-react';
import { useArticulosSearch } from '../../hooks/useArticulosSearch';

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
                        ? 'bg-muted border-border text-muted-foreground cursor-default'
                        : 'bg-card border-border text-foreground hover:border-primary cursor-pointer focus:border-primary focus:ring-2 focus:ring-accent',
                    isOpen ? 'border-primary ring-2 ring-accent' : '',
                ].join(' ')}
            >
                <span className={`truncate uppercase ${!value ? 'text-muted-foreground font-normal' : ''}`}>
                    {value || placeholder}
                </span>
                {loading
                    ? <Loader2 className="w-3.5 h-3.5 text-muted-foreground animate-spin flex-shrink-0" />
                    : <ChevronDown className={`w-3.5 h-3.5 text-muted-foreground flex-shrink-0 transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
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
                    className="bg-card border border-border rounded-xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
                >
                    <div className="px-3 py-2.5 border-b border-border bg-muted/80">
                        <div className="flex items-center gap-2">
                            <Search className="w-3.5 h-3.5 text-muted-foreground flex-shrink-0" />
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
                                className="flex-1 bg-transparent text-[11px] text-foreground outline-none placeholder:text-muted-foreground"
                            />
                        </div>
                    </div>

                    <div className="max-h-52 overflow-y-auto">
                        {loading && (
                            <div className="flex items-center justify-center gap-2 py-5">
                                <Loader2 className="w-3.5 h-3.5 text-muted-foreground animate-spin" />
                                <span className="text-[10px] text-muted-foreground">Cargando maestros...</span>
                            </div>
                        )}

                        {!loading && filtered.length === 0 && !showCreate && (
                            <p className="text-center text-[10px] text-muted-foreground py-6">
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
                                        ${isSelected ? 'bg-accent text-accent-foreground' : 'text-foreground hover:bg-muted'}`}
                                >
                                    <div className={`w-4 h-4 rounded-full flex-shrink-0 flex items-center justify-center border ${isSelected ? 'bg-primary border-primary' : 'border-border-strong'}`}>
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
                        <div className="border-t border-border p-2">
                            <button
                                type="button"
                                onClick={handleCreate}
                                className="w-full flex items-center gap-2.5 px-3 py-2.5 bg-foreground hover:bg-primary text-white rounded-lg transition-colors text-left"
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
                        <div className="border-t border-border px-3 py-1.5">
                            <button
                                type="button"
                                onClick={() => { onChange(''); setIsOpen(false); }}
                                className="w-full text-center text-[10px] text-muted-foreground hover:text-destructive transition-colors py-1"
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
