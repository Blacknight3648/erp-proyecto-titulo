import { Plus, Trash2, ChevronDown, ChevronUp, Check, Loader2, AlertCircle, ClipboardList } from 'lucide-react';
import { useState, useRef, useEffect, useCallback } from 'react';
import { createPortal } from 'react-dom';
import { useTiposLogotipoSearch } from '../../hooks/useTiposLogotipoSearch';
import { useMaestroSearch } from '../../hooks/useMaestroSearch';

/* ═══════════════════════════════════════════════════════════
   CONSTANTES Y OPCIONES (Sentence Case Corporativo)
═══════════════════════════════════════════════════════════ */
const UNIDADES_MEDIDA = [
  { value: 'cm', label: 'cm', titulo: 'Centímetros' },
  { value: 'in', label: 'in', titulo: 'Pulgadas' },
  { value: 'mm', label: 'mm', titulo: 'Milímetros' },
  { value: 'px', label: 'px', titulo: 'Píxeles' },
  { value: 'pt', label: 'pt', titulo: 'Puntos' },
];

const DEFAULT_NOMBRES     = ['Logo pecho', 'Logo espalda', 'Logo manga izq', 'Logo manga der', 'Logo cuello', 'Logo gorra'];
const DEFAULT_UBICACIONES = ['Pecho', 'Espalda', 'Manga izquierda', 'Manga derecha', 'Cuello', 'Borde inferior', 'Hombro izquierdo', 'Hombro derecho'];
const DEFAULT_COLORES    = ['Blanco', 'Negro', 'Rojo', 'Azul', 'Verde', 'Amarillo', 'Gris', 'Café', 'Naranja', 'Multicolor'];

/* ═══════════════════════════════════════════════════════════
   COMPONENTE: SELECCIÓN UNIDAD DE MEDIDA
═══════════════════════════════════════════════════════════ */
function UnidadSelect({ value = 'cm', onChange, readOnly }) {
  const [open, setOpen] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    if (!open) return;
    const h = (e) => { if (ref.current && !ref.current.contains(e.target)) setOpen(false); };
    document.addEventListener('mousedown', h);
    return () => document.removeEventListener('mousedown', h);
  }, [open]);

  const selected = UNIDADES_MEDIDA.find(u => u.value === value) || UNIDADES_MEDIDA[0];

  const dropdownContent = () => {
    if (!ref.current) return null;
    const rect = ref.current.getBoundingClientRect();
    return (
      <div
        style={{ position: 'fixed', top: rect.bottom + 4, left: rect.left, zIndex: 10000, minWidth: 160 }}
        className="bg-card border border-border rounded-xl shadow-xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
      >
        {UNIDADES_MEDIDA.map(u => (
          <button
            key={u.value}
            type="button"
            onClick={() => { onChange(u.value); setOpen(false); }}
            className={`w-full flex items-center justify-between gap-3 px-3.5 py-2 text-left text-xs transition-colors ${
              u.value === value ? 'bg-muted text-foreground font-bold' : 'text-muted-foreground hover:bg-muted font-medium'
            }`}
          >
            <span className="font-semibold">{u.label}</span>
            <span className="text-muted-foreground text-[10px]">{u.titulo}</span>
            {u.value === value && <Check className="w-3.5 h-3.5 text-foreground flex-shrink-0" />}
          </button>
        ))}
      </div>
    );
  };

  return (
    <div ref={ref} className="relative shrink-0">
      <button
        type="button"
        disabled={readOnly}
        onClick={() => !readOnly && setOpen(o => !o)}
        className={`flex items-center gap-1.5 px-3 py-2.5 rounded-xl text-xs font-semibold border transition-all ${
          readOnly
            ? 'bg-muted border-border text-muted-foreground cursor-not-allowed'
            : 'bg-muted border-border text-foreground hover:bg-secondary/80 cursor-pointer'
        } ${open ? 'ring-2 ring-border-strong/10 border-border-strong' : ''}`}
      >
        {selected.label}
        {!readOnly && <ChevronDown className={`w-3 h-3 text-muted-foreground transition-transform ${open ? 'rotate-180' : ''}`} />}
      </button>
      {open && createPortal(dropdownContent(), document.body)}
    </div>
  );
}

