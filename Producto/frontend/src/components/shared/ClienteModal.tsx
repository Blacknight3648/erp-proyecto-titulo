"use client";

import { useState, useMemo, useEffect } from "react";
import {
  X,
  User,
  Tag,
  Mail,
  Phone,
  MapPin,
  Briefcase,
  FileText,
  AlertCircle,
} from "lucide-react";

import { toast } from "sonner";

import {
  validateRUN,
  formatRUN,
  validateEmail,
  validatePhone,
} from "../../utils/validations";

import { useGiros } from "../../hooks/useGiros";

export default function ClienteModal({ onClose, onSave, clienteToEdit = null }) {
  const { giros } = useGiros();

  const initialForm = {
    razonSocial: "",
    runCliente: "",
    correoCliente: "",
    telefonoCliente: "",
    direccionCliente: "",
    contactoCliente: "",
    sigla: "",
    giroId: "",
    activo: true,
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (clienteToEdit) {
      setFormData({
        razonSocial: clienteToEdit.razonSocial || "",
        runCliente: formatRUN(clienteToEdit.runCliente) || "",
        correoCliente: clienteToEdit.correoCliente || "",
        telefonoCliente:
          clienteToEdit.telefonoCliente?.replace("+56", "").trim() || "",
        direccionCliente: clienteToEdit.direccionCliente || "",
        contactoCliente: clienteToEdit.contactoCliente || "",
        sigla: typeof clienteToEdit.sigla === "string" ? clienteToEdit.sigla : (clienteToEdit.sigla?.nombre || ""),
        giroId: clienteToEdit.giro?.giroId?.toString() || "",
        activo: clienteToEdit.activo ?? true,
      });
    } else {
      setFormData(initialForm);
    }
  }, [clienteToEdit]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    let finalValue = value;
    const newErrors = { ...errors };

    if (name === "runCliente") {
      const clean = value.replace(/[^0-9Kk]/g, "");
      finalValue = formatRUN(clean);
      if (clean.length > 0 && !validateRUN(clean)) {
        newErrors[name] = "El RUN ingresado no es válido";
      } else if (clean.length > 0 && clean.length < 8) {
        newErrors[name] = "RUN demasiado corto";
      } else {
        delete newErrors[name];
      }
    } else if (name === "telefonoCliente") {
      const clean = value.replace(/\D/g, "").slice(0, 9);
      finalValue = clean
        .replace(/(\d{1})(\d{4})(\d{4})/, "$1 $2 $3")
        .replace(/(\d{1})(\d{4})/, "$1 $2")
        .trim();
      const phoneError = validatePhone(clean);
      if (phoneError) newErrors[name] = phoneError;
      else delete newErrors[name];
    } else if (name === "correoCliente") {
      finalValue = value.toLowerCase();
      if (value && !validateEmail(value)) {
        newErrors[name] = "Formato de correo inválido";
      } else {
        delete newErrors[name];
      }
    } else if (name === "razonSocial") {
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
    try {
      const runClean = (formData.runCliente || "").replace(/[^0-9Kk]/g, "");
      const requiredFields =
        (formData.razonSocial?.trim().length || 0) >= 3 &&
        validateRUN(runClean) &&
        !!formData.sigla &&
        !!formData.giroId;
      return Object.keys(errors).length === 0 && requiredFields;
    } catch (err) {
      console.error("Error validando ClienteModal:", err);
      return false;
    }
  }, [errors, formData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!isValid) {
      toast.error("Revisa los campos marcados en rojo");
      return;
    }

    const payload = {
      razonSocial: formData.razonSocial,
      runCliente: formData.runCliente.replace(/\./g, ""),
      correoCliente: formData.correoCliente,
      telefonoCliente: formData.telefonoCliente
        ? `+56${formData.telefonoCliente.replace(/\s/g, "")}`
        : "",
      direccionCliente: formData.direccionCliente,
      contactoCliente: formData.contactoCliente,
      activo: formData.activo,
      sigla: formData.sigla.trim(),
      giro: formData.giroId ? { giroId: Number(formData.giroId) } : null,
    };

    onSave(payload);
    toast.success("Cliente procesado correctamente");
  };

  const ErrorMsg = ({ name }) =>
    errors[name] ? (
      <div className="flex items-center gap-1 mt-2 ml-2 text-rose-500">
        <AlertCircle size={12} />
        <span className="text-[10px] font-bold uppercase tracking-wider">
          {errors[name]}
        </span>
      </div>
    ) : null;

  const inputClass = (err) =>
    `w-full pl-14 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
      err
        ? "border-rose-500 bg-rose-50/30"
        : "border-transparent focus:bg-white focus:border-indigo-500"
    }`;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-md">
      <div className="bg-white w-full max-w-2xl rounded-[3rem] p-10 shadow-2xl relative max-h-[90vh] overflow-y-auto">
        <button
          onClick={onClose}
          className="absolute top-8 right-8 p-3 bg-gray-50 rounded-2xl hover:bg-black hover:text-white transition"
        >
          <X size={20} />
        </button>

        <div className="mb-10">
          <h2 className="text-4xl font-black text-gray-800 mb-3">
            {clienteToEdit ? "Editar Cliente" : "Nuevo Cliente"}
          </h2>
          <div className="text-[10px] font-black text-indigo-500 uppercase tracking-[0.2em] flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-indigo-500 animate-pulse"></span>
            Gestión de Registro de Clientes
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-8">
          <div className="grid grid-cols-2 gap-6">
            {/* Razón Social */}
            <div className="col-span-2 space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Razón Social *
              </label>
              <div className="relative">
                <User className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.razonSocial ? "text-rose-500" : "text-gray-400"}`} />
                <input
                  name="razonSocial"
                  value={formData.razonSocial}
                  onChange={handleChange}
                  className={inputClass(errors.razonSocial)}
                  placeholder="Ej: Comercial Valparaíso SPA"
                />
              </div>
              <ErrorMsg name="razonSocial" />
            </div>

            {/* RUN */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                RUN *
              </label>
              <div className="relative">
                <span className={`absolute left-5 top-1/2 -translate-y-1/2 text-sm font-black ${errors.runCliente ? "text-rose-500" : "text-gray-400"}`}>
                  ID
                </span>
                <input
                  name="runCliente"
                  value={formData.runCliente}
                  onChange={handleChange}
                  disabled={!!clienteToEdit}
                  className={inputClass(errors.runCliente)}
                  placeholder="12.345.678-9"
                />
              </div>
              <ErrorMsg name="runCliente" />
            </div>

            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Sigla *
              </label>
              <div className="relative">
                <Tag className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  name="sigla"
                  value={formData.sigla}
                  onChange={handleChange}
                  className={inputClass(false)}
                  placeholder="Ej: S.P.A."
                />
              </div>
            </div>

            {/* Giro (select) */}
            <div className="col-span-2 space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Giro *
              </label>
              <div className="relative">
                <Briefcase className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <select
                  name="giroId"
                  value={formData.giroId}
                  onChange={handleChange}
                  className={inputClass(false)}
                >
                  <option value="">Seleccionar giro...</option>
                  {giros.map((g) => (
                    <option key={g.giroId} value={g.giroId}>
                      {g.descripcionGiro}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            {/* Email */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Email
              </label>
              <div className="relative">
                <Mail className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.correoCliente ? "text-rose-500" : "text-gray-400"}`} />
                <input
                  name="correoCliente"
                  value={formData.correoCliente}
                  onChange={handleChange}
                  className={inputClass(errors.correoCliente)}
                  placeholder="contacto@empresa.cl"
                />
              </div>
              <ErrorMsg name="correoCliente" />
            </div>

            {/* Teléfono */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Teléfono
              </label>
              <div className="relative">
                <Phone className={`absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 ${errors.telefonoCliente ? "text-rose-500" : "text-gray-400"}`} />
                <span className="absolute left-12 top-1/2 -translate-y-1/2 text-sm font-black text-gray-400">
                  +56
                </span>
                <input
                  name="telefonoCliente"
                  value={formData.telefonoCliente}
                  onChange={handleChange}
                  className={`w-full pl-24 pr-6 py-5 bg-gray-50 rounded-[1.5rem] border-2 transition-all outline-none font-bold text-sm ${
                    errors.telefonoCliente
                      ? "border-rose-500 bg-rose-50/30"
                      : "border-transparent focus:bg-white focus:border-indigo-500"
                  }`}
                  placeholder="9 1234 5678"
                />
              </div>
              <ErrorMsg name="telefonoCliente" />
            </div>

            {/* Contacto */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Persona de Contacto
              </label>
              <div className="relative">
                <Briefcase className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  name="contactoCliente"
                  value={formData.contactoCliente}
                  onChange={handleChange}
                  className={inputClass(false)}
                  placeholder="Nombre del responsable"
                />
              </div>
            </div>

            {/* Dirección */}
            <div className="space-y-2">
              <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">
                Dirección
              </label>
              <div className="relative">
                <MapPin className="absolute left-5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  name="direccionCliente"
                  value={formData.direccionCliente}
                  onChange={handleChange}
                  className={inputClass(false)}
                  placeholder="Ciudad, Calle #123"
                />
              </div>
            </div>
          </div>

          <button
            type="submit"
            disabled={!isValid}
            className={`w-full py-6 bg-slate-800 text-white rounded-[2rem] text-xs font-black uppercase tracking-[0.3em] flex items-center justify-center transition shadow-xl ${
              !isValid
                ? "opacity-30 cursor-not-allowed"
                : "hover:bg-indigo-600 hover:-translate-y-1 active:scale-95"
            }`}
          >
            <FileText className="w-5 h-5 mr-3" />
            {clienteToEdit ? "Actualizar Cliente" : "Guardar Nuevo Cliente"}
          </button>
        </form>
      </div>
    </div>
  );
}
