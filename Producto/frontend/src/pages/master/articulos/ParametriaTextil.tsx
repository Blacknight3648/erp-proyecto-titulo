import React, { useState, useEffect, useMemo } from 'react';
import {
  Database, Search, Plus, Edit, Trash2, Save,
  RefreshCw, Palette, ShieldAlert, FileText, Layers, Scale, AlertCircle, Tag
} from 'lucide-react';
import { api } from '../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { confirmDelete } from '../../../utils/confirmDelete';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "../../../ui/card";
import { Input } from "../../../ui/input";
import { Button } from "../../../ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../../../ui/dialog";
import { Badge } from "../../../ui/badge";
import { Checkbox } from "../../../ui/checkbox";

const PARAMETRIA_ENTITIES = [
  {
    id: 'familias', name: 'Familias de Tela', icon: Layers, endpoint: '/maestros/familias-tela', idField: 'idFamiliaTela',
    labelField: 'nombreFamilia', codeField: 'codigoFamilia',
    formFields: [
      { label: 'Nombre de la Familia (Ej: Gabardina)', field: 'nombreFamilia' },
    ],
  },
  {
    id: 'composiciones', name: 'Composiciones', icon: FileText, endpoint: '/maestros/composiciones', idField: 'idComposicion',
    labelField: 'descripcionComposicion', codeField: 'codigoComposicion',
    formFields: [
      { label: 'Composición Exacta (Ej: 60% Algodón / 40% Poliéster)', field: 'descripcionComposicion' },
      { label: 'Clasificación', field: 'clasificacion' },
      { label: 'Uso Típico Industrial', field: 'usoTipico' },
    ],
  },
  {
    id: 'gramajes', name: 'Gramajes / Medidas', icon: Scale, endpoint: '/maestros/gramajes-tela', idField: 'idGramaje',
    labelField: 'valorGramosM2', codeField: 'codigoGramaje',
    formatLabel: (item: any) => item.valorGramosM2 != null ? `${item.valorGramosM2} g/m²` : 'SIN VALOR',
    formFields: [
      { label: 'Valor (g/m²)', field: 'valorGramosM2', type: 'number' },
      { label: 'Categoría de Vestuario Típica', field: 'categoriaVestuario' },
    ],
  },
  {
    id: 'colores', name: 'Colores', icon: Palette, endpoint: '/maestros/colores-tela', idField: 'idColor',
    labelField: 'descripcionColor', codeField: 'codigoColor',
    formFields: [
      { label: 'Descripción del Color (Ej: Azul Marino)', field: 'descripcionColor' },
      { label: '¿Es color Pantone?', field: 'esPantone', type: 'checkbox' },
    ],
  },
  {
    id: 'atributos', name: 'Atributos / Tratamientos', icon: ShieldAlert, endpoint: '/maestros/atributos-tecnicos', idField: 'idAtributo',
    labelField: 'descripcionTecnica', codeField: 'codigoAtributo',
    formFields: [
      { label: 'Descripción Técnica', field: 'descripcionTecnica' },
      { label: 'Clasificación (Físico/Químico)', field: 'clasificacion' },
      { label: 'Impacto en ERP', field: 'impactoErp' },
    ],
  },
  {
    id: 'clasificaciones', name: 'Clasificación Técnica', icon: Tag, endpoint: '/maestros/clasificaciones-tecnicas', idField: 'idClasificacionTecnica',
    labelField: 'nombreClasificacion', codeField: null,
    formFields: [
      { label: 'Nombre de Clasificación (Ej: Tejido Técnico)', field: 'nombreClasificacion' },
    ],
  },
];

