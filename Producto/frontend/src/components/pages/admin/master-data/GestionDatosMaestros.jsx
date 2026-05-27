import React, { useState, useEffect } from 'react';
import { Database, Search, Plus, Edit, Trash2, Save, X, RefreshCw, FolderTree, MapPin, Building2, Briefcase, Network, Phone, CreditCard, Box } from 'lucide-react';
import { api } from '../../../../remote/service/api';
import { Toaster, toast } from 'sonner';
import { confirmDelete } from '../../../../utils/confirmDelete';

const MASTER_DATA_ENTITIES = [
  { id: 'bancos', name: 'Bancos', icon: Building2, endpoint: '/maestros/bancos' },
  { id: 'paises', name: 'Países', icon: MapPin, endpoint: '/maestros/paises' },
  { id: 'regiones', name: 'Regiones', icon: MapPin, endpoint: '/maestros/regiones' },
  { id: 'comunas', name: 'Comunas', icon: MapPin, endpoint: '/maestros/comunas' },
  { id: 'rubros', name: 'Rubros', icon: Briefcase, endpoint: '/maestros/rubros' },
  { id: 'giros', name: 'Giros', icon: Network, endpoint: '/maestros/giros' },
  { id: 'tipos-contacto', name: 'Tipos de Contacto', icon: Phone, endpoint: '/maestros/tipos-contacto' },
  { id: 'tipos-cuenta-bancaria', name: 'Tipos Cuenta Bancaria', icon: CreditCard, endpoint: '/maestros/tipos-cuenta-bancaria' },
  { id: 'tipos-direccion', name: 'Tipos Dirección', icon: Box, endpoint: '/maestros/tipos-direccion' },
  { id: 'tipos-servicio', name: 'Tipos Servicio', icon: FolderTree, endpoint: '/maestros/tipos-servicio' }
];