/* ═══════════════════════════════════════════════════════════
   COMPONENTE: CAMPO AUTOCOMPLETADO CON MAESTRO
═══════════════════════════════════════════════════════════ */
function ComboAutoField({
  value = '',
  onChange,
  readOnly,
  placeholder = 'Escribe...',
  useMaestro,
  isInvalid,
}) {
  const { loading, search, exists, create } = useMaestro;
  const [inputVal, setInputVal] = useState(value);
  const [showDrop, setShowDrop] = useState(false);
  const [dropPos, setDropPos] = useState(null);
  const [highlightIdx, setHighlightIdx] = useState(0);
  const inputRef = useRef(null);
  const wrapRef  = useRef(null);

  useEffect(() => { setInputVal(value || ''); }, [value]);

  const filtered  = search(inputVal);
  const isExact   = exists(inputVal);
  const canCreate = inputVal.trim().length > 0 && !isExact;

  const openDrop = useCallback(() => {
    if (!inputRef.current) return;
    const r = inputRef.current.getBoundingClientRect();
    setDropPos({ top: r.bottom + 4, left: r.left, width: Math.max(r.width, 240) });
    setShowDrop(true);
    setHighlightIdx(0);
  }, []);

  useEffect(() => {
    if (!showDrop) return;
    const h = (e) => { if (wrapRef.current && !wrapRef.current.contains(e.target)) setShowDrop(false); };
    document.addEventListener('mousedown', h);
    return () => document.removeEventListener('mousedown', h);
  }, [showDrop]);

  const selectVal = (valText) => {
    setInputVal(valText);
    onChange(valText);
    setShowDrop(false);
  };

  const handleCreate = async () => {
    const nombre = inputVal.trim();
    if (!nombre) return;
    const result = await create(nombre);
    if (result) selectVal(result);
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Tab') {
      if (filtered.length > 0) { e.preventDefault(); selectVal(filtered[highlightIdx] || filtered[0]); }
      else if (canCreate) { e.preventDefault(); handleCreate(); }
    }
    if (e.key === 'ArrowDown') { e.preventDefault(); setHighlightIdx(i => Math.min(i + 1, filtered.length - 1)); }
    if (e.key === 'ArrowUp')   { e.preventDefault(); setHighlightIdx(i => Math.max(i - 1, 0)); }
    if (e.key === 'Enter') {
      e.preventDefault();
      if (showDrop && filtered.length > 0) selectVal(filtered[highlightIdx] || filtered[0]);
      else if (canCreate) handleCreate();
    }
    if (e.key === 'Escape') setShowDrop(false);
  };

  return (
    <div ref={wrapRef} className="relative w-full">
      <div className="flex items-center gap-2 relative">
        <input
          ref={inputRef}
          type="text"
          value={inputVal}
          readOnly={readOnly}
          placeholder={placeholder}
          onChange={(e) => { setInputVal(e.target.value); onChange(e.target.value); openDrop(); }}
          onFocus={() => !readOnly && openDrop()}
          onKeyDown={!readOnly ? handleKeyDown : undefined}
          className={`w-full px-4 py-2.5 rounded-xl border text-sm font-medium outline-none transition-all ${
            readOnly
              ? 'bg-muted border-border text-muted-foreground cursor-not-allowed'
              : isInvalid
              ? 'bg-destructive/10 border-destructive focus:border-destructive focus:ring-4 focus:ring-destructive/10 text-foreground'
              : 'bg-muted border-border text-foreground focus:bg-card focus:border-border-strong focus:ring-2 focus:ring-border-strong/10'
          }`}
        />
        {loading && <Loader2 className="w-4 h-4 text-muted-foreground animate-spin absolute right-3 shrink-0" />}
      </div>

      {showDrop && !readOnly && dropPos && createPortal(
        <div
          style={{ position: 'fixed', top: dropPos.top, left: dropPos.left, width: dropPos.width, zIndex: 10000 }}
          className="bg-card border border-border rounded-xl shadow-xl overflow-hidden animate-in fade-in zoom-in-95 duration-150"
        >
          {filtered.length > 0 && (
            <div className="max-h-48 overflow-y-auto divide-y divide-border">
              {filtered.map((opt, idx) => (
                <button
                  key={opt}
                  type="button"
                  onMouseDown={(e) => { e.preventDefault(); selectVal(opt); }}
                  onMouseEnter={() => setHighlightIdx(idx)}
                  className={`w-full flex items-center gap-3 px-4 py-2.5 text-left text-xs font-medium transition-colors ${
                    idx === highlightIdx ? 'bg-muted text-foreground' : 'text-muted-foreground'
                  }`}
                >
                  <span className="flex-1">{opt}</span>
                  {opt.toLowerCase() === (value || '').toLowerCase() && <Check className="w-3.5 h-3.5 text-foreground" />}
                </button>
              ))}
            </div>
          )}

          {canCreate && !loading && (
            <button
              type="button"
              onMouseDown={(e) => { e.preventDefault(); handleCreate(); }}
              className="w-full flex items-center gap-2 px-4 py-3 bg-foreground text-white transition-colors text-left"
            >
              <Plus className="w-4 h-4 text-muted-foreground" />
              <div className="text-xs min-w-0 flex-1">
                <p className="text-[10px] text-muted-foreground leading-none mb-0.5 font-semibold">Guardar nuevo en sistema:</p>
                <p className="font-bold truncate text-white">{inputVal.trim()}</p>
              </div>
            </button>
          )}

          {filtered.length === 0 && !canCreate && !loading && (
            <p className="text-center text-xs text-muted-foreground py-4">Sin registros coincidentes</p>
          )}
        </div>,
        document.body
      )}
    </div>
  );
}

