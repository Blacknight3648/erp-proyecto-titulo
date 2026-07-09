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
import { validateRUN, formatRUN, validateEmail } from "../../utils/validations";
import { useGiros } from "../../hooks/useGiros";

interface ProveedorModalProps {
  onClose: () => void;
  onSave: (payload: any) => void;
  proveedorToEdit?: any;
}

export default function ProveedorModal({ onClose, onSave, proveedorToEdit = null }: ProveedorModalProps) {
  const { giros = [] } = useGiros();

  const initialForm = {
    razonSocialProveedor: "",
    runProveedor: "",
    direccionProveedor: "",
    telefonoProveedor: "",
    emailProveedor: "",
    contactoProveedor: "",
    tipoProveedor: "",
    sigla: "",
    giroId: "",
    activo: true,
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  useEffect(() => {
    if (proveedorToEdit) {
      setFormData({
        razonSocialProveedor: proveedorToEdit.razonSocialProveedor || "",
        runProveedor: formatRUN(proveedorToEdit.runProveedor) || "",
        direccionProveedor: proveedorToEdit.direccionProveedor || "",
        telefonoProveedor: proveedorToEdit.telefonoProveedor?.replace("+56", "").trim() || "",
        emailProveedor: proveedorToEdit.emailProveedor || "",
        contactoProveedor: proveedorToEdit.contactoProveedor || "",
        tipoProveedor: proveedorToEdit.tipoProveedor || "",
        sigla: typeof proveedorToEdit.sigla === "string" ? proveedorToEdit.sigla : (proveedorToEdit.sigla?.nombre || ""),
        giroId: proveedorToEdit.giro?.giroId?.toString() || "",
        activo: proveedorToEdit.activo ?? true,
      });
    } else {
      setFormData(initialForm);
    }
    setErrors({});
    setTouched({});
  }, [proveedorToEdit]);

  const validateField = (name: string, value: any) => {
    let error = "";
    switch (name) {
      case "runProveedor": {
        const clean = value.replace(/[^0-9Kk]/g, "");
        if (!clean) error = "El RUN es requerido";
        else if (clean.length < 8) error = "RUN demasiado corto";
        else if (!validateRUN(clean)) error = "RUN inválido (DV incorrecto)";
        break;
      }
      case "telefonoProveedor": {
        const clean = value.replace(/\D/g, "");
        if (clean.length > 0 && (clean.length !== 9 || !clean.startsWith("9"))) {
          error = "Debe iniciar con 9 y tener exactamente 9 dígitos";
        }
        break;
      }
      case "emailProveedor": {
        // Usamos la validación centralizada o un fallback estándar de la app
        const valEmail = validateEmail || ((v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v));
        if (value && !valEmail(value)) {
          error = "Formato de correo electrónico inválido";
        }
        break;
      }
      case "razonSocialProveedor": {
        if (!value.trim()) error = "La razón social es requerida";
        else if (value.trim().length < 3) error = "Debe tener al menos 3 caracteres";
        break;
      }
      case "sigla": {
        if (!value.trim()) error = "La sigla es requerida";
        break;
      }
      case "giroId": {
        if (!value) error = "El giro es requerido";
        break;
      }
    }
    return error;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    let finalValue = value;

    if (name === "runProveedor") {
      const clean = value.replace(/[^0-9Kk]/g, "");
      finalValue = formatRUN(clean);
    } else if (name === "telefonoProveedor") {
      const clean = value.replace(/\D/g, "").slice(0, 9);
      finalValue = clean
        .replace(/(\d{1})(\d{4})(\d{4})/, "$1 $2 $3")
        .replace(/(\d{1})(\d{4})/, "$1 $2")
        .trim();
    } else if (name === "emailProveedor") {
      finalValue = value.toLowerCase();
    }

    setFormData((prev) => ({ ...prev, [name]: finalValue }));

    if (touched[name]) {
      const error = validateField(name, finalValue);
      setErrors((prev) => ({ ...prev, [name]: error }));
    }
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));
    const error = validateField(name, value);
    setErrors((prev) => ({ ...prev, [name]: error }));
  };

  const isFormValid = useMemo(() => {
    const mandatoryFields = ["razonSocialProveedor", "runProveedor", "sigla", "giroId"];
    const hasErrors = Object.values(errors).some((err) => !!err);
    if (hasErrors) return false;

    return (
      mandatoryFields.every((field) => formData[field] && validateField(field, formData[field]) === "") &&
      (!formData.emailProveedor || validateField("emailProveedor", formData.emailProveedor) === "") &&
      (!formData.telefonoProveedor || validateField("telefonoProveedor", formData.telefonoProveedor) === "")
    );
  }, [errors, formData]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const currentErrors: Record<string, string> = {};
    Object.keys(formData).forEach((key) => {
      const error = validateField(key, formData[key]);
      if (error) currentErrors[key] = error;
    });

    if (Object.keys(currentErrors).length > 0) {
      setErrors(currentErrors);
      const allTouched: Record<string, boolean> = {};
      Object.keys(formData).forEach((key) => { allTouched[key] = true; });
      setTouched(allTouched);
      toast.error("Por favor, revisa los campos marcados en rojo");
      return;
    }

    const payload = {
      razonSocialProveedor: formData.razonSocialProveedor.trim(),
      runProveedor: formData.runProveedor.replace(/\./g, ""),
      direccionProveedor: formData.direccionProveedor.trim(),
      telefonoProveedor: formData.telefonoProveedor
        ? `+56${formData.telefonoProveedor.replace(/\s/g, "")}`
        : "",
      emailProveedor: formData.emailProveedor.trim(),
      contactoProveedor: formData.contactoProveedor.trim(),
      tipoProveedor: formData.tipoProveedor.trim(),
      activo: formData.activo,
      sigla: formData.sigla.trim(),
      giro: formData.giroId ? { giroId: Number(formData.giroId) } : null,
    };

    onSave(payload);
    toast.success("Datos procesados correctamente");
  };

  const FieldError = ({ name }: { name: string }) =>
    errors[name] && touched[name] ? (
      <div className="flex items-center gap-1.5 mt-1.5 text-xs font-medium text-destructive animate-in fade-in slide-in-from-top-1 duration-200">
        <AlertCircle className="w-3.5 h-3.5 shrink-0" />
        <span>{errors[name]}</span>
      </div>
    ) : null;

  const inputClass = (err: any) =>
    `w-full pl-10 pr-4 py-2.5 bg-background border rounded-xl text-sm transition-all focus:outline-none focus:ring-2 focus:ring-brand-indigo/10 ${
      err
        ? "border-destructive focus:border-destructive"
        : "border-border hover:border-muted-foreground/30 focus:border-brand-indigo"
    }`;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
      <div className="bg-card w-full max-w-2xl rounded-2xl shadow-2xl border border-border flex flex-col max-h-[90vh]">
        {/* Header */}
        <div className="flex items-center justify-between p-5 border-b border-border">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-brand-indigo/10 rounded-xl text-brand-indigo">
              <Briefcase size={20} />
            </div>
            <div>
              <h2 className="text-lg font-bold text-foreground leading-tight">
                {proveedorToEdit ? "Editar Proveedor" : "Nuevo Proveedor"}
              </h2>
              <p className="text-[11px] text-muted-foreground font-semibold uppercase tracking-wider mt-0.5">
                Gestión de Registro de Proveedores
              </p>
            </div>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-all"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Scrollable Form Body */}
        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-6 space-y-6">
          {/* SECCIÓN 1: Identificación */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-[11px] font-bold text-muted-foreground uppercase tracking-wider">Identificación</span>
              <div className="h-px flex-1 bg-border/60"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-foreground mb-1.5">Razón Social *</label>
                <div className="relative">
                  <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="razonSocialProveedor"
                    value={formData.razonSocialProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(errors.razonSocialProveedor && touched.razonSocialProveedor)}
                    placeholder="Ej: Transportes Integral SPA"
                  />
                </div>
                <FieldError name="razonSocialProveedor" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">RUN *</label>
                <div className="relative">
                  <span className="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs font-bold text-muted-foreground select-none">RUN</span>
                  <input
                    name="runProveedor"
                    value={formData.runProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    disabled={!!proveedorToEdit}
                    className={`${inputClass(errors.runProveedor && touched.runProveedor)} pl-14 disabled:opacity-60 disabled:bg-muted disabled:cursor-not-allowed`}
                    placeholder="12.345.678-9"
                  />
                </div>
                <FieldError name="runProveedor" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">Sigla *</label>
                <div className="relative">
                  <Tag className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="sigla"
                    value={formData.sigla}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(errors.sigla && touched.sigla)}
                    placeholder="Ej: LTDA."
                  />
                </div>
                <FieldError name="sigla" />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-foreground mb-1.5">Giro *</label>
                <div className="relative">
                  <Briefcase className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground pointer-events-none" />
                  <select
                    name="giroId"
                    value={formData.giroId}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`${inputClass(errors.giroId && touched.giroId)} appearance-none cursor-pointer pr-10`}
                  >
                    <option value="">Seleccionar giro...</option>
                    {giros.map((g: any) => (
                      <option key={g.giroId} value={g.giroId}>
                        {g.descripcionGiro}
                      </option>
                    ))}
                  </select>
                  <div className="absolute right-3.5 top-1/2 -translate-y-1/2 pointer-events-none border-l-4 border-r-4 border-t-4 border-l-transparent border-r-transparent border-t-muted-foreground w-0 h-0" />
                </div>
                <FieldError name="giroId" />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-xs font-semibold text-foreground mb-1.5">Tipo de Proveedor</label>
                <div className="relative">
                  <Tag className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="tipoProveedor"
                    value={formData.tipoProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(false)}
                    placeholder="Ej: Logística, Insumos..."
                  />
                </div>
              </div>
            </div>
          </div>

          {/* SECCIÓN 2: Contacto */}
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <span className="text-[11px] font-bold text-muted-foreground uppercase tracking-wider">Contacto</span>
              <div className="h-px flex-1 bg-border/60"></div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="emailProveedor"
                    type="email"
                    value={formData.emailProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(errors.emailProveedor && touched.emailProveedor)}
                    placeholder="mail@ejemplo.cl"
                  />
                </div>
                <FieldError name="emailProveedor" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">Teléfono Móvil</label>
                <div className="relative">
                  <Phone className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <span className="absolute left-10 top-1/2 -translate-y-1/2 text-sm font-medium text-muted-foreground select-none">+56</span>
                  <input
                    name="telefonoProveedor"
                    value={formData.telefonoProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={`${inputClass(errors.telefonoProveedor && touched.telefonoProveedor)} pl-20`}
                    placeholder="9 1234 5678"
                  />
                </div>
                <FieldError name="telefonoProveedor" />
              </div>

              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">Persona de Contacto</label>
                <div className="relative">
                  <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="contactoProveedor"
                    value={formData.contactoProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(false)}
                    placeholder="Nombre del ejecutivo"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-foreground mb-1.5">Dirección</label>
                <div className="relative">
                  <MapPin className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <input
                    name="direccionProveedor"
                    value={formData.direccionProveedor}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    className={inputClass(false)}
                    placeholder="Ciudad, Calle #123"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* ESTADO */}
          <div className="flex items-center justify-between p-4 bg-muted/50 rounded-xl border border-border">
            <div className="flex items-center gap-3">
              <div className={`w-9 h-9 rounded-lg flex items-center justify-center transition-all ${formData.activo ? 'bg-emerald-500/10 text-emerald-600' : 'bg-muted text-muted-foreground'}`}>
                <CheckCircle className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-semibold text-foreground">Estado del Proveedor</h4>
                <p className="text-[11px] text-muted-foreground">Permitir transacciones y operaciones del proveedor</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer select-none">
              <input
                type="checkbox"
                name="activo"
                checked={formData.activo}
                onChange={(e) => setFormData(prev => ({ ...prev, activo: e.target.checked }))}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-input rounded-full peer peer-checked:bg-emerald-500 transition-all after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-card after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-full"></div>
            </label>
          </div>
        </form>

        {/* Footer Actions (Sticky) */}
        <div className="p-4 border-t border-border bg-muted/20 flex gap-3">
          <button
            type="button"
            onClick={onClose}
            className="w-1/3 py-2.5 border border-border bg-background hover:bg-muted text-foreground rounded-xl text-sm font-semibold transition-all"
          >
            Cancelar
          </button>
          <button
            type="button"
            onClick={handleSubmit}
            disabled={!isFormValid}
            className={`flex-1 py-2.5 bg-brand-indigo hover:opacity-95 text-white rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all shadow-sm ${
              !isFormValid ? "opacity-40 cursor-not-allowed" : "hover:shadow-md active:scale-[0.99]"
            }`}
          >
            <Save className="w-4 h-4" />
            {proveedorToEdit ? "Actualizar Proveedor" : "Guardar Proveedor"}
          </button>
        </div>
      </div>
    </div>
  );
}