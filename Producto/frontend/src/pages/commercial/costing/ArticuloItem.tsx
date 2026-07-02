import Tallas from "./Tallas";
import { AlertCircle, Trash2 } from "lucide-react";

const TIPO_TALLA_OPTIONS = [
  { id: "LETRA", label: "Letra", desc: "XS, S, M, L..." },
  { id: "NUMERO", label: "Número", desc: "34, 36, 38..." },
  { id: "PERSONALIZADA", label: "Manual", desc: "Tallas personalizadas" },
  { id: "POR_DEFINIR", label: "Por Definir", desc: "Solo cantidad total" }
];

export default function ArticuloItem({
  articulo,
  index,
  onUpdate,
  onDelete
}) {

  const actualizarCampo = (campo, valor) => {
    onUpdate({
      ...articulo,
      [campo]: valor
    });
  };

  const actualizarTallas = (tallas, total) => {
    onUpdate({
      ...articulo,
      tallas: tallas,
      cantidadTotal: total
    });
  };

  return (
    <div className="bg-card border-2 border-border rounded-2xl p-8 space-y-8 hover:border-primary/30 transition-all group">

      {/* HEADER */}
      <div className="flex justify-between items-center border-b border-border pb-6">
        <div className="flex items-center gap-4">
          <div className="w-10 h-10 bg-sidebar text-white rounded-2xl flex items-center justify-center font-black text-sm">
            {index + 1}
          </div>
          <div>
            <h4 className="text-sm font-black text-foreground uppercase tracking-widest">
              Artículo Detallado
            </h4>
            <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest">
              Especificación de tallas y cantidades
            </p>
          </div>
        </div>

        <button
          type="button"
          onClick={onDelete}
          className="p-3 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-2xl transition-all"
          title="Eliminar artículo"
        >
          <Trash2 className="w-5 h-5" />
        </button>
      </div>

      {/* CAMPOS PRINCIPALES */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="space-y-2">
          <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
            Descripción del Artículo *
          </label>
          <input
            type="text"
            value={articulo.descripcion}
            onChange={(e) => actualizarCampo("descripcion", e.target.value)}
            placeholder="Ej: Polera deportiva dry-fit"
            className="w-full bg-muted p-4 rounded-2xl text-sm font-bold text-foreground outline-none border-2 border-transparent focus:border-primary focus:bg-card transition-all"
          />
        </div>

        <div className="space-y-2">
          <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
            Proveedor Referencia
          </label>
          <input
            type="text"
            value={articulo.proveedor}
            onChange={(e) => actualizarCampo("proveedor", e.target.value)}
            placeholder="Ej: Shein / Fabricante Nacional"
            className="w-full bg-muted p-4 rounded-2xl text-sm font-bold text-foreground outline-none border-2 border-transparent focus:border-primary focus:bg-card transition-all"
          />
        </div>

        <div className="space-y-2">
          <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
            Link Referencia
          </label>
          <input
            type="text"
            value={articulo.linkReferencia}
            onChange={(e) => actualizarCampo("linkReferencia", e.target.value)}
            placeholder="https://ejemplo.com/producto"
            className="w-full bg-muted p-4 rounded-2xl text-sm font-bold text-foreground outline-none border-2 border-transparent focus:border-primary focus:bg-card transition-all"
          />
        </div>
      </div>

      {/* TIPO DE TALLA - RADIO CARDS */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
            Tipo de Talla Obligatorio *
          </label>
          {!articulo.tipoTalla && (
            <div className="flex items-center gap-1.5 text-destructive animate-pulse">
              <AlertCircle size={12} />
              <span className="text-[10px] font-black uppercase tracking-tighter">Debe seleccionar un tipo de talla</span>
            </div>
          )}
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
          {TIPO_TALLA_OPTIONS.map((opt) => (
            <button
              key={opt.id}
              type="button"
              onClick={() => {
                // Al cambiar tipo, reseteamos tallas
                const newTallas = [];
                onUpdate({
                  ...articulo,
                  tipoTalla: opt.id,
                  tallas: newTallas,
                  cantidadTotal: opt.id === 'POR_DEFINIR' ? (articulo.cantidadTotal || 0) : 0
                });
              }}
              className={`p-4 rounded-2xl border-2 text-left transition-all ${
                articulo.tipoTalla === opt.id
                  ? "border-primary bg-primary/5"
                  : "border-border bg-muted/30 hover:border-border-strong"
              }`}
            >
              <div className="flex items-center justify-between mb-1">
                <span className={`text-[11px] font-black uppercase tracking-widest ${
                  articulo.tipoTalla === opt.id ? "text-primary" : "text-muted-foreground"
                }`}>
                  {opt.label}
                </span>
                {articulo.tipoTalla === opt.id && (
                  <div className="w-2.5 h-2.5 rounded-full bg-primary"></div>
                )}
              </div>
              <p className="text-[9px] font-bold text-muted-foreground uppercase leading-none">
                {opt.desc}
              </p>
            </button>
          ))}
        </div>
      </div>

      {/* SECCIÓN DE TALLAS DINÁMICA */}
      {articulo.tipoTalla ? (
        <div className="bg-muted/50 p-8 rounded-2xl border-2 border-dashed border-border">
          <Tallas
            tipoTalla={articulo.tipoTalla}
            tallas={articulo.tallas}
            cantidadTotal={articulo.cantidadTotal}
            onChange={actualizarTallas}
          />

          {/* TOTAL FINAL (SOLO LECTURA EXCEPTO EN POR_DEFINIR) */}
          <div className="mt-8 pt-6 border-t border-border flex justify-between items-center">
            <div>
              <h5 className="text-[10px] font-black text-foreground uppercase tracking-widest">Resumen de Carga</h5>
              <p className="text-[9px] font-bold text-muted-foreground uppercase">Cantidad calculada automáticamente</p>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Cant. Total:</span>
              <div className="bg-foreground text-background px-6 py-3 rounded-2xl font-black text-xl shadow-xl shadow-foreground/10 min-w-[80px] text-center">
                {articulo.cantidadTotal || 0}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="py-12 text-center rounded-2xl border-2 border-dashed border-border bg-muted/30">
          <p className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.3em]">
            Seleccione tipo de talla para continuar
          </p>
        </div>
      )}
    </div>
  );
}