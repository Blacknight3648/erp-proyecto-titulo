"use client";

import React, { useState, useMemo, useEffect } from 'react';
import { X, Save, User, Hash, Mail, Phone, MapPin, Shield, CheckCircle, AlertCircle, Calendar, Home } from 'lucide-react';
import { toast } from 'sonner';
import { validateRUN, formatRUN } from '../../../utils/validations';
import { useRoles } from '../../../hooks/useRoles';
import { useAreas } from '../../../hooks/useAreas';

// --- FORMATEADORES ESTÁNDAR ---
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
    passwordChangeConsent: false,
    activo: true
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  // Carga de datos iniciales
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
        passwordChangeConsent: false,
        activo: collaboratorToEdit.enabled ?? collaboratorToEdit.activo ?? true,
      });
    } else {
      setFormData(initialForm);
    }
    setErrors({});
    setTouched({});
  }, [collaboratorToEdit]);

  // Lógica de filtrado de roles por área
  const filteredRoles = useMemo(() => {
    if (!formData.area) return [];
    const selectedAreaObj = areas.find(a => a.nombre === formData.area);
    const selectedAreaId = selectedAreaObj?.areaId;
    if (!selectedAreaId) return roles;
    return roles.filter(r => r.areaId === selectedAreaId);
  }, [roles, formData.area, areas]);

  // Auto-selección de rol único
  useEffect(() => {
    if (filteredRoles.length === 1 && formData.rol !== filteredRoles[0].nombre) {
      setFormData(prev => ({ ...prev, rol: filteredRoles[0].nombre }));
    }
  }, [filteredRoles]);

  // --- VALIDADOR ESTÁNDAR POR CAMPO ---
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
        break;
      case 'password':
        if (!collaboratorToEdit && (!value || value.length < 4)) {
          error = 'La clave debe tener al menos 4 caracteres';
        } else if (value && value.length < 4) {
          error = 'La clave debe tener al menos 4 caracteres';
        } else if (collaboratorToEdit && value && !formData.passwordChangeConsent) {
          error = 'Debes confirmar que cuentas con el consentimiento del colaborador';
        }
        break;
      default:
        break;
    }

    return error;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    let finalValue = type === 'checkbox' ? checked : value;

    // Aplicar formateadores dinámicos en tiempo de escritura
    if (name === 'usuarioRun') {
      finalValue = formatRunInput(value);
    } else if (name === 'telefono') {
      finalValue = formatPhoneInput(value);
    } else if (name === 'usuarioEmail') {
      finalValue = value.toLowerCase().trim();
    }

    setFormData(prev => ({ ...prev, [name]: finalValue }));

    // Validar inmediatamente si el usuario ya interactuó con el campo
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

  // Validación del formulario completo para el botón Submit
  const isFormValid = useMemo(() => {
    // Campos obligatorios
    const mandatoryFields = ['usuarioNombre', 'usuarioApellidos', 'usuarioRun', 'usuarioEmail'];
    if (!collaboratorToEdit) mandatoryFields.push('password');

    // Verificar que no existan errores activos
    const hasErrors = Object.values(errors).some(err => !!err);
    if (hasErrors) return false;

    // Verificar que los campos requeridos tengan contenido válido
    return mandatoryFields.every(field => {
      const value = formData[field];
      return value && validateField(field, value) === '';
    });
  }, [errors, formData, collaboratorToEdit]);

  const handleSubmit = (e) => {
    e.preventDefault();

    // Al editar, solo se revalidan los campos que el usuario realmente tocó/cambió,
    // para no bloquear el guardado por datos legados que ya no pasan las reglas actuales.
    // Al crear un colaborador nuevo, se validan todos los campos.
    const fieldsToValidate = collaboratorToEdit
      ? Object.keys(formData).filter(key => touched[key])
      : Object.keys(formData);

    const currentErrors = {};
    fieldsToValidate.forEach(key => {
      const error = validateField(key, formData[key]);
      if (error) currentErrors[key] = error;
    });

    if (Object.keys(currentErrors).length > 0) {
      setErrors(currentErrors);
      const allTouched = {};
      fieldsToValidate.forEach(key => { allTouched[key] = true; });
      setTouched(prev => ({ ...prev, ...allTouched }));
      toast.error('Por favor, revisa los errores en el formulario');
      return;
    }

    const payload = {
      usuarioRun: formData.usuarioRun.replace(/\./g, ''),
      usuarioNombre: formData.usuarioNombre.trim(),
      usuarioApellidos: formData.usuarioApellidos.trim(),
      usuarioEmail: formData.usuarioEmail.trim(),
      usuarioPassword: formData.password || undefined,
      passwordChangeConsent: formData.password ? formData.passwordChangeConsent : undefined,
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
        
        {/* Botón Cerrar */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-50 rounded-xl transition-all"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Cabecera */}
        <div className="mb-6">
          <h2 className="text-xl font-bold text-slate-900">
            {collaboratorToEdit ? 'Editar Perfil del Colaborador' : 'Registrar Nuevo Colaborador'}
          </h2>
          <p className="text-xs text-[#635bff] font-semibold mt-0.5 uppercase tracking-wider">
            Módulo de Personal e Identidad corporativa
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6 flex-1">
          
          {/* SECCIÓN 1: IDENTIDAD */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Identidad y Contacto</span>
              <div className="h-px flex-1 bg-slate-100"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {/* Nombre */}
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

              {/* Apellidos */}
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

              {/* RUN */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">RUN *</label>
                <div className="relative">
                  <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs font-bold text-slate-400">N°</span>
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

              {/* Email */}
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

              {/* Teléfono */}
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

              {/* Fecha Nacimiento */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Fecha de Nacimiento</label>
                <div className="relative">
                  <Calendar className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  <input
                    type="date"
                    name="fechaNacimiento"
                    value={formData.fechaNacimiento}
                    onChange={handleChange}
                    className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* SECCIÓN 2: UBICACIÓN */}
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

              {/* Región */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Región</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="region"
                    value={formData.region}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.region && touched.region ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="Ej: Valparaíso"
                  />
                </div>
                <FieldError name="region" />
              </div>

              {/* Comuna */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 mb-1.5">Comuna</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                  <input
                    name="comuna"
                    value={formData.comuna}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`w-full pl-10 pr-4 py-2.5 bg-slate-50 border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 ${
                      errors.comuna && touched.comuna ? 'border-rose-400 focus:border-rose-500' : 'border-slate-200 focus:border-[#635bff]'
                    }`}
                    placeholder="Ej: Quilpué"
                  />
                </div>
                <FieldError name="comuna" />
              </div>
            </div>
          </div>

          {/* SECCIÓN 3: CARGO Y SEGURIDAD */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Cargo y Seguridad</span>
              <div className="h-px flex-1 bg-slate-100"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {/* Área */}
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

              {/* Rol */}
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

              {/* Contraseña */}
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

          {/* ESTADO ACTIVACIÓN */}
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

          {/* Botón de envío */}
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