import { useState, useRef, useEffect } from 'react';
import { Search, ChevronDown, X } from 'lucide-react';

/**
 * Combobox estilizado de propósito general.
 * Recibe opciones locales y renderiza un dropdown propio (sin datalist nativo).
 *
 * @param {string}   value       - Valor seleccionado actualmente (el "key")
 * @param {function} onChange    - (value, label) => void
 * @param {Array}    options     - [{ value: string, label: string }]
 * @param {string}   placeholder
 * @param {boolean}  readOnly
 * @param {string}   className
 */
export default function ComboField({
    value = '',
    onChange,
    options = [],
    placeholder = 'Buscar...',
    readOnly = false,
    className = ''
}) {
    const [isOpen, setIsOpen] = useState(false);
    const [query, setQuery] = useState('');
    const inputRef = useRef(null);
    const containerRef = useRef(null);

    const filtered = options.filter(opt =>
        !query.trim() || opt.label.toLowerCase().includes(query.toLowerCase().trim())
    );

    const displayLabel = options.find(o => String(o.value) === String(value))?.label ?? value;

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
        setQuery('');
        setIsOpen(true);
        setTimeout(() => inputRef.current?.focus(), 50);
    };

    const select = (opt) => {
        onChange(opt.value, opt.label);
        setIsOpen(false);
        setQuery('');
    };

    const clear = (e) => {
        e.stopPropagation();
        onChange('', '');
    };

    return (
        <div ref={containerRef} className={`relative ${className}`}>
            <button
                type="button"
                onClick={open}
                disabled={readOnly}
                className={[
                    'w-full flex items-center justify-between gap-2 px-4 py-2.5 rounded-xl border text-left text-sm font-medium transition-all outline-none',
                    readOnly
                        ? 'bg-muted border-border text-muted-foreground cursor-default'
                        : 'bg-muted border-border text-foreground hover:border-border-strong cursor-pointer',
                    isOpen ? 'bg-card border-primary ring-2 ring-primary/10' : '',
                ].join(' ')}
            >
                <span className={`truncate ${!displayLabel ? 'text-muted-foreground/80 font-normal' : ''}`}>
                    {displayLabel || placeholder}
                </span>
                <div className="flex items-center gap-1 flex-shrink-0">
                    {value && !readOnly && (
                        <span
                            onClick={clear}
                            className="p-0.5 rounded hover:bg-secondary text-muted-foreground hover:text-muted-foreground transition-colors"
                        >
                            <X className="w-3 h-3" />
                        </span>
                    )}
                    <ChevronDown className={`w-3.5 h-3.5 text-muted-foreground transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
                </div>
            </button>

            {isOpen && (
                <div className="absolute top-full left-0 right-0 mt-1.5 z-[200] bg-card border border-border rounded-xl shadow-xl overflow-hidden animate-in fade-in zoom-in-95 duration-150">
                    <div className="px-3 py-2.5 border-b border-border bg-muted/60">
                        <div className="flex items-center gap-2">
                            <Search className="w-3.5 h-3.5 text-muted-foreground flex-shrink-0" />
                            <input
                                ref={inputRef}
                                type="text"
                                value={query}
                                onChange={(e) => setQuery(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Escape') { setIsOpen(false); setQuery(''); }
                                    if (e.key === 'Enter' && filtered.length === 1) select(filtered[0]);
                                }}
                                placeholder="Escribir para filtrar..."
                                className="flex-1 bg-transparent text-sm text-foreground outline-none placeholder:text-muted-foreground"
                            />
                        </div>
                    </div>

                    <div className="max-h-52 overflow-y-auto">
                        {filtered.length === 0 && (
                            <p className="text-center text-xs text-muted-foreground py-6">Sin resultados</p>
                        )}
                        {filtered.map((opt) => {
                            const isSelected = String(opt.value) === String(value);
                            return (
                                <button
                                    key={opt.value}
                                    type="button"
                                    onClick={() => select(opt)}
                                    className={[
                                        'w-full flex items-center px-4 py-2.5 text-left text-sm transition-colors',
                                        isSelected
                                            ? 'bg-accent text-accent-foreground font-semibold'
                                            : 'text-foreground hover:bg-muted font-medium',
                                    ].join(' ')}
                                >
                                    <span className="truncate">{opt.label}</span>
                                </button>
                            );
                        })}
                    </div>

                    {value && !query && (
                        <div className="border-t border-border px-3 py-1.5">
                            <button
                                type="button"
                                onClick={() => { onChange('', ''); setIsOpen(false); }}
                                className="w-full text-center text-xs text-muted-foreground hover:text-destructive transition-colors py-1"
                            >
                                Limpiar selección
                            </button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}
