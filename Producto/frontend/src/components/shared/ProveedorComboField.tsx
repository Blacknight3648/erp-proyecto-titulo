import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { ChevronDown, Search, Check, X, Loader2 } from 'lucide-react';
import { useProveedores } from '../../hooks/useProveedores';

interface ProveedorComboFieldProps {
    value?: string;
    onChange: (val: string) => void;
    readOnly?: boolean;
    placeholder?: string;
    className?: string;
}

export default function ProveedorComboField({
    value = '',
    onChange,
    readOnly = false,
    placeholder = 'Buscar proveedor...',
    className = '',
}: ProveedorComboFieldProps) {
    const [isOpen, setIsOpen] = useState(false);
    const [query, setQuery] = useState('');
    const [dropdownPos, setDropdownPos] = useState<{ top: number; left: number; width: number } | null>(null);

    const triggerRef = useRef<HTMLButtonElement>(null);
    const containerRef = useRef<HTMLDivElement>(null);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const inputRef = useRef<HTMLInputElement>(null);

    const { proveedores, loading } = useProveedores();

    const filtered = proveedores.filter((p) =>
        (p.nombreProveedor || p.nombre || '').toUpperCase().includes(query.toUpperCase())
    );

    // Close on outside click — excludes the portal dropdown itself
    useEffect(() => {
        if (!isOpen) return;
        const handler = (e: MouseEvent) => {
            const inTrigger = containerRef.current?.contains(e.target as Node);
            const inDropdown = dropdownRef.current?.contains(e.target as Node);
            if (!inTrigger && !inDropdown) {
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
            setDropdownPos({ top: r.bottom + 4, left: r.left, width: Math.max(r.width, 240) });
        }
        setQuery('');
        setIsOpen(true);
        setTimeout(() => inputRef.current?.focus(), 50);
    };

    const select = (nombre: string) => {
        onChange(nombre);
        setIsOpen(false);
        setQuery('');
    };

    const clear = (e: React.MouseEvent) => {
        e.stopPropagation();
        onChange('');
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
                <div className="flex items-center gap-1 flex-shrink-0">
                    {value && !readOnly && (
                        <X
                            className="w-3 h-3 text-muted-foreground hover:text-destructive transition-colors"
                            onClick={clear}
                        />
                    )}
                    {loading
                        ? <Loader2 className="w-3.5 h-3.5 text-muted-foreground animate-spin" />
                        : <ChevronDown className={`w-3.5 h-3.5 text-muted-foreground transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
                    }
                </div>
            </button>

            {isOpen && dropdownPos && createPortal(
                <div
                    ref={dropdownRef}
                    style={{
                        position: 'fixed',
                        top: dropdownPos.top,
                        left: dropdownPos.left,
                        width: dropdownPos.width,
                        zIndex: 9999,
                    }}
                    className="bg-card border border-border rounded-xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
                >
                    {/* Search input */}
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
                                    if (e.key === 'Enter' && filtered.length === 1) {
                                        select(filtered[0].nombreProveedor || filtered[0].nombre || '');
                                    }
                                }}
                                placeholder="Escribir para buscar..."
                                className="flex-1 bg-transparent text-[11px] text-foreground outline-none placeholder:text-muted-foreground"
                            />
                        </div>
                    </div>

                    {/* Options list */}
                    <div className="max-h-52 overflow-y-auto">
                        {loading && (
                            <div className="flex items-center justify-center gap-2 py-5">
                                <Loader2 className="w-3.5 h-3.5 text-muted-foreground animate-spin" />
                                <span className="text-[10px] text-muted-foreground">Cargando proveedores...</span>
                            </div>
                        )}

                        {!loading && filtered.length === 0 && (
                            <p className="text-center text-[10px] text-muted-foreground py-6">
                                {query ? 'Sin resultados.' : 'No hay proveedores registrados.'}
                            </p>
                        )}

                        {!loading && filtered.map((p) => {
                            const nombre = p.nombreProveedor || p.nombre || '';
                            const isSelected = nombre.toUpperCase() === value.toUpperCase();
                            return (
                                <button
                                    key={p.proveedorId || p.id}
                                    type="button"
                                    onClick={() => select(nombre)}
                                    className={`w-full flex items-center gap-3 px-4 py-2.5 text-left transition-colors
                                        ${isSelected ? 'bg-accent text-accent-foreground' : 'text-foreground hover:bg-muted'}`}
                                >
                                    <div className={`w-4 h-4 rounded-full flex-shrink-0 flex items-center justify-center border ${isSelected ? 'bg-primary border-primary' : 'border-border'}`}>
                                        {isSelected && <Check className="w-2.5 h-2.5 text-white stroke-[3]" />}
                                    </div>
                                    <span className="text-[11px] font-medium uppercase truncate">{nombre}</span>
                                </button>
                            );
                        })}
                    </div>

                    {/* Clear selection footer */}
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
