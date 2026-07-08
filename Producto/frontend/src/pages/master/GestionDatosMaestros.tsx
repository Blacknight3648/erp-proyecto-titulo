import React, { useState, useEffect, useMemo } from 'react';
import { 
  Database, Search, Plus, Edit, Trash2, Save, X, 
  RefreshCw, MapPin, Building2, Briefcase, Network, 
  Phone, CreditCard, Box, AlertCircle, ShoppingBag
} from 'lucide-react';
import { api } from '../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { confirmDelete } from '../../utils/confirmDelete';

import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "../../ui/card";
import { Input } from "../../ui/input";
import { Button } from "../../ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "../../ui/dialog";
import { Badge } from "../../ui/badge";
import GestionArticulosHub from './articulos/GestionArticulosHub';

const MASTER_DATA_ENTITIES = [
  {
    id: 'bancos', name: 'Bancos', icon: Building2, endpoint: '/bancos', idField: 'bancoId',
    labelField: 'nombreBanco',
    formFields: [
      { label: 'Nombre', field: 'nombreBanco' },
      { label: 'Código Banco', field: 'codigoBanco' }
    ],
  },
  {
    id: 'paises', name: 'Países', icon: MapPin, endpoint: '/paises', idField: 'idPais',
    labelField: 'nombrePais',
    formFields: [
      { label: 'Nombre', field: 'nombrePais' },
    ],
  },
  {
    id: 'regiones', name: 'Regiones', icon: MapPin, endpoint: '/regiones', idField: 'regionId',
    labelField: 'nombreRegion',
    relationField: 'pais.nombrePais',
    formFields: [
      { label: 'Nombre', field: 'nombreRegion' },
      { label: 'País', field: 'paisId', type: 'select', optionsEndpoint: '/paises', optionValueField: 'idPais', optionLabelField: 'nombrePais' },
    ],
  },
  {
    id: 'comunas', name: 'Comunas', icon: MapPin, endpoint: '/comunas', idField: 'comunaId',
    labelField: 'nombreComuna',
    relationField: 'region.nombreRegion',
    formFields: [
      { label: 'Nombre', field: 'nombreComuna' },
      { label: 'Región', field: 'regionId', type: 'select', optionsEndpoint: '/regiones', optionValueField: 'regionId', optionLabelField: 'nombreRegion' },
    ],
  },
  {
    id: 'rubros', name: 'Rubros', icon: Briefcase, endpoint: '/rubros', idField: 'rubroId',
    labelField: 'nombreRubro',
    formFields: [
      { label: 'Nombre Rubro', field: 'nombreRubro' },
      { label: 'Descripción', field: 'descripcionRubro' },
    ],
  },
  {
    id: 'giros', name: 'Giros', icon: Network, endpoint: '/giros', idField: 'giroId',
    labelField: 'nombreGiro',
    relationField: 'rubro.nombreRubro',
    formFields: [
      { label: 'Código SII', field: 'codigoSii' },
      { label: 'Nombre Giro', field: 'nombreGiro' },
      { label: 'Descripción', field: 'descripcionGiro' },
      { label: 'Rubro', field: 'rubroId', type: 'select', optionsEndpoint: '/rubros', optionValueField: 'rubroId', optionLabelField: 'nombreRubro' },
    ],
  },
  {
    id: 'tipo-contacto', name: 'Tipos de Contacto', icon: Phone, endpoint: '/tipo-contacto', idField: 'tipoContactoId',
    labelField: 'descripcionTipoContacto',
    formFields: [{ label: 'Descripción', field: 'descripcionTipoContacto' }],
  },
  {
    id: 'tipos-cuenta-bancaria', name: 'Tipos Cuenta Bancaria', icon: CreditCard, endpoint: '/tipos-cuenta-bancaria', idField: 'tipoCuentaId',
    labelField: 'denominacionCuenta',
    formFields: [{ label: 'Descripción', field: 'denominacionCuenta' }],
  },
  {
    id: 'tipos-direccion', name: 'Tipos Dirección', icon: Box, endpoint: '/tipos-direccion', idField: 'tipoDireccionId',
    labelField: 'descripcion',
    formFields: [{ label: 'Descripción', field: 'descripcion' }],
  },
  {
    id: 'categorias-tela', name: 'Categorías de Tela', icon: Box, endpoint: '/maestros/categorias-tela', idField: 'idCategoriaTela',
    labelField: 'nombreCategoriaTela',
    formFields: [
      { label: 'Nombre de Categoría', field: 'nombreCategoriaTela' }
    ],
  },
  {
    id: 'articulos', name: 'Administrador de Artículos', icon: ShoppingBag, endpoint: '', idField: '',
    labelField: '',
    formFields: [], 
  },
];

