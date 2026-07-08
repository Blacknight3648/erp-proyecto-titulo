import React, { useState, useEffect, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Shield, ChevronLeft, Save, Lock, Users, FileText, Package } from "lucide-react";
import { useRoles } from "../../hooks/useRoles";
import { usePermisos } from "../../hooks/usePermisos";
import { toast, Toaster } from "sonner";

import { Checkbox } from "../../ui/checkbox";

const GestionPermisosRol = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { roles, assignPermissions } = useRoles();
  const { permisos, fetchPermisosPorRol, loading: loadingPermisos } = usePermisos();
  
  const [rol, setRol] = useState<any>(null);
  const [permisosRol, setPermisosRol] = useState<number[]>([]);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const r = roles.find(r => r.id.toString() === id);
    if (r) setRol(r);
  }, [roles, id]);

  useEffect(() => {
    const loadRolPermisos = async () => {
      if (id) {
        const data = await fetchPermisosPorRol(id);
        setPermisosRol(data.map((p: any) => p.id));
      }
    };
    loadRolPermisos();
  }, [id, fetchPermisosPorRol]);

  const groupedPermisos = useMemo(() => {
    return permisos.reduce((acc: any, p: any) => {
      const modulo = p.modulo || "General";
      if (!acc[modulo]) acc[modulo] = [];
      acc[modulo].push(p);
      return acc;
    }, {});
  }, [permisos]);

  const togglePermiso = (permisoId: number) => {
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

  const getModuleIcon = (modulo: string) => {
    switch(modulo.toLowerCase()) {
      case 'clientes': return <Users size={16} className="text-zinc-500" strokeWidth={2} />;
      case 'colaboradores': return <Users size={16} className="text-zinc-500" strokeWidth={2} />;
      case 'productos': return <Package size={16} className="text-zinc-500" strokeWidth={2} />;
      case 'reportes': return <FileText size={16} className="text-zinc-500" strokeWidth={2} />;
      default: return <Shield size={16} className="text-zinc-500" strokeWidth={2} />;
    }
  };

  if (!rol) return (
    <div className="min-h-screen flex items-center justify-center bg-zinc-50/50">
      <div className="text-center text-zinc-500 text-xs font-bold uppercase tracking-widest">
        Cargando rol...
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-zinc-50/50 px-4 py-8 sm:px-8 font-sans antialiased text-zinc-900 selection:bg-zinc-200">
      <Toaster position="top-right" richColors />
      
      <div className="max-w-5xl mx-auto mb-8">
        <button 
          onClick={() => navigate("/admin/roles")}
          className="flex items-center gap-2 text-zinc-400 hover:text-zinc-900 transition-colors mb-6 font-bold text-xs uppercase tracking-wider bg-transparent border-none p-0 cursor-pointer"
        >
          <ChevronLeft size={16} strokeWidth={2.5} />
          Volver a Roles
        </button>
 
        <div className="p-8 bg-white shadow-[0_2px_12px_-4px_rgba(0,0,0,0.05)] border border-zinc-200/80 flex flex-col md:flex-row items-center justify-between gap-6 rounded-3xl">
          <div className="flex items-center gap-5">
            <div className="p-4 bg-zinc-100 border border-zinc-200/60 rounded-2xl text-zinc-700 shadow-sm">
              <Lock size={28} strokeWidth={1.75} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-zinc-900 tracking-tight uppercase">
                Permisos: {rol.nombre}
              </h1>
              <p className="text-xs text-zinc-500 font-medium uppercase tracking-wider mt-1">
                Configure los accesos específicos para este perfil
              </p>
            </div>
          </div>
          <button
            onClick={handleSave}
            disabled={saving}
            className="px-6 py-3.5 bg-zinc-900 text-white rounded-xl font-bold flex items-center gap-2.5 hover:bg-zinc-800 transition-all shadow-sm active:scale-[0.98] disabled:opacity-50 text-xs uppercase tracking-wide"
          >
            <Save size={16} strokeWidth={2} />
            {saving ? "Guardando..." : "Guardar Accesos"}
          </button>
        </div>
      </div>

      <div className="max-w-5xl mx-auto space-y-6 pb-20">
        {Object.entries(groupedPermisos).map(([modulo, permisosModulo]: [string, any]) => (
          <div key={modulo} className="overflow-hidden bg-white border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] rounded-2xl">
            <div className="px-6 py-4 border-b border-zinc-100 bg-zinc-50/50 flex flex-row items-center gap-2.5">
              {getModuleIcon(modulo)}
              <h2 className="text-[11px] font-bold text-zinc-500 uppercase tracking-widest">{modulo}</h2>
            </div>
            
            <div className="p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {permisosModulo.map((p: any) => {
                const isChecked = permisosRol.includes(p.id);
                return (
                  <label 
                    key={p.id}
                    className={`relative flex items-start p-4 rounded-xl border transition-all cursor-pointer group ${
                      isChecked
                        ? "border-zinc-900 bg-zinc-50/80 shadow-sm"
                        : "border-zinc-200 hover:border-zinc-300 bg-white"
                    }`}
                  >
                    <div className="mt-0.5">
                      <Checkbox
                        checked={isChecked}
                        onCheckedChange={() => togglePermiso(p.id)}
                        className={`border-2 ${isChecked ? 'border-zinc-900 data-[state=checked]:bg-zinc-900' : 'border-zinc-300'}`}
                      />
                    </div>
                    <div className="ml-3 flex-1">
                      <p className={`text-xs font-bold uppercase tracking-wide ${isChecked ? "text-zinc-900" : "text-zinc-600 group-hover:text-zinc-900"}`}>
                        {p.nombre}
                      </p>
                      <p className="text-[10px] text-zinc-400 font-medium mt-1 leading-relaxed uppercase">
                        {p.descripcion || `Permitir acceso a ${p.nombre}`}
                      </p>
                    </div>
                    {isChecked && (
                      <div className="absolute top-3 right-3 text-zinc-900">
                         <Shield size={12} strokeWidth={2.5} />
                      </div>
                    )}
                  </label>
                );
              })}
            </div>
          </div>
        ))}

        {Object.keys(groupedPermisos).length === 0 && !loadingPermisos && (
          <div className="text-center p-16 bg-white border border-zinc-200 rounded-2xl flex flex-col items-center justify-center">
            <Shield size={32} className="text-zinc-300 mb-3" strokeWidth={1.5} />
            <p className="text-[11px] text-zinc-400 font-bold uppercase tracking-widest">No se encontraron permisos configurados</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default GestionPermisosRol;
