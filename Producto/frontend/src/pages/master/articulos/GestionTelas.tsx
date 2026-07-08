import React, { useState, useEffect } from 'react';
import { api } from '../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { Layers, Plus, Edit, Trash2, Save, Search, RefreshCw, X } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from "../../../ui/card";
import { Input } from "../../../ui/input";
import { Button } from "../../../ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../../../ui/dialog";
import { Checkbox } from "../../../ui/checkbox";
import { confirmDelete } from '../../../utils/confirmDelete';

export default function GestionTelas() {
  const [telas, setTelas] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  // Parametría States
  const [familias, setFamilias] = useState<any[]>([]);
  const [composiciones, setComposiciones] = useState<any[]>([]);
  const [gramajes, setGramajes] = useState<any[]>([]);
  const [atributos, setAtributos] = useState<any[]>([]);
  const [colores, setColores] = useState<any[]>([]);
  const [categorias, setCategorias] = useState<any[]>([]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);

  // Base Article fields + Tela fields
  const [formData, setFormData] = useState<any>({
    nombreArticulo: '',
    descripcionArticulo: '',
    stockActual: 0,
    tipoArticulo: 'TELA',
    idCategoriaTela: '',

    // Detalle Tela
    detalleTela: {
      familiaTela: { idFamiliaTela: '' },
      composicion: { idComposicion: '' },
      gramaje: { idGramaje: '' },
      abreviaturasHistoricas: '',
      usoTipico: '',
      atributosTecnicos: [], // Array of IDs
      colores: [] // Array of IDs
    }
  });

  useEffect(() => {
    loadParametria();
    loadTelas();
  }, []);

  const loadParametria = async () => {
    try {
      const [resF, resC, resG, resA, resCol, resCat] = await Promise.all([
        api.get('/maestros/familias-tela'),
        api.get('/maestros/composiciones'),
        api.get('/maestros/gramajes-tela'),
        api.get('/maestros/atributos-tecnicos'),
        api.get('/maestros/colores-tela'),
        api.get('/maestros/categorias-tela')
      ]);
      setFamilias(resF.data as any[]);
      setComposiciones(resC.data as any[]);
      setGramajes(resG.data as any[]);
      setAtributos(resA.data as any[]);
      setColores(resCol.data as any[]);
      setCategorias(resCat.data as any[]);
    } catch (error) {
      console.error("Error al cargar parametría", error);
    }
  };

  const loadTelas = async () => {
    setLoading(true);
    try {
      // Assuming articles endpoint supports filtering by type
      const res = await api.get('/maestros/articulos/tipo/TELA');
      setTelas(res.data as any[]);
    } catch (error) {
      console.error(error);
      toast.error("Error al cargar telas");
    } finally {
      setLoading(false);
    }
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setFormData({
      nombreArticulo: '',
      descripcionArticulo: '',
      stockActual: 0,
      tipoArticulo: 'TELA',
      idCategoriaTela: '',
      detalleTela: {
        familiaTela: { idFamiliaTela: '' },
        composicion: { idComposicion: '' },
        gramaje: { idGramaje: '' },
        abreviaturasHistoricas: '',
        usoTipico: '',
        atributosTecnicos: [],
        colores: []
      }
    });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (item: any) => {
    setEditingItem(item);

    // Transform arrays to flat IDs for the form
    const detail = item.detalleTela || {};
    const attrIds = (detail.atributosTecnicos || []).map((a: any) => a.idAtributo);
    const colorIds = (detail.colores || []).map((c: any) => c.idColor);

    setFormData({
      ...item,
      idCategoriaTela: item.idCategoriaTela ?? '',
      detalleTela: {
        familiaTela: detail.familiaTela || { idFamiliaTela: '' },
        composicion: detail.composicion || { idComposicion: '' },
        gramaje: detail.gramaje || { idGramaje: '' },
        abreviaturasHistoricas: detail.abreviaturasHistoricas || '',
        usoTipico: detail.usoTipico || '',
        atributosTecnicos: attrIds,
        colores: colorIds
      }
    });
    setIsModalOpen(true);
  };

  const toggleColor = (id: number) => {
    const current = formData.detalleTela.colores;
    const isSelected = current.includes(id);
    setFormData({
      ...formData,
      detalleTela: {
        ...formData.detalleTela,
        colores: isSelected ? current.filter((x: number) => x !== id) : [...current, id]
      }
    });
  };

  const toggleAtributo = (id: number) => {
    const current = formData.detalleTela.atributosTecnicos;
    const isSelected = current.includes(id);
    setFormData({
      ...formData,
      detalleTela: {
        ...formData.detalleTela,
        atributosTecnicos: isSelected ? current.filter((x: number) => x !== id) : [...current, id]
      }
    });
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.nombreArticulo) {
      return toast.error("El nombre comercial es obligatorio");
    }
    if (!formData.idCategoriaTela) {
      return toast.error("La categoría de tela es obligatoria");
    }

    // Prepare payload exactly as DTO expects. El código (SKU) lo genera el backend solo.
    const payload = {
      ...formData,
      idCategoriaTela: Number(formData.idCategoriaTela),
      detalleTela: {
        ...formData.detalleTela,
        familiaTela: formData.detalleTela.familiaTela.idFamiliaTela ? { idFamiliaTela: Number(formData.detalleTela.familiaTela.idFamiliaTela) } : null,
        composicion: formData.detalleTela.composicion.idComposicion ? { idComposicion: Number(formData.detalleTela.composicion.idComposicion) } : null,
        gramaje: formData.detalleTela.gramaje.idGramaje ? { idGramaje: Number(formData.detalleTela.gramaje.idGramaje) } : null,
        atributosTecnicos: formData.detalleTela.atributosTecnicos.map((id: number) => ({ idAtributo: id })),
        colores: formData.detalleTela.colores.map((id: number) => ({ idColor: id }))
      }
    };

    try {
      if (editingItem) {
        await api.put(`/maestros/articulos/${editingItem.idArticulo}`, payload);
        toast.success("Tela actualizada correctamente");
      } else {
        await api.post('/maestros/articulos', payload);
        toast.success("Tela creada correctamente");
      }
      setIsModalOpen(false);
      loadTelas();
    } catch (error) {
      console.error(error);
      toast.error("Ocurrió un error al guardar la Tela");
    }
  };

  const handleDelete = (item: any) => {
    confirmDelete(`¿Eliminar de forma permanente la tela "${item.nombreArticulo}"?`, async () => {
      try {
        await api.delete(`/maestros/articulos/${item.idArticulo}`);
        toast.success("Tela eliminada exitosamente");
        loadTelas();
      } catch (error) {
        console.error(error);
        toast.error("Error al eliminar");
      }
    });
  };

  const filteredData = telas.filter(item => 
    item.nombreArticulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.codigoArticulo.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="flex flex-col h-full bg-white rounded-2xl border border-zinc-200 overflow-hidden shadow-sm">
      <Toaster position="top-right" richColors />
      
      {/* Header */}
      <div className="px-6 py-5 border-b border-zinc-200 flex justify-between items-center bg-zinc-50/50">
        <div>
          <h2 className="text-lg font-bold text-zinc-900 tracking-tight uppercase">Catálogo de Telas</h2>
          <p className="text-xs font-medium text-zinc-500 mt-0.5">Gestión de tejidos, composiciones y colores</p>
        </div>
        <Button onClick={handleOpenNew} className="bg-zinc-900 text-white rounded-xl font-bold uppercase text-[11px] tracking-widest px-6 h-11 hover:bg-zinc-800 shadow-sm flex items-center gap-2">
          <Plus size={14} /> Nueva Tela
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
        <Button variant="outline" size="icon" onClick={loadTelas} className="h-10 w-10 rounded-xl border-zinc-200 text-zinc-500 hover:text-zinc-900">
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
            <Layers size={32} className="mx-auto text-zinc-300 mb-3" />
            <h3 className="text-sm font-bold text-zinc-900 uppercase">Sin Telas Registradas</h3>
            <p className="text-xs text-zinc-500 mt-1">Comienza agregando una nueva tela al catálogo.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {filteredData.map(item => (
              <div key={item.idArticulo} className="flex items-center justify-between p-4 bg-white border border-zinc-200 rounded-xl hover:border-zinc-300 transition-colors shadow-sm">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-zinc-100 rounded-lg flex items-center justify-center text-zinc-400 border border-zinc-200">
                    <Layers size={20} strokeWidth={1.5} />
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-zinc-900 uppercase">{item.nombreArticulo}</h4>
                    <div className="flex items-center gap-2 mt-1">
                      <span className="text-[10px] font-bold px-2 py-0.5 bg-zinc-100 text-zinc-600 rounded-md border border-zinc-200">
                        SKU: {item.codigoArticulo}
                      </span>
                      {item.detalleTela?.familiaTela?.nombreFamilia && (
                        <span className="text-[10px] font-bold px-2 py-0.5 bg-blue-50 text-blue-600 rounded-md border border-blue-100 uppercase">
                          {item.detalleTela.familiaTela.nombreFamilia}
                        </span>
                      )}
                      {item.detalleTela?.composicion?.descripcionComposicion && (
                        <span className="text-[10px] font-bold px-2 py-0.5 bg-emerald-50 text-emerald-600 rounded-md border border-emerald-100 uppercase">
                          {item.detalleTela.composicion.descripcionComposicion}
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

      {/* Modal CRUD Telas */}
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto p-0 rounded-2xl border-zinc-200 bg-white shadow-2xl">
          <div className="sticky top-0 z-10 bg-white/80 backdrop-blur-md border-b border-zinc-200 px-6 py-4 flex justify-between items-center">
            <DialogTitle className="text-lg font-extrabold tracking-tight text-zinc-900 uppercase">
              {editingItem ? 'Editar Tela' : 'Registrar Nueva Tela'}
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
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Nombre Comercial *</label>
                  <Input required value={formData.nombreArticulo} onChange={(e) => setFormData({...formData, nombreArticulo: e.target.value.toUpperCase()})} className="h-10 text-xs uppercase bg-zinc-50 font-semibold" />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Categoría de Tela *</label>
                  <select
                    required
                    value={formData.idCategoriaTela}
                    onChange={(e) => setFormData({...formData, idCategoriaTela: e.target.value})}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    <option value="">Seleccione Categoría...</option>
                    {categorias.map(c => (
                      <option key={c.idCategoriaTela} value={c.idCategoriaTela}>{c.nombreCategoriaTela}</option>
                    ))}
                  </select>
                </div>
                <div className="space-y-1.5 col-span-2">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Abreviaturas Históricas</label>
                  <Input value={formData.detalleTela.abreviaturasHistoricas} onChange={(e) => setFormData({...formData, detalleTela: {...formData.detalleTela, abreviaturasHistoricas: e.target.value.toUpperCase()}})} placeholder="Ej: GAB, GA, SARGA" className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>
            </div>

            <div className="h-px bg-zinc-100" />

            {/* Sección 2: Parametría Técnica */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-blue-300" /> Ficha Técnica
              </h3>
              <div className="grid grid-cols-2 gap-5">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Familia de Tela</label>
                  <select 
                    value={formData.detalleTela.familiaTela.idFamiliaTela} 
                    onChange={(e) => setFormData({...formData, detalleTela: {...formData.detalleTela, familiaTela: {idFamiliaTela: e.target.value}}})}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    <option value="">Seleccione Familia...</option>
                    {familias.map(f => (
                      <option key={f.idFamiliaTela} value={f.idFamiliaTela}>{f.nombreFamilia}</option>
                    ))}
                  </select>
                </div>
                
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Composición</label>
                  <select 
                    value={formData.detalleTela.composicion.idComposicion} 
                    onChange={(e) => setFormData({...formData, detalleTela: {...formData.detalleTela, composicion: {idComposicion: e.target.value}}})}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    <option value="">Seleccione Composición...</option>
                    {composiciones.map(c => (
                      <option key={c.idComposicion} value={c.idComposicion}>{c.descripcionComposicion}</option>
                    ))}
                  </select>
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Gramaje / Peso</label>
                  <select 
                    value={formData.detalleTela.gramaje.idGramaje} 
                    onChange={(e) => setFormData({...formData, detalleTela: {...formData.detalleTela, gramaje: {idGramaje: e.target.value}}})}
                    className="flex h-10 w-full rounded-xl border border-zinc-200 bg-zinc-50 px-3 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900"
                  >
                    <option value="">Seleccione Gramaje...</option>
                    {gramajes.map(g => (
                      <option key={g.idGramaje} value={g.idGramaje}>{g.codigoGramaje}</option>
                    ))}
                  </select>
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold text-zinc-500 uppercase tracking-wider">Uso Típico Industrial</label>
                  <Input value={formData.detalleTela.usoTipico} onChange={(e) => setFormData({...formData, detalleTela: {...formData.detalleTela, usoTipico: e.target.value.toUpperCase()}})} placeholder="Ej: Ropa Clínica, Ignífugo Base" className="h-10 text-xs uppercase bg-zinc-50 font-medium" />
                </div>
              </div>
            </div>

            <div className="h-px bg-zinc-100" />

            {/* Sección 3: Atributos y Tratamientos */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-amber-300" /> Tratamientos / Atributos Especiales
              </h3>
              <div className="grid grid-cols-2 gap-3 bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                {atributos.map(attr => {
                  const isChecked = formData.detalleTela.atributosTecnicos.includes(attr.idAtributo);
                  return (
                    <label key={attr.idAtributo} className={`flex items-start gap-3 p-3 rounded-lg border cursor-pointer transition-colors ${isChecked ? 'bg-white border-zinc-300 shadow-sm' : 'border-transparent hover:bg-zinc-100'}`}>
                      <div className="mt-0.5">
                        <Checkbox checked={isChecked} onCheckedChange={() => toggleAtributo(attr.idAtributo)} className={isChecked ? 'border-zinc-900 bg-zinc-900' : ''} />
                      </div>
                      <div className="flex-1">
                        <div className="text-[11px] font-bold text-zinc-900 uppercase">{attr.descripcionTecnica}</div>
                        <div className="text-[9px] text-zinc-400 uppercase font-medium mt-0.5">{attr.codigoAtributo}</div>
                      </div>
                    </label>
                  );
                })}
              </div>
            </div>

            <div className="h-px bg-zinc-100" />

            {/* Sección 4: Colores Disponibles */}
            <div>
              <h3 className="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4 flex items-center gap-2">
                <div className="w-1.5 h-1.5 rounded-full bg-pink-300" /> Colores Disponibles
              </h3>
              <div className="grid grid-cols-3 gap-3 bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                {colores.map(color => {
                  const isChecked = formData.detalleTela.colores.includes(color.idColor);
                  return (
                    <label key={color.idColor} className={`flex items-start gap-3 p-3 rounded-lg border cursor-pointer transition-colors ${isChecked ? 'bg-white border-zinc-300 shadow-sm' : 'border-transparent hover:bg-zinc-100'}`}>
                      <div className="mt-0.5">
                        <Checkbox checked={isChecked} onCheckedChange={() => toggleColor(color.idColor)} className={isChecked ? 'border-zinc-900 bg-zinc-900' : ''} />
                      </div>
                      <div className="flex-1">
                        <div className="text-[11px] font-bold text-zinc-900 uppercase">{color.descripcionColor}</div>
                        <div className="text-[9px] text-zinc-400 uppercase font-medium mt-0.5">{color.codigoColor}</div>
                      </div>
                    </label>
                  );
                })}
              </div>
            </div>

            {/* Modal Footer */}
            <div className="sticky bottom-0 bg-white/90 backdrop-blur-sm border-t border-zinc-200 pt-4 flex gap-3 pb-2">
              <Button type="button" variant="outline" onClick={() => setIsModalOpen(false)} className="flex-1 h-12 rounded-xl text-xs font-bold uppercase tracking-widest">
                Cancelar
              </Button>
              <Button type="submit" className="flex-1 h-12 rounded-xl bg-zinc-900 hover:bg-zinc-800 text-white shadow-md text-xs font-bold uppercase tracking-widest flex items-center justify-center gap-2">
                <Save size={16} /> Guardar Tela
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
