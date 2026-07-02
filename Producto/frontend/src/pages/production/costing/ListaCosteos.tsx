import React, { useState, useMemo } from 'react';
import PropTypes from 'prop-types';
import {
    DollarSign,
    PieChart,
    TrendingUp,
    Search,
    Filter,
    X,
    Layers,
    Calculator,
    ClipboardList,
    CheckCircle2,
    XCircle,
    RotateCcw,
    AlertTriangle
} from 'lucide-react';
import EstadoCosteo from '../../../remote/DTO/EstadoCosteo';
import { Button } from '../../../ui/button';
import { Input } from '../../../ui/input';

const ESTADO_BADGE = {
    BORRADOR:  { label: 'Borrador',  className: 'bg-muted text-muted-foreground ring-1 ring-border' },
    COSTEADO:  { label: 'Costeado',  className: 'bg-primary/10 text-primary ring-1 ring-primary/20' },
    APROBADO:  { label: 'Aprobado', className: 'bg-success/10 text-success ring-1 ring-success/20' },
    RECHAZADO: { label: 'Rechazado', className: 'bg-destructive/10 text-destructive ring-1 ring-destructive/20' },
    APROBADA:   { label: 'Aprobado', className: 'bg-success/10 text-success ring-1 ring-success/20' },
    'COSTEO REALIZADO': { label: 'Costeado', className: 'bg-primary/10 text-primary ring-1 ring-primary/20' },
    PENDIENTE:         { label: 'Pendiente', className: 'bg-warning/10 text-warning shadow-sm' },
};

/**
 * Retorna las clases y etiqueta correspondientes al estado del costeo.
 * @param {string} estado
 * @returns {object}
 */
function badgeFor(estado) {
    if (!estado) return { label: 'Pendiente', className: 'bg-warning/10 text-warning' };
    return ESTADO_BADGE[estado]
        ?? ESTADO_BADGE[estado.toUpperCase?.()]
        ?? { label: estado, className: 'bg-warning/10 text-warning' };
}

/**
 * Componente ListaCosteos
 * Presenta un listado de costeos con filtros avanzados, validaciones de negocio en cliente y manejo seguro de estados.
 */
