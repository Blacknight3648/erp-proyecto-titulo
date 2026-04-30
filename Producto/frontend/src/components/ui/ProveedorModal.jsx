"use client";

import React, { useState, useMemo, useEffect } from "react";
import { X, User, Tag, Mail, Phone, MapPin, Briefcase, FileText, AlertCircle } from "lucide-react";
import { toast } from "sonner";
import { validateRUN, formatRUN } from "../../utils/validations";

export default function ProveedorModal({ onClose, onSave, proveedorToEdit = null }) {
  const initialForm = {
    nombreProveedor: "",
    rutProveedor: "",
    direccionProveedor: "",
    telefonoProveedor: "",
    emailProveedor: "",
    contactoProveedor: "",
    categoria: "",
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (proveedorToEdit) {
      // Al editar, formateamos el RUT y teléfono que vienen de la DB
      setFormData({
        ...proveedorToEdit,
        rutProveedor: formatRUN(proveedorToEdit.rutProveedor) || "",
        telefonoProveedor: proveedorToEdit.telefonoProveedor?.replace("+56", "").trim() || "",
      });
    } else {
      setFormData(initialForm);
    }
  }, [proveedorToEdit]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    let finalValue = value;
    const newErrors = { ...errors };

    // --- LÓGICA POR CAMPO ---

    if (name === "rutProveedor") {
      // Limpiar y formatear RUT
      const clean = value.replace(/[^0-9Kk]/g, "");
      finalValue = formatRUN(clean);
      
      if (clean.length > 0 && !validateRUN(clean)) {
        newErrors[name] = "El RUT ingresado no es válido (DV incorrecto)";
      } else if (clean.length > 0 && clean.length < 8) {
        newErrors[name] = "RUT demasiado corto";
      } else {
        delete newErrors[name];
      }
    }

    else if (name === "telefonoProveedor") {
      // Solo números, máx 9 dígitos (formato chileno: 9 1234 5678)
      const clean = value.replace(/\D/g, "").slice(0, 9);
      // Formatear visualmente: X XXXX XXXX
      finalValue = clean
        .replace(/(\d{1})(\d{4})(\d{4})/, "$1 $2 $3")
        .replace(/(\d{1})(\d{4})/, "$1 $2")
        .trim();

      if (clean.length > 0 && clean.length < 9) {
        newErrors[name] = "Deben ser 9 dígitos (Ej: 9 1234 5678)";
      } else if (clean.length > 0 && !clean.startsWith("9")) {
        newErrors[name] = "El número debe comenzar con 9";
      } else {
        delete newErrors[name];
      }
    }

    else if (name === "emailProveedor") {
      finalValue = value.toLowerCase();
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (value && !emailRegex.test(value)) {
        newErrors[name] = "Formato de correo electrónico inválido";
      } else {
        delete newErrors[name];
      }
    }

    else if (name === "nombreProveedor" || name === "categoria") {
      if (value.trim().length > 0 && value.trim().length < 3) {
        newErrors[name] = "Debe tener al menos 3 caracteres";
      } else {
        delete newErrors[name];
      }
    }

    setFormData((prev) => ({ ...prev, [name]: finalValue }));
    setErrors(newErrors);
  };

  const isValid = useMemo(() => {
    const requiredFields = 
      formData.nombreProveedor.length >= 3 &&
      validateRUN(formData.rutProveedor.replace(/[^0-9Kk]/g, "")) &&
      formData.categoria.length >= 2;
    
    return Object.keys(errors).length === 0 && requiredFields;
  }, [errors, formData]);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!isValid) {
      toast.error("Por favor, revisa los campos marcados en rojo");
      return;
    }

    // Limpieza final para guardar
    const payload = {
      ...formData,
      rutProveedor: formData.rutProveedor.replace(/\./g, ""), // Guardar sin puntos para consistencia
      telefonoProveedor: `+56${formData.telefonoProveedor.replace(/\s/g, "")}`,
    };

    onSave(payload);
    toast.success("Datos procesados correctamente");
  };

  // Componente de error reutilizable
  const ErrorMsg = ({ name }) => errors[name] ? (
    <div className="flex items-center gap-1 mt-2 ml-2 text-rose-500">
      <AlertCircle size={12} />
      <span className="text-[10px] font-bold uppercase tracking-wider">{errors[name]}</span>
    </div>
  ) : null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-md">
      <div className="bg-white w-full max-w-2xl rounded-[3rem] p-10 shadow-2xl relative max-h-[90vh] overflow-y-auto">
        
        <button onClick={onClose} className="absolute top-8 right-8 p-3 bg-gray-50 rounded-2xl hover:bg-black hover:text-white transition">
          <X size={20} />
        </button>

        <div className="mb-10">
          <h2 className="text-4xl font-black text-gray-800 mb-3">
            {proveedorToEdit ? "Editar Proveedor" : "Nuevo Proveedor"}
          </h2>
          <div className="text-[10px] font-black text-indigo-500 uppercase tracking-[0.2em] flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-indigo-500 animate-pulse"></span>
            Gestión de Registro de Proveedores
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-8">
          
          <div className="grid grid-cols-2 gap-6">
            {/* Nombre */}
            <div className="col-span-2 space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Nombre Comercial *</label>
              <div className="relative">
                <User className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.nombreProveedor ? "text-rose-500" : "text-gray-400"}`} />
                <input
                  name="nombreProveedor"
                  value={formData.nombreProveedor}
                  onChange={handleChange}
                  className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.nombreProveedor ? "border-rose-500 bg-rose-50/30" : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="Ej: Transportes Integral"
                />
              </div>
              <ErrorMsg name="nombreProveedor" />
            </div>

            {/* RUT */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">RUT *</label>
              <div className="relative">
                <span className={`absolute left-5 top-1/2 -translate-y-1/2 text-sm font-black ${errors.rutProveedor ? "text-rose-500" : "text-gray-400"}`}>ID</span>
                <input
                  name="rutProveedor"
                  value={formData.rutProveedor}
                  onChange={handleChange}
                  className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.rutProveedor ? "border-rose-500 bg-rose-50/30" : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="12.345.678-9"
                />
              </div>
              <ErrorMsg name="rutProveedor" />
            </div>

            {/* Categoría */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Categoría *</label>
              <div className="relative">
                <Tag className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.categoria ? "text-rose-500" : "text-gray-400"}`} />
                <input
                  name="categoria"
                  value={formData.categoria}
                  onChange={handleChange}
                  className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.categoria ? "border-rose-500 bg-rose-50/30" : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="Ej: Logística"
                />
              </div>
              <ErrorMsg name="categoria" />
            </div>

            {/* Email */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Email</label>
              <div className="relative">
                <Mail className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.emailProveedor ? "text-rose-500" : "text-gray-400"}`} />
                <input
                  name="emailProveedor"
                  type="email"
                  value={formData.emailProveedor}
                  onChange={handleChange}
                  className={`w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.emailProveedor ? "border-rose-500 bg-rose-50/30" : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="mail@ejemplo.cl"
                />
              </div>
              <ErrorMsg name="emailProveedor" />
            </div>

            {/* Teléfono con +56 FIJO */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Teléfono Móvil</label>
              <div className="relative">
                <Phone className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.telefonoProveedor ? "text-rose-500" : "text-gray-400"}`} />
                {/* Prefijo Visual */}
                <span className="absolute left-12 top-1/2 -translate-y-1/2 text-sm font-black text-gray-400 select-none">
                  +56
                </span>
                <input
                  name="telefonoProveedor"
                  value={formData.telefonoProveedor}
                  onChange={handleChange}
                  className={`w-full pl-24 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.telefonoProveedor ? "border-rose-500 bg-rose-50/30" : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="9 1234 5678"
                />
              </div>
              <ErrorMsg name="telefonoProveedor" />
            </div>

            {/* Contacto Directo */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Persona de Contacto</label>
              <div className="relative">
                <Briefcase className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  name="contactoProveedor"
                  value={formData.contactoProveedor}
                  onChange={handleChange}
                  className="w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-indigo-500 outline-none font-bold text-sm transition-all"
                  placeholder="Nombre del ejecutivo"
                />
              </div>
            </div>

            {/* Dirección */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Dirección Física</label>
              <div className="relative">
                <MapPin className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  name="direccionProveedor"
                  value={formData.direccionProveedor}
                  onChange={handleChange}
                  className="w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 border-transparent focus:bg-white focus:border-indigo-500 outline-none font-bold text-sm transition-all"
                  placeholder="Ciudad, Calle #123"
                />
              </div>
            </div>
          </div>

          <button
            type="submit"
            disabled={!isValid}
            className={`w-full py-6 bg-slate-800 text-white rounded-[2rem] text-xs font-black uppercase tracking-[0.3em] flex items-center justify-center transition shadow-xl ${
              !isValid ? "opacity-30 cursor-not-allowed" : "hover:bg-indigo-600 hover:-translate-y-1 active:scale-95"
            }`}
          >
            <FileText className="w-5 h-5 mr-3" />
            {proveedorToEdit ? "Actualizar Proveedor" : "Guardar Nuevo Proveedor"}
          </button>
        </form>
      </div>
    </div>
  );
}