export default function GestionDatosMaestros() {
  const [activeEntity, setActiveEntity] = useState(MASTER_DATA_ENTITIES[0]);
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any>(null);
  const [formData, setFormData] = useState<Record<string, any>>({});
  const [optionsData, setOptionsData] = useState<Record<string, any[]>>({});

  const emptyForm = (entity: typeof MASTER_DATA_ENTITIES[0]) =>
    Object.fromEntries((entity.formFields || []).map(f => [f.field, '']));

  useEffect(() => {
    loadData();
    setSearchTerm("");
  }, [activeEntity]);

  const loadData = async () => {
    if (activeEntity.id === 'articulos') return; // El Hub de artículos carga sus propios datos
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

  const loadSelectOptions = async () => {
    let endpointsToLoad = new Set<string>();

    const selectFields: any[] = activeEntity.formFields?.filter((f: any) => f.type === 'select') || [];
    selectFields.forEach(f => {
      if (f.optionsEndpoint) endpointsToLoad.add(f.optionsEndpoint);
    });

    if (endpointsToLoad.size === 0) return;
    
    const newOptions: Record<string, any[]> = { ...optionsData };
    let hasChanges = false;
    
    for (const endpoint of endpointsToLoad) {
      if (!newOptions[endpoint]) {
        try {
          const res = await api.get(endpoint);
          newOptions[endpoint] = res.data as any[];
          hasChanges = true;
        } catch (error) {
          console.error("Error loading options for", endpoint, error);
        }
      }
    }
    if (hasChanges) {
      setOptionsData(newOptions);
    }
  };

  const handleOpenNew = async () => {
    setEditingItem(null);
    setFormData(emptyForm(activeEntity));
    await loadSelectOptions();
    setIsModalOpen(true);
  };

  const handleOpenEdit = async (item: any) => {
    setEditingItem(item);
    let initialData = { ...item };

    if (activeEntity.id === 'regiones' && item.pais) initialData.paisId = item.pais.idPais;
    if (activeEntity.id === 'comunas' && item.region) initialData.regionId = item.region.regionId;
    if (activeEntity.id === 'giros' && item.rubro) initialData.rubroId = item.rubro.rubroId;

    setFormData(initialData);
    await loadSelectOptions();
    setIsModalOpen(true);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    const firstField = activeEntity.formFields?.[0]?.field;
    if (firstField && !String(formData[firstField] ?? '').trim()) {
      return toast.error(`El campo "${activeEntity.formFields[0].label}" es requerido`);
    }

    const payload: any = { ...formData };

    if (activeEntity.id === 'regiones' && payload.paisId) {
      payload.pais = { idPais: Number(payload.paisId) };
      delete payload.paisId;
    }
    if (activeEntity.id === 'comunas' && payload.regionId) {
      payload.region = { regionId: Number(payload.regionId) };
      delete payload.regionId;
    }
    if (activeEntity.id === 'giros' && payload.rubroId) {
      payload.rubro = { rubroId: Number(payload.rubroId) };
      delete payload.rubroId;
    }

    try {
      if (editingItem) {
        const id = editingItem[activeEntity.idField];
        await api.put(`${activeEntity.endpoint}/${id}`, payload);
        toast.success("Registro actualizado correctamente");
      } else {
        await api.post(activeEntity.endpoint, payload);
        toast.success("Registro creado correctamente");
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

  const getLabel = (item: any) => item[activeEntity.labelField] || item.nombre || item.descripcion || 'Sin nombre';

  const getNestedValue = (obj: any, path: string) => {
    return path.split('.').reduce((acc, part) => acc && acc[part], obj) || '';
  };

  const filteredData = useMemo(() => {
    return data.filter(item => {
      const label = getLabel(item).toLowerCase();
      const term = searchTerm.toLowerCase();
      const extraSearch = activeEntity.relationField ? String(getNestedValue(item, activeEntity.relationField)).toLowerCase() : '';
      return label.includes(term) || extraSearch.includes(term);
    });
  }, [data, searchTerm, activeEntity]);

  return (
    <div className="flex h-[calc(100vh-4rem)] gap-6 p-6 font-sans antialiased text-foreground bg-background">
      <Toaster position="top-right" richColors />

      {/* Navigation Sidebar */}
      <Card className="w-80 flex flex-col shadow-md border-border rounded-2xl overflow-hidden shrink-0">
        <CardHeader className="p-5 border-b bg-muted/30">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-xl bg-primary/10 text-primary">
              <Database size={22} />
            </div>
            <div>
              <CardTitle className="text-lg font-bold tracking-tight">Datos Maestros</CardTitle>
              <CardDescription className="text-xs font-medium">Configuración global del sistema</CardDescription>
            </div>
          </div>
        </CardHeader>

        <CardContent className="flex-1 overflow-y-auto p-3 space-y-1 custom-scrollbar">
          {MASTER_DATA_ENTITIES.map((entity) => {
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
      <Card className="flex-1 flex flex-col shadow-md border-border rounded-2xl overflow-hidden">
        {activeEntity.id !== 'articulos' && (
        <>
        {/* Header section */}
        <CardHeader className="p-6 border-b flex flex-row items-center justify-between bg-card shrink-0 gap-4">
          <div>
            <CardTitle className="text-2xl font-extrabold tracking-tight">Gestión de {activeEntity.name}</CardTitle>
            <CardDescription className="text-sm font-medium mt-1">
              Visualiza, crea y modifica los registros de {activeEntity.name.toLowerCase()} del ecosistema.
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
              placeholder={`Buscar por nombre o relaciones...`}
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
            title="Recargar datos"
          >
            <RefreshCw size={16} className={loading ? "animate-spin" : ""} />
          </Button>
        </div>
        </>
        )}

        {/* Workspace List/Table View */}
        <CardContent className={`flex-1 overflow-y-auto custom-scrollbar bg-muted/5 ${activeEntity.id === 'articulos' ? 'p-0' : 'p-6'}`}>
          {activeEntity.id === 'articulos' ? (
             <div className="h-full">
                <GestionArticulosHub />
             </div>
          ) : loading ? (
            <div className="flex flex-col items-center justify-center h-64 text-muted-foreground space-y-3">
              <RefreshCw size={28} className="animate-spin text-primary" />
              <span className="text-xs font-bold uppercase tracking-wider">Cargando información...</span>
            </div>
          ) : filteredData.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-72 bg-card rounded-2xl border border-dashed border-border p-8 text-center max-w-xl mx-auto mt-12 shadow-sm">
              <div className="w-12 h-12 bg-muted rounded-2xl flex items-center justify-center mb-4 text-muted-foreground">
                <AlertCircle size={22} />
              </div>
              <h3 className="text-sm font-bold text-foreground uppercase tracking-wide">No se encontraron registros</h3>
              <p className="text-xs text-muted-foreground max-w-xs mt-1">
                {searchTerm ? 'Prueba cambiando los términos de búsqueda o limpia el filtro.' : 'Empieza agregando un elemento utilizando el botón superior.'}
              </p>
            </div>
          ) : (
            <div className="border rounded-xl bg-card overflow-hidden shadow-sm">
              <div className="divide-y divide-border">
                {/* Custom Flex Table Header */}
                <div className="flex items-center justify-between bg-muted/40 px-6 py-3.5 text-[10px] font-bold tracking-widest text-muted-foreground uppercase">
                  <div className="w-20 text-center">ID</div>
                  <div className="flex-1 px-4">Nombre / Descripción</div>
                  <div className="w-32 text-right">Acciones</div>
                </div>

                {/* Rows */}
                {filteredData.map((item, index) => {
                  const itemId = item[activeEntity.idField] || (index + 1);
                  const itemLabel = getLabel(item);
                  const secondaryRelation = activeEntity.relationField ? getNestedValue(item, activeEntity.relationField) : null;

                  return (
                    <div key={itemId} className="flex items-center justify-between px-6 py-4 hover:bg-muted/30 transition-colors bg-card group">
                      <div className="w-20 text-center">
                        <span className="bg-muted text-muted-foreground font-mono px-2.5 py-1 rounded-lg text-xs font-bold border border-border/40">
                          {itemId}
                        </span>
                      </div>
                      
                      <div className="flex-1 px-4 flex flex-col gap-1 min-w-0">
                        <span className="text-sm font-semibold text-foreground uppercase truncate">
                          {itemLabel}
                        </span>
                        {secondaryRelation && (
                          <div className="flex items-center">
                            <Badge variant="outline" className="text-[10px] font-bold px-2 py-0 bg-muted/40 uppercase text-muted-foreground rounded-md border-border/60">
                              {String(secondaryRelation)}
                            </Badge>
                          </div>
                        )}
                      </div>

                      <div className="w-32 flex items-center justify-end gap-1">
                        <Button 
                          variant="ghost"
                          size="icon"
                          onClick={() => handleOpenEdit(item)}
                          className="h-8 w-8 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-lg transition-colors"
                          title="Editar"
                        >
                          <Edit size={15} />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleDelete(item)}
                          className="h-8 w-8 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                          title="Eliminar"
                        >
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
        {activeEntity.id !== 'articulos' && (
        <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
          <DialogContent aria-describedby={undefined} className="max-w-md p-6 rounded-2xl border-border bg-card shadow-2xl">
            <DialogHeader className="mb-4">
              <DialogTitle className="text-xl font-extrabold tracking-tight text-foreground">
                {editingItem ? 'Editar' : 'Nuevo'} Registro en {activeEntity.name}
              </DialogTitle>
            </DialogHeader>

            <form onSubmit={handleSave} className="space-y-4">
              {(activeEntity.formFields || []).map((f: any, i) => (
                <div key={f.field} className="space-y-1.5">
                  <label className="text-xs font-bold text-muted-foreground uppercase tracking-wider">
                    {f.label}
                  </label>
                  {f.type === 'select' ? (
                    <select
                      className="flex h-11 w-full rounded-xl border border-input bg-background px-3 py-2 text-xs font-semibold uppercase tracking-wide focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:cursor-not-allowed disabled:opacity-50 transition-all shadow-sm"
                      value={formData[f.field] || ''}
                      onChange={(e) => setFormData({ ...formData, [f.field]: Number(e.target.value) })}
                    >
                      <option value="" className="text-muted-foreground">SELECCIONE {f.label.toUpperCase()}</option>
                      {(optionsData[f.optionsEndpoint] || []).map(opt => (
                        <option key={opt[f.optionValueField]} value={opt[f.optionValueField]}>
                          {String(opt[f.optionLabelField] || 'Sin nombre').toUpperCase()}
                        </option>
                      ))}
                    </select>
                  ) : (
                    <Input
                      type={f.type || 'text'}
                      value={formData[f.field] ?? ''}
                      onChange={(e) => setFormData({
                        ...formData,
                        [f.field]: f.type === 'number'
                          ? (e.target.value === '' ? '' : Number(e.target.value))
                          : e.target.value.toUpperCase()
                      })}
                      placeholder={`Ingresar ${f.label.toLowerCase()}...`}
                      className="uppercase rounded-xl h-11 text-xs font-semibold bg-background"
                      autoFocus={i === 0}
                    />
                  )}
                </div>
              ))}

              <div className="flex gap-3 pt-4">
                <Button 
                  type="button"
                  variant="outline"
                  onClick={() => setIsModalOpen(false)}
                  className="flex-1 py-5 rounded-xl font-bold uppercase text-xs border-muted-foreground/20 hover:bg-muted"
                >
                  Cancelar
                </Button>
                <Button 
                  type="submit"
                  className="flex-1 py-5 rounded-xl font-bold text-white bg-primary hover:bg-primary/90 shadow-md transition-all flex items-center justify-center gap-2 uppercase text-xs tracking-wider"
                >
                  <Save size={15} />
                  Guardar Cambios
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
        )}
      </Card>
    </div>
  );
}