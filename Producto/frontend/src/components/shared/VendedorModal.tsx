import React, { useState, useEffect, useMemo } from "react";
import { X, UserCheck, Save, AlertCircle } from "lucide-react";
import { useColaboradores } from "../../hooks/useColaboradores";

const VendedorModal = ({ isOpen, onClose, onSave, vendedorToEdit }) => {
  const { colaboradores, loading: loadingColaboradores } = useColaboradores();

  const initialForm = {
    usuarioId: "",
    codigoVendedor: "",
  };

  const [formData, setFormData] = useState(initialForm);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  useEffect(() => {
    if (vendedorToEdit) {
      setFormData({
        usuarioId: vendedorToEdit.usuarioId ? String(vendedorToEdit.usuarioId) : "",
        codigoVendedor: vendedorToEdit.codigoVendedor || "",
      });
    } else {
      setFormData(initialForm);
    }
    setErrors({});
    setTouched({});
  }, [vendedorToEdit, isOpen]);

  if (!isOpen) return null;

  const validateField = (name: string, value: any) => {
    let error = "";
    switch (name) {
      case "usuarioId": {
        if (!value) error = "Debes seleccionar un colaborador";
        break;
      }
      case "codigoVendedor": {
        if (!value.trim()) error = "El código es requerido";
        else if (value.trim().length < 3) error = "El código debe tener al menos 3 caracteres";
        else if (/\s/.test(value)) error = "El código no debe contener espacios";
        break;
      }
    }
    return error;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    let finalValue = value;

    if (name === "codigoVendedor") {
      finalValue = value.toUpperCase().trim();
    }

    setFormData((prev) => ({ ...prev, [name]: finalValue }));

    if (touched[name]) {
      const error = validateField(name, finalValue);
      setErrors((prev) => ({ ...prev, [name]: error }));
    }
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));
    const error = validateField(name, value);
    setErrors((prev) => ({ ...prev, [name]: error }));
  };

  const isFormValid = useMemo(() => {
    const mandatoryFields = ["usuarioId", "codigoVendedor"];
    const hasErrors = Object.values(errors).some((err) => !!err);
    if (hasErrors) return false;

    return mandatoryFields.every((field) => {
      const value = formData[field];
      return value && validateField(field, value) === "";
    });
  }, [errors, formData]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const currentErrors: Record<string, string> = {};
    Object.keys(formData).forEach((key) => {
      const error = validateField(key, formData[key]);
      if (error) currentErrors[key] = error;
    });

    if (Object.keys(currentErrors).length > 0) {
      setErrors(currentErrors);
      const allTouched: Record<string, boolean> = {};
      Object.keys(formData).forEach((key) => {
        allTouched[key] = true;
      });
      setTouched(allTouched);
      return;
    }

    onSave({
      usuarioId: Number(formData.usuarioId),
      codigoVendedor: formData.codigoVendedor.trim(),
    });
  };

  const FieldError = ({ name }) =>
    errors[name] && touched[name] ? (
      <div className="flex items-center gap-1.5 mt-1.5 text-xs font-medium text-destructive transition-all">
        <AlertCircle className="w-3.5 h-3.5 shrink-0" />
        <span>{errors[name]}</span>
      </div>
    ) : null;

  const inputClass = (err) =>
    `w-full px-4 py-2.5 bg-muted border rounded-xl text-sm outline-none focus:ring-2 focus:ring-brand-indigo/20 transition-all ${
      err
        ? "border-destructive focus:border-destructive"
        : "border-border focus:border-brand-indigo"
    }`;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 overlay-backdrop">
      <div className="bg-card w-full max-w-md rounded-2xl shadow-xl border border-border overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-border">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-brand-indigo/10 rounded-xl text-brand-indigo">
              <UserCheck size={20} />
            </div>
            <h2 className="text-lg font-bold text-foreground">
              {vendedorToEdit ? "Editar Vendedor" : "Registrar Nuevo Vendedor"}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-all"
          >
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Usuario */}
          <div>
            <label className="block text-xs font-semibold text-muted-foreground mb-1.5">
              Colaborador <span className="text-destructive">*</span>
            </label>
            <select
              name="usuarioId"
              value={formData.usuarioId}
              onChange={handleChange}
              onBlur={handleBlur}
              disabled={loadingColaboradores}
              className={`${inputClass(errors.usuarioId && touched.usuarioId)} disabled:opacity-60 disabled:cursor-not-allowed`}
            >
              <option value="">
                {loadingColaboradores ? "Cargando colaboradores..." : "— Seleccionar colaborador —"}
              </option>
              {colaboradores.map((c) => (
                <option key={c.usuarioId} value={c.usuarioId}>
                  {[c.usuarioNombre, c.usuarioApellidos].filter(Boolean).join(" ")} — {c.usuarioRun || c.usuarioEmail}
                </option>
              ))}
            </select>
            <FieldError name="usuarioId" />
          </div>

          {/* Código Vendedor */}
          <div>
            <label className="block text-xs font-semibold text-muted-foreground mb-1.5">
              Código de Vendedor <span className="text-destructive">*</span>
            </label>
            <input
              type="text"
              name="codigoVendedor"
              value={formData.codigoVendedor}
              onChange={handleChange}
              onBlur={handleBlur}
              placeholder="Ej: VND-001, V-100..."
              className={inputClass(errors.codigoVendedor && touched.codigoVendedor)}
            />
            <FieldError name="codigoVendedor" />
          </div>

          <button
            type="submit"
            disabled={!isFormValid}
            className={`w-full py-3 bg-brand-indigo hover:opacity-90 text-white rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all shadow-sm ${
              !isFormValid ? "opacity-40 cursor-not-allowed" : "hover:shadow-md active:scale-[0.98]"
            }`}
          >
            <Save className="w-4 h-4" />
            {vendedorToEdit ? "Guardar Cambios" : "Registrar Vendedor"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default VendedorModal;