export default function GestionDatosMaestros() {
  const [activeEntity, setActiveEntity] = useState(MASTER_DATA_ENTITIES[0]);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  
  // Modal / Editing State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [formData, setFormData] = useState({ nombre: '' }); // We assume most entities just need a 'nombre'
  
  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeEntity]);

  const loadData = async () => {
    setLoading(true);
    try {
      // Usar un endpoint generico de la API o los definidos
      const res = await api.get(activeEntity.endpoint);
      setData(res.data);
    } catch (error) {
      console.error(`Error loading ${activeEntity.name}:`, error);
      toast.error(`No se pudieron cargar los datos de ${activeEntity.name}`);
      setData([]); // fallback for now
    } finally {
      setLoading(false);
    }
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setFormData({ nombre: '' });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (item) => {
    setEditingItem(item);
    setFormData({ nombre: item.nombre || item.descripcion || item.name || '' });
    setIsModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!formData.nombre.trim()) return toast.error("El nombre es requerido");

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
    confirmDelete(`¿Eliminar de forma permanente el registro "${item.nombre || item.descripcion || item.name}" de ${activeEntity.name}?`, async () => {
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

  const filteredData = data.filter(item => {
    const term = searchTerm.toLowerCase();
    const name = (item.nombre || item.descripcion || item.name || '').toLowerCase();
    return name.includes(term);
  });

  return (
    <div className="flex h-[calc(100vh-6rem)] gap-6 animate-in fade-in duration-500">
      <Toaster position="top-right" richColors />

      {/* Sidebar for Entities */}
      <div className="w-72 bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden flex flex-col">
        <div className="p-6 border-b border-slate-100 bg-slate-50/50">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-blue-100 flex items-center justify-center text-blue-600">
              <Database size={20} />
            </div>
            <div>
              <h2 className="font-bold text-slate-800 tracking-tight">Datos Maestros</h2>
              <p className="text-xs text-slate-500 font-medium">Configuración base</p>
            </div>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-4 space-y-1 custom-scrollbar">
          {MASTER_DATA_ENTITIES.map((entity) => {
            const Icon = entity.icon;
            const isActive = activeEntity.id === entity.id;
            return (
              <button
                key={entity.id}
                onClick={() => setActiveEntity(entity)}
                className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 text-sm font-semibold ${
                  isActive 
                    ? 'bg-blue-50 text-blue-700' 
                    : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
                }`}
              >
                <Icon size={18} className={isActive ? 'text-blue-500' : 'text-slate-400'} />
                {entity.name}
              </button>
            );
          })}
        </div>
      </div>

      {/* Main Content Area */}
      <div className="flex-1 bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden flex flex-col relative">
        {/* Header */}
        <div className="p-6 border-b border-slate-100 flex items-center justify-between bg-white z-10">
          <div>
            <h1 className="text-2xl font-black text-slate-800 tracking-tight">Gestión de {activeEntity.name}</h1>
            <p className="text-sm text-slate-500 mt-1 font-medium">
              Administra los registros maestros para el módulo de {activeEntity.name.toLowerCase()}
            </p>
          </div>
          
          <button 
            onClick={handleOpenNew}
            className="flex items-center gap-2 bg-blue-600 text-white px-5 py-2.5 rounded-xl text-sm font-bold shadow-sm shadow-blue-600/20 hover:bg-blue-700 hover:shadow-md hover:-translate-y-0.5 transition-all duration-200"
          >
            <Plus size={18} />
            Nuevo Registro
          </button>
        </div>

        {/* Toolbar */}
        <div className="p-6 pb-2 flex gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text"
              placeholder={`Buscar en ${activeEntity.name}...`}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all"
            />
          </div>
          <button 
            onClick={loadData}
            className="p-2.5 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-xl transition-all border border-transparent hover:border-blue-100"
            title="Recargar datos"
          >
            <RefreshCw size={18} className={loading ? "animate-spin" : ""} />
          </button>
        </div>

        {/* Table Content */}
        <div className="flex-1 overflow-auto p-6 pt-4">
          {loading ? (
            <div className="flex flex-col items-center justify-center h-64 text-slate-400 space-y-4">
              <RefreshCw size={32} className="animate-spin text-blue-500" />
              <span className="font-medium">Cargando {activeEntity.name}...</span>
            </div>
          ) : filteredData.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-64 bg-slate-50 rounded-2xl border border-dashed border-slate-200">
              <div className="w-16 h-16 bg-white rounded-full flex items-center justify-center shadow-sm mb-4">
                <Database className="text-slate-300" size={24} />
              </div>
              <h3 className="text-slate-600 font-bold">No hay registros</h3>
              <p className="text-slate-400 text-sm mt-1">Crea el primer registro haciendo clic en "Nuevo Registro"</p>
            </div>
          ) : (
            <div className="border border-slate-200 rounded-2xl overflow-hidden bg-white">
              <table className="w-full text-left text-sm whitespace-nowrap">
                <thead className="bg-slate-50 border-b border-slate-200 text-slate-600 font-bold uppercase tracking-wider text-[11px]">
                  <tr>
                    <th className="px-6 py-4 w-20 text-center">ID</th>
                    <th className="px-6 py-4">Nombre / Descripción</th>
                    <th className="px-6 py-4 w-32 text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 font-medium text-slate-700">
                  {filteredData.map((item, index) => (
                    <tr key={item.id || item.codigo || index} className="hover:bg-slate-50/80 transition-colors group">
                      <td className="px-6 py-4 text-center">
                        <span className="bg-slate-100 text-slate-500 px-2 py-1 rounded-md text-xs font-bold">
                          {item.id || item.codigo || (index + 1)}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        {item.nombre || item.descripcion || item.name || 'Sin nombre'}
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex items-center justify-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                          <button 
                            onClick={() => handleOpenEdit(item)}
                            className="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                          >
                            <Edit size={16} />
                          </button>
                          <button 
                            onClick={() => handleDelete(item)}
                            className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors"
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Create / Edit Modal */}
        {isModalOpen && (
          <div className="absolute inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white rounded-3xl p-8 max-w-md w-full mx-4 shadow-2xl border border-slate-100 animate-in zoom-in-95 duration-200">
              <div className="flex items-center justify-between mb-6">
                <h3 className="text-xl font-black text-slate-800 tracking-tight">
                  {editingItem ? 'Editar' : 'Nuevo'} Registro
                </h3>
                <button 
                  onClick={() => setIsModalOpen(false)}
                  className="p-2 bg-slate-50 text-slate-400 rounded-full hover:bg-slate-100 hover:text-slate-700 transition-colors"
                >
                  <X size={18} />
                </button>
              </div>

              <form onSubmit={handleSave} className="space-y-6">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">
                    Nombre / Descripción
                  </label>
                  <input
                    type="text"
                    value={formData.nombre}
                    onChange={(e) => setFormData({...formData, nombre: e.target.value})}
                    placeholder={`Ej. Nuevo registro de ${activeEntity.name}`}
                    className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all"
                    autoFocus
                  />
                </div>

                <div className="flex gap-3 pt-2">
                  <button 
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="flex-1 py-3 px-4 rounded-xl font-bold text-slate-600 bg-slate-100 hover:bg-slate-200 transition-colors"
                  >
                    Cancelar
                  </button>
                  <button 
                    type="submit"
                    className="flex-1 py-3 px-4 rounded-xl font-bold text-white bg-blue-600 hover:bg-blue-700 shadow-md shadow-blue-600/20 transition-colors flex items-center justify-center gap-2"
                  >
                    <Save size={18} />
                    Guardar
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
