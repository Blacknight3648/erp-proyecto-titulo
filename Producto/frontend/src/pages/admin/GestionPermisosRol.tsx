import React, { useState, useEffect, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Shield, ChevronLeft, Save, Lock, Users, FileText, Package } from "lucide-react";
import { useRoles } from "../../hooks/useRoles";
import { usePermisos } from "../../hooks/usePermisos";
import { toast, Toaster } from "sonner";

import { Card, CardHeader, CardTitle, CardContent } from "../../ui/card";
import { Button } from "../../ui/button";
import { Checkbox } from "../../ui/checkbox";

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
      case 'clientes': return <Users size={20} className="text-primary" />;
      case 'colaboradores': return <Users size={20} className="text-success" />;
      case 'productos': return <Package size={20} className="text-warning" />;
      case 'reportes': return <FileText size={20} className="text-brand-violet" />;
      default: return <Shield size={20} className="text-muted-foreground" />;
    }
  };

  if (!rol) return <div className="p-10 text-center text-muted-foreground">Cargando rol...</div>;

  return (
    <div className="min-h-screen bg-background p-8 font-sans antialiased text-foreground">
      <Toaster position="top-right" richColors />
      
      <div className="max-w-5xl mx-auto mb-8">
        <Button 
          variant="ghost"
          size="sm"
          onClick={() => navigate("/admin/roles")}
          className="flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors mb-4 font-semibold p-0 hover:bg-transparent"
        >
          <ChevronLeft size={20} />
          Volver a Roles
        </Button>
 
        <Card className="p-8 shadow-xl border border-border flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="flex items-center gap-6">
            <div className="p-5 bg-primary rounded-3xl text-white shadow-lg shadow-primary/20">
              <Lock size={32} />
            </div>
            <div>
              <h1 className="text-3xl font-black text-foreground tracking-tight">Permisos de {rol.nombre}</h1>
              <p className="text-muted-foreground font-medium">Configure los accesos específicos para este perfil</p>
            </div>
          </div>
          <Button
            onClick={handleSave}
            disabled={saving}
            className="px-8 py-4 bg-primary text-white rounded-2xl font-bold flex items-center gap-3 hover:bg-primary-hover transition-all shadow-xl disabled:opacity-50"
          >
            <Save size={20} />
            {saving ? "Guardando..." : "Guardar Matrix"}
          </Button>
        </Card>
      </div>

      <div className="max-w-5xl mx-auto space-y-6 pb-20">
        {Object.entries(groupedPermisos).map(([modulo, permisosModulo]) => (
          <Card key={modulo} className="overflow-hidden border border-border shadow-sm animate-in slide-in-from-bottom-4 duration-500">
            <CardHeader className="px-8 py-5 border-b border-border bg-muted/50 flex flex-row items-center gap-3">
              {getModuleIcon(modulo)}
              <CardTitle className="text-sm font-black text-foreground uppercase tracking-widest">{modulo}</CardTitle>
            </CardHeader>
            
            <CardContent className="p-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {permisosModulo.map(p => {
                const isChecked = permisosRol.includes(p.id);
                return (
                  <label 
                    key={p.id}
                    className={`relative flex items-center p-4 rounded-2xl border-2 transition-all cursor-pointer group ${
                      isChecked
                        ? "border-primary bg-primary/5"
                        : "border-border bg-card hover:border-border-strong"
                    }`}
                  >
                    <Checkbox
                      checked={isChecked}
                      onCheckedChange={() => togglePermiso(p.id)}
                      className="mr-4"
                    />
                    <div>
                      <p className={`text-sm font-bold ${isChecked ? "text-primary" : "text-foreground"}`}>
                        {p.nombre}
                      </p>
                      <p className="text-[10px] text-muted-foreground font-medium mt-0.5 leading-tight">
                        {p.descripcion || `Permitir acceso a ${p.nombre.toLowerCase()}`}
                      </p>
                    </div>
                    {isChecked && (
                      <div className="absolute top-2 right-2">
                         <Shield size={12} className="text-primary" />
                      </div>
                    )}
                  </label>
                );
              })}
            </CardContent>
          </Card>
        ))}

        {Object.keys(groupedPermisos).length === 0 && !loadingPermisos && (
          <Card className="text-center p-20 bg-card border-2 border-dashed border-border flex flex-col items-center justify-center">
            <Shield size={48} className="text-muted-foreground mb-4" />
            <p className="text-muted-foreground font-bold uppercase tracking-widest">No se encontraron permisos configurados</p>
          </Card>
        )}
      </div>
    </div>
  );
};

export default GestionPermisosRol;