export default function ListaCosteos({
    onOpenDashboard,
    onOpenCompare,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    recordsToDisplay = [],
    clientes = [],
    handleOpenForm,
    onAprobar,
    onRechazar,
    onReabrir
}) {
    const [rechazoRecord, setRechazoRecord] = useState(null);
    const [motivo, setMotivo] = useState('');
    const [errorUI, setErrorUI] = useState({ visible: false, message: '' });

    // Mapeo optimizado de clientes para evitar busquedas O(N) recurrentes en el renderizado.
    const clienteMap = useMemo(() => {
        const map = new Map();
        clientes.forEach(c => {
            const id = (c.clienteId || c.id)?.toString();
            if (id) map.set(id, c);
        });
        return map;
    }, [clientes]);

    /**
     * Muestra una advertencia temporal en la interfaz ante una accion invalida.
     * @param {string} message
     */
    const triggerWarning = (message) => {
        setErrorUI({ visible: true, message });
        setTimeout(() => {
            setErrorUI({ visible: false, message: '' });
        }, 5000);
    };

    /**
     * Valida y ejecuta la aprobacion de un costeo.
     * @param {object} record
     */
    const ejecutarAprobacion = (record) => {
        if (!record) return;

        // Validacion de consistencia: No se puede enviar informacion nula o vacia.
        if (!record.costoTotal || record.costoTotal <= 0) {
            triggerWarning(`Error: El costeo ID ${record.id || record.numero} no posee un costo total valido para ser aprobado.`);
            return;
        }

        if (record.costeoEstado !== EstadoCosteo.COSTEADO) {
            triggerWarning("Error de flujo: Solo los registros en estado COSTEADO pueden ser aprobados.");
            return;
        }

        onAprobar?.(record);
    };

    /**
     * Valida y procesa la confirmacion del rechazo exigiendo justificacion.
     */
    const confirmarRechazo = () => {
        if (!motivo.trim()) {
            triggerWarning("El motivo de rechazo es mandatorio para registrar la excepcion en el sistema.");
            return;
        }

        if (motivo.trim().length < 10) {
            triggerWarning("Descripcion insuficiente. Indique un motivo detallado (minimo 10 caracteres).");
            return;
        }

        onRechazar?.(rechazoRecord, motivo.trim());
        setRechazoRecord(null);
        setMotivo('');
    };

    /**
     * Valida y gestiona la reapertura del flujo del costeo a Borrador.
     * @param {object} record
     */
    const ejecutarReapertura = (record) => {
        if (!record) return;

        const estadosPermitidos = [EstadoCosteo.RECHAZADO, EstadoCosteo.COSTEADO];
        if (!estadosPermitidos.includes(record.costeoEstado)) {
            triggerWarning("Operacion denegada: El estado actual no permite la reapertura a borrador.");
            return;
        }

        onReabrir?.(record);
    };

    return (
        <div className="max-w-full p-4 space-y-6 animate-in fade-in duration-500">

            {/* Banner Global de Alertas y Errores de Validacion */}
            {errorUI.visible && (
                <div className="fixed top-4 right-4 z-50 max-w-md bg-destructive/10 border-l-4 border-destructive p-4 rounded-r-xl shadow-xl animate-in slide-in-from-top-4 duration-300">
                    <div className="flex items-start gap-3">
                        <AlertTriangle className="w-5 h-5 text-destructive shrink-0 mt-0.5" />
                        <div className="flex-1">
                            <h4 className="text-xs font-black text-destructive uppercase tracking-wider">Error de Validacion Interna</h4>
                            <p className="text-xs font-bold text-destructive/80 mt-1">{errorUI.message}</p>
                        </div>
                        <button onClick={() => setErrorUI({ visible: false, message: '' })} className="text-destructive/60 hover:text-destructive">
                            <X className="w-4 h-4" />
                        </button>
                    </div>
                </div>
            )}

            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <h1 className="text-4xl font-black text-foreground tracking-tight italic flex items-center">
                        <DollarSign className="w-10 h-10 mr-4 text-success" />
                        Costeos OP
                    </h1>
                    <p className="text-muted-foreground font-bold text-xs uppercase tracking-widest mt-2 ml-1">
                        Análisis de Costos y Márgenes Directos
                    </p>
                </div>
                <div className="flex flex-wrap gap-3">
                    <Button
                        variant="outline"
                        onClick={onOpenDashboard}
                        className="gap-3 px-5 py-3 h-auto text-success rounded-2xl text-[10px] font-black uppercase tracking-[0.15em] hover:bg-success hover:text-white hover:border-success hover:shadow-lg hover:shadow-success/20"
                    >
                        <PieChart className="w-4 h-4" />
                        Dashboard de Costeos
                    </Button>
                    <Button
                        variant="outline"
                        onClick={onOpenCompare}
                        className="gap-3 px-5 py-3 h-auto text-brand-indigo rounded-2xl text-[10px] font-black uppercase tracking-[0.15em] hover:bg-brand-indigo hover:text-white hover:border-brand-indigo hover:shadow-lg hover:shadow-brand-indigo/20"
                    >
                        <TrendingUp className="w-4 h-4" />
                        Comparativa de Precios
                    </Button>
                </div>
            </div>

            {/* Seccion de Filtros */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 bg-card p-4 rounded-[2rem] shadow-sm border border-border">
                <div className="md:col-span-2 relative">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
                    <Input
                        type="text"
                        placeholder="Buscar por cliente o ID de costeo..."
                        className="pl-11 pr-4 py-3 h-auto rounded-2xl text-sm font-bold focus-visible:ring-success/50"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="relative">
                    <Filter className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground w-3 h-3 pointer-events-none" />
                    <select
                        className="w-full pl-10 pr-10 py-3 bg-muted border border-transparent rounded-2xl text-xs font-black uppercase tracking-widest focus:ring-2 focus:ring-success/50 focus:bg-card transition-all outline-none appearance-none cursor-pointer text-foreground shadow-sm"
                        style={{
                            backgroundImage: `url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%236b7280'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2.5' d='M19 9l-7 7-7-7'/%3E%3C/svg%3E")`,
                            backgroundPosition: 'right 1rem center',
                            backgroundSize: '1rem',
                            backgroundRepeat: 'no-repeat'
                        }}
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="Todos">Todos los Estados</option>
                        <option value="BORRADOR">Borrador</option>
                        <option value="COSTEADO">Costeado</option>
                        <option value="APROBADO">Aprobado</option>
                        <option value="RECHAZADO">Rechazado</option>
                    </select>
                </div>

                <Button
                    variant="default"
                    onClick={() => {
                        setSearchTerm('');
                        setStatusFilter('Todos');
                    }}
                    className="gap-2 px-4 py-3 h-auto bg-foreground text-background rounded-2xl text-xs font-black uppercase tracking-widest hover:bg-foreground/90"
                >
                    <X className="w-3 h-3" />
                    Limpiar Filtros
                </Button>
            </div>

            {/* Grid Principal de Tarjetas */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {recordsToDisplay.map((record) => {
                    const recordClienteId = record.clienteId?.toString();
                    const cliente = clienteMap.get(recordClienteId);
                    const displayId = record.numero || record.id;
                    const estadoCosteo = record.costeoEstado;

                    // Definicion explicita de politicas y restricciones de transicion de estados
                    const puedeAprobar = estadoCosteo === EstadoCosteo.COSTEADO && (record.costoTotal > 0);
                    const puedeRechazar = estadoCosteo === EstadoCosteo.BORRADOR || estadoCosteo === EstadoCosteo.COSTEADO;
                    const puedeReabrir = estadoCosteo === EstadoCosteo.RECHAZADO || estadoCosteo === EstadoCosteo.COSTEADO;

                    // Validacion interna: Detecta inconsistencia fisica en la data del registro
                    const tieneInconsistencia = !record.cantidad || record.cantidad <= 0;

                    return (
                        <div
                            key={displayId}
                            onClick={() => handleOpenForm(record)}
                            className={`group bg-card p-6 rounded-[2.5rem] border-2 transition-all cursor-pointer relative overflow-hidden flex flex-col ${
                                tieneInconsistencia
                                ? 'border-warning/30 bg-warning/5 shadow-sm'
                                : 'border-border hover:border-success hover:shadow-2xl hover:shadow-success/10'
                            }`}
                        >
                            {/* Alerta de Registro Incompleto / Inconsistente */}
                            {tieneInconsistencia && (
                                <div className="mb-3 px-3 py-1.5 bg-warning/10 text-warning rounded-xl flex items-center gap-2 text-[9px] font-black uppercase tracking-wider">
                                    <AlertTriangle className="w-3 h-3 text-warning" />
                                    Cantidad faltante o invalida en SCOS
                                </div>
                            )}

                            <div className="flex justify-between items-start mb-4">
                                <div className="flex-1">
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">ID: {displayId}</span>
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest ${badgeFor(estadoCosteo ?? record.estado).className}`}>
                                            {badgeFor(estadoCosteo ?? record.estado).label}
                                        </span>
                                        {record.costeoVersion != null && (
                                            <span className="px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest bg-brand-indigo/10 text-brand-indigo">v{record.costeoVersion}</span>
                                        )}
                                    </div>
                                    <h3 className="text-md font-black text-foreground group-hover:text-success transition-colors uppercase leading-tight">
                                        {record.clienteNombre || cliente?.nombreCliente || cliente?.nombre || 'Cliente SCOS'}
                                    </h3>
                                </div>
                            </div>

                            <div className="space-y-3 mb-4">
                                <div className="p-4 bg-muted rounded-2xl flex flex-col justify-center">
                                    <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1">Costo Total Costeo OP</p>
                                    <p className={`text-xl font-black tracking-tight ${record.costoTotal <= 0 ? 'text-destructive' : 'text-foreground'}`}>
                                        ${(record.costoTotal || 0).toLocaleString('es-CL')}
                                    </p>
                                </div>
                                <div className="flex items-center gap-2 px-1">
                                    <Layers className="w-3 h-3 text-muted-foreground" />
                                    <span className="text-[10px] font-bold text-muted-foreground uppercase italic truncate">
                                        {record.articuloDescripcion || 'Sin descripcion de articulo'}
                                    </span>
                                </div>
                            </div>

                            {/* Informacion de Cantidades e ingreso al Formulario */}
                            <div className="flex items-center justify-between mt-auto pt-4 border-t border-border">
                                <div className="flex flex-col">
                                    <p className="text-[8px] font-black text-muted-foreground uppercase tracking-[0.2em] mb-1">Cant. Requerida</p>
                                    <p className={`text-xs font-black italic ${tieneInconsistencia ? 'text-destructive' : 'text-primary'}`}>
                                        {record.cantidad || 0} und
                                    </p>
                                </div>
                                <Button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleOpenForm(record);
                                    }}
                                    className="px-4 py-2 h-auto bg-brand-indigo text-white text-[9px] font-black rounded-xl uppercase tracking-widest hover:bg-brand-indigo/90 flex items-center shadow-lg shadow-brand-indigo/20"
                                >
                                    <Calculator className="w-3 h-3 mr-2" />
                                    {estadoCosteo === EstadoCosteo.APROBADO
                                        ? 'Ver Costeo'
                                        : estadoCosteo === EstadoCosteo.COSTEADO
                                        ? 'Revisar Costos'
                                        : 'Añadir Costos'}
                                </Button>
                            </div>

                            {/* Bloque de Acciones Transaccionales Organizadas */}
                            <div className="grid grid-cols-2 gap-2 mt-4 pt-3 border-t border-border">
                                <button
                                    onClick={(e) => { e.stopPropagation(); ejecutarAprobacion(record); }}
                                    disabled={!puedeAprobar}
                                    title={puedeAprobar ? 'Aprobar transaccion' : 'Requiere estar en estado COSTEADO y con montos validos'}
                                    className={`px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all ${
                                        puedeAprobar
                                        ? 'bg-success text-white hover:bg-success/90 active:scale-95 shadow-md shadow-success/20'
                                        : 'bg-muted text-muted-foreground cursor-not-allowed'
                                    }`}
                                >
                                    <CheckCircle2 className="w-3 h-3" />
                                    Aprobar
                                </button>

                                {estadoCosteo === EstadoCosteo.RECHAZADO ? (
                                    <button
                                        onClick={(e) => { e.stopPropagation(); ejecutarReapertura(record); }}
                                        className="px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all bg-warning text-white hover:bg-warning/90 active:scale-95 shadow-md shadow-warning/20"
                                    >
                                        <RotateCcw className="w-3 h-3" />
                                        Reabrir
                                    </button>
                                ) : (
                                    <button
                                        onClick={(e) => { e.stopPropagation(); setRechazoRecord(record); setMotivo(''); }}
                                        disabled={!puedeRechazar}
                                        title={puedeRechazar ? 'Rechazar transaccion' : 'Solo valido en estados BORRADOR o COSTEADO'}
                                        className={`px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all ${
                                            puedeRechazar
                                            ? 'bg-destructive text-white hover:bg-destructive/90 active:scale-95 shadow-md shadow-destructive/20'
                                            : 'bg-muted text-muted-foreground cursor-not-allowed'
                                        }`}
                                    >
                                        <XCircle className="w-3 h-3" />
                                        Rechazar
                                    </button>
                                )}
                            </div>

                            {/* Accion Secundaria de Reapertura en Flujo Activo */}
                            {puedeReabrir && estadoCosteo !== EstadoCosteo.RECHAZADO && (
                                <button
                                    onClick={(e) => { e.stopPropagation(); ejecutarReapertura(record); }}
                                    className="w-full mt-2 px-3 py-1.5 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all bg-muted text-muted-foreground hover:bg-warning/10 hover:text-warning active:scale-95"
                                >
                                    <RotateCcw className="w-3 h-3" />
                                    Revertir a Borrador
                                </button>
                            )}
                        </div>
                    );
                })}

                {recordsToDisplay.length === 0 && (
                    <div className="col-span-full py-20 text-center">
                        <ClipboardList className="w-16 h-16 text-muted-foreground/40 mx-auto mb-4" />
                        <p className="text-xs font-black text-muted-foreground uppercase tracking-widest italic">
                            No se encontraron solicitudes pendientes de costeo
                        </p>
                    </div>
                )}
            </div>

            {/* Modal de Rechazo Exigible */}
            {rechazoRecord && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-foreground/60 backdrop-blur-sm p-4" onClick={() => setRechazoRecord(null)}>
                    <div className="bg-card w-full max-w-md rounded-[2rem] p-6 shadow-2xl animate-in zoom-in-95 duration-200" onClick={(e) => e.stopPropagation()}>
                        <div className="flex items-center gap-3 mb-4">
                            <div className="w-10 h-10 rounded-2xl bg-destructive/10 flex items-center justify-center">
                                <XCircle className="w-5 h-5 text-destructive" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-foreground uppercase tracking-tight">Confirmar Rechazo Obligatorio</h3>
                                <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest">
                                    ID Registro: {rechazoRecord.numero || rechazoRecord.id}
                                </p>
                            </div>
                        </div>

                        <div className="space-y-1">
                            <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest block">
                                Motivo del rechazo (Minimo 10 caracteres) *
                            </label>
                            <textarea
                                value={motivo}
                                onChange={(e) => setMotivo(e.target.value)}
                                rows={3}
                                autoFocus
                                placeholder="Escriba de forma clara y concisa el error o variacion por la cual rechaza este costeo para su posterior correccion..."
                                className="w-full p-3 bg-muted border-2 border-transparent rounded-2xl text-sm font-bold focus:bg-card focus:border-destructive/50 focus:ring-0 outline-none resize-none transition-all"
                            />
                            <div className="flex justify-between items-center text-[10px] font-bold text-muted-foreground px-1">
                                <span>Caracteres introducidos: {motivo.length}</span>
                                {motivo.trim().length > 0 && motivo.trim().length < 10 && (
                                    <span className="text-destructive font-black uppercase">Faltan {10 - motivo.trim().length} caracteres</span>
                                )}
                            </div>
                        </div>

                        <div className="flex items-center justify-end gap-2 mt-5">
                            <Button
                                variant="ghost"
                                onClick={() => setRechazoRecord(null)}
                                className="px-4 py-2 h-auto text-[10px] font-black uppercase tracking-widest text-muted-foreground rounded-xl"
                            >
                                Cancelar
                            </Button>
                            <Button
                                variant="destructive"
                                onClick={confirmarRechazo}
                                disabled={motivo.trim().length < 10}
                                className="px-5 py-2 h-auto text-[10px] font-black uppercase tracking-widest rounded-xl shadow-lg shadow-destructive/20"
                            >
                                Registrar Rechazo
                            </Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

ListaCosteos.propTypes = {
    onOpenDashboard: PropTypes.func.isRequired,
    onOpenCompare: PropTypes.func.isRequired,
    searchTerm: PropTypes.string.isRequired,
    setSearchTerm: PropTypes.func.isRequired,
    statusFilter: PropTypes.string.isRequired,
    setStatusFilter: PropTypes.func.isRequired,
    recordsToDisplay: PropTypes.array,
    clientes: PropTypes.array,
    handleOpenForm: PropTypes.func.isRequired,
    onAprobar: PropTypes.func,
    onRechazar: PropTypes.func,
    onReabrir: PropTypes.func
};
