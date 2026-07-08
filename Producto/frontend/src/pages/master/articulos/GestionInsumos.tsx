import React, { useState, useEffect } from 'react';
import { api } from '../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { Scissors, Plus, Edit, Trash2, Save, Search, RefreshCw, X } from 'lucide-react';
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Dialog, DialogContent, DialogTitle } from "../../../ui/dialog";
import { Checkbox } from "../../../ui/checkbox";
import { confirmDelete } from '../../../utils/confirmDelete';

type AtributoValor = string | number | '';

const emptyDetalle = () => ({
  tipoAccesorio: { idTipoAccesorio: '' },
  atributosValores: {} as Record<number, AtributoValor>,
  proveedor: '',
  codigoProveedor: '',
  requiereLogoCliente: false,
});

export default function GestionInsumos() {
  const [insumos, setInsumos] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const [tiposAccesorio, setTiposAccesorio] = useState<any[]>([]);
  const [colores, setColores] = useState<any[]>([]);
  const [definiciones, setDefiniciones] = useState<any[]>([]);
  const [loadingDefiniciones, setLoadingDefiniciones] = useState(false);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);

  const [formData, setFormData] = useState<any>({
    nombreArticulo: '',
    descripcionArticulo: '',
    stockActual: 0,
    tipoArticulo: 'ACCESORIO',
    detalleAccesorio: emptyDetalle(),
  });

  useEffect(() => {
    loadInsumos();
    loadCatalogos();
  }, []);

  const loadCatalogos = async () => {
    try {
      const [resTipos, resColores] = await Promise.all([
        api.get('/maestros/tipos-accesorio'),
        api.get('/maestros/colores-tela'),
      ]);
      setTiposAccesorio(resTipos.data as any[]);
      setColores(resColores.data as any[]);
    } catch (error) {
      console.error("Error al cargar catálogos de insumos", error);
    }
  };

  const loadDefiniciones = async (idTipoAccesorio: number | string) => {
    if (!idTipoAccesorio) {
      setDefiniciones([]);
      return [];
    }
    setLoadingDefiniciones(true);
    try {
      const res = await api.get(`/maestros/atributos-accesorio-definicion/por-tipo/${idTipoAccesorio}`);
      const lista = (res.data as any[]) || [];
      setDefiniciones(lista);
      return lista;
    } catch (error) {
      console.error("Error al cargar atributos del tipo de accesorio", error);
      setDefiniciones([]);
      return [];
    } finally {
      setLoadingDefiniciones(false);
    }
  };

  const loadInsumos = async () => {
    setLoading(true);
    try {
      const res = await api.get('/maestros/articulos/tipo/ACCESORIO');
      setInsumos(res.data as any[]);
    } catch (error) {
      console.error(error);
      toast.error("Error al cargar insumos");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setDefiniciones([]);
    setFormData({
      nombreArticulo: '',
      descripcionArticulo: '',
      stockActual: 0,
      tipoArticulo: 'ACCESORIO',
      detalleAccesorio: emptyDetalle(),
    });
    setIsModalOpen(true);
  };

  const handleOpenEdit = async (item: any) => {
    setEditingItem(item);
    const detalle = item.detalleAccesorio || {};
    const idTipo = detalle.tipoAccesorio?.idTipoAccesorio || '';

    const defs = await loadDefiniciones(idTipo);
    const atributosValores: Record<number, AtributoValor> = {};
    (detalle.atributos || []).forEach((v: any) => {
      const def = defs.find((d: any) => d.idDefinicion === v.idDefinicion);
      atributosValores[v.idDefinicion] = def?.tipoDato === 'REFERENCIA_COLOR'
        ? (v.idColorReferencia ?? '')
        : (v.valorTexto ?? '');
    });

    setFormData({
      ...item,
      detalleAccesorio: {
        tipoAccesorio: { idTipoAccesorio: idTipo },
        atributosValores,
        proveedor: detalle.proveedor || '',
        codigoProveedor: detalle.codigoProveedor || '',
        requiereLogoCliente: detalle.requiereLogoCliente || false,
      },
    });
    setIsModalOpen(true);
  };

  const handleTipoAccesorioChange = async (idTipoAccesorio: string) => {
    const defs = await loadDefiniciones(idTipoAccesorio);
    const atributosValores: Record<number, AtributoValor> = {};
    defs.forEach((d: any) => { atributosValores[d.idDefinicion] = ''; });
    setFormData({
      ...formData,
      detalleAccesorio: {
        ...formData.detalleAccesorio,
        tipoAccesorio: { idTipoAccesorio },
        atributosValores,
      },
    });
  };

  const setAtributoValor = (idDefinicion: number, valor: AtributoValor) => {
    setFormData({
      ...formData,
      detalleAccesorio: {
        ...formData.detalleAccesorio,
        atributosValores: { ...formData.detalleAccesorio.atributosValores, [idDefinicion]: valor },
      },
    });
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.nombreArticulo) {
      return toast.error("El nombre del insumo es obligatorio");
    }
    if (!formData.detalleAccesorio.tipoAccesorio.idTipoAccesorio) {
      return toast.error("Selecciona el tipo de insumo (Cierre, Botón, etc.)");
    }
    const faltante = definiciones.find(d => d.requerido && !formData.detalleAccesorio.atributosValores[d.idDefinicion]);
    if (faltante) {
      return toast.error(`El campo "${faltante.nombreCampo}" es requerido`);
    }

    const atributos = definiciones
      .map(d => {
        const valor = formData.detalleAccesorio.atributosValores[d.idDefinicion];
        if (valor === '' || valor === null || valor === undefined) return null;
        return d.tipoDato === 'REFERENCIA_COLOR'
          ? { idDefinicion: d.idDefinicion, idColorReferencia: Number(valor) }
          : { idDefinicion: d.idDefinicion, valorTexto: String(valor) };
      })
      .filter(Boolean);

    const payload = {
      nombreArticulo: formData.nombreArticulo,
      descripcionArticulo: formData.descripcionArticulo,
      stockActual: Number(formData.stockActual || 0),
      tipoArticulo: 'ACCESORIO',
      detalleAccesorio: {
        tipoAccesorio: { idTipoAccesorio: Number(formData.detalleAccesorio.tipoAccesorio.idTipoAccesorio) },
        atributos,
        proveedor: formData.detalleAccesorio.proveedor,
        codigoProveedor: formData.detalleAccesorio.codigoProveedor,
        requiereLogoCliente: formData.detalleAccesorio.requiereLogoCliente,
      },
    };

    try {
      if (editingItem) {
        await api.put(`/maestros/articulos/${editingItem.idArticulo}`, payload);
        toast.success("Insumo actualizado correctamente");
      } else {
        await api.post('/maestros/articulos', payload);
        toast.success("Insumo creado correctamente. El código se generó automáticamente.");
      }
      setIsModalOpen(false);
      loadInsumos();
    } catch (error) {
      console.error(error);
      toast.error("Ocurrió un error al guardar el Insumo");
    }
  };

  const handleDelete = (item: any) => {
    confirmDelete(`¿Eliminar de forma permanente el insumo "${item.nombreArticulo}"?`, async () => {
      try {
        await api.delete(`/maestros/articulos/${item.idArticulo}`);
        toast.success("Insumo eliminado exitosamente");
        loadInsumos();
      } catch (error) {
        console.error(error);
        toast.error("Error al eliminar");
      }
    });
  };

  const filteredData = insumos.filter(item =>
    item.nombreArticulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.codigoArticulo.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="flex flex-col h-full bg-white rounded-2xl border border-zinc-200 overflow-hidden shadow-sm">
      <Toaster position="top-right" richColors />

      {/* Header */}
      <div className="px-6 py-5 border-b border-zinc-200 flex justify-between items-center bg-zinc-50/50">
        <div>
          <h2 className="text-lg font-bold text-zinc-900 tracking-tight uppercase">Insumos y Accesorios</h2>
          <p className="text-xs font-medium text-zinc-500 mt-0.5">Gestión de cierres, botones, cintas y complementos</p>
        </div>
        <Button onClick={handleOpenNew} className="bg-zinc-900 text-white rounded-xl font-bold uppercase text-[11px] tracking-widest px-6 h-11 hover:bg-zinc-800 shadow-sm flex items-center gap-2">
          <Plus size={14} /> Nuevo Insumo
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
        <Button variant="outline" size="icon" onClick={loadInsumos} className="h-10 w-10 rounded-xl border-zinc-200 text-zinc-500 hover:text-zinc-900">
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
            <Scissors size={32} className="mx-auto text-zinc-300 mb-3" />
            <h3 className="text-sm font-bold text-zinc-900 uppercase">Sin Insumos Registrados</h3>
            <p className="text-xs text-zinc-500 mt-1">Comienza agregando un cierre, botón o cinta al catálogo.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {filteredData.map(item => (
              <div key={item.idArticulo} className="flex items-center justify-between p-4 bg-white border border-zinc-200 rounded-xl hover:border-zinc-300 transition-colors shadow-sm">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-zinc-100 rounded-lg flex items-center justify-center text-zinc-400 border border-zinc-200">
                    <Scissors size={20} strokeWidth={1.5} />
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-zinc-900 uppercase">{item.nombreArticulo}</h4>
                    <div className="flex items-center gap-2 mt-1 flex-wrap">
                      <span className="text-[10px] font-bold px-2 py-0.5 bg-zinc-100 text-zinc-600 rounded-md border border-zinc-200">
                        SKU: {item.codigoArticulo}
                      </span>
                      {item.detalleAccesorio?.tipoAccesorio?.nombre && (
                        <span className="text-[10px] font-bold px-2 py-0.5 bg-indigo-50 text-indigo-600 rounded-md border border-indigo-100 uppercase">
                          {item.detalleAccesorio.tipoAccesorio.nombre}
                        </span>
                      )}
                      {(item.detalleAccesorio?.atributos || []).map((a: any) => (
                        <span key={a.idValor} className="text-[10px] font-bold px-2 py-0.5 bg-zinc-100 text-zinc-600 rounded-md border border-zinc-200 uppercase">
                          {a.nombreCampo}: {a.colorDescripcion || a.valorTexto}
                        </span>
                      ))}
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

      {/* Modal CRUD Insumos */}
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto p-0 rounded-2xl border-zinc-200 bg-white shadow-2xl">
          <div className="sticky top-0 z-10 bg-white/80 backdrop-blur-md border-b border-zinc-200 px-6 py-4 flex justify-between items-center">
            <DialogTitle className="text-lg font-extrabold tracking-tight text-zinc-900 uppercase">
              {editingItem ? 'Editar Insumo' : 'Registrar Nuevo Insumo'}
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
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Nombre del Insumo *</label>
                  <Input required value={formData.nombreArticulo} onChange={(e) => setFormData({...formData, nombreArticulo: e.target.value.toUpperCase()})} placeholder="Ej: Cierre Fijo Nylon 15cm" className="h-10 text-xs uppercase bg-zinc-50 font-semibold" />
                </div>
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Descripción Breve</label>
                  <Input value={formData.descripcionArticulo} onChange={(e) => setFormData({...formData, descripcionArticulo: e.target.value.toUpperCase()})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>
            </div>

            <div className="h-px bg-zinc-100" />

            {/* Sección 2: Tipo de Insumo + Atributos Dinámicos */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-indigo-300" /> Configuraciones del Insumo
              </h3>
              <div className="grid grid-cols-2 gap-5">
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Tipo de Insumo *</label>
                  <select
                    value={formData.detalleAccesorio.tipoAccesorio.idTipoAccesorio}
                    onChange={(e) => handleTipoAccesorioChange(e.target.value)}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    <option value="">Seleccione Tipo de Insumo...</option>
                    {tiposAccesorio.map(t => (
                      <option key={t.idTipoAccesorio} value={t.idTipoAccesorio}>{t.nombre}</option>
                    ))}
                  </select>
                </div>

                {loadingDefiniciones ? (
                  <div className="col-span-2 flex items-center justify-center py-6 text-zinc-400">
                    <RefreshCw size={18} className="animate-spin mr-2" /> Cargando atributos del insumo...
                  </div>
                ) : (
                  definiciones.map(def => {
                    const valor = formData.detalleAccesorio.atributosValores[def.idDefinicion] ?? '';
                    return (
                      <div key={def.idDefinicion} className="space-y-1.5">
                        <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">
                          {def.nombreCampo}{def.requerido ? ' *' : ''}
                        </label>
                        {def.tipoDato === 'LISTA' ? (
                          <select
                            value={valor}
                            onChange={(e) => setAtributoValor(def.idDefinicion, e.target.value)}
                            className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                          >
                            <option value="">Seleccione...</option>
                            {(def.opciones || []).map((o: string) => (
                              <option key={o} value={o}>{o}</option>
                            ))}
                          </select>
                        ) : def.tipoDato === 'REFERENCIA_COLOR' ? (
                          <select
                            value={valor}
                            onChange={(e) => setAtributoValor(def.idDefinicion, e.target.value)}
                            className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                          >
                            <option value="">Seleccione Color...</option>
                            {colores.map(c => (
                              <option key={c.idColor} value={c.idColor}>{c.descripcionColor}</option>
                            ))}
                          </select>
                        ) : def.tipoDato === 'NUMERO' ? (
                          <Input
                            type="number"
                            value={valor}
                            onChange={(e) => setAtributoValor(def.idDefinicion, e.target.value)}
                            className="h-10 text-xs bg-zinc-50 font-medium"
                          />
                        ) : (
                          <Input
                            value={valor}
                            onChange={(e) => setAtributoValor(def.idDefinicion, e.target.value.toUpperCase())}
                            className="h-10 text-xs uppercase bg-zinc-50 font-medium"
                          />
                        )}
                      </div>
                    );
                  })
                )}

                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Proveedor Recomendado</label>
                  <Input value={formData.detalleAccesorio.proveedor} onChange={(e) => setFormData({...formData, detalleAccesorio: {...formData.detalleAccesorio, proveedor: e.target.value.toUpperCase()}})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Código del Proveedor</label>
                  <Input value={formData.detalleAccesorio.codigoProveedor} onChange={(e) => setFormData({...formData, detalleAccesorio: {...formData.detalleAccesorio, codigoProveedor: e.target.value.toUpperCase()}})} className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>

              <div className="mt-5 p-4 bg-zinc-50 rounded-xl border border-zinc-200 flex items-center justify-between">
                <div>
                  <h4 className="text-xs font-bold text-zinc-900 uppercase">Exclusividad de Marca</h4>
                  <p className="text-[10px] text-zinc-500 mt-0.5 uppercase font-medium">¿Este insumo lleva el logotipo del cliente grabado?</p>
                </div>
                <Checkbox
                  checked={formData.detalleAccesorio.requiereLogoCliente}
                  onCheckedChange={(c) => setFormData({...formData, detalleAccesorio: {...formData.detalleAccesorio, requiereLogoCliente: !!c}})}
                  className={formData.detalleAccesorio.requiereLogoCliente ? 'border-zinc-900 bg-zinc-900' : ''}
                />
              </div>
            </div>

            {/* Modal Footer */}
            <div className="sticky bottom-0 bg-white/90 backdrop-blur-sm border-t border-zinc-200 pt-4 flex gap-3 pb-2">
              <Button type="button" variant="outline" onClick={() => setIsModalOpen(false)} className="flex-1 h-12 rounded-xl text-xs font-bold uppercase tracking-widest">
                Cancelar
              </Button>
              <Button type="submit" className="flex-1 h-12 rounded-xl bg-zinc-900 hover:bg-zinc-800 text-white shadow-md text-xs font-bold uppercase tracking-widest flex items-center justify-center gap-2">
                <Save size={16} /> Guardar Insumo
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
