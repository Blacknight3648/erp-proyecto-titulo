import { toast } from 'sonner';
import { validateNumericInput } from '../../utils/validations';

export default function CostosFijosPanel({ costos, onChange, readOnly = false }) {
  const fields = [
    { key: "flete", label: "Flete" },
    { key: "manoObra", label: "Mano de Obra" },
    { key: "hilos", label: "Hilos" },
    { key: "etiquetas", label: "Etiquetas" },
    { key: "embalaje", label: "Embalaje" },
    { key: "porcentajeCostoFijo", label: "% Costo Fijo" }
  ];

  return (
    <div className={`grid grid-cols-1 md:grid-cols-2 gap-6 ${readOnly ? 'opacity-90' : ''}`}>
      {fields.map(field => (
        <div key={field.key} className="space-y-2">
          <label className="text-[10px] font-black uppercase tracking-widest text-muted-foreground">
            {field.label}
          </label>
          <input
            type="number"
            readOnly={readOnly}
            value={(costos && costos[field.key]) || 0}
            onChange={(e) => {
              if (readOnly) return;
              const error = validateNumericInput(e.target.value, field.label);
              if (error) {
                toast.error(error);
                return;
              }
              onChange(field.key, parseFloat(e.target.value) || 0);
            }}
            className={`w-full p-3 rounded-xl bg-muted border border-border text-sm font-bold outline-none focus:ring-2 focus:ring-primary ${readOnly ? 'cursor-default' : ''}`}
          />
        </div>
      ))}
    </div>
  );
}