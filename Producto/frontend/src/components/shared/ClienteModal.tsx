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
  Save,
  AlertCircle,
  CheckCircle,
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
  const [errors, setErrors] = useState<Record<string, string>>({});

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

  const FieldError = ({ name }) =>
    errors[name] ? (
      <div className="flex items-center gap-1.5 mt-1.5 text-xs font-medium text-destructive">
        <AlertCircle className="w-3.5 h-3.5 shrink-0" />
        <span>{errors[name]}</span>
      </div>
    ) : null;

  const inputClass = (err) =>
    `w-full pl-10 pr-4 py-2.5 bg-muted border rounded-xl text-sm transition-all outline-none focus:ring-2 focus:ring-brand-indigo/20 ${
      err
        ? "border-destructive focus:border-destructive"
        : "border-border focus:border-brand-indigo"
    }`;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 overlay-backdrop">
      <div className="bg-card w-full max-w-2xl rounded-2xl shadow-xl border border-border relative max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between p-6 border-b border-border">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-brand-indigo/10 rounded-xl text-brand-indigo">
              <User size={20} />
            </div>
            <div>
              <h2 className="text-lg font-bold text-foreground">
                {clienteToEdit ? "Editar Cliente" : "Nuevo Cliente"}
              </h2>
              <p className="text-xs text-brand-indigo font-semibold uppercase tracking-wider">
                Gestión de Registro de Clientes
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-all"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* SECCIÓN 1: Identificación */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-muted-foreground uppercase tracking-wider">Identificación</span>
              <div className="h-px flex-1 bg-muted"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Razón Social *</label>
                <div className="relative">
                  <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="razonSocial"
                    value={formData.razonSocial}
                    onChange={handleChange}
                    className={inputClass(errors.razonSocial)}
                    placeholder="Ej: Comercial Valparaíso SPA"
                  />
                </div>
                <FieldError name="razonSocial" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">RUN *</label>
                <div className="relative">
                  <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs font-bold text-muted-foreground">ID</span>
                  <input
                    name="runCliente"
                    value={formData.runCliente}
                    onChange={handleChange}
                    disabled={!!clienteToEdit}
                    className={`${inputClass(errors.runCliente)} disabled:opacity-60 disabled:cursor-not-allowed`}
                    placeholder="12.345.678-9"
                  />
                </div>
                <FieldError name="runCliente" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Sigla *</label>
                <div className="relative">
                  <Tag className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="sigla"
                    value={formData.sigla}
                    onChange={handleChange}
                    className={inputClass(false)}
                    placeholder="Ej: S.P.A."
                  />
                </div>
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Giro *</label>
                <div className="relative">
                  <Briefcase className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground pointer-events-none" />
                  <select
                    name="giroId"
                    value={formData.giroId}
                    onChange={handleChange}
                    className={`${inputClass(false)} appearance-none cursor-pointer`}
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
            </div>
          </div>

          {/* SECCIÓN 2: Contacto */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-xs font-bold text-muted-foreground uppercase tracking-wider">Contacto</span>
              <div className="h-px flex-1 bg-muted"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="correoCliente"
                    value={formData.correoCliente}
                    onChange={handleChange}
                    className={inputClass(errors.correoCliente)}
                    placeholder="contacto@empresa.cl"
                  />
                </div>
                <FieldError name="correoCliente" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Teléfono</label>
                <div className="relative">
                  <Phone className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <span className="absolute left-10 top-1/2 -translate-y-1/2 text-sm font-semibold text-muted-foreground select-none">+56</span>
                  <input
                    name="telefonoCliente"
                    value={formData.telefonoCliente}
                    onChange={handleChange}
                    className={`w-full pl-18 pr-4 py-2.5 bg-muted border rounded-xl text-sm transition-all outline-none focus:ring-2 focus:ring-brand-indigo/20 ${
                      errors.telefonoCliente
                        ? "border-destructive focus:border-destructive"
                        : "border-border focus:border-brand-indigo"
                    }`}
                    placeholder="9 1234 5678"
                  />
                </div>
                <FieldError name="telefonoCliente" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Persona de Contacto</label>
                <div className="relative">
                  <Briefcase className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="contactoCliente"
                    value={formData.contactoCliente}
                    onChange={handleChange}
                    className={inputClass(false)}
                    placeholder="Nombre del responsable"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-muted-foreground mb-1.5">Dirección</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
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
          </div>

          {/* ESTADO */}
          <div className="flex items-center justify-between p-4 bg-muted rounded-xl border border-border">
            <div className="flex items-center gap-3">
              <div className={`w-9 h-9 rounded-lg flex items-center justify-center transition-all ${formData.activo ? 'bg-success-bg text-success' : 'bg-secondary text-muted-foreground'}`}>
                <CheckCircle className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-semibold text-foreground">Estado del Cliente</h4>
                <p className="text-[11px] text-muted-foreground">Permitir transacciones y operaciones del cliente</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                name="activo"
                checked={formData.activo}
                onChange={(e) => setFormData(prev => ({ ...prev, activo: e.target.checked }))}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-secondary rounded-full peer peer-checked:bg-success transition-all after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-card after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-full"></div>
            </label>
          </div>

          <button
            type="submit"
            disabled={!isValid}
            className={`w-full py-3 bg-brand-indigo hover:opacity-90 text-white rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all shadow-sm ${
              !isValid ? "opacity-40 cursor-not-allowed" : "hover:shadow-md active:scale-[0.98]"
            }`}
          >
            <Save className="w-4 h-4" />
            {clienteToEdit ? "Actualizar Cliente" : "Guardar Nuevo Cliente"}
          </button>
        </form>
      </div>
    </div>
  );
}
