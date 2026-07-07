"use client";

import { useState } from "react";
import { Truck, Search, Plus, Edit2, Trash2, UserCheck, UserX } from "lucide-react";
import { Toaster } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";

import { useProveedores } from "../../hooks/useProveedores";
import ProveedorModal from "../../components/shared/ProveedorModal";
import { normalizeProveedor } from "../../utils/helpers/normalizeProveedor";
import { formatRUN } from "../../utils/validations";

export default function ProveedoresView() {
    const {
        proveedores,
        loading,
        createProveedor,
        updateProveedor,
        deleteProveedor,
        toggleProveedor
    } = useProveedores();

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [proveedorToEdit, setProveedorToEdit] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [filterStatus, setFilterStatus] = useState("Todos");

    // Filtrado de proveedores
    const filteredProveedores = proveedores.filter((p) => {
        const search = searchTerm.toLowerCase();

        const matchesSearch =
            p.razonSocialProveedor?.toLowerCase()?.includes(search) ||
            p.runProveedor?.toLowerCase()?.includes(search) ||
            p.tipoProveedor?.toLowerCase()?.includes(search) ||
            p.sigla?.descripcionSigla?.toLowerCase()?.includes(search) ||
            p.sigla?.siglaAbreviatura?.toLowerCase()?.includes(search) ||
            p.giro?.descripcionGiro?.toLowerCase()?.includes(search);

        const isActive = p.activo;
        const status = isActive ? "Activo" : "Suspendido";
        const matchesFilter = filterStatus === "Todos" || filterStatus === status;

        return matchesSearch && matchesFilter;
    });

    const handleCreate = () => {
        setProveedorToEdit(null);
        setIsModalOpen(true);
    };

    const handleEdit = (proveedor) => {
        setProveedorToEdit(proveedor);
        setIsModalOpen(true);
    };

    const handleSaveProveedor = (proveedorData) => {
        const normalizedData = normalizeProveedor(proveedorData);

        if (proveedorToEdit) {
            updateProveedor({
                ...normalizedData,
                proveedorId: proveedorToEdit.proveedorId,
                // Preserve original nested arrays so buildPayload can reuse existing DB IDs
                contactos: proveedorToEdit.contactos,
                direccion: proveedorToEdit.direccion,
            });
        } else {
            createProveedor(normalizedData);
        }

        setProveedorToEdit(null);
        setIsModalOpen(false);
    };

    const handleDelete = (id) => {
        confirmDelete("¿Está seguro de eliminar este proveedor?", () => deleteProveedor(id));
    };

    const handleCloseModal = () => {
        setProveedorToEdit(null);
        setIsModalOpen(false);
    };

    return (
        <div className="min-h-screen bg-muted p-6 sm:p-8 font-sans antialiased text-foreground">
            <Toaster position="top-right" richColors />

            {/* --- HEADER PRINCIPAL --- */}
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8 bg-card p-6 rounded-2xl shadow-sm border border-border">
                <div className="flex items-center gap-4">
                    <div className="p-3 bg-brand-violet/10 text-brand-violet rounded-xl">
                        <Truck className="w-6 h-6" />
                    </div>
                    <div>
                        <h1 className="text-xl font-bold text-foreground">Gestión de Proveedores</h1>
                        <p className="text-sm text-muted-foreground">Directorio Central y Control de Cuentas</p>
                    </div>
                </div>

                <button
                    onClick={handleCreate}
                    className="inline-flex items-center justify-center gap-2 bg-brand-violet hover:bg-brand-violet/90 text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-all shadow-sm hover:shadow-md"
                >
                    <Plus className="w-4 h-4" />
                    Registrar Nuevo Proveedor
                </button>
            </div>

            {/* --- SECCIÓN DE FILTROS --- */}
            <div className="bg-card rounded-2xl shadow-sm border border-border overflow-hidden">
                <div className="p-5 border-b border-border flex flex-col sm:flex-row gap-4 items-center justify-between bg-muted/50">
                    {/* Barra de búsqueda */}
                    <div className="relative w-full sm:max-w-md">
                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                        <input
                            type="text"
                            placeholder="Buscar por razón social, RUN, sigla o giro..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="w-full pl-10 pr-4 py-2 bg-card border border-border rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-brand-violet/20 focus:border-brand-violet transition-all"
                        />
                    </div>

                    {/* Selector de Estado */}
                    <div className="flex items-center gap-2 w-full sm:w-auto justify-end">
                        <span className="text-xs font-medium text-muted-foreground uppercase tracking-wider">Estado:</span>
                        <select
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)}
                            className="bg-card border border-border rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-violet/20 focus:border-brand-violet transition-all cursor-pointer"
                        >
                            <option value="Todos">Todos los estados</option>
                            <option value="Activo">Activos</option>
                            <option value="Suspendido">Suspendidos</option>
                        </select>
                    </div>
                </div>

                {/* --- TABLA DE PROVEEDORES --- */}
                <div className="overflow-x-auto">
                    {loading ? (
                        <div className="p-12 text-center text-muted-foreground text-sm animate-pulse">
                            Cargando proveedores...
                        </div>
                    ) : filteredProveedores.length === 0 ? (
                        <div className="p-12 text-center text-muted-foreground text-sm">
                            No se encontraron proveedores que coincidan con la búsqueda.
                        </div>
                    ) : (
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="border-b border-border bg-muted/50 text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                                    <th className="p-4 pl-6">Proveedor</th>
                                    <th className="p-4">RUN / Identificación</th>
                                    <th className="p-4">Sigla y Giro</th>
                                    <th className="p-4 text-center">Estado</th>
                                    <th className="p-4 pr-6 text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-border">
                                {filteredProveedores.map((p) => {
                                    const id = p.proveedorId;
                                    const isActive = p.activo;
                                    const name = p.razonSocialProveedor || "Proveedor sin razón social";
                                    const initial = name.charAt(0).toUpperCase();
                                    const email = p.emailProveedor || "Sin correo";
                                    const run = p.runProveedor ? formatRUN(p.runProveedor) : "Sin RUN";
                                    const siglaLabel =
                                        p.sigla?.siglaAbreviatura ||
                                        p.sigla?.descripcionSigla ||
                                        (typeof p.sigla === "string" ? p.sigla : "Sin sigla");
                                    const giroLabel = p.giro?.descripcionGiro || "Sin giro";

                                    return (
                                        <tr key={id} className="hover:bg-muted/70 transition-colors group">
                                            {/* Proveedor e Email */}
                                            <td className="p-4 pl-6">
                                                <div className="flex items-center gap-3">
                                                    <div className="w-10 h-10 rounded-full bg-muted text-brand-violet font-bold text-sm flex items-center justify-center border border-border">
                                                        {initial}
                                                    </div>
                                                    <div>
                                                        <div className="font-semibold text-foreground text-sm">{name}</div>
                                                        <div className="text-xs text-muted-foreground">{email}</div>
                                                    </div>
                                                </div>
                                            </td>

                                            {/* RUN */}
                                            <td className="p-4 text-sm text-muted-foreground font-medium">
                                                {run}
                                            </td>

                                            {/* Sigla y Giro */}
                                            <td className="p-4">
                                                <div className="text-sm font-medium text-foreground">{siglaLabel}</div>
                                                <div className="text-xs text-muted-foreground">{giroLabel}</div>
                                            </td>

                                            {/* Estado con Badge */}
                                            <td className="p-4 text-center">
                                                <button
                                                    onClick={() => toggleProveedor(p)}
                                                    title="Click para cambiar estado"
                                                    className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold transition-all border ${
                                                        isActive
                                                            ? "bg-success/10 border-success/20 text-success hover:bg-success/20"
                                                            : "bg-warning/10 border-warning/20 text-warning hover:bg-warning/20"
                                                    }`}
                                                >
                                                    {isActive ? (
                                                        <>
                                                            <UserCheck className="w-3.5 h-3.5" /> Activo
                                                        </>
                                                    ) : (
                                                        <>
                                                            <UserX className="w-3.5 h-3.5" /> Suspendido
                                                        </>
                                                    )}
                                                </button>
                                            </td>

                                            {/* Acciones */}
                                            <td className="p-4 pr-6 text-right">
                                                <div className="flex items-center justify-end gap-2 opacity-80 group-hover:opacity-100 transition-opacity">
                                                    <button
                                                        onClick={() => handleEdit(p)}
                                                        className="p-2 text-muted-foreground hover:text-brand-violet hover:bg-muted rounded-lg transition-colors"
                                                        title="Editar"
                                                    >
                                                        <Edit2 className="w-4 h-4" />
                                                    </button>
                                                    <button
                                                        onClick={() => handleDelete(id)}
                                                        className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                                                        title="Eliminar"
                                                    >
                                                        <Trash2 className="w-4 h-4" />
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>

            {isModalOpen && (
                <ProveedorModal
                    onClose={handleCloseModal}
                    onSave={handleSaveProveedor}
                    proveedorToEdit={proveedorToEdit}
                />
            )}
        </div>
    );
}