import React, { useEffect } from "react";
import { Plus, Minus, Trash2, Hash } from "lucide-react";

const PRESETS = {
  LETRA: ["XS", "S", "M", "L", "XL", "XXL", "XXXL"],
  NUMERO: ["34", "36", "38", "40", "42", "44", "46", "48", "50"]
};

export default function Tallas({ tipoTalla, tallas, cantidadTotal, onChange }) {

  useEffect(() => {
    // Inicialización de presets si el arreglo está vacío
    if (tallas.length === 0) {
      if (tipoTalla === "LETRA" || tipoTalla === "NUMERO") {
        const base = PRESETS[tipoTalla].map(t => ({
          talla: t,
          cantidad: 0
        }));
        onChange(base, 0);
      }
    }
  }, [tipoTalla]);

  const updateQuantity = (idx, delta) => {
    const newTallas = [...tallas];
    const newQty = Math.max(0, (newTallas[idx].cantidad || 0) + delta);
    newTallas[idx] = { ...newTallas[idx], cantidad: newQty };
    
    const newTotal = newTallas.reduce((acc, t) => acc + (t.cantidad || 0), 0);
    onChange(newTallas, newTotal);
  };

  const handleManualQty = (idx, value) => {
    const val = parseInt(value) || 0;
    const newTallas = [...tallas];
    newTallas[idx] = { ...newTallas[idx], cantidad: Math.max(0, val) };
    
    const newTotal = newTallas.reduce((acc, t) => acc + (t.cantidad || 0), 0);
    onChange(newTallas, newTotal);
  };

  const addCustomSize = () => {
    const newTallas = [...tallas, { talla: "", cantidad: 0 }];
    const newTotal = newTallas.reduce((acc, t) => acc + (t.cantidad || 0), 0);
    onChange(newTallas, newTotal);
  };

  const removeSize = (idx) => {
    const newTallas = tallas.filter((_, i) => i !== idx);
    const newTotal = newTallas.reduce((acc, t) => acc + (t.cantidad || 0), 0);
    onChange(newTallas, newTotal);
  };

  const updateCustomName = (idx, name) => {
    const newTallas = [...tallas];
    newTallas[idx] = { ...newTallas[idx], talla: name };
    onChange(newTallas, cantidadTotal);
  };

  if (tipoTalla === "POR_DEFINIR") {
    return (
      <div className="flex flex-col items-center justify-center py-6 space-y-4">
        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
          Especificar Cantidad Total
        </label>
        <div className="flex items-center gap-4 bg-card p-2 rounded-2xl border-2 border-border shadow-sm">
          <button
            type="button"
            onClick={() => onChange([], Math.max(0, cantidadTotal - 1))}
            className="w-10 h-10 flex items-center justify-center bg-muted rounded-xl hover:bg-destructive/10 hover:text-destructive transition-all"
          >
            <Minus size={16} />
          </button>
          <input
            type="number"
            value={cantidadTotal}
            onChange={(e) => onChange([], Math.max(0, parseInt(e.target.value) || 0))}
            className="w-24 text-center font-black text-xl text-foreground outline-none bg-transparent"
          />
          <button
            type="button"
            onClick={() => onChange([], cantidadTotal + 1)}
            className="w-10 h-10 flex items-center justify-center bg-muted rounded-xl hover:bg-primary/10 hover:text-primary transition-all"
          >
            <Plus size={16} />
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center px-2">
        <h6 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest flex items-center gap-2">
          <Hash size={12} />
          Desglose de Curva / Tallas
        </h6>
        {tipoTalla === "PERSONALIZADA" && (
          <button
            type="button"
            onClick={addCustomSize}
            className="flex items-center gap-2 px-4 py-2 bg-foreground text-background rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-primary hover:text-primary-foreground transition-all"
          >
            <Plus size={12} />
            Agregar Talla
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        {tallas.map((t, idx) => (
          <div key={idx} className="bg-card p-4 rounded-2xl border border-border shadow-sm flex items-center justify-between group/item hover:border-primary/30 transition-all">
            <div className="flex flex-col gap-1 flex-1">
              {tipoTalla === "PERSONALIZADA" ? (
                <input
                  type="text"
                  value={t.talla}
                  onChange={(e) => updateCustomName(idx, e.target.value)}
                  placeholder="Ej: M Slim"
                  className="bg-transparent font-black text-xs text-primary outline-none w-full"
                />
              ) : (
                <span className="font-black text-xs text-foreground uppercase">{t.talla}</span>
              )}
              <span className="text-[9px] font-bold text-muted-foreground uppercase">Cantidad</span>
            </div>

            <div className="flex items-center gap-2">
              <div className="flex items-center bg-muted rounded-xl p-1 border border-border">
                <button
                  type="button"
                  onClick={() => updateQuantity(idx, -1)}
                  className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-card hover:text-destructive transition-all"
                >
                  <Minus size={14} />
                </button>
                <input
                  type="text"
                  value={t.cantidad}
                  onChange={(e) => handleManualQty(idx, e.target.value)}
                  className="w-10 text-center bg-transparent font-black text-xs text-foreground outline-none"
                />
                <button
                  type="button"
                  onClick={() => updateQuantity(idx, 1)}
                  className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-card hover:text-primary transition-all"
                >
                  <Plus size={14} />
                </button>
              </div>

              {tipoTalla === "PERSONALIZADA" && (
                <button
                  type="button"
                  onClick={() => removeSize(idx)}
                  className="p-2 text-muted-foreground/50 hover:text-destructive transition-colors"
                >
                  <Trash2 size={14} />
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {tallas.length === 0 && (
        <div className="text-center py-6 border-2 border-dotted border-border rounded-2xl">
          <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
            Sin tallas registradas todavía
          </p>
        </div>
      )}
    </div>
  );
}