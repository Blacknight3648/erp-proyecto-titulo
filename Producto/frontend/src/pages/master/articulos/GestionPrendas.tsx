import React, { useState, useEffect } from 'react';
import { api } from '../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { Package, Plus, Edit, Trash2, Save, Search, RefreshCw, X } from 'lucide-react';
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Dialog, DialogContent, DialogTitle } from "../../../ui/dialog";
import { Checkbox } from "../../../ui/checkbox";
import { confirmDelete } from '../../../utils/confirmDelete';

const TIPOS_PRENDA = [
  { value: 'PRENDA_LISTA', label: 'Prenda Lista (Stock)' },
  { value: 'PRENDA_CONFECCIONAR', label: 'Prenda a Confeccionar (A medida)' },
];

const emptyForm = () => ({
  nombreArticulo: '',
  descripcionArticulo: '',
  stockActual: 0,
  tipoArticulo: 'PRENDA_LISTA',
  detallePrenda: {
    marca: '',
    tallasDisponibles: '',
    proveedor: '',
    codigoProveedor: '',
    requiereLogoCliente: false,
    tieneEstampado: false,
    ubicacionLogo: '',
  },
});

export default function GestionPrendas() {
  const [prendas, setPrendas] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);
  const [formData, setFormData] = useState<any>(emptyForm());

  useEffect(() => {
    loadPrendas();
  }, []);

  const loadPrendas = async () => {
    setLoading(true);
    try {
      const [resLista, resConfeccionar] = await Promise.all([
        api.get('/maestros/articulos/tipo/PRENDA_LISTA'),
        api.get('/maestros/articulos/tipo/PRENDA_CONFECCIONAR'),
      ]);
      setPrendas([...(resLista.data as any[]), ...(resConfeccionar.data as any[])].filter((p: any) => p.activo !== false));
    } catch (error) {
      console.error(error);
      toast.error("Error al cargar prendas");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setFormData(emptyForm());
    setIsModalOpen(true);
  };

  const handleOpenEdit = (item: any) => {
    setEditingItem(item);
    const detalle = item.detallePrenda || {};
    setFormData({
      ...item,
      detallePrenda: {
        marca: detalle.marca || '',
        tallasDisponibles: detalle.tallasDisponibles || '',
        proveedor: detalle.proveedor || '',
        codigoProveedor: detalle.codigoProveedor || '',
        requiereLogoCliente: detalle.requiereLogoCliente || false,
        tieneEstampado: detalle.tieneEstampado || false,
        ubicacionLogo: detalle.ubicacionLogo || '',
      },
    });
    setIsModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.nombreArticulo) {
      return toast.error("El nombre de la prenda es obligatorio");
    }

    const payload = {
      nombreArticulo: formData.nombreArticulo,
      descripcionArticulo: formData.descripcionArticulo,
      stockActual: Number(formData.stockActual || 0),
      tipoArticulo: formData.tipoArticulo,
      detallePrenda: formData.detallePrenda,
    };

    try {
      if (editingItem) {
        await api.put(`/maestros/articulos/${editingItem.idArticulo}`, payload);
        toast.success("Prenda actualizada correctamente");
      } else {
        await api.post('/maestros/articulos', payload);
        toast.success("Prenda creada correctamente. El código se generó automáticamente.");
      }
      setIsModalOpen(false);
      loadPrendas();
    } catch (error) {
      console.error(error);
      toast.error("Ocurrió un error al guardar la Prenda");
    }
  };

  const handleDelete = (item: any) => {
    confirmDelete(`¿Eliminar de forma permanente la prenda "${item.nombreArticulo}"?`, async () => {
      try {
        await api.delete(`/maestros/articulos/${item.idArticulo}`);
        toast.success("Prenda eliminada exitosamente");
        loadPrendas();
      } catch (error) {
        console.error(error);
        toast.error("Error al eliminar");
      }
    });
  };

  const filteredData = prendas.filter(item =>
    item.nombreArticulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.codigoArticulo.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const tipoLabel = (tipo: string) => TIPOS_PRENDA.find(t => t.value === tipo)?.label || tipo;

  return (
    <div className="flex flex-col h-full bg-white rounded-2xl border border-zinc-200 overflow-hidden shadow-sm">
      <Toaster position="top-right" richColors />

      {/* Header */}
      <div className="px-6 py-5 border-b border-zinc-200 flex justify-between items-center bg-zinc-50/50">
        <div>
          <h2 className="text-lg font-bold text-zinc-900 tracking-tight uppercase">Prendas (Producto Terminado)</h2>
          <p className="text-xs font-medium text-zinc-500 mt-0.5">Gestión de prendas listas y a confeccionar</p>
        </div>
        <Button onClick={handleOpenNew} className="bg-zinc-900 text-white rounded-xl font-bold uppercase text-[11px] tracking-widest px-6 h-11 hover:bg-zinc-800 shadow-sm flex items-center gap-2">
          <Plus size={14} /> Nueva Prenda
        </Button>
      </div>

      {/* Toolbar */}
      <div className="px-6 py-4 flex gap-3 border-b border-zinc-100 bg-white">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-400" size={16} />
          <Input
            placeholder="Buscar por código o nombre..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10 h-10 rounded-xl bg-zinc-50 border-zinc-200 text-xs font-medium uppercase"
          />
        </div>
        <Button variant="outline" size="icon" onClick={loadPrendas} className="h-10 w-10 rounded-xl border-zinc-200 text-zinc-500 hover:text-zinc-900">
          <RefreshCw size={16} className={loading ? "animate-spin" : ""} />
        </Button>
      </div>

      {/* List */}
      <div className="flex-1 overflow-y-auto p-6 bg-zinc-50/30 custom-scrollbar">
        {loading ? (
          <div className="flex justify-center items-center h-32">
            <RefreshCw size={24} className="animate-spin text-zinc-400" />
          </div>
        ) : filteredData.length === 0 ? (
          <div className="text-center p-12 bg-white border border-dashed border-zinc-300 rounded-2xl">
            <Package size={32} className="mx-auto text-zinc-300 mb-3" />
            <h3 className="text-sm font-bold text-zinc-900 uppercase">Sin Prendas Registradas</h3>
            <p className="text-xs text-zinc-500 mt-1">Comienza agregando una prenda lista o a confeccionar.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {filteredData.map(item => (
              <div key={item.idArticulo} className="flex items-center justify-between p-4 bg-white border border-zinc-200 rounded-xl hover:border-zinc-300 transition-colors shadow-sm">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-zinc-100 rounded-lg flex items-center justify-center text-zinc-400 border border-zinc-200">
                    <Package size={20} strokeWidth={1.5} />
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-zinc-900 uppercase">{item.nombreArticulo}</h4>
                    <div className="flex items-center gap-2 mt-1 flex-wrap">
                      <span className="text-[10px] font-bold px-2 py-0.5 bg-zinc-100 text-zinc-600 rounded-md border border-zinc-200">
                        SKU: {item.codigoArticulo}
                      </span>
                      <span className="text-[10px] font-bold px-2 py-0.5 bg-purple-50 text-purple-600 rounded-md border border-purple-100 uppercase">
                        {tipoLabel(item.tipoArticulo)}
                      </span>
                      {item.detallePrenda?.tallasDisponibles && (
                        <span className="text-[10px] font-bold px-2 py-0.5 bg-zinc-100 text-zinc-600 rounded-md border border-zinc-200 uppercase">
                          Tallas: {item.detallePrenda.tallasDisponibles}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
                <div className="flex gap-2">
                  <Button variant="ghost" size="icon" onClick={() => handleOpenEdit(item)} className="h-8 w-8 text-zinc-500 hover:bg-zinc-100 rounded-lg">
                    <Edit size={14} />
                  </Button>
                  <Button variant="ghost" size="icon" onClick={() => handleDelete(item)} className="h-8 w-8 text-red-500 hover:bg-red-50 rounded-lg">
                    <Trash2 size={14} />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Modal CRUD Prendas */}
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto p-0 rounded-2xl border-zinc-200 bg-white shadow-2xl">
          <div className="sticky top-0 z-10 bg-white/80 backdrop-blur-md border-b border-zinc-200 px-6 py-4 flex justify-between items-center">
            <DialogTitle className="text-lg font-extrabold tracking-tight text-zinc-900 uppercase">
              {editingItem ? 'Editar Prenda' : 'Registrar Nueva Prenda'}
            </DialogTitle>
            <button onClick={() => setIsModalOpen(false)} className="text-zinc-400 hover:text-zinc-600"><X size={20}/></button>
          </div>

          <form onSubmit={handleSave} className="p-6 space-y-8">
            {/* Sección 1: Datos Base ERP */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-zinc-300" /> Identificación Base
              </h3>
              <div className="grid grid-cols-2 gap-5">
                {editingItem && (
                  <div className="space-y-1.5 col-span-2">
                    <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Código Interno (SKU) — generado automáticamente</label>
                    <div className="h-10 flex items-center px-3 rounded-xl border border-dashed border-zinc-300 bg-zinc-50">
                      <span className="text-xs font-mono font-bold text-zinc-700">{formData.codigoArticulo}</span>
                    </div>
                  </div>
                )}
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Tipo de Prenda *</label>
                  <select
                    value={formData.tipoArticulo}
                    onChange={(e) => setFormData({...formData, tipoArticulo: e.target.value})}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    {TIPOS_PRENDA.map(t => (
                      <option key={t.value} value={t.value}>{t.label}</option>
                    ))}
                  </select>
                </div>
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Nombre de la Prenda *</label>
                  <Input required value={formData.nombreArticulo} onChange={(e) => setFormData({...formData, nombreArticulo: e.target.value.toUpperCase()})} placeholder="Ej: Polerón Corporativo Premium" className="h-10 text-xs uppercase bg-zinc-50 font-semibold" />
                </div>
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Descripción Breve</label>
                  <Input value={formData.descripcionArticulo} onChange={(e) => setFormData({...formData, descripcionArticulo: e.target.value.toUpperCase()})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>
            </div>

            <div className="h-px bg-zinc-100" />

            {/* Sección 2: Ficha de Prenda */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-purple-300" /> Ficha de Prenda
              </h3>
              <div className="grid grid-cols-2 gap-5">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Marca</label>
                  <Input value={formData.detallePrenda.marca} onChange={(e) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, marca: e.target.value.toUpperCase()}})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Tallas Disponibles</label>
                  <Input value={formData.detallePrenda.tallasDisponibles} onChange={(e) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, tallasDisponibles: e.target.value.toUpperCase()}})} placeholder="Ej: S, M, L, XL" className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Proveedor</label>
                  <Input value={formData.detallePrenda.proveedor} onChange={(e) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, proveedor: e.target.value.toUpperCase()}})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Código del Proveedor</label>
                  <Input value={formData.detallePrenda.codigoProveedor} onChange={(e) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, codigoProveedor: e.target.value.toUpperCase()}})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Ubicación del Logo</label>
                  <Input value={formData.detallePrenda.ubicacionLogo} onChange={(e) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, ubicacionLogo: e.target.value.toUpperCase()}})} placeholder="Ej: Pecho Izquierdo, Manga" className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>

              <div className="mt-5 grid grid-cols-2 gap-3">
                <div className="p-4 bg-zinc-50 rounded-xl border border-zinc-200 flex items-center justify-between">
                  <div>
                    <h4 className="text-xs font-bold text-zinc-900 uppercase">Logo del Cliente</h4>
                    <p className="text-[10px] text-zinc-500 mt-0.5 uppercase font-medium">¿Requiere logo del cliente?</p>
                  </div>
                  <Checkbox
                    checked={formData.detallePrenda.requiereLogoCliente}
                    onCheckedChange={(c) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, requiereLogoCliente: !!c}})}
                    className={formData.detallePrenda.requiereLogoCliente ? 'border-zinc-900 bg-zinc-900' : ''}
                  />
                </div>
                <div className="p-4 bg-zinc-50 rounded-xl border border-zinc-200 flex items-center justify-between">
                  <div>
                    <h4 className="text-xs font-bold text-zinc-900 uppercase">Estampado</h4>
                    <p className="text-[10px] text-zinc-500 mt-0.5 uppercase font-medium">¿Tiene estampado?</p>
                  </div>
                  <Checkbox
                    checked={formData.detallePrenda.tieneEstampado}
                    onCheckedChange={(c) => setFormData({...formData, detallePrenda: {...formData.detallePrenda, tieneEstampado: !!c}})}
                    className={formData.detallePrenda.tieneEstampado ? 'border-zinc-900 bg-zinc-900' : ''}
                  />
                </div>
              </div>
            </div>

            {/* Modal Footer */}
            <div className="sticky bottom-0 bg-white/90 backdrop-blur-sm border-t border-zinc-200 pt-4 flex gap-3 pb-2">
              <Button type="button" variant="outline" onClick={() => setIsModalOpen(false)} className="flex-1 h-12 rounded-xl text-xs font-bold uppercase tracking-widest">
                Cancelar
              </Button>
              <Button type="submit" className="flex-1 h-12 rounded-xl bg-zinc-900 hover:bg-zinc-800 text-white shadow-md text-xs font-bold uppercase tracking-widest flex items-center justify-center gap-2">
                <Save size={16} /> Guardar Prenda
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