/* ═══════════════════════════════════════════════════════════
   COMPONENTE: TARJETA INDIVIDUAL DE LOGOTIPO
═══════════════════════════════════════════════════════════ */
function LogotipoCard({ item, index, onUpdate, onRemove, readOnly }) {
  const maestroTipo      = useTiposLogotipoSearch();
  const maestroNombre   = useMaestroSearch('LOGO_NOMBRE',    DEFAULT_NOMBRES);
  const maestroUbicacion = useMaestroSearch('LOGO_UBICACION', DEFAULT_UBICACIONES);
  const maestroColor    = useMaestroSearch('LOGO_COLOR',     DEFAULT_COLORES);

  const subtotal = (item.cantidad || 0) * (item.precio || 0);

  // Estados de error visuales basados en reglas corporativas
  const errors = {
    tipo: !item.tipo?.trim(),
    nombre: !item.nombre?.trim(),
    cantidad: !item.cantidad || item.cantidad < 1,
  };

  return (
    <div className="bg-card border border-border rounded-xl shadow-sm hover:shadow-md transition-all duration-200 group overflow-hidden">
      
      {/* Encabezado limpio de la Ficha */}
      <div className="flex items-center justify-between px-5 py-3 bg-muted/80 border-b border-border">
        <div className="flex items-center gap-3">
          <span className="px-2.5 py-0.5 rounded-md bg-secondary text-foreground text-xs font-bold font-mono">
            ID: {index + 1}
          </span>
          <span className="text-xs font-bold text-muted-foreground uppercase tracking-wide">
            Especificación de Logotipo
          </span>
        </div>

        <div className="flex items-center gap-4">
          <div className="text-right">
            <span className="text-xs font-semibold text-muted-foreground mr-1.5">Subtotal:</span>
            <span className="text-sm font-bold text-foreground">
              ${subtotal.toLocaleString('es-CL', { minimumFractionDigits: 0 })}
            </span>
          </div>
          {!readOnly && (
            <button
              type="button"
              onClick={() => onRemove(item.id)}
              className="p-1.5 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-all md:opacity-0 md:group-hover:opacity-100"
              title="Eliminar registro"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>

      {/* Rejilla de campos optimizada horizontalmente en 3 columnas */}
      <div className="p-5 grid grid-cols-1 md:grid-cols-3 gap-5 items-start">
        
        {/* Fila 1: Técnica, Nombre y Ubicación */}
        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Técnica / Tipo de Bordado <span className="text-destructive">*</span></label>
          <ComboAutoField
            value={item.tipo || ''}
            onChange={(val) => !readOnly && onUpdate(item.id, 'tipo', val)}
            readOnly={readOnly}
            placeholder="Ej: Bordado, Estampado..."
            useMaestro={maestroTipo}
            isInvalid={errors.tipo}
          />
          {errors.tipo && (
            <p className="text-[11px] text-destructive flex items-center gap-1"><AlertCircle className="w-3 h-3" /> Requerido</p>
          )}
        </div>

        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Nombre identificador <span className="text-destructive">*</span></label>
          <ComboAutoField
            value={item.nombre || ''}
            onChange={(val) => !readOnly && onUpdate(item.id, 'nombre', val)}
            readOnly={readOnly}
            placeholder="Ej: Logo Pecho Corporativo"
            useMaestro={maestroNombre}
            isInvalid={errors.nombre}
          />
          {errors.nombre && (
            <p className="text-[11px] text-destructive flex items-center gap-1"><AlertCircle className="w-3 h-3" /> Requerido</p>
          )}
        </div>

        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Ubicación física</label>
          <ComboAutoField
            value={item.ubicacion || ''}
            onChange={(val) => !readOnly && onUpdate(item.id, 'ubicacion', val)}
            readOnly={readOnly}
            placeholder="Ej: Manga izquierda, Espalda..."
            useMaestro={maestroUbicacion}
          />
        </div>

        {/* Fila 2: Color, Dimensión y Cantidad/Precio */}
        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Color dominante</label>
          <ComboAutoField
            value={item.color || ''}
            onChange={(val) => !readOnly && onUpdate(item.id, 'color', val)}
            readOnly={readOnly}
            placeholder="Ej: Azul Marino, Blanco..."
            useMaestro={maestroColor}
          />
        </div>

        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Dimensión / Tamaño</label>
          <div className="flex items-center gap-2">
            <input
              type="number"
              step="0.1"
              min="0"
              value={item.tamanio || ''}
              disabled={readOnly}
              onChange={(e) => {
                const val = parseFloat(e.target.value);
                onUpdate(item.id, 'tamanio', isNaN(val) || val < 0 ? '' : val);
              }}
              placeholder="0.0"
              className={`w-full px-4 py-2.5 rounded-xl border text-sm font-medium text-foreground outline-none transition-all ${
                readOnly ? 'bg-muted border-border cursor-not-allowed' : 'bg-muted border-border focus:bg-card focus:border-border-strong focus:ring-2 focus:ring-border-strong/10'
              }`}
            />
            <UnidadSelect
              value={item.unidadMedidaLogo || 'cm'}
              onChange={(u) => !readOnly && onUpdate(item.id, 'unidadMedidaLogo', u)}
              readOnly={readOnly}
            />
          </div>
        </div>

        <div className="space-y-1.5">
          <label className="text-xs font-semibold text-muted-foreground">Costos operativos</label>
          <div className="flex items-center gap-3">
            {/* Cantidad */}
            <div className="w-1/2">
              <input
                type="number"
                min="1"
                value={item.cantidad || ''}
                disabled={readOnly}
                onChange={(e) => {
                  const val = parseInt(e.target.value);
                  onUpdate(item.id, 'cantidad', isNaN(val) || val < 1 ? '' : val);
                }}
                placeholder="Cant"
                className={`w-full px-3 py-2.5 rounded-xl border text-sm font-bold text-center outline-none transition-all ${
                  readOnly 
                    ? 'bg-muted border-border text-muted-foreground' 
                    : errors.cantidad
                    ? 'border-destructive bg-destructive/10 focus:ring-4 focus:ring-destructive/10'
                    : 'bg-accent/40 border-accent text-accent-foreground focus:bg-card focus:border-primary'
                }`}
              />
            </div>
            
            {/* Precio Unitario */}
            <div className="w-1/2 flex items-center bg-muted border border-border rounded-xl px-3 focus-within:bg-card focus-within:border-border-strong focus-within:ring-2 focus-within:ring-border-strong/10 transition-all">
              <span className="text-xs font-semibold text-muted-foreground mr-1">$</span>
              <input
                type="number"
                min="0"
                step="0.01"
                value={item.precio || ''}
                disabled={readOnly}
                onChange={(e) => {
                  const val = parseFloat(e.target.value);
                  onUpdate(item.id, 'precio', isNaN(val) || val < 0 ? '' : val);
                }}
                placeholder="Precio"
                className="w-full bg-transparent border-none outline-none py-2.5 text-sm font-medium text-foreground"
              />
            </div>
          </div>
          {errors.cantidad && (
            <p className="text-[11px] text-destructive flex items-center gap-1"><AlertCircle className="w-3 h-3" /> Mínimo 1 unidad</p>
          )}
        </div>

      </div>
    </div>
  );
}

/* ═══════════════════════════════════════════════════════════
   PANEL DE CONTROL PRINCIPAL
═══════════════════════════════════════════════════════════ */
export default function LogotipoPanel({ data, onAdd, onUpdate, onRemove, readOnly = false, nombrePrenda = "" }) {
  const [isExpanded, setIsExpanded] = useState(true);
  const items = data || [];

  return (
    <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
      
      {/* Header Corporativo del Panel */}
      <div className="flex justify-between items-center bg-foreground px-5 py-4 rounded-xl text-white shadow-sm">
        <div className="flex items-center gap-2.5">
          <ClipboardList className="w-4 h-4 text-accent-foreground" />
          <h4 className="text-sm font-semibold tracking-wide text-white">
            {nombrePrenda ? `Módulo de Logotipos — ${nombrePrenda}` : "Módulo de Logotipos y Diseños de Marca"}
          </h4>
          <span className="ml-2 bg-primary/20 text-accent-foreground text-[11px] font-bold px-2.5 py-0.5 rounded-full border border-primary/30">
            {items.length} {items.length === 1 ? 'Ítem asignado' : 'Ítems asignados'}
          </span>
        </div>

        <div className="flex items-center gap-3">
          {!readOnly && (
            <button
              type="button"
              onClick={onAdd}
              className="flex items-center gap-1.5 px-4 py-1.5 bg-primary hover:bg-primary text-white rounded-lg text-xs font-bold transition-colors shadow-sm"
            >
              <Plus className="w-4 h-4" />
              Agregar Logotipo
            </button>
          )}
          <button
            type="button"
            onClick={() => setIsExpanded(e => !e)}
            className="p-1.5 hover:bg-white/10 rounded-lg text-muted-foreground hover:text-white transition-all"
          >
            {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
          </button>
        </div>
      </div>

      {/* Contenido Desplegable */}
      {isExpanded ? (
        <div className="space-y-4 animate-in slide-in-from-top-2 duration-200">
          {items.length === 0 ? (
            <div className="bg-card py-12 rounded-xl border border-dashed border-border-strong text-center space-y-3">
              <p className="text-sm font-medium text-muted-foreground">
                No existen logotipos o marcas registradas en esta ficha técnica.
              </p>
              {!readOnly && (
                <button
                  type="button"
                  onClick={onAdd}
                  className="inline-flex items-center gap-2 px-4 py-2 bg-foreground hover:bg-foreground text-white rounded-lg text-xs font-bold transition-all"
                >
                  <Plus className="w-4 h-4" />
                  Insertar primer registro
                </button>
              )}
            </div>
          ) : (
            items.map((item, index) => (
              <LogotipoCard
                key={item.id || index}
                item={item}
                index={index}
                onUpdate={onUpdate}
                onRemove={onRemove}
                readOnly={readOnly}
              />
            ))
          )}
        </div>
      ) : (
        <div className="bg-muted p-3.5 rounded-xl text-center text-xs font-medium text-muted-foreground border border-border">
          Sección colapsada de logotipos • {items.length} registros vigentes.
        </div>
      )}
    </div>
  );
}