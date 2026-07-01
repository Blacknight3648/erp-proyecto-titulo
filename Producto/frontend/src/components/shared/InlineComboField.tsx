import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';

/**
 * ComboBox de celda de tabla: input editable con sugerencias en dropdown.
 * El dropdown se renderiza en document.body (portal) para escapar de
 * cualquier contenedor con overflow:hidden o overflow-x:auto.
 *
 * @param {string}          value
 * @param {function}        onChange    - (value: string) => void
 * @param {string[]|Array}  options     - string[] o [{value, label}]
 * @param {string}          placeholder
 * @param {boolean}         readOnly
 * @param {string}          className
 */
export default function InlineComboField({
    value = '',
    onChange,
    options = [],
    placeholder = '',
    readOnly = false,
    className = ''
}) {
    const [focused, setFocused] = useState(false);
    const [inputValue, setInputValue] = useState(value || '');
    const [dropdownPos, setDropdownPos] = useState(null);
    const inputRef = useRef(null);

    // Normaliza el array de opciones a [{value, label}]
    const normalized = options.map(o =>
        typeof o === 'string' ? { value: o, label: o } : o
    );

    const filtered = normalized.filter(opt =>
        !inputValue.trim() || opt.label.toLowerCase().includes(inputValue.toLowerCase())
    ).slice(0, 10);

    const showDropdown = focused && filtered.length > 0 && !readOnly;

    // Sincroniza si el valor externo cambia
    useEffect(() => {
        setInputValue(value || '');
    }, [value]);

    const recalcPos = () => {
        if (!inputRef.current) return;
        const r = inputRef.current.getBoundingClientRect();
        setDropdownPos({ top: r.bottom + 4, left: r.left, width: r.width });
    };

    const handleFocus = () => {
        if (readOnly) return;
        recalcPos();
        setFocused(true);
    };

    const handleChange = (e) => {
        if (readOnly) return;
        const val = e.target.value.toUpperCase();
        setInputValue(val);
        onChange(val);
        recalcPos();
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Tab' && filtered.length > 0) {
            const first = filtered[0];
            setInputValue(first.label);
            onChange(first.value);
            setFocused(false);
            // No se llama preventDefault — Tab sigue su comportamiento nativo (avanza al próximo campo)
        }
        if (e.key === 'Escape') {
            setFocused(false);
        }
        if (e.key === 'Enter' && filtered.length > 0) {
            e.preventDefault();
            select(filtered[0]);
        }
    };

    const handleBlur = () => {
        // Pequeño delay para que onMouseDown del dropdown se ejecute primero
        setTimeout(() => setFocused(false), 120);
    };

    const select = (opt) => {
        setInputValue(opt.label);
        onChange(opt.value);
        setFocused(false);
    };

    return (
        <div className={`relative ${className}`}>
            <input
                ref={inputRef}
                type="text"
                value={inputValue}
                readOnly={readOnly}
                placeholder={placeholder}
                onFocus={handleFocus}
                onChange={handleChange}
                onKeyDown={handleKeyDown}
                onBlur={handleBlur}
                className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-bold text-gray-700 uppercase outline-none transition-all
                    ${readOnly ? 'cursor-default' : 'focus:bg-white focus:border-blue-300 focus:ring-2 focus:ring-blue-100'}`}
            />

            {showDropdown && dropdownPos && createPortal(
                <div
                    style={{
                        position: 'fixed',
                        top: dropdownPos.top,
                        left: dropdownPos.left,
                        width: Math.max(dropdownPos.width, 180),
                        zIndex: 9999,
                    }}
                    className="bg-white border border-slate-200 rounded-xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
                >
                    {filtered.map((opt) => (
                        <button
                            key={opt.value}
                            type="button"
                            onMouseDown={() => select(opt)}
                            className={`w-full text-left px-3 py-2 text-[11px] font-bold uppercase transition-colors
                                ${opt.value === value
                                    ? 'bg-blue-50 text-blue-700'
                                    : 'text-slate-700 hover:bg-slate-50 hover:text-slate-900'
                                }`}
                        >
                            {opt.label}
                        </button>
                    ))}
                </div>,
                document.body
            )}
        </div>
    );
}
