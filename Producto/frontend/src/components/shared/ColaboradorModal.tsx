"use client";

import React, { useState, useMemo, useEffect } from 'react';
import { X, Save, User, Hash, Mail, Phone, MapPin, Shield, CheckCircle, AlertCircle, Calendar, Home } from 'lucide-react';
import { toast } from 'sonner';
import { validateRUN, formatRUN } from '../../utils/validations';
import { useRoles } from '../../hooks/useRoles';
import { useAreas } from '../../hooks/useAreas';

const REGIONES_COMUNAS = {
  "Arica y Parinacota": ["Arica", "Camarones", "Putre", "General Lagos"],
  "Tarapacá": ["Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"],
  "Antofagasta": ["Antofagasta", "Mejillones", "Sierra Gorda", "Taltal", "Calama", "Ollagüe", "San Pedro de Atacama", "Tocopilla", "María Elena"],
  "Atacama": ["Copiapó", "Caldera", "Tierra Amarilla", "Chañaral", "Diego de Almagro", "Vallenar", "Alto del Carmen", "Freirina", "Huasco"],
  "Coquimbo": ["La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paihuano", "Vicuña", "Illapel", "Canela", "Los Vilos", "Salamanca", "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"],
  "Valparaíso": ["Valparaíso", "Casablanca", "Concón", "Juan Fernández", "Puchuncaví", "Quintero", "Viña del Mar", "Isla de Pascua", "Los Andes", "Calle Larga", "Rinconada", "San Esteban", "La Ligua", "Cabildo", "Papudo", "Petorca", "Zapallar", "Quillota", "Calera", "Hijuelas", "La Cruz", "Nogales", "San Antonio", "Algarrobo", "Cartagena", "El Quisco", "El Tabo", "Santo Domingo", "San Felipe", "Catemu", "Llaillay", "Panquehue", "Putaendo", "Santa María", "Quilpué", "Limache", "Olmué", "Villa Alemana"],
  "Metropolitana de Santiago": ["Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Joaquín", "San Miguel", "San Ramón", "Vitacura", "Puente Alto", "Pirque", "San José de Maipo", "Colina", "Lampa", "Tiltil", "San Bernardo", "Buin", "Calera de Tango", "Paine", "Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro", "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"],
  "O'Higgins": ["Rancagua", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "Las Cabras", "Machalí", "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua", "Quinta de Tilcoco", "Rengo", "Requínoa", "San Vicente", "Pichilemu", "La Estrella", "Litueche", "Marchihue", "Navidad", "Paredones", "San Fernando", "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla", "Peralillo", "Placilla", "Pumanque", "Santa Cruz"],
  "Maule": ["Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco", "Pencahue", "Río Claro", "San Clemente", "San Rafael", "Cauquenes", "Chanco", "Pelluhue", "Curicó", "Hualañé", "Licantén", "Molina", "Rauco", "Romeral", "Sagrada Familia", "Teno", "Vichuquén", "Linares", "Colbún", "Longaví", "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"],
  "Ñuble": ["Chillán", "Bulnes", "Chillán Viejo", "El Carmen", "Pemuco", "Pinto", "Quillón", "San Ignacio", "Yungay", "Cobquecura", "Coelemu", "Ninhue", "Portezuelo", "Quirihue", "Ránquil", "Treguaco", "Coihueco", "Ñiquén", "San Carlos", "San Fabián", "San Nicolás"],
  "Biobío": ["Concepción", "Coronel", "Chiguayante", "Florida", "Hualqui", "Lota", "Penco", "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Hualpén", "Lebu", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Los Álamos", "Tirúa", "Los Ángeles", "Antuco", "Cabrero", "Laja", "Mulchén", "Nacimiento", "Negrete", "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel", "Yumbel", "Alto Biobío"],
  "La Araucanía": ["Temuco", "Carahue", "Cunco", "Curarrehue", "Freire", "Galvarino", "Gorbea", "Lautaro", "Loncoche", "Melipeuco", "Nueva Imperial", "Padre las Casas", "Perquenco", "Pitrufquén", "Pucón", "Saavedra", "Teodoro Schmidt", "Toltén", "Vilcún", "Villarrica", "Cholchol", "Angol", "Collipulli", "Curacautín", "Ercilla", "Lonquimay", "Los Sauces", "Lumaco", "Purén", "Renaico", "Traiguén", "Victoria"],
  "Los Ríos": ["Valdivia", "Corral", "Futrono", "La Unión", "Lago Ranco", "Lanco", "Los Lagos", "Máfil", "Mariquina", "Paillaco", "Panguipulli", "Río Bueno"],
  "Los Lagos": ["Puerto Montt", "Calbuco", "Cochamó", "Fresia", "Frutillar", "Los Muermos", "Llanquihue", "Maullín", "Puerto Varas", "Castro", "Ancud", "Chonchi", "Curaco de Vélez", "Dalcahue", "Puqueldón", "Queilén", "Quellón", "Quemchi", "Quinchao", "Osorno", "Puerto Octay", "Purranque", "Puyehue", "Río Negro", "San Juan de la Costa", "San Pablo", "Chaitén", "Futaleufú", "Hualaihué", "Palena"],
  "Aysén": ["Coyhaique", "Lago Verde", "Aysén", "Cisnes", "Guaitecas", "Cochrane", "O'Higgins", "Tortel", "Chile Chico", "Río Ibáñez"],
  "Magallanes": ["Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio", "Cabo de Hornos", "Antártica", "Porvenir", "Primavera", "Timaukel", "Natales", "Torres del Paine"]
};

const REGIONES = Object.keys(REGIONES_COMUNAS);

const formatPhoneInput = (value) => {
  const clean = value.replace(/\D/g, '').slice(0, 9);
  if (!clean) return '';
  if (clean.length <= 1) return clean;
  if (clean.length <= 5) return `${clean.slice(0, 1)} ${clean.slice(1)}`;
  return `${clean.slice(0, 1)} ${clean.slice(1, 5)} ${clean.slice(5, 9)}`;
};

const formatRunInput = (value) => {
  const clean = value.replace(/[^0-9Kk]/g, '');
  return formatRUN(clean);
};

export default function ColaboradorModal({ onClose, onSave, collaboratorToEdit = null }) {
  const { roles = [] } = useRoles();
  const { areas = [] } = useAreas();

  const initialForm = {
    usuarioNombre: '',
    usuarioApellidos: '',
    usuarioRun: '',
    usuarioEmail: '',
    telefono: '',
    fechaNacimiento: '',
    direccion: '',
    region: '',
    comuna: '',
    area: '',
    rol: '',
    password: '',
    activo: true
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  useEffect(() => {
    if (collaboratorToEdit) {
      setFormData({
        usuarioNombre: collaboratorToEdit.usuarioNombre || '',
        usuarioApellidos: collaboratorToEdit.usuarioApellidos || '',
        usuarioRun: formatRUN(collaboratorToEdit.usuarioRun) || '',
        usuarioEmail: collaboratorToEdit.usuarioEmail || '',
        telefono: formatPhoneInput((collaboratorToEdit.telefono || '').replace('+56', '').trim()),
        fechaNacimiento: collaboratorToEdit.fechaNacimiento || '',
        direccion: collaboratorToEdit.direccion || '',
        region: collaboratorToEdit.region || '',
        comuna: collaboratorToEdit.comuna || '',
        area: collaboratorToEdit.areas?.[0]?.nombre || collaboratorToEdit.area || '',
        rol: collaboratorToEdit.roles?.[0]?.nombre || collaboratorToEdit.rol || '',
        password: '',
        activo: collaboratorToEdit.enabled ?? collaboratorToEdit.activo ?? true,
      });
    } else {
      setFormData(initialForm);
    }
    setErrors({});
    setTouched({});
  }, [collaboratorToEdit]);

  const filteredRoles = useMemo(() => {
    if (!formData.area) return [];
    const selectedAreaObj = areas.find(a => a.nombre === formData.area);
    const selectedAreaId = selectedAreaObj?.areaId;
    if (!selectedAreaId) return roles;
    return roles.filter(r => r.areaId === selectedAreaId);
  }, [roles, formData.area, areas]);

  useEffect(() => {
    if (filteredRoles.length === 1 && formData.rol !== filteredRoles[0].nombre) {
      setFormData(prev => ({ ...prev, rol: filteredRoles[0].nombre }));
    }
  }, [filteredRoles]);

  // --- VALIDADOR CENTRALIZADO ---
  const validateField = (name, value) => {
    let error = '';

    switch (name) {
      case 'usuarioRun': {
        const clean = value.replace(/[^0-9Kk]/g, '');
        if (!clean) error = 'El RUN es requerido';
        else if (clean.length < 8) error = 'RUN demasiado corto';
        else if (!validateRUN(clean)) error = 'RUN inválido (DV incorrecto)';
        break;
      }
      case 'telefono': {
        const clean = value.replace(/\D/g, '');
        if (clean.length > 0 && (clean.length < 9 || !clean.startsWith('9'))) {
          error = 'Debe empezar con 9 y tener 9 dígitos';
        }
        break;
      }
      case 'usuarioEmail': {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!value) error = 'El correo es requerido';
        else if (!emailRegex.test(value)) error = 'Formato de correo no válido';
        break;
      }
      case 'usuarioNombre':
      case 'usuarioApellidos':
        if (!value.trim()) error = 'Este campo es requerido';
        else if (value.trim().length < 2) error = 'Mínimo 2 caracteres';
        break;
      case 'region':
      case 'comuna':
        if (!value.trim()) error = 'Este campo es requerido';
        break;
      case 'password':
        if (!collaboratorToEdit && (!value || value.length < 4)) {
          error = 'La clave debe tener al menos 4 caracteres';
        }
        break;
      case 'fechaNacimiento': {
        if (!value) break;
        const fechaNac = new Date(value);
        const hoy = new Date();
        if (isNaN(fechaNac.getTime())) {
          error = 'Fecha inválida';
          break;
        }
        let edad = hoy.getFullYear() - fechaNac.getFullYear();
        const mes = hoy.getMonth() - fechaNac.getMonth();
        if (mes < 0 || (mes === 0 && hoy.getDate() < fechaNac.getDate())) {
          edad--;
        }
        if (fechaNac > hoy) {
          error = 'La persona aún no ha nacido';
        } else if (edad < 18) {
          error = 'Debe ser mayor de edad (18+ años)';
        } else if (edad > 110) {
          error = 'La edad no puede superar los 110 años';
        }
        break;
      }
      default:
        break;
    }

    return error;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    let finalValue = type === 'checkbox' ? checked : value;

    if (name === 'usuarioRun') {
      finalValue = formatRunInput(value);
    } else if (name === 'telefono') {
      finalValue = formatPhoneInput(value);
    } else if (name === 'usuarioEmail') {
      finalValue = value.toLowerCase().trim();
    }

    if (name === 'region') {
      setFormData(prev => ({ ...prev, region: finalValue, comuna: '' }));
      if (touched['region']) {
        const error = validateField('region', finalValue);
        setErrors(prev => ({ ...prev, region: error, comuna: '' }));
      }
      return;
    }

    setFormData(prev => ({ ...prev, [name]: finalValue }));

    if (touched[name]) {
      const error = validateField(name, finalValue);
      setErrors(prev => ({ ...prev, [name]: error }));
    }
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    setTouched(prev => ({ ...prev, [name]: true }));
    const error = validateField(name, value);
    setErrors(prev => ({ ...prev, [name]: error }));
  };

  const isFormValid = useMemo(() => {
    const mandatoryFields = ['usuarioNombre', 'usuarioApellidos', 'usuarioRun', 'usuarioEmail', 'region', 'comuna'];
    if (!collaboratorToEdit) mandatoryFields.push('password');

    const hasErrors = Object.values(errors).some(err => !!err);
    if (hasErrors) return false;

    return mandatoryFields.every(field => {
      const value = formData[field];
      return value && validateField(field, value) === '';
    }) && validateField('fechaNacimiento', formData.fechaNacimiento) === '';
  }, [errors, formData, collaboratorToEdit]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const currentErrors = {};
    Object.keys(formData).forEach(key => {
      const error = validateField(key, formData[key]);
      if (error) currentErrors[key] = error;
    });

    if (Object.keys(currentErrors).length > 0) {
      setErrors(currentErrors);
      const allTouched = {};
      Object.keys(formData).forEach(key => { allTouched[key] = true; });
      setTouched(allTouched);
      toast.error('Por favor, revisa los errores en el formulario');
      return;
    }

    const payload = {
      usuarioRun: formData.usuarioRun.replace(/\./g, ''),
      usuarioNombre: formData.usuarioNombre.trim(),
      usuarioApellidos: formData.usuarioApellidos.trim(),
      usuarioEmail: formData.usuarioEmail.trim(),
      usuarioPassword: formData.password || undefined,
      telefono: formData.telefono ? `+56${formData.telefono.replace(/\s/g, '')}` : '',
      fechaNacimiento: formData.fechaNacimiento || null,
      direccion: formData.direccion || '',
      region: formData.region.trim(),
      comuna: formData.comuna.trim(),
      enabled: formData.activo,
      roles: formData.rol ? [formData.rol] : [],
      areas: formData.area ? [formData.area] : [],
    };

    onSave(payload);
  };

  const FieldError = ({ name }) => errors[name] && touched[name] ? (
    <div className="flex items-center gap-1.5 mt-1.5 text-xs font-medium text-rose-600 transition-all">
      <AlertCircle className="w-3.5 h-3.5 shrink-0" />
      <span>{errors[name]}</span>
    </div>
  ) : null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-in fade-in duration-200">
      <div className="bg-white w-full max-w-2xl rounded-2xl p-6 md:p-8 shadow-xl border border-slate-100 relative max-h-[90vh] overflow-y-auto flex flex-col">
        
        <button onClick={onClose} className="absolute top-5 right-5 p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-50 rounded-xl transition-all">
          <X className="w-5 h-5" />
        </button>

        <div className="mb-6">
          <h2 className="text-xl font-bold text-slate-900">
            {collaboratorToEdit ? 'Editar Perfil del Colaborador' : 'Registrar Nuevo Colaborador'}
          </h2>
          <p className="text-xs text-[#635bff] font-semibold mt-0.5 uppercase tracking-wider">
            Módulo de Personal e Identidad corporativa
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6 flex-1">
          
          {/* SECCIÓN 1 */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Identidad y Contacto</span>
              <div className="h-px flex-1 bg-slate-100"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Nombre *</label>
                <div className="relative">
                  <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="usuarioNombre"
                    value={formData.usuarioNombre}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.usuarioNombre && touched.usuarioNombre ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="Ej: Juan"
                  />
                </div>
                <FieldError name="usuarioNombre" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Apellidos *</label>
                <div className="relative">
                  <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="usuarioApellidos"
                    value={formData.usuarioApellidos}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.usuarioApellidos && touched.usuarioApellidos ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="Ej: Pérez González"
                  />
                </div>
                <FieldError name="usuarioApellidos" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">RUN *</label>
                <div className="relative">
                  <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs font-bold text-slate-400">ID</span>
                  <input
                    name="usuarioRun"
                    value={formData.usuarioRun}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.usuarioRun && touched.usuarioRun ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="12.345.678-9"
                  />
                </div>
                <FieldError name="usuarioRun" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Email Corporativo *</label>
                <div className="relative">
                  <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="usuarioEmail"
                    value={formData.usuarioEmail}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.usuarioEmail && touched.usuarioEmail ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="juan@empresa.cl"
                  />
                </div>
                <FieldError name="usuarioEmail" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Teléfono Móvil</label>
                <div className="relative">
                  <Phone className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <span className="absolute left-10 top-1/2 -translate-y-1/2 text-sm font-semibold text-slate-400 select-none">+56</span>
                  <input
                    name="telefono"
                    value={formData.telefono}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-18 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.telefono && touched.telefono ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="9 1234 5678"
                  />
                </div>
                <FieldError name="telefono" />
              </div>

              {/* Input de Fecha con Validador */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Fecha de Nacimiento</label>
                <div className="relative">
                  <Calendar className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <input
                    type="date"
                    name="fechaNacimiento"
                    value={formData.fechaNacimiento}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.fechaNacimiento && touched.fechaNacimiento ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                  />
                </div>
                <FieldError name="fechaNacimiento" />
              </div>
            </div>
          </div>

          {/* SECCIÓN 2 */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Ubicación</span>
              <div className="h-px flex-1 bg-slate-100"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Dirección</label>
                <div className="relative">
                  <Home className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="direccion"
                    value={formData.direccion}
                    onChange={handleChange}
                    className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all"
                    placeholder="Ej: Las Rosas 458"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Región *</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <select
                    name="region"
                    value={formData.region}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-8 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 appearance-none cursor-pointer ${
                      errors.region && touched.region ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                  >
                    <option value="">— Seleccionar región —</option>
                    {REGIONES.map(r => (
                      <option key={r} value={r}>{r}</option>
                    ))}
                  </select>
                </div>
                <FieldError name="region" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Comuna *</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <select
                    name="comuna"
                    value={formData.comuna}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    disabled={!formData.region}
                    className={`w-full pl-10 pr-8 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 appearance-none cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed ${
                      errors.comuna && touched.comuna ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                  >
                    <option value="">— Seleccionar comuna —</option>
                    {(REGIONES_COMUNAS[formData.region] || []).map(c => (
                      <option key={c} value={c}>{c}</option>
                    ))}
                  </select>
                </div>
                <FieldError name="comuna" />
              </div>
            </div>
          </div>

          {/* SECCIÓN 3 */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Cargo y Seguridad</span>
              <div className="h-px flex-1 bg-slate-100"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Área Asignada</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <select
                    name="area"
                    value={formData.area}
                    onChange={handleChange}
                    className="w-full pl-10 pr-8 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all cursor-pointer appearance-none"
                  >
                    <option value="">— Sin área —</option>
                    {areas.map(a => (
                      <option key={a.areaId} value={a.nombre}>{a.nombre}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Rol de Usuario</label>
                <div className="relative">
                  <Shield className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <select
                    name="rol"
                    value={formData.rol}
                    onChange={handleChange}
                    className="w-full pl-10 pr-8 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all cursor-pointer appearance-none"
                  >
                    <option value="">— Sin rol —</option>
                    {(formData.area ? filteredRoles : roles).map(r => (
                      <option key={r.id} value={r.nombre}>{r.nombre}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">
                  {collaboratorToEdit ? 'Nueva Contraseña (Opcional)' : 'Contraseña de Acceso *'}
                </label>
                <div className="relative">
                  <Hash className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.password && touched.password ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="••••••••"
                  />
                </div>
                <FieldError name="password" />
              </div>
            </div>
          </div>

          {/* ESTADO */}
          <div className="flex items-center justify-between p-4 bg-slate-50 rounded-xl border border-slate-100">
            <div className="flex items-center gap-3">
              <div className={`w-9 h-9 rounded-lg flex items-center justify-center transition-all ${formData.activo ? 'bg-emerald-50 text-emerald-600' : 'bg-slate-200 text-slate-400'}`}>
                <CheckCircle className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-semibold text-slate-800">Acceso al Sistema</h4>
                <p className="text-[11px] text-slate-400">Permitir el inicio de sesión del usuario</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" name="activo" checked={formData.activo} onChange={handleChange} className="sr-only peer" />
              <div className="w-11 h-6 bg-slate-200 rounded-full peer peer-checked:bg-emerald-500 transition-all after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-full"></div>
            </label>
          </div>

          <button
            type="submit"
            disabled={!isFormValid}
            className={`w-full py-3 bg-[#635bff] hover:bg-[#5249d3] text-white rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all shadow-sm ${
              !isFormValid ? 'opacity-40 cursor-not-allowed' : 'hover:shadow-md active:scale-[0.98]'
            }`}
          >
            <Save className="w-4 h-4" />
            {collaboratorToEdit ? 'Actualizar Colaborador' : 'Registrar Colaborador'}
          </button>
        </form>
      </div>
    </div>
  );
}