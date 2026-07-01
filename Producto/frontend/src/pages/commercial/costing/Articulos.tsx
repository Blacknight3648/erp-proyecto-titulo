import ArticuloItem from "./ArticuloItem";
import { Plus } from "lucide-react";

/**
 * Componente Articulos optimizado para trabajar con FormTabs
 * Soporta props estándar de panel (data, onAdd, onUpdate, onRemove)
 */
export default function Articulos({ 
  data, 
  onAdd, 
  onUpdate, 
  onRemove,
  // Props antiguos para compatibilidad si fuera necesario
  formData, 
  setFormData 
}) {

  // Normalizar datos (usar 'data' de FormTabs o 'formData.articulos' antiguo)
  const items = data || (formData && formData.articulos) || [];

  const handleUpdate = (id, updatedItem) => {
    if (onUpdate) {
      // FormTabs habitualmente pasa field y value, pero aquí pasamos el objeto completo o campos específicos
      // Para mantener compatibilidad con ArticuloItem que pasa el objeto actualizado:
      Object.keys(updatedItem).forEach(key => {
        onUpdate(id, key, updatedItem[key]);
      });
    } else if (setFormData) {
      setFormData(prev => ({
        ...prev,
        articulos: prev.articulos.map(a => a.id === id ? updatedItem : a)
      }));
    }
  };

  const handleRemove = (id) => {
    if (onRemove) {
      onRemove(id);
    } else if (setFormData) {
      setFormData(prev => ({
        ...prev,
        articulos: prev.articulos.filter(a => a.id !== id)
      }));
    }
  };

  return (
    <div className="space-y-6">
      {/* HEADER */}
      <div className="flex justify-between items-center mb-6">
        <h4 className="text-sm font-black text-slate-800 uppercase tracking-widest flex items-center gap-2">
          <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
          Productos / Artículos
        </h4>

        <button
          type="button"
          onClick={onAdd}
          className="flex items-center px-4 py-2 bg-slate-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all shadow-sm"
        >
          <Plus className="w-3.5 h-3.5 mr-2" />
          Agregar Artículo
        </button>
      </div>

      {/* LISTA VACÍA */}
      {items.length === 0 && (
        <div className="text-center py-20 bg-slate-50/50 rounded-2xl border-2 border-dashed border-slate-100">
           <p className="text-[10px] font-black text-slate-300 uppercase tracking-widest italic">
            No hay productos registrados todavía
          </p>
        </div>
      )}

      {/* LISTA DE ITEMS */}
      <div className="space-y-4">
        {items.map((articulo, index) => (
          <ArticuloItem
            key={articulo.id || index}
            articulo={articulo}
            index={index}
            onUpdate={(updated) => handleUpdate(articulo.id, updated)}
            onDelete={() => handleRemove(articulo.id)}
          />
        ))}
      </div>
    </div>
  );
}