export default function ParametriaTextil() {
  const [activeEntity, setActiveEntity] = useState(PARAMETRIA_ENTITIES[0]);
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);
  const [formData, setFormData] = useState<Record<string, any>>({});

  const emptyForm = (entity: typeof PARAMETRIA_ENTITIES[0]) =>
    Object.fromEntries((entity.formFields || []).map(f => [f.field, f.type === 'checkbox' ? false : '']));

  useEffect(() => {
    loadData();
    setSearchTerm("");
  }, [activeEntity]);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await api.get(activeEntity.endpoint);
      setData(res.data as any[]);
    } catch (error) {
      console.error(`Error loading ${activeEntity.name}:`, error);
      toast.error(`No se pudieron cargar los datos de ${activeEntity.name}`);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setFormData(emptyForm(activeEntity));
    setIsModalOpen(true);
  };

  const handleOpenEdit = (item: any) => {
    setEditingItem(item);
    setFormData({ ...item });
    setIsModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const firstField = activeEntity.formFields?.[0]?.field;
    if (firstField && !String(formData[firstField] ?? '').trim()) {
      return toast.error(`El campo "${activeEntity.formFields[0].label}" es requerido`);
    }

    try {
      if (editingItem) {
        const id = editingItem[activeEntity.idField];
        await api.put(`${activeEntity.endpoint}/${id}`, formData);
        toast.success("Registro actualizado correctamente");
      } else {
        await api.post(activeEntity.endpoint, formData);
        toast.success("Registro creado correctamente. El código se generó automáticamente.");
      }
      setIsModalOpen(false);
      loadData();
    } catch (error) {
      console.error("Error al guardar:", error);
      toast.error("Ocurrió un error al guardar el registro");
    }
  };

  const handleDelete = (item: any) => {
    const id = item[activeEntity.idField];
    confirmDelete(`¿Eliminar de forma permanente "${getLabel(item)}"?`, async () => {
      try {
        await api.delete(`${activeEntity.endpoint}/${id}`);
        toast.success("Registro eliminado exitosamente");
        loadData();
      } catch (error) {
        console.error("Error deleting:", error);
        toast.error("Error al eliminar el registro");
      }
    });
  };

  const getLabel = (item: any) => {
    if (activeEntity.formatLabel) return activeEntity.formatLabel(item);
    return item[activeEntity.labelField] || 'Sin nombre';
  };

  const filteredData = useMemo(() => {
    return data.filter(item => {
      const label = String(getLabel(item)).toLowerCase();
      const codigo = activeEntity.codeField ? String(item[activeEntity.codeField] || '').toLowerCase() : '';
      const term = searchTerm.toLowerCase();
      return label.includes(term) || codigo.includes(term);
    });
  }, [data, searchTerm, activeEntity]);

  return (
    <div className="flex h-full gap-6 font-sans antialiased text-foreground">
      <Toaster position="top-right" richColors />

      {/* Navigation Sidebar */}
      <Card className="w-72 flex flex-col shadow-sm border-border rounded-2xl overflow-hidden shrink-0 h-full">
        <CardHeader className="p-5 border-b bg-muted/30">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-xl bg-primary/10 text-primary">
              <Database size={22} />
            </div>
            <div>
              <CardTitle className="text-lg font-bold tracking-tight">Parametría</CardTitle>
              <CardDescription className="text-xs font-medium">Lógica Textil ERP</CardDescription>
            </div>
          </div>
        </CardHeader>

        <CardContent className="flex-1 overflow-y-auto p-3 space-y-1 custom-scrollbar">
          {PARAMETRIA_ENTITIES.map((entity) => {
            const Icon = entity.icon;
            const isActive = activeEntity.id === entity.id;
            return (
              <Button
                key={entity.id}
                variant={isActive ? "secondary" : "ghost"}
                onClick={() => setActiveEntity(entity)}
                className={`w-full justify-start gap-3 px-4 py-6 text-xs font-bold tracking-wider uppercase rounded-xl transition-all ${
                  isActive
                    ? 'bg-primary/10 text-primary hover:bg-primary/15'
                    : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                }`}
              >
                <Icon size={16} className={isActive ? 'text-primary' : 'text-muted-foreground'} />
                {entity.name}
              </Button>
            );
          })}
        </CardContent>
      </Card>

      {/* Main Content Workspace */}
      <Card className="flex-1 flex flex-col shadow-sm border-border rounded-2xl overflow-hidden h-full">
        <CardHeader className="p-6 border-b flex flex-row items-center justify-between bg-card shrink-0 gap-4">
          <div>
            <CardTitle className="text-2xl font-extrabold tracking-tight">Gestión de {activeEntity.name}</CardTitle>
            <CardDescription className="text-sm font-medium mt-1">
              Visualiza y gestiona las clasificaciones de {activeEntity.name.toLowerCase()} para las telas. El código se genera automáticamente.
            </CardDescription>
          </div>

          <Button
            onClick={handleOpenNew}
            className="flex items-center gap-2 px-5 py-5 rounded-xl font-bold uppercase tracking-wider text-xs shadow-sm shrink-0"
          >
            <Plus size={16} />
            Nuevo Registro
          </Button>
        </CardHeader>

        {/* Action Toolbar */}
        <div className="px-6 py-4 flex gap-3 border-b bg-muted/10 shrink-0">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" size={16} />
            <Input
              type="text"
              placeholder={`Buscar por nombre o código...`}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 uppercase bg-card rounded-xl border-muted-foreground/20 focus-visible:ring-primary h-10 text-xs font-semibold"
            />
          </div>
          <Button
            variant="outline"
            size="icon"
            onClick={loadData}
            className="h-10 w-10 rounded-xl border-muted-foreground/20 text-muted-foreground hover:text-primary hover:bg-primary/5 transition-colors"
          >
            <RefreshCw size={16} className={loading ? "animate-spin" : ""} />
          </Button>
        </div>

        {/* Workspace List/Table View */}
        <CardContent className="flex-1 overflow-y-auto p-6 custom-scrollbar bg-muted/5">
          {loading ? (
            <div className="flex flex-col items-center justify-center h-64 text-muted-foreground space-y-3">
              <RefreshCw size={28} className="animate-spin text-primary" />
              <span className="text-xs font-bold uppercase tracking-wider">Cargando...</span>
            </div>
          ) : filteredData.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-72 bg-card rounded-2xl border border-dashed border-border p-8 text-center max-w-xl mx-auto mt-12 shadow-sm">
              <div className="w-12 h-12 bg-muted rounded-2xl flex items-center justify-center mb-4 text-muted-foreground">
                <AlertCircle size={22} />
              </div>
              <h3 className="text-sm font-bold text-foreground uppercase tracking-wide">No se encontraron registros</h3>
              <p className="text-xs text-muted-foreground max-w-xs mt-1">Crea registros aquí para poder utilizarlos luego al crear telas.</p>
            </div>
          ) : (
            <div className="border rounded-xl bg-card overflow-hidden shadow-sm">
              <div className="divide-y divide-border">
                {filteredData.map((item, index) => {
                  const itemKey = item[activeEntity.idField] ?? index;
                  const codigo = activeEntity.codeField ? item[activeEntity.codeField] : null;
                  return (
                    <div key={itemKey} className="flex items-center justify-between px-6 py-4 hover:bg-muted/30 transition-colors bg-card group">
                      <div className="w-24 text-center shrink-0">
                        {codigo ? (
                          <span className="bg-muted text-muted-foreground font-mono px-2.5 py-1 rounded-lg text-xs font-bold border border-border/40">
                            {codigo}
                          </span>
                        ) : (
                          <span className="text-muted-foreground text-xs">—</span>
                        )}
                      </div>

                      <div className="flex-1 px-4 flex flex-col gap-1 min-w-0">
                        <span className="text-sm font-semibold text-foreground uppercase truncate">
                          {getLabel(item)}
                        </span>
                        {activeEntity.formFields.length > 1 && (
                          <div className="flex items-center">
                            <span className="text-[10px] font-bold text-muted-foreground uppercase">
                              {item[activeEntity.formFields[1].field] || 'SIN CLASIFICACIÓN'}
                            </span>
                          </div>
                        )}
                      </div>

                      <div className="w-32 flex items-center justify-end gap-1">
                        <Button variant="ghost" size="icon" onClick={() => handleOpenEdit(item)} className="h-8 w-8 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-lg">
                          <Edit size={15} />
                        </Button>
                        <Button variant="ghost" size="icon" onClick={() => handleDelete(item)} className="h-8 w-8 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg">
                          <Trash2 size={15} />
                        </Button>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </CardContent>

        {/* Dialog Form Template */}
        <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
          <DialogContent className="max-w-md p-6 rounded-2xl border-border bg-card shadow-2xl">
            <DialogHeader className="mb-4">
              <DialogTitle className="text-xl font-extrabold tracking-tight text-foreground">
                {editingItem ? 'Editar' : 'Nuevo'} {activeEntity.name}
              </DialogTitle>
            </DialogHeader>

            <form onSubmit={handleSave} className="space-y-4">
              {editingItem && activeEntity.codeField && (
                <div className="space-y-1.5">
                  <label className="text-xs font-bold text-muted-foreground uppercase tracking-wider">Código (generado)</label>
                  <div className="h-11 rounded-xl border border-dashed border-muted-foreground/30 bg-muted/40 flex items-center px-3">
                    <Badge variant="outline" className="font-mono text-xs font-bold">
                      {editingItem[activeEntity.codeField]}
                    </Badge>
                  </div>
                </div>
              )}

              {(activeEntity.formFields || []).map((f: any, i) => (
                <div key={f.field} className="space-y-1.5">
                  {f.type === 'checkbox' ? (
                    <label className="flex items-center gap-3 p-3 rounded-xl border border-border cursor-pointer bg-background">
                      <Checkbox
                        checked={!!formData[f.field]}
                        onCheckedChange={(c) => setFormData({ ...formData, [f.field]: !!c })}
                      />
                      <span className="text-xs font-bold text-foreground uppercase tracking-wider">{f.label}</span>
                    </label>
                  ) : (
                    <>
                      <label className="text-xs font-bold text-muted-foreground uppercase tracking-wider">
                        {f.label}
                      </label>
                      <Input
                        type={f.type === 'number' ? 'number' : 'text'}
                        step={f.type === 'number' ? '0.01' : undefined}
                        value={formData[f.field] ?? ''}
                        onChange={(e) => setFormData({
                          ...formData,
                          [f.field]: f.type === 'number'
                            ? (e.target.value === '' ? '' : Number(e.target.value))
                            : e.target.value.toUpperCase()
                        })}
                        placeholder={`Ingresar ${f.label.toLowerCase()}...`}
                        className={`rounded-xl h-11 text-xs font-semibold bg-background ${f.type === 'number' ? '' : 'uppercase'}`}
                        autoFocus={i === 0}
                      />
                    </>
                  )}
                </div>
              ))}

              <div className="flex gap-3 pt-4">
                <Button type="button" variant="outline" onClick={() => setIsModalOpen(false)} className="flex-1 py-5 rounded-xl font-bold uppercase text-xs">Cancelar</Button>
                <Button type="submit" className="flex-1 py-5 rounded-xl font-bold bg-primary hover:bg-primary/90 text-white shadow-md flex gap-2 uppercase text-xs">
                  <Save size={15} /> Guardar
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </Card>
    </div>
  );
}
