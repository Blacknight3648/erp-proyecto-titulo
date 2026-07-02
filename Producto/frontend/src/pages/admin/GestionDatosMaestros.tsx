import React, { useState, useEffect } from 'react';
import { Database, Search, Plus, Edit, Trash2, Save, X, RefreshCw, FolderTree, MapPin, Building2, Briefcase, Network, Phone, CreditCard, Box } from 'lucide-react';
import { api } from '../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { confirmDelete } from '../../utils/confirmDelete';

import { Card, CardHeader, CardTitle, CardContent } from "../../ui/card";
import { Input } from "../../ui/input";
import { Button } from "../../ui/button";

// labelField: campo que muestra en la tabla como "Nombre / Descripción"
// formFields: campos del formulario con su label y nombre de campo en el DTO del backend
const MASTER_DATA_ENTITIES = [
  {
    id: 'bancos', name: 'Bancos', icon: Building2, endpoint: '/bancos',
    labelField: 'nombre',
    formFields: [{ label: 'Nombre', field: 'nombre' }],
  },
  {
    id: 'paises', name: 'Países', icon: MapPin, endpoint: '/paises',
    labelField: 'nombre',
    formFields: [
      { label: 'Nombre', field: 'nombre' },
      { label: 'Código', field: 'codigo' },
    ],
  },
  {
    id: 'regiones', name: 'Regiones', icon: MapPin, endpoint: '/regiones',
    labelField: 'nombre',
    formFields: [
      { label: 'Nombre', field: 'nombre' },
      { label: 'ID País', field: 'paisId', type: 'number' },
    ],
  },
  {
    id: 'comunas', name: 'Comunas', icon: MapPin, endpoint: '/comunas',
    labelField: 'nombre',
    formFields: [
      { label: 'Nombre', field: 'nombre' },
      { label: 'ID Región', field: 'regionId', type: 'number' },
    ],
  },
  {
    id: 'rubros', name: 'Rubros', icon: Briefcase, endpoint: '/rubros',
    labelField: 'nombreRubro',
    formFields: [
      { label: 'Nombre Rubro', field: 'nombreRubro' },
      { label: 'Descripción', field: 'descripcion' },
    ],
  },
  {
    id: 'giros', name: 'Giros', icon: Network, endpoint: '/giros',
    labelField: 'nombreGiro',
    formFields: [
      { label: 'Código SII', field: 'codigoSii' },
      { label: 'Nombre Giro', field: 'nombreGiro' },
      { label: 'Descripción', field: 'descripcionGiro' },
    ],
  },
  {
    id: 'tipo-contacto', name: 'Tipos de Contacto', icon: Phone, endpoint: '/tipo-contacto',
    labelField: 'descripcionTipoContacto',
    formFields: [{ label: 'Descripción', field: 'descripcionTipoContacto' }],
  },
  {
    id: 'tipos-cuenta-bancaria', name: 'Tipos Cuenta Bancaria', icon: CreditCard, endpoint: '/tipos-cuenta-bancaria',
    labelField: 'descripcion',
    formFields: [{ label: 'Descripción', field: 'descripcion' }],
  },
  {
    id: 'tipos-direccion', name: 'Tipos Dirección', icon: Box, endpoint: '/tipos-direccion',
    labelField: 'descripcion',
    formFields: [{ label: 'Descripción', field: 'descripcion' }],
  },
];

