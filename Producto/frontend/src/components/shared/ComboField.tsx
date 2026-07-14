import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
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
 * @param {boolean}  portal      - Si es true, el dropdown se renderiza en un portal (document.body),
 *                                 posicionado en `fixed`. Usar dentro de contenedores con overflow
 *                                 recortado (ej. celdas de tabla).
 * @param {boolean}  allowCustom - Si es true, permite confirmar un valor libre que no está en
 *                                 `options` (aparece un footer "Usar '<texto>'").
 */
export default function ComboField({
    value = '',
    onChange,
    options = [],
    placeholder = 'Buscar...',
    readOnly = false,
    className = '',
    portal = false,
    allowCustom = false
}) {
    const [isOpen, setIsOpen] = useState(false);
    const [query, setQuery] = useState('');
    const [dropdownPos, setDropdownPos] = useState(null);
    const inputRef = useRef(null);
    const triggerRef = useRef(null);
    const containerRef = useRef(null);
    const dropdownRef = useRef(null);

    const filtered = options.filter(opt =>
        !query.trim() || opt.label.toLowerCase().includes(query.toLowerCase().trim())
    );

    const displayLabel = options.find(o => String(o.value) === String(value))?.label ?? value;

    const exactMatch = filtered.some(o => o.label.toLowerCase() === query.trim().toLowerCase());
    const showCustom = allowCustom && query.trim().length > 0 && !exactMatch;

    useEffect(() => {
        if (!isOpen) return;
        const handler = (e) => {
            const inTrigger = containerRef.current && containerRef.current.contains(e.target);
            const inDropdown = dropdownRef.current && dropdownRef.current.contains(e.target);
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
        if (portal && triggerRef.current) {
            const r = triggerRef.current.getBoundingClientRect();
            setDropdownPos({ top: r.bottom + 6, left: r.left, width: Math.max(r.width, 220) });
        }
        setQuery('');
        setIsOpen(true);
        setTimeout(() => inputRef.current?.focus(), 50);
    };

    const select = (opt) => {
        onChange(opt.value, opt.label);
        setIsOpen(false);
        setQuery('');
    };

    const selectCustom = () => {
        const texto = query.trim();
        if (!texto) return;
        onChange(texto, texto);
        setIsOpen(false);
        setQuery('');
    };

    const clear = (e) => {
        e.stopPropagation();
        onChange('', '');
    };

    const dropdownContent = (
        <div
            ref={dropdownRef}
            style={portal ? { position: 'fixed', top: dropdownPos?.top, left: dropdownPos?.left, width: dropdownPos?.width, zIndex: 9999 } : undefined}
            className={portal
                ? 'bg-popover text-popover-foreground border border-border rounded-xl shadow-[0_8px_32px_rgba(0,0,0,0.25)] overflow-hidden animate-in fade-in slide-in-from-top-1 duration-150'
                : 'absolute top-full left-0 right-0 mt-1.5 z-[200] bg-card border border-border rounded-xl shadow-xl overflow-hidden animate-in fade-in zoom-in-95 duration-150'}
        >
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
                            if (e.key === 'Enter') {
                                if (filtered.length === 1) select(filtered[0]);
                                else if (showCustom) selectCustom();
                            }
                        }}
                        placeholder="Escribir para filtrar..."
                        className="flex-1 bg-transparent text-sm text-foreground outline-none placeholder:text-muted-foreground"
                    />
                </div>
            </div>

            <div className="max-h-52 overflow-y-auto">
                {filtered.length === 0 && !showCustom && (
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

            {showCustom && (
                <div className="border-t border-border p-2">
                    <button
                        type="button"
                        onClick={selectCustom}
                        className="w-full flex items-center px-3 py-2.5 bg-foreground hover:bg-primary text-white rounded-lg transition-colors text-left"
                    >
                        <span className="text-[11px] font-bold truncate">Usar "{query.trim()}"</span>
                    </button>
                </div>
            )}

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
    );

    return (
        <div ref={containerRef} className={`relative ${className}`}>
            <button
                ref={triggerRef}
                type="button"
                onClick={open}
                disabled={readOnly}
                className={[
                    'w-full flex items-center justify-between gap-3 p-3.5 rounded-xl text-left transition-all outline-none',
                    readOnly
                        ? 'bg-muted/50 border-none text-muted-foreground cursor-default'
                        : 'bg-muted hover:bg-muted/80 text-foreground cursor-pointer',
                    isOpen ? 'ring-2 ring-primary/50' : 'border-none',
                ].join(' ')}
            >
                <span className={`text-xs font-bold uppercase truncate ${!displayLabel ? 'text-muted-foreground/50' : ''}`}>
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

            {isOpen && (portal ? (dropdownPos && createPortal(dropdownContent, document.body)) : dropdownContent)}
        </div>
    );
}
