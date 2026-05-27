"use client";

import React, { useState, useMemo, useEffect } from 'react';
import { X, Save, User, Hash, Mail, Phone, MapPin, Shield, CheckCircle, AlertCircle, Calendar, Home } from 'lucide-react';
import { toast } from 'sonner';
import { validateRUN, formatRUN } from '../../../utils/validations';
import { useRoles } from '../../../hooks/useRoles';
import { useAreas } from '../../../hooks/useAreas';

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

    useEffect(() => {
        if (collaboratorToEdit) {
            setFormData({
                usuarioNombre: collaboratorToEdit.usuarioNombre || '',
                usuarioApellidos: collaboratorToEdit.usuarioApellidos || '',
                usuarioRun: formatRUN(collaboratorToEdit.usuarioRun) || '',
                usuarioEmail: collaboratorToEdit.usuarioEmail || '',
                telefono: (collaboratorToEdit.telefono || '').replace('+56', '').trim(),
                fechaNacimiento: collaboratorToEdit.fechaNacimiento || '',
                direccion: collaboratorToEdit.direccion || '',
                region: collaboratorToEdit.region || '',
                comuna: collaboratorToEdit.comuna || '',
                // Resolver área y rol desde arrays del backend
                area: collaboratorToEdit.areas?.[0]?.nombre || collaboratorToEdit.area || '',
                rol: collaboratorToEdit.roles?.[0]?.nombre || collaboratorToEdit.rol || '',
                password: '',
                activo: collaboratorToEdit.enabled ?? collaboratorToEdit.activo ?? true,
            });
        } else {
            setFormData(initialForm);
        }
    }, [collaboratorToEdit]);

    const filteredRoles = useMemo(() => {
        if (!formData.area) return [];
        const selectedAreaObj = areas.find(a => a.nombre === formData.area);
        const selectedAreaId = selectedAreaObj?.areaId;
        if (!selectedAreaId) return roles; // si no hay área en el backend, mostrar todos
        return roles.filter(r => r.areaId === selectedAreaId);
    }, [roles, formData.area, areas]);

    // Auto-selección de rol único
    useEffect(() => {
        if (filteredRoles.length === 1 && formData.rol !== filteredRoles[0].nombre) {
            setFormData(prev => ({ ...prev, rol: filteredRoles[0].nombre }));
        }
    }, [filteredRoles]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        let finalValue = type === 'checkbox' ? checked : value;
        const newErrors = { ...errors };

        if (name === 'usuarioRun') {
            const clean = value.replace(/[^0-9Kk]/g, '');
            finalValue = formatRUN(clean);
            if (clean.length > 0 && !validateRUN(clean)) newErrors[name] = 'RUN inválido (DV incorrecto)';
            else if (clean.length > 0 && clean.length < 8) newErrors[name] = 'RUN demasiado corto';
            else delete newErrors[name];
        } else if (name === 'telefono') {
            const clean = value.replace(/\D/g, '').slice(0, 9);
            finalValue = clean.replace(/(\d{1})(\d{4})(\d{4})/, '$1 $2 $3').replace(/(\d{1})(\d{4})/, '$1 $2').trim();
            if (clean.length > 0 && (clean.length < 9 || !clean.startsWith('9'))) newErrors[name] = 'Debe empezar con 9 y tener 9 dígitos';
            else delete newErrors[name];
        } else if (name === 'usuarioEmail') {
            finalValue = value.toLowerCase();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (value && !emailRegex.test(value)) newErrors[name] = 'Formato de correo no válido';
            else delete newErrors[name];
        } else if (['usuarioNombre', 'usuarioApellidos', 'region', 'comuna'].includes(name)) {
            if (value.trim().length > 0 && value.trim().length < 2) newErrors[name] = 'Mínimo 2 caracteres';
            else delete newErrors[name];
        } else if (name === 'password' && !collaboratorToEdit) {
            if (value.length > 0 && value.length < 4) newErrors[name] = 'La clave debe tener al menos 4 caracteres';
            else delete newErrors[name];
        }

        setFormData(prev => ({ ...prev, [name]: finalValue }));
        setErrors(newErrors);
    };

    const isValid = useMemo(() => {
        const fieldsOk =
            formData.usuarioNombre.trim().length >= 2 &&
            formData.usuarioApellidos.trim().length >= 2 &&
            validateRUN(formData.usuarioRun.replace(/[^0-9Kk]/g, '')) &&
            formData.usuarioEmail.includes('@') &&
            formData.region.trim() !== '' &&
            formData.comuna.trim() !== '';
        const passwordOk = collaboratorToEdit ? true : formData.password.length >= 4;
        return Object.keys(errors).length === 0 && fieldsOk && passwordOk;
    }, [errors, formData, collaboratorToEdit]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!isValid) {
            toast.error('Revisa los errores en el formulario');
            return;
        }

        // Build the payload exactly matching CreateUserDTO
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

    const FieldError = ({ name }) => errors[name] ? (
        <div className="flex items-center gap-1 mt-1.5 ml-2 text-rose-500 animate-in slide-in-from-left-1">
            <AlertCircle size={10} />
            <span className="text-[9px] font-black uppercase tracking-tighter">{errors[name]}</span>
        </div>
    ) : null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/70 backdrop-blur-md animate-in fade-in duration-300">
            <div className="bg-white w-full max-w-2xl rounded-[3.5rem] p-12 shadow-[0_35px_60px_-15px_rgba(0,0,0,0.3)] relative max-h-[90vh] overflow-y-auto">

                <button
                    onClick={onClose}
                    className="absolute top-10 right-10 p-3 bg-gray-50 rounded-2xl hover:bg-rose-500 hover:text-white transition-all duration-300"
                >
                    <X size={20} />
                </button>

                <div className="mb-12">
                    <h2 className="text-4xl font-black text-gray-800 tracking-tighter mb-2">
                        {collaboratorToEdit ? 'Editar Perfil' : 'Alta de Colaborador'}
                    </h2>
                    <div className="flex items-center gap-2">
                        <div className="w-2 h-2 rounded-full bg-blue-600 animate-pulse"></div>
                        <span className="text-[10px] font-black text-blue-600 uppercase tracking-[0.3em]">
                            SISTEMA CENTRAL DE RECURSOS HUMANOS
                        </span>
                    </div>
                </div>

                <form onSubmit={handleSubmit} className="space-y-10">

                    {/* SECCIÓN 1: IDENTIDAD */}
                    <div className="space-y-6">
                        <div className="flex items-center gap-4">
                            <span className="text-[10px] font-black text-gray-300 uppercase tracking-widest whitespace-nowrap">Identidad y Contacto</span>
                            <div className="h-px w-full bg-gray-100"></div>
                        </div>

                        <div className="grid grid-cols-2 gap-6">
                            {/* Nombre */}
                            <div className="col-span-2 md:col-span-1 space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Nombre *</label>
                                <div className="relative">
                                    <User className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.usuarioNombre ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="usuarioNombre"
                                        value={formData.usuarioNombre}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.usuarioNombre ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="Ej: Juan"
                                    />
                                </div>
                                <FieldError name="usuarioNombre" />
                            </div>

                            {/* Apellidos */}
                            <div className="col-span-2 md:col-span-1 space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Apellidos *</label>
                                <div className="relative">
                                    <User className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.usuarioApellidos ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="usuarioApellidos"
                                        value={formData.usuarioApellidos}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.usuarioApellidos ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="Ej: Pérez González"
                                    />
                                </div>
                                <FieldError name="usuarioApellidos" />
                            </div>

                            {/* RUN */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">RUN *</label>
                                <div className="relative">
                                    <span className={`absolute left-5 top-1/2 -translate-y-1/2 text-[10px] font-black ${errors.usuarioRun ? 'text-rose-500' : 'text-gray-400'}`}>RUN</span>
                                    <input
                                        name="usuarioRun"
                                        value={formData.usuarioRun}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.usuarioRun ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="12.345.678-9"
                                    />
                                </div>
                                <FieldError name="usuarioRun" />
                            </div>

                            {/* Email */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Email Corporativo *</label>
                                <div className="relative">
                                    <Mail className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.usuarioEmail ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="usuarioEmail"
                                        value={formData.usuarioEmail}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.usuarioEmail ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="juan@empresa.cl"
                                    />
                                </div>
                                <FieldError name="usuarioEmail" />
                            </div>

                            {/* Teléfono */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Teléfono Móvil</label>
                                <div className="relative">
                                    <Phone className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.telefono ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <span className="absolute left-12 top-1/2 -translate-y-1/2 text-sm font-black text-gray-400 select-none">+56</span>
                                    <input
                                        name="telefono"
                                        value={formData.telefono}
                                        onChange={handleChange}
                                        className={`w-full pl-24 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.telefono ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="9 1234 5678"
                                    />
                                </div>
                                <FieldError name="telefono" />
                            </div>

                            {/* Fecha de Nacimiento */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Fecha de Nacimiento</label>
                                <div className="relative">
                                    <Calendar className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                    <input
                                        type="date"
                                        name="fechaNacimiento"
                                        value={formData.fechaNacimiento}
                                        onChange={handleChange}
                                        className="w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-blue-500 transition-all font-bold text-sm outline-none"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* SECCIÓN 2: UBICACIÓN */}
                    <div className="space-y-6">
                        <div className="flex items-center gap-4">
                            <span className="text-[10px] font-black text-gray-300 uppercase tracking-widest whitespace-nowrap">Ubicación</span>
                            <div className="h-px w-full bg-gray-100"></div>
                        </div>

                        <div className="grid grid-cols-2 gap-6">
                            {/* Dirección */}
                            <div className="col-span-2 space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Dirección</label>
                                <div className="relative">
                                    <Home className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                    <input
                                        name="direccion"
                                        value={formData.direccion}
                                        onChange={handleChange}
                                        className="w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-blue-500 transition-all font-bold text-sm outline-none"
                                        placeholder="Ej: Las Rosas 458"
                                    />
                                </div>
                            </div>

                            {/* Región */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Región *</label>
                                <div className="relative">
                                    <MapPin className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.region ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="region"
                                        value={formData.region}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.region ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="Ej: Valparaíso"
                                    />
                                </div>
                                <FieldError name="region" />
                            </div>

                            {/* Comuna */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Comuna *</label>
                                <div className="relative">
                                    <MapPin className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.comuna ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="comuna"
                                        value={formData.comuna}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.comuna ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="Ej: Quilpué"
                                    />
                                </div>
                                <FieldError name="comuna" />
                            </div>
                        </div>
                    </div>

                    {/* SECCIÓN 3: CARGO Y ACCESO */}
                    <div className="space-y-6">
                        <div className="flex items-center gap-4">
                            <span className="text-[10px] font-black text-gray-300 uppercase tracking-widest whitespace-nowrap">Cargo y Seguridad</span>
                            <div className="h-px w-full bg-gray-100"></div>
                        </div>

                        <div className="grid grid-cols-2 gap-6">
                            {/* Área */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Área Asignada</label>
                                <div className="relative">
                                    <MapPin className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                    <select
                                        name="area"
                                        value={formData.area}
                                        onChange={handleChange}
                                        className="w-full pl-14 pr-8 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-blue-500 outline-none font-bold text-sm appearance-none transition-all cursor-pointer"
                                    >
                                        <option value="">— Sin área —</option>
                                        {areas.map(a => (
                                            <option key={a.areaId} value={a.nombre}>{a.nombre}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>

                            {/* Rol */}
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Rol de Usuario</label>
                                <div className="relative">
                                    <Shield className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                    <select
                                        name="rol"
                                        value={formData.rol}
                                        onChange={handleChange}
                                        className="w-full pl-14 pr-8 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-blue-500 outline-none font-bold text-sm appearance-none transition-all cursor-pointer"
                                    >
                                        <option value="">— Sin rol —</option>
                                        {(formData.area ? filteredRoles : roles).map(r => (
                                            <option key={r.id} value={r.nombre}>{r.nombre}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>

                            {/* Contraseña */}
                            <div className="col-span-2 space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                                    {collaboratorToEdit ? 'Nueva Contraseña (Opcional)' : 'Contraseña de Acceso *'}
                                </label>
                                <div className="relative">
                                    <Hash className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.password ? 'text-rose-500' : 'text-gray-400'}`} />
                                    <input
                                        name="password"
                                        type="password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all font-bold text-sm outline-none ${errors.password ? 'border-rose-500 bg-rose-50/20' : 'border-transparent focus:bg-white focus:border-blue-500'}`}
                                        placeholder="••••••••••••"
                                    />
                                </div>
                                <FieldError name="password" />
                            </div>
                        </div>
                    </div>

                    {/* ESTADO ACTIVACIÓN */}
                    <div className="flex items-center justify-between p-8 bg-slate-50 rounded-[2.5rem] border-2 border-dashed border-gray-200 group hover:border-blue-500 transition-colors">
                        <div className="flex items-center gap-5">
                            <div className={`w-14 h-14 rounded-2xl flex items-center justify-center transition-all ${formData.activo ? 'bg-blue-600 text-white shadow-xl shadow-blue-200' : 'bg-gray-200 text-gray-400'}`}>
                                <CheckCircle size={28} />
                            </div>
                            <div>
                                <h4 className="text-sm font-black text-gray-800 uppercase tracking-tighter">Acceso al Sistema</h4>
                                <p className="text-[10px] font-bold text-gray-400 uppercase">Habilitar o suspender cuenta</p>
                            </div>
                        </div>
                        <label className="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" name="activo" checked={formData.activo} onChange={handleChange} className="sr-only peer" />
                            <div className="w-16 h-9 bg-gray-300 rounded-full peer peer-checked:bg-blue-600 transition-all after:content-[''] after:absolute after:top-[4px] after:left-[4px] after:bg-white after:rounded-full after:h-7 after:w-7 after:transition-all peer-checked:after:translate-x-full shadow-inner"></div>
                        </label>
                    </div>

                    <button
                        type="submit"
                        disabled={!isValid}
                        className={`w-full py-7 bg-slate-900 text-white rounded-[2rem] text-[11px] font-black uppercase tracking-[0.4em] flex items-center justify-center gap-3 transition-all duration-300 shadow-2xl ${!isValid ? 'opacity-30 cursor-not-allowed grayscale' : 'hover:bg-blue-600 hover:-translate-y-1 active:scale-95 shadow-blue-100'}`}
                    >
                        <Save size={20} />
                        {collaboratorToEdit ? 'Actualizar Colaborador' : 'Registrar Colaborador'}
                    </button>
                </form>
            </div>
        </div>
    );
}