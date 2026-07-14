import React, { useState, useEffect, useRef } from 'react';
import { api } from '../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { Layers, Plus, Edit, Trash2, Save, Search, RefreshCw, X, Eye, Palette, Check } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from "../../../ui/card";
import { Input } from "../../../ui/input";
import { Button } from "../../../ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../../../ui/dialog";
import { Checkbox } from "../../../ui/checkbox";
import { confirmDelete } from '../../../utils/confirmDelete';

/**
 * Combobox multi-selección de colores: lista ordenada alfabéticamente,
 * filtrado por búsqueda y opción de crear un color nuevo cuando no existe.
 */
function ColorMultiCombobox({
  options,
  selectedIds,
  onChange,
  onCreate,
}: {
  options: any[];
  selectedIds: number[];
  onChange: (ids: number[]) => void;
  onCreate: (nombre: string) => Promise<any>;
}) {
  const [query, setQuery] = useState('');
  const [open, setOpen] = useState(false);
  const [creating, setCreating] = useState(false);
  const wrapRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!open) return;
    const handler = (e: MouseEvent) => {
      if (wrapRef.current && !wrapRef.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, [open]);

  const sorted = [...options].sort((a, b) =>
    (a.descripcionColor || '').localeCompare(b.descripcionColor || '')
  );
  const term = query.trim().toLowerCase();
  const filtered = sorted.filter(c => (c.descripcionColor || '').toLowerCase().includes(term));
  const exactMatch = sorted.some(c => (c.descripcionColor || '').toLowerCase() === term);
  const canCreate = term.length > 0 && !exactMatch;
  const selected = options.filter(c => selectedIds.includes(c.idColor));

  const toggle = (id: number) => {
    onChange(selectedIds.includes(id) ? selectedIds.filter(x => x !== id) : [...selectedIds, id]);
  };

  const handleCreate = async () => {
    const nombre = query.trim();
    if (!nombre || creating) return;
    setCreating(true);
    try {
      const created = await onCreate(nombre);
      if (created?.idColor) {
        onChange([...selectedIds, created.idColor]);
        setQuery('');
      }
    } finally {
      setCreating(false);
    }
  };

  return (
    <div ref={wrapRef} className="relative w-full">
      <div className="flex flex-wrap gap-2 mb-2 min-h-[1.75rem]">
        {selected.length === 0 && (
          <span className="text-xs text-zinc-400 italic">Ningún color seleccionado</span>
        )}
        {selected.map(c => (
          <span key={c.idColor} className="flex items-center gap-1.5 pl-2.5 pr-1.5 py-1 bg-zinc-900 text-white text-[11px] font-bold rounded-lg uppercase">
            {c.descripcionColor}
            <button type="button" onClick={() => toggle(c.idColor)} className="hover:text-zinc-300 rounded-full">
              <X size={11} />
            </button>
          </span>
        ))}
      </div>

      <div className="relative">
        <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-400" size={14} />
        <input
          type="text"
          value={query}
          onChange={(e) => { setQuery(e.target.value); setOpen(true); }}
          onFocus={() => setOpen(true)}
          placeholder="Buscar un color o escribir uno nuevo..."
          className="w-full h-10 pl-9 pr-3 rounded-xl border border-zinc-200 bg-zinc-50 text-xs font-semibold uppercase focus:ring-2 focus:ring-zinc-900 outline-none"
        />
      </div>

      {open && (
        <div className="absolute z-20 mt-1 w-full bg-white border border-zinc-200 rounded-xl shadow-xl overflow-hidden">
          <div className="max-h-52 overflow-y-auto divide-y divide-zinc-100">
            {filtered.length > 0 ? (
              filtered.map(c => {
                const isChecked = selectedIds.includes(c.idColor);
                return (
                  <div
                    role="option"
                    aria-selected={isChecked}
                    key={c.idColor}
                    onMouseDown={(e) => { e.preventDefault(); toggle(c.idColor); }}
                    className={`w-full flex items-center gap-3 px-3.5 py-2.5 text-left text-xs font-semibold transition-colors cursor-pointer ${
                      isChecked ? 'bg-zinc-100 text-zinc-900' : 'text-zinc-600 hover:bg-zinc-50'
                    }`}
                  >
                    <div className={`w-4 h-4 shrink-0 rounded border flex items-center justify-center ${isChecked ? 'bg-zinc-900 border-zinc-900' : 'border-zinc-300'}`}>
                      {isChecked && <Check size={10} className="text-white" strokeWidth={3} />}
                    </div>
                    <span className="flex-1 uppercase">{c.descripcionColor}</span>
                    <span className="text-[10px] text-zinc-400">{c.codigoColor}</span>
                  </div>
                );
              })
            ) : (
              <p className="text-center text-xs text-zinc-400 py-4">Sin colores coincidentes</p>
            )}
          </div>

          {canCreate && (
            <button
              type="button"
              disabled={creating}
              onMouseDown={(e) => { e.preventDefault(); handleCreate(); }}
              className="w-full flex items-center gap-2 px-3.5 py-3 bg-zinc-900 text-white text-left transition-colors hover:bg-zinc-800 disabled:opacity-60"
            >
              <Plus size={14} />
              <span className="text-xs font-bold uppercase">
                {creating ? 'Creando...' : `Agregar nuevo color "${query.trim()}"`}
              </span>
            </button>
          )}
        </div>
      )}
    </div>
  );
}

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
  const [isDetailsModalOpen, setIsDetailsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);
  const [viewingItem, setViewingItem] = useState<any>(null);

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
      const res = await api.get('/maestros/articulos/tipo/TELA');
      setTelas((res.data as any[]).filter((t: any) => t.activo !== false));
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
      idCategoriaTela: 1,
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

  const handleOpenDetails = (item: any) => {
    setViewingItem(item);
    setIsDetailsModalOpen(true);
  };

  const handleCreateColor = async (nombre: string) => {
    try {
      const res = await api.post('/maestros/colores-tela', { descripcionColor: nombre.toUpperCase() });
      const created = res.data as any;
      setColores(prev => [...prev, created]);
      toast.success(`Color "${created.descripcionColor}" agregado al catálogo`);
      return created;
    } catch (error) {
      console.error(error);
      toast.error('No se pudo crear el color');
      return null;
    }
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

  const filteredData = telas.filter(item => {
    const term = searchTerm.toLowerCase();
    return item.nombreArticulo.toLowerCase().includes(term) ||
           item.codigoArticulo.toLowerCase().includes(term) ||
           (item.detalleTela?.familiaTela?.nombreFamilia || '').toLowerCase().includes(term) ||
           (item.detalleTela?.composicion?.descripcionComposicion || '').toLowerCase().includes(term);
  });

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
            placeholder="Buscar código, nombre, familia o comp..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10 h-10 rounded-xl bg-zinc-50 border-zinc-200 text-xs font-medium uppercase w-full"
          />
        </div>
        <Button variant="outline" size="icon" onClick={loadTelas} className="h-10 w-10 shrink-0 rounded-xl border-zinc-200 text-zinc-500 hover:text-zinc-900">
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
                  <Button variant="ghost" size="icon" onClick={() => handleOpenDetails(item)} className="h-8 w-8 text-blue-500 hover:bg-blue-50 rounded-lg">
                    <Eye size={14} />
                  </Button>
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
        <DialogContent aria-describedby={undefined} className="max-w-3xl max-h-[90vh] overflow-y-auto p-0 rounded-2xl border-zinc-200 bg-white shadow-2xl">
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
                <div className="w-1.5 h-1.5 rounded-full bg-pink-300" /> Colores Disponibles para esta Tela
              </h3>
              <ColorMultiCombobox
                options={colores}
                selectedIds={formData.detalleTela.colores}
                onChange={(ids) => setFormData({...formData, detalleTela: {...formData.detalleTela, colores: ids}})}
                onCreate={handleCreateColor}
              />
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

      {/* Modal DETALLES Tela */}
      <Dialog open={isDetailsModalOpen} onOpenChange={setIsDetailsModalOpen}>
        <DialogContent aria-describedby={undefined} className="max-w-2xl max-h-[85vh] overflow-y-auto p-0 rounded-2xl border-zinc-200 bg-white shadow-2xl">
          {viewingItem && (
            <>
              <div className="sticky top-0 z-10 bg-white/80 backdrop-blur-md border-b border-zinc-200 px-6 py-5 flex justify-between items-center">
                <div>
                  <DialogTitle className="text-xl font-extrabold tracking-tight text-zinc-900 uppercase">
                    Ficha Técnica: {viewingItem.nombreArticulo}
                  </DialogTitle>
                  <p className="text-xs font-bold text-zinc-400 mt-1 uppercase">SKU: {viewingItem.codigoArticulo}</p>
                </div>
                <button onClick={() => setIsDetailsModalOpen(false)} className="text-zinc-400 hover:text-zinc-600 bg-zinc-100 p-2 rounded-full"><X size={18}/></button>
              </div>

              <div className="p-6 space-y-6">
                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                    <p className="text-[10px] text-zinc-400 font-bold uppercase tracking-wider mb-1">Familia</p>
                    <p className="text-sm font-bold text-zinc-900 uppercase">{viewingItem.detalleTela?.familiaTela?.nombreFamilia || 'N/A'}</p>
                  </div>
                  <div className="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                    <p className="text-[10px] text-zinc-400 font-bold uppercase tracking-wider mb-1">Composición</p>
                    <p className="text-sm font-bold text-zinc-900 uppercase">{viewingItem.detalleTela?.composicion?.descripcionComposicion || 'N/A'}</p>
                  </div>
                  <div className="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                    <p className="text-[10px] text-zinc-400 font-bold uppercase tracking-wider mb-1">Gramaje / Peso</p>
                    <p className="text-sm font-bold text-zinc-900 uppercase">{viewingItem.detalleTela?.gramaje?.codigoGramaje || 'N/A'}</p>
                  </div>
                  <div className="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                    <p className="text-[10px] text-zinc-400 font-bold uppercase tracking-wider mb-1">Uso Típico Industrial</p>
                    <p className="text-sm font-bold text-zinc-900 uppercase">{viewingItem.detalleTela?.usoTipico || 'N/A'}</p>
                  </div>
                </div>

                <div>
                  <h4 className="text-xs font-bold text-zinc-400 uppercase tracking-widest flex items-center gap-2 mb-3">
                    <Palette size={14} className="text-pink-400"/> Colores Disponibles
                  </h4>
                  <div className="flex flex-wrap gap-2">
                    {viewingItem.detalleTela?.colores?.length > 0 ? (
                      viewingItem.detalleTela.colores.map((c: any) => (
                        <span key={c.idColor} className="px-3 py-1.5 bg-zinc-100 border border-zinc-200 text-zinc-700 text-xs font-bold rounded-lg uppercase">
                          {c.descripcionColor} <span className="text-[10px] text-zinc-400 ml-1">({c.codigoColor})</span>
                        </span>
                      ))
                    ) : (
                      <span className="text-xs text-zinc-400 italic">No hay colores registrados.</span>
                    )}
                  </div>
                </div>

                <div>
                  <h4 className="text-xs font-bold text-zinc-400 uppercase tracking-widest flex items-center gap-2 mb-3">
                    <Layers size={14} className="text-amber-400"/> Tratamientos Técnicos
                  </h4>
                  <div className="flex flex-wrap gap-2">
                    {viewingItem.detalleTela?.atributosTecnicos?.length > 0 ? (
                      viewingItem.detalleTela.atributosTecnicos.map((a: any) => (
                        <span key={a.idAtributo} className="px-3 py-1.5 bg-amber-50 border border-amber-200 text-amber-700 text-xs font-bold rounded-lg uppercase">
                          {a.descripcionTecnica}
                        </span>
                      ))
                    ) : (
                      <span className="text-xs text-zinc-400 italic">Sin tratamientos especiales.</span>
                    )}
                  </div>
                </div>
              </div>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