export default function GestionDatosMaestros() {
  const [activeEntity, setActiveEntity] = useState(MASTER_DATA_ENTITIES[0]);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  
  // Modal / Editing State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [formData, setFormData] = useState({});

  const emptyForm = (entity) =>
    Object.fromEntries((entity.formFields || []).map(f => [f.field, '']));

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeEntity]);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await api.get(activeEntity.endpoint);
      setData(res.data);
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

  const handleOpenEdit = (item) => {
    setEditingItem(item);
    const populated = Object.fromEntries(
      (activeEntity.formFields || []).map(f => [f.field, item[f.field] ?? ''])
    );
    setFormData(populated);
    setIsModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    const firstField = activeEntity.formFields?.[0]?.field;
    if (firstField && !String(formData[firstField] ?? '').trim()) {
      return toast.error(`El campo "${activeEntity.formFields[0].label}" es requerido`);
    }

    try {
      if (editingItem) {
        const id = editingItem.id || editingItem.codigo;
        await api.put(`${activeEntity.endpoint}/${id}`, formData);
        toast.success("Registro actualizado correctamente");
      } else {
        await api.post(activeEntity.endpoint, formData);
        toast.success("Registro creado correctamente");
      }
      setIsModalOpen(false);
      loadData();
    } catch (error) {
      console.error("Error al guardar:", error);
      toast.error("Ocurrió un error al guardar el registro");
    }
  };

  const handleDelete = (item) => {
    const id = item.id || item.codigo;
    confirmDelete(`¿Eliminar de forma permanente el registro "${getLabel(item)}" de ${activeEntity.name}?`, async () => {
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

  const getLabel = (item) => item[activeEntity.labelField] || item.nombre || item.descripcion || item.name || 'Sin nombre';

  const filteredData = data.filter(item =>
    getLabel(item).toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="flex h-[calc(100vh-6rem)] gap-6 animate-in fade-in duration-500 text-foreground bg-background">
      <Toaster position="top-right" richColors />

      {/* Sidebar for Entities */}
      <Card className="w-72 overflow-hidden flex flex-col rounded-3xl">
        <CardHeader className="p-6 border-b border-border bg-muted/50">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center text-primary">
              <Database size={20} />
            </div>
            <div>
              <CardTitle className="font-bold text-foreground tracking-tight">Datos Maestros</CardTitle>
              <p className="text-xs text-muted-foreground font-medium">Configuración base</p>
            </div>
          </div>
        </CardHeader>

        <CardContent className="flex-1 overflow-y-auto p-4 space-y-1 custom-scrollbar">
          {MASTER_DATA_ENTITIES.map((entity) => {
            const Icon = entity.icon;
            const isActive = activeEntity.id === entity.id;
            return (
              <Button
                key={entity.id}
                variant={isActive ? "secondary" : "ghost"}
                onClick={() => setActiveEntity(entity)}
                className={`w-full flex items-center justify-start gap-3 px-4 py-3 text-sm font-semibold rounded-xl uppercase ${
                  isActive
                    ? 'bg-primary/10 text-primary hover:bg-primary/15 border-none'
                    : 'text-muted-foreground hover:text-foreground'
                }`}
              >
                <Icon size={18} className={isActive ? 'text-primary' : 'text-muted-foreground'} />
                {entity.name}
              </Button>
            );
          })}
        </CardContent>
      </Card>

      {/* Main Content Area */}
      <Card className="flex-1 overflow-hidden flex flex-col relative rounded-3xl">
        {/* Header */}
        <CardHeader className="p-6 border-b border-border flex flex-row items-center justify-between bg-card z-10">
          <div>
            <CardTitle className="text-2xl font-black tracking-tight">Gestión de {activeEntity.name}</CardTitle>
            <p className="text-sm text-muted-foreground mt-1 font-medium">
              Administra los registros maestros para el módulo de {activeEntity.name.toLowerCase()}
            </p>
          </div>
          
          <Button 
            onClick={handleOpenNew}
            className="flex items-center gap-2 px-5 py-2.5 rounded-xl uppercase text-xs"
          >
            <Plus size={18} />
            Nuevo Registro
          </Button>
        </CardHeader>

        {/* Toolbar */}
        <div className="p-6 pb-2 flex gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground z-10" size={18} />
            <Input 
              type="text"
              placeholder={`Buscar en ${activeEntity.name}...`}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 uppercase"
            />
          </div>
          <Button 
            variant="ghost"
            size="sm"
            onClick={loadData}
            className="p-2.5 text-muted-foreground hover:text-primary hover:bg-primary/10 border border-transparent rounded-xl"
            title="Recargar datos"
          >
            <RefreshCw size={18} className={loading ? "animate-spin" : ""} />
          </Button>
        </div>

        {/* Table Content */}
        <CardContent className="flex-1 overflow-auto p-6 pt-4 custom-scrollbar">
          {loading ? (
            <div className="flex flex-col items-center justify-center h-64 text-muted-foreground space-y-4">
              <RefreshCw size={32} className="animate-spin text-primary" />
              <span className="font-medium">Cargando {activeEntity.name}...</span>
            </div>
          ) : filteredData.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-64 bg-muted rounded-2xl border border-dashed border-border">
              <div className="w-16 h-16 bg-card rounded-full flex items-center justify-center shadow-sm mb-4">
                <Database className="text-muted-foreground" size={24} />
              </div>
              <h3 className="text-foreground font-bold">No hay registros</h3>
              <p className="text-muted-foreground text-sm mt-1">Crea el primer registro haciendo clic en "Nuevo Registro"</p>
            </div>
          ) : (
            <div className="border border-border rounded-2xl overflow-hidden bg-card">
              <table className="w-full text-left text-sm whitespace-nowrap">
                <thead className="bg-muted border-b border-border text-muted-foreground font-bold uppercase tracking-wider text-[11px]">
                  <tr>
                    <th className="px-6 py-4 w-20 text-center">ID</th>
                    <th className="px-6 py-4">Nombre / Descripción</th>
                    <th className="px-6 py-4 w-32 text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border font-medium text-foreground">
                  {filteredData.map((item, index) => (
                    <tr key={item.id || item.codigo || index} className="hover:bg-muted/80 transition-colors group">
                      <td className="px-6 py-4 text-center">
                        <span className="bg-muted text-muted-foreground px-2 py-1 rounded-md text-xs font-bold">
                          {item.id || item.codigo || (index + 1)}
                        </span>
                      </td>
                      <td className="px-6 py-4 uppercase">
                        {getLabel(item)}
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex items-center justify-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                          <Button 
                            variant="ghost"
                            size="sm"
                            onClick={() => handleOpenEdit(item)}
                            className="p-2 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-lg transition-colors"
                          >
                            <Edit size={16} />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDelete(item)}
                            className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                          >
                            <Trash2 size={16} />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>

        {/* Create / Edit Modal */}
        {isModalOpen && (
          <div className="absolute inset-0 z-50 flex items-center justify-center bg-foreground/40 backdrop-blur-sm animate-in fade-in duration-200">
            <Card className="p-8 max-w-md w-full mx-4 shadow-2xl animate-in zoom-in-95 duration-200 rounded-[2rem]">
              <CardHeader className="flex flex-row items-center justify-between mb-6 p-0">
                <CardTitle className="text-xl font-black tracking-tight">
                  {editingItem ? 'Editar' : 'Nuevo'} Registro
                </CardTitle>
                <Button 
                  variant="ghost"
                  size="sm"
                  onClick={() => setIsModalOpen(false)}
                  className="p-2 bg-muted text-muted-foreground rounded-full hover:bg-muted/70 transition-colors h-9 w-9 flex items-center justify-center"
                >
                  <X size={18} />
                </Button>
              </CardHeader>

              <form onSubmit={handleSave}>
                <CardContent className="space-y-4 p-0">
                  {(activeEntity.formFields || []).map((f, i) => (
                    <div key={f.field}>
                      <label className="block text-sm font-bold text-muted-foreground mb-2">
                        {f.label}
                      </label>
                      <Input
                        type={f.type || 'text'}
                        value={formData[f.field] ?? ''}
                        onChange={(e) => setFormData({ ...formData, [f.field]: f.type === 'number' ? (e.target.value === '' ? '' : Number(e.target.value)) : e.target.value.toUpperCase() })}
                        placeholder={f.label}
                        className="uppercase"
                        autoFocus={i === 0}
                      />
                    </div>
                  ))}

                  <div className="flex gap-3 pt-4">
                    <Button 
                      type="button"
                      variant="secondary"
                      onClick={() => setIsModalOpen(false)}
                      className="flex-1 py-3 px-4 rounded-xl font-bold uppercase text-xs"
                    >
                      Cancelar
                    </Button>
                    <Button 
                      type="submit"
                      className="flex-1 py-3 px-4 rounded-xl font-bold text-white bg-primary hover:bg-primary-hover shadow-md transition-colors flex items-center justify-center gap-2 uppercase text-xs"
                    >
                      <Save size={18} />
                      Guardar
                    </Button>
                  </div>
                </CardContent>
              </form>
            </Card>
          </div>
        )}
      </Card>
    </div>
  );
}
