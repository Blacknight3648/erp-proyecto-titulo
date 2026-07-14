import { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { ChevronDown, Check } from 'lucide-react';

interface Option {
    value: string;
    label: string;
}

interface CustomSelectFieldProps {
    value: string;
    options: Option[];
    onChange: (val: string) => void;
    placeholder?: string;
    readOnly?: boolean;
    className?: string;
}

export default function CustomSelectField({
    value,
    options,
    onChange,
    placeholder = 'Seleccionar...',
    readOnly = false,
    className = ''
}: CustomSelectFieldProps) {
    const [isOpen, setIsOpen] = useState(false);
    const [dropdownPos, setDropdownPos] = useState<{ top: number; left: number; width: number } | null>(null);

    const triggerRef = useRef<HTMLButtonElement>(null);
    const containerRef = useRef<HTMLDivElement>(null);
    const dropdownRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!isOpen) return;
        const handler = (e: MouseEvent) => {
            const inTrigger = containerRef.current?.contains(e.target as Node);
            const inDropdown = dropdownRef.current?.contains(e.target as Node);
            if (!inTrigger && !inDropdown) {
                setIsOpen(false);
            }
        };
        document.addEventListener('mousedown', handler);
        return () => document.removeEventListener('mousedown', handler);
    }, [isOpen]);

    const open = () => {
        if (readOnly) return;
        if (triggerRef.current) {
            const r = triggerRef.current.getBoundingClientRect();
            setDropdownPos({ top: r.bottom + 4, left: r.left, width: Math.max(r.width, 100) });
        }
        setIsOpen(true);
    };

    const select = (val: string) => {
        onChange(val);
        setIsOpen(false);
    };

    const selectedOption = options.find(o => o.value === value);
    const displayValue = selectedOption ? selectedOption.label : value;

    return (
        <div ref={containerRef} className={`relative ${className}`}>
            <button
                ref={triggerRef}
                type="button"
                onClick={open}
                disabled={readOnly}
                className={[
                    'w-full flex items-center justify-between gap-2 px-2.5 py-2 rounded-xl border text-left text-[11px] font-bold uppercase transition-all outline-none',
                    readOnly
                        ? 'bg-muted border-border text-muted-foreground cursor-default'
                        : 'bg-card border-border hover:border-primary/60 text-foreground cursor-pointer focus:border-primary focus:ring-2 focus:ring-primary/20',
                    isOpen ? 'border-primary ring-2 ring-primary/20' : '',
                ].join(' ')}
            >
                <span className={`truncate ${!value ? 'text-muted-foreground font-semibold' : ''}`}>
                    {displayValue || placeholder}
                </span>
                <ChevronDown className={`w-3.5 h-3.5 text-muted-foreground flex-shrink-0 transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
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
                    className="bg-card border border-border rounded-xl shadow-[0_8px_32px_rgba(0,0,0,0.25)] overflow-hidden animate-in fade-in zoom-in-95 duration-150"
                >
                    <div className="max-h-52 overflow-y-auto py-1">
                        {options.map((opt) => {
                            const isSelected = opt.value === value;
                            return (
                                <button
                                    key={opt.value}
                                    type="button"
                                    onClick={() => select(opt.value)}
                                    className={`w-full flex items-center gap-2.5 px-3 py-2 text-left transition-colors
                                        ${isSelected ? 'bg-primary/10 text-primary' : 'text-foreground hover:bg-accent/80'}`}
                                >
                                    <div className={`w-3.5 h-3.5 rounded-full flex-shrink-0 flex items-center justify-center border transition-colors ${isSelected ? 'bg-primary border-primary' : 'border-border'}`}>
                                        {isSelected && <Check className="w-2.5 h-2.5 text-white stroke-[3]" />}
                                    </div>
                                    <span className="text-[11px] font-bold uppercase truncate">{opt.label}</span>
                                </button>
                            );
                        })}
                    </div>
                </div>,
                document.body
            )}
        </div>
    );
}
