import React, { useState, useEffect, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Shield, ChevronLeft, Save, Lock, Smartphone, Users, FileText, Package } from "lucide-react";
import { useRoles } from "../../../hooks/useRoles";
import { usePermisos } from "../../../hooks/usePermisos";
import { toast, Toaster } from "sonner";

const GestionPermisosRol = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { roles, assignPermissions } = useRoles();
  const { permisos, fetchPermisosPorRol, loading: loadingPermisos } = usePermisos();
  
  const [rol, setRol] = useState(null);
  const [permisosRol, setPermisosRol] = useState([]);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const r = roles.find(r => r.id.toString() === id);
    if (r) setRol(r);
  }, [roles, id]);

  useEffect(() => {
    const loadRolPermisos = async () => {
      const data = await fetchPermisosPorRol(id);
      setPermisosRol(data.map(p => p.id));
    };
    if (id) loadRolPermisos();
  }, [id]);

  const groupedPermisos = useMemo(() => {
    return permisos.reduce((acc, p) => {
      const modulo = p.modulo || "General";
      if (!acc[modulo]) acc[modulo] = [];
      acc[modulo].push(p);
      return acc;
    }, {});
  }, [permisos]);

  const togglePermiso = (permisoId) => {
    setPermisosRol(prev => 
      prev.includes(permisoId) 
        ? prev.filter(id => id !== permisoId)
        : [...prev, permisoId]
    );
  };

  const handleSave = async () => {
    setSaving(true);
    const success = await assignPermissions(id, permisosRol);
    setSaving(false);
    if (success) {
      setTimeout(() => navigate("/admin/roles"), 1500);
    }
  };

  const getModuleIcon = (modulo) => {
    switch(modulo.toLowerCase()) {
      case 'clientes': return <Users size={20} className="text-blue-500" />;
      case 'colaboradores': return <Users size={20} className="text-emerald-500" />;
      case 'productos': return <Package size={20} className="text-orange-500" />;
      case 'reportes': return <FileText size={20} className="text-purple-500" />;
      default: return <Shield size={20} className="text-slate-500" />;
    }
  };

  if (!rol) return <div className="p-10 text-center">Cargando rol...</div>;

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <Toaster position="top-right" richColors />
      
      <div className="max-w-5xl mx-auto mb-8">
        <button 
          onClick={() => navigate("/admin/roles")}
          className="flex items-center gap-2 text-slate-500 hover:text-slate-800 transition-colors mb-4 font-semibold"
        >
          <ChevronLeft size={20} />
          Volver a Roles
        </button>

        <div className="bg-white rounded-[2.5rem] p-8 shadow-xl border border-slate-100 flex items-center justify-between">
          <div className="flex items-center gap-6">
            <div className="p-5 bg-indigo-600 rounded-3xl text-white shadow-lg shadow-indigo-200">
              <Lock size={32} />
            </div>
            <div>
              <h1 className="text-3xl font-black text-slate-800 tracking-tight">Permisos de {rol.nombre}</h1>
              <p className="text-slate-500 font-medium">Configure los accesos específicos para este perfil</p>
            </div>
          </div>
          <button
            onClick={handleSave}
            disabled={saving}
            className="px-8 py-4 bg-slate-900 text-white rounded-[1.5rem] font-bold flex items-center gap-3 hover:bg-black transition-all shadow-xl disabled:opacity-50"
          >
            <Save size={20} />
            {saving ? "Guardando..." : "Guardar Matrix"}
          </button>
        </div>
      </div>

      <div className="max-w-5xl mx-auto space-y-6 pb-20">
        {Object.entries(groupedPermisos).map(([modulo, permisosModulo]) => (
          <div key={modulo} className="bg-white rounded-[2rem] border border-slate-100 shadow-sm overflow-hidden animate-in slide-in-from-bottom-4 duration-500">
            <div className="px-8 py-5 border-b border-slate-50 bg-slate-50/50 flex items-center gap-3">
              {getModuleIcon(modulo)}
              <h3 className="text-sm font-black text-slate-800 uppercase tracking-widest">{modulo}</h3>
            </div>
            
            <div className="p-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {permisosModulo.map(p => (
                <label 
                  key={p.id}
                  className={`relative flex items-center p-4 rounded-2xl border-2 transition-all cursor-pointer group ${
                    permisosRol.includes(p.id) 
                      ? "border-indigo-600 bg-indigo-50/30" 
                      : "border-slate-100 bg-white hover:border-slate-200"
                  }`}
                >
                  <input
                    type="checkbox"
                    className="sr-only"
                    checked={permisosRol.includes(p.id)}
                    onChange={() => togglePermiso(p.id)}
                  />
                  <div className={`w-6 h-6 rounded-lg border-2 mr-4 flex items-center justify-center transition-all ${
                    permisosRol.includes(p.id) 
                      ? "bg-indigo-600 border-indigo-600 text-white" 
                      : "border-slate-200"
                  }`}>
                    {permisosRol.includes(p.id) && <div className="w-2 h-2 rounded-full bg-white" />}
                  </div>
                  <div>
                    <p className={`text-sm font-bold ${permisosRol.includes(p.id) ? "text-indigo-900" : "text-slate-700"}`}>
                      {p.nombre}
                    </p>
                    <p className="text-[10px] text-slate-400 font-medium mt-0.5 leading-tight">
                      {p.descripcion || `Permitir acceso a ${p.nombre.toLowerCase()}`}
                    </p>
                  </div>
                  {permisosRol.includes(p.id) && (
                    <div className="absolute top-2 right-2">
                       <Shield size={12} className="text-indigo-400" />
                    </div>
                  )}
                </label>
              ))}
            </div>
          </div>
        ))}

        {Object.keys(groupedPermisos).length === 0 && !loadingPermisos && (
          <div className="text-center p-20 bg-white rounded-[2rem] border-2 border-dashed border-slate-200">
            <Shield size={48} className="mx-auto text-slate-200 mb-4" />
            <p className="text-slate-400 font-bold uppercase tracking-widest">No se encontraron permisos configurados</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default GestionPermisosRol;
